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

package eu.europeana.portal2.web.presentation.model.abstracts;

import java.io.UnsupportedEncodingException;

import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public abstract class UrlAwareData<T> extends ResultPageData<T> {

	abstract public UrlBuilder getPortalFormattedUrl(UrlBuilder url) throws UnsupportedEncodingException;

	abstract public UrlBuilder enrichFullDocUrl(UrlBuilder url) throws UnsupportedEncodingException;

	abstract public UrlBuilder createSearchUrl(String searchTerm, String[] qf,
			String start) throws UnsupportedEncodingException;
}
