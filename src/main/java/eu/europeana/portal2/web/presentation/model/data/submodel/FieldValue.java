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

package eu.europeana.portal2.web.presentation.model.data.submodel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;

public class FieldValue {

	private UrlAwareData<?> model;
	private Field field;
	private String value;

	public FieldValue(UrlAwareData<?> model, Field field, String value) {
		this.model = model;
		this.field = field;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getValueClean() {
		return StringUtils.remove(value, "\"'");
	}

	/**
	 * Getter method for the value of the field
	 * 
	 * @return value of the field
	 */
	public String getValueXML() {
		return StringEscapeUtils.escapeXml(value);
	}

	/**
	 * Getter method for the value of the field
	 * 
	 * @return value of the field
	 */
	public String getValueJSON() {
		String s = value;
		s = StringUtils.strip(s, "\\/");
		s = StringUtils.trim(s);
		return StringEscapeUtils.escapeJava(s);
	}

	/**
	 * Getter method for the value of the field
	 * 
	 * @return value of the field
	 */
	public String getValueURL() {
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// ingore this, will never happen
		}
		;
		return value;
	}

	public boolean isUrl() {
		return StringUtils.startsWith(getValueClean(), "http");
	}

	public String getSearchOn() {
		if ((field.getSearchOn() != null) && StringUtils.isNotBlank(getValue())) {
			String value = getValue();
			// clean up value first
			value = value.replaceAll("[\\<({\\[].*?[})\\>\\]]", ""); // (..)
																		// [..]
																		// <..>
																		// {..}
			value = StringUtils.strip(value, ",."); // strip , or . from begin
													// or end
			value = StringUtils.trim(value);// strip spaces
			value = StringUtils.replace(value, " and ", " \\and "); // prevent
																	// and word
																	// from
																	// breaking
																	// search
			// return search string if still not empty
			if (StringUtils.isNotBlank(value)) {
				try {
					return model.createSearchUrl(
							StringUtils.replace(field.getSearchOn(), "%s",
									value), null, null).toString();
				} catch (UnsupportedEncodingException e) {
					// ignore, will never happen
				}
			}
		}
		return null;
	}

}
