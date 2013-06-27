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

package eu.europeana.portal2.web.presentation.model.data.decorators;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import eu.europeana.portal2.querymodel.query.FacetCountLink;
import eu.europeana.portal2.querymodel.query.RightsOption;
import eu.europeana.portal2.web.presentation.PortalLanguage;

public class FacetCountLinkDecorator implements FacetCountLink {

	private static final String RIGHTS = "RIGHTS";
	private static final String COUNTRY = "COUNTRY";
	private static final String LANGUAGE = "LANGUAGE";

	private FacetCountLink facetCountLink;
	private String type;
	private String title;
	private String value;

	public FacetCountLinkDecorator(String type, FacetCountLink facetCountLink) {
		this.facetCountLink = facetCountLink;
		this.type = type;
	}

	@Override
	public void update(FacetCountLink facetCountLink) {
		// Ignore
	}

	@Override
	public String getUrl() {
		return facetCountLink.getUrl();
	}

	@Override
	public RightsOption getRightsOption() {
		return facetCountLink.getRightsOption();
	}

	@Override
	public boolean isRemove() {
		return facetCountLink.isRemove();
	}

	/**
	 * Gives back a label matching labels in the resource files
	 * 
	 * @return Null if no valid label is available
	 */
	public String getLabel() {
		return null;
	}

	/**
	 * Gives back a title which should be output directly. Not matching any labels
	 * 
	 * @return
	 */
	public String getTitle() {
		if (title == null) {
			if (StringUtils.equals(type, LANGUAGE)) {
				PortalLanguage language = PortalLanguage.safeValueOf(facetCountLink.getValue());
				if ((language != null) && (language.getLanguageName() != null)) {
					title = language.getLanguageName();
				} else {
					title = facetCountLink.getValue();
				}
			} else {
				title = facetCountLink.getValue();
				if (StringUtils.equals(type, RIGHTS)) {
					if (facetCountLink.getRightsOption() != null) {
						title = facetCountLink.getRightsOption().getRightsText();
					}
				}
				if (StringUtils.equals(type, COUNTRY)) {
					if (StringUtils.length(title) <= 3) {
						title = StringUtils.upperCase(title);
					} else {
						title = WordUtils.capitalizeFully(title);
					}
				}
				title = StringUtils.abbreviate(title, 20);
			}
		}
		return title;
	}

	@Override
	public String getValue() {
		if (value == null) {
			if (StringUtils.equals(type, RIGHTS) && facetCountLink.getRightsOption() != null) {
				value = facetCountLink.getRightsOption().getRightsText();
			} else {
				value = facetCountLink.getValue();
			}
		}
		return value;
	}

	@Override
	public long getCount() {
		return facetCountLink.getCount();
	}

}
