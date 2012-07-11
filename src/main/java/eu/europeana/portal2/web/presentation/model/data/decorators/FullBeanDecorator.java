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

package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Aggregation;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.EuropeanaAggregation;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.ProvidedCHO;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.utils.StringArrayUtils;

public class FullBeanDecorator implements FullBean {

	private static final Logger log = Logger.getLogger(FullBeanDecorator.class.getName());

	private FullBean fulldoc;

	public FullBeanDecorator(FullBean fulldoc) {
		this.fulldoc = fulldoc;
	}

	/**
	 * Returns the title of the post
	 * 
	 * @return post title
	 * @throws UnsupportedEncodingException
	 */
	public String getPostTitle() throws UnsupportedEncodingException {
		final int POST_TITLE_MAX_LENGTH = 110;
		StringBuilder postTitle = new StringBuilder();
		if (getTitle().length > POST_TITLE_MAX_LENGTH) {
			postTitle.append(getTitle()[0].substring(0, POST_TITLE_MAX_LENGTH));
			postTitle.append("...");
		} else {
			postTitle.append(getTitle()[0]);
		}
		return postTitle.toString();
	}

	/**
	 * Returns the posts author
	 * 
	 * @return posts author
	 */
	public String getPostAuthor() {
		if (StringUtils.isBlank(getDcCreator()[0])) {
			return "none";
		} else {
			return getDcCreator()[0];
		}
	}

	public String getUrlKml() {
		if (isPositionAvailable()) {
			String fullDocUrl = getId();

			if (fullDocUrl.indexOf("?") > -1) {
				fullDocUrl = fullDocUrl.substring(0, fullDocUrl.indexOf(".html"));
			}

			fullDocUrl = fullDocUrl.replace("/resolve/", "/portal/");
			fullDocUrl = fullDocUrl + ".kml";
			return fullDocUrl;
		}
		return null;
	}

	public boolean isPositionAvailable() {
		return (getEdmPlaceLatitude() != 0) || (getEdmPlaceLongitude() != 0);
	}

	@Override
	public String getFullDocUrl() {
		return fulldoc.getFullDocUrl();
	}

	@Override
	public String[] getTitle() {
		return fulldoc.getTitle();
	}

	/*
	 * @Override public String getThumbnail() { return fulldoc.getThumbnail(); }
	 */

	/*
	 * @Override public String getCreator() { return fulldoc.getCreator(); }
	 */

	@Override
	public String[] getYear() {
		return fulldoc.getYear();
	}

	@Override
	public String[] getProvider() {
		return fulldoc.getProvider();
	}

	@Override
	public String[] getDataProvider() {
		return fulldoc.getDataProvider();
	}

	@Override
	public String[] getLanguage() {
		return fulldoc.getLanguage();
	}

	@Override
	public DocType getType() {
		return fulldoc.getType();
	}
	
