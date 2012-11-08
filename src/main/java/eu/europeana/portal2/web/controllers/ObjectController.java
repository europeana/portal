/*
 * Copyright 2007-2012 The Europeana Foundation
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
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.tools.utils.EuropeanaUriUtils;
import eu.europeana.corelib.web.interceptor.LocaleInterceptor;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.controllers.utils.ApiFulldocParser;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.FullBeanView;
import eu.europeana.portal2.web.presentation.model.FullBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.FullDocPage;
import eu.europeana.portal2.web.presentation.model.SeeAlsoSuggestions;
import eu.europeana.portal2.web.presentation.model.abstracts.UrlAwareData;
import eu.europeana.portal2.web.presentation.model.data.decorators.BriefBeanDecorator;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ClickStreamLogger.UserAction;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.FullBeanShortcut;
import eu.europeana.portal2.web.util.Injector;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 */
@Controller
public class ObjectController {

	private final Logger log = Logger.getLogger(getClass().getName());

	@Resource private SearchService searchService;

	@Resource(name="configurationService") private Configuration config;

	@Resource private ClickStreamLogger clickStreamLogger;

	public static final int MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES = 6;

	public static final String V1_PATH = "/v1/record/";
	public static final String SRW_EXT = ".srw";
	public static final String JSON_EXT = ".json";

	public static final Map<String, List<String>> seeAlsoFields = new LinkedHashMap<String, List<String>>() {
		private static final long serialVersionUID = 1L;
		{
			put("title", Arrays.asList(new String[]{"DcTitle", "DctermsAlternative"}));
			put("who", Arrays.asList(new String[]{"DcContributor", "DcCreator"}));
			put("what", Arrays.asList(new String[]{"DcType", "DcSubject", "DcFormat"}));
			// put("when", Arrays.asList(new String[]{"DcCoverage", "DcDate", "DcSubject", "DctermsCreated", "DctermsTemporal"}));
			// put("where", Arrays.asList(new String[]{"DcCoverage", "DcSubject", "DctermsSpatial"}));
			put("PROVIDER", Arrays.asList(new String[]{"EdmProvider"}));
		}
	};

	@RequestMapping(value = "/record/{collectionId}/{recordId}.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView record(
			@PathVariable String collectionId,
			@PathVariable String recordId,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "embedded", required = false) String embedded,
			@RequestParam(value = "query", required = false) String queryString,
			@RequestParam(value = "qf", required = false) String[] qf,
			@RequestParam(value = "start", required = false, defaultValue = "1") int start,
			@RequestParam(value = "returnTo", required = false, defaultValue = "SEARCH_HTML") SearchPageEnum returnTo,
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			@RequestParam(value = "source", required = false, defaultValue="corelib") String source,
			
			@RequestParam(value = "rows", required = false, defaultValue="12") int rows,

