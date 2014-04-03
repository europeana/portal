package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Aggregation;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.definitions.solr.entity.WebResource;
import eu.europeana.portal2.web.presentation.model.data.decorators.contextual.ContextualItemDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.Resource;

public class FullBeanConnections extends FullBeanDecorator implements FullBeanConnectable {

	private Map<String, Agent> agentMap;
	private Map<String, Concept> conceptMap;
	private Map<String, Place> placeMap;
	private Map<String, Timespan> timespanMap;
	private Map<String, WebResource> webResourceMap;

	private Proxy europeanaProxy;
	private List<Proxy> providedProxies;
	private Boolean singletonProvidedProxy;

	public FullBeanConnections(FullBean fullBean) {
		super(fullBean);
	}

	public FullBeanConnections(FullBean fullBean, String userLanguage) {
		super(fullBean, userLanguage);
	}

	@Override
	public Proxy getEuropeanaProxy() {
		if (europeanaProxy == null) {
			setUpProxies();
		}
		return europeanaProxy;
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

	@SuppressWarnings("unchecked")
	public ContextualItemDecorator getContextualConnections(ContextualEntity type, String value, Resource resource) {

		if (resource != null) {
			ContextualItemDecorator entity = null;
			switch (type) {
				case AGENT:
					entity = (ContextualItemDecorator)getAgentByURI(resource.getUri()); break;
				case CONCEPT:
					entity = (ContextualItemDecorator)getConceptByURI(resource.getUri()); break;
				case PLACE:
					entity = (ContextualItemDecorator)getPlaceByURI(resource.getUri()); break;
				case TIMESPAN:
					entity = (ContextualItemDecorator)getTimespanByURI(resource.getUri()); break;
				default:
					break;
			}

			if (entity != null) {
				entity.setShowInContext(true);
				entity.setMatchUrl(true);
				return entity;
			}
		}

		List<? extends ContextualItemDecorator> entities = null;
		switch (type) {
			case AGENT:
				entities = (List<? extends ContextualItemDecorator>)getDecoratedAgents(); break;
			case CONCEPT:
				entities = (List<? extends ContextualItemDecorator>)getDecoratedConcepts(); break;
			case PLACE:
				entities = (List<? extends ContextualItemDecorator>)getDecoratedPlaces(); break;
			case TIMESPAN:
				entities = (List<? extends ContextualItemDecorator>)getDecoratedTimespans(); break;
			default:
				break;
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
}
