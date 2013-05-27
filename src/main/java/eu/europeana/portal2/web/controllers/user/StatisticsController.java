package eu.europeana.portal2.web.controllers.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.ApiLogService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.model.statistics.TypeStatistics;
import eu.europeana.corelib.definitions.model.statistics.UserStatistics;
import eu.europeana.corelib.utils.DateIntervalUtils;
import eu.europeana.corelib.utils.model.DateInterval;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.StatisticsPage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class StatisticsController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource(name="apiKeyService") private ApiKeyService apiKeyService;

	@Resource private ApiLogService apiLogService;

	private static final List<String> TYPES = Arrays.asList(new String[]{
		"dates",
		"months", "recordTypes", "apiKeys", 
		"month",  "recordType",  "apiKey",
		"monthsByUser", "usersByMonth", "usersByRecordType", "recordTypesByuser"
	});

	private static final List<String> ORDERS = Arrays.asList(new String[]{"name", "count", "apikey"});

	private static final Map<String, Integer> RECORD_TYPES = new HashMap<String, Integer>(){
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
	public ModelAndView statisticsHandler(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "month", required = false, defaultValue="0") int month,
			@RequestParam(value = "order", required = false, defaultValue="name") String order,
			@RequestParam(value = "recordType", required = false, defaultValue="SEARCH") String recordType,
			@RequestParam(value = "apiKey", required = false, defaultValue="") String apiKey,
			@RequestParam(value = "dir", required = false, defaultValue="") String direction,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin/statistics.html ====");
		Injector injector = new Injector(request, response, locale);
		StatisticsPage model = new StatisticsPage();
		injector.injectProperties(model);
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
			model.setDateStatistics(getDateStatistics());
		} else if (type.equals("months")) {
			model.setMonthStatistics(getMonthsStatistics());
		} else if (type.equals("recordTypes")) {
			model.setTypeStatistics(getTypeStatistics());
		} else if (type.equals("apiKeys")) {
			model.setUserStatistics(getUserStatistics(order, model.isDescending()));
		} else if (type.equals("month")) {
			model.setMonthLabel(getMonthLabel(month));
			model.setUserStatistics(getUsersByMonthStatistics(month, order, model.isDescending()));
			model.setTypeStatistics(getRecordTypesByMonthStatistics(month));
		} else if (type.equals("recordType")) {
			model.setRecordType(recordType);
			model.setUserStatistics(getUsersByRecordTypeStatistics(recordType, order, model.isDescending()));
			model.setMonthStatistics(getMonthsByRecordTypeStatistics(recordType));
		} else if (type.equals("apiKey")) {
			model.setApiKey(apiKey);
			model.setUserName(getUserName(apiKey));
			model.setTypeStatistics(getRecordTypesByUserStatistics(apiKey));
			model.setMonthStatistics(getMonthsByApiKeyStatistics(apiKey));
		// legacy code
		} else if (type.equals("usersByMonth")) {
			model.setMonthLabel(getMonthLabel(month));
			model.setUserStatistics(getUsersByMonthStatistics(month, order, model.isDescending()));
		} else if (type.equals("usersByRecordType")) {
			model.setRecordType(recordType);
			model.setUserStatistics(getUsersByRecordTypeStatistics(recordType, order, model.isDescending()));
		}

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.ADMIN_STATISTICS);
		injector.postHandle(this, page);
		return page;
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

	private Map<Object, List<TypeStatistics>> getRecordTypesByUserStatistics(
			String apiKey) {
		List<TypeStatistics> types = apiLogService.getStatisticsForRecordTypesByUser(apiKey);

		return createOrderedTypeMap(types);
	}

	private Map<Object, List<TypeStatistics>> getRecordTypesByMonthStatistics(
			int month) {
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

	private Map<Object, List<UserStatistics>> getUsersByRecordTypeStatistics(
			String recordType, String orderBy, boolean descending) {
		Map<Object, List<UserStatistics>> stat = createUserStatisticsMap(orderBy, descending);
		List<UserStatistics> users = apiLogService.getStatisticsForUsersByRecordType(recordType);
		resolveUsers(users, stat, orderBy);
		return stat;
	}

	private String getMonthLabel(int month) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		DateTime now = new DateTime();
		DateInterval interval = DateIntervalUtils.getMonth(now.getMonthOfYear() - month);
		String label = String.format("%s&mdash;%s" ,  dt1.format(interval.getBegin()), dt1.format(interval.getEnd()));
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
			long count =  apiLogService.countByInterval(interval);
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

	private List<MonthStatistics> getMonthsByRecordTypeStatistics(
			String recordType) {
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
	 * Create the month based statistics
	 */
	private Map<String, Long> getMonthStatistics() {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		DateTime now = new DateTime();
		Map<String, Long> stat = new LinkedHashMap<String, Long>();
		for (int i = 1, max = now.getMonthOfYear(); i <= max; i++) {
			DateInterval interval = DateIntervalUtils.getMonth(max - i);
			long count =  apiLogService.countByInterval(interval);
			String key = dt1.format(interval.getBegin()) + "&mdash;" + dt1.format(interval.getEnd());
			stat.put(key, count);
		}
		return stat;
	}

	/**
	 * Returns the users with names
	 *
	 * @param users
	 *   The users match for the criterias
	 * @param stat
	 *   The ordered map
	 * @param orderBy
	 *   The ordering key
	 * @return
	 */
	private void resolveUsers(List<UserStatistics> users, Map<Object, List<UserStatistics>> stat, String orderBy) {
		for (UserStatistics userStatistics : users) {
			String name = null;
			String wskey = userStatistics.getApiKey();
			if (wskey != null) {
				name = getUserName(wskey);
			} else {
				log.warning("No wskey");
				continue;
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
			log.severe("Database exception during retrieving apiKey: " + e.getLocalizedMessage());
		}
		if (apiKey != null) {
			User user = apiKey.getUser();
			if (user != null) {
				if (!StringUtils.isBlank(user.getLastName())
						&& !StringUtils.isBlank(user.getFirstName())) {
					userName.append(user.getLastName()).append(", ").append(user.getFirstName());
				} else if (!StringUtils.isBlank(user.getEmail())) {
					userName.append(user.getEmail());
				}
			}
		} else {
			log.warning("API key object found for wskey: " + wskey);
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
					public int compare(Object a, Object b) {
						return ((Long)b).compareTo((Long)a);
					}
				});
			} else {
				stat = new TreeMap<Object, List<UserStatistics>>(new Comparator<Object>() {
					public int compare(Object a, Object b) {
						return ((String)b).compareTo((String)a);
					}
				});
			}
		}

		return stat;
	}

	private Map<Object, List<TypeStatistics>> createOrderedTypeMap(
			List<TypeStatistics> types) {
		Map<Object, List<TypeStatistics>> stat = new TreeMap<Object, List<TypeStatistics>>();

		for (TypeStatistics type : types) {
			int typeSymbol = RECORD_TYPES.containsKey(type.getRecordType())
							? RECORD_TYPES.get(type.getRecordType())
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
		stat.put(LAST_TYPE_SYMBOL, new ArrayList<TypeStatistics>(Arrays.asList(new TypeStatistics("All record types", "total", total))));

		return stat;
	}

}
