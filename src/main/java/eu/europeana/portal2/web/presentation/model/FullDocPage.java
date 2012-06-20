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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.portal2.web.presentation.enums.ExternalService;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public class FullDocPage {// extends FullDocPreparation {

	/*
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
	/*
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
	/*
    public String getImageRef() {
        return StringArrayUtils.isNotBlank(document.getEuropeanaIsShownBy()) ? document.getEuropeanaIsShownBy()[0]
                : document.getEuropeanaIsShownAt()[0];
    }
    
    private String lightboxRef = null;
    private boolean lightboxRefChecked = false;
    
    /**
     * TODO: we need to allow for providers that haven't followed the guidelines and haven't given us an "isShownBy" value.
     * For example, the pdf here is not downloadable:
     * 	http://test.europeana.eu/portal/record/91611/D7D2BA903AA018AA86390F326F4F426256A9FD61.html
     * 
     * In this case (and others where this is no "isShownBy") we need to check the value of "isShownAt", and if it passes the
     * mime type test show that instead.
     * */
	/*
    public String getLightboxRef() {
    	if (!lightboxRefChecked) {
	    	if (StringArrayUtils.isBlank(document.getEuropeanaIsShownBy()) && StringArrayUtils.isBlank(document.getEuropeanaIsShownAt())) {
	    		lightboxRef = null;
	    	}
	    	String shownBy = StringUtils.substringBefore(document.getEuropeanaIsShownBy()[0], "?");
	    	String shownAt = StringUtils.substringBefore(document.getEuropeanaIsShownAt()[0], "?");
	    	if(StringUtils.startsWith(shownBy, "mms") && StringUtils.startsWith(shownAt, "mms")){
	    		lightboxRef = null;
	    	}
	    	
	    	FileNameMap fileNameMap = URLConnection.getFileNameMap();
	    	
	    	
	    	if (WebUtils.checkMimeType(shownBy) != null) {
	    		lightboxRef = shownBy;
	    		String type = fileNameMap.getContentTypeFor(document.getEuropeanaIsShownBy()[0]);
	    		if (WebUtils.checkMimeType(type) != null) {
	    			lightboxRef = document.getEuropeanaIsShownBy()[0];
	    		}
	    	}
	    	else if (WebUtils.checkMimeType(shownAt) != null){
	    		lightboxRef = shownAt;
	    		String type = fileNameMap.getContentTypeFor(document.getEuropeanaIsShownAt()[0]);
	    		if (WebUtils.checkMimeType(type) != null) {
	    			lightboxRef = document.getEuropeanaIsShownAt()[0];
	    		}
	    	}
	    	
	    	lightboxRefChecked = true;
    	}
    	return lightboxRef;
    }
    

    
    public boolean isEuropeanaIsShownBy() {
    	return StringArrayUtils.isNotBlank(document.getEuropeanaIsShownBy());
    }

    /**
     * Returns a collection of Meta Data fields
     * 
     * @return collection of meta data fields
     * @throws EuropeanaQueryException
     */
	/*
    public List<MetaDataFieldPresentation> getMetaDataFields() {
        List<MetaDataFieldPresentation> fields = new ArrayList<MetaDataFieldPresentation>();

        addMetaField(fields, Field.EUROPEANA_URI, document.getId());
        addMetaField(fields, Field.EUROPEANA_COUNTRY, document.getEuropeanaCountry());
        addMetaField(fields, Field.EUROPEANA_PROVIDER, document.getEuropeanaProvider());
        addMetaField(fields, Field.EUROPEANA_COLLECTIONNAME, document.getEuropeanaCollectionName());
        addMetaField(fields, Field.EUROPEANA_ISSHOWNAT, document.getEuropeanaIsShownAt());
        addMetaField(fields, Field.EUROPEANA_ISSHOWNBY, document.getEuropeanaIsShownBy());
        addMetaField(fields, Field.EUROPEANA_OBJECT, document.getThumbnails());
        addMetaField(fields, Field.EUROPEANA_LANGUAGE, document.getEuropeanaLanguage());
        addMetaField(fields, Field.EUROPEANA_TYPE, document.getType().toString());
        addMetaField(fields, Field.EUROPEANA_USERTAG, document.getEuropeanaUserTag());
        addMetaField(fields, Field.EUROPEANA_YEAR, document.getEuropeanaYear());
        addMetaField(fields, Field.EUROPEANA_RIGHTS, document.getEuropeanaRights());
        addMetaField(fields, Field.EUROPEANA_DATAPROVIDER, document.getEuropeanaDataProvider());
        addMetaField(fields, Field.EUROPEANA_UGC, document.getEuropeanaUGC());

        addMetaField(fields, Field.DCTERMS_ALTERNATIVE, document.getDcTermsAlternative());
        addMetaField(fields, Field.DCTERMS_CONFORMSTO, document.getDcTermsConformsTo());
        addMetaField(fields, Field.DCTERMS_CREATED, document.getDcTermsCreated());
        addMetaField(fields, Field.DCTERMS_EXTENT, document.getDcTermsExtent());
        addMetaField(fields, Field.DCTERMS_HASFORMAT, document.getDcTermsHasFormat());
        addMetaField(fields, Field.DCTERMS_HASPART, document.getDcTermsHasPart());
        addMetaField(fields, Field.DCTERMS_HASVERSION, document.getDcTermsHasVersion());
        addMetaField(fields, Field.DCTERMS_ISFORMATOF, document.getDcTermsIsFormatOf());
        addMetaField(fields, Field.DCTERMS_ISPARTOF, document.getDcTermsIsPartOf());
        addMetaField(fields, Field.DCTERMS_ISREFERENCEDBY, document.getDcTermsIsReferencedBy());
        addMetaField(fields, Field.DCTERMS_ISREPLACEDBY, document.getDcTermsIsReplacedBy());
        addMetaField(fields, Field.DCTERMS_ISREQUIREDBY, document.getDcTermsIsRequiredBy());
        addMetaField(fields, Field.DCTERMS_ISSUED, document.getDcTermsIssued());
        addMetaField(fields, Field.DCTERMS_ISVERSIONOF, document.getDcTermsIsVersionOf());
        addMetaField(fields, Field.DCTERMS_MEDIUM, document.getDcTermsMedium());
        addMetaField(fields, Field.DCTERMS_PROVENANCE, document.getDcTermsProvenance());
        addMetaField(fields, Field.DCTERMS_REFERENCES, document.getDcTermsReferences());
        addMetaField(fields, Field.DCTERMS_REPLACES, document.getDcTermsReplaces());
        addMetaField(fields, Field.DCTERMS_REQUIRES, document.getDcTermsRequires());
        addMetaField(fields, Field.DCTERMS_SPATIAL, document.getDcTermsSpatial());
        addMetaField(fields, Field.DCTERMS_TABLEOFCONTENTS, document.getDcTermsTableOfContents());
        addMetaField(fields, Field.DCTERMS_TEMPORAL, document.getDcTermsTemporal());

        addMetaField(fields, Field.DC_CONTRIBUTOR, document.getDcContributor());
        addMetaField(fields, Field.DC_COVERAGE, document.getDcCoverage());
        addMetaField(fields, Field.DC_CREATOR, document.getDcCreator());
        addMetaField(fields, Field.DC_DATE, document.getDcDate());
        addMetaField(fields, Field.DC_DESCRIPTION, document.getDcDescription());
        addMetaField(fields, Field.DC_FORMAT, document.getDcFormat());
        addMetaField(fields, Field.DC_IDENTIFIER, document.getDcIdentifier());
        addMetaField(fields, Field.DC_LANGUAGE, document.getDcLanguage());
        addMetaField(fields, Field.DC_PUBLISHER, document.getDcPublisher());
        addMetaField(fields, Field.DC_RELATION, document.getDcRelation());
        addMetaField(fields, Field.DC_RIGHTS, document.getDcRights());
        addMetaField(fields, Field.DC_SOURCE, document.getDcSource());
        addMetaField(fields, Field.DC_SUBJECT, document.getDcSubject());
        addMetaField(fields, Field.DC_TITLE, document.getDcTitle());
        addMetaField(fields, Field.DC_TYPE, document.getDcType());

        addMetaField(fields, Field.EUROPEANA_COMPLETENESS, Integer.toString(document.getEuropeanaCompleteness()));
        addMetaField(fields, Field.ENRICHMENT_PLACE_TERM, document.getEnrichmentPlaceTerm());
        addMetaField(fields, Field.ENRICHMENT_PLACE_LABEL, document.getEnrichmentPlaceLabel());
        if ((document.getEnrichmentPlaceLatitude() != 0) || (document.getEnrichmentPlaceLongitude() != 0)) {
            addMetaField(fields, Field.ENRICHMENT_PLACE_LATITUDE, Float.toString(document.getEnrichmentPlaceLatitude()));
            addMetaField(fields, Field.ENRICHMENT_PLACE_LONGITUDE, Float.toString(document.getEnrichmentPlaceLongitude()));
        }
        addMetaField(fields, Field.ENRICHMENT_PLACE_BROADER_TERM, document.getEnrichmentPlaceBroaderTerm());
        addMetaField(fields, Field.ENRICHMENT_PLACE_BROADER_LABEL, document.getEnrichmentPlaceBroaderLabel());

        addMetaField(fields, Field.ENRICHMENT_PERIOD_TERM, document.getEnrichmentPeriodTerm());
        addMetaField(fields, Field.ENRICHMENT_PERIOD_LABEL, document.getEnrichmentPeriodLabel());
        if (document.getEnrichmentPeriodBegin() != null) {
            addMetaField(fields, Field.ENRICHMENT_PERIOD_BEGIN, document.getEnrichmentPeriodBegin()
                    .toString());
        }
        if (document.getEnrichmentPeriodEnd() != null) {
            addMetaField(fields, Field.ENRICHMENT_PERIOD_END, document.getEnrichmentPeriodEnd()
                    .toString());
        }
        addMetaField(fields, Field.ENRICHMENT_PERIOD_BROADER_TERM, document.getEnrichmentPeriodBroaderTerm());
        addMetaField(fields, Field.ENRICHMENT_PERIOD_BROADER_LABEL, document.getEnrichmentPeriodBroaderLabel());

        addMetaField(fields, Field.ENRICHMENT_CONCEPT_TERM, document.getEnrichmentConceptTerm());
        addMetaField(fields, Field.ENRICHMENT_CONCEPT_LABEL, document.getEnrichmentConceptLabel());
        addMetaField(fields, Field.ENRICHMENT_CONCEPT_BROADER_TERM, document.getEnrichmentConceptBroaderTerm());
        addMetaField(fields, Field.ENRICHMENT_CONCEPT_BROADER_LABEL, document.getEnrichmentConceptBroaderLabel());

        addMetaField(fields, Field.ENRICHMENT_AGENT_TERM, document.getEnrichmentAgentTerm());
        addMetaField(fields, Field.ENRICHMENT_AGENT_LABEL, document.getEnrichmentAgentLabel());

        return fields;
    }

    /**
     * Link should only appear if there are results to show
     * 
     * @return - Whether or not to show the more fields link
     * @throws Exception
     */
	/*
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
	/*
    public RightsValue getRightsOption() {
        if (rightsOption == null) {
            rightsOption = RightsValue.safeValueByUrl( document.getEuropeanaRights()[0] );
        }
        return rightsOption;
    }

    public String getThumbnailUrl() throws UnsupportedEncodingException {
        String thumbnail = URLEncoder.encode(document.getThumbnails()[0], "utf-8");
        if (isUseCache()) {
            UrlBuilder url = new UrlBuilder(getCacheUrl());
            url.addParam("uri", thumbnail, true);
            url.addParam("size", "FULL_DOC", true);
            url.addParam("type", document.getType().toString(), true);
            return prepareFullDocUrl(url).toString();
        }
        return thumbnail;
    }
    
    public boolean isAllowIndexing() {
        return (document.getEuropeanaCompleteness() >= SitemapController.MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES);
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
        
        if( doc.getDcDate() != null && doc.getDcDate().length > 0 ){
        	sDate = doc.getDcDate()[0];
        }
        if( doc.getEnrichmentPlaceLabel() != null && doc.getEnrichmentPlaceLabel().length > 0 ){
        	sPlace = doc.getEnrichmentPlaceLabel()[0];
        }
        
        return KmlPresentation.getKmlDescriptor(getMetaCanonicalUrl(), getCacheUrl(),
                WebUtils.returnFirst(doc.getThumbnails(), ""), doc.getTitle(), descr,
                sDate,
                sPlace
                );
    }
    
    public String getObjectTitle() {
        StringBuilder title = new StringBuilder(StringUtils.defaultIfBlank(
                document.getDcTitle()[0],
                StringUtils.defaultIfBlank(document.getDcTermsAlternative()[0],
                        StringUtils.left(document.getDcDescription()[0], 50))));
        return StringUtils.left(title.toString(), 250);
    }

    /**
     * Returns the title of the page
     * 
     * @return page title
     */
	/*
    public String getPageTitle() {
    	
        StringBuilder title = new StringBuilder(StringUtils.defaultIfBlank(
                document.getDcTitle()[0],
                StringUtils.defaultIfBlank(document.getDcTermsAlternative()[0],
                        StringUtils.left(document.getDcDescription()[0], 50))));
        if (StringArrayUtils.isNotBlank(document.getDcCreator())) {
        	String creator = document.getDcCreator()[0];
        	// clean up creator first
        	creator = creator.replaceAll("[\\<({\\[].*?[})\\>\\]]", ""); // (..) [..] <..> {..}
        	creator = StringUtils.strip(creator, ","); // strip , from begin or end
        	creator = StringUtils.trim(creator);// strip spaces
        	if (StringUtils.isNotBlank(creator)) {
        		title.append(" | ").append(creator);
        	}
        	
        }
        return StringUtils.left(title.toString(), 250);
    }

    /**
     * Returns the reference url
     * 
     * @return reference url
     */
	/*
    public String getUrlRef() {
        return StringUtils.defaultIfBlank(document.getEuropeanaIsShownAt()[0],
                StringUtils.defaultIfBlank(document.getEuropeanaIsShownBy()[0], "#"));
    }
    
    public boolean isUrlRefIsShownBy() {
    	return StringUtils.isBlank(document.getEuropeanaIsShownAt()[0]) && isEuropeanaIsShownBy();
    }

    public boolean isUrlRefMms() {
        return StringUtils.startsWith(getUrlRef(), "mms");
    }
    
    public boolean isHasDataProvider() {
        return StringArrayUtils.isNotBlank(document.getEuropeanaDataProvider());
    }
    
    
    /**
     * 
     * @return
     */
	/*
    public String getShownAtProvider() {
    	if(isHasDataProvider() && !ArrayUtils.contains(shownAtProviderOverride, getCollectionId() )){
    		return getDocument().getEuropeanaDataProvider()[0];
    	}
    	else{
    		return getDocument().getEuropeanaProvider()[0];
    	}
    }

    
    
    /**
     * Returns a list of possible citation types that can be applied to a given object.
     * 
     * @return - list of available citation types
     */
	/*
    public CiteValue[] getCiteStyles() {
        return CiteStyle.values(this);
    }

    /**
     * Whether or not to display the box of site style options. If only 1 option exists the box makes no sense.
     * 
     * @return Return true if more than 1 option is available else false
     */
    /*
    public boolean isCiteStyleBox() {
        return CiteStyle.values().length > 1;
    }

    /**
     * Selects the first available cite style and returns it as the default.
     * 
     * @return
     * @throws Exception
     *             Must be at least 1 cite style of functionality makes no sense so throw error if less that 1 cite
     *             style is available
     */
    /*
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
     * For any values that are valid creates a new object and adds it to the collection of all meta data objects.
     * 
     * @param metaDataFields
     *            - Collection new meta data fields should be added to
     * @param fieldName
     *            - Name associated with the field
     * @param values
     *            - Any values associated with the field
     * @return
     */
    /*
    private void addMetaField(List<MetaDataFieldPresentation> metaDataFields, Field field, String... values) {
        if ((values != null) && (field.getFieldName() != null)) {
            MetaDataFieldPresentation metaDataField = new MetaDataFieldPresentation(this, field, values);
            if (!metaDataField.isEmpty()) {
                metaDataFields.add(metaDataField);
            }
        }
    }
    
    /**
     * Formats any url adding in any required addition parameters required for the brief view page. Useful for embedded
     * version which must keep track of its configuration
     * 
     * @param url
     *            - Url to be formatted
     * @return
     * @throws UnsupportedEncodingException
     */
    /*
    @Override
    public UrlBuilder getPortalFormattedUrl(UrlBuilder url) throws UnsupportedEncodingException {
        
        // override or remove these params, because they are based on internal search actions...
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
    public UrlBuilder createSearchUrl(String searchTerm, String[] qf, String start) throws UnsupportedEncodingException {
    	return SearchUtils.createSearchUrl(getPortalName(), returnTo, searchTerm, qf, start);
    }
    
    public String getReturnTo() {
    	return returnTo.toString();
    }
    */

}
