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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.portal2.querymodel.query.FacetQueryLinks;
import eu.europeana.portal2.web.model.spellcheck.SpellCheck;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.ResultPagination;
import eu.europeana.portal2.web.presentation.model.SearchFilter;
import eu.europeana.portal2.web.presentation.model.data.SearchData;
import eu.europeana.portal2.web.presentation.model.data.decorators.lists.BriefBeanListDecorator;

public class BriefBeanViewDecorator implements BriefBeanView {

	private final Logger log = Logger.getLogger(getClass().getName());

	private SearchData model;
	private BriefBeanView view;

	private List<FacetQueryLinks> facets = null;

	public BriefBeanViewDecorator(SearchData model, BriefBeanView view) {
		this.model = model;
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends BriefBean> getBriefDocs() {
		//if (breadcrumbs != null) {
			BriefBeanListDecorator<BriefBean> decorator = new BriefBeanListDecorator<BriefBean>(model, (List<BriefBean>) view.getBriefDocs());
			// BreadcrumbListDecorator breadcrumbListDecorator = new BreadcrumbListDecorator(this, breadcrumbs);
			return decorator.asDecoList();
		//}
		//return null;

		// return new BriefBeanListDecorator<BriefBean>(model, (List<BriefBean>) view.getBriefDocs());
	}

	@Override
	public List<FacetQueryLinks> getFacetQueryLinks() {
		if (facets == null) {
			// setting the order and facet types we want
			String[] order = new String[] { "TYPE", "LANGUAGE", "YEAR",
					"COUNTRY", "RIGHTS", "PROVIDER", "DATA_PROVIDER" };
			FacetQueryLinks[] oFacets = new FacetQueryLinks[order.length];
			List<FacetQueryLinks> list = view.getFacetQueryLinks();
			for (FacetQueryLinks facet : list) {
				if ((facet.getLinks() != null) && (facet.getLinks().size() > 0)) {
					int index = ArrayUtils.indexOf(order, facet.getType());
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

}
