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

import java.util.Date;

import eu.europeana.corelib.definitions.model.RightsOption;

public class RightsValue {

	private final static String RIGHTS_URL_IDENTIFIER_LONG = "http://www.europeana.eu/rights/";
	private final static String RIGHTS_URL_IDENTIFIER_SHORT = "/rights/";

	private RightsOption right;

	private String url;
	private Date expiryDate;
	

	private String portalUrl;

	public RightsValue(RightsOption right, String url, String portalUrl, Date expiryDate) {
		this.right = right;
		this.url = url;
		this.portalUrl = portalUrl;
		this.expiryDate = expiryDate;
	}

	public static final RightsValue safeValueByUrl(String url, String portalUrl, Date expiryDate) {
		RightsOption right = RightsOption.safeValueByUrl(url);
		if (right != null) {
			return new RightsValue(right, url, portalUrl, expiryDate);
		}
		return null;
	}

	/**
	 * Returns short version of url associated with the rights
	 * 
	 * @return Short version of URL associated with the results
	 */
	public String getRightsUrl() {
		return url.replace(RIGHTS_URL_IDENTIFIER_LONG, RIGHTS_URL_IDENTIFIER_SHORT);
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

	public boolean getRightsShowExternalIcon() {
		return right.getShowExternalIcon();
	}

	public String getRelativeUrl() {
		if (right.isRelativeUrl()) {
			return right.getRelativeUrl(portalUrl);
		}
		return url;
	}

	public boolean isNoc() {
		return right == RightsOption.NOC || right == RightsOption.CC_ZERO;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	@Override
	public String toString() {
		return "RightsValue [RIGHTS_URL_IDENTIFIER_SHORT=" + RIGHTS_URL_IDENTIFIER_SHORT 
				+ ", rightsText=" + right.getRightsText()
				+ ", rightsIcon=" + right.getRightsIcon()
				+ ", showExternalIcon=" + right.getShowExternalIcon()
				+ ", rightUrl=" + right.getUrl()
				+ ", expiryDate=" + expiryDate
				+ ", url=" + url + "]";
	}
}
