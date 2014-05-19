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

package eu.europeana.portal2.web.presentation.model.abstracts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.utils.model.LanguageVersion;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.service.MicrosoftTranslatorService;
import eu.europeana.corelib.web.service.impl.EuropeanaUrlServiceImpl;
import eu.europeana.corelib.web.service.impl.MicrosoftTranslatorServiceImpl;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.PortalLanguage;
import eu.europeana.portal2.web.presentation.model.PortalPageData;
import eu.europeana.portal2.web.presentation.model.data.decorators.LanguageVersionLink;
import eu.europeana.portal2.web.presentation.model.submodel.LanguageContainer;
import eu.europeana.portal2.web.util.QueryUtil;

/**
 * Abstract model for all pages containing a searchform...
 * 
 * @author GJW Boogerd
 */
public abstract class SearchPageData extends PortalPageData {

	public EuropeanaUrlService europeanaUrlservice = EuropeanaUrlServiceImpl.getBeanInstance();
	public MicrosoftTranslatorService translationUrlservice = MicrosoftTranslatorServiceImpl.getBeanInstance();

	private String query;

	private boolean enableRefinedSearch = false;

	private String[] refinements;

	/**
	 * The first result count
	 */
	private int start = 1;

	/**
	 * Number of results
	 */
	private int rows = 24;

	/**
	 * The Solr sort parameter
	 */
	private String sort;

	/**
	 * The translated version of the query
	 */
	private List<LanguageVersion> queryTranslations;

	private List<String> keywordLanguages;

	private String portalLanguage;

	private String itemLanguage;

	private boolean useJsTranslations = false;

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setEnableRefinedSearch(boolean enableRefinedSearch) {
		this.enableRefinedSearch = enableRefinedSearch;
	}

	public boolean isEnableRefinedSearch() {
		return enableRefinedSearch && !isEmbedded();
	}

	public boolean isEuroeanaUri() {
		return StringUtils.startsWith(query, "europeana_uri:");
	}

	public void setRefinements(String[] refinements) {
		this.refinements = refinements;
	}

	public String[] getRefinements() {
		return ArrayUtils.nullToEmpty(refinements);
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

	public String getImageLocale() {
		PortalLanguage current = PortalLanguage.safeValueOf(getLocale());
		if (!current.hasImageSupport()) {
			current = PortalLanguage.EN;
		}
		return current.getLanguageCode();
	}

	/**
	 * Get the list of available languages
	 */
	public List<PortalLanguage> getPortalLanguages() {
		return PortalLanguage.getSupported();
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public List<LanguageVersion> getQueryTranslations() {
		return queryTranslations;
	}

	public void setQueryTranslations(List<LanguageVersion> queryTranslations) {
		this.queryTranslations = queryTranslations;
	}

	public String getNoTranslationUrl() {
		try {
			UrlBuilder url = getBaseSearchUrl();
			url.addMultiParam("qt", "false");
			return url.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] getQueryTranslationParams() {
		List<String> params = QueryUtil.getQueryTranslationParams(getQueryTranslations());
		return params.toArray(new String[params.size()]);
	}

	public List<LanguageVersionLink> getQueryTranslationLinks() {
		List<LanguageVersionLink> links = new ArrayList<LanguageVersionLink>();
		List<LanguageVersion> queryTranslationsList = getQueryTranslations();
		if (queryTranslationsList != null && queryTranslationsList.size() > 0) {
			try {
				for (LanguageVersion query : queryTranslationsList) {
					String queryLink = createLanguageQueryLink(query.getText());
					UrlBuilder url = getBaseSearchUrl();
					if (queryTranslationsList.size() == 1) {
						url.addMultiParam("qt", "false");
					} else {
						for (LanguageVersion other : queryTranslationsList) {
							if (!other.equals(query)) {
								url.addMultiParam("qt", other.getLanguageCode() + ":" + other.getText());
							}
						}
					}
					links.add(new LanguageVersionLink(query, queryLink, url.toString()));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return links;
	}

	private UrlBuilder getBaseSearchUrl()
			throws UnsupportedEncodingException {
		UrlBuilder url = europeanaUrlservice.getPortalSearch(true, getQuery(), String.valueOf(getRows()));
		url.addParam("qf", getRefinements());
		return url;
	}

	private String createLanguageQueryLink(String query)
			throws UnsupportedEncodingException {
		UrlBuilder url = europeanaUrlservice.getPortalSearch(true, query, String.valueOf(getRows()));
		url.addParam("qf", getRefinements());
		url.addMultiParam("qt", "false");
		return url.toString();
	}

	public void setLanguages(LanguageContainer languageContainer) {
		queryTranslations = languageContainer.getQueryTranslations();
		keywordLanguages = languageContainer.getKeywordLanguages();
		portalLanguage = languageContainer.getPortalLanguage();
		itemLanguage = languageContainer.getItemLanguage();
	}

	public List<String> getKeywordLanguages() {
		return keywordLanguages;
	}

	public void setKeywordLanguages(List<String> keywordLanguages) {
		this.keywordLanguages = keywordLanguages;
	}

	public String getPortalLanguage() {
		return portalLanguage;
	}

	public void setPortalLanguage(String portalLanguage) {
		this.portalLanguage = portalLanguage;
	}

	public String getItemLanguage() {
		return itemLanguage;
	}

	public void setItemLanguage(String itemLanguage) {
		this.itemLanguage = itemLanguage;
	}

	public boolean isUseJsTranslations() {
		return useJsTranslations;
	}

	public void setUseJsTranslations(boolean useJsTranslations) {
		this.useJsTranslations = useJsTranslations;
	}
}
