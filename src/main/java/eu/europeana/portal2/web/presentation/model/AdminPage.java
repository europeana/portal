package eu.europeana.portal2.web.presentation.model;

import java.text.DecimalFormat;

import eu.europeana.portal2.web.presentation.model.data.AdminData;

public class AdminPage extends AdminData {

	public String getTotalMemory() {
		return new DecimalFormat("#,###,###,###").format(Runtime.getRuntime().totalMemory());
	}

	public String getFreeMemory() {
		return new DecimalFormat("#,###,###,###").format(Runtime.getRuntime().freeMemory());
	}

	public String getMaxMemory() {
		return new DecimalFormat("#,###,###,###").format(Runtime.getRuntime().maxMemory());
	}
}
