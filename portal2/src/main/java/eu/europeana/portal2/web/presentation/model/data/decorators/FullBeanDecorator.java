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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

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
import eu.europeana.corelib.definitions.solr.entity.WebResource;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.utils.DateUtils;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.service.impl.EuropeanaUrlServiceImpl;
import eu.europeana.portal2.web.presentation.model.data.decorators.contextual.AgentDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.contextual.ConceptDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.contextual.ContextualItemDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.contextual.PlaceDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.contextual.TimespanDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.Resource;
import eu.europeana.portal2.web.util.FullBeanShortcut;

public class FullBeanDecorator implements FullBean, FullBeanConnections {

	Logger log = Logger.getLogger(FullBeanDecorator.class.getCanonicalName());

	public enum ContextualEntity {AGENT, CONCEPT, PLACE, TIMESPAN};

	private FullBean fullBean;

	private FullBeanShortcut shortcut;

	private EuropeanaUrlService europeanaUrlService;
	private String userLanguage;
	private String edmLanguage;
	List<ConceptDecorator> concepts;
	List<PlaceDecorator> places;
	List<TimespanDecorator> timespans;
	List<AgentDecorator> agents;

	private Proxy europeanaProxy;
	private List<Proxy> providedProxies;
	private Boolean singletonProvidedProxy;

	private Map<String, Agent> agentMap;
	private Map<String, Concept> conceptMap;
	private Map<String, Place> placeMap;
	private Map<String, Timespan> timespanMap;

	private Map<String, WebResource> webResourceMap;

	public FullBeanDecorator(FullBean fullBean) {
		log.info("creating FullBeanDecorator");
		this.fullBean = fullBean;
		europeanaUrlService = EuropeanaUrlServiceImpl.getBeanInstance();
	}

