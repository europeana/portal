/*
 * Copyright 2007-2013 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.presentation.model.preparation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import eu.europeana.corelib.definitions.edm.entity.Agent;
import eu.europeana.corelib.definitions.edm.entity.Concept;
import eu.europeana.corelib.definitions.edm.entity.Place;
import eu.europeana.corelib.definitions.edm.entity.Timespan;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.presentation.model.data.submodel.FieldPresentation;

public abstract class FullDocPreparation extends FullDocData {

	private final Logger log = Logger.getLogger(FullDocPreparation.class);

	// caching fields
	private List<FieldPresentation> fields;
	private Map<Field, List<String>> hiddenFields;
	private List<FieldPresentation> fieldsLightbox;
	private List<FieldPresentation> fieldsAdditional;
	private Map<String, List<FieldPresentation>> fieldsEnrichment;

	/**
	 * Returns the fieldsLightbox for the show more section where values exist
	 * for that field
	 * 
	 * @return Fields for lightbox section
	 */
	public List<FieldPresentation> getFieldsLightbox() throws Exception {
		if (fieldsLightbox == null) {
			fieldsLightbox = new ArrayList<FieldPresentation>();

			addField(fieldsLightbox, Field.DC_CREATOR, shortcut.get("DcCreator"));
			addField(fieldsLightbox, Field.DC_RIGHTS, shortcut.get("DcRights"));
			addField(fieldsLightbox, Field.EDM_DATAPROVIDER, shortcut.get("DataProvider"));
			addField(fieldsLightbox, Field.EDM_PROVIDER,
					document.getProvider(),
					Field.EDM_COUNTRY.getValues(document.getCountry()));
		}
		return fieldsLightbox;
	}

	/**
	 * Returns the fields for the show more section where values exist for that
	 * field
	 * 
	 * @return Fields for more section
	 */
	public List<FieldPresentation> getFieldsAdditional() throws Exception {
		if (fieldsAdditional == null) {
			fieldsAdditional = new ArrayList<FieldPresentation>();
			/*

			addField(fieldsAdditional, Field.DC_RIGHTS, shortcut.get("AggregationDcRights"));
			addField(fieldsAdditional, Field.DC_IDENTIFIER, shortcut.get("DcIdentifier"));
			addField(fieldsAdditional, Field.DC_FORMAT, 
					shortcut.get("DcFormat"),
					shortcut.get("DctermsExtent"),
					shortcut.get("DctermsMedium"));
			addField(fieldsAdditional, Field.DC_LANGUAGE, document.getLanguage());
			addField(fieldsAdditional, Field.DC_SOURCE, shortcut.get("DcSource"));
			addField(fieldsAdditional, Field.DCTERMS_PROVENANCE, shortcut.get("DctermsProvenance"));
			addField(fieldsAdditional, Field.DC_PUBLISHER, shortcut.get("DcPublisher"));
			addField(fieldsAdditional, Field.DCTERMS_ISSUED, shortcut.get("DctermsIssued"));
			addField(fieldsAdditional, Field.EUROPEANA_COLLECTIONNAME, document.getEuropeanaCollectionName());
			*/
		}
		return fieldsAdditional;
	}

	public Map<String, List<FieldPresentation>> getFieldsEnrichment() throws Exception {
		if (fieldsEnrichment == null) {
			prepareFieldsEnrichment();
		}
		return fieldsEnrichment;
	}

	/**
	 * List of enrichment fields containing information we want to be exposed to
	 * google
	 * 
	 * @return list of enrichment fields
	 * @throws Exception
	 */
	private void prepareFieldsEnrichment() throws Exception {
		fieldsEnrichment = new HashMap<String, List<FieldPresentation>>();
		fieldsEnrichment.put("enrichment_category_where_t", extractWhereFields());
		fieldsEnrichment.put("enrichment_category_who_t", extractWhoFields());
		fieldsEnrichment.put("enrichment_category_what_t", extractWhatFields());
		fieldsEnrichment.put("enrichment_category_when_t", extractWhenFields());
	}

	private List<FieldPresentation> extractWhenFields() {
		List<FieldPresentation> when = new ArrayList<FieldPresentation>();
		if (document.getTimespans() != null) {
			for (Timespan timespan : document.getTimespans()) {
				addField(when, Field.ENRICHMENT_TIMESPAN_ABOUT, new String[]{timespan.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (timespan.getPrefLabel() != null) {
					for (String language : timespan.getPrefLabel().keySet()) {
						labels.add(timespan.getPrefLabel().get(language) + " (" + language + ")");
					}
				}
				addField(when, Field.ENRICHMENT_TIMESPAN_LABEL, StringArrayUtils.toArray(labels));
				if (timespan.getBegin() != null) {
					for (Entry<String, List<String>> item : timespan.getBegin().entrySet()) {
						for (String value : item.getValue()) {
							addField(when, Field.ENRICHMENT_TIMESPAN_BEGIN, value);
						}
					}
				}
				if (timespan.getEnd() != null) {
					for (Entry<String, List<String>> item : timespan.getEnd().entrySet()) {
						for (String value : item.getValue()) {
							addField(when, Field.ENRICHMENT_TIMESPAN_END, value);
						}
					}
				}
			}
		}
		return when;
	}

	private List<FieldPresentation> extractWhatFields() {
		List<FieldPresentation> what = new ArrayList<FieldPresentation>();
		if (document.getConcepts() != null) {
			for (Concept concept : document.getConcepts()) {
				addField(what, Field.ENRICHMENT_CONCEPT_ABOUT, new String[]{concept.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (concept.getPrefLabel() != null) {
					for (String language : concept.getPrefLabel().keySet()) {
						labels.add(concept.getPrefLabel().get(language) + " (" + language + ")");
					}
				}
				addField(what, Field.ENRICHMENT_CONCEPT_LABEL, StringArrayUtils.toArray(labels));
				addField(what, Field.ENRICHMENT_CONCEPT_BROADER_LABEL, concept.getBroader());
			}
		}
		return what;
	}

	private List<FieldPresentation> extractWhoFields() {
		List<FieldPresentation> who = new ArrayList<FieldPresentation>();
		if (document.getAgents() != null) {
			for (Agent agent : document.getAgents()) {
				addField(who, Field.ENRICHMENT_AGENT_ABOUT, new String[]{agent.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (agent.getPrefLabel() != null) {
					for (String language : agent.getPrefLabel().keySet()) {
						labels.add(agent.getPrefLabel().get(language) + " (" + language + ")");
					}
				}
				addField(who, Field.ENRICHMENT_AGENT_LABEL, StringArrayUtils.toArray(labels));
			}
		}
		return who;
	}

	private List<FieldPresentation> extractWhereFields() {
		List<FieldPresentation> where = new ArrayList<FieldPresentation>();
		if (document.getPlaces() != null) {
			for (Place place : document.getPlaces()) {
				addField(where, Field.ENRICHMENT_PLACE_ABOUT, new String[]{place.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (place.getPrefLabel() != null) {
					for (String language : place.getPrefLabel().keySet()) {
						labels.add(place.getPrefLabel().get(language) + " (" + language + ")");
					}
				}
				addField(where, Field.ENRICHMENT_PLACE_LABEL, StringArrayUtils.toArray(labels));

				if (getDocument().isPositionAvailable()) {
					addField(where, Field.ENRICHMENT_PLACE_LAT_LONG,
							new String[]{String.valueOf(place.getLatitude())},
							new String[]{String.valueOf(place.getLongitude())},
							new String[]{getDocument().getUrlKml()});
				}
			}
		}
		return where;
	}

	/**
	 * Returns a collection of field
	 * 
	 * @return collection of fields
	 */
	public List<FieldPresentation> getFields() throws Exception {

		if (fields == null) {

			Map<Field, FieldPresentation> fieldMap = new LinkedHashMap<Field, FieldPresentation>();

			addFieldMap(fieldMap, Field.DCTERMS_ALTERNATIVE, shortcut.getList("DctermsAlternative"));
			addFieldMap(fieldMap, Field.DC_DESCRIPTION,
					shortcut.getList("DcDescription"),
					map(Field.DCTERMS_TABLEOFCONTENTS, shortcut.getList("DctermsTableOfContents"))
			);
			addFieldMap(fieldMap, Field.DC_CREATOR, shortcut.getList("DcCreator"));
			addFieldMap(fieldMap, Field.DC_CONTRIBUTOR, shortcut.getList("DcContributor"));
			addFieldMap(fieldMap, Field.DC_COVERAGE, shortcut.getList("DcCoverage"));
			addFieldMap(fieldMap, Field.DCTERMS_SPATIAL, shortcut.getList("DctermsSpatial"));
			addFieldMap(fieldMap, Field.DC_DATE, shortcut.getList("DcDate"));
			addFieldMap(fieldMap, Field.DCTERMS_TEMPORAL, shortcut.getList("DctermsTemporal"));
			addFieldMap(fieldMap, Field.DCTERMS_ISSUED, shortcut.getList("DctermsIssued"));
			addFieldMap(fieldMap, Field.DCTERMS_CREATED, shortcut.getList("DctermsCreated"));
			addFieldMap(fieldMap, Field.DC_TYPE, shortcut.getList("DcType"));
			addFieldMap(fieldMap, Field.DC_FORMAT, shortcut.getList("DcFormat"),
					map(Field.DCTERMS_EXTENT, shortcut.getList("DctermsExtent")),
					map(Field.DCTERMS_MEDIUM, shortcut.getList("DctermsMedium"))
			);
			addFieldMap(fieldMap, Field.DC_SUBJECT, shortcut.getList("DcSubject"));
			addFieldMap(fieldMap, Field.DC_IDENTIFIER, shortcut.getList("DcIdentifier"));
			addFieldMap(fieldMap, Field.DC_RELATION,
					shortcut.getList("DcRelation"),
					map(Field.DCTERMS_REFERENCES, shortcut.getList("DctermsReferences")),
					map(Field.DCTERMS_ISREFERENCEDBY, shortcut.getList("DctermsIsReferencedBy")),
					map(Field.DCTERMS_ISREPLACEDBY, shortcut.getList("DctermsIsReplacedBy")),
					map(Field.DCTERMS_ISREQUIREDBY, shortcut.getList("DctermsIsRequiredBy")),
					map(Field.DCTERMS_REPLACES, shortcut.getList("DctermsReplaces")),
					map(Field.DCTERMS_REQUIRES, shortcut.getList("DctermsRequires")),
					map(Field.DCTERMS_ISVERSIONOF, shortcut.getList("DctermsIsVersionOf")),
					map(Field.DCTERMS_HASVERSION, shortcut.getList("DctermsHasVersion")),
					map(Field.DCTERMS_CONFORMSTO, shortcut.getList("DctermsConformsTo")),
					map(Field.DCTERMS_HASFORMAT, shortcut.getList("DctermsHasFormat")),
					map(Field.DCTERMS_ISFORMATOF, shortcut.getList("DctermsIsFormatOf")),
					map(Field.EDM_CURRENTLOCATION, shortcut.getList("edm:currentLocation")),
					map(Field.EDM_HASMET, shortcut.getList("EdmHasMet")),
					map(Field.EDM_HASTYPE, shortcut.getList("EdmHasType")),
					map(Field.EDM_INCORPORATES, shortcut.getList("EdmIncorporates")),
					map(Field.EDM_ISDERIVATIVEOF, shortcut.getList("EdmIsDerivativeOf")),
					map(Field.EDM_ISRELATEDTO, shortcut.getList("EdmIsRelatedTo")),
					map(Field.EDM_ISREPRESENTATIONOF, shortcut.getList("EdmIsRepresentationOf")),
					map(Field.EDM_ISSIMILARTO, shortcut.getList("EdmIsSimilarTo")),
					map(Field.EDM_ISSUCCESSOROF, shortcut.getList("EdmIsSuccessorOf")),
					map(Field.EDM_REALIZES, shortcut.getList("EdmRealizes"))
			);
			addFieldMap(fieldMap, Field.PROXY_DCTERMS_ISPARTOF, shortcut.getList("DctermsIsPartOf", "Proxy")); // place vs proxy
			addFieldMap(fieldMap, Field.DCTERMS_HASPART, shortcut.getList("DctermsHasPart"));
			addFieldMap(fieldMap, Field.EDM_ISNEXTINSEQUENCE, shortcut.getList("EdmIsNextInSequence"));
			addFieldMap(fieldMap, Field.DC_LANGUAGE, shortcut.getList("DcLanguage"));
			addFieldMap(fieldMap, Field.DC_RIGHTS, shortcut.getList("DcRights"));
			addFieldMap(fieldMap, Field.DCTERMS_PROVENANCE, shortcut.getList("DctermsProvenance"));
			addFieldMap(fieldMap, Field.DC_PUBLISHER, shortcut.getList("DcPublisher"));
			addFieldMap(fieldMap, Field.DC_SOURCE, shortcut.getList("DcSource"));
			addFieldMap(fieldMap, Field.EDM_DATAPROVIDER, shortcut.getList("DataProvider"));
			addFieldMap(fieldMap, Field.EDM_PROVIDER, shortcut.getList("EdmProvider"));
			addFieldMap(fieldMap, Field.EDM_COUNTRY, Arrays.asList(Field.EDM_COUNTRY.getValues(shortcut.get("EdmCountry"))));
			// addFieldMap(fieldMap, Field.EDM_LANDINGPAGE, shortcut.getList("EdmLandingPage"));

			fields = new LinkedList<FieldPresentation>();
			for (FieldPresentation fieldPresentation : fieldMap.values()) {
				if (fieldPresentation.getFieldValues() != null && fieldPresentation.getFieldValues().size() > 0) {
					fields.add(fieldPresentation);
				}
			}
		}
		return fields;
	}

	public Map<Field, List<String>> getHiddenFields() throws Exception {
		if (hiddenFields == null) {
			hiddenFields = new LinkedHashMap<Field, List<String>>();

			hiddenFields.put(Field.EDM_CURRENTLOCATION, shortcut.getList("EdmCurrentLocation"));
			hiddenFields.put(Field.EDM_OBJECT, shortcut.getList("EdmObject"));
			hiddenFields.put(Field.EDM_ISSHOWNAT, shortcut.getList("EdmIsShownAt"));
			hiddenFields.put(Field.EDM_ISSHOWNBY, shortcut.getList("EdmIsShownBy"));
			hiddenFields.put(Field.EDM_HASVIEW, shortcut.getList("EdmHasView"));

		}
		return hiddenFields;
	}

	public static Map<Field, List<String>> map(Field field, List<String> values) {
		Map<Field, List<String>> map = new HashMap<Field, List<String>>();
		map.put(field, values);
		return map;
	}

	private void addField(List<FieldPresentation> fields, Field fieldInfo, String fieldValue) {
		addField(fields, fieldInfo, new String[]{fieldValue});
	}

	private void addField(List<FieldPresentation> fields, Field fieldInfo,
			String[]... fieldValuesArrays) {
		if ((fieldValuesArrays == null) || (fieldInfo.getFieldLabel() == null)) {
			log.info(String.format("fieldInfo: %s, %s", fieldInfo.getFieldName(), fieldInfo.getFieldLabel()));
			return;
		}

		List<String> fieldValues = new LinkedList<String>();
		for (String[] fieldValueArray : fieldValuesArrays) {
			if (fieldValueArray == null) {
				continue;
			}
			for (String value : fieldValueArray) {
				if (StringUtils.isNotBlank(value) && !value.equals("0000")) {
					if (fieldInfo.getMaxLength() == -1) {
						fieldValues.add(value);
					} else {
						String shortValue = StringUtils.abbreviate(value, fieldInfo.getMaxLength() + 3);
						fieldValues.add(shortValue);
					}
				}
			}
		}
		if (fieldInfo.doSortValues()) {
			Collections.sort(fieldValues);
		}

		if (!fieldValues.isEmpty()) {
			try {
				fields.add(new FieldPresentation(this, fieldInfo, fieldValues));
			} catch (NullPointerException npe) {
				if (log.isDebugEnabled()) {
					log.debug("Failed to add field in fullDocPresentationImpl for: " + fieldInfo.getFieldLabel());
				}
				log.error(ExceptionUtils.getFullStackTrace(npe));
			}
		}
	}

	private FieldPresentation getFieldPresentation(Map<Field, FieldPresentation> fields, Field fieldInfo) {
		FieldPresentation fieldPresentation = null;
		if (fields.containsKey(fieldInfo)) {
			fieldPresentation = fields.get(fieldInfo);
		} else {
			fieldPresentation = new FieldPresentation(this, fieldInfo);
			fields.put(fieldInfo, fieldPresentation);
		}
		return fieldPresentation;
	}

	@SuppressWarnings("unchecked")
	private void extractAllFieldValues(FieldPresentation fieldPresentation, Field fieldInfo, Object... fieldValuesArrays) {
		for (Object fieldValueArray : fieldValuesArrays) {
			if (fieldValueArray == null) {
				continue;
			}

			if (fieldValueArray instanceof String) {
				extractFieldValues(fieldPresentation, fieldInfo, Arrays.asList((String)fieldValueArray));
			} else if (fieldValueArray instanceof String[]) {
				extractFieldValues(fieldPresentation, fieldInfo, Arrays.asList((String[])fieldValueArray));
			} else if (fieldValueArray instanceof List<?>) {
				extractFieldValues(fieldPresentation, fieldInfo, (List<String>)fieldValueArray);
			} else if (fieldValueArray instanceof Map) {
				fieldPresentation.addMap(this, (Map<Field, List<String>>)fieldValueArray);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Unhandled data type in addFieldMap(): " + fieldValueArray.getClass());
				}
			}
		}
	}

	private void extractFieldValues(FieldPresentation fieldPresentation, Field fieldInfo, List<String> values) {
		for (String value : values) {
			if (StringUtils.isNotBlank(value) && !value.equals("0000")) {
				// modifying the landing page value
				if (fieldInfo.equals(Field.EDM_LANDINGPAGE)) {
					value = value.replace(EuropeanaUrlService.URL_EUROPEANA+"/portal", getPortalUrl()) + EuropeanaUrlService.EXT_HTML;
				}

				if (fieldInfo.getMaxLength() == -1) {
					fieldPresentation.add(this, value);
				} else {
					String shortValue = StringUtils.abbreviate(value, fieldInfo.getMaxLength() + 3);
					fieldPresentation.add(this, shortValue);
				}
			}
		}
	}

	private void addFieldMap(Map<Field, FieldPresentation> fields, Field fieldInfo,
			Object... fieldValuesArrays) {
		if (fieldValuesArrays == null) {
			return;
		}
		if (fieldInfo.getFieldLabel() == null) {
			if (log.isDebugEnabled()) {
				log.debug(String.format("No field label for fieldInfo: %s, %s", fieldInfo.getFieldName(), fieldInfo.getFieldLabel()));
			}
			return;
		}
		FieldPresentation fieldPresentation = getFieldPresentation(fields, fieldInfo);
		if (fieldValuesArrays.length > 1) {
			fieldPresentation.setCombined(true);
		}
		extractAllFieldValues(fieldPresentation, fieldInfo, fieldValuesArrays);
	}
}
