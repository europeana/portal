package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import eu.europeana.corelib.definitions.solr.beans.FullBean;

public class FullBeanDecorator extends ShortcutWrapper {

	public FullBeanDecorator(FullBean fullBean) {
		super(fullBean);
	}

	public FullBeanDecorator(FullBean fullBean, String userLanguage) {
		super(fullBean, userLanguage);
	}

}
