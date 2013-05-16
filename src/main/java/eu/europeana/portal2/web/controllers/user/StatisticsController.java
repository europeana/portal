package eu.europeana.portal2.web.controllers.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.ApiLogService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.utils.DateIntervalUtils;
import eu.europeana.corelib.utils.model.DateInterval;
import eu.europeana.portal2.web.model.statistics.UserStatistics;
import eu.europeana.portal2.web.model.statistics.UserStatisticsImpl;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.StatisticsPage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class StatisticsController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource(name="apiKeyService") private ApiKeyService apiKeyService;

	@Resource private ApiLogService apiLogService;

	private static final List<String> TYPES = Arrays.asList(new String[]{"month", "date", "type", "user"});

	private static final List<String> ORDERS = Arrays.asList(new String[]{"name", "count", "apikey"});

	@RequestMapping("/admin/statistics.html")
	public ModelAndView statisticsHandler(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "order", required = false, defaultValue="name") String order,
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
			type = "date";
		}
		model.setType(type);

		if (StringUtils.isBlank(order) || !ORDERS.contains(order)) {
			order = "name";
		}
		model.setOrder(order);

		if (direction.equals("desc")) {
			model.setDescending(true);
		}

		if (type.equals("date")) {
			model.setDateStatistics(getDateStatistics());
		} else if (type.equals("type")) {
			model.setTypeStatistics(getTypeStatistics());
		} else if (type.equals("user")) {
			model.setUserStatistics(getUserStatistics(order, model.isDescending()));
		} else if (type.equals("month")) {
			model.setDateStatistics(getMonthStatistics());
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
	private Map<String, Map<String, Integer>> getTypeStatistics() {
		Map<String, Map<String, Integer>> stat = new TreeMap<String, Map<String, Integer>>();
		DBObject types = apiLogService.getStatisticsByType();
		for (String key : types.keySet()) {
			BasicDBObject item = (BasicDBObject) types.get(key);
			String recordType = item.getString("recordType");
			String profile = item.getString("profile");
			int count = item.getInt("count");

			Map<String, Integer> itemStat;
			if (stat.containsKey(recordType)) {
				itemStat = stat.get(recordType);
			} else {
				itemStat = new TreeMap<String, Integer>();
				itemStat.put("total", 0);
				stat.put(recordType, itemStat);
			}
			itemStat.put(profile, count);
			itemStat.put("total", (itemStat.get("total") + count));
		}
		return stat;
	}

	/**
	 * Create the user based statistics
	 */
	private Map<Object, List<UserStatistics>> getUserStatistics(String orderBy, boolean descending) {

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

		DBObject users = apiLogService.getStatisticsByUser();
		for (String key : users.keySet()) {
			BasicDBObject item = (BasicDBObject) users.get(key);
			String wskey = item.getString("apiKey");
			StringBuilder nameInfo = new StringBuilder();
			if (wskey != null) {
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
							nameInfo.append(user.getLastName()).append(", ").append(user.getFirstName());
						} else if (!StringUtils.isBlank(user.getEmail())) {
							nameInfo.append(user.getEmail());
						}
					}
				} else {
					log.warning("API key object found for wskey: " + wskey);
				}

				if (nameInfo.length() == 0) {
					nameInfo.append("unknown");
				}
			} else {
				log.warning("No wskey");
				nameInfo.append("unknown");
			}
			String name = nameInfo.toString();
			UserStatistics userStatistics = new UserStatisticsImpl(name, wskey, item.getLong("count"));

			Object orderByKey;
			if (orderBy.equals("count")) {
				orderByKey = item.getLong("count");
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

}
