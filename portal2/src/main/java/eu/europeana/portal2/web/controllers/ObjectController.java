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

package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.edm.beans.FullBean;
import eu.europeana.corelib.definitions.edm.entity.Proxy;
import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.edm.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.edm.exceptions.MongoDBException;
import eu.europeana.corelib.edm.exceptions.SolrTypeException;
import eu.europeana.corelib.search.SearchService;
import eu.europeana.corelib.search.model.ResultSet;
import eu.europeana.corelib.search.utils.SearchUtils;
import eu.europeana.corelib.utils.EuropeanaUriUtils;
import eu.europeana.corelib.utils.StringArrayUtils;
import eu.europeana.corelib.utils.service.MltStopwordsService;
import eu.europeana.corelib.utils.service.OptOutService;
import eu.europeana.corelib.web.model.rights.RightReusabilityCategorizer;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.corelib.web.utils.RequestUtils;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.portal2.services.BingTokenService;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.ClickStreamLogService.UserAction;
import eu.europeana.portal2.web.model.mlt.EuropeanaMlt;
import eu.europeana.portal2.web.model.mlt.EuropeanaMltCategory;
import eu.europeana.portal2.web.model.mlt.EuropeanaMltLink;
import eu.europeana.portal2.web.model.mlt.MltCollector;
import eu.europeana.portal2.web.model.mlt.MltConfiguration;
import eu.europeana.portal2.web.model.mlt.MltSuggestion;
import eu.europeana.portal2.web.model.seealso.SeeAlsoCollector;
import eu.europeana.portal2.web.model.seealso.SeeAlsoSuggestion;
import eu.europeana.portal2.web.model.seealso.SeeAlsoSuggestions;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.FullDocPage;
import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanShortcut;
import eu.europeana.portal2.web.presentation.model.submodel.FullBeanView;
import eu.europeana.portal2.web.presentation.model.submodel.LanguageContainer;
import eu.europeana.portal2.web.presentation.model.submodel.impl.FullBeanViewImpl;
import eu.europeana.portal2.web.presentation.semantic.SchemaOrgMapping;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 */
@Controller
public class ObjectController {

	Logger log = Logger.getLogger(this.getClass());
	@Resource
	private SearchService searchService;

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	// @Resource
	// private OptOutService optOutService;

	@Resource
	private SchemaOrgMapping schemaOrgMapping;

	@Resource
	private MltStopwordsService mltStopwordsService;

	@Resource
	private UserService userService;

	@Resource
	private ReloadableResourceBundleMessageSource messageSource;

	@Resource
	private EuropeanaUrlService europeanaUrlService;
	
	public static final String V1_PATH = "/v1/record/";
	public static final String SRW_EXT = ".srw";
	public static final String JSON_EXT = ".json";
	public static final int MAX_COUNT_PER_FIELD = 20;

	public static final Map<String, MltConfiguration> SEE_ALSO_FIELDS = new LinkedHashMap<String, MltConfiguration>();
	public static final Map<String, MltConfiguration> MLT_FIELDS = new LinkedHashMap<String, MltConfiguration>();

