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

/**
 * 
 * Utilities for handling REST requests
 * 
 * @author Willem-Jan Boogerd
 * @author Borys Omelayenko
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
		StringBuilder sb = new StringBuilder();
		sb.append("http://www.europeana.eu/resolve/record/");
		sb.append(collectionId);
		sb.append("/");
		sb.append(recordId);
		return sb.toString();
	}

	@Override
	// [server]/[portalName]/record/[collectionId]/[recordId].html
	public String getMetaCanonicalUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPortalServer());
		sb.append(getPortalName());
		sb.append("/record/");
		sb.append(collectionId);
		sb.append("/");
		sb.append(recordId);
		sb.append(".html");
		return sb.toString();
	}

	// /action/embed/record/[collectionId]/[recordId].html
	public String getEmbedRecordUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPortalServer());
		sb.append(getPortalName());
		sb.append("/action/embed/record/");
		sb.append(collectionId);
		sb.append("/");
		sb.append(recordId);
		sb.append(".html");
		return sb.toString();
	}

	// /record/[collectionId]/[recordId].png
	public String getCanonicalPng() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPortalServer());
		sb.append(getPortalName());
		sb.append("/record/");
		sb.append(collectionId);
		sb.append("/");
		sb.append(recordId);
		sb.append(".png");
		return sb.toString();
	}

	// /[portalName]/action/siwa/record/[collectionId]/[recordId].html
	public String getEssUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		sb.append(getPortalName());
		sb.append("/action/siwa/record/");
		sb.append(collectionId);
		sb.append("/");
		sb.append(recordId);
		sb.append(".html");
		return sb.toString();
	}

}