package eu.europeana.portal2.services;

import org.springframework.beans.factory.annotation.Value;

public class Configuration {

	@Value("#{europeanaProperties['portal.blog.url']}")
	private String blogUrl;

	@Value("#{europeanaProperties['portal.blog.timeout']}")
	private Integer blogTimeout;

	@Value("#{europeanaProperties['portal.pinterest.url']}")
	private String pintUrl;

	@Value("#{europeanaProperties['portal.pinterest.feedurl']}")
	private String pintFeedUrl;

	@Value("#{europeanaProperties['portal.pinterest.timeout']}")
	private Integer pintTimeout;

	@Value("#{europeanaProperties['portal.pinterest.itemslimit']}")
	private Integer pintItemLimit;
	
	@Value("#{europeanaProperties['portal.theme']}")
	private String defaultTheme;

	@Value("#{europeanaProperties['static.page.path']}")
	private String staticPagePath;

	@Value("#{europeanaProperties['portal.responsive.widths']}")
	private String responsiveImageWidthString;

	@Value("#{europeanaProperties['portal.responsive.labels']}")
	private String responsiveImageLabelString;

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
}
