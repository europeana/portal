package eu.europeana.portal2.web.presentation.model.submodel.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.IdBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.web.presentation.model.submodel.DocIdWindow;
import eu.europeana.portal2.web.presentation.model.submodel.DocIdWindowPager;
import eu.europeana.portal2.web.util.QueryUtil;

/**
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 */

@SuppressWarnings("serial")
public class DocIdWindowPagerImpl implements DocIdWindowPager, Serializable {

	private DocIdWindow docIdWindow;
	private boolean hasNext;
	private String nextUri;
	private int nextInt;
	private boolean hasPrevious;
	private String previousUri;
	private int previousInt;
	private String fullDocUri;
	private String queryStringForPaging;
	private String startPage;
	private String query;
	private String returnToResults;
	private String nextFullDocUrl;
	private String previousFullDocUrl;
	private String pageId;
	// private String siwa;
	private List<BreadCrumb> breadcrumbs;
	private int fullDocUriInt;

	public static DocIdWindowPager fetchPager(
			Map<String, String[]> httpParameters, 
			Query query,
			SearchService searchService, 
			Class<? extends BriefBean> clazz) throws SolrTypeException {

		DocIdWindowPagerImpl pager = new DocIdWindowPagerImpl();
		pager.query = query.getQuery();
		int fullDocUriInt = getFullDocInt(httpParameters, query, pager);

		// negative parameters should return null so no pager is rendered
		if (fullDocUriInt < 1 || Integer.parseInt(fetchParameter(httpParameters, "startPage", "1")) < 1
				|| StringUtils.isBlank(query.getQuery())) {

			return null;
		}
		int solrStartRow = getSolrStart(pager, fullDocUriInt);
		// query.setAllowSpellcheck(false);
		// query.setAllowFacets(false);
		ResultSet<? extends BriefBean> queryResponse = getQueryResponse(query, searchService, solrStartRow,
				clazz);

		// if no results are found return null to signify that docIdPage can be created.
		if (queryResponse.getResults() == null) {
			return null;
		}

		List<? extends IdBean> list = queryResponse.getResults(); // <? extends IdBean>
		int numFound = (int) queryResponse.getResultSize();
		setNextAndPrevious(pager, fullDocUriInt, list, solrStartRow, numFound);
		pager.docIdWindow = new DocIdWindowImpl(list, solrStartRow, numFound);
		if (pager.hasNext) {
			pager.setNextFullDocUrl(httpParameters);
		}
		if (pager.hasPrevious) {
			pager.setPreviousFullDocUrl(httpParameters);
		}
		pager.fullDocUriInt = fullDocUriInt;
		return pager;
	}

	static int getFullDocInt(Map<String, String[]> httpParameters, Query query, DocIdWindowPagerImpl pager) {

		pager.fullDocUri = fetchParameter(httpParameters, "start", "1");
		if (pager.fullDocUri.isEmpty()) {
			// TODO: a better exception
			throw new IllegalArgumentException("Expected 'uri' parameter");
		}
		pager.startPage = fetchParameter(httpParameters, "startPage", "1");
		pager.pageId = fetchParameter(httpParameters, "pageId", "");
		// pager.siwa = fetchParameter(httpParameters, "siwa", "");
		if (pager.pageId != null) {
			pager.setReturnToResults(httpParameters);
		}
		int fullDocUriInt = 0;
		if (!pager.startPage.isEmpty()) {
			fullDocUriInt = Integer.parseInt(fetchParameter(httpParameters, "start", "1"));
			pager.setQueryStringForPaging(query, pager.startPage);
		}
		return fullDocUriInt;
	}

	private static int getSolrStart(DocIdWindowPagerImpl pager, int fullDocUriInt) {
		int solrStartRow = fullDocUriInt;
		pager.hasPrevious = fullDocUriInt > 1;
		if (fullDocUriInt > 1) {
			solrStartRow -= 2;
		} else {
			solrStartRow -= 1;
		}
		return solrStartRow;
	}

