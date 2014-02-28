package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.EuropeanaAggregation;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.WebResource;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import eu.europeana.corelib.solr.entity.AggregationImpl;
import eu.europeana.corelib.solr.entity.PlaceImpl;
import eu.europeana.corelib.utils.StringArrayUtils;

/**
 * This class provides shortcuts to Aggregation or Proxy properties
 * 
 * @author peter.kiraly@kb.nl
 */
public class FullBeanShortcut {

	private FullBeanImpl document;

	public FullBeanShortcut(FullBeanImpl document) {
		this.document = document;
	}

	private Map<String, List<String>> values;

	private Map<String, List<Map<String, List<String>>>> mapValues;

	private Map<String, List<Float>> floats;

	private Map<String, Float[]> floatArrays;

	/**
	 * Adds multiple values
	 * @param property
	 *   The property name
	 * @param _values
	 *   The array of values
	 */
	private void add(String parent, String property, String[] _values) {
		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!values.containsKey(qualifiedProperty)) {
			values.put(qualifiedProperty, new ArrayList<String>());
		}
		StringArrayUtils.addToList(values.get(property), _values);
		StringArrayUtils.addToList(values.get(qualifiedProperty), _values);
	}

	/**
	 * Adds a single value
	 * 
	 * @param property
	 *   The property name
	 * @param value
	 *   The value
	 */
	private void add(String parent, String property, String value) {
		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!values.containsKey(qualifiedProperty)) {
			values.put(qualifiedProperty, new ArrayList<String>());
		}
		values.get(property).add(value);
		values.get(qualifiedProperty).add(value);
	}

	private void add(String parent, String property, Map<String, List<String>> value) {
		if (value == null) {
			return;
		}

		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!values.containsKey(qualifiedProperty)) {
			values.put(qualifiedProperty, new ArrayList<String>());
		}

		List<String> propertyValues = values.get(property);
		List<String> qPropertyValues = values.get(qualifiedProperty);
		for (Entry<String, List<String>> entry : value.entrySet()) {
			// adding only the value, not the language
			propertyValues.addAll(entry.getValue());
			qPropertyValues.addAll(entry.getValue());
		}

		// saving it into mapValues as well
		if (!mapValues.containsKey(property)) {
			mapValues.put(property, new ArrayList<Map<String, List<String>>>());
		}
		if (!mapValues.containsKey(qualifiedProperty)) {
			mapValues.put(qualifiedProperty, new ArrayList<Map<String, List<String>>>());
		}
		
		mapValues.get(property).add(value);
		mapValues.get(qualifiedProperty).add(value);
	}

	private void addFloat(String parent, String property, Float value) {
		if (!floats.containsKey(property)) {
			floats.put(property, new ArrayList<Float>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!floats.containsKey(qualifiedProperty)) {
			floats.put(qualifiedProperty, new ArrayList<Float>());
		}
		floats.get(property).add(value);
		floats.get(qualifiedProperty).add(value);
	}

	/**
	 * Initializes the values
	 */
	private void initialize() {
		values = new HashMap<String, List<String>>();
		mapValues = new HashMap<String, List<Map<String, List<String>>>>();

		String parent = null;
		if (document.getAggregations() != null) {
			parent = "Aggregation";
			for (AggregationImpl aggregation : document.getAggregations()) {
				add(parent, "EdmIsShownBy", aggregation.getEdmIsShownBy());
				add(parent, "EdmIsShownAt", aggregation.getEdmIsShownAt());
				add(parent, "EdmRights", aggregation.getEdmRights()); // Map
				add(parent, "EdmProvider", aggregation.getEdmProvider()); // Map - Aggregation/edm:provider
				add(parent, "EdmObject", aggregation.getEdmObject());
				add(parent, "EdmUGC", aggregation.getEdmUgc());
				add(parent, "DataProvider", aggregation.getEdmDataProvider()); // Map - Aggregation/edm:dataProvider
				add(parent, "AggregationDcRights", aggregation.getDcRights()); // Map
				add(parent, "EdmHasView", aggregation.getHasView()); // Map
				for (WebResource webResource : aggregation.getWebResources()) {
					add(parent, "WebResourceAbout", webResource.getAbout()); // Map
				}
			}
		}

		if (document.getProxies() != null) {
			parent = "Proxy";
			for (Proxy proxy : document.getProxies()) {
				add(parent, "DcContributor", proxy.getDcContributor()); // Map
				add(parent, "DcCoverage", proxy.getDcCoverage()); // Map
				add(parent, "DcCreator", proxy.getDcCreator()); // Map
				add(parent, "DcDate", proxy.getDcDate()); // Map
				add(parent, "DcDescription", proxy.getDcDescription()); // Map - Proxy/dc:description
				add(parent, "DcFormat", proxy.getDcFormat()); // Map
				add(parent, "DcIdentifier", proxy.getDcIdentifier()); // Map
				add(parent, "DcLanguage", proxy.getDcLanguage()); // Map
				add(parent, "DcPublisher", proxy.getDcPublisher()); // Map
				add(parent, "DcRelation", proxy.getDcRelation()); // Map - Proxy/dc:relation
				add(parent, "DcRights", proxy.getDcRights()); // Map
				add(parent, "DcSource", proxy.getDcSource()); // Map
				add(parent, "DcSubject", proxy.getDcSubject()); // Map - Proxy/dc:subject
				add(parent, "DcTitle", proxy.getDcTitle()); // Map
				add(parent, "DcType", proxy.getDcType()); // Map - Proxy/dc:type
				add(parent, "DctermsAlternative", proxy.getDctermsAlternative()); // Map
				add(parent, "DctermsConformsTo", proxy.getDctermsConformsTo()); // Map - Proxy/dcterms:conformsTo
				add(parent, "DctermsCreated", proxy.getDctermsCreated()); // Map
				add(parent, "DctermsExtent", proxy.getDctermsExtent()); // Map
				add(parent, "DctermsHasFormat", proxy.getDctermsHasFormat()); // Map - Proxy/dcterms:hasFormat
				add(parent, "DctermsHasPart", proxy.getDctermsHasPart()); // Map - Proxy/dcterms:hasPart
				add(parent, "DctermsHasVersion", proxy.getDctermsHasVersion()); // Map - Proxy/dcterms:hasVersion
				add(parent, "DctermsIsFormatOf", proxy.getDctermsIsFormatOf()); // Map - Proxy/dcterms:isFormatOf
				add(parent, "DctermsIsPartOf", proxy.getDctermsIsPartOf()); // Map - Proxy/dcterms:isPartOf
				add(parent, "DctermsIsReferencedBy", proxy.getDctermsIsReferencedBy()); // Map - Proxy/dcterms:isReferencedBy
				add(parent, "DctermsIsReplacedBy", proxy.getDctermsIsReplacedBy()); // Map - Proxy/dcterms:isReplacedBy
				add(parent, "DctermsIsRequiredBy", proxy.getDctermsIsRequiredBy()); // Map - Proxy/dcterms:isRequiredBy
				add(parent, "DctermsIssued", proxy.getDctermsIssued()); // Map
				add(parent, "DctermsIsVersionOf", proxy.getDctermsIsVersionOf()); // Map - Proxy/dcterms:isVersionOf
				add(parent, "DctermsMedium", proxy.getDctermsMedium()); // Map
				add(parent, "DctermsProvenance", proxy.getDctermsProvenance()); // Map
				add(parent, "DctermsReferences", proxy.getDctermsReferences()); // Map - Proxy/dcterms:references
				add(parent, "DctermsReplaces", proxy.getDctermsReplaces()); // Map - Proxy/dcterms:replaces
				add(parent, "DctermsRequires", proxy.getDctermsRequires()); // Map - Proxy/dcterms:requires
				add(parent, "DctermsSpatial", proxy.getDctermsSpatial()); // Map - Proxy/dcterms:spatial
				add(parent, "DctermsTableOfContents", proxy.getDctermsTOC()); // Map - Proxy/dcterms:tableOfContents
				add(parent, "DctermsTemporal", proxy.getDctermsTemporal()); // Map - Proxy/dcterms:temporal
				add(parent, "EdmCurrentLocation", proxy.getEdmCurrentLocation());
				add(parent, "EdmHasMet", proxy.getEdmHasMet()); // Map - Proxy/dcterms:temporal
				add(parent, "EdmHasType", proxy.getEdmHasType());
				add(parent, "EdmIncorporates", proxy.getEdmIncorporates());
				add(parent, "EdmIsDerivativeOf", proxy.getEdmIsDerivativeOf());
				add(parent, "EdmIsRelatedTo", proxy.getEdmIsRelatedTo());
				add(parent, "EdmIsRepresentationOf", proxy.getEdmIsRepresentationOf());
				add(parent, "EdmIsSimilarTo", proxy.getEdmIsSimilarTo());
				add(parent, "EdmIsSuccessorOf", proxy.getEdmIsSuccessorOf());
				add(parent, "EdmRealizes", proxy.getEdmRealizes());
				add(parent, "EdmIsNextInSequence", proxy.getEdmIsNextInSequence());
			}
		}

		if (document.getAgents() != null) {
			parent = "Agent";
			for (Agent agent : document.getAgents()) {
				add(parent, "AgentPrefLabel", agent.getPrefLabel()); // Map
			}
		}

		if (document.getConcepts() != null) {
			parent = "Concept";
			for (Concept concept : document.getConcepts()) {
				add(parent, "ConceptPrefLabel", concept.getPrefLabel()); // Map
				add(parent, "ConceptBroader", concept.getBroader()); // String[]
				add(parent, "ConceptAltLabel", concept.getAltLabel()); // Map
			}
		}

		if (document.getEuropeanaAggregation() != null) {
			parent = "EuropeanaAggregation";
			EuropeanaAggregation aggregation = document.getEuropeanaAggregation();
			add(parent, "EdmCountry", aggregation.getEdmCountry()); // Map - EuropeanaAggregation/edm:country
			add(parent, "EdmLanguage", aggregation.getEdmLanguage()); // Map
			add(parent, "EdmLandingPage", aggregation.getEdmLandingPage()); // String
		}
	}

	/**
	 * Gets values of a property
	 *
	 * @param property
	 *   The name of a property
	 * @return
	 *   All the values registered for that property
	 */
	public String[] get(String property) {
		if (values == null) {
			initialize();
		}

		if (values.containsKey(property)) {
			return StringArrayUtils.toArray(values.get(property));
		}
		return null;
	}

	public String[] get(String property, String parent) {
		return get(parent + "/" + property);
	}

	public List<String> getList(String property) {
		if (values == null) {
			initialize();
		}

		if (values.containsKey(property)) {
			return values.get(property);
		}
		return null;
	}

	public List<String> getList(String property, String parent) {
		return getList(parent + "/" + property);
	}

	public void initializeFloats() {
		floats = new HashMap<String, List<Float>>();
		floatArrays = new HashMap<String, Float[]>();
		if (document.getPlaces() != null && document.getPlaces().size() > 0) {
			String parent = "Place";
			for (PlaceImpl place : document.getPlaces()) {
				addFloat(parent, "EdmPlaceLatitude", place.getLatitude());
				addFloat(parent, "EdmPlaceLongitude", place.getLongitude());
				addFloat(parent, "EdmPlaceAltitude", place.getAltitude());
			}
		}
	}

	public Float[] getFloat(String property) {
		if (floats == null) {
			initializeFloats();
		}

		if (floats.containsKey(property)) {
			if (!floatArrays.containsKey(property)) {
				floatArrays.put(property, floats.get(property).toArray(new Float[floats.get(property).size()]));
			}
			return floatArrays.get(property);
		}
		return null;
	}

	public Float[] getFloat(String property, String parent) {
		return getFloat(parent + "/" + property);
	}

	public Float[] getEdmPlaceLatitude() {
		return getFloat("EdmPlaceLatitude");
	}

	public Float[] getEdmPlaceLongitude() {
		return getFloat("EdmPlaceLongitude");
	}
}
