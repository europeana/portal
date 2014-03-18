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

package eu.europeana.portal2.web.presentation;

public enum SearchPageEnum {

	SEARCH_HTML(PortalPageInfo.SEARCH_HTML),
	TIMELINE(PortalPageInfo.TIMELINE),
	MAP(PortalPageInfo.MAP);

	private PortalPageInfo pageInfo;

	SearchPageEnum(PortalPageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public PortalPageInfo getPageInfo() {
		return pageInfo;
	}

	public String getPageName() {
		return pageInfo.getPageName();
	}

	public static SearchPageEnum getDefault() {
		return SEARCH_HTML;
	}
}
