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

import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.model.QueryTranslation;
import eu.europeana.corelib.logging.Logger;
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

	Logger log = Logger.getLogger(SearchPageData.class.getCanonicalName());

	/**
	 * The Europeana URL service is responsible for creation of portal URLs
	 */
	public EuropeanaUrlService europeanaUrlService = EuropeanaUrlServiceImpl.getBeanInstance();

	/**
	 * The translation service that makes use of Microsoft Translation API
	 */
	public MicrosoftTranslatorService translationService = MicrosoftTranslatorServiceImpl.getBeanInstance();

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

	private LanguageContainer languageContainer;

	private String apiUrl;

	private boolean useBackendItemTranslation = false;

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
	 * Get the list of supported languages
	 */
	public List<PortalLanguage> getPortalLanguages() {
		return PortalLanguage.getSupported();
	}

	/**
	 * Get the list of all languages
	 */
	public List<PortalLanguage> getAllPortalLanguages() {
		return Arrays.asList(PortalLanguage.values());
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

	public List<LanguageVersion> getQueryLanguageVersions() {
		if (languageContainer != null) {
			return languageContainer.getQueryLanguageVersions();
		} else {
			return null;
		}
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
		if (getQueryTranslation() != null) {
			List<String> params = QueryUtil.getQueryTranslationParams(getQueryTranslation().getLanguageVersionMap());
			return params.toArray(new String[params.size()]);
		} else {
			return null;
		}
	}

	private boolean shouldSkip(String code, List<LanguageVersion> texts, LanguageVersion englishEntry, LanguageVersion query){
		boolean result = false;
		if (!code.equalsIgnoreCase("EN")
			&& texts.size() > 1
			&& englishEntry != null
			&& query.getText().equalsIgnoreCase(englishEntry.getText())) {
			result = true;
		}
		return result;
	}

	public List<LanguageVersionLink> getQueryTranslationLinks() {
		List<LanguageVersionLink> links = new ArrayList<LanguageVersionLink>();
		QueryTranslation queryTranslation = getQueryTranslation();
		if (queryTranslation == null) {
			return links;
		}
		Map<String, List<LanguageVersion>> languageVersionMap = getQueryTranslation().getLanguageVersionMap();

		if (languageVersionMap == null || languageVersionMap.keySet().size() == 0) {
			log.warn("languageVersionMap is null or empty");
			return links;
		}
		for (String position : languageVersionMap.keySet()) {
			List<LanguageVersion> queryTranslationsList = languageVersionMap.get(position);

			if (queryTranslationsList == null || queryTranslationsList.size() == 0) {
				log.warn("queryTranslationsList is null or empty");
				continue;
			}
			// get the English / group multiple entries
			LanguageVersion english = null;
			Map<String, List<LanguageVersion>> textsByCode = new HashMap<String, List<LanguageVersion>>();

			for (LanguageVersion query : queryTranslationsList) {
				String code = query.getLanguageCode();
				if (code.equalsIgnoreCase("EN")) {
					english = query;
				}
				if (textsByCode.get(code) == null) {
					textsByCode.put(code, new Vector<LanguageVersion>());
				}
				textsByCode.get(code).add(query);
			}

			try {
				for (LanguageVersion query : queryTranslationsList) {
					String code = query.getLanguageCode();
					// Add only if the has multiple
					List <LanguageVersion> textsInThisLang = textsByCode.get(code);
					if (!shouldSkip(code, textsInThisLang, english, query)) {
						String queryLink = createLanguageQueryLink(query.getText());
						UrlBuilder removeLink = getBaseSearchUrl();
						boolean doAdd = false;
						String param;

						if (queryTranslationsList.size() == 1) {
							removeLink.addMultiParam("qt", "false");
							doAdd = true;
						} else {
							for (LanguageVersion other : queryTranslationsList) {
								if (!other.equals(query)) {
									if (!shouldSkip(other.getLanguageCode(), textsByCode.get(other.getLanguageCode()), english, other)) {
										param = String.format("%s:%s:%s", position, other.getLanguageCode(), other.getText());
										removeLink.addMultiParam("qt", param);
										doAdd = true;
									}
								}
							}
						}
						if (doAdd) {
							links.add(new LanguageVersionLink(query, queryLink, removeLink.toString()));
						}
					}
				}
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return links;
	}

	private UrlBuilder getBaseSearchUrl()
			throws UnsupportedEncodingException {
		UrlBuilder url = europeanaUrlService.getPortalSearch(true, getQuery(), String.valueOf(getRows()));
		url.addParam("qf", getRefinements());
		return url;
	}

	private String createLanguageQueryLink(String query)
			throws UnsupportedEncodingException {
		query = QueryUtil.createPhraseValue("text", query);
		UrlBuilder url = europeanaUrlService.getPortalSearch(true, query, String.valueOf(getRows()));
		url.addParam("qf", getRefinements());
		url.addMultiParam("qt", "false");
		return url.toString();
	}

	public void setLanguages(LanguageContainer languageContainer) {
		this.languageContainer = languageContainer;
	}

	public List<String> getKeywordLanguages() {
		return languageContainer.getKeywordLanguages();
	}

	public String getPortalLanguage() {
		return languageContainer.getPortalLanguage();
	}

	public String getItemLanguage() {
		return languageContainer.getItemLanguage();
	}

	public QueryTranslation getQueryTranslation() {
		if (languageContainer != null) {
			return languageContainer.getQueryTranslation();
		}
		return null;
	}

	public boolean isUseBackendItemTranslation() {
		return useBackendItemTranslation;
	}

	public void setUseBackendItemTranslation(boolean useBackendItemTranslation) {
		this.useBackendItemTranslation = useBackendItemTranslation;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

}
