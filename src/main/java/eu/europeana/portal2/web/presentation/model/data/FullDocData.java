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

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.FullBeanView;
import eu.europeana.portal2.web.presentation.model.abstracts.RestLocationsData;
import eu.europeana.portal2.web.presentation.model.data.decorators.FullBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.FullBeanViewDecorator;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public abstract class FullDocData extends RestLocationsData<Void> {

	protected FullBeanView fullBeanView;

	protected FullBean document;

	protected boolean showFields = false;

	abstract public UrlBuilder prepareFullDocUrl(UrlBuilder builder);

	private String jsonCallback;

	private String format;

	protected SearchPageEnum returnTo = SearchPageEnum.getDefault();

	protected String[] shownAtProviderOverride;

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setFullBeanView(FullBeanView fullBeanView)
			throws EuropeanaQueryException {
		this.fullBeanView = fullBeanView;
		this.document = fullBeanView.getFullDoc();
	}

	public FullBeanViewDecorator getFullBeanView() {
		return new FullBeanViewDecorator(this, fullBeanView);
	}

	public void setShowFields(boolean showFields) {
		this.showFields = showFields;
	}

	public boolean isShowFields() {
		return showFields;
	}

	public FullBeanDecorator getDocument() {
		return new FullBeanDecorator(document);
	}

	public void setJsonCallback(String jsonCallback) {
		this.jsonCallback = jsonCallback;
	}

	public String getJsonCallback() {
		return jsonCallback;
	}

	public void setReturnTo(SearchPageEnum returnTo) {
		this.returnTo = returnTo;
	}

	public void setShownAtProviderOverride(String[] shownAtProviderOverride) {
		this.shownAtProviderOverride = shownAtProviderOverride;
	}
}
