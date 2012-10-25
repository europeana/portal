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

package eu.europeana.portal2.web.controllers.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.util.QueryUtil;

public class ApiWrapper {

	private static final String SEARCH_PATH = "/v2/search.json?";
	private static final String SUGGESTIONS_PATH = "/v2/suggestions.json?";
	private static final String RECORD_PATH = "/v2/record/";
	private static final String RECORD_EXT = ".json";

	private static final String SESSION_KEY = "apisession";

	private static final String WSKEY_PARAM = "?wskey=";
	private static final String START_PARAM = "&start=";
	private static final String ROWS_PARAM = "&rows=";
	private static final String PROFILE_PARAM = "&profile=";
	private static final String CALLBACK_PARAM = "&callback=";
	private static final String QUERY_PARAM = "&query=";
	private static final String QF_PARAM = "&qf=";
	private static final String PHRASES_PARAM = "&phrases=";

	private final Logger log = Logger.getLogger(getClass().getName());

	private String lastUrl;

	protected String apiUrl;
	protected String api2key;
	protected String api2secret;
	protected HttpSession session;

	public ApiWrapper(String apiUrl, String api2key, String api2secret, HttpSession session) {
		this.apiUrl = apiUrl;
		this.api2key = api2key;
		this.api2secret = api2secret;
		this.session = session;
	}

	public String getSearchResult(String query, String[] refinements, String profile, int start, int rows, String sort, String callback) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(SEARCH_PATH);
		url.append(WSKEY_PARAM).append(api2key);
		if (!StringUtils.isBlank(query)) {
			url.append(QUERY_PARAM).append(QueryUtil.encode(query));
		}
		if (!ArrayUtils.isEmpty(refinements)) {
			for (String qf : refinements) {
				if (!StringUtils.isBlank(qf)) {
					url.append(QF_PARAM).append(QueryUtil.encode(qf));
				}
			}
		}
		url.append(START_PARAM).append(start);
		url.append(ROWS_PARAM).append(rows);
		url.append(PROFILE_PARAM).append(profile);
		url.append(CALLBACK_PARAM).append(callback);

		return getJsonResponse(url.toString());
	}

	public String getObject(String collectionId, String recordId, String callback) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(RECORD_PATH);
		url.append(collectionId).append("/").append(recordId).append(RECORD_EXT);
		url.append(WSKEY_PARAM).append(api2key);
		if (!StringUtils.isBlank(callback)) {
			url.append(CALLBACK_PARAM).append(callback);
		}

		return getJsonResponse(url.toString());
	}

	public String getObject(String recordId, String callback) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(RECORD_PATH);
		url.append(recordId).append(RECORD_EXT);
		url.append(WSKEY_PARAM).append(api2key);
		if (!StringUtils.isBlank(callback)) {
			url.append(CALLBACK_PARAM).append(callback);
		}

		return getJsonResponse(url.toString());
	}

	public String getSuggestions(String query, int rows, boolean phrases, String callback) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(SUGGESTIONS_PATH);
		if (!StringUtils.isBlank(query)) {
			url.append(QUERY_PARAM).append(QueryUtil.encode(query));
		}
		url.append(ROWS_PARAM).append(rows);
		url.append(PHRASES_PARAM).append(phrases);
		if (!StringUtils.isBlank(callback)) {
			url.append(CALLBACK_PARAM).append(callback);
		}

		return getJsonResponse(url.toString());
	}

	public String getJsonResponse(String url) {
		lastUrl = url;
		String jsonResponse = null;
		try {
			/*
			String sessionId = getSessionID();
			if (sessionId == null) {
				return null;
			}
			*/

			HttpClient client = new HttpClient();
			GetMethod method = new GetMethod(url);
			log.info("get URL: " + method.getURI().toString());
			// method.setRequestHeader("Cookie", sessionId);

			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.severe("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			jsonResponse = writer.toString();
		} catch (IOException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}

		return jsonResponse;
	}

	protected String getSessionID() {
		String sessionId = null;
		if (session != null) {
			sessionId = (String) session.getAttribute(SESSION_KEY);
		}

		if (sessionId == null || sessionId.equals("null")) {
			sessionId = requestApiSession();
			if (sessionId != null && session != null) {
				session.setAttribute(SESSION_KEY, sessionId);
			} else {
				log.severe("It was unsuccessfull to get apiSession");
			}
		}

		return sessionId;
	}

	private String requestApiSession() {
		final String url = apiUrl + "/login";
		String apiSession = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.addParameter("api2key", api2key);
		method.addParameter("secret", api2secret);
		method.setFollowRedirects(false);
		try {
			client.executeMethod(method);
			apiSession = method.getResponseHeader("Set-Cookie").getValue();
			if (apiSession != null) {
				apiSession = apiSession.substring(0, apiSession.indexOf(";"));
			} else {
				log.severe("No cookie information in API2 login response");
			}
		} catch (HttpException e) {
			log.severe("HttpException: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		log.info("resulted apiSession: " + apiSession);
		return apiSession;
	}

	public String getUrl() {
		return lastUrl;
	}
}
