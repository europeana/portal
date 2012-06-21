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

import eu.europeana.portal2.querymodel.query.RightsOption;

public class RightsValue {

	private RightsOption right;

	private String url;

	public RightsValue(RightsOption right, String url) {
		this.right = right;
		this.url = url;
	}

	public static final RightsValue safeValueByUrl(String url) {
		RightsOption right = RightsOption.safeValueByUrl(url);
		if (right != null) {
			return new RightsValue(right, url);
		}
		return null;
	}

	/**
	 * Returns short version of url associated with the rights
	 * 
	 * @return Short version of URL associated with the results
	 */
	public String getRightsUrl() {
		final String RIGHTS_URL_IDENTIFIER_LONG = "http://www.europeana.eu/rights/";
		final String RIGHTS_URL_IDENTIFIER_SHORT = "/rights/";
		return url.replace(RIGHTS_URL_IDENTIFIER_LONG,
				RIGHTS_URL_IDENTIFIER_SHORT);
	}

	/**
	 * Returns the full Url associated with the rights
	 * 
	 * @return Full url associated with the rights
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Return text associated with the rights
	 * 
	 * @return text associated with the results
	 */
	public String getRightsText() {
		return right.getRightsText();
	}

	/**
	 * Returns the url of the icon associated with the results
	 * 
	 * @return url of icon associated with the results
	 */
	public String getRightsIcon() {
		return right.getRightsIcon();
	}

	/**
	 * Returns the url of the icon associated with the results (for the lightbox)
	 * 
	 * @return url of icon associated with the results
	 */
	public String getRightsIconLightbox() {
		return right.getRightsIconLightbox();
	}

	public boolean isNoc() {
		return right == RightsOption.NOC;
	}

}
