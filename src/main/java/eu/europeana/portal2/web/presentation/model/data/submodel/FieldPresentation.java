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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.presentation.enums.ExternalService;
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;

/**
 * Class used to represent a field on the fullViewPage
 * 
 * @author kevinparkings
 * 
 */
public class FieldPresentation {

	public static final String FIELD_ID_PREFIX = "fpfn_";

	protected FullDocData model;
	protected Field field;
	protected List<FieldValue> fieldValues;
	protected boolean isCombined = false;

	/**
	 * Constructor full
	 * 
	 * @param fieldName
	 *            - Tag name of the field
	 * @param fieldValues
	 *            - Values associated with field
	 * @param isSeperateLines
	 *            - Whether or not to put each value on a new line
	 * @param isTranslatable
	 *            - Whether or not the field is appropriate for translation
	 * @param externalServices
	 *            - External Services associated with the field
	 */
	public FieldPresentation(FullDocData model, Field field) {
		this.model = model;
		this.field = field;
	}

	public FieldPresentation(FullDocData model, Field field, List<String> fieldValues) {
		this(model, field);
		add(model, fieldValues);
	}

	public FieldPresentation(FullDocData model, Field field, Map<Field, List<String>> fieldValues) {
		this(model, field);
		addMap(model, fieldValues);
	}

	/**
	 * Initializes the class attributes
	 * 
	 * @param fieldName
	 *            - Tag name of the field
	 * @param fieldValues
	 *            - Values associated with field
	 * @param isSeperateLines
	 *            - Whether or not to put each value on a new line
	 * @param isTranslatable
	 *            - Whether or not the fields is appropriate for translation
	 * @param externalServices
	 *            - External services associate with the field
	 */
	public void add(FullDocData model, List<String> fieldValues) {
		if (fieldValues != null) {
			for (String value : fieldValues) {
				add(model, value);
			}
		}
	}

	public void add(FullDocData model, String fieldValue) {
		if (!StringUtils.isBlank(fieldValue)) {
			if (this.fieldValues == null) {
				this.fieldValues = new ArrayList<FieldValue>();
			}
			this.fieldValues.add(new FieldValue(model, field, fieldValue));
		}
	}

	public void addMap(FullDocData model, Map<Field, List<String>> fieldValues) {
		if (fieldValues != null) {
			if (this.fieldValues == null) {
				this.fieldValues = new ArrayList<FieldValue>();
			}
			for (Entry<Field, List<String>> fieldValue : fieldValues.entrySet()) {
				if (fieldValue.getValue() == null) {
					continue;
				}
				for (String value : fieldValue.getValue()) {
					if (!StringUtils.isBlank(value)) {
						this.fieldValues.add(new FieldValue(model, fieldValue.getKey(), value));
					}
				}
			}
		}
	}

	/**
	 * Getter for fielName
	 * 
	 * @return - Tag to retrieve name for field
	 */
	public String getFieldName() {
		return field.getFieldName();
	}

	/**
	 * Getter for fieldLabel
	 * 
	 * @return - Tag to retrieve name for field
	 */
	public String getFieldLabel() {
		return field.getFieldLabel();
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

	/**
	 * Getter for fieldValue
	 * 
	 * @return Collection of values associated with field
	 */
	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}

	/**
	 * Getter for isSeperateLines
	 * 
	 * @return Whether to display values on individual lines or a single line
	 */
	public boolean isSeperateLines() {
		return field.isSeperateLines();
	}

	public String getFieldUniqueId() {
		return FIELD_ID_PREFIX + field.getFieldName();
	}

	public ExternalService[] getExternalServices() {
		return field.getExternalServices();
	}

	public boolean isShowExternalServices() {
		if (getExternalServices().length > 0) {
			return getExternalServices()[0] != null;
		}
		return false;
	}

	public boolean isShowTranslationServices() {
		return field.isTranslatable();
	}

	public boolean isESSEnabled() {
		return isShowExternalServices();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FieldPresentation[name: ").append(getFieldName());
		sb.append(", label: ").append(getFieldLabel());
		sb.append(", values: [").append(StringUtils.join(fieldValues, ", ")).append("]");
		sb.append("]");
		return sb.toString();
	}

	public boolean isCombined() {
		return isCombined;
	}

	public boolean isOptedOut() {
		return field.isOptOutAware() && model.isOptedOut();
	}

	public void setCombined(boolean isCombined) {
		this.isCombined = isCombined;
	}
}
