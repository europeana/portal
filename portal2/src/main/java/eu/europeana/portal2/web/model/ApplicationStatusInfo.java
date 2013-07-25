package eu.europeana.portal2.web.model;

import java.util.Calendar;

public class ApplicationStatusInfo {

	private static Calendar lastResponseCacheChecked;

	private static Calendar lastStaticPageCacheChecked;

	public static Calendar getLastResponseCacheChecked() {
		return lastResponseCacheChecked;
	}

	public static void setLastResponseCacheChecked(Calendar lastResponseCacheChecked) {
		ApplicationStatusInfo.lastResponseCacheChecked = lastResponseCacheChecked;
	}

	public static Calendar getLastStaticPageCacheChecked() {
		return lastStaticPageCacheChecked;
	}

	public static void setLastStaticPageCacheChecked(
			Calendar lastStaticPageCacheChecked) {
		ApplicationStatusInfo.lastStaticPageCacheChecked = lastStaticPageCacheChecked;
	}
}
