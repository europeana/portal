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

import java.util.ArrayList;
import java.util.List;

public class DataProviderItem extends Item implements DatasetContainer {

	private List<Dataset> datasets;
	private long totalDatasetCount;

	public DataProviderItem(String name, long count) {
		this.name = name;
		this.count = count;
		this.datasets = new ArrayList<Dataset>();
	}

	public void addDataset(Dataset dataset) {
		datasets.add(dataset);
	}

	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}

	public List<Dataset> getDatasets() {
		return datasets;
	}

	public long getTotalDatasetCount() {
		return totalDatasetCount;
	}

	public void setTotalDatasetCount(long totalDatasetCount) {
		this.totalDatasetCount = totalDatasetCount;
	}
}