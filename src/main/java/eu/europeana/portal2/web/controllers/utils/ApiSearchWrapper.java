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

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class ApiSearchWrapper extends ApiWrapper {
	
	private final Logger log = Logger.getLogger(getClass().getName());

	public ApiSearchWrapper(String apiUrl, String api2key, String api2secret, HttpSession session) {
		super(apiUrl, api2key, api2secret, session);
	}

	public String getSearchResult(String query, String[] refinements, String profile, int start, int rows, String sort) {
		StringBuilder url = new StringBuilder(apiUrl);
		url.append("/search.json?");
		if (!StringUtils.isBlank(query)) {
			url.append("&query=").append(query);
		}
		if (!ArrayUtils.isEmpty(refinements)) {
			for (String qf : refinements) {
				if (!StringUtils.isBlank(qf)) {
					url.append("&qf=").append(qf);
				}
			}
		}
		url.append("&start=").append(start);
		url.append("&rows=").append(rows);

		return getJsonResponse(url.toString());
	}
}
