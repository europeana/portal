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

package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import eu.europeana.corelib.definitions.ApplicationContextContainer;
import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Aggregation;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.EuropeanaAggregation;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.ProvidedCHO;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.utils.DateUtils;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.AgentDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ConceptDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.PlaceDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.TimespanDecorator;

public class FullBeanWrapper implements FullBean {

	Logger log = Logger.getLogger(FullBeanWrapper.class.getCanonicalName());

	public enum ContextualEntity {AGENT, CONCEPT, PLACE, TIMESPAN};

	protected FullBean fullBean;

	private EuropeanaUrlService europeanaUrlService;

	protected String userLanguage;
	protected String edmLanguage;

	public FullBeanWrapper(FullBean fullBean) {
		this.fullBean = fullBean;
		europeanaUrlService = ApplicationContextContainer.getBean(EuropeanaUrlService.class);//EuropeanaUrlServiceImpl.getBeanInstance();
	}

	public FullBeanWrapper(FullBean fullBean, String userLanguage) {
		this(fullBean);
		this.userLanguage = userLanguage;
		this.edmLanguage = fullBean.getEuropeanaAggregation().getEdmLanguage().get("def").get(0);
	}

	/**
	 * Returns the title of the post
	 * 
	 * @return post title
	 * @throws UnsupportedEncodingException
	 */
	public String getPostTitle() throws UnsupportedEncodingException {
		final int POST_TITLE_MAX_LENGTH = 110;
		StringBuilder postTitle = new StringBuilder();
		if (getTitle().length > POST_TITLE_MAX_LENGTH) {
			postTitle.append(getTitle()[0].substring(0, POST_TITLE_MAX_LENGTH));
			postTitle.append("...");
		} else {
			postTitle.append(getTitle()[0]);
		}
		return postTitle.toString();
	}

	@Override
	public String[] getTitle() {
		return fullBean.getTitle();
	}

	@Override
	public String[] getYear() {
		return fullBean.getYear();
	}

	@Override
	public String[] getProvider() {
		return fullBean.getProvider();
	}

	@Override
	public String[] getLanguage() {
		return fullBean.getLanguage();
	}

	@Override
	public DocType getType() {
		return fullBean.getType();
	}

	public String getEdmType() {
		for (Proxy proxy : fullBean.getProxies()) {
			return proxy.getEdmType().toString();
		}
		return null;
	}

	@Override
	public int getEuropeanaCompleteness() {
		return fullBean.getEuropeanaCompleteness();
	}

	public String getCannonicalUrl() {
		return europeanaUrlService.getPortalResolve(fullBean.getAbout());
	}

	@Override
	public String getId() {
		return fullBean.getId();
	}

	public String getEdmLandingPage() {
		return fullBean.getEuropeanaAggregation().getEdmLandingPage();
	}

	@Override
	public String[] getEuropeanaCollectionName() {
		return fullBean.getEuropeanaCollectionName();
	}

	@Override
	public Date getTimestamp() {
		return fullBean.getTimestamp();
	}

	@Override
	public String[] getCountry() {
		return fullBean.getCountry();
	}

	@Override
	public void setCountry(String[] country) {
		fullBean.setCountry(country);
	}

	@Override
	public void setEuropeanaCollectionName(String[] europeanaCollectionName) {
		fullBean.setEuropeanaCollectionName(europeanaCollectionName);
	}

	@Override
	public String[] getUserTags() {
		return fullBean.getUserTags();
	}

	@Override
	public List<? extends Place> getPlaces() {
		return fullBean.getPlaces();
	}

	@Override
	public void setPlaces(List<? extends Place> places) {
		fullBean.setPlaces(places);
	}

	@Override
	public List<? extends Agent> getAgents() {
		return fullBean.getAgents();
	}

	@Override
	public void setAgents(List<? extends Agent> agents) {
		fullBean.setAgents(agents);
	}

	@Override
	public List<? extends Timespan> getTimespans() {
		return fullBean.getTimespans();
	}

	@Override
	public List<? extends Concept> getConcepts() {
		return fullBean.getConcepts();
	}

	@Override
	public void setConcepts(List<? extends Concept> concepts) {
		fullBean.setConcepts(concepts);
	}

