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

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.portal2.web.controllers.ObjectController;
import eu.europeana.portal2.web.presentation.enums.CiteStyle;
import eu.europeana.portal2.web.presentation.enums.ExternalService;
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.decorators.FullBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.submodel.CiteValue;
import eu.europeana.portal2.web.presentation.model.data.submodel.MetaDataFieldPresentation;
import eu.europeana.portal2.web.presentation.model.data.submodel.RightsValue;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;
import eu.europeana.portal2.web.util.KmlPresentation;
import eu.europeana.portal2.web.util.SearchUtils;
import eu.europeana.portal2.web.util.WebUtils;

public class FullDocPage extends FullDocPreparation {

	private static final Logger log = Logger.getLogger(FullDocPage.class.getName());

	private RightsValue rightsOption = null;

	@Override
	public UrlBuilder prepareFullDocUrl(UrlBuilder builder) {
		if (isEmbedded()) {
			builder.addParam("embedded", "true", true);
		}

		// remove default values to clean up url...
		builder.removeDefault("view", "table");
		builder.removeDefault("start", "1");
		builder.removeDefault("startPage", "1");
		builder.removeDefault("embedded", "false");

		return builder;
	}

	/**
	 * Link should only appear if there are results to show
	 * 
	 * @return - Whether or not to show the extended fields link
	 * @throws Exception
	 */
	public String getEnrichedFieldsLinkStyle() throws Exception {
		return this.getFieldsEnrichment().isEmpty() ? "display:none" : "display:block";
	}

	public ExternalService[] getExternalSearchServices() {
		return ExternalService.values();
	}

	/**
	 * Returns the url for the image that links to the source image
	 * 
	 * @return image reference
	 */
	public String getImageRef() {
		return StringArrayUtils.isNotBlank(document.getEdmIsShownBy()) 
			? document.getEdmIsShownBy()[0]
			: document.getEdmIsShownAt()[0];
	}

	private String lightboxRef = null;
	private boolean lightboxRefChecked = false;

	/**
	 * TODO: we need to allow for providers that haven't followed the guidelines
	 * and haven't given us an "isShownBy" value. For example, the pdf here is
	 * not downloadable: http://test.europeana.eu/portal/record/91611/
	 * D7D2BA903AA018AA86390F326F4F426256A9FD61.html
	 * 
	 * In this case (and others where this is no "isShownBy") we need to check
	 * the value of "isShownAt", and if it passes the mime type test show that
	 * instead.
	 * */
	public String getLightboxRef() {
		if (!lightboxRefChecked) {
			if (StringArrayUtils.isBlank(document.getEdmIsShownBy())
					&& StringArrayUtils.isBlank(document.getEdmIsShownAt())) {
				lightboxRef = null;
			}
			String shownBy = document.getEdmIsShownBy()[0] != null
					? StringUtils.substringBefore(document.getEdmIsShownBy()[0], "?")
					: null;
			String shownAt = document.getEdmIsShownAt()[0] != null
					? StringUtils.substringBefore(document.getEdmIsShownAt()[0], "?")
					: "";
			if (StringUtils.startsWith(shownBy, "mms")
					&& StringUtils.startsWith(shownAt, "mms")) {
				lightboxRef = null;
			}

			FileNameMap fileNameMap = URLConnection.getFileNameMap();

			if (WebUtils.checkMimeType(shownBy) != null) {
				lightboxRef = shownBy;
				String type = fileNameMap.getContentTypeFor(document.getEdmIsShownBy()[0]);
				if (WebUtils.checkMimeType(type) != null) {
					lightboxRef = document.getEdmIsShownBy()[0];
				}
			} else if (WebUtils.checkMimeType(shownAt) != null) {
				lightboxRef = shownAt;
				String type = fileNameMap.getContentTypeFor(document.getEdmIsShownAt()[0]);
				if (WebUtils.checkMimeType(type) != null) {
					lightboxRef = document.getEdmIsShownAt()[0];
				}
			}

			lightboxRefChecked = true;
		}
		return lightboxRef;
	}

	public boolean isEuropeanaIsShownBy() {
		return StringArrayUtils.isNotBlank(document.getEdmIsShownBy());
	}

