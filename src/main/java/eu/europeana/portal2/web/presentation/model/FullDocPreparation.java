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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.presentation.model.data.submodel.FieldPresentation;

public abstract class FullDocPreparation {//extends FullDocData {

	/*
	private static final Logger log = Logger.getLogger(FullDocData.class
			.getName());

	// caching fields
	private List<FieldPresentation> fields;
	private List<FieldPresentation> fieldsLightbox;
	private List<FieldPresentation> fieldsAdditional;
	private Map<String, List<FieldPresentation>> fieldsEnrichment;

	/**
	 * Returns the fieldsLightbox for the show more section where values exist
	 * for that field
	 * 
	 * @return Fields for lightbox section
	 */
	/*
	public List<FieldPresentation> getFieldsLightbox() throws Exception {
		if (fieldsLightbox == null) {
			fieldsLightbox = new ArrayList<FieldPresentation>();

			addField(fieldsLightbox, Field.DC_CREATOR, document.getDcCreator());
			addField(fieldsLightbox, Field.DC_RIGHTS, document.getEdmRights());
			addField(fieldsLightbox, Field.EUROPEANA_DATAPROVIDER, document.getDataProvider());
			addField(fieldsLightbox, Field.EUROPEANA_PROVIDER,
					document.getProvider(),
					Field.EUROPEANA_COUNTRY.getValues(document.getCountry()));
		}
		return fieldsLightbox;
	}

	/**
	 * Returns the fields for the show more section where values exist for that
	 * field
	 * 
	 * @return Fields for more section
	 */
	/*
	public List<FieldPresentation> getFieldsAdditional() throws Exception {
		if (fieldsAdditional == null) {
			fieldsAdditional = new ArrayList<FieldPresentation>();

			addField(fieldsAdditional, Field.DC_RIGHTS, document.getRights());
			addField(fieldsAdditional, Field.DC_IDENTIFIER,
					document.getDcIdentifier());
			addField(fieldsAdditional, Field.DC_FORMAT, document.getDcFormat(),
					document.getDcTermsExtent(), document.getDcTermsMedium());
			addField(fieldsAdditional, Field.DC_LANGUAGE,
					document.getDcLanguage());
			addField(fieldsAdditional, Field.DC_SOURCE, document.getDcSource());
			addField(fieldsAdditional, Field.DCTERMS_PROVENANCE,
					document.getDcTermsProvenance());
			addField(fieldsAdditional, Field.DC_PUBLISHER,
					document.getDcPublisher());
			addField(fieldsAdditional, Field.DCTERMS_ISSUED,
					document.getDcTermsIssued());
			addField(fieldsAdditional, Field.EUROPEANA_COLLECTIONNAME,
					new String[] { document.getEuropeanaCollectionName() });
		}
		return fieldsAdditional;
	}

	public Map<String, List<FieldPresentation>> getFieldsEnrichment()
			throws Exception {
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
	/*
	private void prepareFieldsEnrichment() throws Exception {
		fieldsEnrichment = new HashMap<String, List<FieldPresentation>>();

		// WHERE
		List<FieldPresentation> where = new ArrayList<FieldPresentation>();
		addField(where, Field.ENRICHMENT_PLACE_LABEL,
				document.getEnrichmentPlaceLabel());
		addField(where, Field.ENRICHMENT_PLACE_TERM,
				document.getEnrichmentPlaceTerm());
		if (getDocument().isPositionAvailable()) {
			addField(where, Field.ENRICHMENT_PLACE_LAT_LONG,
					new String[] { String.valueOf(document
							.getEnrichmentPlaceLatitude()) },
					new String[] { String.valueOf(document
							.getEnrichmentPlaceLongitude()) },
					new String[] { getDocument().getUrlKml() });
		}
		addField(where, Field.ENRICHMENT_PLACE_BROADER_LABEL,
				document.getEnrichmentPlaceBroaderLabel());
		addField(where, Field.ENRICHMENT_PLACE_BROADER_TERM,
				document.getEnrichmentPlaceBroaderTerm());
		if (where.size() > 0) {
			fieldsEnrichment.put("enrichment_category_where_t", where);
		}

		// WHO
		List<FieldPresentation> who = new ArrayList<FieldPresentation>();
		addField(who, Field.ENRICHMENT_AGENT_LABEL,
				document.getEnrichmentAgentLabel());
		addField(who, Field.ENRICHMENT_AGENT_TERM,
				document.getEnrichmentAgentTerm());
		if (who.size() > 0) {
			fieldsEnrichment.put("enrichment_category_who_t", who);
		}

		// WHAT
		List<FieldPresentation> what = new ArrayList<FieldPresentation>();
		addField(what, Field.ENRICHMENT_CONCEPT_LABEL,
				document.getEnrichmentConceptLabel());
		addField(what, Field.ENRICHMENT_CONCEPT_TERM,
				document.getEnrichmentConceptTerm());
		addField(what, Field.ENRICHMENT_CONCEPT_BROADER_LABEL,
				document.getEnrichmentConceptBroaderLabel());
		addField(what, Field.ENRICHMENT_CONCEPT_BROADER_TERM,
				document.getEnrichmentConceptBroaderTerm());
		if (what.size() > 0) {
			fieldsEnrichment.put("enrichment_category_what_t", what);
		}

		// WHEN
		List<FieldPresentation> when = new ArrayList<FieldPresentation>();
		addField(when, Field.ENRICHMENT_PERIOD_LABEL,
				document.getEnrichmentPeriodLabel());
		addField(when, Field.ENRICHMENT_PERIOD_TERM,
				document.getEnrichmentPeriodTerm());
		if (document.getEnrichmentPeriodBegin() != null) {
			addField(when, Field.ENRICHMENT_PERIOD_BEGIN,
					new String[] { document.getEnrichmentPeriodBegin()
							.toString() });
		}
		if (document.getEnrichmentPeriodEnd() != null) {
			addField(when, Field.ENRICHMENT_PERIOD_END, new String[] { document
					.getEnrichmentPeriodEnd().toString() });
		}
		addField(when, Field.ENRICHMENT_PERIOD_BROADER_LABEL,
				document.getEnrichmentPeriodBroaderLabel());
		addField(when, Field.ENRICHMENT_PERIOD_BROADER_TERM,
				document.getEnrichmentPeriodBroaderTerm());
		if (when.size() > 0) {
			fieldsEnrichment.put("enrichment_category_when_t", when);
		}

	}

	/**
	 * Returns a collection of field
	 * 
	 * @return collection of fields
	 */
	/*
	public List<FieldPresentation> getFields() throws Exception {
		if (fields == null) {

			fields = new ArrayList<FieldPresentation>();

			addField(fields, Field.DCTERMS_ALTERNATIVE, document.getDcTermsAlternative());
			addField(fields, Field.DC_CREATOR, document.getDcCreator());
			addField(fields, Field.DC_CONTRIBUTOR, document.getDcContributor());
			addField(fields, Field.DC_COVERAGE, document.getDcCoverage());
			addField(fields, Field.DC_DATE, document.getDcDate(), document.getDcTermsCreated());
			addField(fields, Field.DCTERMS_TEMPORAL, document.getDcTermsTemporal());
			addField(fields, Field.DCTERMS_SPATIAL, document.getDcTermsSpatial());
			addField(fields, Field.DC_TYPE, document.getDcType());
			addField(fields, Field.DC_SUBJECT, document.getDcSubject());
			addField(fields, Field.DC_RELATION, document.getDcRelation(),
					document.getDcTermsReferences(),
					document.getDcTermsIsReferencedBy(),
					document.getDcTermsIsReplacedBy(),
					document.getDcTermsIsRequiredBy(),
					document.getDcTermsIsPartOf(),
					document.getDcTermsHasPart(),
					document.getDcTermsReplaces(),
					document.getDcTermsRequires(),
					document.getDcTermsIsVersionOf(),
					document.getDcTermsHasVersion(),
					document.getDcTermsConformsTo(),
					document.getDcTermsHasFormat(),
					document.getDcTermsIsFormatOf());
			addField(fields, Field.DC_DESCRIPTION, document.getDcDescription(),
					document.getDcTermsTableOfContents());
			addField(fields, Field.EUROPEANA_DATAPROVIDER,
					document.getEuropeanaDataProvider());
			addField(fields, Field.EUROPEANA_PROVIDER,
					document.getEuropeanaProvider(),
					Field.EUROPEANA_COUNTRY.getValues(document
							.getEuropeanaCountry()));
		}
		return fields;
	}

	private void addField(List<FieldPresentation> fields, Field fieldInfo,
			String[]... fieldValuesArrays) {
		if ((fieldValuesArrays == null) || (fieldInfo.getFieldLabel() == null)) {
			return;
		}

		List<String> fieldValues = new LinkedList<String>();
		for (String[] fieldValueArray : fieldValuesArrays) {
			for (String value : fieldValueArray) {
				if (StringUtils.isNotBlank(value) && !value.equals("0000")) {
					if (fieldInfo.getMaxLength() == -1) {
						fieldValues.add(value);
					} else {
						String shortValue = StringUtils.abbreviate(value,
								fieldInfo.getMaxLength() + 3);
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
				log.info("Failed to add field in fullDocPresentationImpl");
				log.info("for Field:  " + fieldInfo.getFieldLabel());
			}
		}
	}
	*/
}
