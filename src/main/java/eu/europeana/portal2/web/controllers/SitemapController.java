package eu.europeana.portal2.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
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
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.IngestionUtils;
import eu.europeana.portal2.web.util.StaticCache;

@Controller
public class SitemapController {

	@Resource(name = "configurationService") private Configuration config;

	@Resource private StaticCache staticPageCache;

	@Resource private SearchService searchService;

	// @Resource private BeanQueryModelFactory beanQueryModelFactory;

	@Resource private ClickStreamLogger clickStreamLogger;

	@Resource private ThumbnailService thumbnailService;

	private Logger log = Logger.getLogger(this.getClass().getName());

	private static final int VIDEO_SITEMAP_VOLUME_SIZE = 25000;

	public static final int MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES = 6;

	public static String solrQueryClauseToIncludeRecordsToPromoteInSitemaps() {
		return solrQueryClauseToIncludeRecordsToPromoteInSitemaps(MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES);
	}

	public static String solrQueryClauseToIncludeRecordsToPromoteInSitemaps(int min) {
		return "COMPLETENESS:[" + min + " TO *] ";
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

		PrintWriter out = new PrintWriter(response.getOutputStream(), true);
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

		for (String ab : makeHexLetterPairs()) {
			for (String cd : makeHexLetters()) {
				String sitemap = ab + cd;
				StringBuilder sb = new StringBuilder();
				sb.append(config.getPortalServer()).append(config.getPortalName()).append("/");
				sb.append("europeana-sitemap-hashed.xml?prefix=");
				sb.append(sitemap);
				sb.append("&images=");
				sb.append(StringUtils.contains(images, "true"));
				sb.append("&places=");
				sb.append(StringUtils.contains(places, "true"));
				out.println("<sitemap>");
				out.println("  <loc>"
						+ StringEscapeUtils.escapeXml(sb.toString()) + "</loc>");
				out.println("</sitemap>");
			}
		}
		out.println("</sitemapindex>");
		out.flush();
		out.close();
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

		boolean isImageSitemap = StringUtils.contains(images, "true");
		boolean isPlaceSitemap = StringUtils.contains(places, "true");
		SearchPage model = new SearchPage();

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = new PrintWriter(response.getOutputStream(), true);
		try {
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns:geo=\"http://www.google.com/geo/schemas/sitemap/1.0\">");

			String queryString = solrQueryClauseToIncludeRecordsToPromoteInSitemaps(config.getMinCompletenessToPromoteInSitemaps());
			Query query = new Query("id3hash:" + prefix)
							.setRefinements(queryString)
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
				resultSet = searchService.sitemap(BriefBean.class, query).getResults();
			} catch (SolrTypeException e) {
				e.printStackTrace();
			}

			if (resultSet != null) {
				for (BriefBean bean : resultSet) {
					BriefBeanDecorator docId = new BriefBeanDecorator(model, bean);

					String title = "";
					if (docId.getTitle() != null) {
						title = docId.getTitle()[0];
					} else {
						log.info("no title");
					}
					SitemapEntry entry = new SitemapEntry(convertEuropeanaUriToCanonicalUrl(docId.getId()), docId.getThumbnail(), title, docId.getEuropeanaCompleteness());
					out.println("<url>");
					String url = entry.getLoc();
					if (isPlaceSitemap) {
						url = StringUtils.replace(url, ".html", ".kml");
					}
					out.println("  <loc>" + url + "</loc>");
					if (isImageSitemap && docId.getType() == DocType.IMAGE) {
						out.println("  <image:image>");
						out.println("    <image:loc>" + config.getImageCacheUrl() + "uri="
							+ URLEncoder.encode(entry.getImage(), "UTF-8")
							+ "&amp;size=FULL_DOC</image:loc>");
						out.println("    <image:title>"
							+ StringEscapeUtils.escapeXml(entry.getTitle())
							+ "</image:title>");
						out.println("  </image:image>");
					}
					if (isPlaceSitemap) {
						out.println("  <geo:geo>");
						out.println("      <geo:format>kml</geo:format> ");
						out.println("  </geo:geo>");
					}
					out.println("  <priority>" + entry.getPriority() + "</priority>");
					out.println("</url>");
				}
			}

