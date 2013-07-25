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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.portal2.web.presentation.enums.Field;
import eu.europeana.portal2.web.presentation.model.FullDocPage;

/**
 * Class provides functionality to present meta Data Fields
 * 
 * @author kevinparkings
 * 
 */
public class MetaDataFieldPresentation {

	Field field = null;
	List<FieldValue> fieldValues = new ArrayList<FieldValue>();

	/**
	 * Constructor for Class.
	 * 
	 * @param fieldName
	 *            - name of the meta data field
	 * @param value
	 *            - value of meta data field
	 */
	public MetaDataFieldPresentation(FullDocPage model, Field field, String value) {
		this.field = field;
		fieldValues.add(new FieldValue(model, field, StringArrayUtils.clean(value)));
	}

	/**
	 * Constructor for Class.
	 * 
	 * @param fieldName
	 *            - name of the meta data field
	 * @param value
	 *            - value of meta data field
	 */
	public MetaDataFieldPresentation(FullDocPage model, Field field, String[] value) {
		this.field = field;
		for (String string : value) {
			if (StringUtils.isNotBlank(string) && !string.equals("0000")) {
				fieldValues.add(new FieldValue(model, field, StringArrayUtils.clean(string)));
			}
		}
	}

	/**
	 * Returns the property of the meta data field which is a formatted version
	 * of the FieldName
	 * 
	 * @return property of meta data field
	 */
	public String getProperty() {

		final String MARCEL_DTC_PROPERTY = "marcrel:dtc";
		final String DC_CREATOR_PROPERTY = "cc:attributionName";

		StringBuilder property = new StringBuilder();

		if (field == Field.EDM_DATAPROVIDER) {
			property.append(getFieldName()).append(" ").append(MARCEL_DTC_PROPERTY);
		} else if (field == Field.EDM_PROVIDER) {
			property.append(getFieldName()).append(" ").append(MARCEL_DTC_PROPERTY);
		} else if (field == Field.DC_CREATOR) {
			property.append(getFieldName()).append(" ").append(DC_CREATOR_PROPERTY);
		} else {
			property.append(getFieldName());
		}

		return property.toString();
	}

	/**
	 * Getter method for fieldName.
	 * 
	 * @return The field name
	 */
	public String getFieldName() {
		return field.getFieldName();
	}

	public boolean isArray() {
		return fieldValues.size() > 1;
	}

	public boolean isEmpty() {
		return fieldValues.isEmpty();
	}

	public FieldValue getFieldValue() {
		return fieldValues.get(0);
	}

	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}
}
