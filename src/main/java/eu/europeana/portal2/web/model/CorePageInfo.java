/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "License");
 * you may not use this work except in compliance with the
 * License.
 * You may obtain a copy of the License at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the License is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
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
