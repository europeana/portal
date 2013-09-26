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

package eu.europeana.portal2.web.presentation.model.abstracts;

import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.service.impl.EuropeanaUrlServiceImpl;
import eu.europeana.corelib.web.utils.UrlBuilder;

/**
 * 
 * Utilities for handling REST requests
 * 
 * @author Willem-Jan Boogerd
 * 
 */
public abstract class RestLocationsData<T> extends UrlAwareData<T> {

	protected String collectionId;
	protected String recordId;

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getResolveUri() {
		return EuropeanaUrlServiceImpl.getBeanInstance().getPortalResolve(collectionId, recordId);
	}

	@Override
	public String getMetaCanonicalUrl() {
		return EuropeanaUrlServiceImpl.getBeanInstance().getPortalRecord(false, collectionId, recordId).toString();
	}

	// /action/embed/record/[collectionId]/[recordId].html
	public String getEmbedRecordUrl() {
		UrlBuilder url = EuropeanaUrlServiceImpl.getBeanInstance().getPortalHome(false);
		url.addPath("action","embed", EuropeanaUrlService.PATH_RECORD, collectionId);
		url.addPage(recordId+EuropeanaUrlService.EXT_HTML);
		return url.toString();
	}

	// /[portalName]/action/siwa/record/[collectionId]/[recordId].html
	public String getEssUrl() {
		UrlBuilder url = EuropeanaUrlServiceImpl.getBeanInstance().getPortalHome(true);
		url.addPath("action","siwa", EuropeanaUrlService.PATH_RECORD, collectionId);
		url.addPage(recordId+EuropeanaUrlService.EXT_HTML);
		return url.toString();
	}

}