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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;
import eu.europeana.portal2.web.presentation.model.data.decorators.lists.BriefBeanListDecorator;
import eu.europeana.portal2.web.presentation.utils.UrlBuilder;

public class BriefBeanDecorator implements BriefBean {

	private static final Logger log = Logger.getLogger(BriefBeanDecorator.class.getName());

	protected BriefBean briefDoc;
	private UrlAwareData<?> model;

	public BriefBeanDecorator(UrlAwareData<?> model, BriefBean briefDoc) {
		this.model = model;
		this.briefDoc = briefDoc;
	}

	public String getKmlBegin() {
		return getEdmTimespanBegin() == null 
			? null
			: DateFormatUtils.ISO_DATE_FORMAT.format(getEdmTimespanBegin());
	}

	public String getKmlEnd() {
		return getEdmTimespanEnd() == null 
			? null
			: DateFormatUtils.ISO_DATE_FORMAT.format(getEdmTimespanEnd());
	}

	public boolean isParent() {
		// TODO: Disabled because function is not in use yet...
		// return StringArrayUtils.isNotBlank(getDcTermsHasPart());
		return false;
	}

	/*
	 * @Override public int getIndex() { return briefDoc.getIndex(); }
	 */

	@Override
	public String getFullDocUrl() {
		// log.info("1: " + briefDoc.getId());
		String url = "record/" + briefDoc.getId().replace("#", "") + ".html";
		UrlBuilder builder = new UrlBuilder(url);
		// log.info("1: " + briefDoc.getId());
		// log.info("2: " + url);

		// builder.addParam("start", Integer.toString(getIndex()), true);

		try {
			builder = model.enrichFullDocUrl(builder);
		} catch (UnsupportedEncodingException e) {
			// will never happen, do ignore...
		}
		// log.info("3: " + builder.toString());
		return builder.toString();
	}

	public String getFullDocUrlJSON() {
		return StringUtils.replace(getFullDocUrl(), "&amp;", "&");
	}

	@Override
	public String getId() {
		return briefDoc.getId();
	}

	@Override
	public String[] getTitle() {
		return briefDoc.getTitle();
	}

	public String getTitleJSON() {
		String s = StringUtils.join(briefDoc.getTitle(), ' ');
		s = StringUtils.strip(s, "\\/");
		s = StringUtils.trim(s);
		return s;
	}

	public String getTitleXML() {
		return StringEscapeUtils.escapeXml(StringUtils.join(briefDoc.getTitle(), ' '));
	}

	/*
	 * @Override public String getThumbnail() { return
	 * getThumbnail("BRIEF_DOC"); }
	 */

	/*
	 * private String getThumbnail(String size) { try { String tn =
	 * StringUtils.defaultIfBlank(briefDoc.getThumbnail(), ""); UrlBuilder url =
	 * null; if (model.isUseCache()) { url = new
	 * UrlBuilder(model.getCacheUrl()); url.addParam("uri",
	 * URLEncoder.encode(tn, "UTF-8"), true); url.addParam("size", size, true);
	 * url.addParam("type", getType().toString(), true); } else { url = new
	 * UrlBuilder(tn); } return model.getPortalFormattedUrl(url).toString(); }
	 * catch (UnsupportedEncodingException e) { return e.getMessage(); } }
	 */

	/*
	 * public String getThumbnailJSON() { return
	 * StringUtils.replace(getThumbnail(), "&amp;", "&"); }
	 */
	/*
	 * public String getIcon() { return getThumbnail("TINY"); }
	 */

	/*
	 * public String getIconJSON() { return StringUtils.replace(getIcon(),
	 * "&amp;", "&"); }
	 */

	/*
	 * @Override public String[] getThumbnails() { return null;
	 * //briefDoc.getThumbnails(); }
	 */

	public String getCreator() {
		if (StringArrayUtils.isNotBlank(briefDoc.getDcCreator())) {
			return briefDoc.getDcCreator()[0];
		}
		if (StringArrayUtils.isNotBlank(briefDoc.getDcContributor())) {
			return briefDoc.getDcContributor()[0];
		}
		return null;
	}

	public String getCreatorXML() {
		return StringEscapeUtils.escapeXml(getCreator());
	}

	@Override
	public String[] getYear() {
		return briefDoc.getYear();
	}

	@Override
	public String[] getProvider() {
		return briefDoc.getProvider();
	}

