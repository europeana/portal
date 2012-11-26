package eu.europeana.portal2.web.util;

import java.util.Calendar;

import javax.annotation.Resource;

import eu.europeana.portal2.services.Configuration;

public class ResponsiveImageCache extends StaticCache {

	@Resource(name="configurationService") private Configuration config;

	public ResponsiveImageCache() {
		super();
	}

	protected Integer getCheckFrequency() {
		return config.getResponsiveCacheCheckFrequencyInMinute();
	}

	protected Calendar getLastCheck() {
		return ApplicationStatusInfo.getLastResponseCacheChecked();
	}

	protected void setLastCheck(Calendar lastCheck) {
		ApplicationStatusInfo.setLastResponseCacheChecked(lastCheck);
	}
}