	/**
	 * Returns a collection of Meta Data fields
	 * 
	 * @return collection of meta data fields
	 * @throws EuropeanaQueryException
	 */
	public List<MetaDataFieldPresentation> getMetaDataFields() {
		List<MetaDataFieldPresentation> fields = new ArrayList<MetaDataFieldPresentation>();

		addMetaField(fields, Field.EUROPEANA_URI, document.getId());
		addMetaField(fields, Field.EUROPEANA_COUNTRY, getDocument().getEdmCountry());
		addMetaField(fields, Field.EUROPEANA_PROVIDER, document.getEdmProvider());
		addMetaField(fields, Field.EUROPEANA_COLLECTIONNAME, document.getEuropeanaCollectionName());
		addMetaField(fields, Field.EUROPEANA_ISSHOWNAT, document.getEdmIsShownAt());
		addMetaField(fields, Field.EUROPEANA_ISSHOWNBY, document.getEdmIsShownBy());
		// addMetaField(fields, Field.EUROPEANA_OBJECT, document.getThumbnails());
		addMetaField(fields, Field.EUROPEANA_OBJECT, document.getEdmObject());
		addMetaField(fields, Field.EUROPEANA_LANGUAGE, getDocument().getEdmLanguage());
		// addMetaField(fields, Field.EUROPEANA_TYPE, document.getType().toString());
		// addMetaField(fields, Field.EUROPEANA_USERTAG, document.getEdmUserTag());
		// addMetaField(fields, Field.EUROPEANA_YEAR, getDocument().getEdmYear());
		addMetaField(fields, Field.EUROPEANA_RIGHTS, document.getEdmRights());
		addMetaField(fields, Field.EUROPEANA_DATAPROVIDER, getDocument().getEdmDataProvider());
		addMetaField(fields, Field.EUROPEANA_UGC, document.getEdmUGC());

		addMetaField(fields, Field.DCTERMS_ALTERNATIVE, document.getDctermsAlternative());
		addMetaField(fields, Field.DCTERMS_CONFORMSTO, document.getDctermsConformsTo());
		addMetaField(fields, Field.DCTERMS_CREATED, document.getDctermsCreated());
		addMetaField(fields, Field.DCTERMS_EXTENT, document.getDctermsExtent());
		addMetaField(fields, Field.DCTERMS_HASFORMAT, document.getDctermsHasFormat());
		addMetaField(fields, Field.DCTERMS_HASPART, document.getDctermsHasPart());
		addMetaField(fields, Field.DCTERMS_HASVERSION, getDocument().getDctermsHasVersion());
		addMetaField(fields, Field.DCTERMS_ISFORMATOF, getDocument().getDctermsIsFormatOf());
		addMetaField(fields, Field.DCTERMS_ISPARTOF, document.getDctermsIsPartOf());
		addMetaField(fields, Field.DCTERMS_ISREFERENCEDBY, document.getDctermsIsReferencedBy());
		addMetaField(fields, Field.DCTERMS_ISREPLACEDBY, document.getDctermsIsReplacedBy());
		addMetaField(fields, Field.DCTERMS_ISREQUIREDBY, document.getDctermsIsRequiredBy());
		addMetaField(fields, Field.DCTERMS_ISSUED, document.getDctermsIssued());
		addMetaField(fields, Field.DCTERMS_ISVERSIONOF, document.getDctermsIsVersionOf());
		addMetaField(fields, Field.DCTERMS_MEDIUM, document.getDctermsMedium());
		addMetaField(fields, Field.DCTERMS_PROVENANCE, document.getDctermsProvenance());
		addMetaField(fields, Field.DCTERMS_REFERENCES, document.getDctermsReferences());
		addMetaField(fields, Field.DCTERMS_REPLACES, document.getDctermsReplaces());
		addMetaField(fields, Field.DCTERMS_REQUIRES, document.getDctermsRequires());
		addMetaField(fields, Field.DCTERMS_SPATIAL, document.getDctermsSpatial());
		addMetaField(fields, Field.DCTERMS_TABLEOFCONTENTS, document.getDctermsTableOfContents());
		addMetaField(fields, Field.DCTERMS_TEMPORAL, document.getDctermsTemporal());

		addMetaField(fields, Field.DC_CONTRIBUTOR, document.getDcContributor());
		addMetaField(fields, Field.DC_COVERAGE, document.getDcCoverage());
		addMetaField(fields, Field.DC_CREATOR, document.getDcCreator());
		addMetaField(fields, Field.DC_DATE, getDocument().getDcDate());
		addMetaField(fields, Field.DC_DESCRIPTION, getDocument().getDcDescription());
		addMetaField(fields, Field.DC_FORMAT, getDocument().getDcFormat());
		addMetaField(fields, Field.DC_IDENTIFIER, document.getDcIdentifier());
		addMetaField(fields, Field.DC_LANGUAGE, getDocument().getDcLanguage());
		addMetaField(fields, Field.DC_PUBLISHER, document.getDcPublisher());
		addMetaField(fields, Field.DC_RELATION, document.getDcRelation());
		addMetaField(fields, Field.DC_RIGHTS, getDocument().getDcRights());
		addMetaField(fields, Field.DC_SOURCE, document.getDcSource());
		addMetaField(fields, Field.DC_SUBJECT, getDocument().getDcSubject());
		addMetaField(fields, Field.DC_TITLE, getDocument().getDcTitle());
		addMetaField(fields, Field.DC_TYPE, getDocument().getDcType());

		addMetaField(fields, Field.EUROPEANA_COMPLETENESS, Integer.toString(document.getEuropeanaCompleteness()));
		
		/*
		addMetaField(fields, Field.ENRICHMENT_PLACE_TERM, getDocument().getEnrichmentPlaceTerm());
		addMetaField(fields, Field.ENRICHMENT_PLACE_LABEL, document.getEnrichmentPlaceLabel());
		if ((document.getEnrichmentPlaceLatitude() != 0)
				|| (document.getEnrichmentPlaceLongitude() != 0)) {
			addMetaField(fields, Field.ENRICHMENT_PLACE_LATITUDE, Float.toString(document.getEnrichmentPlaceLatitude()));
			addMetaField(fields, Field.ENRICHMENT_PLACE_LONGITUDE, Float.toString(document.getEnrichmentPlaceLongitude()));
		}
		addMetaField(fields, Field.ENRICHMENT_PLACE_BROADER_TERM, document.getEnrichmentPlaceBroaderTerm());
		addMetaField(fields, Field.ENRICHMENT_PLACE_BROADER_LABEL, document.getEnrichmentPlaceBroaderLabel());
		*/
		if (document.getPlaces() != null) {
			for (Place place : document.getPlaces()) {
				addMetaField(fields, Field.ENRICHMENT_PLACE_TERM, place.getAbout());
				if (place.getPrefLabel() != null) {
					for (String key : place.getPrefLabel().keySet()) {
						addMetaField(fields, Field.ENRICHMENT_PLACE_LABEL, place.getPrefLabel().get(key) + " (" + key + ")");
					}
				}
				if ((place.getLatitude() != 0) || (place.getLongitude() != 0)) {
					addMetaField(fields, Field.ENRICHMENT_PLACE_LATITUDE, Float.toString(place.getLatitude()));
					addMetaField(fields, Field.ENRICHMENT_PLACE_LONGITUDE, Float.toString(place.getLongitude()));
				}
				// addMetaField(fields, Field.ENRICHMENT_PLACE_BROADER_TERM, document.getEnrichmentPlaceBroaderTerm());
				// addMetaField(fields, Field.ENRICHMENT_PLACE_BROADER_LABEL, document.getEnrichmentPlaceBroaderLabel());
			}
		}

		if (document.getTimespans() != null) {
			for (Timespan timespan : document.getTimespans()) {
				addMetaField(fields, Field.ENRICHMENT_PERIOD_TERM, timespan.getAbout());
				for (String key : timespan.getPrefLabel().keySet()) {
					addMetaField(fields, Field.ENRICHMENT_PERIOD_LABEL, timespan.getPrefLabel().get(key) + " (" + key + ")");
				}
				if (timespan.getBegin() != null) {
					addMetaField(fields, Field.ENRICHMENT_PERIOD_BEGIN, timespan.getBegin());
				}
				if (timespan.getEnd() != null) {
					addMetaField(fields, Field.ENRICHMENT_PERIOD_END, timespan.getEnd());
				}
				// addMetaField(fields, Field.ENRICHMENT_PERIOD_BROADER_TERM, document.getEnrichmentPeriodBroaderTerm());
				// addMetaField(fields, Field.ENRICHMENT_PERIOD_BROADER_LABEL, document.getEnrichmentPeriodBroaderLabel());
			}
		}

		if (document.getConcepts() != null) {
			for (Concept concept : document.getConcepts()) {
				addMetaField(fields, Field.ENRICHMENT_CONCEPT_TERM, concept.getAbout());
				for (String key : concept.getPrefLabel().keySet()) {
					addMetaField(fields, Field.ENRICHMENT_CONCEPT_LABEL, concept.getPrefLabel().get(key) + " (" + key + ")");
				}
				addMetaField(fields, Field.ENRICHMENT_CONCEPT_BROADER_TERM, concept.getBroader());
				// addMetaField(fields, Field.ENRICHMENT_CONCEPT_BROADER_LABEL, document.getEnrichmentConceptBroaderLabel());
			}
		}

		if (document.getAgents() != null) {
			for (Agent agent : document.getAgents()) {
				addMetaField(fields, Field.ENRICHMENT_AGENT_TERM, agent.getAbout());
				for (String key : agent.getPrefLabel().keySet()) {
					addMetaField(fields, Field.ENRICHMENT_AGENT_LABEL, agent.getPrefLabel().get(key) + " (" + key + ")");
				}
			}
		}

		return fields;
	}

