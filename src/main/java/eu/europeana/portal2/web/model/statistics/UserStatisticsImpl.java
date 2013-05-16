package eu.europeana.portal2.web.model.statistics;

public class UserStatisticsImpl implements UserStatistics {

	String name;
	String apiKey;
	long count;

	public UserStatisticsImpl(String name, String apiKey, long count) {
		super();
		this.name = name;
		this.apiKey = apiKey;
		this.count = count;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getApiKey() {
		return apiKey;
	}

	@Override
	public long getCount() {
		return count;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setCount(long count) {
		this.count = count;
	}

	
}
