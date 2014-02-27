package eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy;

import java.util.List;

public interface DatasetContainer {

	public void setDatasets(List<Dataset> datasets);
	public void setTotalDatasetCount(long totalDatasetCount);

}
