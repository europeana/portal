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

package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.io.UnsupportedEncodingException;
import java.util.List;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.model.DocIdWindow;
import eu.europeana.portal2.web.presentation.model.DocIdWindowPager;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;

public class DocIdWindowPagerDecorator implements DocIdWindowPager {

	DocIdWindowPager pager;
	FullDocData model;

	public DocIdWindowPagerDecorator(FullDocData model) {
		this.model = model;
		this.pager = null;
	}

	public DocIdWindowPagerDecorator(FullDocData model, DocIdWindowPager pager) {
		this.model = model;
		this.pager = pager;
	}

	/**
	 * Returns the url to go back to the previous page
	 * 
	 * @return url of previous page
	 */
	public String getPagerReturnToPreviousPageUrl() throws UnsupportedEncodingException {
		if (model.getQuery() == null) {
			return null;
		}

		int maxPageResults = model.getRows();
		// the "start" param is manipulated here to ensure that the
		// "return to search results" link doesn't swallow previous results
		int pageNo = (model.getStart() - 1) / maxPageResults;
		int calcStart = 1 + (pageNo * maxPageResults);
		UrlBuilder builder = model.createSearchUrl(model.getQuery(), model.getRefinements(),
				Integer.toString(calcStart));
		builder.addParam("rows", String.valueOf(model.getRows()), true);
		return model.prepareFullDocUrl(builder).toString();
	}

	@Override
	public DocIdWindow getDocIdWindow() {
		return pager.getDocIdWindow();
	}

	@Override
	public boolean isNext() {
		return pager.isNext();
	}

	@Override
	public boolean isPrevious() {
		return pager.isPrevious();
	}

	@Override
	public String getQueryStringForPaging() {
		return pager.getQueryStringForPaging();
	}

	@Override
	public String getFullDocUri() {
		return pager.getFullDocUri();
	}

	@Override
	public String getNextFullDocUrl() {
		return pager.getNextFullDocUrl();
	}

	@Override
	public String getPreviousFullDocUrl() {
		return pager.getPreviousFullDocUrl();
	}

	@Override
	public String getNextUri() {
		return pager.getNextUri();
	}

	@Override
	public int getNextInt() {
		return pager.getNextInt();
	}

	@Override
	public String getPreviousUri() {
		return pager.getPreviousUri();
	}

	@Override
	public int getPreviousInt() {
		return pager.getPreviousInt();
	}

	@Override
	public String getQuery() {
		return pager.getQuery();
	}

	@Override
	public String getReturnToResults() {
		return pager.getReturnToResults();
	}

	@Override
	public String getPageId() {
		return pager.getPageId();
	}

	@Override
	public String getStartPage() {
		return pager.getStartPage();
	}

	@Override
	public List<BreadCrumb> getBreadcrumbs() {
		return pager.getBreadcrumbs();
	}

	@Override
	public int getFullDocUriInt() {
		return pager.getFullDocUriInt();
	}
}
