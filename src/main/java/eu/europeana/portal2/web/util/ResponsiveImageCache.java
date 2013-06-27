package eu.europeana.portal2.web.util;

import java.util.Calendar;

import javax.annotation.Resource;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.util.abstracts.StaticCache;

public class ResponsiveImageCache extends StaticCache {

	@Resource(name = "configurationService")
	private Configuration config;

	public ResponsiveImageCache() {
		super();
	}

	@Override
	protected Integer getCheckFrequency() {
		return config.getResponsiveCacheCheckFrequencyInMinute();
	}

	@Override
	protected Calendar getLastCheck() {
		return ApplicationStatusInfo.getLastResponseCacheChecked();
	}

	@Override
	protected void setLastCheck(Calendar lastCheck) {
		ApplicationStatusInfo.setLastResponseCacheChecked(lastCheck);
	}
}
