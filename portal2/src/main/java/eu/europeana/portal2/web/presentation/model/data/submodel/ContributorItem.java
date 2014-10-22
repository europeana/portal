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
import java.util.List;

import org.apache.solr.search.QueryUtils;
import org.springframework.util.StringUtils;

import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.utils.EuropeanaUriUtils;

/**
 * Sitemap index entry, model for MVC.
 */
public class ContributorItem {

	private final Logger log = Logger.getLogger(getClass());

	private final String loc;
	private final String name;
	private final long count;
	private List<DataProviderItem> dataProviders;
	private final String portalServer;

	public ContributorItem(String loc, String name, long count, String portalServer) {
		this.loc = loc;
		this.count = count;
		this.name = name;
		this.portalServer = portalServer;
	}

	public void setDataProviders(List<DataProviderItem> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public List<DataProviderItem> getDataProviders() {
		return dataProviders;
	}

	public boolean isDataProvidersListed() {
		return !dataProviders.isEmpty();
	}

	private String getLocDataProvider(String providerName, String dataProviderName) {
		try {
			StringBuilder pToEncode = new StringBuilder("PROVIDER:\"").append(providerName).append("\"");

			String dp = StringUtils.replace(dataProviderName, " and ", " \\and ");
			StringBuilder dpToEncode = new StringBuilder("DATA_PROVIDER:\"").append(dp).append("\"");

			StringBuilder sb = new StringBuilder(portalServer);
			sb.append("/search.html?query=").append(URLEncoder.encode(dpToEncode.toString(), "UTF-8"));
			sb.append("&qf=").append(URLEncoder.encode(pToEncode.toString(), "UTF-8"));
			return sb.toString();

		} catch (UnsupportedEncodingException e) {
			log.warn(e.getMessage() + " on " + dataProviderName);
		}
		return "";
	}

	public String getLoc() {
		return loc;
	}

	public long getCount() {
		return count;
	}

	public String getName() {
		return name;
	}

	public String getEncodedName() {
		return EuropeanaUriUtils.encode(name);
	}

	public class DataProviderItem {

		private ContributorItem parent;
		private final String name;
		private final long count;

		public DataProviderItem(ContributorItem parent, String name, long count) {
			this.parent = parent;
			this.name = name;
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public String getEncodedName() {
			return EuropeanaUriUtils.encode(name);
		}

		public long getCount() {
			return count;
		}

		public String getLoc() {
			return parent.getLocDataProvider(parent.name, name);
		}
	}
}