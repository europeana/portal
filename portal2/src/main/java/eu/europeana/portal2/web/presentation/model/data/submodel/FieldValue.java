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

package eu.europeana.portal2.web.presentation.model.data.submodel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ContextualItemDecorator;

public class FieldValue {

	private FullDocData model;
	private Field field;
	private String value;
	private FullBeanDecorator.ContextualEntity entityType = null;
	private ContextualItemDecorator decorator;
	private boolean resourceUri = false;
	private boolean translated = false;
	private String translatedValue;

	public FieldValue(FullDocData model, Field field, String value) {
		this.model = model;
		this.field = field;
		this.value = value;
		setContextualDecorator();
	}

	private void setContextualDecorator() {
		if (field.equals(Field.DC_CREATOR)
				|| field.equals(Field.DC_CONTRIBUTOR)) {
			entityType = FullBeanDecorator.ContextualEntity.AGENT;
		} else if (field.equals(Field.DC_SUBJECT)
				|| field.equals(Field.DC_FORMAT)
				|| field.equals(Field.DC_TYPE)) {
			entityType = FullBeanDecorator.ContextualEntity.CONCEPT;
		} else if (field.equals(Field.DCTERMS_SPATIAL)
				|| field.equals(Field.DC_COVERAGE)) {
			entityType = FullBeanDecorator.ContextualEntity.PLACE;
		} else if (field.equals(Field.DC_DATE) 
				|| field.equals(Field.DC_COVERAGE)
				|| field.equals(Field.DCTERMS_TEMPORAL)
				|| field.equals(Field.EDM_YEAR)) {
			entityType = FullBeanDecorator.ContextualEntity.TIMESPAN;
		}

		// model.getShortcut().isEnriched(field.name())
		if (entityType != null) {
			resourceUri = model.getShortcut().isResourceUri(field.name(), value);

			if (resourceUri) {
				return;
			}
			Resource resource = model.getShortcut().getResource(field.name(), value);

			decorator = model.getDocument().getContextualConnections(entityType, value, resource);
		} else if (value.startsWith("http://") && !field.isEntityIdentifier()) {
			decorator = model.getDocument().getContextualConnections(FullBeanDecorator.ContextualEntity.ALL, value, null);
			if (decorator != null) {
				entityType = decorator.getEntityType();
			}
		}
	}

	public String getValue() {
		return value;
	}

	public String getTranslatedValue() {
		if (model.isUseBackendItemTranslation()
			&& field.isTranslatable()
			&& StringUtils.isNotBlank(model.getItemLanguage())) {
			if (translated == false) {
				translatedValue = model.translationService.translate(value, model.getItemLanguage());
				translated = true;
			}
		}
		return translatedValue;
	}

	public String getValueClean() {
		return StringUtils.remove(value, "\"'");
	}
	
	public String getValueCleaner() {
		return FieldCleaner.clean(value);
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
			// (..) [..] <..> {..}
			value = value.replaceAll("[\\<({\\[].*?[})\\>\\]]", ""); 
			// strip , or . from begin or end
			value = StringUtils.strip(value, ",."); 
			// strip spaces
			value = StringUtils.trim(value);
			// prevent 'and' word from breaking search
			value = StringUtils.replace(value, " and ", " \\and "); 
			// return search string if still not empty
			if (StringUtils.isNotBlank(value)) {
				try {
					return model.createSearchUrl(StringUtils.replace(field.getSearchOn(), "%s", value), null, null).toString();
				} catch (UnsupportedEncodingException e) {
					// ignore, will never happen
				}
			}
		}
		return null;
	}

	public String getFieldName() {
		return field.getFieldName();
	}

	public String getContextualEntity() {
		return field.getContextualEntity();
	}

	public String getSemanticAttributes() {
		return field.getSemanticAttributes();
	}

	public boolean isSemanticUrl() {
		return field.isSemanticUrl();
	}

	public boolean isOptedOut() {
		return field.isOptOutAware() && model.isOptedOut();
	}

	public ContextualItemDecorator getDecorator() {
		return decorator;
	}

	public void setDecorator(ContextualItemDecorator decorator) {
		this.decorator = decorator;
	}

	public FullBeanDecorator.ContextualEntity getEntityType() {
		return entityType;
	}

	public boolean isResourceUri() {
		return resourceUri;
	}

	@Override
	public String toString() {
		return "FieldValue [field=" + field + ", value=" + value + "]";
	}
}
