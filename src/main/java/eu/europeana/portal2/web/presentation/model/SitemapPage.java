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

package eu.europeana.portal2.web.presentation.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import eu.europeana.portal2.web.presentation.model.data.SitemapData;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public class SitemapPage<T> extends SitemapData<T> {

	public static final String[] HEX = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "A", "B", "C", "D", "E", "F" };

	public String[] getSitemapCols() {
		return HEX;
	}

	public String[] getSitemapRows() {
		return HEX;
	}

	@Override
	public UrlBuilder getPortalFormattedUrl(UrlBuilder url)
			throws UnsupportedEncodingException {

		// override or remove these params, because they are based on internal
		// search actions...
		if (getQuery() != null) { // prevent nullpointer on empty Query...
			url.addParam("query", URLEncoder.encode(getQuery(), "utf-8"), true);
		}
		url.addParam("qf", getRefinements(), true);
		url.removeParam("pageId");

		// remove default values to clean up url...
		url.removeDefault("start", "1");
		url.removeDefault("startPage", "1");
		url.removeDefault("embedded", "false");

		return url;
	}

	@Override
	public UrlBuilder enrichFullDocUrl(UrlBuilder builder)
			throws UnsupportedEncodingException {

		// builder.addParamsFromURL(getBriefBeanView().getPagination().getPresentationQuery().getQueryForPresentation());
		// builder.addParam("startPage", Integer.toString(getBriefBeanView().getPagination().getStart()), true);
		builder = getPortalFormattedUrl(builder);

		if (isEmbedded()) {
			builder.removeParam("embedded");
			builder.removeParam("embeddedBgColor");
			builder.removeParam("embeddedForeColor");
			builder.removeParam("embeddedLogo");
		}

		return builder;
	}

	@Override
	public UrlBuilder createSearchUrl(String searchTerm, String[] qf,
			String start) throws UnsupportedEncodingException {
		return null;
	}
}
