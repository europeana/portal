package eu.europeana.portal2.web.presentation.model;

import java.util.List;
import java.util.logging.Logger;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.portal2.web.util.QueryUtil;

/**
 * This class is used to compute result navigation to be inserted in the
 * Freemarker Model
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public class ResultPaginationImpl implements ResultPagination {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static final String FACET_PROMPT = "&qf=";
	private boolean isPrevious;
	private int previousPage;
	private boolean isFirst;
	private boolean isLast;
	private boolean isNext;
	private int firstPage;
	private int lastPage;
	private int nextPage;
	private int numberOfPages;
	private int pageNumber;
	private int start;
	private int rows;
	private int numFound;
	private List<BreadCrumb> breadcrumbs;
	private PresentationQueryImpl presentationQuery = new PresentationQueryImpl();
	
	
	public ResultPaginationImpl(int start, int rows, int numFound, Query query, List<BreadCrumb> breadcrumbs) {
		this.numFound	= numFound;
		this.rows		= rows;
		
		int totalPages = numFound / rows;
		lastPage		=    (  (numFound / rows) * rows ) +  ( numFound % rows > 0 ? 1 : (0-rows)+1 );
		firstPage		= 1;

		if (numFound % rows != 0) {
			totalPages++;
		}
		this.numberOfPages = totalPages;

		this.start = 1;
		if (start != 0) {
			this.start = start;
		}
		pageNumber = start / rows + 1;
		
		this.isPrevious		= start > 1;
		this.previousPage	= start - rows;
		this.isFirst		= pageNumber == 1;
		this.isLast			= pageNumber == totalPages;
		this.isNext			= pageNumber < totalPages;
		this.nextPage		= start + rows;
		this.breadcrumbs	= breadcrumbs;

		presentationQuery.queryForPresentation = createQueryForPresentation(query);
		presentationQuery.queryToSave = query.getQuery();
		presentationQuery.userSubmittedQuery = query.getQuery();
		presentationQuery.typeQuery = removePresentationFilters(query.getQuery());
	}

	private static String removePresentationFilters(String requestQueryString) {
		String[] filterQueries = requestQueryString.split("&");
		StringBuilder url = new StringBuilder();
		for (String filterQuery : filterQueries) {
			if (filterQuery.startsWith("qf=TYPE:")) {
				continue;
			}
			if (filterQuery.startsWith("tab=")) {
				continue;
			}
			if (filterQuery.startsWith("view=")) {
				continue;
			}
			if (filterQuery.startsWith("start=")) {
				continue; // start page must be reset to eliminate paging errors
			}
			url.append(filterQuery).append("&");
		}
		String urlString = url.toString().trim();
		if (urlString.endsWith("&")) {
			urlString = urlString.substring(0, urlString.length() - 1);
		}
		return urlString;
	}

	private static String createQueryForPresentation(Query query) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("query").append("=").append(QueryUtil.encode(query.getQuery()));
		String[] facetQueries = query.getRefinements();
		if (facetQueries != null) {
			for (String facetTerm : facetQueries) {
				queryString.append(FACET_PROMPT).append(facetTerm);
			}
		}
		return queryString.toString();
	}

	@Override
	public int getLastPage() {
		return lastPage;
	}

	@Override
	public int getFirstPage() {
		return firstPage;
	}

	@Override
	public boolean isPrevious() {
		return isPrevious;
	}

	@Override
	public int getPreviousPage() {
		return previousPage;
	}

	@Override
	public boolean isFirst() {
		return isFirst;
	}

	@Override
	public boolean isLast() {
		return isLast;
	}

	@Override
	public boolean isNext() {
		return isNext;
	}

	@Override
	public int getNextPage() {
		return nextPage;
	}

	@Override
	public int getLastViewableRecord() {
		return Math.min(nextPage - 1, numFound);
	}

	@Override
	public int getNumFound() {
		return numFound;
	}

	@Override
	public int getNumberOfPages() {
		return numberOfPages;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public int getRows() {
		return this.rows;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public List<BreadCrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	@Override
	public PresentationQuery getPresentationQuery() {
		return presentationQuery;
	}

	private class PresentationQueryImpl implements PresentationQuery {
		private String userSubmittedQuery;
		private String queryForPresentation;
		private String queryToSave;
		private String typeQuery;

		@Override
		public String getUserSubmittedQuery() {
			return userSubmittedQuery;
		}

		@Override
		public String getQueryForPresentation() {
			return queryForPresentation;
		}

		@Override
		public String getQueryToSave() {
			return queryToSave;
		}

		@Override
		public String getTypeQuery() {
			return typeQuery;
		}
	}
}