	/**
	 * Link should only appear if there are results to show
	 * 
	 * @return - Whether or not to show the more fields link
	 * @throws Exception
	 */
	public String getMoreLinkStyle() throws Exception {

		if (getFieldsAdditional().isEmpty() && getFieldsEnrichment().isEmpty()) {
			return "display:none";
		} else {
			return "display:block";
		}
	}

	/**
	 * Obtains and sets the correct rights options to the class level variable
	 * 
	 * @return
	 */
	public RightsValue getRightsOption() {
		if (rightsOption == null) {
			// ANDY: avoid null pointer here but edm rights aren't being mapped properly. 
			if(document.getEdmRights() != null){
				rightsOption = RightsValue.safeValueByUrl(document.getEdmRights()[0]);				
			}
			
		}
		return rightsOption;
	}

	public String getThumbnailUrl() throws UnsupportedEncodingException {
		String thumbnail = URLEncoder.encode(document.getEdmObject()[0], "utf-8");
		// TODO: check isUseCache()
		// if (isUseCache()) {
		boolean useCache = true;
		if (useCache) {
			UrlBuilder url = new UrlBuilder(getCacheUrl());
			url.addParam("uri", thumbnail, true);
			url.addParam("size", "FULL_DOC", true);
			url.addParam("type", getDocument().getEdmType(), true);
			return prepareFullDocUrl(url).toString();
		}
		return thumbnail;
	}

