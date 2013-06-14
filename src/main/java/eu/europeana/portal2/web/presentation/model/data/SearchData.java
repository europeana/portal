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

package eu.europeana.portal2.web.presentation.model.data;

import java.util.List;
import java.util.logging.Logger;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanViewDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.lists.BreadcrumbListDecorator;

public abstract class SearchData extends UrlAwareData<BriefBean> {

	private final Logger log = Logger.getLogger(getClass().getName());

	protected List<BreadCrumb> breadcrumbs;

	protected BriefBeanViewDecorator briefBeanView;

	private SearchPageEnum currentSearch; // = SearchPage.getDefault();

	private int startPage;

	private String refineKeyword;

	protected String coords;

	protected int maxMapResults;

	protected boolean markupOnly;
	
	abstract public String getRswUserId();

	abstract public String getRswDefqry();

	public int getMaxMapResults() {
		return maxMapResults;
	}

	public void setMaxMapResults(int val) {
		maxMapResults = val;
	}

	public BriefBeanViewDecorator getBriefBeanView() {
		return briefBeanView;
	}

	public List<? extends BreadCrumb> getBreadcrumbs() {
		if (breadcrumbs != null) {
			BreadcrumbListDecorator breadcrumbListDecorator = new BreadcrumbListDecorator(this, breadcrumbs);
			return breadcrumbListDecorator.asDecoList();
		}
		return null;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public String getRefineKeyword() {
		return refineKeyword;
	}

	public void setRefineKeyword(String refineKeyword) {
		this.refineKeyword = refineKeyword;
	}

	public SearchPageEnum getCurrentSearch() {
		return currentSearch;
	}

	public void setCurrentSearch(SearchPageEnum currentSearch) {
		this.currentSearch = currentSearch;
	}

	public String getCoords() {
		return coords;
	}

	public void setCoords(String coords) {
		this.coords = coords;
	}
	
	public boolean isMarkupOnly() {
		return markupOnly;
	}

	public void setMarkupOnly(boolean markupOnly) {
		this.markupOnly = markupOnly;
	}

}
