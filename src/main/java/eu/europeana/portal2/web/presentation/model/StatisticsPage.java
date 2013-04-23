package eu.europeana.portal2.web.presentation.model;

import java.util.Map;

import eu.europeana.portal2.web.presentation.model.data.AdminData;

public class StatisticsPage extends AdminData {

	private String type;

	private Map<String, Long> dateStatistics;

	private Map<String, Long> userStatistics;

	private Map<String, Map<String, Integer>> typeStatistics;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Long> getDateStatistics() {
		return dateStatistics;
	}

	public void setDateStatistics(Map<String, Long> dateStatistics) {
		this.dateStatistics = dateStatistics;
	}

	public Map<String, Map<String, Integer>> getTypeStatistics() {
		return typeStatistics;
	}

	public void setTypeStatistics(Map<String, Map<String, Integer>> typeStatistics) {
		this.typeStatistics = typeStatistics;
	}

	public Map<String, Long> getUserStatistics() {
		return userStatistics;
	}

	public void setUserStatistics(Map<String, Long> userStatistics) {
		this.userStatistics = userStatistics;
	}
}