	private static void setNextAndPrevious(DocIdWindowPagerImpl pager, int fullDocUriInt, List<? extends IdBean> list,
			int offset, int numFound) {

		if (offset + 2 < numFound) {
			pager.hasNext = true;
		} else if (fullDocUriInt == 1 && list.size() == 2) {
			pager.hasNext = true;
		}

		if (fullDocUriInt > numFound || list.size() < 2) {
			pager.hasPrevious = false;
			pager.hasNext = false;
		}

		if (pager.hasPrevious) {
			pager.previousInt = fullDocUriInt - 1;
			pager.previousUri = "/record" + list.get(0).getId();
		}

		if (pager.hasNext) {
			pager.nextInt = fullDocUriInt + 1;
			if (pager.hasPrevious) {
				pager.nextUri = "/record" + list.get(2).getId();
			} else {
				pager.nextUri = "/record" + list.get(1).getId();
			}
		}
	}

	/**
	 * This method queries the SolrSearch engine to get a QueryResponse with 3 DocIds
	 * <p/>
	 * This method does not have to be Unit Tested
	 * 
	 * @param originalBriefSolrQuery
	 * @param solrServer
	 * @param pager
	 * @param solrStartRow
	 * 
	 * @return
	 * @throws SolrTypeException
	 * 
	 * @throws EuropeanaQueryException
	 * @throws SolrServerException
	 */
	private static ResultSet<? extends BriefBean> getQueryResponse(Query query, SearchService searchService,
			int start, Class<? extends BriefBean> clazz) throws SolrTypeException {

		// query.setParameter("fl", "europeana_uri");
		query.setStart(start);
		query.setPageSize(3);

		/*
		 * pager.breadcrumbs = BreadCrumb.createList(originalBriefSolrQuery); return
		 * solrServer.query(originalBriefSolrQuery);
		 */
		return searchService.search(clazz, query);
	}

	private void setReturnToResults(Map<String, String[]> httpParameters) {
		StringBuilder out = new StringBuilder();
		if (pageId.equalsIgnoreCase("brd")) {
			// out.append(MessageFormat.format("/{0}/search.html?", portalName));
			out.append("/search.html?");
			out.append("query=").append(encode(query));
			final String[] filterQueries = httpParameters.get("qf");
			if (filterQueries != null) {
				for (String filterQuery : filterQueries) {
					out.append("&qf=").append(filterQuery);
				}
			}
		} else if (pageId.equalsIgnoreCase("yg")) {
			// out.append(MessageFormat.format("/{0}/year-grid.html?", portalName));
			out.append("/year-grid.html?");
			if (query.length() > 4) {
				String userQueryString = query.replaceFirst("^\\d{4}", "").trim();
				out.append("query=").append(encode(userQueryString)).append("&");
			}
			out.append("bq=").append(encode(query));
		} else if (pageId.equalsIgnoreCase("tlv")) {
			// out.append(MessageFormat.format("/{0}/timeline.html?", portalName));
			out.append("/timeline.html?");
			out.append("query=").append(encode(query));
			// todo enable later when facets are also supported
			// final String[] filterQueries = httpParameters.get("qf");
			// if (filterQueries != null) {
			// for (String filterQuery : filterQueries) {
			// out.append("&qf=").append(filterQuery);
			// }
			// }
		}
		out.append("&start=").append(startPage);
		// if (!siwa.isEmpty()) {
		// out.append("&siwa=").append(siwa);
		// }
		out.append("&rtr=true");
		returnToResults = out.toString();
	}

	private void setNextFullDocUrl(Map<String, String[]> httpParameters) {
		StringBuilder out = new StringBuilder();
		out.append(MessageFormat.format("{0}.html?", nextUri.replace("http://www.europeana.eu/resolve", "")));
		out.append("query=").append(encode(query));
		final String[] filterQueries = httpParameters.get("qf");
		if (filterQueries != null) {
			for (String filterQuery : filterQueries) {
				out.append("&qf=").append(filterQuery);
			}
		}
		out.append("&start=").append(nextInt);
		out.append("&startPage=").append(startPage);
		out.append("&pageId=").append(pageId);
		// if (!siwa.isEmpty()) {
		// out.append("&siwa=").append(siwa);
		// }
		nextFullDocUrl = out.toString();
	}

