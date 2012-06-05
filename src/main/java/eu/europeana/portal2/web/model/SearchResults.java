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

package eu.europeana.portal2.web.model;

import java.util.List;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.portal2.web.model.abstracts.AbstractSearchResults;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.spellcheck.SpellCheck;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 */
public class SearchResults extends AbstractSearchResults {
	
	public List<BreadCrumb> breadCrumbs;
	
	public List<Facet> facets;
	
	public SpellCheck spellcheck;
	
	public SearchResults(String action) {
		super(action);
	}
	
	public List<Facet> getFacets() {
		return facets;
	}
}
