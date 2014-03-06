package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.util.List;

import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.Timespan;

/**
 * Interface to retrieve connections between parts of a FullBean
 *
 * @author Peter.Kiraly@europeana.eu
 */
public interface FullBeanConnections {

	public Proxy getEuropeanaProxy();

	public List<Proxy> getProvidedProxies();

	public boolean isSingletonProxy();

	public Agent getAgentByURI(String uri);

	public Concept getConceptByURI(String uri);

	public Place getPlaceByURI(String uri);

	public Timespan getTimespanByURI(String uri);
}
