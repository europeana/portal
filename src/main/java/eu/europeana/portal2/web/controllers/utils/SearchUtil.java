package eu.europeana.portal2.web.controllers.utils;

import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.utils.NavigationUtils;
import eu.europeana.portal2.web.model.ModelUtils;
import eu.europeana.portal2.web.model.SearchResults;
import eu.europeana.portal2.web.presentation.model.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.BriefBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.ResultPagination;
import eu.europeana.portal2.web.presentation.model.ResultPaginationImpl;
import eu.europeana.portal2.web.util.QueryUtil;

public class SearchUtil {

	private static final Logger log = Logger.getLogger(SearchUtil.class.getName());

	@Resource
	private static SearchService searchService;

	private static BriefBeanView createResults(Class<? extends BriefBean> clazz, String profile, Query query, 
			int start, int rows) 
			throws SolrTypeException {
		log.info("createResults");
		BriefBeanViewImpl briefBeanView = new BriefBeanViewImpl();

		SearchResults response = new SearchResults("search.json");
		ResultSet<? extends BriefBean> resultSet = searchService.search(clazz, query);
		log.info("resultSet: " + resultSet);
		resultSet.getQuery();
		response.totalResults = resultSet.getResultSize();
		response.itemsCount = resultSet.getResults().size();
		response.items = resultSet.getResults();
		briefBeanView.setBriefDocs(resultSet.getResults());
		if (StringUtils.containsIgnoreCase(profile, "facets") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.makeQueryLinks(ModelUtils.conventFacetList(resultSet.getFacetFields()), query);
		}
		if (StringUtils.containsIgnoreCase(profile, "breadcrumb") || StringUtils.containsIgnoreCase(profile, "portal")) {
			response.breadCrumbs = NavigationUtils.createBreadCrumbList(QueryUtil.escapeQuery(query));
		}
		if (StringUtils.containsIgnoreCase(profile, "spelling") || StringUtils.containsIgnoreCase(profile, "portal")) {
			briefBeanView.setSpellcheck(ModelUtils.convertSpellCheck(resultSet.getSpellcheck()));
		}
//		if (StringUtils.containsIgnoreCase(profile, "suggestions") || StringUtils.containsIgnoreCase(profile, "portal")) {
//		}
		
		ResultPagination pagination = new ResultPaginationImpl(start, rows, (int)resultSet.getResultSize(), 
				query.getQuery(), query.getQuery(), response.breadCrumbs);
		briefBeanView.setPagination(pagination);
		log.info("end of createResults");
		return briefBeanView;
	}

}
