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

public class FeaturedPartner {

	private int i;

	public FeaturedPartner(int i) {
		this.i = i;
	}

	public String getH2() {
		return String.format("featured-partner-%d_h2_t", i);
	}

	public String getH3() {
		return String.format("featured-partner-%d_h3_t", i);
	}

	public String getAnchorUrl() {
		return String.format("notranslate_featured-partner-%d_a_url_t", i);
	}

	public String getAnchorTitle() {
		return String.format("featured-partner-%d_a_title_t", i);
	}

	public String getAnchorTarget() {
		return String.format("notranslate_featured-partner-%d_a_target_t", i);
	}

	public String getImgUrl() {
		return String.format("notranslate_featured-partner-%d_img_url_t", i);
	}

	public String getImgAlt() {
		return String.format("featured-partner-%d_img_alt_t", i);
	}

	public String getImgWidth() {
		return String.format("notranslate_featured-partner-%d_img_width_t", i);
	}

	public String getImgHeight() {
		return String.format("notranslate_featured-partner-%d_img_height_t", i);
	}

	public String getP() {
		return String.format("featured-partner-%d_p_t", i);
	}
}
