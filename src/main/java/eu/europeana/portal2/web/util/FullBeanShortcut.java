package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	/**
	 * Adds multiple values
	 * @param property
	 *   The property name
	 * @param _values
	 *   The array of values
	 */
	private void add(String property, String[] _values) {
		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}
		StringArrayUtils.addToList(values.get(property), _values);
	}

	/**
	 * Adds a single value
	 * 
	 * @param property
	 *   The property name
	 * @param value
	 *   The value
	 */
	private void add(String property, String value) {
		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}
		values.get(property).add(value);
	}

	private void add(String property, Map<String, List<String>> value) {
		if (value == null) {
			return;
		}

		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}

		for (Entry<String, List<String>> entry : value.entrySet()) {
			values.get(property).addAll(entry.getValue()); // adding only the value, not the language
		}

		// saving it into mapValues as well
		if (!mapValues.containsKey(property)) {
			mapValues.put(property, new ArrayList<Map<String, List<String>>>());
		}
		mapValues.get(property).add(value);
	}

	private void addFloat(String property, Float value) {
		if (!floats.containsKey(property)) {
			floats.put(property, new ArrayList<Float>());
		}
		floats.get(property).add(value);
	}

	/**
	 * Initializes the values
	 */
	private void initialize() {
		values = new HashMap<String, List<String>>();
		mapValues = new HashMap<String, List<Map<String, List<String>>>>();

		if (document.getAggregations() != null) {
			for (AggregationImpl aggregation : document.getAggregations()) {
				add("EdmIsShownBy", aggregation.getEdmIsShownBy());
				add("EdmIsShownAt", aggregation.getEdmIsShownAt());
				add("EdmRights", aggregation.getEdmRights()); // Map
				add("EdmProvider", aggregation.getEdmProvider()); // Map - Aggregation/edm:provider
				add("EdmObject", aggregation.getEdmObject());
				add("EdmUGC", aggregation.getEdmUgc());
				add("DataProvider", aggregation.getEdmDataProvider()); // Map - Aggregation/edm:dataProvider
				add("AggregationDcRights", aggregation.getDcRights()); // Map
				add("EdmHasView", aggregation.getHasView()); // Map
				for (WebResource webResource : aggregation.getWebResources()) {
					add("WebResourceAbout", webResource.getAbout()); // Map
				}
			}
		}

		if (document.getProxies() != null) {
			for (Proxy proxy : document.getProxies()) {
				add("DcContributor", proxy.getDcContributor()); // Map
				add("DcCoverage", proxy.getDcCoverage()); // Map
				add("DcCreator", proxy.getDcCreator()); // Map
				add("DcDate", proxy.getDcDate()); // Map
				add("DcDescription", proxy.getDcDescription()); // Map - Proxy/dc:description
				add("DcFormat", proxy.getDcFormat()); // Map
				add("DcIdentifier", proxy.getDcIdentifier()); // Map
				add("DcLanguage", proxy.getDcLanguage()); // Map
				add("DcPublisher", proxy.getDcPublisher()); // Map
				add("DcRelation", proxy.getDcRelation()); // Map - Proxy/dc:relation
				add("DcRights", proxy.getDcRights()); // Map
				add("DcSource", proxy.getDcSource()); // Map
				add("DcSubject", proxy.getDcSubject()); // Map - Proxy/dc:subject
				add("DcTitle", proxy.getDcTitle()); // Map
				add("DcType", proxy.getDcType()); // Map - Proxy/dc:type
				add("DctermsAlternative", proxy.getDctermsAlternative()); // Map
				add("DctermsConformsTo", proxy.getDctermsConformsTo()); // Map - Proxy/dcterms:conformsTo
				add("DctermsCreated", proxy.getDctermsCreated()); // Map
				add("DctermsExtent", proxy.getDctermsExtent()); // Map
				add("DctermsHasFormat", proxy.getDctermsHasFormat()); // Map - Proxy/dcterms:hasFormat
				add("DctermsHasPart", proxy.getDctermsHasPart()); // Map - Proxy/dcterms:hasPart
				add("DctermsHasVersion", proxy.getDctermsHasVersion()); // Map - Proxy/dcterms:hasVersion
				add("DctermsIsFormatOf", proxy.getDctermsIsFormatOf()); // Map - Proxy/dcterms:isFormatOf
				add("DctermsIsPartOf", proxy.getDctermsIsPartOf()); // Map - Proxy/dcterms:isPartOf
				add("DctermsIsReferencedBy", proxy.getDctermsIsReferencedBy()); // Map - Proxy/dcterms:isReferencedBy
				add("DctermsIsReplacedBy", proxy.getDctermsIsReplacedBy()); // Map - Proxy/dcterms:isReplacedBy
				add("DctermsIsRequiredBy", proxy.getDctermsIsRequiredBy()); // Map - Proxy/dcterms:isRequiredBy
				add("DctermsIssued", proxy.getDctermsIssued()); // Map
				add("DctermsIsVersionOf", proxy.getDctermsIsVersionOf()); // Map - Proxy/dcterms:isVersionOf
				add("DctermsMedium", proxy.getDctermsMedium()); // Map
				add("DctermsProvenance", proxy.getDctermsProvenance()); // Map
				add("DctermsReferences", proxy.getDctermsReferences()); // Map - Proxy/dcterms:references
				add("DctermsReplaces", proxy.getDctermsReplaces()); // Map - Proxy/dcterms:replaces
				add("DctermsRequires", proxy.getDctermsRequires()); // Map - Proxy/dcterms:requires
				add("DctermsSpatial", proxy.getDctermsSpatial()); // Map - Proxy/dcterms:spatial
				add("DctermsTableOfContents", proxy.getDctermsTOC()); // Map - Proxy/dcterms:tableOfContents
				add("DctermsTemporal", proxy.getDctermsTemporal()); // Map - Proxy/dcterms:temporal
				add("EdmHasMet", proxy.getEdmHasMet()); // Map - Proxy/dcterms:temporal
				add("EdmHasType", proxy.getEdmHasType());
				add("EdmIncorporates", proxy.getEdmIncorporates());
				add("EdmIsDerivativeOf", proxy.getEdmIsDerivativeOf());
				add("EdmIsRelatedTo", proxy.getEdmIsRelatedTo());
				add("EdmIsRepresentationOf", proxy.getEdmIsRepresentationOf());
				add("EdmIsSimilarTo", proxy.getEdmIsSimilarTo());
				add("EdmIsSuccessorOf", proxy.getEdmIsSuccessorOf());
				add("EdmRealizes", proxy.getEdmRealizes());
				add("EdmIsNextInSequence", proxy.getEdmIsNextInSequence());
			}
		}

		if (document.getEuropeanaAggregation() != null) {
			EuropeanaAggregation aggregation = document.getEuropeanaAggregation();
			add("EdmCountry", aggregation.getEdmCountry()); // Map - EuropeanaAggregation/edm:country
			add("EdmLanguage", aggregation.getEdmLanguage()); // Map
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

	public void initializeFloats() {
		floats = new HashMap<String, List<Float>>();
		if (document.getPlaces() != null && document.getPlaces().size() > 0) {
			for (PlaceImpl place : document.getPlaces()) {
				addFloat("EdmPlaceLatitude", place.getLatitude());
				addFloat("EdmPlaceLongitude", place.getLongitude());
				addFloat("EdmPlaceAltitude", place.getAltitude());
			}
		}
	}

	public Float[] getFloat(String property) {
		if (floats == null) {
			initializeFloats();
		}

		if (floats.containsKey(property)) {
			floats.get(property).toArray(new Float[floats.get(property).size()]);
		}
		return null;
	}

	public Float[] getEdmPlaceLatitude() {
		return getFloat("EdmPlaceLatitude");
	}

	public Float[] getEdmPlaceLongitude() {
		return getFloat("EdmPlaceLongitude");
	}
}
