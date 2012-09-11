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

	private Map<String, List<Map<String, String>>> mapValues;

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

	private void add(String property, Map<String, String> value) {
		if (value == null) {
			return;
		}

		if (!values.containsKey(property)) {
			values.put(property, new ArrayList<String>());
		}

		for (Entry<String, String> entry : value.entrySet()) {
			values.get(property).add(entry.getValue()); // adding only the value, not the language
		}

		// saving it into mapValues as well
		if (!mapValues.containsKey(property)) {
			mapValues.put(property, new ArrayList<Map<String, String>>());
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
		mapValues = new HashMap<String, List<Map<String, String>>>();

		if (document.getAggregations() != null) {
			for (AggregationImpl aggregation : document.getAggregations()) {
				add("EdmIsShownBy", aggregation.getEdmIsShownBy());
				add("EdmIsShownAt", aggregation.getEdmIsShownAt());
				add("EdmRights", aggregation.getEdmRights());
				add("EdmProvider", aggregation.getEdmProvider());
				add("EdmObject", aggregation.getEdmObject());
				add("EdmUGC", aggregation.getEdmUgc());
				add("DataProvider", aggregation.getEdmDataProvider());
				add("AggregationDcRights", aggregation.getDcRights());
			}
		}

		if (document.getProxies() != null) {
			for (Proxy proxy : document.getProxies()) {
				add("DcContributor", proxy.getDcContributor());
				add("DcCoverage", proxy.getDcCoverage());
				add("DcCreator", proxy.getDcCreator());
				add("DcDate", proxy.getDcDate());
				add("DcDescription", proxy.getDcDescription());
				add("DcFormat", proxy.getDcFormat());
				add("DcIdentifier", proxy.getDcIdentifier());
				add("DcLanguage", proxy.getDcLanguage());
				add("DcPublisher", proxy.getDcPublisher());
				add("DcRelation", proxy.getDcRelation());
				add("DcRights", proxy.getDcRights());
				add("DcSource", proxy.getDcSource());
				add("DcSubject", proxy.getDcSubject());
				add("DcTitle", proxy.getDcTitle());
				add("DcType", proxy.getDcType());
				add("DctermsAlternative", proxy.getDctermsAlternative());
				add("DctermsConformsTo", proxy.getDctermsConformsTo());
				add("DctermsCreated", proxy.getDctermsCreated());
				add("DctermsExtent", proxy.getDctermsExtent());
				add("DctermsHasFormat", proxy.getDctermsHasFormat());
				add("DctermsHasPart", proxy.getDctermsHasPart());
				add("DctermsHasVersion", proxy.getDctermsHasVersion());
				add("DctermsIsFormatOf", proxy.getDctermsIsFormatOf());
				add("DctermsIsReferencedBy", proxy.getDctermsIsReferencedBy());
				add("DctermsIsReplacedBy", proxy.getDctermsIsReplacedBy());
				add("DctermsIsRequiredBy", proxy.getDctermsIsRequiredBy());
				add("DctermsIsPartOf", proxy.getDctermsIsPartOf());
				add("DctermsIssued", proxy.getDctermsIssued());
				add("DctermsIsVersionOf", proxy.getDctermsIsVersionOf());
				add("DctermsMedium", proxy.getDctermsMedium());
				add("DctermsProvenance", proxy.getDctermsProvenance());
				add("DctermsReferences", proxy.getDctermsReferences());
				add("DctermsReplaces", proxy.getDctermsReplaces());
				add("DctermsRequires", proxy.getDctermsRequires());
				add("DctermsSpatial", proxy.getDctermsSpatial());
				add("DctermsTemporal", proxy.getDctermsTemporal());
				add("DctermsTableOfContents", proxy.getDctermsTOC());
			}
		}
		
		if (document.getEuropeanaAggregation() != null) {
			EuropeanaAggregation aggregation = document.getEuropeanaAggregation();
			add("EdmCountry", aggregation.getEdmCountry());
			add("EdmLanguage", aggregation.getEdmLanguage());
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
