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

package eu.europeana.portal2.web.presentation.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.presentation.PortalLanguage;
import eu.europeana.portal2.web.presentation.model.data.IndexData;

public class IndexPage extends IndexData {

	@Override
	public String getMetaCanonicalUrl() {
		return StringUtils.remove(super.getMetaCanonicalUrl(), "index.html");
	}

	public List<String> getProvidersForInclusion() {
		List<String> providersForInclusion = new ArrayList<String>();
		if (getRefinements() != null) {
			for (String provider : getRefinements()) {
				if (provider.contains("PROVIDER:")) {
					providersForInclusion.add(provider);
				}
			}
		}
		return providersForInclusion;
	}

	public List<PortalLanguage> getPortalLanguages() {
		return PortalLanguage.getSupported();
	}
}
