package eu.europeana.portal2.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;

public class Configuration {

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="corelib_web_configInterceptor") private ConfigInterceptor configInterceptor;

	@Resource private Properties europeanaProperties;

	private final Logger log = Logger.getLogger(getClass().getName());

	///////////////////////////////// properties

	// basic portal value
	@Value("#{europeanaProperties['portal.name']}")
	private String portalName;

	@Value("#{europeanaProperties['portal.server']}")
	private String portalServer;

	@Value("#{europeanaProperties['portal.theme']}")
	private String defaultTheme;

	@Value("#{europeanaProperties['static.page.path']}")
	private String staticPagePath;

	@Value("#{europeanaProperties['static.page.checkFrequencyInMinute']}")
	private Integer staticPageCheckFrequencyInMinute;

	// blog settings
	@Value("#{europeanaProperties['portal.blog.url']}")
	private String blogUrl;

	@Value("#{europeanaProperties['portal.blog.timeout']}")
	private Integer blogTimeout;

	// Pinterest settings
	@Value("#{europeanaProperties['portal.pinterest.url']}")
	private String pintUrl;

	@Value("#{europeanaProperties['portal.pinterest.feedurl']}")
	private String pintFeedUrl;

	@Value("#{europeanaProperties['portal.pinterest.timeout']}")
	private Integer pintTimeout;

	@Value("#{europeanaProperties['portal.pinterest.itemslimit']}")
	private Integer pintItemLimit;

	// Google+ settings
	@Value("#{europeanaProperties['portal.google.plus.publisher.id']}")
	private static String portalGooglePlusPublisherId;

	// responsive images in the index page
	@Value("#{europeanaProperties['portal.responsive.widths']}")
	private String responsiveImageWidthString;

	@Value("#{europeanaProperties['portal.responsive.carousel.widths']}")
	private String responsiveCarouselImageWidthString;

	@Value("#{europeanaProperties['portal.responsive.labels']}")
	private String responsiveImageLabelString;

	@Value("#{europeanaProperties['portal.responsive.carousel.labels']}")
	private String responsiveCarouselImageLabelString;

	@Value("#{europeanaProperties['portal.shownAtProviderOverride']}")
	private String[] shownAtProviderOverride;

	// API settings
	@Value("#{europeanaProperties['api2.url']}")
	private String api2url;

	@Value("#{europeanaProperties['api2.key']}")
	private String api2key;

	@Value("#{europeanaProperties['api2.secret']}")
	private String api2secret;

	// Schema.org maping
	@Value("#{europeanaProperties['schema.org.mapping']}")
	private String schemaOrgMappingFile;

	@Value("#{europeanaProperties['imageCacheUrl']}")
	private String imageCacheUrl;

	@Value("#{europeanaProperties['portal.minCompletenessToPromoteInSitemaps']}")
	private int minCompletenessToPromoteInSitemaps;

	@Value("#{europeanaProperties['portal.contentchecker']}")
	private String isContentChecker;

	@Value("#{europeanaProperties['portal.rowLimit']}")
	private String rowLimit;

	@Value("#{europeanaProperties['debug']}")
	private String debug;

	@Value("#{europeanaProperties['portal.responsive.cache']}")
	private String responsiveCache;
	
	@Value("#{europeanaProperties['portal.responsive.cache.checkFrequencyInMinute']}")
	private Integer responsiveCacheCheckFrequencyInMinute;

	/////////////////////////////// generated/derivated properties

	private Map<String, String> seeAlsoTranslations;

	private String portalUrl;

	private String[] responsiveImageLabels;

	private String[] responsiveCarouselImageLabels;

	private Integer[] responsiveImageWidths;

	private Integer[] responsiveCarouselImageWidths;

	///////////////////////////////// getters and setters

	public String getPortalName() {
		return portalName;
	}

	public String getPortalServer() {
		return portalServer;
	}

	public String[] getShownAtProviderOverride() {
		return shownAtProviderOverride;
	}

	public String getApi2url() {
		return api2url;
	}

	public String getApi2key() {
		return api2key;
	}

	public String getApi2secret() {
		return api2secret;
	}

	public String getSchemaOrgMappingFile() {
		return schemaOrgMappingFile;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public int getBlogTimeout() {
		return blogTimeout.intValue();
	}

	public String getPintUrl() {
		return pintUrl;
	}

	public String getPintFeedUrl() {
		return pintFeedUrl;
	}

	public int getPintTimeout() {
		return pintTimeout.intValue();
	}

	public int getPintItemLimit() {
		return pintItemLimit.intValue();
	}

	public String getDefaultTheme() {
		return defaultTheme;
	}

	public String getStaticPagePath() {
		return staticPagePath;
	}

	public String getResponsiveImageWidthString() {
		return responsiveImageWidthString;
	}

	public String getResponsiveImageLabelString() {
		return responsiveImageLabelString;
	}

	public static String getPortalGooglePlusPublisherId() {
		return portalGooglePlusPublisherId;
	}

	public String getImageCacheUrl() {
		return imageCacheUrl;
	}

	public int getMinCompletenessToPromoteInSitemaps() {
		return minCompletenessToPromoteInSitemaps;
	}

	public boolean isContentChecker() {
		return isContentChecker.equals("true");
	}

	public int getRowLimit() {
		return Integer.parseInt(rowLimit);
	}

	public boolean getDebugMode() {
		return StringUtils.isBlank(debug) || Boolean.getBoolean(debug);
	}

	public String getResponsiveCache() {
		return responsiveCache;
	}

	public Integer getStaticPageCheckFrequencyInMinute() {
		return staticPageCheckFrequencyInMinute;
	}

	public Integer getResponsiveCacheCheckFrequencyInMinute() {
		return responsiveCacheCheckFrequencyInMinute;
	}

	public Map<String, String> getSeeAlsoTranslations() {
		if (seeAlsoTranslations == null) {
			seeAlsoTranslations = new HashMap<String, String>();
			int i = 1;
			while (europeanaProperties.containsKey("portal.seeAlso.field." + i)) {
				String[] parts = europeanaProperties.getProperty("portal.seeAlso.field." + i).split("=", 2);
				seeAlsoTranslations.put(parts[0], parts[1]);
				i++;
			}
		}
		return seeAlsoTranslations;
	}

	public String getPortalUrl() {
		if (portalUrl == null) {
			StringBuilder sb = new StringBuilder(portalServer);
			if (!portalServer.endsWith("/") && !portalName.startsWith("/")) {
				sb.append("/");
			}
			sb.append(portalName);
			portalUrl = sb.toString();
		}
		return portalUrl;
	}

	public String[] getResponsiveImageLabels() {
		if (responsiveImageLabels == null) {
			responsiveImageLabels = responsiveImageLabelString.split(",");
		}
		return responsiveImageLabels;
	}

	public Integer[] getResponsiveImageWidths(){
		log.info("getResponsiveImageWidths()");
		if (responsiveImageWidths == null) {
			String[] imageWidths = responsiveImageWidthString.split(",");
			responsiveImageWidths = new Integer[imageWidths.length];

			for (int i=0, len=imageWidths.length; i<len; i++) {
				responsiveImageWidths[i] = Integer.parseInt(imageWidths[i]);
			}
			log.info("responsiveImageWidths: " + responsiveImageWidths);
		}
		log.info("responsiveImageWidths: " + responsiveImageWidths);

		return responsiveImageWidths;
	}

	public String[] getResponsiveCarouselImageLabels() {
		if (responsiveCarouselImageLabels == null) {
			responsiveCarouselImageLabels = responsiveCarouselImageLabelString.split(",");
		}
		return responsiveCarouselImageLabels;
	}

	public Integer[] getResponsiveCarouselImageWidths(){
		if (responsiveCarouselImageWidths == null) {
			String[] imageWidths = responsiveCarouselImageWidthString.split(",");
			responsiveCarouselImageWidths = new Integer[imageWidths.length];

			for (int i=0, len=imageWidths.length; i<len; i++) {
				responsiveCarouselImageWidths[i] = Integer.parseInt(imageWidths[i]);
			}
		}

		return responsiveCarouselImageWidths;
	}
}
