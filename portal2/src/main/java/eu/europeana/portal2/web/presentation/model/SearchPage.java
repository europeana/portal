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

package eu.europeana.portal2.web.presentation.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.corelib.web.utils.UrlBuilder;
import eu.europeana.portal2.web.presentation.PortalLanguage;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.preparation.SearchPreparation;

public class SearchPage extends SearchPreparation {

	@Override
	public boolean isIndexable() {
		return false;
	}

	/**
	 * Formats any url adding in any required addition parameters required for the brief view page.
	 * 
	 * @param url
	 *            - Url to be formatted
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public UrlBuilder getPortalFormattedUrl(UrlBuilder url) {
		// remove default values to clean up url...
		url.removeDefault("view", "table");
		// url.removeDefault("start", "1");
		// url.removeDefault("startPage", "1");
		url.removeDefault("embedded", "false");

		return url;
	}

	@Override
	public UrlBuilder enrichFullDocUrl(UrlBuilder builder) throws UnsupportedEncodingException {
		if (getBriefBeanView() != null) {
			builder.addParamsFromURL(getBriefBeanView().getPagination().getPresentationQuery()
					.getQueryForPresentation());
			builder.addParam("startPage", getBriefBeanView().getPagination().getStart(), true);
		}
		builder = getPortalFormattedUrl(builder);
		return builder;
	}

	@Override
	public String getPageTitle() {
		if (StringUtils.isNotBlank(getQuery())) {
			StringBuilder sb = new StringBuilder();
			sb.append(getQuery());
			sb.append(" - ");
			sb.append(super.getPageTitle());
			return sb.toString();
		}
		return super.getPageTitle();
	}

	/**
	 * Generates the url for the map kml link to show the results on the map
	 * 
	 * @param portalName
	 *            - name of the portal
	 * @return - Mapview url
	 */
	public String getMapKmlUrl() throws UnsupportedEncodingException {
		return StringUtils.replace(getPortalFormattedUrl(getViewUrl(PortalPageInfo.SEARCH_KML.getPageName()))
				.toString(), "&amp;", "&");
	}

	/**
	 * Generates the url for the map json link to show the results on the map
	 * 
	 * @param portalName
	 *            - name of the portal
	 * @return - Mapview url
	 */
	public String getMapJsonUrl() throws UnsupportedEncodingException {
		UrlBuilder url = getPortalFormattedUrl(getViewUrl(PortalPageInfo.MAP_JSON.getPageName()));
		if (coords != null) {
			url.addParam("coords", coords, true);
		}
		return StringUtils.replace(url.toString(), "&amp;", "&");
	}

	/**
	 * Returns the URL to navigate back to the previous page of results
	 * 
	 * @param viewType
	 *            - How to display the results
	 * @return - The URL to navigate back to the previous page of results
	 * @throws UnsupportedEncodingException
	 */
	public String getNextPageUrl() throws UnsupportedEncodingException {
		return createNavigationUrl(briefBeanView.getPagination().getNextPage());
	}

	/**
	 * Returns the URL to go back to the previous page of results
	 * 
	 * @param viewType
	 *            - What format the view should be displayed in
	 * @return - URL to go back to previous page of results
	 * @throws UnsupportedEncodingException
	 */
	public String getPreviousPageUrl() throws UnsupportedEncodingException {
		return createNavigationUrl(briefBeanView.getPagination().getPreviousPage());
	}

	/**
	 * Returns the URL to navigate to the first page of results
	 * 
	 * @param viewType
	 *            - How to display the results
	 * @return - The URL to navigate back to the previous page of results
	 * @throws UnsupportedEncodingException
	 */
	public String getFirstPageUrl() throws UnsupportedEncodingException {
		return createNavigationUrl(briefBeanView.getPagination().getFirstPage());
	}

