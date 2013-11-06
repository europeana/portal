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

package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.web.service.impl.EuropeanaUrlServiceImpl;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;

public class BriefBeanDecorator implements BriefBean {

	protected BriefBean briefBean;
	private UrlAwareData<?> model;
	private int index = 1;
	private String fullDocUrl;

	public BriefBeanDecorator(UrlAwareData<?> model, BriefBean briefBean, int index) {
		this(model, briefBean);
		this.index = index;
	}

	public BriefBeanDecorator(UrlAwareData<?> model, BriefBean briefBean) {
		this.model = model;
		this.briefBean = briefBean;
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

	public int getIndex() {
		return index;
	}

	public String getFullDocUrl() {
		return getFullDocUrl(true);
	}

	public String getFullDocUrl(boolean addParams) {
		if (fullDocUrl == null) {
			UrlBuilder url = EuropeanaUrlServiceImpl.getBeanInstance().getPortalRecord(true, briefBean.getId());

			if (addParams) {
				url.addParam("start", Integer.toString(getIndex()), true);
				try {
					model.enrichFullDocUrl(url);
				} catch (UnsupportedEncodingException e) {
					// never happen
				}
			}
			fullDocUrl = url.toString();
		}

		return fullDocUrl;
	}

	public String getFullDocUrlJSON() {
		return StringUtils.replace(getFullDocUrl(), "&amp;", "&");
	}

	@Override
	public String getId() {
		return briefBean.getId();
	}

	@Override
	public String[] getTitle() {
		return briefBean.getTitle();
	}

	public String[] getTitleBidi() {
		String[] title = briefBean.getTitle();
		if (title != null) {
			String[] result = new String[title.length];
			for (int i=0; i<title.length; i++) {
				String s =  StringEscapeUtils.escapeXml(title[i]);
				s = s.replaceAll("(\\d+$)", "&lrm;$1&lrm;"); // bi-directional fix for breaking yiddish texts ending with a year
				result[i] = s;
			}
			return result;
		}
		return null;
	}

	public String getTitleJoined() {
		String[] title = briefBean.getTitle();
		if (title != null) {
			String[] result = new String[title.length];

			for (int i=0; i<title.length; i++) {
				String s =  StringEscapeUtils.escapeXml(title[i]);
				s = s.replaceAll("(\\d+$)", "&lrm;$1&lrm;"); // bi-directional fix for breaking yiddish texts ending with a year

				boolean duplicate = false;
				for (String res : result) {
					if (s.equals(res)) {
						duplicate = true;
					}
				}
				if (!duplicate) {
					result[i] = s;
				}
			}
			return StringUtils.join(result, ' ');
		}
		return null;
	}

	public String getTitleJSON() {
		String s = StringUtils.join(briefBean.getTitle(), ' ');
		s = StringUtils.strip(s, "\\/");
		s = StringUtils.trim(s);
		return s;
	}

	public String getTitleXML() {
		return StringEscapeUtils.escapeXml(StringUtils.join(briefBean.getTitle(), ' '));
	}

	public String getThumbnail() {
		return getThumbnail("BRIEF_DOC"); 
	}

	private String getThumbnail(String size) {
		try {
			String tn = "";
			if (briefBean.getEdmObject() != null 
				&& briefBean.getEdmObject().length > 0
				&& !StringUtils.isBlank(briefBean.getEdmObject()[0])) {
				tn = briefBean.getEdmObject()[0];
			}
			UrlBuilder url = null;
			url = new UrlBuilder("http://europeanastatic.eu/api/image");
			url.addParam("uri", URLEncoder.encode(tn, "UTF-8"), true);
			url.addParam("size", size, true);
			url.addParam("type", getType().toString(), true);

			return model.getPortalFormattedUrl(url).toString();
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		}
	}

	public String getThumbnailJSON() {
		return StringUtils.replace(getThumbnail(), "&amp;", "&");
	}

	public String getIcon() {
		return getThumbnail("TINY"); 
	}

	public String getIconJSON() { 
		return StringUtils.replace(getIcon(), "&amp;", "&");
	}

	public String getCreator() {
		if (StringArrayUtils.isNotBlank(briefBean.getDcCreator())) {
			return briefBean.getDcCreator()[0];
		}
		if (StringArrayUtils.isNotBlank(briefBean.getDcContributor())) {
			return briefBean.getDcContributor()[0];
		}
		return null;
	}

	public String getCreatorXML() {
		return StringEscapeUtils.escapeXml(getCreator());
	}

	@Override
	public String[] getYear() {
		return briefBean.getYear();
	}

	@Override
	public String[] getProvider() {
		return briefBean.getProvider();
	}

	@Override
	public String[] getDataProvider() {
		return briefBean.getDataProvider();
	}

	@Override
	public String[] getLanguage() {
		return briefBean.getLanguage();
	}

	@Override
	public DocType getType() {
		if (briefBean.getType() == null) {
			// prevent user visual errors on unknown (invalid?) doctypes
			return DocType.IMAGE;
		}
		return briefBean.getType();
	}

	@Override
	public int getEuropeanaCompleteness() {
		return briefBean.getEuropeanaCompleteness();
	}

	@Override
	public String[] getEdmPlace() {
		return briefBean.getEdmPlace();
	}

	@Override
	public List<Map<String, String>> getEdmPlaceLabel() {
		return briefBean.getEdmPlaceLabel();
	}

	@Override
	public List<String> getEdmPlaceLatitude() {
		return briefBean.getEdmPlaceLatitude();
	}

	@Override
	public List<String> getEdmPlaceLongitude() {
		return briefBean.getEdmPlaceLongitude();
	}

	@Override
	public String[] getEdmTimespan() {
		return briefBean.getEdmTimespan();
	}

	@Override
	public List<Map<String, String>> getEdmTimespanLabel() {
		return briefBean.getEdmTimespanLabel();
	}

	@Override
	public String[] getEdmTimespanBegin() {
		return briefBean.getEdmTimespanBegin();
	}

	@Override
	public String[] getEdmTimespanEnd() {
		return briefBean.getEdmTimespanEnd();
	}

	@Override
	public List<Map<String, String>> getEdmAgentLabel() {
		return briefBean.getEdmAgentLabel();
	}

	@Override
	public Date getTimestamp() {
		return briefBean.getTimestamp();
	}

	@Override
	public String[] getDcContributor() {
		return briefBean.getDcContributor();
	}

	@Override
	public String[] getDcCreator() {
		return briefBean.getDcCreator();
	}

	@Override
	public String[] getDctermsHasPart() {
		return briefBean.getDctermsHasPart();
	}

	@Override
	public String[] getDctermsSpatial() {
		return returnNullIfEmpty(briefBean.getDctermsSpatial());
	}

	private String[] returnNullIfEmpty(String[] test) {
		return StringArrayUtils.isNotBlank(test) ? test : null;
	}

	@Override
	public String[] getEdmObject() {
		return briefBean.getEdmObject();
	}

	@Override
	public String[] getEdmAgent() {
		return briefBean.getEdmAgent();
	}

	@Override
	public Boolean isOptedOut() {
		return briefBean.isOptedOut();
	}

	@Override
	public String[] getRights() {
		return briefBean.getRights();
	}

	@Override
	public String[] getEdmPreview() {
		return briefBean.getEdmPreview();
	}
	
	@Override
	public float getScore(){
		return briefBean.getScore();
	}
}
