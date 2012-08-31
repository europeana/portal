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

package eu.europeana.portal2.web.presentation.model.data.submodel;

/**
 * Placeholder objects for featured items.
 * 
 * Message keys:
 * 
 * featured-item-%d_heading_t
 * notranslate_featured-item-%d_a_url_t
 * featured-item-%d_a_title_t
 * notranslate_featured-item-%d_a_target_t
 * notranslate_featured-item-%d_img_url_t
 * featured-item-%d_img_alt_t
 * featured-item-%d_p_t
 * 
 * @author peter.kiraly@kb.nl
 */
public class FeaturedItem {

	private int i;

	public FeaturedItem(int i) {
		this.i = i;
	}

	/** Gets the message key for H3 tag */
	public String getHeading() {
		return String.format("featured-item-%d_heading_t", i);
	}

	/** Gets the URL */
	public String getAnchorUrl() {
		return String.format("notranslate_featured-item-%d_a_url_t", i);
	}

	/** Gets the message key for title */
	public String getAnchorTitle() {
		return String.format("featured-item-%d_a_title_t", i);
	}

	/** Gets the target */
	public String getAnchorTarget() {
		return String.format("notranslate_featured-item-%d_a_target_t", i);
	}

	/** Gets the image URL */
	public String getImgUrl() {
		return String.format("notranslate_featured-item-%d_img_url_t", i);
	}

	/** Gets the message key for image alt label */
	public String getImgAlt() {
		return String.format("featured-item-%d_img_alt_t", i);
	}

	/** Gets the message key for p tag */
	public String getP() {
		return String.format("featured-item-%d_p_t", i);
	}
}
