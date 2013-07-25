package eu.europeana.portal2.web.controllers.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.ApiLogService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.model.statistics.TypeStatistics;
import eu.europeana.corelib.definitions.model.statistics.UserStatistics;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.utils.DateIntervalUtils;
import eu.europeana.corelib.utils.model.DateInterval;
import eu.europeana.portal2.web.model.stats.MonthStatistics;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.StatisticsPage;
import eu.europeana.portal2.web.util.CSVUtils;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class StatisticsController {

	@Log
	private Logger log;

	@Resource
	private ApiKeyService apiKeyService;

	@Resource
	private ApiLogService apiLogService;

	private static final List<String> TYPES = Arrays
			.asList(new String[] { "dates", "months", "recordTypes", "apiKeys", "month", "recordType", "apiKey",
					"monthsByUser", "usersByMonth", "usersByRecordType", "recordTypesByuser" });

	private static final List<String> ORDERS = Arrays.asList(new String[] { "name", "count", "apikey" });

	private static final Map<String, Integer> RECORD_TYPES = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("SEARCH", 1);
			put("OBJECT", 2);
			put("LIMIT", 3);
			put("REDIRECT", 4);
		}
	};

	private static final int DEFAULT_TYPE_SYMBOL = 10;
	private static final int LAST_TYPE_SYMBOL = 100;

	@RequestMapping("/admin/statistics.html")
	public ModelAndView statisticsHandler(@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "order", required = false, defaultValue = "name") String order,
			@RequestParam(value = "recordType", required = false, defaultValue = "SEARCH") String recordType,
			@RequestParam(value = "apiKey", required = false, defaultValue = "") String apiKey,
			@RequestParam(value = "dir", required = false, defaultValue = "") String direction,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		log.info("==== admin/statistics.html ====");
		StatisticsPage model = new StatisticsPage();
		if (StringUtils.isBlank(type) || !TYPES.contains(type)) {
			type = "dates";
		}
		model.setType(type);

		if (StringUtils.isBlank(order) || !ORDERS.contains(order)) {
			order = "name";
		}
		model.setOrder(order);
		model.setMonth(month);
		if (StringUtils.isBlank(recordType) || !RECORD_TYPES.containsKey(recordType)) {
			recordType = "SEARCH";
		}

		if (direction.equals("desc")) {
			model.setDescending(true);
		}

		if (type.equals("dates")) {
			model.setPageTitle("Statistics for last 30 days");
			model.setDateStatistics(getDateStatistics());
		} else if (type.equals("months")) {
			model.setPageTitle("Statistics for months");
			model.setMonthStatistics(getMonthsStatistics());
		} else if (type.equals("recordTypes")) {
			model.setPageTitle("Statistics for record types");
			model.setTypeStatistics(getTypeStatistics());
		} else if (type.equals("apiKeys")) {
			model.setPageTitle("Statistics for API keys");
			model.setUserStatistics(getUserStatistics(order, model.isDescending()));
		} else if (type.equals("month")) {
			model.setPageTitle("Statistics for a month");
			model.setMonthLabel(getMonthLabel(month));
			model.setUserStatistics(getUsersByMonthStatistics(month, order, model.isDescending()));
			model.setTypeStatistics(getRecordTypesByMonthStatistics(month));
		} else if (type.equals("recordType")) {
			model.setPageTitle("Statistics for a record type");
			model.setRecordType(recordType);
			model.setUserStatistics(getUsersByRecordTypeStatistics(recordType, order, model.isDescending()));
			model.setMonthStatistics(getMonthsByRecordTypeStatistics(recordType));
		} else if (type.equals("apiKey")) {
			model.setPageTitle("Statistics for an API key");
			model.setApiKey(apiKey);
			model.setUserName(getUserName(apiKey));
			model.setTypeStatistics(getRecordTypesByUserStatistics(apiKey));
			model.setMonthStatistics(getMonthsByApiKeyStatistics(apiKey));
			// legacy code
		} else if (type.equals("usersByMonth")) {
			model.setPageTitle("Statistics for users by month");
			model.setMonthLabel(getMonthLabel(month));
			model.setUserStatistics(getUsersByMonthStatistics(month, order, model.isDescending()));
		} else if (type.equals("usersByRecordType")) {
			model.setPageTitle("Statistics for users by record type");
			model.setRecordType(recordType);
			model.setUserStatistics(getUsersByRecordTypeStatistics(recordType, order, model.isDescending()));
		}

		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.ADMIN_STATISTICS);
	}

	@RequestMapping(value = "/admin/statistics.csv", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody
	String statisticsCSVHandler(@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "month", required = false, defaultValue = "0") int month,
			@RequestParam(value = "stat", required = false, defaultValue = "") String stat,
			@RequestParam(value = "recordType", required = false, defaultValue = "SEARCH") String recordType,
			@RequestParam(value = "apiKey", required = false, defaultValue = "") String apiKey,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		if (StringUtils.isBlank(type) || !TYPES.contains(type)) {
			type = "dates";
		}
		if (StringUtils.isBlank(recordType) || !RECORD_TYPES.containsKey(recordType)) {
			recordType = "SEARCH";
		}

		String fileName = "api-statistics.csv";

		StringBuilder sb = new StringBuilder();

		if (type.equals("dates")) {
			fileName = "api-statistics-dates.csv";
			datesToCSV(sb, getDateStatistics());
		} else if (type.equals("months")) {
			fileName = "api-statistics-months.csv";
			monthToCSV(sb, getMonthsStatistics());
		} else if (type.equals("recordTypes")) {
			fileName = "api-statistics-recordTypes.csv";
			recordTypeToCSV(sb, getTypeStatistics());
		} else if (type.equals("apiKeys")) {
			fileName = "api-statistics-apiKeys.csv";
			apiKeyToCSV(sb, getUserStatistics("name", false));
		} else if (type.equals("month")) {
			if (stat.equals("apiKey")) {
				fileName = "api-statistics-month-by-apiKey.csv";
				apiKeyToCSV(sb, getUsersByMonthStatistics(month, "name", false));
			} else if (stat.equals("recordType")) {
				fileName = "api-statistics-month-by-recordType.csv";
				recordTypeToCSV(sb, getRecordTypesByMonthStatistics(month));
			}
		} else if (type.equals("recordType")) {
			if (stat.equals("apiKey")) {
				fileName = "api-statistics-recordType-by-apiKey.csv";
				apiKeyToCSV(sb, getUsersByRecordTypeStatistics(recordType, "name", false));
			} else if (stat.equals("month")) {
				fileName = "api-statistics-recordType-by-month.csv";
				monthToCSV(sb, getMonthsByRecordTypeStatistics(recordType));
			}
		} else if (type.equals("apiKey")) {
			if (stat.equals("recordType")) {
				fileName = "api-statistics-apiKey-by-recordType.csv";
				recordTypeToCSV(sb, getRecordTypesByUserStatistics(apiKey));
			} else if (stat.equals("month")) {
				fileName = "api-statistics-apiKey-by-month.csv";
				monthToCSV(sb, getMonthsByApiKeyStatistics(apiKey));
			}
		}

		response.setHeader("Content-Type", "text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		return sb.toString();
	}

	private void datesToCSV(StringBuilder sb, Map<String, Long> values) {
		List<String> fieldNames = new LinkedList<String>(Arrays.asList("Date", "Count"));
		sb.append(CSVUtils.encodeRecord(fieldNames));
		for (Map.Entry<String, Long> item : values.entrySet()) {
			List<String> fields = new LinkedList<String>();
			fields.add(CSVUtils.encodeField(item.getKey()));
			fields.add(item.getValue().toString());
			sb.append(CSVUtils.encodeRecord(fields));
		}
	}

	private void monthToCSV(StringBuilder sb, List<MonthStatistics> values) {
		List<String> fieldNames = new LinkedList<String>(Arrays.asList("Month", "Count"));
		sb.append(CSVUtils.encodeRecord(fieldNames));

		for (MonthStatistics item : values) {
			List<String> fields = new LinkedList<String>();
			fields.add(CSVUtils.encodeField(item.getLabel()));
			fields.add(String.valueOf(item.getCount()));
			sb.append(CSVUtils.encodeRecord(fields));
		}
	}

	private void recordTypeToCSV(StringBuilder sb, Map<Object, List<TypeStatistics>> values) {
		List<String> fieldNames = new LinkedList<String>(Arrays.asList("Record Type", "Profile", "Count"));
		sb.append(CSVUtils.encodeRecord(fieldNames));

		for (Map.Entry<Object, List<TypeStatistics>> value : values.entrySet()) {
			for (TypeStatistics item : value.getValue()) {
				List<String> fields = new LinkedList<String>();
				fields.add(CSVUtils.encodeField(item.getRecordType()));
				fields.add(CSVUtils.encodeField(item.getProfile()));
				fields.add(String.valueOf(item.getCount()));
				sb.append(CSVUtils.encodeRecord(fields));
			}
		}
	}

	private void apiKeyToCSV(StringBuilder sb, Map<Object, List<UserStatistics>> values) {
		List<String> fieldNames = new LinkedList<String>(Arrays.asList("User name", "API Key", "Count"));
		sb.append(CSVUtils.encodeRecord(fieldNames));
		for (Map.Entry<Object, List<UserStatistics>> value : values.entrySet()) {
			for (UserStatistics item : value.getValue()) {
				List<String> fields = new LinkedList<String>();
				fields.add(CSVUtils.encodeField(item.getName()));
				fields.add(CSVUtils.encodeField(item.getApiKey()));
				fields.add(String.valueOf(item.getCount()));
				sb.append(CSVUtils.encodeRecord(fields));
			}
		}
	}

	/**
	 * Create the date based statistics (last 30 days)
	 */
	private Map<String, Long> getDateStatistics() {
		Map<String, Long> stat = new TreeMap<String, Long>();
		for (int i = 0; i < 30; i++) {
			DateInterval interval = DateIntervalUtils.getDay(i);
			String day = new SimpleDateFormat("yyyy-MM-dd").format(interval.getBegin());
			long num = apiLogService.countByInterval(DateIntervalUtils.getDay(i));
			stat.put(day, num);
		}
		return stat;
	}

	/**
	 * Create the type based statistics
	 */
	private Map<Object, List<TypeStatistics>> getTypeStatistics() {
		List<TypeStatistics> types = apiLogService.getStatisticsForType();

		return createOrderedTypeMap(types);
	}

	private Map<Object, List<TypeStatistics>> getRecordTypesByUserStatistics(String apiKey) {
		List<TypeStatistics> types = apiLogService.getStatisticsForRecordTypesByUser(apiKey);

		return createOrderedTypeMap(types);
	}

	private Map<Object, List<TypeStatistics>> getRecordTypesByMonthStatistics(int month) {
		DateInterval interval = DateIntervalUtils.getMonth(new DateTime().getMonthOfYear() - month);
		List<TypeStatistics> types = apiLogService.getStatisticsForRecordTypesByInterval(interval);
		return createOrderedTypeMap(types);
	}

	/**
	 * Create the user based statistics
	 */
	private Map<Object, List<UserStatistics>> getUserStatistics(String orderBy, boolean descending) {
		Map<Object, List<UserStatistics>> stat = createUserStatisticsMap(orderBy, descending);
		List<UserStatistics> users = apiLogService.getStatisticsForUser();
		resolveUsers(users, stat, orderBy);

		return stat;
	}

	private Map<Object, List<UserStatistics>> getUsersByMonthStatistics(int month, String orderBy, boolean descending) {
		Map<Object, List<UserStatistics>> stat = createUserStatisticsMap(orderBy, descending);

		DateInterval interval = DateIntervalUtils.getMonth(new DateTime().getMonthOfYear() - month);
		List<UserStatistics> users = apiLogService.getStatisticsForUsersByInterval(interval);
		resolveUsers(users, stat, orderBy);

		return stat;
	}

	private Map<Object, List<UserStatistics>> getUsersByRecordTypeStatistics(String recordType, String orderBy,
			boolean descending) {
		Map<Object, List<UserStatistics>> stat = createUserStatisticsMap(orderBy, descending);
		List<UserStatistics> users = apiLogService.getStatisticsForUsersByRecordType(recordType);
		resolveUsers(users, stat, orderBy);
		return stat;
	}

	private String getMonthLabel(int month) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		DateTime now = new DateTime();
		DateInterval interval = DateIntervalUtils.getMonth(now.getMonthOfYear() - month);
		String label = String.format("%s&mdash;%s", dt1.format(interval.getBegin()), dt1.format(interval.getEnd()));
		return label;
	}

	/**
	 * Create the month based statistics
	 */
	private List<MonthStatistics> getMonthsStatistics() {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		DateTime now = new DateTime();
		List<MonthStatistics> stat = new LinkedList<MonthStatistics>();
		for (int month = 1, max = now.getMonthOfYear(); month <= max; month++) {
			DateInterval interval = DateIntervalUtils.getMonth(max - month);
			long count = apiLogService.countByInterval(interval);
			String label = dt1.format(interval.getBegin()) + "&mdash;" + dt1.format(interval.getEnd());
			stat.add(new MonthStatistics(month, label, count));
		}
		return stat;
	}

	/**
	 * Create the month based statistics for a apiKey
	 */
	private List<MonthStatistics> getMonthsByApiKeyStatistics(String apiKey) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		DateTime now = new DateTime();
		List<MonthStatistics> stat = new LinkedList<MonthStatistics>();
		for (int month = 1, max = now.getMonthOfYear(); month <= max; month++) {
			DateInterval interval = DateIntervalUtils.getMonth(max - month);
			long count = apiLogService.countByIntervalAndApiKey(interval, apiKey);
			String label = dt1.format(interval.getBegin()) + "&mdash;" + dt1.format(interval.getEnd());
			stat.add(new MonthStatistics(month, label, count));
		}
		return stat;
	}

	private List<MonthStatistics> getMonthsByRecordTypeStatistics(String recordType) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		DateTime now = new DateTime();
		List<MonthStatistics> stat = new LinkedList<MonthStatistics>();
		for (int month = 1, max = now.getMonthOfYear(); month <= max; month++) {
			DateInterval interval = DateIntervalUtils.getMonth(max - month);
			long count = apiLogService.countByIntervalAndRecordType(interval, recordType);
			String label = dt1.format(interval.getBegin()) + "&mdash;" + dt1.format(interval.getEnd());
			stat.add(new MonthStatistics(month, label, count));
		}
		return stat;
	}

	/**
	 * Returns the users with names
	 * 
	 * @param users
	 *            The users match for the criterias
	 * @param stat
	 *            The ordered map
	 * @param orderBy
	 *            The ordering key
	 * @return
	 */
	private void resolveUsers(List<UserStatistics> users, Map<Object, List<UserStatistics>> stat, String orderBy) {
		for (UserStatistics userStatistics : users) {
			String name = null;
			String wskey = userStatistics.getApiKey();
			if (wskey != null) {
				name = getUserName(wskey);
			} else {
				name = "unknown";
				userStatistics.setApiKey("-");
				wskey = "-";
				// log.warning("No wskey");
				// continue;
			}
			userStatistics.setName(name);

			Object orderByKey;
			if (orderBy.equals("count")) {
				orderByKey = userStatistics.getCount();
			} else if (orderBy.equals("apikey")) {
				orderByKey = wskey;
			} else {
				orderByKey = name;
			}

			if (!stat.containsKey(orderByKey)) {
				stat.put(orderByKey, new ArrayList<UserStatistics>());
			}
			stat.get(orderByKey).add(userStatistics);
		}
	}

	private String getUserName(String wskey) {
		StringBuilder userName = new StringBuilder("");
		ApiKey apiKey = null;
		try {
			apiKey = apiKeyService.findByID(wskey);
		} catch (DatabaseException e) {
			log.error("Database exception during retrieving apiKey: " + e.getLocalizedMessage());
		}
		if (apiKey != null) {
			User user = apiKey.getUser();
			if (user != null) {
				if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getFirstName())) {
					userName.append(user.getLastName()).append(", ").append(user.getFirstName());
				} else if (!StringUtils.isBlank(user.getEmail())) {
					userName.append(user.getEmail());
				}
			}
		} else {
			// log.warning("No API key object found for wskey: " + wskey);
		}

		if (userName.length() == 0) {
			userName.append("unknown");
		}
		return userName.toString();
	}

	private Map<Object, List<UserStatistics>> createUserStatisticsMap(String orderBy, boolean descending) {
		Map<Object, List<UserStatistics>> stat;

		if (!descending) {
			stat = new TreeMap<Object, List<UserStatistics>>();
		} else {
			if (orderBy.equals("count")) {
				stat = new TreeMap<Object, List<UserStatistics>>(new Comparator<Object>() {
					@Override
					public int compare(Object a, Object b) {
						return ((Long) b).compareTo((Long) a);
					}
				});
			} else {
				stat = new TreeMap<Object, List<UserStatistics>>(new Comparator<Object>() {
					@Override
					public int compare(Object a, Object b) {
						return ((String) b).compareTo((String) a);
					}
				});
			}
		}

		return stat;
	}

	private Map<Object, List<TypeStatistics>> createOrderedTypeMap(List<TypeStatistics> types) {
		Map<Object, List<TypeStatistics>> stat = new TreeMap<Object, List<TypeStatistics>>();

		for (TypeStatistics type : types) {
			int typeSymbol = RECORD_TYPES.containsKey(type.getRecordType()) ? RECORD_TYPES.get(type.getRecordType())
					: DEFAULT_TYPE_SYMBOL;

			if (!stat.containsKey(typeSymbol)) {
				stat.put(typeSymbol, new ArrayList<TypeStatistics>());
			}
			stat.get(typeSymbol).add(type);
		}

		long total = 0;
		for (Object typeSymbol : stat.keySet()) {
			long subTotal = 0;
			String recordType = null;
			for (TypeStatistics itemStat : stat.get(typeSymbol)) {
				total += itemStat.getCount();
				subTotal += itemStat.getCount();
				if (recordType == null) {
					recordType = itemStat.getRecordType();
				}
			}
			stat.get(typeSymbol).add(new TypeStatistics(recordType, "total", subTotal));
		}
		stat.put(LAST_TYPE_SYMBOL,
				new ArrayList<TypeStatistics>(Arrays.asList(new TypeStatistics("All record types", "total", total))));

		return stat;
	}

}