	public String getEdmType() {
		// List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			return proxy.getEdmType().toString();
			//items.add(proxy.getEdmType().toString());
		}
		// return StringArrayUtils.toArray(items);
		return null;
	}


	/*
	 * @Override public void setId(String id) { // left empty on purpose }
	 */

	/*
	 * @Override public void setFullDocUrl(String fullDocUrl) { // left empty on
	 * purpose }
	 */

	@Override
	public int getEuropeanaCompleteness() {
		return fulldoc.getEuropeanaCompleteness();
	}

	/*
	 * @Override public String[] getEdmnrichmentPlaceTerm() { return
	 * fulldoc.getEnrichmentPlaceTerm(); }
	 * 
	 * @Override public String[] getEnrichmentPlaceLabel() { return
	 * fulldoc.getEnrichmentPlaceLabel(); }
	 * 
	 * @Override public String[] getEnrichmentPlaceBroaderTerm() { return
	 * fulldoc.getEnrichmentPlaceBroaderTerm(); }
	 * 
	 * @Override public String[] getEnrichmentPlaceBroaderLabel() { return
	 * fulldoc.getEnrichmentPlaceBroaderLabel(); }
	 * 
	 * @Override public float getEnrichmentPlaceLatitude() { return
	 * fulldoc.getEnrichmentPlaceLatitude(); }
	 * 
	 * @Override public float getEnrichmentPlaceLongitude() { return
	 * fulldoc.getEnrichmentPlaceLongitude(); }
	 * 
	 * @Override public String[] getEnrichmentPeriodTerm() { return
	 * fulldoc.getEnrichmentPeriodTerm(); }
	 * 
	 * @Override public String[] getEnrichmentPeriodLabel() { return
	 * fulldoc.getEnrichmentPeriodLabel(); }
	 * 
	 * @Override public String[] getEnrichmentPeriodBroaderTerm() { return
	 * fulldoc.getEnrichmentPeriodBroaderTerm(); }
	 * 
	 * @Override public String[] getEnrichmentPeriodBroaderLabel() { return
	 * fulldoc.getEnrichmentPeriodBroaderLabel(); }
	 * 
	 * @Override public Date getEnrichmentPeriodBegin() { return
	 * fulldoc.getEnrichmentPeriodBegin(); }
	 * 
	 * @Override public Date getEnrichmentPeriodEnd() { return
	 * fulldoc.getEnrichmentPeriodEnd(); }
	 * 
	 * @Override public String[] getEnrichmentConceptTerm() { return
	 * fulldoc.getEnrichmentConceptTerm(); }
	 * 
	 * @Override public String[] getEnrichmentConceptLabel() { return
	 * fulldoc.getEnrichmentConceptLabel(); }
	 * 
	 * @Override public String[] getEnrichmentConceptBroaderTerm() { return
	 * fulldoc.getEnrichmentConceptBroaderTerm(); }
	 * 
	 * @Override public String[] getEnrichmentConceptBroaderLabel() { return
	 * fulldoc.getEnrichmentConceptBroaderLabel(); }
	 * 
	 * @Override public String[] getEnrichmentAgentTerm() { return
	 * fulldoc.getEnrichmentAgentTerm(); }
	 * 
	 * @Override public String[] getEnrichmentAgentLabel() { return
	 * fulldoc.getEnrichmentAgentLabel(); }
	 */

	@Override
	public String getId() {
		return fulldoc.getId();
	}

	/*
	 * @Override public String[] getThumbnails() { return
	 * fulldoc.getThumbnails(); }
	 */

	/*
	 * @Override public String[] getEuropeanaUserTag() { return
	 * fulldoc.getEuropeanaUserTag(); }
	 */

	public String getEdmCountry() {
		if (getEuropeanaAggregation() != null) {
			return getEuropeanaAggregation().getEdmCountry();
		}
		return null;
	}

	/*
	 * @Override public String[] getEuropeanaProvider() { return
	 * fulldoc.getEuropeanaProvider(); }
	 * 
	 * @Override public String[] getEuropeanaSource() { return
	 * fulldoc.getEuropeanaSource(); }
	 */

	public String getEdmLanguage() {
		if (getEuropeanaAggregation() != null) {
			return getEuropeanaAggregation().getEdmLanguage();
		}
		return null;
	}

	/*
	 * @Override public String[] getEuropeanaYear() { return
	 * fulldoc.getEuropeanaYear(); }
	 */

	public String[] getEdmDataProvider() {
		List<String> items = new ArrayList<String>();
		for (Aggregation aggregation : fulldoc.getAggregations()) {
			items.add(aggregation.getEdmDataProvider());
		}
		return StringArrayUtils.toArray(items);
	}

	public boolean isUserGeneratedContent() {
		if (StringArrayUtils.isNotBlank(fulldoc.getEdmUGC())) {
			return StringUtils.equalsIgnoreCase(fulldoc.getEdmUGC()[0], "true");
		}
		return false;
	}

	@Override
	public String[] getDctermsAlternative() {
		return fulldoc.getDctermsAlternative();
	}

	@Override
	public String[] getDctermsConformsTo() {
		return fulldoc.getDctermsConformsTo();
	}

	@Override
	public String[] getDctermsCreated() {
		return fulldoc.getDctermsCreated();
	}

	@Override
	public String[] getDctermsExtent() {
		return fulldoc.getDctermsExtent();
	}

	@Override
	public String[] getDctermsHasFormat() {
		return fulldoc.getDctermsHasFormat();
	}

	@Override
	public String[] getDctermsHasPart() {
		return fulldoc.getDctermsHasPart();
	}

	public String[] getDctermsHasVersion() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDctermsHasVersion());
		}
		return StringArrayUtils.toArray(items);
	}

	public String[] getDctermsIsFormatOf() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDctermsIsFormatOf());
		}
		return StringArrayUtils.toArray(items);
	}

	@Override
	public String[] getDctermsIsPartOf() {
		return fulldoc.getDctermsIsPartOf();
	}

	@Override
	public String[] getDctermsIsReferencedBy() {
		return fulldoc.getDctermsIsReferencedBy();
	}

	@Override
	public String[] getDctermsIsReplacedBy() {
		return fulldoc.getDctermsIsReplacedBy();
	}

	@Override
	public String[] getDctermsIsRequiredBy() {
		return fulldoc.getDctermsIsRequiredBy();
	}

	@Override
	public String[] getDctermsIssued() {
		return fulldoc.getDctermsIssued();
	}

	@Override
	public String[] getDctermsIsVersionOf() {
		return fulldoc.getDctermsIsVersionOf();
	}

	@Override
	public String[] getDctermsMedium() {
		return fulldoc.getDctermsMedium();
	}

	@Override
	public String[] getDctermsProvenance() {
		return fulldoc.getDctermsProvenance();
	}

	@Override
	public String[] getDctermsReferences() {
		return fulldoc.getDctermsReferences();
	}

	@Override
	public String[] getDctermsReplaces() {
		return fulldoc.getDctermsReplaces();
	}

	@Override
	public String[] getDctermsRequires() {
		return fulldoc.getDctermsRequires();
	}

	@Override
	public String[] getDctermsSpatial() {
		return fulldoc.getDctermsSpatial();
	}

	@Override
	public String[] getDctermsTableOfContents() {
		return fulldoc.getDctermsTableOfContents();
	}

	@Override
	public String[] getDctermsTemporal() {
		return fulldoc.getDctermsTemporal();
	}

	@Override
	public String[] getDcContributor() {
		return fulldoc.getDcContributor();
	}

	@Override
	public String[] getDcCoverage() {
		return fulldoc.getDcCoverage();
	}

	@Override
	public String[] getDcCreator() {
		return fulldoc.getDcCreator();
	}

	public String[] getDcDate() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcDate());
		}
		return StringArrayUtils.toArray(items);
	}

	public String[] getDcDescription() {
		log.info("getDcDescription()");
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			log.info("description: " + proxy.getDcDescription());
			StringArrayUtils.addToList(items, proxy.getDcDescription());
		}
		return StringArrayUtils.toArray(items);
	}

	public String getDcDescriptionCombined() {
		return StringEscapeUtils.escapeXml(StringUtils.join(getDcDescription(),
				";"));
	}

	public String[] getDcFormat() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcFormat());
		}
		return StringArrayUtils.toArray(items);
	}

	@Override
	public String[] getDcIdentifier() {
		return fulldoc.getDcIdentifier();
	}

	public String[] getDcLanguage() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcLanguage());
		}
		return StringArrayUtils.toArray(items);
	}

	@Override
	public String[] getDcPublisher() {
		return fulldoc.getDcPublisher();
	}

	@Override
	public String[] getDcRelation() {
		return fulldoc.getDcRelation();
	}

	public String[] getDcRights() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcRights());
		}
		return StringArrayUtils.toArray(items);
	}

	@Override
	public String[] getDcSource() {
		return fulldoc.getDcSource();
	}

	public String[] getDcSubject() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcSubject());
		}
		return StringArrayUtils.toArray(items);
	}

	public String[] getDcTitle() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcTitle());
		}
		return StringArrayUtils.toArray(items);
	}

	public String getDcTitleCombined() {
		return StringEscapeUtils.escapeXml(StringUtils.join(getDcTitle(), ";"));
	}

	public String[] getDcType() {
		List<String> items = new ArrayList<String>();
		for (Proxy proxy : fulldoc.getProxies()) {
			StringArrayUtils.addToList(items, proxy.getDcType());
		}
		return StringArrayUtils.toArray(items);
	}

	@Override
	public String[] getEuropeanaCollectionName() {
		return fulldoc.getEuropeanaCollectionName();
	}

	/*
	 * @Override public String getEuropeanaUri() { return
	 * fulldoc.getEuropeanaUri(); }
	 */

	@Override
	public Date getTimestamp() {
		return fulldoc.getTimestamp();
	}

	//@Override
	//public String getEuropeanaType() {
	//	return fulldoc.getEuropeanaType();
	//}


	@Override
	public String[] getAggregationEdmRights() {
		return fulldoc.getAggregationEdmRights();
	}

	@Override
	public String[] getEdmConcept() {
		return fulldoc.getEdmConcept();
	}

	@Override
	public List<Map<String, String>> getEdmConceptLabel() {
		return fulldoc.getEdmConceptLabel();
	}

	@Override
	public String[] getEdmConceptBroaderTerm() {
		return fulldoc.getEdmConceptBroaderTerm();
	}

	@Override
	public List<Map<String, String>> getEdmPlaceAltLabel() {
		return fulldoc.getEdmPlaceAltLabel();
	}

	@Override
	public List<Map<String, String>> getEdmConceptBroaderLabel() {
		return fulldoc.getEdmConceptBroaderLabel();
	}

	@Override
	public String[] getEdmTimespanBroaderTerm() {
		return fulldoc.getEdmTimespanBroaderTerm();
	}

	@Override
	public List<Map<String, String>> getEdmTimespanBroaderLabel() {
		return fulldoc.getEdmTimespanBroaderLabel();
	}

	@Override
	public String[] getEdmPlaceBroaderTerm() {
		return fulldoc.getEdmPlaceBroaderTerm();
	}

	@Override
	public String[] getUgc() {
		return fulldoc.getUgc();
	}

	@Override
	public String[] getEdmRights() {
		return fulldoc.getEdmRights();
	}

	@Override
	public void setEdmRights(String[] edmRights) {
		fulldoc.setEdmRights(edmRights);
	}

	@Override
	public String[] getCountry() {
		return fulldoc.getCountry();
	}

	@Override
	public void setCountry(String[] country) {
		fulldoc.setCountry(country);
	}

	@Override
	public void setEuropeanaCollectionName(String[] europeanaCollectionName) {
		fulldoc.setEuropeanaCollectionName(europeanaCollectionName);
	}

	@Override
	public void setDctermsIsPartOf(String[] dctermsIsPartOf) {
		fulldoc.setDctermsIsPartOf(dctermsIsPartOf);
	}

	@Override
	public String[] getEdmObject() {
		return fulldoc.getEdmObject();
	}

	@Override
	public String[] getEdmPlace() {
		return fulldoc.getEdmPlace();
	}

	@Override
	public List<Map<String, String>> getEdmPlaceLabel() {
		return fulldoc.getEdmPlaceLabel();
	}

	@Override
	public Float getEdmPlaceLatitude() {
		return fulldoc.getEdmPlaceLatitude();
	}

	@Override
	public Float getEdmPlaceLongitude() {
		return fulldoc.getEdmPlaceLongitude();
	}

	@Override
	public String[] getEdmTimespan() {
		return fulldoc.getEdmTimespan();
	}

	@Override
	public List<Map<String, String>> getEdmTimespanLabel() {
		return fulldoc.getEdmTimespanLabel();
	}

	@Override
	public String[] getEdmTimespanBegin() {
		return fulldoc.getEdmTimespanBegin();
	}

	@Override
	public String[] getEdmTimespanEnd() {
		return fulldoc.getEdmTimespanEnd();
	}

	@Override
	public String[] getEdmAgent() {
		return fulldoc.getEdmAgent();
	}

	@Override
	public List<Map<String, String>> getEdmAgentLabel() {
		return fulldoc.getEdmAgentLabel();
	}

	@Override
	public String[] getEdmIsShownBy() {
		return fulldoc.getEdmIsShownBy();
	}

	@Override
	public String[] getEdmIsShownAt() {
		return fulldoc.getEdmIsShownAt();
	}

	@Override
	public String[] getEdmProvider() {
		return fulldoc.getEdmProvider();
	}

	@Override
	public String[] getAggregationDcRights() {
		return fulldoc.getAggregationDcRights();
	}

	@Override
	public String[] getOreProxy() {
		return fulldoc.getOreProxy();
	}

	@Override
	public String[] getOwlSameAs() {
		return fulldoc.getOwlSameAs();
	}

	@Override
	public String[] getProxyDcRights() {
		return fulldoc.getProxyDcRights();
	}

	@Override
	public String[] getEdmUGC() {
		return fulldoc.getEdmUGC();
	}

	@Override
	public String[] getEdmCurrentLocation() {
		return fulldoc.getEdmCurrentLocation();
	}

	@Override
	public String[] getEdmIsNextInSequence() {
		return fulldoc.getEdmIsNextInSequence();
	}

	@Override
	public String[] getUserTags() {
		return fulldoc.getUserTags();
	}

	@Override
	public List<Map<String, String>> getEdmAgentAltLabels() {
		return fulldoc.getEdmAgentAltLabels();
	}

	@Override
	public String[] getEdmAgentSkosNote() {
		return fulldoc.getEdmAgentSkosNote();
	}

	@Override
	public String[] getEdmAgentBegin() {
		return fulldoc.getEdmAgentBegin();
	}

	@Override
	public String[] getEdmAgentEnd() {
		return fulldoc.getEdmAgentEnd();
	}

	@Override
	public String[] getEdmTimeSpanSkosNote() {
		return fulldoc.getEdmTimeSpanSkosNote();
	}

	@Override
	public String[] getEdmPlaceSkosNote() {
		return fulldoc.getEdmPlaceSkosNote();
	}

	@Override
	public String[] getEdmConceptNote() {
		return fulldoc.getEdmConceptNote();
	}

	@Override
	public List<Map<String, String>> getEdmPlaceAltLabels() {
		return fulldoc.getEdmPlaceAltLabels();
	}

	@Override
	public List<Map<String, String>> getEdmTimespanAltLabels() {
		return fulldoc.getEdmTimespanAltLabels();
	}

	@Override
	public List<Map<String, String>> getSkosConceptAltLabels() {
		return fulldoc.getSkosConceptAltLabels();
	}

	@Override
	public Boolean[] getEdmPreviewNoDistribute() {
		return fulldoc.getEdmPreviewNoDistribute();
	}

	@Override
	public String[] getEdmPlaceIsPartOf() {
		return fulldoc.getEdmPlaceIsPartOf();
	}

	@Override
	public String[] getEdmTimespanIsPartOf() {
		return fulldoc.getEdmTimespanIsPartOf();
	}

	@Override
	public String[] getEdmWebResource() {
		return fulldoc.getEdmWebResource();
	}

	@Override
	public String[] getEdmWebResourceDcRights() {
		return fulldoc.getEdmWebResourceDcRights();
	}

	@Override
	public String[] getEdmWebResourceEdmRights() {
		return fulldoc.getEdmWebResourceEdmRights();
	}

	@Override
	public List<? extends Place> getPlaces() {
		return fulldoc.getPlaces();
	}

	@Override
	public void setPlaces(List<? extends Place> places) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends Agent> getAgents() {
		return fulldoc.getAgents();
	}

	@Override
	public void setAgents(List<? extends Agent> agents) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends Timespan> getTimespans() {
		return fulldoc.getTimespans();
	}

	@Override
	public List<? extends Concept> getConcepts() {
		return fulldoc.getConcepts();
	}

	@Override
	public void setConcepts(List<? extends Concept> concepts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAggregations(List<? extends Aggregation> aggregations) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends Proxy> getProxies() {
		return fulldoc.getProxies();
	}

	@Override
	public void setProxies(List<? extends Proxy> proxies) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEuropeanaId(ObjectId europeanaId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTitle(String[] title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setYear(String[] year) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProvider(String[] provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLanguage(String[] language) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setType(DocType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEuropeanaCompleteness(int europeanaCompleteness) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTimespans(List<? extends Timespan> timespans) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends Aggregation> getAggregations() {
		return fulldoc.getAggregations();
	}

	@Override
	public List<? extends BriefBean> getRelatedItems() {
		return fulldoc.getRelatedItems();
	}

	@Override
	public void setRelatedItems(List<? extends BriefBean> relatedItems) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends ProvidedCHO> getProvidedCHOs() {
		return fulldoc.getProvidedCHOs();
	}

	@Override
	public void setProvidedCHOs(List<? extends ProvidedCHO> providedCHOs) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAbout() {
		return fulldoc.getAbout();
	}

	@Override
	public void setAbout(String about) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWhen(String[] when) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getWhen() {
		// TODO Auto-generated method stub
		return fulldoc.getWhen();
	}

	@Override
	public void setWhere(String[] where) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getWhere() {
		// TODO Auto-generated method stub
		return fulldoc.getWhere();
	}

	@Override
	public void setWhat(String[] what) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getWhat() {
		// TODO Auto-generated method stub
		return fulldoc.getWhat();
	}

	@Override
	public void setWho(String[] who) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getWho() {
		return fulldoc.getWho();
	}

	@Override
	public void setUgc(String[] ugc) {
		// TODO Auto-generated method stub

	}

	@Override
	public EuropeanaAggregation getEuropeanaAggregation() {
		return fulldoc.getEuropeanaAggregation();
	}
}