	/**
	 * Returns the URL to navigate to the last page of results
	 * 
	 * @param viewType
	 *            - How to display the results
	 * @return - The URL to navigate back to the previous page of results
	 * @throws UnsupportedEncodingException
	 */
	public String getLastPageUrl() throws UnsupportedEncodingException {
		// return createNavigationUrl(briefBeanView.getPagination().getLastPage() + 1);
		return createNavigationUrl(briefBeanView.getPagination().getLastPage());
	}

	/**
	 * Creates the navigation URL (first, last, prev, next)
	 * 
	 * @param start
	 *            The start parameter
	 * @return The URL of the page
	 * @throws UnsupportedEncodingException
	 */
	private String createNavigationUrl(int start) throws UnsupportedEncodingException {
		if (briefBeanView == null) {
			return null;
		}
		UrlBuilder builder = createSearchUrl(getQuery(), getRefinements(), Integer.toString(start));
		builder.addParam("rows", getRows(), true);
		builder.addParamsFromURL(briefBeanView.getPagination().getPresentationQuery().getQueryForPresentation(),
				"query", "qf", "start", "rows");
		return getPortalFormattedUrl(builder).toString();
	}

	public int getNumberOfPages() {
		if (briefBeanView == null) {
			return 0;
		}
		return briefBeanView.getPagination().getNumberOfPages();
	}

	public int getPageNumber() {
		if (briefBeanView == null) {
			return 0;
		}
		return briefBeanView.getPagination().getPageNumber();
	}

	public List<String> getProvidersForInclusion() {
		List<String> providersForInclusion = new ArrayList<String>();
		if (getRefinements() != null) {
			for (String provider : getRefinements()) {
				if (provider.contains("PROVIDER:")) {
					providersForInclusion.add(provider);
				}
			}
		}
		return providersForInclusion;
	}

	/**
	 * Returns the url used by the refine search box
	 * 
	 * @param portalName
	 * @return
	 * @throws Exception
	 */
	public String getRefineSearchUrl() throws Exception {
		StringBuilder url = new StringBuilder();
		url.append("/").append(getPageName()).append("/search.html");
		UrlBuilder builder = new UrlBuilder(url.toString());
		return getPortalFormattedUrl(builder).toString();
	}

	/**
	 * Generates the url for the mapview link to show the results on the map
	 * 
	 * @param portalName
	 *            - name of the portal
	 * @return - Mapview url
	 */
	public String getViewUrlMap() throws UnsupportedEncodingException {
		return getPortalFormattedUrl(getViewUrl(PortalPageInfo.MAP.getPageName())).toString();
	}

	public String getViewUrlTable() throws UnsupportedEncodingException {
		return getPortalFormattedUrl(getViewUrl(PortalPageInfo.SEARCH_HTML.getPageName())).toString();
	}

	public String getJsonUrlMap() throws UnsupportedEncodingException {
		return StringUtils.replace(getPortalFormattedUrl(getViewUrl(PortalPageInfo.MAP_JSON.getPageName())).toString(),
				"&amp;", "&");
	}

	public String getQueryForPresentation() {
		if (briefBeanView == null) {
			return null;
		}
		return briefBeanView.getPagination().getPresentationQuery().getQueryForPresentation();
	}

	/**
	 * Generates the url for the timeline link to show the results on the timeline
	 * 
	 * @param portalName
	 *            - name of the portal
	 * @return - Timeline url
	 */
	public String getViewUrlTimeline() throws UnsupportedEncodingException {
		return getPortalFormattedUrl(getViewUrl(PortalPageInfo.TIMELINE.getPageName())).toString();
	}

	public String getJsonUrlTimeline() throws UnsupportedEncodingException {
		return StringUtils.replace(getPortalFormattedUrl(getViewUrl(PortalPageInfo.TIMELINE_JSON.getPageName()))
				.toString(), "&amp;", "&");
	}

	public boolean isShowTimeLine() {
		boolean startsWithEuropeanaUri = true;
		if (getQuery().length() >= 14) {
			startsWithEuropeanaUri = !getQuery().substring(0, 14).equals("europeana_uri:");
		}
		return startsWithEuropeanaUri;
	}

