/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.0 or as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package eu.europeana.portal2.querymodel.query;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedResource;

import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.portal2.web.presentation.model.FullBeanView;

/**
 * Bean-based implementation of QueryModelFactory. This bean is exposed via JMX.
 *
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 * @author Serkan Demirel <serkan@blackbuilt.nl>
 *         todo: make singleton
 */

@ManagedResource(objectName = "BeanQueryModelFactory:type=runtime")
public class BeanQueryModelFactory extends NotificationBroadcasterSupport implements QueryModelFactory, BeanQueryModelFactoryMBean {

    private static final int ROW_LIMIT = 12;
    private Logger log = Logger.getLogger(getClass().getName());
    private QueryAnalyzer queryAnalyzer;
    private SolrServer solrServer1;
    private SolrServer solrServer2;
    private AnnotationProcessor annotationProcessor;
    private String portalName;

    @Value("#{europeanaProperties['debug']}")
    private String debug;

    @Value("#{europeanaProperties['solr.facetLimit']}")
    private int facetLimit;

    // Monitoring

    private static long solrResponseDuration;
    private static long cacheResponseDuration;
    private static long executedQueries;
    private static long fullDocsServedFromCache;
    private static long fullDocsServedFromSolr;
    private static long briefDocsServedFromSolr;
    private static long briefDocFromSolrResponseDuration;
    private static int sequence;
    private static long failoverSolrHits;
    private static long failedQueries;
    private static long mainSolrHits;
    private static long deadHits;

    @Value("#{europeanaProperties['portal.name']}")
    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public void setSolrServer1(SolrServer solrServer) {
        this.solrServer1 = solrServer;
    }

    public void setSolrServer2(SolrServer solrServer) {
        this.solrServer2 = solrServer;
    }

    public SolrServer getSolrServer2() {
        return solrServer2;
    }

    public SolrServer getSolrServer1() {
        return solrServer1;
    }

    public SolrServer getActiveSolrServer() {
        return solrServer1.isActive() ? solrServer1 : solrServer2;
    }

    @Autowired
    public void setAnnotationProcessor(AnnotationProcessor annotationProcessor) {
        this.annotationProcessor = annotationProcessor;
    }

    @Autowired
    public void setQueryAnalyzer(QueryAnalyzer queryAnalyzer) {
        this.queryAnalyzer = queryAnalyzer;
    }

    @Autowired
    @Qualifier("emailSenderForExceptions")
    private EmailSender emailSenderForExceptions;

    private QueryResponse executeSolrQuery(SolrQuery solrQuery) throws EuropeanaQueryException, SolrServerException {
        long startTime = System.currentTimeMillis();
        QueryResponse response;
        if (getSolrServer1().isActive()) {
            try {
                mainSolrHits++;
                response = getSolrServer1().query(solrQuery);
            }
            catch (SolrServerException e) {
                failedQueries++;
                if (isSolrUnreachableException(e)) {
                    if (getSolrServer1().isActive()) {
                        if (!Boolean.valueOf(debug)) {
                            sendSolrOverflowEmail(solrQuery, e);
                        }
                        getSolrServer1().suspend();
                    }
                    // todo: what if this fails?
                    failoverSolrHits++;
                    response = getSolrServer2().query(solrQuery);
                }
                else {
                    publish(e);
                    throw e;
                }
            }
        }
        else {
            failoverSolrHits++;
            response = getSolrServer2().query(solrQuery);
        }
        solrResponseDuration = System.currentTimeMillis() - startTime;
        executedQueries++;
        return response;
    }

    // todo: launch this from an executor service and create a factory for this.
    private void sendSolrOverflowEmail(SolrQuery solrQuery, Exception exception) {
        try {
            Map<String, Object> model = new TreeMap<String, Object>();
            model.put("hostName", getSolrServer1().getBaseURL());
            model.put("request", "" + solrQuery.getQuery());
            model.put("stackTrace", ExceptionUtils.getFullStackTrace(exception));
            model.put("portalName", portalName);
            model.put("agent", exception.getMessage());
            model.put("referer", "");
            model.put("date", new DateTime());
            model.put(EmailSender.SUBJECT, "PRODUCTION SOLR OVERLOADED, traffic diverted till " + getSolrServer1().getActiveFrom());
            emailSenderForExceptions.sendEmail(model);
        }
        catch (Exception e) {
            publish(e);
            log.warn("Unable to send email to " + emailSenderForExceptions.getToEmail(), e);
        }
    }

