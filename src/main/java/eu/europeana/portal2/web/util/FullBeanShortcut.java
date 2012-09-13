package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.europeana.corelib.definitions.solr.entity.EuropeanaAggregation;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
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
				add("EdmProvider", aggregation.getEdmProvider()); // Map
				add("EdmObject", aggregation.getEdmObject());
				add("EdmUGC", aggregation.getEdmUgc());
				add("DataProvider", aggregation.getEdmDataProvider()); // Map
				add("AggregationDcRights", aggregation.getDcRights()); // Map
			}
		}

		if (document.getProxies() != null) {
			for (Proxy proxy : document.getProxies()) {
				add("DcContributor", proxy.getDcContributor()); // Map
				add("DcCoverage", proxy.getDcCoverage()); // Map
				add("DcCreator", proxy.getDcCreator()); // Map
				add("DcDate", proxy.getDcDate()); // Map
				add("DcDescription", proxy.getDcDescription()); // Map
				add("DcFormat", proxy.getDcFormat()); // Map
				add("DcIdentifier", proxy.getDcIdentifier()); // Map
				add("DcLanguage", proxy.getDcLanguage()); // Map
				add("DcPublisher", proxy.getDcPublisher()); // Map
				add("DcRelation", proxy.getDcRelation()); // Map
				add("DcRights", proxy.getDcRights()); // Map
				add("DcSource", proxy.getDcSource()); // Map
				add("DcSubject", proxy.getDcSubject()); // Map
				add("DcTitle", proxy.getDcTitle()); // Map
				add("DcType", proxy.getDcType()); // Map
				add("DctermsAlternative", proxy.getDctermsAlternative()); // Map
				add("DctermsConformsTo", proxy.getDctermsConformsTo()); // Map
				add("DctermsCreated", proxy.getDctermsCreated()); // Map
				add("DctermsExtent", proxy.getDctermsExtent()); // Map
				add("DctermsHasFormat", proxy.getDctermsHasFormat()); // Map
				add("DctermsHasPart", proxy.getDctermsHasPart()); // Map
				add("DctermsHasVersion", proxy.getDctermsHasVersion()); // Map
				add("DctermsIsFormatOf", proxy.getDctermsIsFormatOf()); // Map
				add("DctermsIsReferencedBy", proxy.getDctermsIsReferencedBy()); // Map
				add("DctermsIsReplacedBy", proxy.getDctermsIsReplacedBy()); // Map
				add("DctermsIsRequiredBy", proxy.getDctermsIsRequiredBy()); // Map
				add("DctermsIsPartOf", proxy.getDctermsIsPartOf()); // Map
				add("DctermsIssued", proxy.getDctermsIssued()); // Map
				add("DctermsIsVersionOf", proxy.getDctermsIsVersionOf()); // Map
				add("DctermsMedium", proxy.getDctermsMedium()); // Map
				add("DctermsProvenance", proxy.getDctermsProvenance()); // Map
				add("DctermsReferences", proxy.getDctermsReferences()); // Map
				add("DctermsReplaces", proxy.getDctermsReplaces()); // Map
				add("DctermsRequires", proxy.getDctermsRequires()); // Map
				add("DctermsSpatial", proxy.getDctermsSpatial()); // Map
				add("DctermsTemporal", proxy.getDctermsTemporal()); // Map
				add("DctermsTableOfContents", proxy.getDctermsTOC()); // Map
			}
		}
		
		if (document.getEuropeanaAggregation() != null) {
			EuropeanaAggregation aggregation = document.getEuropeanaAggregation();
			add("EdmCountry", aggregation.getEdmCountry()); // Map
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