	@Override
	public boolean isShowDidYouMean() {
		if ((getBriefBeanView().getPagination() != null) && (getBriefBeanView().getPagination().getNumFound() > 0)) {
			return false;
		}
		if ((getBriefBeanView().getSpellCheck() != null) && (getBriefBeanView().getSpellCheck().suggestions != null)
				&& !getBriefBeanView().getSpellCheck().correctlySpelled) {
			return true;
		}
		return false;
	}

	@Override
	public String[] getRefinements() {
		String qf[] = super.getRefinements();
		// add refinement keyword to refinement list if needed
		if (briefBeanView == null && StringUtils.isNotBlank(getRefineKeyword())) {
			String refine = getRefineKeyword();
			String cleanedRefine = refine; // QueryAnalyzer.sanitize(refine);
			refine = StringUtils.contains(refine, ":") ? cleanedRefine
					: (StringUtils.startsWith(refine, "-") ? "-text:" : "text:") + cleanedRefine;
			qf = (String[]) ArrayUtils.add(qf, refine);
		}
		return qf;
	}

	/**
	 * Generates a url to see the results shown in a specific view. That is a url to determine how the results should be
	 * displayed
	 * 
	 * @param portalName
	 *            - Name of the portal
	 * @param pageName
	 *            - Name of the page
	 * @param viewType
	 *            - Which view to use to show the results
	 * @return - Url to show results in specified view
	 */
	private UrlBuilder getViewUrl(String pageName) throws UnsupportedEncodingException {
		String queryForPresentation = null;
		if (briefBeanView != null) {
			queryForPresentation = briefBeanView.getPagination().getPresentationQuery().getQueryForPresentation();
		} else {
			if (StringUtils.isNotBlank(getQuery())) {
				queryForPresentation = "query=" + URLEncoder.encode(getQuery(), "utf8");
			}
		}
		StringBuilder url = new StringBuilder();
		url.append("/").append(getPortalName()).append("/").append(pageName).append("?").append(queryForPresentation);

		UrlBuilder builder = new UrlBuilder(url.toString());
		builder.addParam("start", getStart(), true);
		builder.addParam("startPage", getStartPage(), true);
		if (briefBeanView == null && getRefinements() != null) {
			// add refinements back if there is no BriefBeanView available.
			for (String _qf : getRefinements()) {
				builder.addMultiParam("qf", URLEncoder.encode(_qf, "utf8"));
			}
		}
		return builder;
	}

	@Override
	public UrlBuilder createSearchUrl(String searchTerm, String[] qf, String start) throws UnsupportedEncodingException {
		return createSearchUrl(getPortalName(), SearchPageEnum.SEARCH_HTML, searchTerm, qf, start);
	}

	public static UrlBuilder createSearchUrl(String portalname, SearchPageEnum returnTo, String searchTerm,
			String[] qf, String start) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("/").append(portalname).append("/").append(returnTo.getPageInfo().getPageName());
		UrlBuilder url = new UrlBuilder(sb.toString());
		url.addParam("query", searchTerm, true);
		if (qf != null) {
			for (String _qf : qf) {
				url.addMultiParam("qf", URLEncoder.encode(_qf, "UTF-8"));
			}
		}
		url.addParam("start", start, true);
		return url;
	}

	public boolean isUGCFilter() {
		if (getRefinements() != null) {
			for (String qf : getRefinements()) {
				if (StringUtils.equalsIgnoreCase(qf, Configuration.FACET_UCG_FILTER)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getUGCUrl() {
		UrlBuilder url = new UrlBuilder(getCurrentUrl());
		if (isUGCFilter()) {
			url.removeDefault("qf", Configuration.FACET_UCG_FILTER);
		} else {
			url.addMultiParam("qf", Configuration.FACET_UCG_FILTER);
		}
		return url.toString();
	}

	@Override
	public String getImageLocale() {
		PortalLanguage current = PortalLanguage.safeValueOf(getLocale());
		if (!current.hasImageSupport()) {
			current = PortalLanguage.EN;
		}
		return current.getLanguageCode();
	}

}
