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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.portal2.web.model.ObjectResult;
import eu.europeana.portal2.web.model.json.Json2FullBean;

public class ApiFulldocParser {
	
	private static final Logger log = Logger.getLogger(ApiFulldocParser.class.getName());

	private String apiUrl;
	private String api2key;
	private String api2secret;
	private HttpSession session;

	private int itemLimit;

	public ApiFulldocParser(String apiUrl, String api2key, String api2secret, HttpSession session) {
		this.apiUrl = apiUrl;
		this.api2key = api2key;
		this.api2secret = api2secret;
		this.session = session;
	}

	public FullBean getFullBean(String collectionId, String recordId) {
		FullBean fullBean = null;
		String json = getFullBeanAsJson(collectionId, recordId);
		fullBean = transformJsonToBean(json);
		return fullBean;
	}

	private FullBean transformJsonToBean(String json) {
		FullBean fullBean = null;
		log.info(json);
		try {
			Json2FullBean parser = new Json2FullBean(json);
			fullBean = parser.extractFullBean();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fullBean;
	}

	public String getFullBeanAsJson(String collectionId, String recordId) {
		String jsonResponse = null;
		try {
			String apiSession = null;
			if (session != null) {
				apiSession = (String) session.getAttribute("apisession");
			}
			if (apiSession == null || apiSession.equals("null")) {
				apiSession = requestApiSession();
				if (apiSession != null && session != null) {
					session.setAttribute("apisession", apiSession);
				}
			} else {
				log.info("using apiSession: " + apiSession);
			}
			
			HttpClient client = new HttpClient();
			GetMethod method = new GetMethod(apiUrl + "/record/" + collectionId + "/" + recordId + ".json");
			log.info(method.getURI().toString());
			method.setRequestHeader("Cookie", apiSession);
			
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.severe("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			StringWriter writer = new StringWriter();
			IOUtils.copy(method.getResponseBodyAsStream(), writer, "UTF-8");
			jsonResponse = writer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonResponse;
	}
	
	private String requestApiSession() {
		String apiSession = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(apiUrl + "/login");
		method.addParameter("api2key", api2key);
		method.addParameter("secret", api2secret);
		method.setFollowRedirects(false);
		try {
			client.executeMethod(method);
			apiSession = method.getResponseHeader("Set-Cookie").getValue();
			if (apiSession != null) {
				apiSession = apiSession.substring(0, apiSession.indexOf(";"));
			}
		} catch (HttpException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info("resulted apiSession: " + apiSession);
		return apiSession;
	}
}