    /**
     * create solr query from http query parameters
     */
    @Override
    public SolrQuery createFromQueryParams(Map<String, String[]> params) throws EuropeanaQueryException {
        SolrQuery query = SolrQueryUtil.createFromQueryParams(params, queryAnalyzer);
        specifySortFields(query);
        return query;
    }

    private Class<?> briefBean;

    public void setBriefBean(Class<?> briefBean) {
        this.briefBean = briefBean;
    }

    private Class<? extends DocId> idBean;

    public void setIdBean(Class<? extends DocId> idBean) {
        this.idBean = idBean;
    }

    public Class<? extends DocId> getIdBean() {
        return idBean;
    }

    @Override
    public BriefBeanView getBriefResultView(SolrQuery solrQuery, String requestQueryString) throws EuropeanaQueryException, UnsupportedEncodingException {
        return getBriefResultView(solrQuery, requestQueryString, null);
    }

    @Override
    public BriefBeanView getBriefResultView(SolrQuery solrQuery, String requestQueryString, String[] removeFilters) throws EuropeanaQueryException, UnsupportedEncodingException {
        QueryResponse queryResponse = getSolrResponse(solrQuery, briefBean);
        if (removeFilters != null) {
            for (String filter : removeFilters) {
                solrQuery.removeFilterQuery(filter);
            }
        }
        return new BriefBeanViewImpl(this, solrQuery, queryResponse, requestQueryString);
    }
    
    public List<? extends BriefDoc> getBriefDocs(SolrQuery solrQuery) throws EuropeanaQueryException, UnsupportedEncodingException {
        QueryResponse queryResponse = getSolrResponse(solrQuery, briefBean);
        return addIndexToBriefDocList(solrQuery, SolrBinder.getInstance().bindBriefDoc(queryResponse.getResults()));
    }

    // TODO: convert (http)params to (query)parameters
    @Override
    public FullBeanView getFullResultView(String europeanaUri, Map<String, String[]> params) 
    		throws EuropeanaQueryException, SolrServerException {

        Map<String, String[]> paramsWithUri = preparePrameters(europeanaUri, params);
        return fetchFullDocFromSolr(europeanaUri, paramsWithUri);
    }

    @Override
    public TermBeanView getTermBeanView(SolrQuery solrQuery, String requestQueryString) throws EuropeanaQueryException, UnsupportedEncodingException {
        return null;  // todo: implement
    }

    private Map<String, String[]> preparePrameters(String europeanaUri, Map<String, String[]> params) {

        Map<String, String[]> paramsWithUri = new HashMap<String, String[]>();
        if (params != null) {
            paramsWithUri.putAll(params);
        }
        paramsWithUri.put("uri", new String[]{europeanaUri});
        return paramsWithUri;
    }

    private FullBeanView fetchFullDocFromSolr(String europeanaUri, Map<String, String[]> paramsWithUri)
            throws EuropeanaQueryException, SolrServerException {
        long startTime = System.currentTimeMillis();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("europeana_uri:\"" + europeanaUri + "\"");
        solrQuery.setQueryType(QueryType.MORE_LIKE_THIS_QUERY.toString());
//        String view = paramsWithUri.get("pageId") == null ? null : paramsWithUri.get("pageId")[0];
//        if (view != null && view.equalsIgnoreCase("tlv")) {
//            solrQuery.setSortField("YEAR", SolrQuery.ORDER.asc);
//        }
        QueryResponse response = getSolrServer2().query(solrQuery);
        fullDocsServedFromSolr++;
        solrResponseDuration = System.currentTimeMillis() - startTime;
        return new FullBeanViewImpl(this, solrQuery, response, paramsWithUri);
    }

