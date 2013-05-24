package eu.europeana.portal2.web.presentation.model;

import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.model.statistics.TypeStatistics;
import eu.europeana.corelib.definitions.model.statistics.UserStatistics;
import eu.europeana.portal2.web.controllers.user.MonthStatistics;
import eu.europeana.portal2.web.presentation.model.data.AdminData;

public class StatisticsPage extends AdminData {

	private String type;

	private String order;

	private int month;

	private String monthLabel;

	/**
	 * Whether the order is descending (true) or ascending (false)
	 */
	private boolean descending = false;

	private Map<String, Long> dateStatistics;

	private Map<Object, List<UserStatistics>> userStatistics;

	private Map<Object, List<TypeStatistics>> typeStatistics;

	private List<MonthStatistics> monthStatistics;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean isDescending() {
		return descending;
	}

	public void setDescending(boolean descending) {
		this.descending = descending;
	}

	public Map<String, Long> getDateStatistics() {
		return dateStatistics;
	}

	public void setDateStatistics(Map<String, Long> dateStatistics) {
		this.dateStatistics = dateStatistics;
	}

	public Map<Object, List<TypeStatistics>> getTypeStatistics() {
		return typeStatistics;
	}

	public void setTypeStatistics(Map<Object, List<TypeStatistics>> typeStatistics) {
		this.typeStatistics = typeStatistics;
	}

	public Map<Object, List<UserStatistics>> getUserStatistics() {
		return userStatistics;
	}

	public void setUserStatistics(Map<Object, List<UserStatistics>> userStatistics) {
		this.userStatistics = userStatistics;
	}

	public List<MonthStatistics> getMonthStatistics() {
		return monthStatistics;
	}

	public void setMonthStatistics(List<MonthStatistics> monthStatistics) {
		this.monthStatistics = monthStatistics;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getMonthLabel() {
		return monthLabel;
	}

	public void setMonthLabel(String monthLabel) {
		this.monthLabel = monthLabel;
	}
}
