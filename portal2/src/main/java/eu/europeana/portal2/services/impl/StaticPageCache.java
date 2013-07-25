package eu.europeana.portal2.services.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;

import eu.europeana.portal2.services.impl.abstracts.StaticCache;
import eu.europeana.portal2.web.model.ApplicationStatusInfo;

public class StaticPageCache extends StaticCache {

	@Value("#{europeanaProperties['static.page.checkFrequencyInMinute']}")
	private Integer staticPageCheckFrequencyInMinute;

	public StaticPageCache() {
		super();
	}

	@Override
	protected Integer getCheckFrequency() {
		return staticPageCheckFrequencyInMinute;
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
