package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

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
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.AgentDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ConceptDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ContextualItemDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.PlaceDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.TimespanDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.Resource;

public class FullBeanLinked extends FullBeanWrapper implements FullBeanConnectable {

	protected List<ConceptDecorator> concepts;
	protected List<PlaceDecorator> places;
	protected List<TimespanDecorator> timespans;
	protected List<AgentDecorator> agents;

	private Map<String, Agent> agentMap;
	private Map<String, Concept> conceptMap;
	private Map<String, Place> placeMap;
	private Map<String, Timespan> timespanMap;
	private Map<String, WebResource> webResourceMap;

	private Proxy europeanaProxy;
	private List<Proxy> providedProxies;
	private Boolean singletonProvidedProxy;

	public FullBeanLinked(FullBean fullBean) {
		super(fullBean);
	}

	public FullBeanLinked(FullBean fullBean, String userLanguage) {
		super(fullBean, userLanguage);
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
