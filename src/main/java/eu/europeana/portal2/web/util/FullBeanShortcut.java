package eu.europeana.portal2.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				add("DcIdentifier", proxy.getDcIdentifier());
				add("DcPublisher", proxy.getDcPublisher());
				add("DcRelation", proxy.getDcRelation());
				add("DcSource", proxy.getDcSource());
				add("DctermsAlternative", proxy.getDctermsAlternative());
				add("DctermsConformsTo", proxy.getDctermsConformsTo());
				add("DctermsCreated", proxy.getDctermsCreated());
				add("DctermsExtent", proxy.getDctermsExtent());
				add("DctermsHasFormat", proxy.getDctermsHasFormat());
				add("DctermsHasPart", proxy.getDctermsHasPart());
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
