package eu.europeana.corelib.solr.service.mock.bean;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.solr.bean.impl.BriefBeanImpl;
import eu.europeana.corelib.utils.StringArrayUtils;

public class BriefBeanMock extends BriefBeanImpl {

	public BriefBeanMock(String id, DocType type, String title) {
		this.id = id;
		this.docType = StringArrayUtils.toArray(type.toString());
		this.title = StringArrayUtils.toArray(title);
	}
}
