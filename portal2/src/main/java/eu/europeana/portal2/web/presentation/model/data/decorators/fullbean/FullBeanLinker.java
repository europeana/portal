package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.edm.beans.FullBean;
import eu.europeana.corelib.definitions.edm.entity.Agent;
import eu.europeana.corelib.definitions.edm.entity.Aggregation;
import eu.europeana.corelib.definitions.edm.entity.Concept;
import eu.europeana.corelib.definitions.edm.entity.Place;
import eu.europeana.corelib.definitions.edm.entity.Proxy;
import eu.europeana.corelib.definitions.edm.entity.Timespan;
import eu.europeana.corelib.definitions.edm.entity.WebResource;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.AgentDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ConceptDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ContextualItemDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.PlaceDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.TimespanDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.Resource;

public class FullBeanLinker extends FullBeanWrapper implements FullBeanLinkable {

	protected List<ConceptDecorator> concepts;
	protected List<PlaceDecorator> places;
	protected List<TimespanDecorator> timespans;
	protected List<AgentDecorator> agents;
	protected List<ContextualItemDecorator> entities;

	private Map<String, Agent> agentMap;
	private Map<String, Concept> conceptMap;
	private Map<String, Place> placeMap;
	private Map<String, Timespan> timespanMap;
	private Map<String, WebResource> webResourceMap;

	private Proxy europeanaProxy;
	private List<Proxy> providedProxies;
	private Boolean singletonProvidedProxy;
	private boolean havingEntityInContext = false;

	public FullBeanLinker(FullBean fullBean) {
		super(fullBean);
	}

	public FullBeanLinker(FullBean fullBean, String userLanguage) {
		super(fullBean, userLanguage);
	}

	public List<? extends Agent> getDecoratedAgents() {
		if (agents == null) {
			Map<String, String> ids = new HashMap<String, String>();
			agents = new ArrayList<AgentDecorator>();
			if (fullBean.getAgents() != null) {
				for (Agent agent : fullBean.getAgents()) {
					AgentDecorator decorator = new AgentDecorator(this, agent,
							userLanguage, edmLanguage);
					agents.add(decorator);
					ids.put(agent.getAbout(), decorator.getLabel());
				}
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

	public List<? extends Concept> getDecoratedConcepts() {
		if (concepts == null) {
			Map<String, String> ids = new HashMap<String, String>();
			concepts = new ArrayList<ConceptDecorator>();
			if (fullBean.getConcepts() != null) {
				for (Concept concept : fullBean.getConcepts()) {
					ConceptDecorator decorator = new ConceptDecorator(this,
							concept, userLanguage, edmLanguage);
					concepts.add(decorator);
					if (decorator.getLabel() != null) {
						ids.put(concept.getAbout(), decorator.getLabel());
					}
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

	public List<? extends Place> getDecoratedPlaces() {
		if (places == null) {
			Map<String, String> ids = new HashMap<String, String>();
			places = new ArrayList<PlaceDecorator>();
			if (fullBean.getPlaces() != null) {
				for (Place place : fullBean.getPlaces()) {
					PlaceDecorator decorator = new PlaceDecorator(this, place,
							userLanguage, edmLanguage);
					places.add(decorator);
					ids.put(place.getAbout(), decorator.getLabel());
				}
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

	public List<? extends Timespan> getDecoratedTimespans() {
		if (timespans == null) {
			Map<String, String> ids = new HashMap<String, String>();
			timespans = new ArrayList<TimespanDecorator>();
			if (fullBean.getTimespans() != null) {
				for (Timespan timespan : fullBean.getTimespans()) {
					TimespanDecorator decorator = new TimespanDecorator(this,
							timespan, userLanguage, edmLanguage);
					timespans.add(decorator);
					ids.put(timespan.getAbout(), decorator.getLabel());
				}
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

	private synchronized void setUpProxies() {
		if (providedProxies == null) {
			providedProxies = new ArrayList<Proxy>();
			for (Proxy proxy : getProxies()) {
				if (proxy.isEuropeanaProxy()) {
					europeanaProxy = proxy;
				} else {
					providedProxies.add(proxy);
				}
			}
			singletonProvidedProxy = (providedProxies.size() == 1);
		}
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
		return providedProxies;
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
			if (getEuropeanaAggregation().getWebResources() != null) {
				for (WebResource webResource : getEuropeanaAggregation().getWebResources()) {
					webResourceMap.put(webResource.getAbout(), webResource);
				}
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

	public ContextualItemDecorator getContextualConnections(ContextualEntity type, String value, Resource resource) {
		ContextualItemDecorator foundEntity = null;
		boolean matchedUrl = false;
		if (resource != null) {
			foundEntity = getDecoratedEntityByResource(type, resource);
			matchedUrl = (foundEntity != null);
		}

		if (foundEntity == null) {
			List<? extends ContextualItemDecorator> entities = getDecoratedEntities(type);
			if (entities != null) {
				for (ContextualItemDecorator entity : entities) {
					matchedUrl = hasMatchedUrl(entity, value, resource);
					boolean matchedLabel = hasMatchedLabel(entity, value);
					if (matchedUrl || matchedLabel) {
						foundEntity = entity;
						break;
					}
				}
			}
		}

		if (foundEntity != null && !foundEntity.isShowInContext()) {
			havingEntityInContext = true;
			foundEntity.setMatchUrl(matchedUrl);
			foundEntity.setShowInContext(true);
		}

		return foundEntity;
	}

	private boolean hasMatchedUrl(ContextualItemDecorator entity, String value, Resource resource) {
		return hasMatchedResource(entity, resource) || hasMatchedAbout(entity, value);
	}

	private boolean hasMatchedResource(ContextualItemDecorator entity, Resource resource) {
		return resource != null && entity.getAbout().equals(resource.getUri());
	}

	private boolean hasMatchedAbout(ContextualItemDecorator entity, String value) {
		return entity.getAbout().equals(value);
	}

	private boolean hasMatchedLabel(ContextualItemDecorator entity, String value) {
		return entity.hasPrefLabel(value) || entity.hasAltLabel(value);
	}

	private ContextualItemDecorator getDecoratedEntityByResource(ContextualEntity type, Resource resource) {
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
		return entity;
	}

	@SuppressWarnings("unchecked")
	private List<? extends ContextualItemDecorator> getDecoratedEntities(ContextualEntity type) {
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
			case ALL:
				entities = (List<? extends ContextualItemDecorator>)getDecoratedEntities(); break;
			default:
				break;
		}
		return entities;
	}

	private List<ContextualItemDecorator> getDecoratedEntities() {
		if (entities == null) {
			entities = new ArrayList<ContextualItemDecorator>();
			entities.addAll(getDecoratedEntities(ContextualEntity.AGENT));
			entities.addAll(getDecoratedEntities(ContextualEntity.CONCEPT));
			entities.addAll(getDecoratedEntities(ContextualEntity.PLACE));
			entities.addAll(getDecoratedEntities(ContextualEntity.TIMESPAN));
		}
		return entities;
	}

	public boolean isHavingEntityInContext() {
		return havingEntityInContext;
	}
}
