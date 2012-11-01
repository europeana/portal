package eu.europeana.portal2.web.util;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.corelib.web.interceptor.LocaleInterceptor;
import eu.europeana.portal2.services.Configuration;

/**
 * This class collects some important beans, and made them available for other classes.
 * 
 * @author peter.kiraly@kb.nl
 */
public class Beans {

	/**
	 * corelib_db_userService
	 */
	private static UserService userService;

	/**
	 * corelib_web_configInterceptor
	 */
	private static ConfigInterceptor configInterceptor;

	/**
	 * configurationService
	 */
	private static Configuration config;

	/**
	 * localeChangeInterceptor
	 */
	private static LocaleInterceptor localeChangeInterceptor;

	public Beans() {}

	public static ConfigInterceptor getConfigInterceptor() {
		return configInterceptor;
	}

	public static void setConfigInterceptor(ConfigInterceptor configInterceptor) {
		Beans.configInterceptor = configInterceptor;
	}

	public static UserService getUserService() {
		return userService;
	}

	public static void setUserService(UserService userService) {
		Beans.userService = userService;
	}

	public static Configuration getConfig() {
		return config;
	}

	public static void setConfig(Configuration config) {
		Beans.config = config;
	}

	public static LocaleInterceptor getLocaleChangeInterceptor() {
		return localeChangeInterceptor;
	}

	public static void setLocaleChangeInterceptor(LocaleInterceptor localeChangeInterceptor) {
		Beans.localeChangeInterceptor = localeChangeInterceptor;
	}
}
