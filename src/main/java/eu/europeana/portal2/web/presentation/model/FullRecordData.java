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

import eu.europeana.portal2.web.presentation.enums.ExternalService;
import eu.europeana.portal2.web.presentation.model.abstracts.RestLocationsData;

public abstract class FullRecordData extends RestLocationsData<Void> {

	private boolean showEssOptions;
	private boolean showEssTranslationServices;
	private boolean showEssServices;
	private ExternalService[] services;
	private String field;
	protected String value;

	public boolean isShowEssOptions() {
		return showEssOptions;
	}

	public void setShowEssOptions(boolean showEssOptions) {
		this.showEssOptions = showEssOptions;
	}

	public boolean isShowEssTranslationServices() {
		return showEssTranslationServices;
	}

	public void setShowEssTranslationServices(boolean showEssTranslationServices) {
		this.showEssTranslationServices = showEssTranslationServices;
	}

	public boolean isShowEssServices() {
		return showEssServices;
	}

	public void setShowEssServices(boolean showEssServices) {
		this.showEssServices = showEssServices;
	}

	public ExternalService[] getServices() {
		return services;
	}

	public void setServices(ExternalService[] services) {
		this.services = services;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
