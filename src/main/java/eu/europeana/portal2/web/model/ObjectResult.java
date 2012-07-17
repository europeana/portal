package eu.europeana.portal2.web.model;

import eu.europeana.portal2.web.model.abstracts.ApiResponse;

public class ObjectResult extends ApiResponse {
	
	public FullBean4Json object;
	
	public ObjectResult(){
		// used by Jackson
	};
	
	public ObjectResult(String apikey, String action) {
		super(apikey, action);
	}
}