	private void setPreviousFullDocUrl(Map<String, String[]> httpParameters) {
		StringBuilder out = new StringBuilder();
		out.append(MessageFormat.format("{0}.html?", previousUri.replace("http://www.europeana.eu/resolve", "")));
		out.append("query=").append(encode(query));
		final String[] filterQueries = httpParameters.get("qf");
		if (filterQueries != null) {
			for (String filterQuery : filterQueries) {
				out.append("&qf=").append(filterQuery);
			}
		}
		out.append("&start=").append(previousInt);
		out.append("&startPage=").append(startPage);
		out.append("&pageId=").append(pageId);
		previousFullDocUrl = out.toString();
	}

	// TODO: it filters out the normal refinements and rows as well!!!
	private void setQueryStringForPaging(Query solrQuery, String startPage) {
		if (solrQuery.getQuery() == null) {
			return;
		}
		StringBuilder out = new StringBuilder();
		out.append("query=").append(encode(solrQuery.getQuery()));
		final List<String> facetQueries = QueryUtil.getFilterQueriesWithoutPhrases(solrQuery).get(QueryUtil.FACETS);
		if (facetQueries != null) {
			for (String facetTerm : facetQueries) {
				out.append("&qf=").append(facetTerm);
			}
		}
		out.append("&startPage=").append(startPage);
		this.queryStringForPaging = out.toString();
	}

	private static String fetchParameter(Map<String, String[]> httpParameters, String key, String defaultValue) {
		String[] array = httpParameters.get(key);

		if (array == null || array.length == 0) {
			return defaultValue;
		} else {
			return array[0];
		}
	}

	private static String encode(String string) {
		if (StringUtils.isBlank(string)) {
			return string;
		}
		try {
			return URLEncoder.encode(string, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	DocIdWindowPagerImpl() {
	}

	@Override
	public DocIdWindow getDocIdWindow() {
		return docIdWindow;
	}

	@Override
	public String getStartPage() {
		return startPage;
	}

	@Override
	public List<BreadCrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	@Override
	public boolean isNext() {
		return hasNext;
	}

	@Override
	public boolean isPrevious() {
		return hasPrevious;
	}

	@Override
	public String getQueryStringForPaging() {
		return queryStringForPaging;
	}

	@Override
	public String getFullDocUri() {
		return fullDocUri;
	}

	@Override
	public String getNextFullDocUrl() {
		return nextFullDocUrl;
	}

	@Override
	public String getPreviousFullDocUrl() {
		return previousFullDocUrl;
	}

	@Override
	public String getNextUri() {
		return nextUri;
	}

	@Override
	public int getNextInt() {
		return nextInt;
	}

	@Override
	public String getPreviousUri() {
		return previousUri;
	}

	@Override
	public int getPreviousInt() {
		return previousInt;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public String getReturnToResults() {
		return returnToResults;
	}

	@Override
	public String getPageId() {
		return pageId;
	}

	// todo fix this it throws an nullPointerException now
	@Override
	public String toString() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("query", query);
		map.put("queryStringForPaging", queryStringForPaging);
		map.put("fullDocUri", fullDocUri);
		map.put("fullDocStart", String.valueOf(docIdWindow.getOffset()));
		map.put("hitCount", String.valueOf(docIdWindow.getHitCount()));
		map.put("isPrevious", String.valueOf(hasPrevious));
		map.put("previousInt", String.valueOf(previousInt));
		map.put("previousUri", String.valueOf(previousUri));
		map.put("isNext", String.valueOf(hasNext));
		map.put("nextInt", String.valueOf(nextInt));
		map.put("nextUri", String.valueOf(nextUri));
		map.put("returnToResults", returnToResults);
		StringBuilder out = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			out.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
		}
		return out.toString();
	}

	@Override
	public int getFullDocUriInt() {
		return fullDocUriInt;
	}

	private static class DocIdWindowImpl implements DocIdWindow {

		private List<? extends IdBean> docIds;
		private int offset;
		private int hitCount;

		private DocIdWindowImpl(List<? extends IdBean> docIds, int offset, int hitCount) {
			this.docIds = docIds;
			this.offset = offset;
			this.hitCount = hitCount;
		}

		@Override
		public List<? extends IdBean> getIds() {
			return docIds;
		}

		@Override
		public Integer getOffset() {
			return offset;
		}

		@Override
		public Integer getHitCount() {
			return hitCount;
		}
	}
}