    /**
     * Get records from Sorl for a particular collection for the siteMap.
     *
     * @param rowsReturned number of rows to be returned from Solr
     * @param pageNumber   which page of the sitemap per collection will be returned.
     *
     * @return list of IdBeans
     *
     * @throws EuropeanaQueryException
     * @throws SolrServerException
     */
    public SiteMapBeanView getSiteMapBeanView(String query, int rowsReturned, int pageNumber) throws EuropeanaQueryException, SolrServerException {

        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setRows(rowsReturned);
        //solrQuery.setFields("europeana_uri", "timestamp");
        solrQuery.setStart(pageNumber * rowsReturned);
        solrQuery.setQueryType(QueryType.ADVANCED_QUERY.toString());
        EuropeanaBean bean = annotationProcessor.getEuropeanaBean(BriefBean.class);
        solrQuery.setFields(bean.getFieldStrings());
        QueryResponse queryResponse = executeSolrQuery(solrQuery);
        return new SiteMapBeanViewImpl(queryResponse, rowsReturned);
    }

    public List<? extends BriefDoc> addIndexToBriefDocList(SolrQuery solrQuery, List<? extends BriefDoc> briefDocList) {
        Integer start = solrQuery.getStart();
        int index = start == null ? 1 : start + 1;
        for (BriefDoc briefDoc : briefDocList) {
            briefDoc.setIndex(index++);
            // todo: hardcoded ESE
            briefDoc.setFullDocUrl(createFullDocUrl(briefDoc.getId()));
        }
        return briefDocList;
    }

    public String createFullDocUrl(String europeanaId) {
        if (StringUtils.isEmpty(europeanaId)) {
            failedQueries++;
            throw new NullPointerException("EuropeanaId can't be null");
        }
        return MessageFormat.format("/{0}{1}.html", portalName, europeanaId.replaceAll("http://www.europeana.eu/resolve", ""));
    }

    @Override
    public QueryResponse getSolrResponse(SolrQuery solrQuery) throws EuropeanaQueryException {
        return getSolrResponse(solrQuery, true);
    }

    @Override
    public DocumentObjectBinder getBinder() {
        return null;  // todo: implement
    }

    private QueryResponse getSolrResponse(SolrQuery solrQuery, boolean decrementStart) throws EuropeanaQueryException {
        if (solrQuery.getStart() != null && solrQuery.getStart() < 0) {
            solrQuery.setStart(0);
            log.warn("Solr Start cannot be negative");
        }
        // solr query is 0 based
        if (decrementStart && solrQuery.getStart() != null && solrQuery.getStart() > 0) {
            solrQuery.setStart(solrQuery.getStart() - 1);
        }
        QueryResponse queryResponse;
        try {
            log.info(solrQuery);
            queryResponse = executeSolrQuery(solrQuery);
        }
        catch (SolrException e) {
            log.error("unable to execute SolrQuery", e);
            throw new EuropeanaQueryException(ProblemType.MALFORMED_QUERY.toString(), e);
        }
        catch (SolrServerException e) {
            //todo determine which errors the SolrServer can throw
            log.error("Unable to fetch result", e);
            if (isSolrUnreachableException(e)) {
                throw new EuropeanaQueryException(ProblemType.SOLR_UNREACHABLE.toString(), e);
            }
            else {
                throw new EuropeanaQueryException(ProblemType.MALFORMED_QUERY.toString(), e);
            }
        }
        return queryResponse;
    }

    private boolean isSolrUnreachableException(SolrServerException e) {
        return !e.getMessage().equalsIgnoreCase("Error executing query");
    }

