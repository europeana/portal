package eu.europeana.portal2.web.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.ThumbnailService;
import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.controllers.statics.StaticPageController;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.SitemapPage;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.ContributorItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.SitemapEntry;
import eu.europeana.portal2.web.util.Beans;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.IngestionUtils;
import eu.europeana.portal2.web.util.Injector;
import eu.europeana.portal2.web.util.StaticCache;

@Controller
public class SitemapController {

	@Resource(name = "configurationService") private Configuration config;

	@Resource private StaticCache staticPageCache;

	@Resource private SearchService searchService;

	@Resource private ClickStreamLogger clickStreamLogger;

	@Resource private ThumbnailService thumbnailService;

	private static Logger log = Logger.getLogger(SitemapController.class.getCanonicalName());

	private static final int VIDEO_SITEMAP_VOLUME_SIZE = 25000;

	public static final int MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES = 6;

	private static final String europeanaUriPrefix = "http://www.europeana.eu/resolve/";
	private static final String europeanaUriInfix = "/resolve/";
	private static final String canonicalUrlPrefix = "http://www.europeana.eu/portal/";
	private static final String canonicalUrlInfix = "/portal/";
	private static final String SITEMAP_INDEX = "europeana-sitemap-index-hashed-";
	private static final String SITEMAP_HASHED = "europeana-sitemap-hashed-";
	private static final String SITEMAP_VIDEO = "europeana-video-sitemap-";
	private static final String XML = ".xml";
	private static final String LN = "\n";
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String SITEMAP_HEADER = "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";
	private static final String URLSET_HEADER = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns:geo=\"http://www.google.com/geo/schemas/sitemap/1.0\">";

	private static final String URL_S = "<url>";
	private static final String URL_E = "</url>";
	private static final String LOC_S = "<loc>";
	private static final String LOC_E = "</loc>";

	private static String portalUrl;
	private static String sitemapCacheName;
	private static File sitemapCacheDir;
	private static Date lastSolrUpdate;
	private volatile static Calendar lastCheck;
	private static Map<String, Boolean> inProcess = new ConcurrentHashMap<String, Boolean>();

	private static List<ContributorItem> contributorEntries;

	public static String solrQueryClauseToIncludeRecordsToPromoteInSitemaps() {
		return solrQueryClauseToIncludeRecordsToPromoteInSitemaps(MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES);
	}

	public static String solrQueryClauseToIncludeRecordsToPromoteInSitemaps(int min) {
		return "COMPLETENESS:[" + min + " TO *]";
	}

	public static String solrQueryClauseToIncludePlaces() {
		// latitude and longitude should be added together, so a second check
		// for longitude would just overload the
		// server
		return "pl_wgs84_pos_lat:*"; // enrichment_place_latitude:*
	}

