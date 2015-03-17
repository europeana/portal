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

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.model.PageData;

public class CarouselItem {

	private int id;
	private String url;
	private PageData model;
	private Map<String, String> responsiveImages;
	private Map<String, String> translatableUrls;

	public CarouselItem(PageData model, int i, String url) {
		this.model = model;
		this.id = i;
		this.url = url;
	}

	public String getUrl() {
		if (!isExternalLink()) {
			StringBuilder sb = new StringBuilder();
			
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
		return String.format("notranslate_carousel-item-%d_a_url_t", id);
	}

	public String getAnchorTitle() {
		return String.format("carousel-item-%d_a_title_t", id);
	}

	public String getDescription() {
		return String.format("carousel-item-%d_a_description_t", id);
	}

	public String getLinkDescription() {
		return String.format("carousel-item-%d_a_linkdescription_t", id);
	}

	public String getAnchorTarget() {
		return String.format("notranslate_carousel-item-%d_a_target_t", id);
	}

	public String getImgUrl() {
		return String.format("notranslate_carousel-item-%d_img_url_t", id);
	}

	public String getImgAlt() {
		return String.format("carousel-item-%d_img_alt_t", id);
	}

	public String getImgWidth() {
		return String.format("notranslate_carousel-item-%d_img_width_t", id);
	}

	public String getImgHeight() {
		return String.format("notranslate_carousel-item-%d_img_height_t", id);
	}

	public int getId() {
		return id;
	}

	public Map<String, String> getResponsiveImages() {
		return responsiveImages;
	}

	public void setResponsiveImages(Map<String, String> images) {
		responsiveImages = images;
	}

	public Map<String, String> getTranslatableUrls() {
		return translatableUrls;
	}

	public void setTranslatableUrls(Map<String, String> translatableUrls) {
		this.translatableUrls = translatableUrls;
	}
}
