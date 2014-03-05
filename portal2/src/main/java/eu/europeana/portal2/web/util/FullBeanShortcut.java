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
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.submodel.Resource;

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

	private Map<String, List<String>> valuesListMap;

	private Map<String, List<Map<String, List<String>>>> mapValues;

	private Map<String, List<Float>> floats;

	private Map<String, Float[]> floatArrays;

	private Map<String, EnrichmentFieldMapper> enrichedMap;

	private Map<String, Map<String, Resource>> resourceValueMap;

	private Map<String, List<String>> resourceUris;

	/**
	 * Adds multiple values
	 * @param property
	 *   The property name
	 * @param _values
	 *   The array of values
	 */
	private void add(String parent, String property, String[] _values) {
		if (!valuesListMap.containsKey(property)) {
			valuesListMap.put(property, new ArrayList<String>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!valuesListMap.containsKey(qualifiedProperty)) {
			valuesListMap.put(qualifiedProperty, new ArrayList<String>());
		}
		StringArrayUtils.addToList(valuesListMap.get(property), _values);
		StringArrayUtils.addToList(valuesListMap.get(qualifiedProperty), _values);
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
		if (!valuesListMap.containsKey(property)) {
			valuesListMap.put(property, new ArrayList<String>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!valuesListMap.containsKey(qualifiedProperty)) {
			valuesListMap.put(qualifiedProperty, new ArrayList<String>());
		}
		valuesListMap.get(property).add(value);
		valuesListMap.get(qualifiedProperty).add(value);
	}

	private void add(String parent, String property, Map<String, List<String>> value) {
		if (value == null) {
			return;
		}

		if (!valuesListMap.containsKey(property)) {
			valuesListMap.put(property, new ArrayList<String>());
		}
		String qualifiedProperty = parent + "/" + property;
		if (!valuesListMap.containsKey(qualifiedProperty)) {
			valuesListMap.put(qualifiedProperty, new ArrayList<String>());
		}

		List<String> propertyValues = valuesListMap.get(property);
		List<String> qPropertyValues = valuesListMap.get(qualifiedProperty);
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
		valuesListMap = new HashMap<String, List<String>>();
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

				saveEnrichmentCandidate(Field.DC_CREATOR.name(), proxy.isEuropeanaProxy(), proxy.getDcCreator());
				saveEnrichmentCandidate(Field.DC_CONTRIBUTOR.name(), proxy.isEuropeanaProxy(), proxy.getDcContributor());
				saveEnrichmentCandidate(Field.DC_SUBJECT.name(), proxy.isEuropeanaProxy(), proxy.getDcSubject());
				saveEnrichmentCandidate(Field.DC_TYPE.name(), proxy.isEuropeanaProxy(), proxy.getDcType());
				saveEnrichmentCandidate(Field.DCTERMS_SPATIAL.name(), proxy.isEuropeanaProxy(), proxy.getDctermsSpatial());
				saveEnrichmentCandidate(Field.DC_COVERAGE.name(), proxy.isEuropeanaProxy(), proxy.getDcCoverage());
				saveEnrichmentCandidate(Field.DC_DATE.name(), proxy.isEuropeanaProxy(), proxy.getDcDate());
				saveEnrichmentCandidate(Field.DCTERMS_TEMPORAL.name(), proxy.isEuropeanaProxy(), proxy.getDctermsTemporal());
			}
			mapEnrichments();
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
	 * Maps the enrichments, and see whether a Europeana provided field could
	 * refer to a provider provided field. Creates Resource objects if that
	 * happens, and updates auxiliary variables for sake of findability
	 */
	private void mapEnrichments() {
		resourceValueMap = new HashMap<String, Map<String, Resource>>();
		resourceUris = new HashMap<String, List<String>>();

		for (String name : enrichedMap.keySet()) {
			EnrichmentFieldMapper cardinalities = enrichedMap.get(name);
			cardinalities.setEnriched();
			List<Resource> resources = cardinalities.getResources();
			if (resources != null) {
				Map<String, Resource> resourceValueMapEntry = new HashMap<String, Resource>();
				resourceValueMap.put(name, resourceValueMapEntry);
				List<String> resourceUrisEntry = new ArrayList<String>();
				resourceUris.put(name, resourceUrisEntry);
				for (Resource resource : resources) {
					resourceValueMapEntry.put(resource.getValue(), resource);
					resourceUrisEntry.add(resource.getUri());
				}
			}
		}
	}

	public boolean isEnriched(String fieldName) {
		if (enrichedMap.containsKey(fieldName)) {
			return enrichedMap.get(fieldName).isEnriched();
		}
		return false;
	}

	public boolean isResourceValue(String fieldName, String fieldValue) {
		if (resourceValueMap.containsKey(fieldName)
			&& resourceValueMap.get(fieldName).keySet().contains(fieldValue)) {
			return true;
		}
		return false;
	}

	/**
	 * Get Resource object belongs to this field instance
	 * @param fieldName
	 *   EDM field name
	 * @param fieldValue
	 *   A single field value
	 * @return
	 *   The Resource object belongs to the instance
	 */
	public Resource getResource(String fieldName, String fieldValue) {
		if (resourceValueMap.containsKey(fieldName)
			&& resourceValueMap.get(fieldName).keySet().contains(fieldValue)) {
			return resourceValueMap.get(fieldName).get(fieldValue);
		}
		return null;
	}

	/**
	 * Returns true is the field value is a URI of a Resource object
	 * @param fieldName
	 *   EDM field name
	 * @param fieldValue
	 *   A simple field value
	 * @return
	 */
	public boolean isResourceUri(String fieldName, String fieldValue) {
		if (resourceValueMap.containsKey(fieldName)
			&& resourceUris.get(fieldName).contains(fieldValue)) {
			return true;
		}
		return false;
	}

	/**
	 * Save enrichments temporary
	 * @param name
	 *   EDM field name
	 * @param europeanaProxy
	 *   Flag denotes whether the field comes from an Europeana proxy or a provider proxy
	 * @param fieldValues
	 *   Multilingual field values
	 */
	private void saveEnrichmentCandidate(String name, boolean europeanaProxy,
			Map<String, List<String>> fieldValues) {
		if (fieldValues == null) {
			return;
		}

		if (enrichedMap == null) {
			enrichedMap = new HashMap<String, EnrichmentFieldMapper>();
		}

		EnrichmentFieldMapper enrichmentFieldMapper;
		if (!enrichedMap.containsKey(name)) {
			enrichmentFieldMapper = new EnrichmentFieldMapper();
			enrichedMap.put(name, enrichmentFieldMapper);
		} else {
			enrichmentFieldMapper = enrichedMap.get(name);
		}

		MultilangFieldValue cardinality = new MultilangFieldValue(fieldValues);
		if (europeanaProxy) {
			enrichmentFieldMapper.setEuropeana(cardinality);
		} else {
			enrichmentFieldMapper.setProvided(cardinality);
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
		if (valuesListMap == null) {
			initialize();
		}

		if (valuesListMap.containsKey(property)) {
			return StringArrayUtils.toArray(valuesListMap.get(property));
		}
		return null;
	}

	public String[] get(String property, String parent) {
		return get(parent + "/" + property);
	}

	public List<String> getList(String property) {
		if (valuesListMap == null) {
			initialize();
		}

		if (valuesListMap.containsKey(property)) {
			return valuesListMap.get(property);
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

	private class EnrichmentFieldMapper {

		private MultilangFieldValue provided;
		private MultilangFieldValue europeana;
		private Boolean isEnriched = false;
		private List<Resource> resources = null;

		public void setProvided(MultilangFieldValue provided) {
			this.provided = provided;
		}

		public void setEuropeana(MultilangFieldValue europeana) {
			this.europeana = europeana;
		}

		public Boolean isEnriched() {
			return isEnriched;
		}

		public void setEnriched() {
			if (europeana == null) {
				isEnriched = false;
			} else {
				isEnriched = provided.isSimilar(europeana);
			}
			if (isEnriched) {
				createResources();
			}
		}

		private List<Resource> getResources() {
			return resources;
		}

		private void createResources() {
			resources = new ArrayList<Resource>();
			for (String lang : provided.getLangs()) {
				resources.add(new Resource(provided.get(lang).get(0), europeana.get(lang).get(0)));
			}
		}
	}

	private class MultilangFieldValue {

		private Map<String, List<String>> langValueMap;

		public MultilangFieldValue(Map<String, List<String>> langValueMap) {
			this.langValueMap = langValueMap;
		}

		public boolean has(String lang) {
			return langValueMap.containsKey(lang);
		}

		public List<String> get(String lang) {
			return langValueMap.get(lang);
		}

		public Integer getSize(String lang) {
			return langValueMap.get(lang).size();
		}

		public List<String> getLangs() {
			List<String> langs = new ArrayList<String>();
			langs.addAll(langValueMap.keySet());
			return langs;
		}

		public boolean isSimilar(MultilangFieldValue other) {
			boolean similar = false;
			for (String lang : langValueMap.keySet()) {
				if (!other.has(lang)) {
					similar = false;
					break;
				}
				if (getSize(lang) != 1 || other.getSize(lang) != 1) {
					similar = false;
					break;
				}
				similar = true;
			}
			return similar;
		}
	}
}
