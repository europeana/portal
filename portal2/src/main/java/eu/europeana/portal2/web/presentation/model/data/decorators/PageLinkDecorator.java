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

package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.io.UnsupportedEncodingException;

import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.model.data.SearchData;
import eu.europeana.portal2.web.presentation.model.submodel.PageLink;

public class PageLinkDecorator extends PageLink {

	SearchData model;

	public PageLinkDecorator(SearchData model, PageLink pageLink) {
		super(pageLink.getDisplay(), pageLink.getStart(), pageLink.isLinked());
		this.model = model;
	}

	public String getUrl() throws UnsupportedEncodingException {
		UrlBuilder builder = model.europeanaUrlService.getPortalSearch(true, model.getQuery(), String.valueOf(model.getRows()));
		builder.addParam("qf", model.getRefinements());
		builder.addParam("start", getStart());
		return model.getPortalFormattedUrl(builder).toString();
	}

}
