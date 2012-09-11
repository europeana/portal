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

package eu.europeana.portal2.web.presentation.model.preparation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.portal2.web.presentation.Configuration;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.data.SearchEmbeddedData;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanViewDecorator;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public abstract class SearchPreparation extends SearchEmbeddedData {

	/**
	 * pack into decorator class
	 * 
	 * @param view
	 *            Original search results
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public void setBriefBeanView(BriefBeanView view)
			throws UnsupportedEncodingException {
		briefBeanView = new BriefBeanViewDecorator(this, view);
		setResults((List<BriefBean>) briefBeanView.getBriefDocs());
		setBreadcrumbs();
		setStart(view.getPagination().getStart());
		setStartPage(getBriefBeanView().getPagination().getPageNumber());
	}

	/**
	 * Returns the background color for the page. Only applicable in the
	 * embedded version
	 * 
	 * @return - background color
	 */
	public void setEmbeddedBgColor(String color) {
		embeddedBgColor = StringUtils.defaultIfEmpty(
				StringUtils.replace(color, "%23", "#"),
				Configuration.EMBEDDED_BGCOLOR);
	}

	/**
	 * Returns the color for text in the embedded version.
	 * 
	 * @return color of text
	 */
	public void setEmbeddedForeColor(String color) {
		embeddedForeColor = StringUtils.defaultIfEmpty(
				StringUtils.replace(color, "%23", "#"),
				Configuration.EMBEDDED_FORECOLOR);
	}

	private void setBreadcrumbs() throws UnsupportedEncodingException {
		if (!isEmbedded()) {
			breadcrumbs = briefBeanView.getPagination().getBreadcrumbs();
		} else {
			breadcrumbs = new ArrayList<BreadCrumb>();
			List<BreadCrumb> defaultBreadcrumbs = briefBeanView.getPagination().getBreadcrumbs();

			/**
			 * We don't want the normal default as that will remove all the
			 * default filters that have been applied
			 */
			defaultBreadcrumbs.remove(0);

			for (BreadCrumb crumb : defaultBreadcrumbs) {
				/**
				 * The only crumbs we are interested in in embed mode are the
				 * refine search crumbs. Therefore we only include these and the
				 * default query. The default query is defined by the last non
				 * refine search related crumb.
				 */
				if (crumb.getHref().contains("qf=text:")) {
					breadcrumbs.add(crumb);
				}
			}

			/**
			 * Make the default crumb the first in the list
			 */
			UrlBuilder url = new UrlBuilder(briefBeanView.getPagination().getPresentationQuery().getQueryToSave());

			/**
			 * Replace any subsequent search params with the embedded default.
			 */
			if (url.hasParam("query")) {
				String queryValue = URLEncoder.encode(getQuery(), "utf-8");
				url.addParam("query", queryValue, true);
			}
			url.removeParam("rq");
			url.removeStartWith("qf", "text:");
			BreadCrumb defaultCrumb = new BreadCrumb(url.toString(), "*:*");
			breadcrumbs.add(0, defaultCrumb);
		}
	}

}