	@Override
	public void setAggregations(List<? extends Aggregation> aggregations) {
		fullBean.setAggregations(aggregations);
	}

	@Override
	public List<? extends Proxy> getProxies() {
		return fullBean.getProxies();
	}

	@Override
	public void setProxies(List<? extends Proxy> proxies) {
		fullBean.setProxies(proxies);
	}

	@Override
	public void setEuropeanaId(ObjectId europeanaId) {
		fullBean.setEuropeanaId(europeanaId);
	}

	@Override
	public void setTitle(String[] title) {
		fullBean.setTitle(title);
	}

	@Override
	public void setYear(String[] year) {
		fullBean.setYear(year);
	}

	@Override
	public void setProvider(String[] provider) {
		fullBean.setProvider(provider);
	}

	@Override
	public void setLanguage(String[] language) {
		fullBean.setLanguage(language);
	}

	@Override
	public void setType(DocType type) {
		fullBean.setType(type);
	}

	@Override
	public void setEuropeanaCompleteness(int europeanaCompleteness) {
		fullBean.setEuropeanaCompleteness(europeanaCompleteness);
	}

	@Override
	public void setTimespans(List<? extends Timespan> timespans) {
		fullBean.setTimespans(timespans);
	}

	@Override
	public List<? extends Aggregation> getAggregations() {
		return fullBean.getAggregations();
	}

	@Override
	public List<? extends ProvidedCHO> getProvidedCHOs() {
		return fullBean.getProvidedCHOs();
	}

	@Override
	public void setProvidedCHOs(List<? extends ProvidedCHO> providedCHOs) {
		fullBean.setProvidedCHOs(providedCHOs);
	}

	@Override
	public String getAbout() {
		return fullBean.getAbout();
	}

	@Override
	public void setAbout(String about) {
		fullBean.setAbout(about);
	}

	@Override
	public EuropeanaAggregation getEuropeanaAggregation() {
		EuropeanaAggregation europeanaAggregation = fullBean.getEuropeanaAggregation();
		String edmPreview = "";
		if ((this.getAggregations() != null) && !this.getAggregations().isEmpty()
				&& (this.getAggregations().get(0).getEdmObject() != null)) {
			String url = this.getAggregations().get(0).getEdmObject();
			if (StringUtils.isNotBlank(url)) {
				edmPreview = europeanaUrlService.getThumbnailUrl(url, getType()).toString();
			}
		}
		europeanaAggregation.setEdmPreview(edmPreview);
		return europeanaAggregation;
	}

	@Override
	public void setEuropeanaAggregation(EuropeanaAggregation europeanaAggregation) {
		fullBean.setEuropeanaAggregation(europeanaAggregation);
	}

	@Override
	public Boolean isOptedOut() {
		return fullBean.isOptedOut();
	}

	public Boolean getOptedOut() {
		return fullBean.isOptedOut();
	}

	@Override
	public List<? extends BriefBean> getSimilarItems() {
		return fullBean.getSimilarItems();
	}

	@Override
	public void setSimilarItems(List<? extends BriefBean> similarItems) {
		fullBean.setSimilarItems(similarItems);
	}

	@Override
	public void setOptOut(boolean optOut) {
		fullBean.setOptOut(optOut);
	}

	@Override
	public Date getTimestampCreated() {
		return fullBean.getTimestampCreated();
	}

	public String getTimestampCreatedString() {
		if (fullBean.getTimestampCreated() != null) {
			return DateUtils.format(fullBean.getTimestampCreated());
		}
		return null;
	}

	@Override
	public Date getTimestampUpdated() {
		return fullBean.getTimestampUpdated();
	}

	public String getTimestampUpdatedString() {
		if (fullBean.getTimestampUpdated() != null) {
			return DateUtils.format(fullBean.getTimestampUpdated());
		}
		return null;
	}

	@Override
	public void setTimestampCreated(Date timestampCreated) {
		fullBean.setTimestampCreated(timestampCreated);
	}

	@Override
	public void setTimestampUpdated(Date timestampUpdated) {
		fullBean.setTimestampUpdated(timestampUpdated);
	}
}