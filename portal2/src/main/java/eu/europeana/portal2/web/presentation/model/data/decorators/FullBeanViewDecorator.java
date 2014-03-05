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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.presentation.model.submodel.DocIdWindowPager;
import eu.europeana.portal2.web.presentation.model.submodel.FullBeanView;

public class FullBeanViewDecorator implements FullBeanView {
	private static final long serialVersionUID = -5504231572868214828L;

	private FullDocData model;
	private FullBeanView fullBeanView;
	private FullBeanDecorator fullBeanDecorator;

	private List<BriefBeanDecorator> relatedItems = null;
	private List<BriefBeanDecorator> parents = null;
	private List<BriefBeanDecorator> children = null;

	public FullBeanViewDecorator(FullDocData model, FullBeanView fullBeanView,
			FullBeanDecorator fullBeanDecorator) {
		this.model = model;
		this.fullBeanView = fullBeanView;
		this.fullBeanDecorator = fullBeanDecorator;
	}

	@Override
	public DocIdWindowPager getDocIdWindowPager() throws Exception, UnsupportedEncodingException {
		if (fullBeanView.getDocIdWindowPager() != null) {
			return new DocIdWindowPagerDecorator(model, fullBeanView.getDocIdWindowPager());
		}
		return null;
	}

	/**
	 * Give back only the first 3 list item
	 */
	@Override
	public List<? extends BriefBean> getRelatedItems() {
		if (relatedItems == null) {
			relatedItems = createDecoratedBeans(fullBeanView.getRelatedItems());
		}
		return relatedItems;
	}

	@Override
	public FullBean getFullDoc() {
		return fullBeanDecorator;
	}

	@Override
	public List<? extends BriefBean> getChildren() {
		if (children != null) {
			children = createDecoratedBeans(fullBeanView.getChildren());
		}
		return children;
	}

	@Override
	public BriefBeanDecorator getParent() {
		if (fullBeanView.getParent() != null) {
			return new BriefBeanDecorator(model, fullBeanView.getParent());
		}
		return null;
	}

	@Override
	public List<? extends BriefBean> getParents() {
		if (parents != null) {
			parents = createDecoratedBeans(fullBeanView.getParents());
		}
		return parents;
	}

	private List<BriefBeanDecorator> createDecoratedBeans(List<? extends BriefBean> list) {
		List<BriefBeanDecorator> items = new ArrayList<BriefBeanDecorator>();
		int index = 1;
		for (BriefBean briefDoc : list) {
			items.add(new BriefBeanDecorator(model, briefDoc, index++));
		}
		return items;
	}
}