	public boolean isAllowIndexing() {
		return (document.getEuropeanaCompleteness() >= ObjectController.MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES);
	}

	public String getKmlDescription() throws UnsupportedEncodingException, EuropeanaQueryException {
		FullBeanDecorator doc = (FullBeanDecorator) getFullBeanView().getFullDoc();
		String descr = doc.getDcDescriptionCombined();
		if (StringUtils.length(descr) > 250) {
			descr = descr.substring(0, 240);
			descr = StringUtils.substringBeforeLast(descr, " ");
			descr = descr + "(...)";
		}

		// Andy added this - needs optimised
		String sDate = "not available";
		String sPlace = "not available";

		if (doc.getDcDate() != null && doc.getDcDate().length > 0) {
			sDate = doc.getDcDate()[0];
		}
		GET_PLACE: for (Place place : document.getPlaces()) {
			for (String key : place.getPrefLabel().keySet()) {
				sPlace = place.getPrefLabel().get(key) + " (" + key + ")";
				break GET_PLACE;
			}
		}

		return KmlPresentation.getKmlDescriptor(getMetaCanonicalUrl(),
				getCacheUrl(), WebUtils.returnFirst(doc.getEdmObject(), ""),
				WebUtils.returnFirst(doc.getTitle(), ""), descr, sDate, sPlace);
	}

	public String getObjectTitle() {
		return StringUtils.left(getBaseTitle(), 250);
	}

	/**
	 * Returns the title of the page
	 * 
	 * @return page title
	 */
	public String getPageTitle() {
		StringBuilder title = new StringBuilder(getBaseTitle());
		if (StringArrayUtils.isNotBlank(document.getDcCreator())) {
			String creator = document.getDcCreator()[0];
			// clean up creator first
			creator = creator.replaceAll("[\\<({\\[].*?[})\\>\\]]", ""); // (..)
																			// [..]
																			// <..>
																			// {..}
			creator = StringUtils.strip(creator, ","); // strip , from begin or
														// end
			creator = StringUtils.trim(creator);// strip spaces
			if (StringUtils.isNotBlank(creator)) {
				title.append(" | ").append(creator);
			}
		}

		return StringUtils.left(title.toString(), 250);
	}
	
	private String getBaseTitle() {
		String dcTitle = "";

		if (StringArrayUtils.isNotBlank(getDocument().getDcTitle())) {
			dcTitle = getDocument().getDcTitle()[0];
		} else if (StringArrayUtils.isNotBlank(document.getDctermsAlternative())) {
			dcTitle = document.getDctermsAlternative()[0];
		} else if (StringArrayUtils.isNotBlank(getDocument().getDcDescription())) {
			dcTitle = getDocument().getDcDescription()[0];
			if (dcTitle.indexOf("<br/>\n") > 0) {
				dcTitle = dcTitle.substring(0, dcTitle.indexOf("<br/>\n"));
			}
			if (dcTitle.length() > 50) {
				dcTitle = StringUtils.left(dcTitle, 50) + "...";
			}
		} else if (StringArrayUtils.isNotBlank(document.getTitle())) {
			dcTitle = document.getTitle()[0];
		}

		return dcTitle;
	}

