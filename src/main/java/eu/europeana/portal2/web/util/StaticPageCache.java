package eu.europeana.portal2.web.util;

import java.util.Calendar;

import javax.annotation.Resource;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.util.abstracts.StaticCache;

public class StaticPageCache extends StaticCache {

	@Resource(name = "configurationService")
	private Configuration config;

	public StaticPageCache() {
		super();
	}

	@Override
	protected Integer getCheckFrequency() {
		return config.getStaticPageCheckFrequencyInMinute();
	}

	@Override
	protected Calendar getLastCheck() {
		return ApplicationStatusInfo.getLastStaticPageCacheChecked();
	}

	@Override
	protected void setLastCheck(Calendar lastCheck) {
		ApplicationStatusInfo.setLastStaticPageCacheChecked(lastCheck);
	}
}
