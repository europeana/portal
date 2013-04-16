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

import java.util.Map;

import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;

public abstract class SitemapData<T> extends UrlAwareData<T> {

	private boolean showImages;

	private Map<String, Object> allObjects;

	private String prefix;

	private String title;

	private String leftContent = null;

	public void setShowImages(boolean showImages) {
		this.showImages = showImages;
	}

	public boolean isShowImages() {
		return showImages;
	}

	public void setAllObjects(Map<String, Object> allObjects) {
		this.allObjects = allObjects;
	}

	public Map<String, Object> getAllObjects() {
		return allObjects;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getLeftContent() {
		return leftContent;
	}

	public void setLeftContent(String leftContent) {
		this.leftContent = leftContent;
	}
}
