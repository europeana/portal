/*
 * Copyright 2007-2012 The Europeana Foundation
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

package eu.europeana.portal2.web.presentation.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.presentation.model.data.submodel.FieldPresentation;

public abstract class FullDocPreparation extends FullDocData {

	protected final Logger log = Logger.getLogger(getClass().getName());

	// caching fields
	private List<FieldPresentation> fields;
	private Map<Field, FieldPresentation> fieldsMap;
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
			addField(fieldsLightbox, Field.EDM_PROVIDER, document.getProvider(),
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

		// WHERE
		List<FieldPresentation> where = new ArrayList<FieldPresentation>();
		/*
		addField(where, Field.ENRICHMENT_PLACE_LABEL, document.getEnrichmentPlaceLabel());
		addField(where, Field.ENRICHMENT_PLACE_TERM, document.getEnrichmentPlaceTerm());
		if (getDocument().isPositionAvailable()) {
			addField(where, Field.ENRICHMENT_PLACE_LAT_LONG,
				new String[]{String.valueOf(document.getEnrichmentPlaceLatitude())},
				new String[]{String.valueOf(document.getEnrichmentPlaceLongitude())},
				new String[]{getDocument().getUrlKml()});
		}
		addField(where, Field.ENRICHMENT_PLACE_BROADER_LABEL, document.getEnrichmentPlaceBroaderLabel());
		addField(where, Field.ENRICHMENT_PLACE_BROADER_TERM, document.getEnrichmentPlaceBroaderTerm());
		if (where.size() > 0) {
			fieldsEnrichment.put("enrichment_category_where_t", where);
		}
		*/

		if (document.getPlaces() != null) {
			for (Place place : document.getPlaces()) {
				addField(where, Field.ENRICHMENT_PLACE_TERM, new String[]{place.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (place.getPrefLabel() != null) {
					for (String key : place.getPrefLabel().keySet()) {
						labels.add(place.getPrefLabel().get(key) + " (" + key + ")");
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
		if (where.size() > 0) {
			fieldsEnrichment.put("enrichment_category_where_t", where);
		}

		// WHO
		List<FieldPresentation> who = new ArrayList<FieldPresentation>();
		// addField(who, Field.ENRICHMENT_AGENT_LABEL, document.getEnrichmentAgentLabel());
		// addField(who, Field.ENRICHMENT_AGENT_TERM, document.getEnrichmentAgentTerm());
		if (document.getAgents() != null) {
			for (Agent agent : document.getAgents()) {
				addField(who, Field.ENRICHMENT_AGENT_TERM, new String[]{agent.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (agent.getPrefLabel() != null) {
					for (String key : agent.getPrefLabel().keySet()) {
						labels.add(agent.getPrefLabel().get(key) + " (" + key + ")");
					}
				}
				addField(who, Field.ENRICHMENT_AGENT_LABEL, StringArrayUtils.toArray(labels));
			}
		}
		if (who.size() > 0) {
			fieldsEnrichment.put("enrichment_category_who_t", who);
		}

		// WHAT
		List<FieldPresentation> what = new ArrayList<FieldPresentation>();
		// addField(what, Field.ENRICHMENT_CONCEPT_LABEL, document.getEnrichmentConceptLabel());
		// addField(what, Field.ENRICHMENT_CONCEPT_TERM, document.getEnrichmentConceptTerm());
		// addField(what, Field.ENRICHMENT_CONCEPT_BROADER_LABEL, document.getEnrichmentConceptBroaderLabel());
		// addField(what, Field.ENRICHMENT_CONCEPT_BROADER_TERM, document.getEnrichmentConceptBroaderTerm());
		if (document.getConcepts() != null) {
			for (Concept concept : document.getConcepts()) {
				addField(what, Field.ENRICHMENT_CONCEPT_TERM, new String[]{concept.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (concept.getPrefLabel() != null) {
					for (String key : concept.getPrefLabel().keySet()) {
						labels.add(concept.getPrefLabel().get(key) + " (" + key + ")");
					}
				}
				addField(what, Field.ENRICHMENT_CONCEPT_LABEL, StringArrayUtils.toArray(labels));
				addField(what, Field.ENRICHMENT_CONCEPT_BROADER_LABEL, concept.getBroader());
			}
		}
		if (what.size() > 0) {
			fieldsEnrichment.put("enrichment_category_what_t", what);
		}

		// WHEN
		List<FieldPresentation> when = new ArrayList<FieldPresentation>();
		/*
		addField(when, Field.ENRICHMENT_PERIOD_LABEL, document.getEnrichmentPeriodLabel());
		addField(when, Field.ENRICHMENT_PERIOD_TERM, document.getEnrichmentPeriodTerm());
		if (document.getEnrichmentPeriodBegin() != null) {
			addField(when, Field.ENRICHMENT_PERIOD_BEGIN, new String[]{document.getEnrichmentPeriodBegin().toString()});
		}
		if (document.getEnrichmentPeriodEnd() != null) {
			addField(when, Field.ENRICHMENT_PERIOD_END, new String[]{document.getEnrichmentPeriodEnd().toString()});
		}
		addField(when, Field.ENRICHMENT_PERIOD_BROADER_LABEL, document.getEnrichmentPeriodBroaderLabel());
		addField(when, Field.ENRICHMENT_PERIOD_BROADER_TERM, document.getEnrichmentPeriodBroaderTerm());
		*/
		if (document.getTimespans() != null) {
			for (Timespan timespan : document.getTimespans()) {
				addField(when, Field.ENRICHMENT_PERIOD_TERM, new String[]{timespan.getAbout()});
				ArrayList<String> labels = new ArrayList<String>();
				if (timespan.getPrefLabel() != null) {
					for (String key : timespan.getPrefLabel().keySet()) {
						labels.add(timespan.getPrefLabel().get(key) + " (" + key + ")");
					}
				}
				addField(when, Field.ENRICHMENT_PERIOD_LABEL, StringArrayUtils.toArray(labels));
				if (timespan.getBegin() != null) {
					// TODO: handle language (item.getKey())
					for (Entry<String, List<String>> item : timespan.getBegin().entrySet()) {
						for (String value : item.getValue()) {
							addField(when, Field.ENRICHMENT_PERIOD_BEGIN, value);
						}
					}
				}
				if (timespan.getEnd() != null) {
					// TODO: handle language (item.getKey())
					for (Entry<String, List<String>> item : timespan.getEnd().entrySet()) {
						for (String value : item.getValue()) {
							addField(when, Field.ENRICHMENT_PERIOD_END, value);
						}
					}
				}
			}
		}
		if (when.size() > 0) {
			fieldsEnrichment.put("enrichment_category_when_t", when);
		}
	}

	/**
	 * Returns a collection of field
	 * 
	 * @return collection of fields
	 */
	public List<FieldPresentation> getFields() throws Exception {

		if (fields == null) {

			Map<Field, FieldPresentation> fieldMap = new LinkedHashMap<Field, FieldPresentation>();

			addFieldMap(fieldMap, Field.DCTERMS_ALTERNATIVE, shortcut.get("DctermsAlternative"));
			addFieldMap(fieldMap, Field.DC_DESCRIPTION, 
					getDocument().getDcDescription(),
					map(Field.DCTERMS_TABLEOFCONTENTS, shortcut.get("DctermsTableOfContents"))
			);
			addFieldMap(fieldMap, Field.DC_CREATOR, shortcut.get("DcCreator"));
			addFieldMap(fieldMap, Field.DC_CONTRIBUTOR, shortcut.get("DcContributor"));
			addFieldMap(fieldMap, Field.DC_COVERAGE, shortcut.get("DcCoverage"));
			addFieldMap(fieldMap, Field.DCTERMS_SPATIAL, shortcut.get("DctermsSpatial"));
			addFieldMap(fieldMap, Field.DC_DATE, getDocument().getDcDate());
			addFieldMap(fieldMap, Field.DCTERMS_TEMPORAL, shortcut.get("DctermsTemporal"));
			addFieldMap(fieldMap, Field.DCTERMS_ISSUED, shortcut.get("DctermsIssued"));
			addFieldMap(fieldMap, Field.DCTERMS_CREATED, shortcut.get("DctermsCreated"));
			addFieldMap(fieldMap, Field.DC_TYPE, getDocument().getDcType());
			addFieldMap(fieldMap, Field.DC_FORMAT, shortcut.get("DcFormat"), 
					shortcut.get("DctermsExtent"),
					shortcut.get("DctermsMedium"));
			addFieldMap(fieldMap, Field.DC_SUBJECT, getDocument().getDcSubject());
			addFieldMap(fieldMap, Field.DC_IDENTIFIER, shortcut.get("DcIdentifier"));
			addFieldMap(fieldMap, Field.DC_RELATION,
					shortcut.get("DcRelation"),
					shortcut.get("DctermsReferences"),
					shortcut.get("DctermsIsReferencedBy"),
					shortcut.get("DctermsIsReplacedBy"),
					shortcut.get("DctermsIsRequiredBy"),
					shortcut.get("DctermsReplaces"),
					shortcut.get("DctermsRequires"),
					shortcut.get("DctermsIsVersionOf"),
					getDocument().getDctermsHasVersion(),
					shortcut.get("DctermsConformsTo"),
					shortcut.get("DctermsHasFormat"),
					getDocument().getDctermsIsFormatOf(),
					shortcut.get("EdmHasMet"),
					map(Field.EDM_HASTYPE, shortcut.get("EdmHasType")),
					shortcut.get("EdmIncorporates"),
					shortcut.get("EdmIsDerivativeOf"),
					map(Field.EDM_ISRELATEDTO, shortcut.get("EdmIsRelatedTo")),
					map(Field.EDM_ISREPRESENTATIONOF, shortcut.get("EdmIsRepresentationOf")),
					shortcut.get("EdmIsSimilarTo"),
					shortcut.get("EdmIsSuccessorOf"),
					shortcut.get("EdmRealizes")
			);
			addFieldMap(fieldMap, Field.DCTERMS_ISPARTOF, shortcut.get("DctermsIsPartOf"));
			addFieldMap(fieldMap, Field.DCTERMS_HASPART, shortcut.get("DctermsHasPart"));
			addFieldMap(fieldMap, Field.EDM_ISNEXTINSEQUENCE, shortcut.get("EdmIsNextInSequence"));
			addFieldMap(fieldMap, Field.DC_LANGUAGE, shortcut.get("DcLanguage"));
			addFieldMap(fieldMap, Field.DC_RIGHTS, shortcut.get("DcRights"));
			addFieldMap(fieldMap, Field.DCTERMS_PROVENANCE, shortcut.get("DctermsProvenance"));
			addFieldMap(fieldMap, Field.DC_PUBLISHER, shortcut.get("DcPublisher"));
			addFieldMap(fieldMap, Field.DC_SOURCE, shortcut.get("DcSource"));
			addFieldMap(fieldMap, Field.EDM_DATAPROVIDER, shortcut.get("DataProvider"));
			addFieldMap(fieldMap, Field.EDM_PROVIDER, shortcut.get("EdmProvider"));
			addFieldMap(fieldMap, Field.EDM_COUNTRY, Field.EDM_COUNTRY.getValues(shortcut.get("EdmCountry")));

			fields = new LinkedList<FieldPresentation>();
			for (FieldPresentation fieldPresentation : fieldMap.values()) {
				if (fieldPresentation.getFieldValues() != null && fieldPresentation.getFieldValues().size() > 0) {
					fields.add(fieldPresentation);
				}
			}
		}
		return fields;
	}

	public static Map map(Field field, String[] values) {
		Map map = new HashMap<Field, String[]>();
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

		FieldPresentation fieldPresentation = new FieldPresentation(fieldInfo);
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
				log.warning("Failed to add field in fullDocPresentationImpl for: " + fieldInfo.getFieldLabel());
				log.severe(ExceptionUtils.getFullStackTrace(npe));
			}
		}
	}

	private FieldPresentation getFieldPresentation(Map<Field, FieldPresentation> fields, Field fieldInfo) {
		FieldPresentation fieldPresentation = null;
		if (fields.containsKey(fieldInfo)) {
			fieldPresentation = fields.get(fieldInfo);
		} else {
			fieldPresentation = new FieldPresentation(fieldInfo);
			fields.put(fieldInfo, fieldPresentation);
		}
		return fieldPresentation;
	}

	private void extractAllFieldValues(FieldPresentation fieldPresentation, Field fieldInfo, Object... fieldValuesArrays) {
		for (Object fieldValueArray : fieldValuesArrays) {
			if (fieldValueArray == null) {
				continue;
			}

			if (fieldValueArray instanceof String) {
				extractFieldValues(fieldPresentation, fieldInfo, new String[]{(String)fieldValueArray});
			} else if (fieldValueArray instanceof String[]) {
				extractFieldValues(fieldPresentation, fieldInfo, (String[])fieldValueArray);
			} else if (fieldValueArray instanceof Map) {
				fieldPresentation.addMap(this, (Map<Field, String[]>)fieldValueArray);
			} else {
				log.warning("Unhandled data type in addFieldMap(): " + fieldValueArray.getClass());
			}
		}
	}

	private void extractFieldValues(FieldPresentation fieldPresentation, Field fieldInfo, String[] values) {
		for (String value : values) {
			if (StringUtils.isNotBlank(value) && !value.equals("0000")) {
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
			log.warning(String.format("No field label for fieldInfo: %s, %s", fieldInfo.getFieldName(), fieldInfo.getFieldLabel()));
			return;
		}
		FieldPresentation fieldPresentation = getFieldPresentation(fields, fieldInfo);
		extractAllFieldValues(fieldPresentation, fieldInfo, fieldValuesArrays);
	}
}
