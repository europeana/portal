package eu.europeana.portal2.web.model.stats;

public class MonthStatistics {

	private int month;
	private String label;
	private long count;

	public MonthStatistics(int month, String label, long count) {
		super();
		this.month = month;
		this.label = label;
		this.count = count;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