    public QueryResponse getSolrResponse(SolrQuery solrQuery, Class<?> beanClass) throws EuropeanaQueryException { // add bean to ???
        long startTime = System.currentTimeMillis();
        // since we make a defensive copy before the start is decremented we must do it here
        if (solrQuery.getStart() != null && solrQuery.getStart() > 0) {
            solrQuery.setStart(solrQuery.getStart() - 1);
        }
        // set facets
        if (beanClass == briefBean) {
            // only show spelling-suggestion on the first result page
            if ((solrQuery.getStart() == null || solrQuery.getStart() == 0) && solrQuery.getFilterQueries() == null) {
                // give spelling suggestions
                solrQuery.setParam("spellcheck", true);
                solrQuery.setParam("spellcheck.collate", true);
                solrQuery.setParam("spellcheck.extendedResults", true);
                solrQuery.setParam("spellcheck.onlyMorePopular", true);
                //                solrQuery.setParam("spellcheck.count", "4");
            }
            solrQuery.setFacet(true);
            solrQuery.setFacetMinCount(1);
            solrQuery.setFacetLimit(facetLimit);
            if (solrQuery.getRows() == null) {
                solrQuery.setRows(ROW_LIMIT); // todo replace with annotation later
            }
            solrQuery.addFacetField(annotationProcessor.getFacetFieldStrings());
            EuropeanaBean bean = annotationProcessor.getEuropeanaBean(beanClass);
            solrQuery.setFields(bean.getFieldStrings());
            // TODO: from model solrQuery.setFields("*");
            if (solrQuery.getQueryType().equalsIgnoreCase(QueryType.SIMPLE_QUERY.toString())) {
                solrQuery.setQueryType(queryAnalyzer.findSolrQueryType(solrQuery.getQuery()).toString());
            }
            solrResponseDuration = System.currentTimeMillis() - startTime;
        }
        SolrQuery dCopy = copySolrQuery(solrQuery);
        return getSolrResponse(dCopy, false);
    }

    private void specifySortFields(SolrQuery solrQuery) {
        // sorting
    	solrQuery.addSortField("score", ORDER.desc);
    	//ASSETS
    	//solrQuery.addSortField("europeana_completeness", ORDER.desc);
        

        // field queries contain a colon and are often heavy with nice results order being less of importance
        //ASSETS
    	//if (!StringUtils.contains(solrQuery.getQuery(), ":")) {
        //    solrQuery.addSortField("europeana_recordHashFirstSix", ORDER.desc);
        //}
    }

    private SolrQuery copySolrQuery(SolrQuery solrQuery) {
        SolrQuery dCopy = solrQuery.getCopy();
        dCopy.setFilterQueries(SolrQueryUtil.getFilterQueriesAsOrQueries(solrQuery, annotationProcessor.getFacetMap()));
        return dCopy;
    }

    public static ResultPagination createPagination(QueryResponse response, SolrQuery query, String requestQueryString) throws EuropeanaQueryException {
        int numFound = (int) response.getResults().getNumFound();
        Boolean debug = query.getBool("debugQuery");
        String parsedQuery = "Information not available";
        if (debug != null && debug) {
            parsedQuery = String.valueOf(response.getDebugMap().get("parsedquery_toString"));
        }
        return new ResultPaginationImpl(query, numFound, requestQueryString, parsedQuery);
    }

    // Monitoring

    @Override
    public long getSolrResponseDuration() {
        return solrResponseDuration;
    }

    @Override
    public long getFullDocFromCacheResponseDuration() {
        return cacheResponseDuration;
    }

    @Override
    public long getBriefDocFromSolrResponseDuration() {
        return briefDocFromSolrResponseDuration;
    }

    @Override
    public long getExecutedQueries() {
        return executedQueries;
    }

    @Override
    public long getFailedQueries() {
        return failedQueries;
    }

    @Override
    public long getFullDocsServedFromCache() {
        return fullDocsServedFromCache;
    }

    @Override
    public long getFullDocsServedFromSolr() {
        return fullDocsServedFromSolr;
    }

    @Override
    public long getBriefDocsServedFromSolr() {
        return briefDocsServedFromSolr;
    }

    @Override
    public void publish(Throwable throwable) {
        Notification notification = new Notification(
                getClass().getName(),
                this,
                sequence++,
                System.currentTimeMillis(),
                throwable.getMessage()
        );
        sendNotification(notification);
    }

    @Override
    public long getMainSolrHits() {
        return mainSolrHits;
    }

    @Override
    public long getFailoverSolrHits() {
        return failoverSolrHits;
    }

    @Override
    public long getDeadHits() {
        return deadHits;
    }

    public void setTermBean(Class termBean) {
        //To change body of created methods use File | Settings | File Templates.
    }
}