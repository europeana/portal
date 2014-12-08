package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import java.util.List;

import eu.europeana.corelib.definitions.edm.entity.Agent;
import eu.europeana.corelib.definitions.edm.entity.Concept;
import eu.europeana.corelib.definitions.edm.entity.Place;
import eu.europeana.corelib.definitions.edm.entity.Proxy;
import eu.europeana.corelib.definitions.edm.entity.Timespan;

/**
 * Interface to retrieve connections between parts of a FullBean
 *
 * @author Peter.Kiraly@europeana.eu
 */
public interface FullBeanLinkable {

	public Proxy getEuropeanaProxy();

	public List<Proxy> getProvidedProxies();

	public boolean isSingletonProxy();

	public Agent getAgentByURI(String uri);

	public Concept getConceptByURI(String uri);

	public Place getPlaceByURI(String uri);

	public Timespan getTimespanByURI(String uri);
}