	@RequestMapping("/europeana-sitemap-index-hashed.xml")
	public void handleSitemapIndexHashed(
			@RequestParam(value = "images", required = false, defaultValue = "false") String images,
			@RequestParam(value = "places", required = false, defaultValue = "false") String places,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		setSitemapCacheDir();

		String params = request.getQueryString() != null ? request.getQueryString().replaceAll("[^a-z0-9A-F]", "-") : "";
		File cacheFile = new File(sitemapCacheDir.getAbsolutePath(), SITEMAP_INDEX + params + XML);
		if ((solrOutdated() || !cacheFile.exists()) && !inProcess.containsKey(params)) {
			// generate file
			boolean success = false;
			FileWriter fstream = new FileWriter(cacheFile);
			BufferedWriter fout = new BufferedWriter(fstream);
			ServletOutputStream out = response.getOutputStream();
			StringBuilder fullXML = new StringBuilder();
			try {
				fullXML.append(XML_HEADER).append(LN);
				fullXML.append(SITEMAP_HEADER).append(LN);

				String prefix, line;
				String urlPath = "europeana-sitemap-hashed.xml?prefix=";
				String paramImages = "&images=";
				String paramPlaces = "&places=";
				for (String ab : makeHexLetterPairs()) {
					for (String cd : makeHexLetters()) {
						prefix = ab + cd;
						StringBuilder sb = new StringBuilder();
						sb.append(getPortalUrl()).append(urlPath).append(prefix);
						sb.append(paramImages).append(StringUtils.contains(images, "true"));
						sb.append(paramPlaces).append(StringUtils.contains(places, "true"));
						fullXML.append("<sitemap>").append(LOC_S).append(StringEscapeUtils.escapeXml(sb.toString())).append(LOC_E).append("</sitemap>").append(LN);
					}
				}
				fullXML.append("</sitemapindex>");

				out.print(fullXML.toString());
				out.flush();

				fout.write(fullXML.toString());
				fout.flush();
			} catch (Exception e) {
				success = false;
				log.severe("Exception during generation of europeana-sitemap-index-hashed.xml: " + e.getLocalizedMessage());
				log.severe(ExceptionUtils.getFullStackTrace(e));
			} finally {
				fout.close();
			}
			if (!success) {
				cacheFile.delete();
			}
		} else {
			if (inProcess.containsKey(params)) {
				do {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.severe(e.getLocalizedMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (inProcess.containsKey(params));
			}
			// read from file
			readCahedFile(response.getOutputStream(), cacheFile);
		}
	}

	String makeSitemapLocationUrl(String baseUrl, String provider, String images, int pageCounter) 
			throws UnsupportedEncodingException {
		return StringEscapeUtils.escapeXml(String.format(
			"%seuropeana-sitemap.xml?provider=%s&page=%d&images=%s",
			baseUrl, convertProviderToUrlParameter(provider), pageCounter, images));
	}

	@RequestMapping("/europeana-sitemap-hashed.xml")
	public void handleSitemap(
			@RequestParam(value = "prefix", required = true) String prefix,
			@RequestParam(value = "images", required = false, defaultValue = "false") String images,
			@RequestParam(value = "places", required = false, defaultValue = "false") String places,
			HttpServletRequest request, HttpServletResponse response)
					throws EuropeanaQueryException, IOException {
		setSitemapCacheDir();

		String params = request.getQueryString() != null ? request.getQueryString().replaceAll("[^a-z0-9A-F]", "-") : "";
		File cacheFile = new File(sitemapCacheDir.getAbsolutePath(), SITEMAP_HASHED + params + XML);
		
		if ((solrOutdated() || !cacheFile.exists()) && !inProcess.containsKey(params)) {
			// generate file
			log.info(String.format("Generating %s", cacheFile));

			inProcess.put(params, true);
			int success = 0;
			boolean isImageSitemap = StringUtils.contains(images, "true");
			boolean isPlaceSitemap = StringUtils.contains(places, "true");
			SearchPage model = new SearchPage();

			response.setCharacterEncoding("UTF-8");
			long t = new Date().getTime();
			StringBuilder fullXML = createSitemapHashedContent(prefix, model, isImageSitemap, isPlaceSitemap);
			log.info(String.format("Generated XML size: %s took: %s", fullXML.length(), (new Date().getTime() - t)));
			BufferedWriter fout = null;
			try {
				ServletOutputStream out = response.getOutputStream();
				log.info("Generated XML size is null? " + (fullXML == null));
				log.info("out is null? " + (out == null));
				out.print(fullXML.toString());
				out.flush();
				success = 1;
			} catch (Exception e) {
				success = 0;
				log.severe(String.format("Exception during outputing europeana-sitemap-hashed.xml: %s. File: %s", e.getLocalizedMessage(),
						cacheFile));
				log.severe("out is null? " + (response.getOutputStream() == null));
				log.severe(ExceptionUtils.getFullStackTrace(e));
			}

			try {
				FileWriter fstream = new FileWriter(cacheFile);
				fout = new BufferedWriter(fstream);
				fout.write(fullXML.toString());
				fout.flush();
				if (success == 1) {
					success = 2;
				}
			} catch (Exception e) {
				success = 0;
				log.severe(String.format("Exception during outputing europeana-sitemap-hashed.xml: %s. File: %s", e.getLocalizedMessage(),
						cacheFile));
				log.severe(ExceptionUtils.getFullStackTrace(e));
			} finally {
				if (fout != null) {
					fout.close();
				}
			}

			if (success != 2) {
				cacheFile.delete();
			}
			log.info(Thread.currentThread().getName() + " served by generation");
			inProcess.remove(params);
		} else {
			if (inProcess.containsKey(params) || !cacheFile.exists()) {
				do {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.severe(String.format("Exception during outputing europeana-sitemap-hashed.xml: %s. File: %s", e.getLocalizedMessage(),
								cacheFile));
						log.severe(e.getLocalizedMessage());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (inProcess.containsKey(params) || !cacheFile.exists());
			}
			// read from file
			readCahedFile(response.getOutputStream(), cacheFile);
		}
	}

	synchronized private StringBuilder createSitemapHashedContent(String prefix, SearchPage model, boolean isImageSitemap, boolean isPlaceSitemap) {
		StringBuilder fullXML = new StringBuilder();

		fullXML.append(XML_HEADER).append(LN);
		fullXML.append(URLSET_HEADER).append(LN);

		String queryString = solrQueryClauseToIncludeRecordsToPromoteInSitemaps(config.getMinCompletenessToPromoteInSitemaps());
		Query query = new Query("*:*")
						.addRefinement(queryString)
						.addRefinement("id3hash:" + prefix)
						.setPageSize(20000)
						.setParameter("fl", "europeana_id,COMPLETENESS,title,TYPE,provider_aggregation_edm_object");

		if (isPlaceSitemap) {
			String queryForPlaces = solrQueryClauseToIncludePlaces();
			if (!StringUtils.isBlank(queryForPlaces)) {
				query = query.addRefinement(queryForPlaces);
			}
		}

		log.info("queryString: " + query.toString());
		List<BriefBean> resultSet = null;
		try {
			long t = new Date().getTime();
			resultSet = searchService.sitemap(BriefBean.class, query).getResults();
			log.info("Query took: " + (new Date().getTime() - t));
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}

		if (resultSet != null) {
			for (BriefBean bean : resultSet) {
				BriefBeanDecorator doc = new BriefBeanDecorator(model, bean);
				String title = "";
				if (doc.getTitle() != null) {
					title = doc.getTitle()[0];
				}
				SitemapEntry entry = new SitemapEntry(
					getPortalUrl() + convertEuropeanaUriToCanonicalUrl(doc.getFullDocUrl(false), false), 
					doc.getThumbnail(), title, doc.getEuropeanaCompleteness());
				fullXML.append(URL_S).append(LN);

				String url = entry.getLoc();

				if (isPlaceSitemap) {
					url = StringUtils.replace(url, ".html", ".kml");
				}
				fullXML.append(LOC_S).append(url).append(LOC_E).append(LN);

				if (isImageSitemap && doc.getType() == DocType.IMAGE) {
					String image = "";
					try {
						image = URLEncoder.encode(entry.getImage(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fullXML.append("<image:image>").append(LN);
					fullXML.append("<image:loc>")
						.append(config.getImageCacheUrl() + "uri=" + image + "&amp;size=FULL_DOC")
						.append("</image:loc>").append(LN);
					fullXML.append("<image:title>").append(StringEscapeUtils.escapeXml(entry.getTitle())).append("</image:title>").append(LN);
					fullXML.append("</image:image>").append(LN);
				}

				if (isPlaceSitemap) {
					fullXML.append("<geo:geo><geo:format>kml</geo:format></geo:geo>").append(LN);
				}

				fullXML.append("<priority>").append(entry.getPriority()).append("</priority>").append(LN);
				fullXML.append(URL_E).append(LN);
			}
		}

		fullXML.append("</urlset>").append(LN);

		return fullXML;
	}

	// draft, not completed yet
	@RequestMapping("/europeana-video-sitemap.xml")
	public void handleSitemap(
			@RequestParam(value = "volume", required = true) String volumeString,
			HttpServletRequest request, HttpServletResponse response)
			throws EuropeanaQueryException, IOException {
		setSitemapCacheDir();

		String params = request.getQueryString() != null ? request.getQueryString().replaceAll("[^a-z0-9A-F]", "-") : "";
		File cacheFile = new File(sitemapCacheDir.getAbsolutePath(), SITEMAP_VIDEO + params + XML);
		if (solrOutdated() || !cacheFile.exists()) {

			int volume = -1;
			response.setCharacterEncoding("UTF-8");
			// PrintWriter out = new PrintWriter(response.getOutputStream(), true);
			ServletOutputStream out = response.getOutputStream();

			FileWriter fstream = new FileWriter(cacheFile);
			BufferedWriter fout = new BufferedWriter(fstream);

			SearchPage model = new SearchPage();

			try {
				volume = Integer.parseInt(volumeString);
				String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
				out.println(xml);
				fout.write(xml + "\n");

				xml = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">";
				out.println(xml);
				fout.write(xml + "\n");

				String queryString = "TYPE:VIDEO";
				Query query = new Query(queryString)
								.setParameter("rows", String.valueOf(VIDEO_SITEMAP_VOLUME_SIZE))
								.setStart(volume * VIDEO_SITEMAP_VOLUME_SIZE)
								.setParameter("fl", "europeana_id,COMPLETENESS,title,TYPE,provider_aggregation_edm_object");

				List<? extends BriefBean> resultSet = null;
				try {
					resultSet = searchService.sitemap(BriefBean.class, query).getResults();
				} catch (SolrTypeException e) {
					e.printStackTrace();
				}

				if (resultSet != null) {
					for (BriefBean bean : resultSet) {
						BriefBeanDecorator doc = new BriefBeanDecorator(model, bean);
						SitemapEntry entry = new SitemapEntry(
							getPortalUrl() + convertEuropeanaUriToCanonicalUrl(doc.getFullDocUrl(false), false),
							doc.getThumbnail(), 
							doc.getTitle()[0],
							doc.getEuropeanaCompleteness()
						);
						out.println(URL_S);
						out.println(LOC_S + entry.getLoc() + LOC_E);

						fout.write(URL_S + LN);
						fout.write(LOC_S + entry.getLoc() + LOC_E + LN);
						if (doc.getType() == DocType.VIDEO && isVideo(entry.getImage())) {
							String image = entry.getImage().replace("&", "&amp;");
							out.println("<video:video>");
							out.println("<video:thumbnail_loc>" + image + "</video:thumbnail_loc>");
							/*
							out.println("<video:thumbnail_loc>" + config.getImageCacheUrl() 
									+ "uri=" + URLEncoder.encode(entry.getImage(), "UTF-8")
									+ "&amp;size=FULL_DOC</video:thumbnail_loc>");
									*/
							out.println("<video:title>" + StringEscapeUtils.escapeXml(entry.getTitle()) + "</video:title>");
							out.println("<video:description>" + StringEscapeUtils.escapeXml(entry.getTitle()) + "</video:description>");
							out.println("<video:player_loc>" + URLEncoder.encode(entry.getImage(), "UTF-8") + "</video:player_loc>");
							out.println("</video:video>");

							fout.write("<video:video>\n");
							fout.write("<video:thumbnail_loc>" + image + "</video:thumbnail_loc>\n");
							/*
							fout.write("<video:thumbnail_loc>" + config.getImageCacheUrl() 
									+ "uri=" + URLEncoder.encode(entry.getImage(), "UTF-8")
									+ "&amp;size=FULL_DOC</video:thumbnail_loc>\n");
									*/
							fout.write("<video:title>" + StringEscapeUtils.escapeXml(entry.getTitle()) + "</video:title>\n");
							fout.write("<video:description>" + StringEscapeUtils.escapeXml(entry.getTitle()) + "</video:description>\n");
							fout.write("<video:player_loc>" + URLEncoder.encode(entry.getImage(), "UTF-8") + "</video:player_loc>\n");
							fout.write("</video:video>\n");
						}

						out.println(URL_E);
						fout.write(URL_E + LN);
					}
				}

				out.print("</urlset>");
				fout.write("</urlset>");
			} finally {
				out.flush();

				fout.flush();
				fout.close();
			}
		} else {
			readCahedFile(response.getOutputStream(), cacheFile);
		}
	}

	boolean isVideo(String url) {
		if (StringUtils.isEmpty(url)) {
			return false;
		}
		if (url.length() < 15) {
			return false;
		}
		return true;
	}

	/**
	 * Replace http://www.europeana.eu/resolve/xxx to http://www.europeana.eu/portal/xxx.html
	 * 
	 * @param europeanaUri
	 * @return
	 */
	public static String convertEuropeanaUriToCanonicalUrl(String europeanaUri) {
		return convertEuropeanaUriToCanonicalUrl(europeanaUri, true);
	}

	public static String convertEuropeanaUriToCanonicalUrl(String europeanaUri, boolean usePrefix) {
		if (usePrefix) {
			return europeanaUri.replace(europeanaUriPrefix, canonicalUrlPrefix) + ".html";
		}
		return europeanaUri.replace(europeanaUriInfix, canonicalUrlInfix);
	}

	static String convertCanonicalUrlToEuropeanaUri(String canonicalUrl) {
		return convertCanonicalUrlToEuropeanaUri(canonicalUrl, true);
	}

	static String convertCanonicalUrlToEuropeanaUri(String canonicalUrl, boolean usePrefix) {
		if (usePrefix) {
			return StringUtils.removeEnd(canonicalUrl.replace(canonicalUrlPrefix, europeanaUriPrefix), ".html");
		}
		return StringUtils.removeEnd(canonicalUrl.replace(canonicalUrlInfix, europeanaUriInfix), ".html");
	}

	@RequestMapping("/europeana-providers.html")
	public ModelAndView handleListOfContributors(
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws EuropeanaQueryException {
		setSitemapCacheDir();

		Injector injector = new Injector(request, response, locale);

		String portalServer = new StringBuilder(config.getPortalServer()).append(config.getPortalName()).toString();

		// sitemap index - collections overview
		if (solrOutdated() || contributorEntries == null) {
			contributorEntries = new ArrayList<ContributorItem>();
			List<Count> providers;
			try {
				providers = IngestionUtils.getCollectionsFromSolr(searchService, "PROVIDER", "*:*", null);
				for (Count provider : providers) {
					try {
						String query = StringEscapeUtils.escapeXml(String.format(
							"%s/search.html?query=*:*&qf=PROVIDER:%s",
							portalServer, convertProviderToUrlParameter(provider.getName())));
						ContributorItem contributorItem = new ContributorItem(query,
							provider.getName(), provider.getCount(), portalServer);

						List<ContributorItem.DataProviderItem> dataProviders = new ArrayList<ContributorItem.DataProviderItem>();

						List<Count> rawDataProviders = IngestionUtils.getCollectionsFromSolr(searchService, "DATA_PROVIDER",
							"*:*", new String[]{"PROVIDER:\"" + provider.getName() + "\""});
						for (Count dataProvider : rawDataProviders) {
							if (dataProvider.getCount() > 0) {
								dataProviders.add(contributorItem.new DataProviderItem(contributorItem, dataProvider.getName(), dataProvider.getCount()));
							}
						}

						contributorItem.setDataProviders(dataProviders);
						contributorEntries.add(contributorItem);
					} catch (UnsupportedEncodingException e) {
						log.warning(e.getMessage() + " on " + provider.getName());
					}
				}
			} catch (SolrTypeException e1) {
				e1.printStackTrace();
			}
		}

		SitemapPage<ContributorItem> model = new SitemapPage<ContributorItem>();
		model.setResults(contributorEntries);
		model.setPrefix("");
		model.setLeftContent(getStaticPagePart("/newcontent.html", StaticPageController.AFFIX_TEMPLATE_VAR_FOR_LEFT, locale));
		injector.injectProperties(model);

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.PROVIDERS);

		injector.postHandle(this, page);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.SITE_MAP_XML, page);

		return page;
	}

	@RequestMapping("/europeana-sitemap-static.xml")
	public ModelAndView handleSitemap(HttpServletRequest request, Locale locale) {

		List<SitemapEntry> records = new ArrayList<SitemapEntry>();
		records.add(new SitemapEntry("http://www.europeana.eu/portal/europeana-providers.html", null, null, 10));

		SitemapPage<SitemapEntry> model = new SitemapPage<SitemapEntry>();
		model.setResults(records);
		model.setShowImages(false);

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.SITEMAP);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.SITE_MAP_XML, page);
		return page;
	}

	public static String convertProviderToUrlParameter(String provider)
			throws UnsupportedEncodingException {
		String url = URLEncoder.encode(provider.replace("\"", "\\\"").replace("/", "\\/"), "UTF-8");
		return url;
	}

	private String getStaticPagePart(String fileName, String partName, Locale language) {

		if (!StringUtils.isEmpty(partName)) {
			fileName = StringUtils.replaceOnce(fileName, ".", "_" + partName + ".");
		}

		return staticPageCache.getPage(fileName, language);
	}

	public static List<String> makeHexLetterPairs() {
		List<String> hexLetterPairs = new ArrayList<String>();
		for (String dirA : SitemapPage.HEX) {
			for (String dirB : SitemapPage.HEX) {
				hexLetterPairs.add(dirA + dirB);
			}
		}
		return hexLetterPairs;
	}

	public static List<String> makeHexLetters() {
		return Arrays.asList(SitemapPage.HEX);
	}

	private String getPortalUrl() {
		if (portalUrl == null) {
			portalUrl = config.getCannonicalPortalServer() + config.getPortalName();
			if (!portalUrl.endsWith("/")) {
				portalUrl = portalUrl + "/";
			}
		}
		return portalUrl;
	}

	/**
	 * Set sitemap directory, and create it in file system if it does not exist
	 */
	private static void setSitemapCacheDir() {
		if (sitemapCacheDir == null) {
			sitemapCacheName = Beans.getConfig().getSitemapCache();
			if (!sitemapCacheName.endsWith("/")) {
				sitemapCacheName += "/";
			}
			sitemapCacheDir = new File(sitemapCacheName);
			if (!sitemapCacheDir.exists()) {
				sitemapCacheDir.mkdir();
			}
		}
	}

	/**
	 * Read a cached file, and copy its content to the output stream
	 * @param out
	 * @param cacheFile
	 */
	private void readCahedFile(ServletOutputStream out, File cacheFile) {
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(cacheFile));
			while ((sCurrentLine = br.readLine()) != null) {
				out.println(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Try to find out whether the solr index has been modified. If yes, it empties the cache
	 * @return
	 *   Boolean flag whether or not Solr modified
	 */
	synchronized private boolean solrOutdated() {
		// check it once a day
		Calendar timeout = DateUtils.toCalendar(DateUtils.addDays(new Date(), -1));
		if (lastCheck == null || lastCheck.before(timeout)) {
			log.info(String.format("%s requesting solr outdated (timeout: %s, lastCheck: %s)",
					Thread.currentThread().getName(),
					timeout.getTime().toString(),
					(lastCheck == null ? "null" : lastCheck.getTime().toString())));
			lastCheck = Calendar.getInstance();
			Date actualSolrUpdate = null;
			try {
				log.info("start");
				actualSolrUpdate = searchService.getLastSolrUpdate();
				log.info("ends");
			} catch (SolrServerException e) {
				log.severe("SolrServerException " + e.getLocalizedMessage());
			} catch (IOException e) {
				log.severe("IOException " + e.getLocalizedMessage());
			}

			if (actualSolrUpdate == null) {
				return true;
			}

			if (lastSolrUpdate == null) {
				lastSolrUpdate = actualSolrUpdate;
				return true;
			} else {
				if (!actualSolrUpdate.equals(lastSolrUpdate)) {
					deleteCachedFiles();
				}
				return !actualSolrUpdate.equals(lastSolrUpdate);
			}
		} else {
			return false;
		}
	}

	/**
	 * Deletes the cached files
	 */
	private void deleteCachedFiles() {
		setSitemapCacheDir();
		for (File file : sitemapCacheDir.listFiles()) {
			file.delete();
		}
	}
}