			out.print("</urlset>");
			out.flush();
		} finally {
			out.close();
		}
	}

	// draft, not completed yet
	@RequestMapping("/europeana-video-sitemap.xml")
	public void handleSitemap(
			@RequestParam(value = "volume", required = true) String volumeString,
			HttpServletRequest request, HttpServletResponse response)
			throws EuropeanaQueryException, IOException {

		int volume = -1;
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = new PrintWriter(response.getOutputStream(), true);
		SearchPage model = new SearchPage();

		try {
			volume = Integer.parseInt(volumeString);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">");

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
					BriefBeanDecorator docId = new BriefBeanDecorator(model, bean);
					SitemapEntry entry = new SitemapEntry(
							convertEuropeanaUriToCanonicalUrl(docId.getId()),
							docId.getThumbnail(), docId.getTitle()[0],
							docId.getEuropeanaCompleteness());
					out.println("<url>");
					out.println("  <loc>" + entry.getLoc() + "</loc>");
					if (docId.getType() == DocType.VIDEO && isVideo(entry.getImage())) {
						out.println("  <video:video>");
						out.println("    <video:thumbnail_loc>" + config.getImageCacheUrl()
							+ "uri=" + URLEncoder.encode(entry.getImage(), "UTF-8")
							+ "&amp;size=FULL_DOC</video:thumbnail_loc>");
						out.println("    <video:title>"
							+ StringEscapeUtils.escapeXml(entry.getTitle())
							+ "</video:title>");
						out.println("    <video:description>"
							+ StringEscapeUtils.escapeXml(entry.getTitle())
							+ "</video:description>");
						out.println("    <video:player_loc>"
							+ URLEncoder.encode(entry.getImage(), "UTF-8")
							+ "</video:player_loc>");
						out.println("  </video:video>");
					}
					out.println("</url>");
				}
			}

			out.print("</urlset>");
			out.flush();
		} finally {
			out.close();
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
		final String europeanaUriPrefix = "http://www.europeana.eu/resolve/";
		final String canonicalUrlPrefix = "http://www.europeana.eu/portal/";
		return europeanaUri.replace(europeanaUriPrefix, canonicalUrlPrefix) + ".html";
	}

	static String convertCanonicalUrlToEuropeanaUri(String canonicalUrl) {
		final String europeanaUriPrefix = "http://www.europeana.eu/resolve/";
		final String canonicalUrlPrefix = "http://www.europeana.eu/portal/";
		return StringUtils.removeEnd(canonicalUrl.replace(canonicalUrlPrefix, europeanaUriPrefix), ".html");
	}

	@RequestMapping("/europeana-providers.html")
	public ModelAndView handleListOfContributors(HttpServletRequest request,
			Locale locale) throws EuropeanaQueryException {

		String portalServer = new StringBuilder(config.getPortalServer())
									.append(config.getPortalName()).toString();

		// sitemap index - collections overview
		List<ContributorItem> entries = new ArrayList<ContributorItem>();
		List<Count> providers;
		try {
			providers = IngestionUtils.getCollectionsFromSolr(searchService, "PROVIDER", "*:*");
			for (Count provider : providers) {
				try {
					String query = StringEscapeUtils.escapeXml(String.format(
							"%s/search.html?query=*:*&qf=PROVIDER:%s",
							portalServer, convertProviderToUrlParameter(provider.getName())));
					ContributorItem contributorItem = new ContributorItem(query,
							provider.getName(), provider.getCount(), portalServer);

					List<ContributorItem.DataProviderItem> dataProviders = new ArrayList<ContributorItem.DataProviderItem>();

					List<Count> rawDataProviders = IngestionUtils.getCollectionsFromSolr(searchService, "DATA_PROVIDER", 
							"*:* AND PROVIDER:\"" + provider.getName() + "\"");
					for (Count dataProvider : rawDataProviders) {
						if (dataProvider.getCount() > 0) {
							dataProviders.add(contributorItem.new DataProviderItem(
									contributorItem, dataProvider.getName(),
									dataProvider.getCount()));
						}
					}

					contributorItem.setDataProviders(dataProviders);
					entries.add(contributorItem);
				} catch (UnsupportedEncodingException e) {
					log.warning(e.getMessage() + " on " + provider.getName());
				}
			}
		} catch (SolrTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SitemapPage<ContributorItem> model = new SitemapPage<ContributorItem>();
		model.setResults(entries);
		model.setPrefix("");
		model.setLeftContent(getStaticPagePart("/newcontent.html",
				StaticPageController.AFFIX_TEMPLATE_VAR_FOR_LEFT, locale));

		ModelAndView mavPage = ControllerUtil.createModelAndViewPage(model,
				locale, PortalPageInfo.PROVIDERS);
		clickStreamLogger.logUserAction(request,
				ClickStreamLogger.UserAction.SITE_MAP_XML, mavPage);
		return mavPage;
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
		return URLEncoder.encode(provider.replace("\"", "\\\""), "UTF-8");
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
}
