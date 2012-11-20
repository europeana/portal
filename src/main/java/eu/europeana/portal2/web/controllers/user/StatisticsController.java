package eu.europeana.portal2.web.controllers.user;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import eu.europeana.corelib.db.logging.api.ApiLogger;
import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.util.DateInterval;
import eu.europeana.corelib.db.util.DateUtils;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.StatisticsPage;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class StatisticsController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource(name="apiKeyService") private ApiKeyService apiKeyService;

	@Resource private ApiLogger apiLogger;

	private static final List<String> TYPES = Arrays.asList(new String[]{"month", "date", "type", "user"});

	@RequestMapping("/admin/statistics.html")
	public ModelAndView statisticsHandler(
			@RequestParam(value = "type", required = false) String type,
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

		if (type.equals("date")) {
			Map<String, Integer> stat = new TreeMap<String, Integer>();
			for (int i = 0; i < 30; i++) {
				DateInterval interval = DateUtils.getDay(i);
				String day = new SimpleDateFormat("yyyy-MM-dd").format(interval.getBegin());
				int num = apiLogger.getDaily(i);
				stat.put(day, num);
			}
			model.setDateStatistics(stat);

		} else if (type.equals("type")) {
			Map<String, Map<String, Integer>> stat = new TreeMap<String, Map<String, Integer>>();
			DBObject types = apiLogger.getByType();
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
			model.setTypeStatistics(stat);

		} else if (type.equals("user")) {
			Map<String, Integer> stat = new TreeMap<String, Integer>();
			DBObject users = apiLogger.getByUser();
			for (String key : users.keySet()) {
				BasicDBObject item = (BasicDBObject) users.get(key);
				String wskey = item.getString("apiKey");
				if (wskey != null) {
					ApiKey apiKey = apiKeyService.findByID(wskey);
					if (apiKey != null) {
						User user = apiKey.getUser();
						if (user != null) {
							wskey = user.getLastName() + ", " + user.getFirstName();
						}
					}
				} else {
					wskey = "unknown";
				}
				stat.put(wskey, item.getInt("count"));
			}
			model.setUserStatistics(stat);

		} else if (type.equals("month")) {
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			DateTime now = new DateTime();
			Map<String, Integer> stat = new LinkedHashMap<String, Integer>();
			DateInterval interval;
			for (int i = 1, max = now.getMonthOfYear(); i <= max; i++) {
				interval = DateUtils.getMonth(max - i);
				int count = apiLogger.getCountByInterval(interval);
				String key = dt1.format(interval.getBegin()) + "&mdash;" + dt1.format(interval.getEnd());
				stat.put(key, count);
			}
			model.setDateStatistics(stat);

		}

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.ADMIN_STATISTICS);
		injector.postHandle(this, page);
		return page;
	}
}
