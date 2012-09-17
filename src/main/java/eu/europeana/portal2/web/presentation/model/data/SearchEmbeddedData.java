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

package eu.europeana.portal2.web.presentation.model.data;

import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import eu.europeana.portal2.web.presentation.Configuration;

public abstract class SearchEmbeddedData extends SearchData {

	private final Logger log = Logger.getLogger(getClass().getName());

	private String embeddedLogo = "poweredbyeuropeanaBlack.png";
	private String embeddedLang = null;
	protected String embeddedBgColor = Configuration.EMBEDDED_BGCOLOR;
	protected String embeddedForeColor = Configuration.EMBEDDED_FORECOLOR;
	private String[] tabsToBeShown = null;
	private String rswDefqry = "*:*";
	private String rswUserId = "unknown";

	public void setEmbedded(String embedded) {
		setEmbedded(StringUtils.equalsIgnoreCase(embedded, "true"));
	}

	public void setEmbeddedLogo(String embeddedLogo) {
		if (StringUtils.isNotBlank(embeddedLogo)) {
			this.embeddedLogo = embeddedLogo;
		}
	}

	public String getEmbeddedLogo() {
		return embeddedLogo;
	}

	public String getEmbeddedBgColor() {
		return embeddedBgColor;
	}

	public String getEmbeddedForeColor() {
		return embeddedForeColor;
	}

	public void setTabsToBeShown(String[] tabsToBeShown) {
		this.tabsToBeShown = tabsToBeShown;
	}

	public String[] getTabsToBeShown() {
		return tabsToBeShown;
	}

	public void setEmbeddedLang(String embeddedLang) {
		this.embeddedLang = embeddedLang;
	}

	public String getEmbeddedLang() {
		if (StringUtils.isBlank(embeddedLang)) {
			return getLocale().toString();
		}
		return embeddedLang;
	}

	public void setRswUserId(String rswUserId) {
		if (StringUtils.isNotBlank(rswUserId)) {
			this.rswUserId = rswUserId;
		}
	}

	public String getRswUserId() {
		return rswUserId;
	}

	public void setRswDefqry(String rswDefqry) {
		if (StringUtils.isNotBlank(rswDefqry)) {
			this.rswDefqry = rswDefqry;
		}
	}

	public String getRswDefqry() {
		return rswDefqry;
	}

}
