package eu.europeana.portal2.web.controllers;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.search.SearchService;
import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.corelib.web.model.rights.RightReusabilityCategorizer;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.corelib.web.utils.RequestUtils;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SearchPage;
import eu.europeana.portal2.web.presentation.model.submodel.BriefBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.LanguageContainer;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.QueryTranslationsUtil;
import eu.europeana.portal2.web.util.SearchUtils;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class SearchController {

    Logger log = Logger.getLogger(this.getClass());

    @Resource
    private SearchService searchService;

    @Resource
    private InternalResourceViewResolver viewResolver;

    @Resource
    private Configuration config;

    @Resource
    private ClickStreamLogService clickStreamLogger;

    @Resource
    private UserService userService;

    @Resource
    private MessageSource messageSource;

    /**
     * Possible sort options
     */
    private final List<String> sortValues = Arrays.asList(new String[] { "score desc", "title desc", "date desc" });

    /**
     * Deafult sort order
     */
    public static final String DEFAULT_SORT = "score desc";

    @RequestMapping({"/search.html", "/brief-doc.html"})
    public ModelAndView searchHtml(
            @RequestParam(value = "query", required = false, defaultValue = "*:*") String queryString,
            @RequestParam(value = "qf", required = false) String[] qf,
            @RequestParam(value = "qt", required = false) String[] qt,
            @RequestParam(value = "qtApplied", required = false) Boolean qtApplied,
            @RequestParam(value = "start", required = false, defaultValue = "1") int start,
            @RequestParam(value = "rows", required = false, defaultValue = "24") int rows,
            @RequestParam(value = "sort", required = false, defaultValue = "") String sort,
            @RequestParam(value = "profile", required = false, defaultValue = "portal") String profile,
            HttpServletRequest request, Locale locale) {

        Map<String, String[]> params = RequestUtils.getParameterMap(request);
        qt = ControllerUtil.fixParameter(qt, "qt", params);

        boolean userSetNoQT = false;

        if(ControllerUtil.getUser(userService) != null){
            User user = ControllerUtil.getUser(userService);
            try{
                if( qtApplied != null){
                    if(user.getLanguageSearchApplied() != qtApplied){
                        user.setLanguageSearchApplied( qtApplied );
                        userService.updateUserLanguageSearchApplied(user.getId(), qtApplied);
                    }
                }
                userSetNoQT = user.getLanguageSearchApplied() != null && !user.getLanguageSearchApplied();
            }
            catch(Exception e){
                log.error("Error updating user: " + e.getMessage());
              //  e.printStackTrace();
                // do nothing
            }
        }
        else{
            if (new QueryTranslationsUtil().getKeywordLanguagesApplied(request) ){
                userSetNoQT = false;
            }
            else{
                userSetNoQT = true;
            }
        }

        SearchPage model = new SearchPage();
        model.setImageUri(config.getImageCacheUrl());
        model.setRequest(request);
        model.setRefinements(ControllerUtil.fixParameter(qf, "qf", params));
        model.setStart(fixStartParameter(start));
        model.setRows(fixRowsParameter(rows));
        model.setDoTranslation(ControllerUtil.getBooleanBundleValue("notranslate_do_translations", messageSource, locale));
        model.setIsNofEnabled(config.isNofEnabled());

        queryString = eu.europeana.corelib.search.utils.SearchUtils.rewriteQueryFields(queryString);
        queryString = eu.europeana.corelib.search.utils.SearchUtils.normalizeBooleans(queryString);
        model.setQuery(queryString);

        if (model.isDoTranslation() && queryString.length() > 0 && !queryString.equals("*:*") &&  !userSetNoQT ) {
            long t0 = new Date().getTime();

            LanguageContainer languageContainer = ControllerUtil.createQueryTranslations(userService, queryString, qt, request);

            long t1 = new Date().getTime();
           // log.info("Query translation took: " + (t1 - t0));
            model.setLanguages(languageContainer);
            //log.info("ItemLanguage: " + model.getItemLanguage());

            if(qt != null && qt.length >0 && qt[0].equals("false") ){
                model.setLanguagesRemoved(true);
            }
        }
        if(userSetNoQT){
            model.setLanguagesRemoved(true);
        }

        if (!sortValues.contains(sort)) {
            sort = DEFAULT_SORT;
        }
        // TODO: the sorting is not solved yet in Solr, so we maintain only one sorting option regardless
        // what the user sets.
        // REMOVE THIS LINE AS SOON AS POSSIBLE, but not earlier ;-)
        sort = DEFAULT_SORT;
        model.setSort(sort);

        PageInfo view = PortalPageInfo.SEARCH_HTML;
        ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, view);

        Query query = new Query(queryString)
                .setRefinements(model.getRefinements())
                .setPageSize(model.getRows())
                .setStart(model.getStart() - 1) // Solr starts from 0
                .setParameter("facet.mincount", "1") // .setParameter("f.YEAR.facet.mincount", "1")
                .setParameter("sort", sort)
                .setProduceFacetUnion(true)
                .setAllowSpellcheck(false)
                .setQueryTranslation(model.getQueryTranslation())
                ;

        if (model.isEmbedded()) {
            query.setAllowFacets(false);
            query.setAllowSpellcheck(false);
        }

        RightReusabilityCategorizer.setPermissionStrategy(RightReusabilityCategorizer.PERMISSION_STRATEGY_NEGATIVE_ALL);
        query.setValueReplacements(RightReusabilityCategorizer.mapValueReplacements(model.getRefinements()));
        query.setFacetQueries(RightReusabilityCategorizer.getQueryFacets());

        Class<? extends BriefBean> clazz = BriefBean.class;

        if (log.isDebugEnabled()) {
            log.debug("query: " + query);
        }
        BriefBeanView briefBeanView = null;
        try {
            briefBeanView = SearchUtils.createResults(searchService, clazz, profile, query, model.getStart(), model.getRows(), params);
            model.setBriefBeanView(briefBeanView);
            if (log.isDebugEnabled()) {
                log.debug("NumFound: " + briefBeanView.getPagination().getNumFound());
            }
            model.setEnableRefinedSearch(briefBeanView.getPagination().getNumFound() > 0);
        } catch (SolrTypeException e) {
            log.error("SolrTypeException: " + e.getMessage());

            if(StringUtils.equals(ProblemType.SEARCH_LIMIT_REACHED.getMessage(),e.getMessage())){
                model.setIsError(true);
            }
            // return new ApiError("search.json", e.getMessage());
        //    e.printStackTrace();
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
          //  e.printStackTrace();
        }

        clickStreamLogger.logBriefResultView(request, briefBeanView, query, page);
        return page;
    }

    private int fixRowsParameter(int rows) {
        rows = Math.min(rows, config.getRowLimit());
        return rows;
    }

    private int fixStartParameter(int start) {
        if (start < 1) {
            start = 1;
        }
        return start;
    }

    public InternalResourceViewResolver getViewResolver() {
        return viewResolver;
    }

    public void setViewResolver(InternalResourceViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }
}
