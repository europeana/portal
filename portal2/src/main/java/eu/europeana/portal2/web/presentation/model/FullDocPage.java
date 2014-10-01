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

package eu.europeana.portal2.web.presentation.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.definitions.solr.entity.WebResource;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.utils.CollectionUtils;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.web.model.mediaservice.MediaServiceType;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.enums.CiteStyle;
import eu.europeana.portal2.web.presentation.enums.ExternalService;
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanShortcut;
import eu.europeana.portal2.web.presentation.model.data.submodel.CiteValue;
import eu.europeana.portal2.web.presentation.model.data.submodel.MetaDataFieldPresentation;
import eu.europeana.portal2.web.presentation.model.data.submodel.RightsValue;
import eu.europeana.portal2.web.presentation.model.preparation.FullDocPreparation;
import eu.europeana.portal2.web.presentation.model.submodel.Image;
import eu.europeana.portal2.web.presentation.semantic.FieldInfo;
import eu.europeana.portal2.web.util.KmlPresentation;
import eu.europeana.portal2.web.util.SearchUtils;

public class FullDocPage extends FullDocPreparation {

	@Override
	public UrlBuilder prepareFullDocUrl(UrlBuilder builder) {
		if (isEmbedded()) {
			builder.addParam("embedded", "true", true);
		}

		// remove default values to clean up URL
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

	public String getEdmRights(){
		return getShortcutFirstValue("EdmRights");
	}

	/**
	 * Returns the url for the image that links to the source image
	 * 
	 * @return image reference
	 */
	public String getImageRef() {
		return StringUtils.defaultIfBlank(getShortcutFirstValue("EdmIsShownBy"), getShortcutFirstValue("EdmIsShownAt"));
	}

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
			lightboxRef = getShortcutFirstValue("EdmIsShownBy");
			if (lightboxRef != null) {
				lightboxRefField = "edm:isShownBy";
			}
			lightboxRefChecked = true;
		}
		return lightboxRef;
	}

	public boolean isEuropeanaIsShownBy() {
		return StringArrayUtils.isNotBlank(shortcut.get("EdmIsShownBy"));
	}

	public boolean isEuropeanaIsShownAt() {
		return StringArrayUtils.isNotBlank(shortcut.get("EdmIsShownAt"));
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
		addMetaField(fields, Field.EDM_COUNTRY, getDocument().getEdmCountry());
		addMetaField(fields, Field.EDM_PROVIDER, shortcut, "EdmProvider");
		addMetaField(fields, Field.EDM_COLLECTIONNAME, document.getEuropeanaCollectionName());
		addMetaField(fields, Field.EDM_ISSHOWNAT, shortcut, "EdmIsShownAt");
		addMetaField(fields, Field.EDM_ISSHOWNBY, shortcut, "EdmIsShownBy");
		// addMetaField(fields, Field.EUROPEANA_OBJECT, document.getThumbnails());
		addMetaField(fields, Field.EDM_OBJECT, shortcut, "EdmObject");
		addMetaField(fields, Field.EDM_LANGUAGE, getDocument().getEdmLanguage());
		// addMetaField(fields, Field.EUROPEANA_TYPE, document.getType().toString());
		// addMetaField(fields, Field.EUROPEANA_USERTAG, document.getEdmUserTag());
		// addMetaField(fields, Field.EUROPEANA_YEAR, getDocument().getEdmYear());
		addMetaField(fields, Field.EDM_RIGHTS, shortcut, "EdmRights");
		addMetaField(fields, Field.EDM_DATAPROVIDER, getDocument().getEdmDataProvider());
		addMetaField(fields, Field.EDM_UGC, shortcut, "EdmUGC");

		addMetaField(fields, Field.DCTERMS_ALTERNATIVE, shortcut, "DctermsAlternative");
		addMetaField(fields, Field.DCTERMS_CONFORMSTO, shortcut, "DctermsConformsTo");
		addMetaField(fields, Field.DCTERMS_CREATED, shortcut, "DctermsCreated");
		addMetaField(fields, Field.DCTERMS_EXTENT, shortcut, "DctermsExtent");
		addMetaField(fields, Field.DCTERMS_HASFORMAT, shortcut, "DctermsHasFormat");
		addMetaField(fields, Field.DCTERMS_HASPART, shortcut, "DctermsHasPart");
		addMetaField(fields, Field.DCTERMS_HASVERSION, getDocument().getDctermsHasVersion());
		addMetaField(fields, Field.DCTERMS_ISFORMATOF, getDocument().getDctermsIsFormatOf());
		addMetaField(fields, Field.DCTERMS_ISPARTOF, shortcut, "DctermsIsPartOf");
		addMetaField(fields, Field.DCTERMS_ISREFERENCEDBY, shortcut, "DctermsIsReferencedBy");
		addMetaField(fields, Field.DCTERMS_ISREPLACEDBY, shortcut, "DctermsIsReplacedBy");
		addMetaField(fields, Field.DCTERMS_ISREQUIREDBY, shortcut, "DctermsIsRequiredBy");
		addMetaField(fields, Field.DCTERMS_ISSUED, shortcut, "DctermsIssued");
		addMetaField(fields, Field.DCTERMS_ISVERSIONOF, shortcut, "DctermsIsVersionOf");
		addMetaField(fields, Field.DCTERMS_MEDIUM, shortcut, "DctermsMedium");
		addMetaField(fields, Field.DCTERMS_PROVENANCE, shortcut, "DctermsProvenance");
		addMetaField(fields, Field.DCTERMS_REFERENCES, shortcut, "DctermsReferences");
		addMetaField(fields, Field.DCTERMS_REPLACES, shortcut, "DctermsReplaces");
		addMetaField(fields, Field.DCTERMS_REQUIRES, shortcut, "DctermsRequires");
		addMetaField(fields, Field.DCTERMS_SPATIAL, shortcut, "DctermsSpatial");
		addMetaField(fields, Field.DCTERMS_TABLEOFCONTENTS, shortcut, "DctermsTableOfContents");
		addMetaField(fields, Field.DCTERMS_TEMPORAL, shortcut, "DctermsTemporal");

		addMetaField(fields, Field.DC_CONTRIBUTOR, shortcut, "DcContributor");
		addMetaField(fields, Field.DC_COVERAGE, shortcut, "DcCoverage");
		addMetaField(fields, Field.DC_CREATOR, shortcut, "DcCreator");
		addMetaField(fields, Field.DC_DATE, getDocument().getDcDate());
		addMetaField(fields, Field.DC_DESCRIPTION, getDocument().getDcDescription());
		addMetaField(fields, Field.DC_FORMAT, getDocument().getDcFormat());
		addMetaField(fields, Field.DC_IDENTIFIER, shortcut, "DcIdentifier");
		addMetaField(fields, Field.DC_LANGUAGE, getDocument().getDcLanguage());
		addMetaField(fields, Field.DC_PUBLISHER, shortcut, "DcPublisher");
		addMetaField(fields, Field.DC_RELATION, shortcut, "DcRelation");
		addMetaField(fields, Field.DC_RIGHTS, getDocument().getDcRights());
		addMetaField(fields, Field.DC_SOURCE, shortcut, "DcSource");
		addMetaField(fields, Field.DC_SUBJECT, getDocument().getDcSubject());
		addMetaField(fields, Field.DC_TITLE, getDocument().getDcTitle());
		addMetaField(fields, Field.DC_TYPE, getDocument().getDcType());

		addMetaField(fields, Field.EUROPEANA_COMPLETENESS, Integer.toString(document.getEuropeanaCompleteness()));

		if (document.getPlaces() != null) {
			for (Place place : document.getPlaces()) {
				addMetaField(fields, Field.ENRICHMENT_PLACE_ABOUT, place.getAbout());
				if (place.getPrefLabel() != null) {
					for (String key : place.getPrefLabel().keySet()) {
						addMetaField(fields, Field.ENRICHMENT_PLACE_LABEL, place.getPrefLabel().get(key) + " (" + key + ")");
					}
				}
				if ((place.getLatitude() != 0) || (place.getLongitude() != 0)) {
					addMetaField(fields, Field.ENRICHMENT_PLACE_LATITUDE, Float.toString(place.getLatitude()));
					addMetaField(fields, Field.ENRICHMENT_PLACE_LONGITUDE, Float.toString(place.getLongitude()));
				}
			}
		}

		if (document.getTimespans() != null) {
			for (Timespan timespan : document.getTimespans()) {
				addMetaField(fields, Field.ENRICHMENT_TIMESPAN_ABOUT, timespan.getAbout());
				for (String key : timespan.getPrefLabel().keySet()) {
					addMetaField(fields, Field.ENRICHMENT_TIMESPAN_LABEL, timespan.getPrefLabel().get(key) + " (" + key + ")");
				}
				if (timespan.getBegin() != null) {
					for (Entry<String, List<String>> item : timespan.getBegin().entrySet()) {
						for (String value : item.getValue()) {
							addMetaField(fields, Field.ENRICHMENT_TIMESPAN_BEGIN, value);
						}
					}
				}
				if (timespan.getEnd() != null) {
					for (Entry<String, List<String>> item : timespan.getEnd().entrySet()) {
						for (String value : item.getValue()) {
							addMetaField(fields, Field.ENRICHMENT_TIMESPAN_END, value);
						}
					}
				}
			}
		}

		if (document.getConcepts() != null) {
			for (Concept concept : document.getConcepts()) {
				addMetaField(fields, Field.ENRICHMENT_CONCEPT_ABOUT, concept.getAbout());
				for (String key : concept.getPrefLabel().keySet()) {
					addMetaField(fields, Field.ENRICHMENT_CONCEPT_LABEL, concept.getPrefLabel().get(key) + " (" + key + ")");
				}
				addMetaField(fields, Field.ENRICHMENT_CONCEPT_BROADER_TERM, concept.getBroader());
			}
		}

		if (document.getAgents() != null) {
			for (Agent agent : document.getAgents()) {
				addMetaField(fields, Field.ENRICHMENT_AGENT_ABOUT, agent.getAbout());
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
			if(shortcut.get("EdmRights") != null){
				String[] rightsOp = shortcut.get("EdmRights");
				rightsOption = RightsValue.safeValueByUrl(rightsOp[0], getPortalUrl());
				if (rightsOption == null && rightsOp.length > 1){
					rightsOption = RightsValue.safeValueByUrl(rightsOp[1], getPortalUrl());
				}
				if (rightsOption == null && rightsOp.length > 2){
					rightsOption = RightsValue.safeValueByUrl(rightsOp[2], getPortalUrl());
				}
			}
		}
		return rightsOption;
	}

	public String getThumbnailUrl() throws UnsupportedEncodingException {
		String thumbnail = "";
		if (shortcut.get("EdmObject") != null && shortcut.get("EdmObject").length > 0) {
			thumbnail = StringUtils.defaultIfBlank(shortcut.get("EdmObject")[0], "").trim();
		}
		return createImageUrl(thumbnail, getDocument().getEdmType(), "FULL_DOC");
	}

	public String getThumbnailUrlUnescaped() throws UnsupportedEncodingException {
		String thumbnail = "";
		if (shortcut.get("EdmObject") != null && shortcut.get("EdmObject").length > 0) {
			thumbnail = StringUtils.defaultIfBlank(shortcut.get("EdmObject")[0], "").trim();
		}
		return createImageUrl(thumbnail, getDocument().getEdmType(), "FULL_DOC");
	}

	public List<String> getThumbnails() {
		// TODO: use ThumbSize.TINY
		return getImageToShow("BRIEF_DOC");
	}

	public List<String> getFullImages() {
		// TODO: use ThumbSize.LARGE
		return getImageToShow("FULL_DOC");
	}

	private String getImageType(String imageUrl, String docType){
		String imageType = docType;
		if (imageUrl != null) {
			DocType type = DocType.getByExtention(imageUrl);
			if (type != null) {
				return type.name();
			}
		}
		return imageType;
	}

	/**
	 * will always have one entry - even if that's just a one-entry containing a default thumbnail 
	 * */
	public List<Image> getAllImages() throws UnsupportedEncodingException {
		if (imagesToShow == null) {
			String docType = getDocument().getEdmType();
			// it gets from EdmObject
			String firstImageThumbnailUrl = this.getThumbnailUrl();
			// it gets from EdmIsShownBy or EdmIsShownAt (only if EdmIsShownBy does not exists)
			String firstImageFull = getLightboxRef();
			String firstImageType = getImageType(firstImageFull, docType);

			Image firstImage = new Image(firstImageThumbnailUrl, firstImageFull, firstImageType, getLightboxRefField());
			firstImage.setRights(getDocument().getWebResourceEdmRightsByUrl(firstImageFull));
			copyWebResouceFields(firstImageThumbnailUrl, firstImage);
			firstImage.setMediaService(MediaServiceType.findInstance(firstImageFull));

			imagesToShow = new LinkedList<Image>();
			Map<String, String> images = getImages();
			for (String imageUrl : images.keySet()) {
				String imageType = getImageType(imageUrl, docType);
				Image img = createImage(imageUrl, imageType);
				if (img != null) {
					img.setEdmField(images.get(imageUrl));
					img.setRights(getDocument().getWebResourceEdmRightsByUrl(imageUrl));
					img.setMediaService(MediaServiceType.findInstance(imageUrl));
					copyWebResouceFields(imageUrl, img);
				}
				imagesToShow.add(img);
			}
			sortImages(firstImage);
		}

		return imagesToShow;
	}

	private Image createImage(String imageUrl, String imageType) {
		Image img;
		if (imageType.equals("IMAGE")) {
			img = new Image(createImageUrl(imageUrl, imageType, "BRIEF_DOC"), imageUrl, imageType);
		} else {
			img = new Image(createImageUrl("", imageType, "BRIEF_DOC"), imageUrl, imageType);
		}
		return img;
	}

	private void sortImages(Image firstImage) {
		ImageSorter<Image> sorter = new ImageSorter<Image>(imagesToShow);
		imagesToShow = sorter.sort();
		imagesToShow.add(0, firstImage);
	}

	private void copyWebResouceFields(String imageUrl, Image img) {
		WebResource webResource = getDocument().getWebResourceByUrl(imageUrl);
		if (webResource != null) {
			if (webResource.getAbout() != null) {
				img.setAbout(webResource.getAbout());
			}
			if (webResource.getIsNextInSequence() != null) {
				img.setIsNextInSequence(webResource.getIsNextInSequence());
			}
		}
	}

	private List<String> getImageToShow(String size) {
		List<String> imageList = new LinkedList<String>();
		String docType = getDocument().getEdmType();
		Map<String, String> images = getImages();
		for (String image : images.keySet()) {
			String imageType = docType;
			if (image.toLowerCase().endsWith(".mp3")) {
				imageType = DocType.SOUND.name();
			} else if (image.toLowerCase().endsWith(".mpg")) {
				imageType = DocType.VIDEO.name();
			}
			imageList.add(createImageUrl(image, imageType, size));
		}

		return imageList;
	}

	private String createImageUrl(String image, String docType, String size) {
		UrlBuilder url = new UrlBuilder(getCacheUrl());
		url.addParam("uri", image, true);
		url.addParam("size", size, true);
		url.addParam("type", docType, true);

		return prepareFullDocUrl(url).toString();
	}

	public String getIsShownBy() {
		return getShortcutFirstValue("EdmIsShownBy");
	}
	
	public String getIsShownAt() {
		return getShortcutFirstValue("EdmIsShownAt");
	}

	/**
	 * Get map of image URLs and EDM fields
	 * @return
	 */
	private Map<String, String> getImages() {
		if (allImages == null) {
			allImages = new HashMap<String, String>();

			for (String imageField : IMAGE_FIELDS.keySet()) {
				if (shortcut.get(imageField) != null && shortcut.get(imageField).length > 0) {
					for (String imageUrl : shortcut.get(imageField)) {
						if (StringUtils.isNotBlank(imageUrl)) {// && !isShownAt.contains(image)) {
							if (!imageUrl.equals(getIsShownBy())) {
								// ImagePrimitive imagePrimitive = new ImagePrimitive()
								if (!allImages.containsKey(imageUrl)) {
									allImages.put(imageUrl, IMAGE_FIELDS.get(imageField));
								}
							}
						}
					}
				}
			}
		}

		return allImages;
	}

	public String getKmlDescription() throws UnsupportedEncodingException, EuropeanaQueryException {
		FullBeanDecorator doc = (FullBeanDecorator) getFullBeanView().getFullDoc();
//		String descr = doc.getDcDescriptionCombined();
		
		// ANDY: #352 (feature request) "dc:description is not shown in its full length. Please pull in the full lenght text from our database."
		/*
		if (StringUtils.length(descr) > 250) {
			descr = descr.substring(0, 240);
			descr = StringUtils.substringBeforeLast(descr, " ");
			descr = descr + "(...)";
		}
		*/

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
				getCacheUrl(), CollectionUtils.returnFirst(getShortcut().get("EdmObject"), ""),
				CollectionUtils.returnFirst(doc.getTitle(), ""), sDate, sPlace);
	}

	public String getObjectTitle() {
		return StringUtils.left(getBaseTitle(), 250);
	}

	public String getObjectDcIdentifier() {
		return StringUtils.defaultIfBlank(getShortcutFirstValue("DcIdentifier"), "");			
	}
	
	/**
	 * Returns the title of the page
	 * 
	 * @return page title
	 */
	@Override
	public String getPageTitle() {
		StringBuilder title = new StringBuilder(getBaseTitle());
		String creator = getShortcutFirstValue("DcCreator");
		if (creator != null) {
			// clean up creator first (..), [..], <..>, {..}
			creator = creator.replaceAll("[\\<({\\[].*?[})\\>\\]]", "");
			// strip , from begin or end
			creator = StringUtils.strip(creator, ",");
			// strip spaces
			creator = StringUtils.trim(creator);
			if (StringUtils.isNotBlank(creator)) {
				title.append(" | ").append(creator);
			}
		}

		return StringUtils.left(title.toString(), 250);
	}

	private String getBaseTitle() {
		String dcTitle = "";
		if (document == null) {
			return dcTitle;
		}

		if (StringArrayUtils.isNotBlank(getDocument().getDcTitle())) {
			dcTitle = getDocument().getDcTitle()[0];
		} else if (getShortcutFirstValue("DctermsAlternative") != null) {
			dcTitle = getShortcutFirstValue("DctermsAlternative");
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
		if (urlRef == null) {
			urlRef = "#";
			if (getShortcutFirstValue("EdmIsShownAt") != null) {
				urlRef = getShortcutFirstValue("EdmIsShownAt").trim();
			} else if (getShortcutFirstValue("EdmIsShownBy") != null) {
				urlRef = getShortcutFirstValue("EdmIsShownBy").trim();
			}
		}
		return urlRef;
	}

	public boolean isUrlRefIsShownBy() {
		return (StringArrayUtils.isBlank(shortcut.get("EdmIsShownAt")))
			&& isEuropeanaIsShownBy();
	}

	public boolean isHasDataProvider() {
		return StringArrayUtils.isNotBlank(getDocument().getEdmDataProvider());
	}

	public String getShownAtProvider() {
		if (isHasDataProvider() && !ArrayUtils.contains(shownAtProviderOverride, getCollectionId())) {
			return getDocument().getEdmDataProvider()[0];
		}
		return StringUtils.defaultIfBlank(getShortcutFirstValue("EdmProvider"), "");
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
	private void addMetaField(List<MetaDataFieldPresentation> metaDataFields, Field field, String... values) {
		if ((values != null) && (field.getFieldName() != null)) {
			MetaDataFieldPresentation metaDataField = new MetaDataFieldPresentation(this, field, values);
			if (!metaDataField.isEmpty()) {
				metaDataFields.add(metaDataField);
			}
		}
	}

	private void addMetaField(List<MetaDataFieldPresentation> metaDataFields, Field field, FullBeanShortcut shortcut, String key) {
		if (shortcut != null) {
			addMetaField(metaDataFields, field, shortcut.get(key)); 
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

		if (StringUtils.isNotBlank(getQuery())) {
			url.addParam("query", getQuery(), true);
		}
		url.addParam("qf", getRefinements(), true);
		url.addParam("qt", getQueryTranslationParams(), true);

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

	public List<FieldInfo> getTopLevelSchemaMap() {
		List<FieldInfo> list = new ArrayList<FieldInfo>();
		for (FieldInfo field : edmTopLevels) {
			if (!field.getSchemaName().equals("briefBean")) {
				list.add(field);
			}
		}
		return list;
	}

	public FieldInfo getWebResourceField() {
		for (FieldInfo info : edmTopLevels) {
			if (info.getSchemaName().equals("edm:WebResource")) {
				return info;
			}
		}
		return null;
	}

	public FieldInfo getBriefBeanField() {
		for (FieldInfo info : edmTopLevels) {
			if (info.getSchemaName().equals("briefBean")) {
				return info;
			}
		}
		return null;
	}

	public String getSemanticTitle() {
		return Field.DC_TITLE.getSemanticAttributes();
	}

	public boolean isUrlRefMms() {
		return StringUtils.startsWith(getUrlRef(), "mms");
	}
	
	private String getShortcutFirstValue(String name) {
		if ((shortcut != null) && StringArrayUtils.isNotBlank(shortcut.get(name))) {
			return StringUtils.trimToNull(shortcut.get(name)[0]);
		}
		return null;
	}
	
	public String getBingToken() {
		return bingToken;
	}

	public void setBingToken(String bingToken) {
		this.bingToken = bingToken;
	}

}