	public FullBeanDecorator(FullBean fullBean, String userLanguage) {
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

	/**
	 * Returns the posts author
	 * 
	 * @return posts author
	 */
	public String getPostAuthor() {
		if (StringUtils.isBlank(shortcut.get("DcCreator")[0])) {
			return "none";
		} else {
			return shortcut.get("DcCreator")[0];
		}
	}

	public String getUrlKml() {
		if (isPositionAvailable()) {
			// return "record" + getAbout() + ".kml";
			return "";
		}
		return null;
	}

	public boolean isPositionAvailable() {
		return (shortcut.getEdmPlaceLatitude() != null && shortcut.getEdmPlaceLatitude().length > 0
				&& shortcut.getEdmPlaceLatitude()[0] != null && shortcut.getEdmPlaceLatitude()[0] != 0)
				|| (shortcut.getEdmPlaceLongitude() != null && shortcut.getEdmPlaceLongitude().length > 0
						&& shortcut.getEdmPlaceLongitude()[0] != null && shortcut.getEdmPlaceLongitude()[0] != 0);
	}

	public Float[] getEdmPlaceLatitude() {
		return shortcut.getEdmPlaceLatitude();
	}

	public Float[] getEdmPlaceLongitude() {
		return shortcut.getEdmPlaceLongitude();
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

	public String[] getDataProvider() {
		return shortcut.get("DataProvider");
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

	public String[] getEdmCountry() {
		return shortcut.get("EdmCountry");
	}

	public String[] getEdmLanguage() {
		return shortcut.get("EdmLanguage");
	}

	public String[] getEdmLandingPage() {
		return shortcut.get("EdmLandingPage");
	}

	public String getCheckedEdmLandingPage() {
		if (ArrayUtils.isEmpty(shortcut.get("EdmLandingPage"))) {
			return null;
		}
		String landingPage = shortcut.get("EdmLandingPage")[0];
		if (!landingPage.endsWith(".html")) {
			landingPage += ".html";
		}
		return landingPage;
	}

	public String[] getEdmDataProvider() {
		return shortcut.get("EdmDataProvider");
	}

	public boolean isUserGeneratedContent() {
		if (StringArrayUtils.isNotBlank(shortcut.get("EdmUGC"))) {
			return StringUtils.equalsIgnoreCase(shortcut.get("EdmUGC")[0], "true");
		}
		return false;
	}

	public String[] getDctermsHasVersion() {
		return shortcut.get("DctermsHasVersion");
	}

	public String[] getDctermsIsFormatOf() {
		return shortcut.get("DctermsIsFormatOf");
	}

	public String[] getDcDate() {
		return shortcut.get("DcDate");
	}

	public String[] getDcDescription() {
		if (shortcut.get("DcDescription") == null) {
			return null;
		}

		List<String> descriptions = Arrays.asList(shortcut.get("DcDescription"));
		for (int i = 0, l = descriptions.size(); i < l; i++) {
			descriptions.set(i, descriptions.get(i).replace("\n", "<br/>\n"));
		}
		return StringArrayUtils.toArray(descriptions);
	}

	public String getDcDescriptionCombined() {
		return StringEscapeUtils.escapeXml(StringUtils.join(getDcDescription(), ";"));
	}

	public String[] getDcFormat() {
		return shortcut.get("DcFormat");
	}

	public String[] getDcLanguage() {
		return shortcut.get("DcLanguage");
	}

	public String[] getDcRights() {
		return shortcut.get("DcRights");
	}

	public String[] getDcSubject() {
		return shortcut.get("DcSubject");
	}

	public String[] getDcTitle() {
		return shortcut.get("DcTitle");
	}

	public String getDcTitleCombined() {
		return StringEscapeUtils.escapeXml(StringUtils.join(getDcTitle(), ";"));
	}

	public String[] getDcType() {
		return shortcut.get("DcType");
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

	public List<? extends Place> getDecoratedPlaces() {
		if (places == null) {
			Map<String, String> ids = new HashMap<String, String>();
			places = new ArrayList<PlaceDecorator>();
			for (Place place : fullBean.getPlaces()) {
				PlaceDecorator decorator = new PlaceDecorator(this, place, userLanguage, edmLanguage);
				places.add(decorator);
				ids.put(place.getAbout(), decorator.getLabel());
			}
			for (PlaceDecorator agent : places) {
				agent.makeLinks(ids);
			}
		}
		return places;
	}

	public int getNumberOfUnreferencedPlaces() {
		int counter = 0;
		for (Place p : getDecoratedPlaces()) {
			if (!((PlaceDecorator)p).isShowInContext()) {
				counter++;
			}
		}
		return counter;
	}

	@Override
	public void setPlaces(List<? extends Place> places) {
		fullBean.setPlaces(places);
	}

	@Override
	public List<? extends Agent> getAgents() {
		return fullBean.getAgents();
	}

	public List<? extends Agent> getDecoratedAgents() {
		if (agents == null) {
			Map<String, String> ids = new HashMap<String, String>();
			agents = new ArrayList<AgentDecorator>();
			for (Agent agent : fullBean.getAgents()) {
				AgentDecorator decorator = new AgentDecorator(this, agent, userLanguage, edmLanguage);
				agents.add(decorator);
				ids.put(agent.getAbout(), decorator.getLabel());
			}
			for (AgentDecorator agent : agents) {
				agent.makeLinks(ids);
			}
		}
		return agents;
	}

	public int getNumberOfUnreferencedAgents() {
		int counter = 0;
		for (Agent p : getDecoratedAgents()) {
			if (!((AgentDecorator)p).isShowInContext()) {
				counter++;
			}
		}
		return counter;
	}

	@Override
	public void setAgents(List<? extends Agent> agents) {
		fullBean.setAgents(agents);
	}

	@Override
	public List<? extends Timespan> getTimespans() {
		return fullBean.getTimespans();
	}

	public List<? extends Timespan> getDecoratedTimespans() {
		if (timespans == null) {
			Map<String, String> ids = new HashMap<String, String>();
			timespans = new ArrayList<TimespanDecorator>();
			for (Timespan timespan : fullBean.getTimespans()) {
				TimespanDecorator decorator = new TimespanDecorator(this, timespan, userLanguage, edmLanguage);
				timespans.add(decorator);
				ids.put(timespan.getAbout(), decorator.getLabel());
			}
			for (TimespanDecorator timespan : timespans) {
				timespan.makeLinks(ids);
			}
		}
		return timespans;
	}

	public int getNumberOfUnreferencedTimespans() {
		int counter = 0;
		for (Timespan p : getDecoratedTimespans()) {
			if (!((TimespanDecorator)p).isShowInContext()) {
				counter++;
			}
		}
		return counter;
	}

	@Override
	public List<? extends Concept> getConcepts() {
		return fullBean.getConcepts();
	}

	public List<? extends Concept> getDecoratedConcepts() {
		if (concepts == null) {
			Map<String, String> ids = new HashMap<String, String>();
			concepts = new ArrayList<ConceptDecorator>();
			for (Concept concept : fullBean.getConcepts()) {
				ConceptDecorator decorator = new ConceptDecorator(this, concept, userLanguage, edmLanguage);
				concepts.add(decorator);
				if (decorator.getLabel() != null) {
					ids.put(concept.getAbout(), decorator.getLabel());
				}
			}
			for (ConceptDecorator concept : concepts) {
				concept.makeLinks(ids);
			}
		}
		return concepts;
	}

	public int getNumberOfUnreferencedConcepts() {
		int counter = 0;
		for (Concept p : getDecoratedConcepts()) {
			if (!((ConceptDecorator)p).isShowInContext()) {
				counter++;
			}
		}
		return counter;
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

	public void setShortcut(FullBeanShortcut shortcut) {
		this.shortcut = shortcut;
	}

	@SuppressWarnings("unchecked")
	public Object getContextualConnections(ContextualEntity type, String value, Resource resource) {

		if (resource != null) {
			ContextualItemDecorator entity = null;
			if (type.equals(ContextualEntity.AGENT)) {
				entity = (ContextualItemDecorator)getAgentByURI(resource.getUri());
			} else if (type.equals(ContextualEntity.CONCEPT)) {
				entity = (ContextualItemDecorator)getConceptByURI(resource.getUri());
			} else if (type.equals(ContextualEntity.PLACE)) {
				entity = (ContextualItemDecorator)getPlaceByURI(resource.getUri());
			} else if (type.equals(ContextualEntity.TIMESPAN)) {
				entity = (ContextualItemDecorator)getTimespanByURI(resource.getUri());
			}
			if (entity != null) {
				entity.setShowInContext(true);
				entity.setMatchUrl(true);
				return entity;
			}
		}

		List<? extends ContextualItemDecorator> entities = null;
		if (type.equals(ContextualEntity.AGENT)) {
			entities = (List<? extends ContextualItemDecorator>)getDecoratedAgents();
		} else if (type.equals(ContextualEntity.CONCEPT)) {
			entities = (List<? extends ContextualItemDecorator>)getDecoratedConcepts();
		} else if (type.equals(ContextualEntity.PLACE)) {
			entities = (List<? extends ContextualItemDecorator>)getDecoratedPlaces();
		} else if (type.equals(ContextualEntity.TIMESPAN)) {
			entities = (List<? extends ContextualItemDecorator>)getDecoratedTimespans();
		}

		if (entities == null) {
			return null;
		}

		for (ContextualItemDecorator entity : entities) {
			if (resource != null && entity.getAbout().equals(resource.getUri())) {
				entity.setShowInContext(true);
				entity.setMatchUrl(true);
				return entity;
			} else if (value.startsWith("http://")) {
				if (entity.getAbout().equals(value)) {
					entity.setShowInContext(true);
					entity.setMatchUrl(true);
					return entity;
				}
			} else {
				if (entity.hasPrefLabel(value)) {
					entity.setShowInContext(true);
					return entity;
				} else if (entity.hasAltLabel(value)) {
					entity.setShowInContext(true);
					return entity;
				}
			}
		}

		return null;
	}

	private void setUpProxies() {
		List<Proxy> providedProxies = new ArrayList<Proxy>();
		for (Proxy proxy : getProvidedProxies()) {
			if (proxy.isEuropeanaProxy()) {
				europeanaProxy = proxy;
			} else {
				providedProxies.add(proxy);
			}
		}
		singletonProvidedProxy = providedProxies.size() == 1;
	}

	@Override
	public Proxy getEuropeanaProxy() {
		if (europeanaProxy == null) {
			setUpProxies();
		}
		return europeanaProxy;
	}

	@Override
	public List<Proxy> getProvidedProxies() {
		if (providedProxies == null) {
			setUpProxies();
		}
		return null;
	}

	@Override
	public boolean isSingletonProxy() {
		if (singletonProvidedProxy == null) {
			setUpProxies();
		}
		return singletonProvidedProxy;
	}

	private void initializeAgentMap() {
		agentMap = new HashMap<String, Agent>();
		for (Agent agent : getDecoratedAgents()) {
			agentMap.put(agent.getAbout(), agent);
		}
	}

	private void initializeConceptMap() {
		conceptMap = new HashMap<String, Concept>();
		for (Concept item : getDecoratedConcepts()) {
			conceptMap.put(item.getAbout(), item);
		}
	}

	private void initializePlaceMap() {
		placeMap = new HashMap<String, Place>();
		for (Place item : getDecoratedPlaces()) {
			placeMap.put(item.getAbout(), item);
		}
	}

	private void initializeTimespanMap() {
		timespanMap = new HashMap<String, Timespan>();
		for (Timespan item : getDecoratedTimespans()) {
			timespanMap.put(item.getAbout(), item);
		}
	}

	@Override
	public Agent getAgentByURI(String uri) {
		if (agentMap == null) {
			initializeAgentMap();
		}
		if (agentMap.containsKey(uri)) {
			return agentMap.get(uri);
		}
		return null;
	}

	@Override
	public Concept getConceptByURI(String uri) {
		if (conceptMap == null) {
			initializeConceptMap();
		}
		if (conceptMap.containsKey(uri)) {
			return conceptMap.get(uri);
		}
		return null;
	}

	@Override
	public Place getPlaceByURI(String uri) {
		if (placeMap == null) {
			initializePlaceMap();
		}
		if (placeMap.containsKey(uri)) {
			return placeMap.get(uri);
		}
		return null;
	}

	@Override
	public Timespan getTimespanByURI(String uri) {
		if (timespanMap == null) {
			initializeTimespanMap();
		}
		if (timespanMap.containsKey(uri)) {
			return timespanMap.get(uri);
		}
		return null;
	}

	public WebResource getWebResourceByUrl(String url) {
		if (webResourceMap == null) {
			webResourceMap = new HashMap<String, WebResource>();
			for (Aggregation aggregation : getAggregations()) {
				for (WebResource webResource : aggregation.getWebResources()) {
					webResourceMap.put(webResource.getAbout(), webResource);
				}
			}
			for (WebResource webResource : getEuropeanaAggregation().getWebResources()) {
				webResourceMap.put(webResource.getAbout(), webResource);
			}
		}
		if (webResourceMap.containsKey(url)) {
			return webResourceMap.get(url);
		}
		return null;
	}

	public String getWebResourceEdmRightsByUrl(String url) {
		String rightString = null;
		WebResource webResource = getWebResourceByUrl(url);
		if (webResource != null) {
			Map<String, List<String>> rights = webResource.getWebResourceEdmRights();
			if (rights != null) {
				OUTER:
				for (List<String> rList : rights.values()) {
					if (!rList.isEmpty()) {
						for (String r : rList) {
							if (StringUtils.isNotBlank(r)) {
								rightString = r;
								break OUTER;
							}
						}
					}
				}
			}
		}
		return rightString;
	}

	public List<String> getEdmRights() {
		return shortcut.getList("EdmRights");
	}
}