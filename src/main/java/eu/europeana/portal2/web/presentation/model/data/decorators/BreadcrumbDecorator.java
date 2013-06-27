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

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.Configuration;
import eu.europeana.portal2.web.presentation.model.data.SearchData;
import eu.europeana.portal2.web.util.QueryUtil;

public class BreadcrumbDecorator extends BreadCrumb {

	private SearchData model;

	public BreadcrumbDecorator(SearchData model, BreadCrumb breadcrumb) {
		super(breadcrumb.getDisplay(), breadcrumb.getParam(), breadcrumb.getValue(), breadcrumb.getHref());
		if (breadcrumb.isLast()) {
			markAsLast();
		}
		this.model = model;
	}

	@Override
	public String getDisplay() {
		String display = super.getDisplay();
		if (getParam().equals("qf")) {
			return QueryUtil.removeQuotes(display);
		}
		else {
			return display;
		}
	}

	/**
	 * Determines whether or not to show a navigation breadcrumb. In the case of
	 * embedded we only want to show the search terms and not the facets.
	 * 
	 * @param crumbName
	 *            - Display text of the breadcrumb
	 * @return
	 */
	public boolean isShowBreadCrumb() {
		return !model.isEmbedded()
				|| !StringUtils.contains(getDisplay(), Configuration.FACET_TYPE);
	}

	/**
	 * Returns the url of a specific breadcrumn. breadcrumbs being the links to
	 * go back to more generic search results where filters have been applied to
	 * a search criteria
	 * 
	 * @return - Formatted url of the breadcrumb
	 * @throws UnsupportedEncodingException
	 */
	public String getBreadCrumbUrl() throws UnsupportedEncodingException {
		StringBuilder url = new StringBuilder();
		url.append(model.getPageName()).append("?").append(getHref());
		return model.getPortalFormattedUrl(new UrlBuilder(url.toString())).toString();
	}

	public boolean getIsLast() {
		return isLast();
	}
}