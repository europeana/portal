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

package eu.europeana.portal2.web.presentation.model.data;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public abstract class StaticData extends SearchPageData {

	private String defaultContent;
	private String headerContent;
	private String bodyContent;
	private String leftContent;
	private boolean tc;
	private boolean pageFound = true;

	public boolean getTc() {
		return tc;
	}

	public void setTc(boolean tc) {
		this.tc = tc;
	}

	public void setHeaderContent(String content) {
		this.headerContent = content;
	}

	public String getHeaderContent() {
		return headerContent;
	}

	public void setTitleContent(String content) {
		setPageTitle(content);
	}

	public String getTitleContent() {
		return getPageTitle();
	}

	public void setLeftContent(String content) {
		this.leftContent = content;
	}

	public String getLeftContent() {
		return leftContent;
	}

	public void setBodyContent(String content) {
		this.bodyContent = content;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setDefaultContent(String content) {
		this.defaultContent = content;
	}

	public String getDefaultContent() {
		return defaultContent;
	}

	public boolean isPageFound() {
		return pageFound;
	}

	public void setPageFound(boolean pageFound) {
		this.pageFound = pageFound;
	}
}
