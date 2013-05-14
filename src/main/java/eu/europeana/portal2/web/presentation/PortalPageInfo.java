/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.presentation;

import eu.europeana.corelib.web.model.PageInfo;

/**
 * @author willem-jan boogerd
 * @author dan entous <contact@pennlinepublishing.com>
 * 
 * 
 * @param PageName
 *            string | null optional page name that will be supplied to the
 *            front-end
 * 
 * @param PageTitle
 *            string optional browser page title that will be supplied to the
 *            front-end
 * 
 * @param PageTemplate
 *            string required reference to the template directory and filename
 *            filename will have the extension .html.ftl applied to it by
 *            default
 * 
 */
public enum PortalPageInfo implements PageInfo {

	// Exceptions
	EXCEPTION("exception.html", "Europeana - Exception", "exception/exception"),
	ERROR("exception.html","error/error"),

	// Administration-related things
	ADMIN(null, "admin/admin"),
	ADMIN_STATISTICS("admin/statistics.html", "Europeana - statistics", "admin/statistics"),

	API_CONCOLE("api/console.html", "Europeana - API console", "api/console"),
	API_REQUEST("api/registration.html", "Europeana - API registration", "api/request"),
	API_REGISTER_FORM("api/registration.html", "Europeana - Registration", "api/register-api"),
	API_REGISTER_SUCCESS("api/registration-success.html", "api/register-api-success"),
	ADMIN_LIMIT_APIKEY("admin/limitApiKey.html", "Europeana - Limit API key", "admin/limit-apikey"),

	// general views
	AJAX(null, "ajax/ajax"),
	AJAX_SUGGESTION(null, "ajax/suggestions"),

	CONTACT("contact.html", "Europeana - Contact/Feedback", "contact/contact"),

	FULLDOC_HTML("full-doc.html", "Europeana - Search results", "fulldoc/fulldoc"),
	FULLDOC_EMBED_HTML("embed", "Embed Europeana object", "embed-item/embed-item"),
	FULLDOC_JSON(null, "fulldoc/full-doc-json"),
	FULLDOC_KML(null, "fulldoc/full-doc-kml"),
	FULLDOC_SRW(null, "fulldoc/full-doc-srw"),

	INDEX("index.html", "Europeana - Homepage", "index/index"),

	CAROUSEL("carousel.html", "Europeana - Carousel", "carousel/carousels"),

	MAP("map.html", "Europeana - Map", "map/map"),
	MAP_JSON("map.json", "map/map-json"),

	MYEU("myeuropeana.html", "Europeana - My Europeana", "myeuropeana/myeuropeana"), 
	MYEU_TOKEN(null, "myeuropeana/token-expired"),
	MYEU_LOGIN("login.html", "Europeana - Login", "myeuropeana/login"),
	MYEU_LOGOUT("logout.html", "Europeana - Logout", "myeuropeana/logout"),
	MYEU_PASS_CHANGE("forgotPassword.html", "Europeana - Forgot Password", "myeuropeana/change-password"),
	MYEU_PASS_CHANGED("register-success.html", "Europeana - Registration continued", "myeuropeana/change-password-success"),
	MYEU_REGISTER("register.html", "Europeana - Registration", "myeuropeana/register"),
	MYEU_REGISTERED("register-success.html", "myeuropeana/register-success"),

	NEWSLETTER("newsletter.html", "Europeana - Newsletter", "newsletter/newsletter"),

	PROVIDERS("europeana-providers.html", "Europeana - Providers", "providers/providers"),

	SEARCH_HTML("search.html", "Europeana - Search results", "search/search"),
	SEARCH_KML("search.kml", "search/search-kml"),
	SEARCH_EMBED_HTML("search.html", "Europeana - Search results", "search-embed/search-embed"),
	SEARCH_WIDGET("search-widget.html", "search-widget/search-widget"),

	SITEMAP(null, "sitemap/sitemap"),
	SITEMAP_BROWSE_INDEX(null, "sitemap/browse-all-index-page"),
	SITEMAP_BROWSE_LANDING(null, "sitemap/browse-all-landing-page"),

	SIWAMENU(null, "fulldoc/siwa-menu-with-services"),
	STATICPAGE("staticpage.html", "Europeana Page", "staticpage/staticpage"),

	TIMELINE("timeline.html", "Europeana - Timeline", "timeline/timeline"),
	TIMELINE_JSON("search.json", "timeline/timeline-json");

	private String pageName;
	private String template;
	private String pageTitle = "Europeana";

	private PortalPageInfo(String pageName, String template) {
		this.pageName = pageName;
		this.template = template;
	}

	private PortalPageInfo(String pageName, String pageTitle, String template) {
		this(pageName, template);
		this.pageTitle = pageTitle;
	}

	@Override
	public String getPageName() {
		return pageName;
	}

	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public String getPageTitle() {
		return pageTitle;
	}
}
