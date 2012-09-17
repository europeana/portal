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

package eu.europeana.portal2.web.model;

import eu.europeana.corelib.web.model.PageInfo;

public enum CorePageInfo implements PageInfo {

	EXCEPTION("exception.html", "Europeana - Exception", "exception/exception"),
	ERROR("exception.html", "error/error");

	private String pageName;
	private String pageTitle = "Europeana";
	private String template;

	private CorePageInfo(String pageName, String template) {
		this.pageName = pageName;
		this.template = template;
	}

	private CorePageInfo(String pageName, String pageTitle, String template) {
		this.pageName = pageName;
		this.pageTitle = pageTitle;
		this.template = template;
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