	/**
	 * Returns the reference url
	 * 
	 * @return reference url
	 */
	public String getUrlRef() {
		return StringUtils.defaultIfBlank(document.getEdmIsShownAt()[0],
				StringUtils.defaultIfBlank(document.getEdmIsShownBy()[0], "#"));
	}

	public boolean isUrlRefIsShownBy() {
		return StringUtils.isBlank(document.getEdmIsShownAt()[0])
				&& isEuropeanaIsShownBy();
	}

	public boolean isUrlRefMms() {
		return StringUtils.startsWith(getUrlRef(), "mms");
	}

	public boolean isHasDataProvider() {
		return StringArrayUtils.isNotBlank(getDocument().getEdmDataProvider());
	}

	public String getShownAtProvider() {
		if (isHasDataProvider() && !ArrayUtils.contains(shownAtProviderOverride, getCollectionId())) {
			return getDocument().getEdmDataProvider()[0];
		} else {
			return getDocument().getEdmProvider()[0];
		}
	}

	/**
	 * Returns a list of possible citation types that can be applied to a given
	 * object.
	 * 
	 * @return - list of available citation types
	 */
	public CiteValue[] getCiteStyles() {
		return CiteStyle.values(this);
	}

	/**
	 * Whether or not to display the box of site style options. If only 1 option
	 * exists the box makes no sense.
	 * 
	 * @return Return true if more than 1 option is available else false
	 */
	public boolean isCiteStyleBox() {
		return CiteStyle.values().length > 1;
	}

	/**
	 * Selects the first available cite style and returns it as the default.
	 * 
	 * @return
	 * @throws Exception
	 *             Must be at least 1 cite style of functionality makes no sense
	 *             so throw error if less that 1 cite style is available
	 */
	public CiteValue getDefaultCiteStyle() throws Exception {
		if (CiteStyle.values().length >= 1) {
			return getCiteStyles()[0];
		} else {
			throw new Exception("At least one citation type required.");
		}

	}

	public boolean isFormatLabels() {
		return StringUtils.containsIgnoreCase("labels", getFormat());
	}

	/**
	 * For any values that are valid creates a new object and adds it to the
	 * collection of all meta data objects.
	 * 
	 * @param metaDataFields
	 *            - Collection new meta data fields should be added to
	 * @param fieldName
	 *            - Name associated with the field
	 * @param values
	 *            - Any values associated with the field
	 * @return
	 */
	private void addMetaField(List<MetaDataFieldPresentation> metaDataFields,
			Field field, String... values) {
		if ((values != null) && (field.getFieldName() != null)) {
			MetaDataFieldPresentation metaDataField = new MetaDataFieldPresentation(
					this, field, values);
			if (!metaDataField.isEmpty()) {
				metaDataFields.add(metaDataField);
			}
		}
	}

	/**
	 * Formats any url adding in any required addition parameters required for
	 * the brief view page. Useful for embedded version which must keep track of
	 * its configuration
	 * 
	 * @param url
	 *            - Url to be formatted
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public UrlBuilder getPortalFormattedUrl(UrlBuilder url)
			throws UnsupportedEncodingException {

		// override or remove these params, because they are based on internal
		// search actions...
		if (getQuery() != null) { // prevent nullpointer on empty Query...
			url.addParam("query", URLEncoder.encode(getQuery(), "utf-8"), true);
		}
		url.addParam("qf", getRefinements(), true);
		url.removeParam("pageId");

		// remove default values to clean up url...
		url.removeDefault("start", "1");
		url.removeDefault("startPage", "1");
		url.removeDefault("embedded", "false");

		return url;
	}

	@Override
	public UrlBuilder enrichFullDocUrl(UrlBuilder url) {
		try {
			return getPortalFormattedUrl(url);
		} catch (UnsupportedEncodingException e) {
			// should never happen, just ignore
		}
		return url;
	}

	@Override
	public UrlBuilder createSearchUrl(String searchTerm, String[] qf, String start) 
			throws UnsupportedEncodingException {
		return SearchUtils.createSearchUrl(getPortalName(), returnTo, searchTerm, qf, start);
	}

	public String getReturnTo() {
		return returnTo.toString();
	}
	
	/**
	 * Null-returning getter to satisfy EL
	 */
	public List<BreadCrumb> getBreadcrumbs() {
		return null;
	}
}
