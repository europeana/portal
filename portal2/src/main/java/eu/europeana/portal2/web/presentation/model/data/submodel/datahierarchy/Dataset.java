/*
 * Copyright 2007-2014 The Europeana Foundation
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

package eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy;

public class Dataset extends Item {

	private long providerCount;
	private long dataProviderCount;

	public Dataset(String name, long count) {
		super();
		this.name = name;
		this.count = count;
	}

	public long getProviderCount() {
		return providerCount;
	}

	public void setProviderCount(long providerCount) {
		this.providerCount = providerCount;
	}

	public long getDataProviderCount() {
		return dataProviderCount;
	}

	public void setDataProviderCount(long dataProviderCount) {
		this.dataProviderCount = dataProviderCount;
	}
}