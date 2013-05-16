package eu.europeana.portal2.web.model.statistics;

/**
 * Implementation of TypeStatistics.
 * 
 * @author peter.kiraly@kb.nl
 */
public class TypeStatisticsImpl implements TypeStatistics {

	String recordType;
	String profile;
	long count;

	public TypeStatisticsImpl(String recordType, String profile, long count) {
		super();
		this.recordType = recordType;
		this.profile = profile;
		this.count = count;
	}

	@Override
	public String getRecordType() {
		return recordType;
	}

	@Override
	public String getProfile() {
		return profile;
	}

	@Override
	public long getCount() {
		return count;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
