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

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.model.PageData;
import eu.europeana.portal2.web.util.ResponsiveImageUtils;

public class CarouselItem {

	private int i;
	private String url;
	private PageData model;
	private Map<String, String> responsiveImages;

	public CarouselItem(PageData model, int i, String url) {
		this.model = model;
		this.i = i;
		this.url = url;
	}

	public String getUrl() {
		if (!isExternalLink()) {
			StringBuilder sb = new StringBuilder();
			sb.append("/").append(model.getPortalName());
			if (!StringUtils.startsWith(url, "/")) {
				sb.append("/");
			}
			sb.append(url);
			return sb.toString();
		}
		return url;
	}

	public boolean isExternalLink() {
		return StringUtils.containsIgnoreCase(url, "http://")
				|| StringUtils.containsIgnoreCase(url, "https://");
	}

	public String getAnchorUrl() {
		return String.format("notranslate_carousel-item-%d_a_url_t", i);
	}

	public String getAnchorTitle() {
		return String.format("carousel-item-%d_a_title_t", i);
	}

	public String getDescription() {
		return String.format("carousel-item-%d_a_description_t", i);
	}
	
	public String getAnchorTarget() {
		return String.format("notranslate_carousel-item-%d_a_target_t", i);
	}

	public String getImgUrl() {
		return String.format("notranslate_carousel-item-%d_img_url_t", i);
	}

	public String getImgAlt() {
		return String.format("carousel-item-%d_img_alt_t", i);
	}

	public String getImgWidth() {
		return String.format("notranslate_carousel-item-%d_img_width_t", i);
	}

	public String getImgHeight() {
		return String.format("notranslate_carousel-item-%d_img_height_t", i);
	}

	public int getId() {
		return i;
	}

	public Map<String, String> getResponsiveImages() {
		return responsiveImages;
	}

	public void setResponsiveImages(String imgUrl) {
		responsiveImages = ResponsiveImageUtils.createResponsiveImage(imgUrl.replace("//", "/"), false);
	}
}
