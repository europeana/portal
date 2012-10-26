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
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.portal2.web.model.json.Json2FullBean;

public class ApiFulldocParser extends ApiWrapper {

	private final Logger log = Logger.getLogger(getClass().getName());

	public ApiFulldocParser(String apiUrl, String api2key, String api2secret, HttpSession session) {
		super(apiUrl, api2key, api2secret, session);
	}

	public FullBean getFullBean(String collectionId, String recordId) {
		FullBean fullBean = null;
		String json = getObject(collectionId, recordId, null);
		if (json == null) {
			return fullBean;
		}
		fullBean = transformJsonToBean(json);
		json = null;
		return fullBean;
	}

	private FullBean transformJsonToBean(String json) {
		FullBean fullBean = null;
		try {
			Json2FullBean parser = new Json2FullBean(json);
			fullBean = parser.extractFullBean();
		} catch (JsonParseException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		}
		return fullBean;
	}
}
