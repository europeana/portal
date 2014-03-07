package eu.europeana.portal2.web.presentation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import eu.europeana.corelib.web.model.PageData;

public abstract class PortalPageData extends PageData {

	private HttpServletRequest request;

	private String theme = "default";

	private String googlePlusPublisherId;

	private List<String> messages = null;

	private boolean useCache = true;

	private boolean debug = false;

	private boolean minify = false;

	private Locale locale;
	
	private String browserLanguage;
	
	private String[] localeMessages;

	private String portalUrl;
	
	private String blogFeedUrl;

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setGooglePlusPublisherId(String googlePlusPublisherId) {
		this.googlePlusPublisherId = googlePlusPublisherId;
	}

	public String getGooglePlusPublisherId() {
		return googlePlusPublisherId;
	}

	public void addMessage(String message) {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		messages.add(message);
	}

	public List<String> getMessages() {
		return messages;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean isMinify() {
		return minify;
	}

	@Override
	public void setMinify(boolean minify) {
		this.minify = minify;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public String getBlogFeedUrl() {
		return blogFeedUrl;
	}

	public void setBlogFeedUrl(String blogFeedUrl) {
		this.blogFeedUrl = blogFeedUrl;
	}

	public String[] getLocaleMessages() {
		return localeMessages;
	}

	public void setLocaleMessages(String[] localeMessage) {
		this.localeMessages = localeMessage;
	}
	
	public String getBrowserLanguage() {
		return browserLanguage;
	}
	
	public void setBrowserLanguage(String browserLanguage) {
		this.browserLanguage = browserLanguage;
	}
}
