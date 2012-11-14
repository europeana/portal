package eu.europeana.portal2.web.presentation.model;

import java.util.Map;

import eu.europeana.portal2.web.presentation.model.data.AdminData;

public class StatisticsPage extends AdminData {

	private String type;

	private Map<String, Integer> dateStatistics;

	private Map<String, Integer> userStatistics;

	private Map<String, Map<String, Integer>> typeStatistics;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Integer> getDateStatistics() {
		return dateStatistics;
	}

	public void setDateStatistics(Map<String, Integer> dateStatistics) {
		this.dateStatistics = dateStatistics;
	}

	public Map<String, Map<String, Integer>> getTypeStatistics() {
		return typeStatistics;
	}

	public void setTypeStatistics(Map<String, Map<String, Integer>> typeStatistics) {
		this.typeStatistics = typeStatistics;
	}

	public Map<String, Integer> getUserStatistics() {
		return userStatistics;
	}

	public void setUserStatistics(Map<String, Integer> userStatistics) {
		this.userStatistics = userStatistics;
	}
}
