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

package eu.europeana.portal2.web.presentation.model.data.submodel;

import org.apache.commons.lang.StringUtils;

/**
 * Sitemap entry, model for MVC.
 */
public class SitemapEntry {

	private String loc;
	private String image;
	private String title;
	private String priority;
	private int intPriority;

	public String getLoc() {
		return getLoc(false);
	}

	public String getLoc(boolean isPlaceSitemap) {
		return isPlaceSitemap ? StringUtils.replace(loc, ".html", ".kml") : loc;
	}

	public String getImage() {
		return image;
	}

	public String getPriority() {
		return priority;
	}

	public String getTitle() {
		return title;
	}

	public SitemapEntry(String loc, String image, String title, int priority) {
		super();
		this.loc = loc;
		this.image = image;
		this.title = StringUtils.replace(title, "\n", " ");
		this.priority = priority > 9 ? "1.0" : ("0." + priority);
		intPriority = priority;
	}

	final int minFieldLength = 5;

	public boolean isCompletedEnoughForSitemap() {
		return intPriority > 2 && loc != null && image != null && title != null
				&& loc.length() > minFieldLength
				&& image.length() > minFieldLength
				&& title.length() > minFieldLength
				// known problem
				&& !image.endsWith("[sti til fillager]");
	}

	public void completeWithDefaults() {
		if (loc == null || loc.length() < minFieldLength) {
			loc = "http://www.europeana.eu/";
		}
		if (title == null || title.length() < minFieldLength) {
			title = "no title";
		}
		if (image == null || image.length() < minFieldLength) {
			image = "http://api.europeana.eu/api/image?uri=%20&size=BRIEF_DOC&type=IMAGE";
		}
		priority = "0.0";
	}
}