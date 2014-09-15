package eu.europeana.portal2.services.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;

import eu.europeana.portal2.services.impl.abstracts.StaticCache;
import eu.europeana.portal2.web.model.ApplicationStatusInfo;

public class ResponsiveImageCache extends StaticCache {

	@Value("#{europeanaProperties['portal.responsive.cache.checkFrequencyInMinute']}")
	private Integer responsiveCacheCheckFrequencyInMinute;

	public ResponsiveImageCache() {
		super();
	}

	@Override
	protected Integer getCheckFrequency() {
		return responsiveCacheCheckFrequencyInMinute;
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
