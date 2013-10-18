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

package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.querymodel.query.FacetCountLink;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.web.presentation.model.data.decorators.lists.FacetCountLinkListDecorator;

public class FacetQueryLinksDecorator implements FacetQueryLinks {

	private static final String YEAR = "YEAR";

	private FacetQueryLinks facetQueryLinks;

	public FacetQueryLinksDecorator(FacetQueryLinks facets) {
		facetQueryLinks = facets;
	}

	@Override
	public String getType() {
		return facetQueryLinks.getType();
	}

	@Override
	public List<FacetCountLink> getLinks() {
		List<FacetCountLink> list = facetQueryLinks.getLinks();
		if (StringUtils.equals(getType(), YEAR)) {
			Collections.sort(list, new FacetCountLinkComparator());
		}
		return new FacetCountLinkListDecorator(getType(), list);
	}

	@Override
	public boolean isSelected() {
		return facetQueryLinks.isSelected();
	}

	private class FacetCountLinkComparator implements Comparator<FacetCountLink> {

		@Override
		public int compare(FacetCountLink o1, FacetCountLink o2) {
			if ((o1 != null) && (o2 != null)) {
				if (StringUtils.isNotEmpty(o1.getValue())) {
					return o1.getValue().compareToIgnoreCase(o2.getValue());
				}
			}
			return 0;
		}
	}
}
