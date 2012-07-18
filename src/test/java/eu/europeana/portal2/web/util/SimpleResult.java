package eu.europeana.portal2.web.util;

public class SimpleResult {

	private String apikey;
	private String action;
	private boolean success;
	private int statsDuration;
	
	public SimpleResult(){}
	
	public String getApikey() {
		return apikey;
	}
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public int getStatsDuration() {
		return statsDuration;
	}
	
	public void setStatsDuration(int statsDuration) {
		this.statsDuration = statsDuration;
	}
}
