/*
 * Copyright 2007-2013 The Europeana Foundation
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

package eu.europeana.portal2.web.presentation.enums;

import org.apache.commons.lang.StringUtils;

/**
 * List of old static pages redirecting to other pages of external webpages.
 * Redirecting is done by StaticPageController.
 * 
 * @author wjboogerd
 *
 */
public enum Redirect {
	EUROPEANA_CONTRIBUTORS_HTML("/europeana-providers.html" ),
	THINKVIDEO_HTML("/storyline.html" ),
	TERMSOFUSE_HTML("/termsofservice.html"),
	TIMELINE_HISTORY_HTML("/"),
	// since 6 December 2011
	ABOUTUS_PRESS_HTML("http://pro.europeana.eu/news/press-room"),
	ABOUTUS_PRESS_SIGNUP_HTML("http://pro.europeana.eu/news/press-room"),
	ABOUTUS_BACKGROUND_HTML("http://pro.europeana.eu/about"),
	ABOUTUS_JOBS_HTML("http://pro.europeana.eu/about/jobs"),
	PARTNERS_HTML("http://pro.europeana.eu/about/partners"),
	THOUGHT_LAB_HTML("http://pro.europeana.eu/reuse/thoughtlab"),
	THOUGHTLAB_HTML("http://pro.europeana.eu/reuse/thoughtlab"),
	DATA_USAGE_GUIDE_HTML("http://pro.europeana.eu/usage-guidelines");

	private String redirect;

	private Redirect(String redirect) {
		this.redirect = redirect;
	}

	public String getRedirect() {
		return redirect;
	}

	/**
	 * Prepares and cleans the given page url to match one of the enumaration values.
	 * And try to match it, if there is no match, it gives back NULL.
	 * This is done in a NullPointer and Exception safe method.
	 * 
	 * @param url The page url to match
	 * @return The matching page, if there is one.
	 */
	public static final Redirect safeValueOf(String url) {
		if (StringUtils.isNotBlank(url)) {
			url = StringUtils.stripStart(url, "/");
			url = StringUtils.replaceChars(url, "-./", "___");
			url = StringUtils.upperCase(url);
			try {
				return valueOf(url);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