	private BingTokenService tokenService = new BingTokenService();
	
	
	@RequestMapping(value = "/record/{collectionId}/{recordId}.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView record(
			@PathVariable String collectionId, 
			@PathVariable String recordId,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "embedded", required = false) String embedded,
			@RequestParam(value = "query", required = false) String queryString,
			@RequestParam(value = "qf", required = false) String[] qf,
			@RequestParam(value = "qt", required = false) String[] qt,
			@RequestParam(value = "start", required = false, defaultValue = "1") int start,
			@RequestParam(value = "returnTo", required = false, defaultValue = "SEARCH_HTML") SearchPageEnum returnTo,
			@RequestParam(value = "rows", required = false, defaultValue = "24") int rows,
			@RequestParam(value = "mlt", required = false, defaultValue = "false") String mlt,
			@RequestParam(value = "context", required = false, defaultValue = "true") String context,
			@RequestParam(value = "ho", required = false, defaultValue = "false") String ho,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Locale locale) throws EuropeanaQueryException {

		long t0 = new Date().getTime();

		if (SEE_ALSO_FIELDS.isEmpty()) {
			initializeSeeAlsoConfiguration();
		}

		if (MLT_FIELDS.isEmpty()) {
			initializeMltConfiguration();
		}

		// workaround of a Spring issue (https://jira.springsource.org/browse/SPR-7963)
		Map<String, String[]> params = RequestUtils.getParameterMap(request);
		qf = ControllerUtil.fixParameter(qf, "qf", params);
		qt = ControllerUtil.fixParameter(qt, "qt", params);
		boolean showContext = ControllerUtil.getBooleanValue(context);
		
		queryString = ControllerUtil.rewriteQueryFields(queryString);
		boolean showEuropeanaMlt = ControllerUtil.getBooleanBundleValue("notranslate_show_mlt", messageSource, locale);
		boolean showSimilarItems = ControllerUtil.getBooleanBundleValue("notranslate_show_similar_items_t", messageSource, locale);
        boolean showHierarchical = searchService.isHierarchy("/" + collectionId + "/" +recordId);

		FullDocPage model = new FullDocPage();

		// for parameter in "return to search" link
		if(qt != null && qt.length==1 && qt[0].equals("false")){
			model.setLanguagesRemoved(true);
		}

		model.setBingToken(tokenService.getToken(config.getBingTranslateClientId(), config.getBingTranslateClientSecret()));
		model.setCollectionId(collectionId);
		model.setRecordId(recordId);
		model.setFormat(format);
		model.setEmbedded(StringUtils.equalsIgnoreCase(embedded, "true"));
		model.setQuery(queryString);
		model.setRefinements(qf);
		model.setStart(start);
		model.setReturnTo(returnTo);
		model.setRows(rows);
		model.setShowEuropeanaMlt(showEuropeanaMlt);
		model.setShowContext(showContext);
		
		model.setSoundCloudAwareCollections(config.getSoundCloudAwareCollections());
		model.setDoTranslation(ControllerUtil.getBooleanBundleValue("notranslate_do_translations", messageSource, locale));
		model.setUseBackendItemTranslation(config.useBackendTranslation());
		model.setUseAutomatedFrontendTranslation(config.useAutomatedFrontendTranslation());
		model.setApiUrl(config.getApi2url());
		model.setStartTime(t0);
		
		if (model.isDoTranslation()) {
			LanguageContainer languageContainer = ControllerUtil.createQueryTranslationsFromParams(userService, queryString, qt, request);
			model.setLanguages(languageContainer);
		}

        model.setShowHierarchical(showHierarchical);
		model.setShowSimilarItems(showSimilarItems);
		model.setShownAtProviderOverride(config.getShownAtProviderOverride());
		model.setEdmSchemaMappings(schemaOrgMapping);
		model.setBingTranslateId(config.getBingTranslateId());

        log.debug("Record: " + "/" + collectionId + "/" +recordId  + (showHierarchical ? " is" : " is NOT") + " hierarchical");            
		
		long tgetFullBean0 = new Date().getTime();
		FullBean fullBean = getFullBean(collectionId, recordId, showSimilarItems);
		if (fullBean == null) {
			
			String newRecordId = resolveNewRecordId(collectionId, recordId);
			showHierarchical = searchService.isHierarchy(newRecordId);
			model.setShowHierarchical(showHierarchical);
			if (StringUtils.isNotBlank(newRecordId)) {
				StringBuilder location = new StringBuilder();

				location.append(europeanaUrlService.getPortalHome(false)+"/record").append(newRecordId).append(".html");
				response.setStatus(301);
				response.setHeader("Location", location.toString());
				return null;
			} else {
				log.error("REQUESTED RECORD NOT FOUND: " + EuropeanaUriUtils.createResolveEuropeanaId(collectionId, recordId));
				throw new EuropeanaQueryException(ProblemType.RECORD_NOT_FOUND);
			}
		}


		if (log.isDebugEnabled()) {
			long tgetFullBean1 = new Date().getTime();
			log.debug("fullBean takes: " + (tgetFullBean1 - tgetFullBean0));
		}
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.FULLDOC_HTML);
		if (fullBean != null) {
			//removeHasPartsForRoots(model, fullBean);
			long tFullBeanView0 = 0;
			if (log.isDebugEnabled()) {
				tFullBeanView0 = new Date().getTime();
			}

			model.setOptedOut(fullBean.getAggregations().get(0)
					.getEdmPreviewNoDistribute()!=null?fullBean.getAggregations().get(0)
							.getEdmPreviewNoDistribute():false);
			Query query = new Query(queryString)
				.setRefinements(qf)
				.setValueReplacements(RightReusabilityCategorizer.mapValueReplacements(qf))
				.setAllowFacets(false)
				.setAllowSpellcheck(false);

			// full bean view
			FullBeanView fullBeanView = new FullBeanViewImpl(fullBean, RequestUtils.getParameterMap(request), query, searchService);
			model.setFullBeanView(fullBeanView);
			if (log.isDebugEnabled()) {
				long tFullBeanView1 = new Date().getTime();
				log.debug("fullBeanView takes: " + (tFullBeanView1 - tFullBeanView0));
			}

			// more like this
			if (model.isShowSimilarItems()) {
				List<? extends BriefBean> similarItems;
				if (fullBean.getSimilarItems() == null) {
					similarItems = getMoreLikeThis(collectionId, recordId);
				} else {
					similarItems = fullBean.getSimilarItems();
				}
				model.setMoreLikeThis(prepareMoreLikeThis(similarItems, model));
			}

			long tSeeAlso0 = 0;
			if (log.isDebugEnabled()) {
				tSeeAlso0 = new Date().getTime();
			}
			if (showEuropeanaMlt) {
				model.setMltCollector(createMltCollector(model.getShortcut()));
				model.setEuropeanaMlt(createEuropeanaMlt(model, fullBean.getAbout()));
			} else {
				model.setSeeAlsoCollector(createSeeAlsoCollector(model.getShortcut()));
				model.setSeeAlsoSuggestions(createSeeAlsoSuggestions(model.getSeeAlsoCollector()));
			}
			if (log.isDebugEnabled()) {
				long tSeeAlso1 = new Date().getTime();
				log.debug("Similarity takes: " + (tSeeAlso1 - tSeeAlso0));
			}

			clickStreamLogger.logFullResultView(request, UserAction.FULL_RESULT_HMTL, fullBeanView, page, fullBeanView
					.getFullDoc().getAbout());
		}

		if (log.isDebugEnabled()) {
			long t1 = new Date().getTime();
			log.debug("object page takes: " + (t1 - t0));
		}

		return page;
	}

	private void removeHasPartsForRoots(FullDocPage model, FullBean fullBean) {
		if (!model.isFormatLabels()
			&& config.getHierarchyRoots() != null
			&& config.getHierarchyRoots().contains(fullBean.getAbout())) {
			for (Proxy proxy : fullBean.getProxies()) {
				proxy.setDctermsHasPart(null);
			}
		}
	}

	private void initializeSeeAlsoConfiguration() {
		initializeSmilarityConfiguration(SEE_ALSO_FIELDS);
		SEE_ALSO_FIELDS.put("what",
				new MltConfiguration("what", "mlt_what_t", config.getWeightWhat(),
					"DcType", "DcSubject", "ConceptPrefLabel", "ConceptBroader", "ConceptAltLabel"));
	}

	private void initializeMltConfiguration() {
		initializeSmilarityConfiguration(MLT_FIELDS);
		MLT_FIELDS.put("what",
				new MltConfiguration("what", "mlt_what_t", config.getWeightWhat(),
					"DcType", "DcSubject", "ConceptBroader"));
		MLT_FIELDS.put("skos_concept",
			new MltConfiguration("skos_concept", "mlt_what_t", config.getWeightWhat(), "ConceptAbout"));
	}

	private void initializeSmilarityConfiguration(Map<String, MltConfiguration> map) {
		// proxy_dc_title, proxy_dcterms_alternative
		map.put("title",
			new MltConfiguration("title", "mlt_title_t", config.getWeightTitle(), "DcTitle", "DctermsAlternative"));
		// proxy_dc_creator, ag_skos_prefLabel -- missing: ag_skos_altLabel, ag_foaf_name
		map.put("who",
			new MltConfiguration("who", "mlt_who_t", config.getWeightWho(), "DcCreator", "AgentPrefLabel"));
		// proxy_dc_type, proxy_dc_subject, cc_skos_prefLabel, cc_skos_broaderLabel, cc_skos_altLabel
		map.put("DATA_PROVIDER",
			new MltConfiguration("DATA_PROVIDER", "mlt_provider_t", config.getWeightDataProvider(), "DataProvider"));
		map.put("PROVIDER",
			new MltConfiguration("PROVIDER", "mlt_data_provider_t", config.getWeightProvider(), "EdmProvider"));
	}

	/**
	 * Redirects to API's record JSON call
	 * 
	 * @param collectionId
	 * @param recordId
	 * @param wskey
	 * @param callback
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/record/{collectionId}/{recordId}.json", produces = MediaType.TEXT_HTML_VALUE)
	public String redirectJson(
			@PathVariable String collectionId, 
			@PathVariable String recordId,
			HttpServletRequest request) throws Exception {
		StringBuilder sb = new StringBuilder(config.getApi2url());
		sb.append(V1_PATH).append(collectionId).append("/").append(recordId).append(JSON_EXT);
		if (!StringUtils.isBlank(request.getQueryString())) {
			sb.append("?").append(request.getQueryString());
		}
		return "redirect:" + sb.toString();
	}

	/**
	 * Redirects to API's record SRW call
	 * 
	 * @param collectionId
	 * @param recordId
	 * @param wskey
	 * @param callback
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/record/{collectionId}/{recordId}.srw", produces = MediaType.TEXT_HTML_VALUE)
	public String redirectSrw(
			@PathVariable String collectionId, 
			@PathVariable String recordId,
			HttpServletRequest request)	throws Exception {
		StringBuilder sb = new StringBuilder(config.getApi2url());
		sb.append(V1_PATH).append(collectionId).append("/").append(recordId).append(SRW_EXT);
		if (!StringUtils.isBlank(request.getQueryString())) {
			sb.append("?").append(request.getQueryString());
		}
		return "redirect:" + sb.toString();
	}

	/**
	 * Get FullBean through corelib calls
	 * 
	 * @param collectionId
	 * @param recordId
	 * @return
	 */
	private FullBean getFullBean(String collectionId, String recordId, boolean showSimilarItems) {
		FullBean fullBean = null;
		String europeanaId = EuropeanaUriUtils.createResolveEuropeanaId(collectionId, recordId);
		try {
			fullBean = searchService.findById(europeanaId, showSimilarItems);
		} catch (MongoDBException e) {
			System.out.println("here should be a log.error");
			log.error(String.format("MongoDB Exception during getting the full bean for ID %s: %s", europeanaId,
					e.getMessage()));
		} catch (NullPointerException e) {
			System.out.println("here should be a log.error");
			log.error(String.format("Exception during getting the full bean for ID %s: %s", europeanaId,
					e.getStackTrace()[0]));
		}
		return fullBean;
	}

	private String resolveNewRecordId(String collectionId, String recordId) {
		String newRecordId = null;
		String europeanaId = EuropeanaUriUtils.createResolveEuropeanaId(collectionId, recordId);
		try {
			newRecordId = searchService.resolveId(europeanaId);
		} catch (NullPointerException e) {
			System.out.println("here should be a log.error");
			log.error(String.format("Exception during getting the full bean for ID %s: %s", europeanaId,
					e.getStackTrace()[0]));
		}
		return newRecordId;
	}

	private List<BriefBean> getMoreLikeThis(String collectionId, String recordId) {
		String europeanaId = EuropeanaUriUtils.createResolveEuropeanaId(collectionId, recordId);
		List<BriefBean> result = null;
		try {
			result = searchService.findMoreLikeThis(europeanaId);
		} catch (SolrServerException e) {
			log.error("Solr Server Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private List<BriefBeanDecorator> prepareMoreLikeThis(List<? extends BriefBean> result, UrlAwareData<?> model) {
		List<BriefBeanDecorator> moreLikeThis = new ArrayList<BriefBeanDecorator>();
		if (result != null) {
			for (BriefBean bean : result) {
				moreLikeThis.add(new BriefBeanDecorator(model, bean));
			}
		}

		return moreLikeThis;
	}

	
	/**
	 * Create see also suggestions
	 * 
	 * @param fullBean
	 *            The full bean
	 * @return The object contains the see also suggestions
	 */
	private SeeAlsoSuggestions createSeeAlsoSuggestions(SeeAlsoCollector seeAlsoCollector) {

		SeeAlsoSuggestions seeAlsoSuggestions = new SeeAlsoSuggestions(
			config.getSeeAlsoTranslations(),
			config.getSeeAlsoAggregations(),
			seeAlsoCollector
		);

		try {
			Map<String, Integer> seeAlsoResponse = searchService.seeAlso(seeAlsoCollector.getQueries());
			if (seeAlsoResponse != null) {
				for (Entry<String, Integer> entry : seeAlsoResponse.entrySet()) {
					String query = entry.getKey();
					int count = entry.getValue();
					if (count > 0) {
						seeAlsoSuggestions.add(query, count);
					}
				}
			}
		} catch (Exception e) {
			log.error("See also error: " + e.getClass().getCanonicalName() + " " + e.getMessage(),e);
		}

		return seeAlsoSuggestions;
	}

	private EuropeanaMlt createEuropeanaMlt(FullDocPage model, String europeanaId) {
		MltCollector mltCollector = model.getMltCollector();
		config.getSeeAlsoTranslations();
		long tSeeAlso0 = new Date().getTime();
		EuropeanaMlt mlt = new EuropeanaMlt();
		boolean hasDataProvider = (mltCollector.get("DATA_PROVIDER") != null);
		List<String> queryList = new ArrayList<String>();
		for (String field : MLT_FIELDS.keySet()) {
			if ((field.equals("PROVIDER") && hasDataProvider)
				|| mltCollector.get(field) == null
				|| mltCollector.get(field).size() == 0)
			{
				continue;
			}
			String query = mltCollector.getQuery(field, MLT_FIELDS.get(field).getWeight());
			queryList.add(query);
		}

		String query = String.format("(%s) NOT europeana_id:\"%s\"", StringUtils.join(queryList, " OR "), europeanaId);
		EuropeanaMltCategory category = new EuropeanaMltCategory("all", "all", "mlt_similar_t", query);

		ResultSet<? extends BriefBean> results = searchMltItem(query);
		for (BriefBean bean : results.getResults()) {
			if (!bean.getId().equals(europeanaId)) {
				BriefBeanDecorator doc = new BriefBeanDecorator(model, bean);
				String title = bean.getId();
				if (StringArrayUtils.isNotBlank(doc.getTitleBidi())) {
					for (String t : doc.getTitleBidi()) {
						if (StringUtils.isNotBlank(t)) {
							title = t;
							break;
						}
					}
				}
				String thumbnail = doc.getThumbnail();
				category.addUrl(new EuropeanaMltLink(bean.getId(), title, doc.getFullDocUrl(), thumbnail));
			}
		}
		category.addResultSize(results.getResultSize());
		if (category.getUrls().size() > 0) {
			mlt.addCategory(category);
		}

		long tSeeAlso1 = new Date().getTime();
		log.info("createEuropeanaMlt takes: " + (tSeeAlso1 - tSeeAlso0));

		return mlt;
	}

	private ResultSet<? extends BriefBean> searchMltItem(String queryTerm) {
		Query query = new Query(queryTerm)
			.setPageSize(12)
			.setStart(0) // Solr starts from 0
			.setAllowSpellcheck(false)
			.setAllowFacets(false)
		;
		try {
			log.info("Query was: " + query.toString());
			ResultSet<? extends BriefBean> resultSet = searchService.search(BriefBean.class, query);
			return resultSet;
		} catch (SolrTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private SeeAlsoCollector createSeeAlsoCollector(FullBeanShortcut shortcut) {
		SeeAlsoCollector seeAlsoCollector = new SeeAlsoCollector();
		int countPerField = 0, id = 0;
		for (String metaField : SEE_ALSO_FIELDS.keySet()) {
			for (String edmField : SEE_ALSO_FIELDS.get(metaField).getEdmFields()) {
				String[] values = shortcut.get(edmField);
				if (values != null) {
					countPerField = 0;
					for (String value : values) {
						if (!StringUtils.isBlank(value)
							&& value.length() < 500 
							&& countPerField < MAX_COUNT_PER_FIELD)
						{
							SeeAlsoSuggestion suggestion = new SeeAlsoSuggestion(metaField, value, id);
							if (suggestion.getQuery().startsWith("http://")) {
								suggestion.makeEscapedQuery(suggestion.getQuery());
							} else {
								suggestion.makeEscapedQuery(SearchUtils
										.escapeQuery(suggestion.getQuery()));
							}
							seeAlsoCollector.add(suggestion);
							countPerField++; id++;
						}
					}
				}
			}
		}
		return seeAlsoCollector;
	}

	private MltCollector createMltCollector(FullBeanShortcut shortcut) {
		MltCollector mltCollector = new MltCollector();
		int countPerField = 0, id = 0;
		for (String metaField : MLT_FIELDS.keySet()) {
			for (String edmField : MLT_FIELDS.get(metaField).getEdmFields()) {
				String[] values = shortcut.get(edmField);
				if (values != null) {
					countPerField = 0;
					for (String value : values) {
						if (StringUtils.isNotBlank(value)
							&& value.length() < 500
							&& countPerField < MAX_COUNT_PER_FIELD
							&& !mltStopwordsService.check(value)
						)
						{
							boolean clear = true;
							if (StringUtils.equals(metaField, "PROVIDER") ||
							    StringUtils.equals(metaField, "DATA_PROVIDER")) {
								clear = false;
							}
							MltSuggestion suggestion = new MltSuggestion(metaField, StringUtils.trim(value), id, clear);
							if (suggestion.getQuery().startsWith("http://")) {
								suggestion.makeEscapedQuery(suggestion.getQuery());
							} else {
								suggestion.makeEscapedQuery(SearchUtils
										.escapeQuery(suggestion.getQuery()));
							}
							mltCollector.add(suggestion);
							countPerField++; 
							id++;
						}
					}
				}
			}
		}
		return mltCollector;
	}
}
