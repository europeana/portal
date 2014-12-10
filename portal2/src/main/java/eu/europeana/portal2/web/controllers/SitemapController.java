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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.ThumbnailService;
import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.edm.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.edm.service.SearchService;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.impl.StaticPageCache;
import eu.europeana.portal2.web.controllers.statics.StaticPageController;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.SitemapPage;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.ContributorItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.SitemapEntry;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * This class generates the XML sitemap files (the list of pages of a web site accessible to
 * crawlers) for the content of Europeana Portal. It follows the Sitemap Protocol v0.9
 * (http://www.sitemaps.org/protocol.html).<br>
 * It uses a Sitemap index file to group multiple sitemap files, to ensure the protocol limits of
 * 50,000 URLs and 10MB (10,485,760 bytes) filesize. Note that the Sitemap index file itself can
 * only include up to 50,000 Sitemaps (although 1,000 is another quoted figure!) and must not exceed
 * 10MB (10,485,760 bytes)
 * 
 * The sitemap index is generated by querying the {@link SearchService} using the id3hash facet,
 * which splits the records into groups based on the first 3 letters of their record identifiers.
 * Note that this is not a great hash selection, as there are many records that start with the same
 * identifier (the 'pathological data set', in this case 'Bibliography'). To adhere to the above
 * maximmum URL limits, a number of the id3hash groups are split across multiple sitemaps (and thus
 * multiple files in the sitemap index file).
 * 
 * @author Peter Kiraly, Bram Lohman
 * 
 */
@Controller
public class SitemapController {

  @Log
  private Logger log;

  @Resource
  private Configuration config;

  @Resource
  private StaticPageCache staticPageCache;

  @Resource
  private SearchService searchService;

  @Resource
  private ClickStreamLogService clickStreamLogger;

  @Resource
  private ThumbnailService thumbnailService;

  @Resource
  private EuropeanaUrlService urlService;

  private static final int VIDEO_SITEMAP_VOLUME_SIZE = 25000;
  private static final int MAX_URLS_PER_SITEMAP = 45000; // Strictly speaking it's 50,000, but
                                                         // taking a 10% margin for safety

  private static final String SITEMAP_INDEX_PARAMS = "images-%s-places-%s";
  private static final String SITEMAP_INDEX = "europeana-sitemap-index-hashed-";
  private static final String SITEMAP_HASHED_PARAMS = "prefix-%s-index-%s-images-%s-places-%s";
  private static final String SITEMAP_HASHED = "europeana-sitemap-hashed-";
  private static final String SITEMAP_VIDEO = "europeana-video-sitemap-";
  private static final String XML = ".xml";
  private static final String LN = "\n";
  private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private static final String SITEMAP_HEADER =
      "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";
  private static final String URLSET_HEADER =
      "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" xmlns:geo=\"http://www.google.com/geo/schemas/sitemap/1.0\">";

  private static final String URL_OPENING = "<url>";
  private static final String URL_CLOSING = "</url>";
  private static final String LOC_OPENING = "<loc>";
  private static final String LOC_CLOSING = "</loc>";

  // Latitude and longitude should be added together, so a second check
  // for longitude would just overload the server
  private static final String solrClauseIncludePlaces = "pl_wgs84_pos_lat:*";

  private static final String PREFIX_PATTERN = "^[0-9A-Za-z_]{3}$";
  private static String portalUrl;
  private static String sitemapCacheName;
  private static File sitemapCacheDir;
  private static Date lastSolrUpdate;
  private volatile static Calendar lastCheck;
  private static Map<String, Boolean> sitemapsBeingProcessed =
      new ConcurrentHashMap<String, Boolean>();

  private static List<ContributorItem> contributorEntries;

  /**
   * Generate the Solr clause "COMPLETENESS:['min' TO *]", where 'min' is the lower bound of the
   * completeness clause
   * 
   * @param min The lower bound of the completeness clause
   * @return String containing the Solr clause
   */
  public static String solrCompletenessClause(int min) {
    return "COMPLETENESS:[" + min + " TO *]";
  }

  /**
   * Generate the sitemap index file. This file groups multiple sitemap files to adhere to the
   * 50,000 URLs / 10MB (10,485,760 bytes) sitemap limit. This index itself may not list more than
   * 50,000 Sitemaps and must be no larger than 10MB (10,485,760 bytes). The sitemaps are split by
   * id3hash (the first 3 letters of the record), and further split into files of maximum
   * MAX_URLS_PER_SITEMAP if these groups exceed 50,000 URLs.
   * 
   * @param images If the image element should be included in the sitemap
   * @param places If the places element should be included in the sitemap
   * @param response The {@link HttpServletResponse}
   * @throws IOException For any file-related exceptions
   */
  @RequestMapping("/europeana-sitemap-index-hashed.xml")
  public void handleSitemapIndexHashed(@RequestParam(value = "images", required = false,
      defaultValue = "false") String images, @RequestParam(value = "places", required = false,
      defaultValue = "false") String places, HttpServletResponse response) throws IOException {

    boolean isImageSitemap = StringUtils.contains(images, "true");
    boolean isPlaceSitemap = StringUtils.contains(places, "true");

    // Initialise the sitemap directory
    setSitemapCacheDir();

    // Return a 404 if the sitemap directory cannot be used
    if (sitemapCacheDir == null) {
      response.setStatus(404);
      return;
    }

    String params = String.format(SITEMAP_INDEX_PARAMS, isImageSitemap, isPlaceSitemap);
    File cacheFile = new File(sitemapCacheDir.getAbsolutePath(), SITEMAP_INDEX + params + XML);

    // Generate the requested sitemap if it's outdated / doesn't exist (and is not currently being
    // created)
    if ((solrOutdated() || !cacheFile.exists()) && !sitemapsBeingProcessed.containsKey(params)) {
      boolean success = false;
      FileWriter fstream = new FileWriter(cacheFile);
      BufferedWriter fout = new BufferedWriter(fstream);
      ServletOutputStream out = response.getOutputStream();

      if (log.isInfoEnabled()) {
        log.info(String.format("Generating %s", cacheFile));
      }

      // Kick off a new thread
      try {
        PerReqSitemap sitemap =
            new PerReqSitemap(PerReqSitemap.INDEXED_HASHED, null, images, places);
        Thread t = new Thread(sitemap);
        t.start();
        while (StringUtils.equals(sitemap.getState(), PerReqSitemap.IDLE)
            || StringUtils.equals(sitemap.getState(), PerReqSitemap.STARTED)) {
          Thread.sleep(1000);
        }
        out.print(sitemap.getSitemap().toString());
        out.flush();

        fout.write(sitemap.getSitemap().toString());
        fout.flush();
        success = true;
      } catch (Exception e) {
        success = false;
        log.error("Exception during generation of " + cacheFile + ": " + e.getLocalizedMessage(), e);
      } finally {
        fout.close();
      }
      if (!success) {
        cacheFile.delete();
      }
    } else {
      // Sitemap is being generated, grab some coffee...
      if (sitemapsBeingProcessed.containsKey(params)) {
        do {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage(), e);
          }
        } while (sitemapsBeingProcessed.containsKey(params));
      }
      // Read the sitemap from file
      readCachedFile(response.getOutputStream(), cacheFile);
    }
  }

  /**
   * Generate the individual sitemaps, containing the actual record IDs. Each file needs to adhere
   * to the 50,000 URLs / 10MB (10,485,760 bytes) sitemap limit. Each sitemap is split by id3hash
   * (the first 3 letters of the record); an id3hash may be split over multiple files if there are
   * more than 50,000 records (the current implementation uses approx. 5.8 MB for 50,000 URLs, so
   * the size is not the limiting factor).
   * 
   * @param prefix The id3hash of the record
   * @param index An integer indicating which set of 50,000 records are to be put into the sitemap;
   *        multiplied by MAX_URLS_PER_SITEMAP to indicate the start parameter in the Solr query
   * @param images If the image element should be included in the sitemap
   * @param places If the places element should be included in the sitemap
   * @param response The {@link HttpServletResponse}
   * @throws IOException
   */
  @RequestMapping("/europeana-sitemap-hashed.xml")
  public void handleSitemap(@RequestParam(value = "prefix", required = true) String prefix,
      @RequestParam(value = "index", required = true) String index, @RequestParam(value = "images",
          required = false, defaultValue = "false") String images, @RequestParam(value = "places",
          required = false, defaultValue = "false") String places, HttpServletResponse response)
      throws IOException {

    boolean isImageSitemap = StringUtils.contains(images, "true");
    boolean isPlaceSitemap = StringUtils.contains(places, "true");

    // Initialise the sitemap directory
    setSitemapCacheDir();

    // Return a 404 if the sitemap directory cannot be used
    if (sitemapCacheDir == null || prefix.length() > 3 || !prefix.matches(PREFIX_PATTERN)) {
      response.setStatus(404);
      return;
    }
    String params =
        String.format(SITEMAP_HASHED_PARAMS, prefix, index, isImageSitemap, isPlaceSitemap);
    // Store these sitemaps in a subdirectory based on the first letter of the id3hash
    File subDir = new File(sitemapCacheDir.getAbsolutePath(), prefix.substring(0, 1));
    if (!subDir.exists()) {
      subDir.mkdirs();
    }
    File cacheFile = new File(subDir, SITEMAP_HASHED + params + XML);

    // Generate the requested sitemap if it's outdated / doesn't exist (and is not currently being
    // created)
    if ((solrOutdated() || !cacheFile.exists()) && !sitemapsBeingProcessed.containsKey(params)) {
      if (log.isInfoEnabled()) {
        log.info(String.format("Generating %s", cacheFile));
      }

      sitemapsBeingProcessed.put(params, true);
      int success = 0;
      SearchPage model = new SearchPage();

      response.setCharacterEncoding("UTF-8");
      long t = new Date().getTime();
      StringBuilder fullXML = createSitemapHashedContent(prefix, index, model, images, places);
      if (log.isInfoEnabled()) {
        log.info(String.format("Generated XML size: %s chars, it took: %s ms", fullXML.length(),
            (new Date().getTime() - t)));
      }

      // Generate response
      BufferedWriter fout = null;
      try {
        ServletOutputStream out = response.getOutputStream();
        out.print(fullXML.toString());
        out.flush();
        success = 1;
      } catch (Exception e) {
        success = 0;
        log.error(String.format(
            "Exception during outputing europeana-sitemap-hashed.xml: %s. File: %s",
            e.getLocalizedMessage(), cacheFile), e);
      }

      // Also write to disk
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
        log.error(String.format(
            "Exception during outputing europeana-sitemap-hashed.xml: %s. File: %s",
            e.getLocalizedMessage(), cacheFile), e);
      } finally {
        if (fout != null) {
          fout.close();
        }
      }

      if (success != 2 || cacheFile.getTotalSpace() == 0) {
        cacheFile.delete();
      }
      if (log.isInfoEnabled()) {
        log.info(Thread.currentThread().getName() + " served by generation");
      }
      sitemapsBeingProcessed.remove(params);
    } else {
      // Sitemap is being generated, grab some coffee...
      if (sitemapsBeingProcessed.containsKey(params) || !cacheFile.exists()) {
        do {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            log.error(String.format(
                "Exception during outputing europeana-sitemap-hashed.xml: %s. File: %s",
                e.getLocalizedMessage(), cacheFile), e);
          }
        } while (sitemapsBeingProcessed.containsKey(params) || !cacheFile.exists());
      }
      // Read the sitemap from file
      if (log.isInfoEnabled()) {
        log.info(cacheFile.getName() + " is served from cache");
      }
      readCachedFile(response.getOutputStream(), cacheFile);
    }
  }

  /**
   * Generate the content of the individual sitemap. Starts a new thread to do the work
   * 
   * @param prefix The id3hash of the record
   * @param index An integer indicating which set of 50,000 records are to be put into the sitemap;
   *        multiplied by MAX_URLS_PER_SITEMAP to indicate the start parameter in the Solr query
   * @param model The model, used for interaction with the BriefBean
   * @param isImageSitemap If the image element should be included in the sitemap
   * @param isPlaceSitemap If the places element should be included in the sitemap
   * @return StringBuilder containing the XML content of the sitemap
   */
  private StringBuilder createSitemapHashedContent(String prefix, String index, SearchPage model,
      String isImageSitemap, String isPlaceSitemap) {
    PerReqSitemap sitemap =
        new PerReqSitemap(PerReqSitemap.SITEMAP_HASHED, model, isImageSitemap, isPlaceSitemap,
            prefix, index);
    Thread t = new Thread(sitemap);
    t.start();
    while (StringUtils.equals(sitemap.getState(), PerReqSitemap.IDLE)
        || StringUtils.equals(sitemap.getState(), PerReqSitemap.STARTED)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        log.error(
            "Interrupted Exception during waiting for sitemap creation. " + e.getLocalizedMessage(),
            e);
      }
    }

    return sitemap.getSitemap();
  }

  /**
   * NOTE: This is a draft method for creating a video sitemap
   * (https://support.google.com/webmasters/answer/80472?hl=en). The RequestMapping has been
   * disabled to ensure this is not publically accessible.
   * 
   * @param volumeString
   * @param request
   * @param response
   * @throws EuropeanaQueryException
   * @throws IOException
   */
  // TODO: Complete this method, or remove/deprecate it
  // @RequestMapping("/europeana-video-sitemap.xml")
  public void handleVideoSitemap(
      @RequestParam(value = "volume", required = true) String volumeString,
      HttpServletRequest request, HttpServletResponse response) throws EuropeanaQueryException,
      IOException {
    setSitemapCacheDir();
    if (sitemapCacheDir == null) {
      response.setStatus(404);
      return;
    }

    String params =
        request.getQueryString() != null ? request.getQueryString().replaceAll("[^a-z0-9A-F]", "-")
            : "";
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

        xml =
            "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\">";
        out.println(xml);
        fout.write(xml + "\n");

        String queryString = "TYPE:VIDEO";
        Query query =
            new Query(queryString)
                .setParameter("rows", String.valueOf(VIDEO_SITEMAP_VOLUME_SIZE))
                .setStart(volume * VIDEO_SITEMAP_VOLUME_SIZE)
                .setParameter("fl",
                    "europeana_id,COMPLETENESS,title,TYPE,provider_aggregation_edm_object");

        List<? extends BriefBean> resultSet = null;
        try {
          resultSet = searchService.sitemap(BriefBean.class, query).getResults();
        } catch (SolrTypeException e) {
          log.error(String.format("Error during request sitemap from Solr: %s",
              e.getLocalizedMessage()));
        }

        if (resultSet != null) {
          for (BriefBean bean : resultSet) {
            BriefBeanDecorator doc = new BriefBeanDecorator(model, bean);
            SitemapEntry entry =
                new SitemapEntry(urlService.getCanonicalPortalRecord(bean.getId()).toString(),
                    doc.getThumbnail(), doc.getTitle()[0], doc.getEuropeanaCompleteness());
            out.println(URL_OPENING);
            out.println(LOC_OPENING + entry.getLoc() + LOC_CLOSING);

            fout.write(URL_OPENING + LN);
            fout.write(LOC_OPENING + entry.getLoc() + LOC_CLOSING + LN);
            if (doc.getType() == DocType.VIDEO && isVideo(entry.getImage())) {
              String image = entry.getImage().replace("&", "&amp;");
              out.println("<video:video>");
              out.println("<video:thumbnail_loc>" + image + "</video:thumbnail_loc>");
              /*
               * out.println("<video:thumbnail_loc>" + config.getImageCacheUrl() + "uri=" +
               * URLEncoder.encode(entry.getImage(), "UTF-8") +
               * "&amp;size=FULL_DOC</video:thumbnail_loc>");
               */
              out.println("<video:title>" + StringEscapeUtils.escapeXml(entry.getTitle())
                  + "</video:title>");
              out.println("<video:description>" + StringEscapeUtils.escapeXml(entry.getTitle())
                  + "</video:description>");
              out.println("<video:player_loc>" + URLEncoder.encode(entry.getImage(), "UTF-8")
                  + "</video:player_loc>");
              out.println("</video:video>");

              fout.write("<video:video>\n");
              fout.write("<video:thumbnail_loc>" + image + "</video:thumbnail_loc>\n");
              /*
               * fout.write("<video:thumbnail_loc>" + config.getImageCacheUrl() + "uri=" +
               * URLEncoder.encode(entry.getImage(), "UTF-8") +
               * "&amp;size=FULL_DOC</video:thumbnail_loc>\n");
               */
              fout.write("<video:title>" + StringEscapeUtils.escapeXml(entry.getTitle())
                  + "</video:title>\n");
              fout.write("<video:description>" + StringEscapeUtils.escapeXml(entry.getTitle())
                  + "</video:description>\n");
              fout.write("<video:player_loc>" + URLEncoder.encode(entry.getImage(), "UTF-8")
                  + "</video:player_loc>\n");
              fout.write("</video:video>\n");
            }

            out.println(URL_CLOSING);
            fout.write(URL_CLOSING + LN);
          }
        } else {
          log.error("The resultset for video sitemap is null.");
        }

        out.print("</urlset>");
        fout.write("</urlset>");
      } finally {
        out.flush();

        fout.flush();
        fout.close();
      }
    } else {
      readCachedFile(response.getOutputStream(), cacheFile);
    }
  }

  /**
   * Determines if a URL string is a video by checking the length of the URL (???)
   * 
   * @param url The URL to check
   * @return True if the url is not empty and longer than 15 Unicode code units.
   */
  private boolean isVideo(String url) {
    if (StringUtils.isEmpty(url)) {
      return false;
    }
    if (url.length() < 15) {
      return false;
    }
    return true;
  }

  /**
   * Lists the providers and the number of records they provide.
   * 
   * @param request The {@link HttpServletRequest}
   * @param locale The user's {@link Locale}
   * @return A ModelAndView containing the list of providers and number of records provided
   * @throws EuropeanaQueryException
   */
  @RequestMapping("/europeana-providers.html")
  public ModelAndView handleListOfContributors(HttpServletRequest request, Locale locale)
      throws EuropeanaQueryException {
    setSitemapCacheDir();

    String portalServer =
        new StringBuilder(config.getPortalServer()).append(config.getPortalName()).toString();

    // sitemap index - collections overview
    if (solrOutdated() || contributorEntries == null) {
      contributorEntries = new ArrayList<ContributorItem>();
      List<Count> providers;
      try {
        providers = searchService.createCollections("PROVIDER", "*:*");
        for (Count provider : providers) {
          try {
            String query =
                StringEscapeUtils.escapeXml(String.format(
                    "%s/search.html?query=*:*&qf=PROVIDER:%s", portalServer,
                    convertProviderToUrlParameter(provider.getName())));
            ContributorItem contributorItem =
                new ContributorItem(query, provider.getName(), provider.getCount(), portalServer);

            List<ContributorItem.DataProviderItem> dataProviders =
                new ArrayList<ContributorItem.DataProviderItem>();

            List<Count> rawDataProviders =
                searchService.createCollections("DATA_PROVIDER", "*:*",
                    "PROVIDER:\"" + provider.getName() + "\"");
            for (Count dataProvider : rawDataProviders) {
              if (dataProvider.getCount() > 0) {
                dataProviders.add(contributorItem.new DataProviderItem(contributorItem,
                    dataProvider.getName(), dataProvider.getCount()));
              }
            }

            contributorItem.setDataProviders(dataProviders);
            contributorEntries.add(contributorItem);
          } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage() + " on " + provider.getName());
          }
        }
      } catch (SolrTypeException e1) {
        e1.printStackTrace();
      }
    }

    SitemapPage<ContributorItem> model = new SitemapPage<ContributorItem>();
    model.setResults(contributorEntries);
    model.setPrefix("");
    model.setLeftContent(getStaticPagePart("/newcontent.html",
        StaticPageController.AFFIX_TEMPLATE_VAR_FOR_LEFT, locale));

    ModelAndView page =
        ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.PROVIDERS);
    clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.SITE_MAP_XML, page);
    return page;
  }

  /**
   * Returns an XML sitemap representation containing an URL of the
   * {@link SitemapController#handleListOfContributors(HttpServletRequest, Locale)}
   * 
   * @param request
   * @return ModelAndView
   */
  @RequestMapping("/europeana-sitemap-static.xml")
  public ModelAndView handleSitemap(HttpServletRequest request) {

    List<SitemapEntry> records = new ArrayList<SitemapEntry>();
    records.add(new SitemapEntry("http://www.europeana.eu/portal/europeana-providers.html", null,
        null, 10));

    SitemapPage<SitemapEntry> model = new SitemapPage<SitemapEntry>();
    model.setResults(records);
    model.setShowImages(false);

    ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.SITEMAP);
    clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.SITE_MAP_XML, page);
    return page;
  }

  public static String convertProviderToUrlParameter(String provider)
      throws UnsupportedEncodingException {
    String url = URLEncoder.encode(provider.replace("\"", "\\\"").replace("/", "\\/"), "UTF-8");
    return url;
  }

  /**
   * Replace the periods ('.') in fileName with underscore, partName and period
   * 
   * @param fileName
   * @param partName
   * @param language
   * @return The replaced string
   */
  private String getStaticPagePart(String fileName, String partName, Locale language) {

    if (!StringUtils.isEmpty(partName)) {
      fileName = StringUtils.replaceOnce(fileName, ".", "_" + partName + ".");
    }

    return staticPageCache.getPage(fileName, language);
  }

  /**
   * Provide the string representation of the Portal URL, including forward slash
   * 
   * @return
   */
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
   * Set sitemap directory, and create it in the filesystem if it does not exist. The sitemap
   * directory is picked up from the 'portal.sitemap.cache' key in the properties file
   */
  private void setSitemapCacheDir() {
    if (sitemapCacheDir == null) {
      sitemapCacheName = config.getSitemapCache();
      if (sitemapCacheName != null) {
        if (!sitemapCacheName.endsWith("/")) {
          sitemapCacheName += "/";
        }
        sitemapCacheDir = new File(sitemapCacheName);
        if (!sitemapCacheDir.exists()) {
          sitemapCacheDir.mkdir();
        }
      }
    }
  }

  /**
   * Read a cached (sitemap) file, and copy its content to the output stream
   * 
   * @param out
   * @param cacheFile
   */
  private void readCachedFile(ServletOutputStream out, File cacheFile) {
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
        if (br != null)
          br.close();
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
   * Check if the Solr index has been modified since the last check. If it has been modified, the
   * cached files are cleared to ensure they are recreating with the current content.
   * 
   * @return Boolean flag whether or not the Solr has been modified since the last check
   */
  synchronized private boolean solrOutdated() {
    // check it once a day
    Calendar timeout = DateUtils.toCalendar(DateUtils.addDays(new Date(), -1));
    if (lastCheck == null || lastCheck.before(timeout)) {
      if (log.isInfoEnabled()) {
        log.info(String.format("%s requesting solr outdated (timeout: %s, lastCheck: %s)", Thread
            .currentThread().getName(), timeout.getTime().toString(), (lastCheck == null ? "null"
            : lastCheck.getTime().toString())));
      }
      lastCheck = Calendar.getInstance();
      Date actualSolrUpdate = null;
      try {
        if (log.isInfoEnabled()) {
          log.info("start checking Solr update time");
        }
        actualSolrUpdate = searchService.getLastSolrUpdate();
        if (log.isInfoEnabled()) {
          log.info("Solr update time checked");
        }
      } catch (SolrServerException e) {
        log.error("SolrServerException " + e.getLocalizedMessage());
      } catch (IOException e) {
        log.error("IOException " + e.getLocalizedMessage());
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
   * Deletes all the cached files in the Sitemap directory
   */
  private void deleteCachedFiles() {
    setSitemapCacheDir();
    for (File file : sitemapCacheDir.listFiles()) {
      file.delete();
    }
  }

  /**
   * Runnable interface for creating a sitemap per request. Implements the {@link Runnable#run()}
   * method to kick off creating the sitemap index file, or a specific sitemap
   * 
   */
  private class PerReqSitemap implements Runnable {

    String[] args;
    String action;
    String state = IDLE;

    public final static String IDLE = "idle";
    public final static String STARTED = "started";
    public final static String ENDED = "ended";

    private final static String INDEXED_HASHED = "index_hashed";
    private final static String SITEMAP_HASHED = "sitemap_hashed";

    private StringBuilder sitemap;
    private SearchPage model;

    public PerReqSitemap(String action, SearchPage model, String... args) {
      this.action = action;
      this.model = model;
      this.args = args;
    }

    /**
     * Create the sitemap index file (containing links to all the sitemaps). This is limited to
     * 50,000 Sitemaps and must not exceed 10MB (10,485,760 bytes) Note that the same authority
     * lists the number of sitemaps in an index as both 1,000
     * (http://www.sitemaps.org/faq.html#faq_sitemap_size) and 50,000
     * (http://www.sitemaps.org/protocol.html#index); the code below assumes the higher limit is
     * correct
     */
    private void createSitemapIndexedHashed() {
      state = STARTED;
      StringBuilder s = new StringBuilder();
      s.append(XML_HEADER).append(LN);
      s.append(SITEMAP_HEADER).append(LN);

      String urlPath = "europeana-sitemap-hashed.xml?prefix=";
      String paramIndex = "&index=";
      String paramImages = "&images=";
      String paramPlaces = "&places=";
      // ?q=*:*&rows=0&facet=on&facet.field=id3hash&facet.limit=1000000&facet.sort=lexical
      Query query =
          new Query("*:*").setPageSize(0).setParameter("facet", "on")
              .setParameter("facet.field", "id3hash").setParameter("facet.limit", "1000000")
              .setParameter("facet.sort", "lexical");

      try {
        int siteMaps = 0;
        long totalRecordCount = 0;
        List<FacetField> results = searchService.sitemap(BriefBean.class, query).getFacetFields();
        for (FacetField facet : results) {
          if (facet.getName().equals("id3hash")) {
            for (Count value : facet.getValues()) {
              if (!value.getName().matches(PREFIX_PATTERN)) {
                log.warn(String.format("Prefix %s did not match pattern %s; skipping...",
                    value.getName(), PREFIX_PATTERN));
                continue;
              }

              // Split each hashID into sitemaps of max 45,000 URLs (to stay within the 50,000
              // limit)
              int urlPostfix = 0;
              for (int i = 0; i < value.getCount(); i += MAX_URLS_PER_SITEMAP) {
                StringBuilder sb = new StringBuilder();
                sb.append(getPortalUrl()).append(urlPath).append(value.getName())
                    .append(paramIndex).append(String.format("%03d", urlPostfix));
                sb.append(paramImages).append(StringUtils.contains(args[0], "true"));
                sb.append(paramPlaces).append(StringUtils.contains(args[1], "true"));
                s.append("<sitemap>").append(LOC_OPENING)
                    .append(StringEscapeUtils.escapeXml(sb.toString())).append(LOC_CLOSING)
                    .append("</sitemap>").append(LN);

                siteMaps++;
                urlPostfix++;
              }
              totalRecordCount += value.getCount();
            }
          }
        }
        if (log.isInfoEnabled()) {
          log.info(String.format("Child sitemaps: %d, total record count: %d", siteMaps,
              totalRecordCount));
        }
      } catch (SolrTypeException e) {
        log.error(String.format("Error during request sitemap from Solr: %s",
            e.getLocalizedMessage()));
      }
      s.append("</sitemapindex>");

      this.sitemap = s;
      state = ENDED;
    }

    /**
     * Create each individual sitemap listed in the sitemap index file. The filename has to match
     * the Solr id3hash and the postfix generated by splitting it into 45,000 URL chunks.
     */
    private void createSitemapContent() {
      String isImageSitemapString = args[0];
      String isPlaceSitemapString = args[1];
      String id3hashValue = args[2];
      String index = args[3];

      state = STARTED;
      StringBuilder fullXML = new StringBuilder();

      fullXML.append(XML_HEADER).append(LN);
      fullXML.append(URLSET_HEADER).append(LN);
      boolean isImageSitemap = StringUtils.contains(isImageSitemapString, "true");
      boolean isPlaceSitemap = StringUtils.contains(isPlaceSitemapString, "true");
      String queryString = solrCompletenessClause(config.getMinCompletenessToPromoteInSitemaps());
      // ?q=*:*&fq=COMPLETENESS:[0%20TO%20*]&fq=id3hash:<HASH>&start=<START>&rows=45000&fl=europeana_id,COMPLETENESS,title,TYPE,provider_aggregation_edm_object
      Query query =
          new Query("*:*")
              .addRefinement(queryString)
              .addRefinement("id3hash:" + id3hashValue)
              .setStart(Integer.parseInt(index) * MAX_URLS_PER_SITEMAP)
              .setPageSize(MAX_URLS_PER_SITEMAP)
              .setParameter("fl",
                  "europeana_id,COMPLETENESS,title,TYPE,provider_aggregation_edm_object");

      if (isPlaceSitemap) {
        if (!StringUtils.isBlank(solrClauseIncludePlaces)) {
          query = query.addRefinement(solrClauseIncludePlaces);
        }
      }

      if (log.isInfoEnabled()) {
        log.info("queryString: " + query.toString());
      }
      List<BriefBean> resultSet = null;
      try {
        long t = new Date().getTime();
        resultSet = searchService.sitemap(BriefBean.class, query).getResults();
        if (log.isInfoEnabled()) {
          log.info(String.format("Query took: %d ms, produced %d results.",
              (new Date().getTime() - t), resultSet.size()));
        }
      } catch (SolrTypeException e) {
        log.error(String.format("Error during request sitemap from Solr for id3hash:%s: %s",
            id3hashValue, e.getLocalizedMessage()));
      }

      if (resultSet != null) {
        for (BriefBean bean : resultSet) {
          BriefBeanDecorator doc = new BriefBeanDecorator(model, bean);
          String title = "";
          if (doc.getTitle() != null) {
            title = doc.getTitle()[0];
          }
          SitemapEntry entry =
              new SitemapEntry(urlService.getCanonicalPortalRecord(bean.getId()).toString(),
                  doc.getThumbnail(), title, doc.getEuropeanaCompleteness());
          fullXML.append(URL_OPENING).append(LN);

          fullXML.append(LOC_OPENING).append(entry.getLoc(isPlaceSitemap)).append(LOC_CLOSING)
              .append(LN);

          if (isImageSitemap && doc.getType() == DocType.IMAGE) {
            String image = "";
            try {
              image = URLEncoder.encode(entry.getImage(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            fullXML.append("<image:image>").append(LN);
            fullXML.append("<image:loc>")
                .append(config.getImageCacheUrl() + "uri=" + image + "&amp;size=FULL_DOC")
                .append("</image:loc>").append(LN);
            fullXML.append("<image:title>").append(StringEscapeUtils.escapeXml(entry.getTitle()))
                .append("</image:title>").append(LN);
            fullXML.append("</image:image>").append(LN);
          }

          if (isPlaceSitemap) {
            fullXML.append("<geo:geo><geo:format>kml</geo:format></geo:geo>").append(LN);
          }

          fullXML.append("<priority>").append(entry.getPriority()).append("</priority>").append(LN);
          fullXML.append(URL_CLOSING).append(LN);
        }
      } else {
        log.error(String.format("The result set for sitemap is null for id3hash:%s.", id3hashValue));
      }

      fullXML.append("</urlset>").append(LN);

      this.sitemap = fullXML;
      state = ENDED;
    }

    public StringBuilder getSitemap() {
      return sitemap;
    }

    public String getState() {
      return this.state;
    }

    @Override
    public void run() {
      if (StringUtils.equals(action, INDEXED_HASHED)) {
        createSitemapIndexedHashed();
      } else if (StringUtils.equals(action, SITEMAP_HASHED)) {
        createSitemapContent();
      }
    }
  }
}
