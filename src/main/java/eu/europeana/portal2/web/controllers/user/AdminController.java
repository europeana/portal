package eu.europeana.portal2.web.controllers.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.ApiKeyService;
import eu.europeana.corelib.db.service.ApiLogService;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.utils.DateIntervalUtils;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.AdminPage;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

/**
 * 
 * Show monitoring and administration info.
 * 
 * @author Borys Omelayenko
 */

@Controller
public class AdminController {

	@Resource(name="corelib_db_userService") private UserService userService;

	@Resource(name="configurationService") private Configuration config;

	@Resource(name="apiKeyService") private ApiKeyService apiKeyService;

	@Resource private ClickStreamLogger clickStreamLogger;

	@Resource private ApiLogService apiLogService;

	private final Logger log = Logger.getLogger(getClass().getName());

	private static final String RECORD_SEPARATOR = "\n";
	private static final String FIELD_SEPARATOR = ",";

	private static final String ACTUAL = "actual";
	private static final String TOTAL = "total";
	private static final int LIMIT = 50;

	@RequestMapping("/admin.html")
	public ModelAndView adminHandler(
			@RequestParam(value = "page", required = false, defaultValue="1") int pageNr,
			HttpServletRequest request,
			HttpServletResponse response)
					throws Exception {
		log.info("==== admin.html ====");
		Injector injector = new Injector(request, response, null);

		int offset = ((pageNr-1) * LIMIT);
		AdminPage model = new AdminPage();
		model.setPageNr(pageNr);
		model.setTheme("devel");
		injector.injectProperties(model);

		long t0 = new Date().getTime();
		// model.setUsers(userService.findAll());
		Map<String, Map<String, Long>> usage = new HashMap<String, Map<String, Long>>(){
			private static final long serialVersionUID = 1L;
		{
			put(ACTUAL, new HashMap<String, Long>());
			put(TOTAL, new HashMap<String, Long>());
		}};

		List<User> users = new ArrayList<User>();
		long count = apiKeyService.count();
		model.setApiKeyCount(count);

		List<ApiKey> apiKeys = apiKeyService.findAllSortByDate(false, offset, LIMIT);
		for (ApiKey apiKey : apiKeys) {
			User user = apiKey.getUser();
			if (!users.contains(user)) {
				users.add(user);
			}
		}

		// Requests API key statistics
		for (User user : users) {
			for (ApiKey apiKey : user.getApiKeys()) {
				getUsageByApiKey(usage, apiKey.getId());
			}
		}

		long t = (new Date().getTime() - t0);
		log.info("get users took " + t);
		model.setUsers(users);
		model.setUsage(usage);
		List<Integer> pageNumbers = new ArrayList<Integer>();
		for (int i = 1; ((i-1)*LIMIT)+1 < count; i++) {
			pageNumbers.add(i);
		}
		model.setPageNumbers(pageNumbers);

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, PortalPageInfo.ADMIN);
		injector.postHandle(this, page);
		return page;
	}

	@RequestMapping("/admin/removeUser.html")
	public String removeUserHandler(
			@RequestParam(value = "id", required = true) long id,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin.html ====");

		User user = userService.findByID(id);
		userService.remove(user);

		return "redirect:/admin.html";
	}

	/*
	@RequestMapping("/admin/blockUser.html")
	public String blockUserHandler(
			@RequestParam(value = "id", required = true) int id,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin.html ====");

		User user = userService.findByID(id);
		user.setEnabled(false);
		userService.store(user);

		return "redirect:/admin.html";
	}
	*/

	@RequestMapping("/admin/removeApiKey.html")
	public String removeApiKeyHandler(
			@RequestParam(value = "userId", required = true) long userId,
			@RequestParam(value = "apiKey", required = true) String apiKey,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin/removeApiKey.html ====");
		log.info(String.format("%s, %s", userId, apiKey));

		userService.removeApiKey(userId, apiKey);

		return "redirect:/admin.html";
	}

	/**
	 * Export API users to comma separated list
	 * 
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/exportUsers.html", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String exportUsersHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		log.info("==== admin/exportUsers.html ====");
		response.setHeader("Content-Type", "text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"users_with_apikeys.csv\"");

		Map<Long, User> users = new LinkedHashMap<Long, User>();
		List<ApiKey> apiKeys = apiKeyService.findAllSortByDate(true);
		for (ApiKey apiKey : apiKeys) {
			User user = apiKey.getUser();
			if (!users.containsKey(user.getId())) {
				users.put(user.getId(), user);
			}
		}

		StringBuilder sb = new StringBuilder();
		List<String> fieldNames = new LinkedList<String>(Arrays.asList(
			"id", "Date of registration", "First name", "Last name", "Email", "Company", "Country",
			"Address", "Phone", "Website", "Field of work", "Number of keys", "Keys (limit)"
		));
		sb.append(csvEncodeRecord(fieldNames));
		for (User user : users.values()) {
			sb.append(csvEncodeRecord(csvEncodeUser(user)));
		}
		return sb.toString();
	}

	/**
	 * Create an CSV encoded record (list of fields) from a User object
	 * @param user
	 * @return
	 */
	private List<String> csvEncodeUser(User user) {
		List<String> fields = new LinkedList<String>();
		fields.add(user.getId().toString());
		if (user.getRegistrationDate() != null) {
			fields.add(csvEncodeField(new SimpleDateFormat("yyyy-MM-dd").format(user.getRegistrationDate())));
		} else {
			fields.add("");
		}
		fields.add(csvEncodeField(user.getFirstName()));
		fields.add(csvEncodeField(user.getLastName()));
		fields.add(csvEncodeField(user.getEmail()));
		fields.add(csvEncodeField(user.getCompany()));
		fields.add(csvEncodeField(user.getCountry()));
		fields.add(csvEncodeField(user.getAddress()));
		fields.add(csvEncodeField(user.getPhone()));
		fields.add(csvEncodeField(user.getWebsite()));
		fields.add(csvEncodeField(user.getFieldOfWork()));
		fields.add(String.valueOf(user.getApiKeys().size()));
		List<String> keys = new LinkedList<String>();
		for (ApiKey key : user.getApiKeys()) {
			keys.add(key.getId() + " (" + key.getUsageLimit() + ")");
		}
		fields.add(csvEncodeField(StringUtils.join(keys, ", ")));
		return fields;
	}

	/**
	 * Encode a field for usage in CSV
	 * @param field
	 * @return
	 */
	private String csvEncodeField(String field) {
		if (StringUtils.isBlank(field)) {
			return "";
		}
		if (field.indexOf('"') > -1) {
			field = field.replaceAll("\"", "\"\"");
		}
		field = '"' + field + '"';
		return field;
	}

	/**
	 * Encode a record (list of fields) for usage in CSV
	 */
	private String csvEncodeRecord(List<String> fields) {
		return StringUtils.join(fields, FIELD_SEPARATOR) + RECORD_SEPARATOR;
	}

	/**
	 * Gets and saves the total and actual usage by API key
	 *
	 * @param usage
	 *   A map for collecting the usage statistics
	 * @param apiKey
	 *   The API key
	 */
	private void getUsageByApiKey(Map<String, Map<String, Long>> usage, String apiKey) {
		long actual = apiLogService.countByApiKeyByInterval(apiKey, DateIntervalUtils.getToday());
		long total = apiLogService.countByApiKey(apiKey);
		usage.get(ACTUAL).put(apiKey, actual);
		usage.get(TOTAL).put(apiKey, total);
	}
}
