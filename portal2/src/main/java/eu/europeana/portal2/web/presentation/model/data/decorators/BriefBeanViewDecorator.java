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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.web.model.spellcheck.SpellCheck;
import eu.europeana.portal2.web.presentation.model.data.SearchData;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.ResultPagination;
import eu.europeana.portal2.web.presentation.model.submodel.SearchFilter;

public class BriefBeanViewDecorator implements BriefBeanView {

	private final static String[] facetOrder = new String[]{
		"TYPE", "LANGUAGE", "YEAR", "COUNTRY", "REUSABILITY", 
		"RIGHTS", "PROVIDER", "DATA_PROVIDER"
	};

	private SearchData model;
	private BriefBeanView view;
	private String imageUri;

	private List<FacetQueryLinks> facets = null;

	private List<BriefBeanDecorator> beans = null;

	public BriefBeanViewDecorator(SearchData model, BriefBeanView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public List<? extends BriefBean> getBriefBeans() {
		if (beans == null) {
			beans = new ArrayList<BriefBeanDecorator>();
			int index = model.getStart();
			for (BriefBean briefDoc : view.getBriefBeans()) {
				BriefBeanDecorator dec = new BriefBeanDecorator(model, briefDoc, index++);
				dec.setImageUri(imageUri);
				beans.add(dec);
			}
		}
		return beans;
	}

	@Override
	public List<FacetQueryLinks> getFacetQueryLinks() {
		if (facets == null) {
			// setting the order and facet types we want
			FacetQueryLinks[] oFacets = new FacetQueryLinks[facetOrder.length];
			List<FacetQueryLinks> list = view.getFacetQueryLinks();
			for (FacetQueryLinks facet : list) {
				if ((facet.getLinks() != null) && (facet.getLinks().size() > 0)) {
					int index = ArrayUtils.indexOf(facetOrder, facet.getType());
					if (index != -1) {
						oFacets[index] = facet;
					}
				}
			}
			facets = new ArrayList<FacetQueryLinks>();
			for (FacetQueryLinks facet : oFacets) {
				if (facet != null) {
					facets.add(new FacetQueryLinksDecorator(facet));
				}
			}
		}
		return facets;
	}

	/**
	 * return decorated class instead
	 */
	@Override
	public ResultPagination getPagination() {
		return new PaginationDecorator(model, view.getPagination());
	}

	@Override
	public Map<String, String> getFacetLogs() {
		return view.getFacetLogs();
	}

	@Override
	public BriefBean getMatchDoc() {
		return new BriefBeanDecorator(model, view.getMatchDoc());
	}

	@Override
	public SpellCheck getSpellCheck() {
		return view.getSpellCheck();
	}

	@Override
	public List<SearchFilter> getSearchFilters() {
		return view.getSearchFilters();
	}

	@Override
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
		
	}
}
