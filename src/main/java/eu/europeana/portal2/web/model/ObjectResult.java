package eu.europeana.portal2.web.model;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.portal2.web.model.abstracts.ApiResponse;

public class ObjectResult extends ApiResponse {
	
	public FullBean object;
	
	public ObjectResult(String apikey, String action) {
		super(apikey, action);
	}
}