	@Override
	public String[] getDataProvider() {
		return briefDoc.getDataProvider();
	}

	@Override
	public String[] getLanguage() {
		return briefDoc.getLanguage();
	}

	@Override
	public DocType getType() {
		if (briefDoc.getType() == null) {
			// prevent user visual errors on unknown (invalid?) doctypes
			return DocType.IMAGE;
		}
		return briefDoc.getType();
	}

	@Override
	public int getEuropeanaCompleteness() {
		return briefDoc.getEuropeanaCompleteness();
	}

	@Override
	public String[] getEdmPlace() {
		return briefDoc.getEdmPlace();
	}

	@Override
	public List<Map<String, String>> getEdmPlaceLabel() {
		return briefDoc.getEdmPlaceLabel();
	}

	/*
	 * these are in ApiBean
	 * 
	 * @Override public String[] getEdmPlaceBroaderTerm() { return
	 * briefDoc.getEdmPlaceBroaderTerm(); }
	 * 
	 * @Override public String[] getEdmPlaceBroaderLabel() { return
	 * briefDoc.getEdmPlaceBroaderLabel(); }
	 */

	@Override
	public Float getEdmPlaceLatitude() {
		return briefDoc.getEdmPlaceLatitude();
	}

	@Override
	public Float getEdmPlaceLongitude() {
		return briefDoc.getEdmPlaceLongitude();
	}

	@Override
	public String[] getEdmTimespan() {
		return briefDoc.getEdmTimespan();
	}

	@Override
	public List<Map<String, String>> getEdmTimespanLabel() {
		return briefDoc.getEdmTimespanLabel();
	}

	/*
	 * in ApiBean
	 * 
	 * @Override public String[] getEdmPeriodBroaderTerm() { return
	 * briefDoc.getEdmPeriodBroaderTerm(); }
	 * 
	 * @Override public String[] getEdmPeriodBroaderLabel() { return
	 * briefDoc.getEdmPeriodBroaderLabel(); }
	 */

	@Override
	public String[] getEdmTimespanBegin() {
		return briefDoc.getEdmTimespanBegin();
	}

	@Override
	public String[] getEdmTimespanEnd() {
		return briefDoc.getEdmTimespanEnd();
	}

	/*
	 * all in ApiBean
	 * 
	 * @Override public String[] getEdmConceptTerm() { return
	 * briefDoc.getEdmConceptTerm(); }
	 * 
	 * @Override public String[] getEdmConceptLabel() { return
	 * briefDoc.getEdmConceptLabel(); }
	 * 
	 * @Override public String[] getEdmConceptBroaderTerm() { return
	 * briefDoc.getEdmConceptBroaderTerm(); }
	 * 
	 * @Override public String[] getEdmConceptBroaderLabel() { return
	 * briefDoc.getEdmConceptBroaderLabel(); }
	 */

	/*
	 * @Override public String[] getEdmAgentTerm() { return
	 * briefDoc.getEdmAgentTerm(); }
	 */

	@Override
	public List<Map<String, String>> getEdmAgentLabel() {
		return briefDoc.getEdmAgentLabel();
	}

	@Override
	public Date getTimestamp() {
		return briefDoc.getTimestamp();
	}

	/*
	 * in ApiBean aggregationEdmRights
	 * 
	 * @Override public String[] getEuropeanaRights() { return
	 * briefDoc.getEuropeanaRights(); }
	 */

	@Override
	public String[] getDcContributor() {
		return briefDoc.getDcContributor();
	}

	@Override
	public String[] getDcCreator() {
		return briefDoc.getDcCreator();
	}

	@Override
	public String[] getDctermsHasPart() {
		return briefDoc.getDctermsHasPart();
	}

	/*
	 * in ApiBean
	 * 
	 * @Override public String[] getDctermsIsPartOf() { return
	 * briefDoc.getDctermsIsPartOf(); }
	 */

	@Override
	public String[] getDctermsSpatial() {
		return returnNullIfEmpty(briefDoc.getDctermsSpatial());
	}

	private String[] returnNullIfEmpty(String[] test) {
		return StringArrayUtils.isNotBlank(test) ? test : null;
	}

	@Override
	public String[] getEdmObject() {
		return briefDoc.getEdmObject();
	}

	@Override
	public String[] getEdmAgent() {
		return briefDoc.getEdmAgent();
	}

}
