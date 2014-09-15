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

package eu.europeana.portal2.web.util.apiconsole;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.utils.EuropeanaUriUtils;
import eu.europeana.corelib.web.model.ApiResult;
import eu.europeana.corelib.web.model.ApiResultImpl;
import eu.europeana.corelib.web.service.impl.JsonApiServiceImpl;

public class ApiWrapper extends JsonApiServiceImpl {

	private final Logger log = Logger.getLogger(ApiWrapper.class);

	private static final String SEARCH_PATH = "/v2/search.json";
	private static final String SUGGESTIONS_PATH = "/v2/suggestions.json";
	private static final String RECORD_PATH = "/v2/record/";
	private static final String RECORD_EXT = ".json";

	private static final String SESSION_KEY = "apisession";

	private static final String WSKEY_PARAM = "?wskey=";
	private static final String START_PARAM = "&start=";
	private static final String ROWS_PARAM = "&rows=";
	private static final String SUGGESTION_ROWS_PARAM = "?rows=";
	private static final String PROFILE_PARAM = "&profile=";
	private static final String CALLBACK_PARAM = "&callback=";
	private static final String REUSABILITY_PARAM = "&reusability=";
	private static final String QUERY_PARAM = "&query=";
	private static final String QF_PARAM = "&qf=";
	private static final String PHRASES_PARAM = "&phrases=";
	private static final String HEADER = "{\"apikey\":\"";
	private static final String WSKEY = "wskey=";
	private static final String UTM_CAMPAIGN = "utm_campaign=";

	private String lastUrl;

	protected String apiUrl;
	protected String api2key;
	protected String api2secret;
	protected HttpSession session;
	protected String wskeyReplacement;
	protected String headerApiKey;
	protected String headerApiKeyReplacement;
	protected String wskeyInstance;
	protected String wskeyInstanceReplacement;
	protected String utmCampaign;
	protected String utmCampaignReplacement;
	private List<String> requestHeaders;
	private List<String> responseHeaders;

	public ApiWrapper(String apiUrl, String api2key, String api2secret, HttpSession session) {
		this.apiUrl = apiUrl;
		this.api2key = api2key;
		this.api2secret = api2secret;
		this.session = session;
		wskeyReplacement = api2key.replaceAll(".", "x");
		headerApiKey = HEADER + api2key + "\",";
		headerApiKeyReplacement = HEADER + wskeyReplacement + "\",";

		wskeyInstance = WSKEY + api2key;
		wskeyInstanceReplacement = WSKEY + wskeyReplacement;

		utmCampaign = UTM_CAMPAIGN + api2key;
		utmCampaignReplacement = UTM_CAMPAIGN + wskeyReplacement;
	}

	public ApiResult getSearchResult(String query, String[] refinements, String profile, 
			int start, int rows, String callback, String[] reusability) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(SEARCH_PATH);
		url.append(WSKEY_PARAM).append(api2key);
		if (!StringUtils.isBlank(query)) {
			url.append(QUERY_PARAM).append(EuropeanaUriUtils.encode(query));
		}
		if (!ArrayUtils.isEmpty(refinements)) {
			for (String qf : refinements) {
				if (!StringUtils.isBlank(qf)) {
					url.append(QF_PARAM).append(EuropeanaUriUtils.encode(qf));
				}
			}
		}

		url.append(START_PARAM).append(start);
		url.append(ROWS_PARAM).append(rows);
		url.append(PROFILE_PARAM).append(profile);
		if (ArrayUtils.isNotEmpty(reusability)) {
			for (String key : reusability) {
				url.append(REUSABILITY_PARAM).append(key);
			}
		}
		if (StringUtils.isNotBlank(callback)) {
			url.append(CALLBACK_PARAM).append(callback);
		}

		return getAndModifyJsonResponse(url.toString());
	}

	public ApiResult getObject(String collectionId, String recordId, String profile, String callback) {
		return getObject(collectionId, recordId + "/" + profile, callback);
	}

	public ApiResult getObject(String recordId, String profile, String callback) {
		if (recordId.startsWith("/")) {
			recordId = recordId.substring(1);
		}
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(RECORD_PATH);
		url.append(recordId).append(RECORD_EXT);
		url.append(WSKEY_PARAM).append(api2key);
		if (!StringUtils.isBlank(profile)) {
			url.append(PROFILE_PARAM).append(profile);
		}
		if (!StringUtils.isBlank(callback)) {
			url.append(CALLBACK_PARAM).append(callback);
		}

		return getAndModifyJsonResponse(url.toString());
	}

	public ApiResult getSuggestions(String query, int rows, boolean phrases, String callback) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append(SUGGESTIONS_PATH);
		url.append(SUGGESTION_ROWS_PARAM).append(rows);
		if (!StringUtils.isBlank(query)) {
			url.append(QUERY_PARAM).append(EuropeanaUriUtils.encode(query));
		}
		url.append(PHRASES_PARAM).append(phrases);
		if (!StringUtils.isBlank(callback)) {
			url.append(CALLBACK_PARAM).append(callback);
		}

		return getAndModifyJsonResponse(url.toString());
	}

	public ApiResult getAndModifyJsonResponse(String url) {
		lastUrl = url;
		ApiResultImpl result = (ApiResultImpl) getJsonResponse(url.toString());
		result.setContent(
			result.getContent()
				.replace(headerApiKey, headerApiKeyReplacement)
				.replace(wskeyInstance, wskeyInstanceReplacement)
				.replace(utmCampaign, utmCampaignReplacement));

		return result;
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
				log.warn("It was unsuccessfull to get apiSession");
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
				log.warn("No cookie information in API2 login response");
			}
		} catch (HttpException e) {
			log.error("HttpException: " + e.getMessage());
		} catch (IOException e) {
			log.error("IOException: " + e.getMessage());
		}
		log.info("resulted apiSession: " + apiSession);
		return apiSession;
	}

	public String getUrl() {
		return lastUrl == null ? null : lastUrl.replace(api2key, wskeyReplacement);
	}

	public List<String> getRequestHeaders() {
		return requestHeaders;
	}

	public List<String> getResponseHeaders() {
		return responseHeaders;
	}
}