			HttpServletRequest request,
			HttpServletResponse response, 
			Locale locale) throws EuropeanaQueryException {

		long t0 = (new Date()).getTime();

		Injector injector = new Injector(request, response, locale);

		log.info(String.format("=========== /record/{collectionId}/{recordId}.html ============", collectionId, recordId));
		log.info(String.format("=========== /%s/%s.html ============", collectionId, recordId));
		// Map<String, String[]> parameters = sanitizeParameters(request);

		FullDocPage model = new FullDocPage();
		model.setCollectionId(collectionId);
		model.setRecordId(recordId);
		model.setFormat(format);
		model.setEmbedded(StringUtils.equalsIgnoreCase(embedded, "true"));
		model.setQuery(queryString);
		model.setRefinements(qf);
		model.setStart(start);
		model.setReturnTo(returnTo);
		model.setRows(rows);

		injector.injectProperties(model);
		model.setShownAtProviderOverride(config.getShownAtProviderOverride());
		model.setSchemaOrgMappingFile(config.getSchemaOrgMappingFile());

		long tgetFullBean0 = (new Date()).getTime();
		FullBean fullBean = getFullBean(collectionId, recordId, source, request);
		if (fullBean == null) {
			throw new EuropeanaQueryException(ProblemType.RECORD_NOT_FOUND);
		}

		long tgetFullBean1 = (new Date()).getTime();
		log.info("fullBean takes: " + (tgetFullBean1 - tgetFullBean0));
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.FULLDOC_HTML);
		if (fullBean != null) {
			log.info("fullBean: " + (fullBean == null));
			Query query = new Query(queryString).setRefinements(qf);

			// full bean view
			FullBeanView fullBeanView = new FullBeanViewImpl(fullBean, request.getParameterMap(), query, searchService);
			model.setFullBeanView(fullBeanView);

			// more like this
			List<? extends BriefBean> similarItems = fullBean.getSimilarItems();
			if (fullBean.getSimilarItems() == null) {
				similarItems = getMoreLikeThis(collectionId, recordId, model);
			}
			model.setMoreLikeThis(prepareMoreLikeThis(similarItems, model));

			long tSeeAlso0 = (new Date()).getTime();
			model.setSeeAlsoSuggestions(createSeeAlsoSuggestions(fullBean));
			long tSeeAlso1 = (new Date()).getTime();
			log.info("see also takes: " + (tSeeAlso1 - tSeeAlso0));
			clickStreamLogger.logFullResultView(request, UserAction.FULL_RESULT_HMTL, fullBeanView, page, fullBeanView.getFullDoc().getAbout());
		}

		injector.postHandle(this, page);

		long t1 = (new Date()).getTime();
		log.info("object page takes: " + (t1 - t0));

		return page;
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
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "callback", required = false) String callback,
			HttpServletRequest request
				) throws Exception {
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
			@RequestParam(value = "wskey", required = false) String wskey,
			@RequestParam(value = "callback", required = false) String callback,
			HttpServletRequest request
				) throws Exception {
		StringBuilder sb = new StringBuilder(config.getApi2url());
		sb.append(V1_PATH).append(collectionId).append("/").append(recordId).append(SRW_EXT);
		if (!StringUtils.isBlank(request.getQueryString())) {
			sb.append("?").append(request.getQueryString());
		}
		return "redirect:" + sb.toString();
	}

	private FullBean getFullBean(String collectionId, String recordId, String source, HttpServletRequest request) {
		FullBean fullBean = null;
		if (source.equals("api2")) {
			fullBean = getFullBeanFromApi(collectionId, recordId, request);
		} else {
			fullBean = getFullBeanFromCorelib(collectionId, recordId);
		}
		return fullBean;
	}
	
	/**
	 * Get FullBean through API2 calls
	 * @param collectionId
	 * @param recordId
	 * @param request
	 * @return
	 */
	private FullBean getFullBeanFromApi(String collectionId, String recordId, HttpServletRequest request) {
		FullBean fullBean = null;
		ApiFulldocParser parser = new ApiFulldocParser(config.getApi2url(), config.getApi2key(), config.getApi2secret(), request.getSession());
		fullBean = parser.getFullBean(collectionId, recordId);
		if (fullBean == null) {
			log.severe("It is not possible to retrieve FullBean though API2 calls so now the controller tries it with corelib calls");
			fullBean = getFullBeanFromCorelib(collectionId, recordId);
		}
		return fullBean;
	}

	/**
	 * Get FullBean through corelib calls
	 * @param collectionId
	 * @param recordId
	 * @return
	 */
	private FullBean getFullBeanFromCorelib(String collectionId, String recordId) {
		FullBean fullBean = null;
		try {
			String europeanaId = EuropeanaUriUtils.createEuropeanaId(collectionId, recordId);
			fullBean = searchService.findById(europeanaId);
		} catch (SolrTypeException e) {
			log.severe("Solr Type Exception: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.severe("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return fullBean;
	}

	private List<BriefBean> getMoreLikeThis(String collectionId, String recordId, UrlAwareData<?> model) {
		String europeanaId = EuropeanaUriUtils.createEuropeanaId(collectionId, recordId);
		List<BriefBean> result = null;
		try {
			result = searchService.findMoreLikeThis(europeanaId);
		} catch (SolrServerException e) {
			log.severe("Solr Server Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private List<BriefBeanDecorator> prepareMoreLikeThis(List<? extends BriefBean> result, UrlAwareData<?> model) {
		List<BriefBeanDecorator> moreLikeThis = new ArrayList<BriefBeanDecorator>();
		for (BriefBean bean : result) {
			moreLikeThis.add(new BriefBeanDecorator(model, bean));
		}

		return moreLikeThis;
	}

	private Map<String, String[]> sanitizeParameters(HttpServletRequest request) {
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String parameter = params.nextElement().toString();
			String[] values = request.getParameterValues(parameter);

			if (!isBrokenQuery(parameter, values)) {
				parameters.put(parameter, values);
			}
		}
		return parameters;
	}

	private boolean isBrokenQuery(String parameter, String[] values) {
		if ("query".equals(parameter)) {
			if (values == null || values.length != 1 || values[0] == null) {
				return true;
			}
			// one quote
			String query = values[0];
			return query.contains("\"") && StringUtils.indexOf(query, '\"') == StringUtils.lastIndexOf(query, '\"');
		}
		return false;
	}

	/**
	 * Create see also suggestions
	 * 
	 * @param fullBean
	 *   The full bean
	 * @return
	 *   The object contains the see also suggestions
	 */
	private SeeAlsoSuggestions createSeeAlsoSuggestions(FullBean fullBean) {

		FullBeanShortcut shortcut = new FullBeanShortcut((FullBeanImpl) fullBean);
		Map<String, List<String>> seeAlsoParams = new LinkedHashMap<String, List<String>>();
		for (String metaField : seeAlsoFields.keySet()) {
			List<String> fieldValues = new ArrayList<String>();
			for (String edmField : seeAlsoFields.get(metaField)) {
				String[] values = shortcut.get(edmField);
				if (values != null) {
					for (String value : values) {
						fieldValues.add(value);
						// fieldValues.add(ControllerUtil.clearSeeAlso(value));
					}
				}
			}
			if (fieldValues.size() > 0) {
				seeAlsoParams.put(metaField, fieldValues);
			}
		}
		SeeAlsoSuggestions seeAlsoSuggestions = new SeeAlsoSuggestions(config.getSeeAlsoTranslations());
		Map<String, Integer> seeAlsoResponse = searchService.seeAlso(seeAlsoParams);
		if (seeAlsoResponse != null) {
			for (Entry<String, Integer> entry : seeAlsoResponse.entrySet()) {
				seeAlsoSuggestions.add(entry.getKey(), entry.getValue());
			}
		}

		return seeAlsoSuggestions;
	}
	
	private String clearSeeAlso(String value) {
		while (value.matches(" \\([^\\(\\)]+\\)\\s*$")) {
			value = value.replaceAll(" \\([^\\(\\)]+\\)\\s*$", "");
		}
		return value;
	}
}