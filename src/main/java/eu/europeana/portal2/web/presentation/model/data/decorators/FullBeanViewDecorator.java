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

import java.io.UnsupportedEncodingException;
import java.util.List;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.portal2.web.presentation.model.DocIdWindowPager;
import eu.europeana.portal2.web.presentation.model.FullBeanView;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.presentation.model.data.decorators.lists.BriefBeanListDecorator;

public class FullBeanViewDecorator implements FullBeanView {
	private static final long serialVersionUID = -5504231572868214828L;

	private FullDocData model;
	private FullBeanView fullBeanView;

	public FullBeanViewDecorator(FullDocData model, FullBeanView fullBeanView) {
		this.model = model;
		this.fullBeanView = fullBeanView;
	}

	@Override
	public DocIdWindowPager getDocIdWindowPager() throws Exception,
			UnsupportedEncodingException {
		if (fullBeanView.getDocIdWindowPager() != null) {
			return new DocIdWindowPagerDecorator(model, fullBeanView.getDocIdWindowPager());
		}
		return null;
	}

	/**
	 * Give back only the first 3 list item
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends BriefBean> getRelatedItems() {
		return new BriefBeanListDecorator<BriefBean>(model,
				(List<BriefBean>) fullBeanView.getRelatedItems());
	}

	@Override
	public FullBean getFullDoc() throws EuropeanaQueryException {
		if (fullBeanView != null) {
			return new FullBeanDecorator(fullBeanView.getFullDoc());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends BriefBean> getChildren() {
		if (fullBeanView.getChildren() != null) {
			return new BriefBeanListDecorator<BriefBean>(model, (List<BriefBean>) fullBeanView.getChildren());
		}
		return null;
	}

	@Override
	public BriefBeanDecorator getParent() {
		if (fullBeanView.getParent() != null) {
			return new BriefBeanDecorator(model, fullBeanView.getParent());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends BriefBean> getParents() {
		if (fullBeanView.getParents() != null) {
			return new BriefBeanListDecorator<BriefBean>(model, (List<BriefBean>) fullBeanView.getParents());
		}
		return null;
	}

}
