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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.SearchPageEnum;
import eu.europeana.portal2.web.presentation.model.FullBeanView;
import eu.europeana.portal2.web.presentation.model.FullBeanViewImpl;
import eu.europeana.portal2.web.presentation.model.FullDocPage;
import eu.europeana.portal2.web.presentation.model.data.FullDocData;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 */
@Controller
public class ObjectController {

	private static final Logger log = Logger.getLogger(ObjectController.class.getName());

	// @Autowired
	// private QueryModelFactory beanQueryModelFactory;
	
	@Resource
	private ConfigInterceptor corelib_web_configInterceptor;

	@Resource
	private SearchService searchService;

	@Value("#{europeanaProperties['portal.shownAtProviderOverride']}")
    private String[] shownAtProviderOverride;

	public static final int MIN_COMPLETENESS_TO_PROMOTE_TO_SEARCH_ENGINES = 6;

	@RequestMapping(value = "/record/{collectionId}/{recordId}.html", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView record(
			@PathVariable String collectionId,
			@PathVariable String recordId,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "embedded", required = false) String embedded,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "qf", required = false) String[] qf,
			@RequestParam(value = "start", required = false, defaultValue = "1") int start,
			@RequestParam(value = "returnTo", required = false, defaultValue = "BD") SearchPageEnum returnTo,
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			HttpServletRequest request, HttpServletResponse response, Locale locale) {
		log.info("=========== /record/{collectionId}/{recordId}.html ============");
		// Map<String, String[]> parameters = sanitizeParameters(request);

		FullDocData model = new FullDocPage();
		model.setCollectionId(collectionId);
		model.setRecordId(recordId);
		model.setFormat(format);
		model.setEmbedded(StringUtils.equalsIgnoreCase(embedded, "true"));
		model.setQuery(query);
		model.setRefinements(qf);
		model.setStart(start);
		model.setReturnTo(returnTo);
		model.setShownAtProviderOverride(shownAtProviderOverride);
		model.setTheme(ControllerUtil.getSessionManagedTheme(request, theme));

		log.info("so far good #1");
		try {
			FullBean fullBean = searchService.findById(collectionId, recordId);
			log.info("fullBean: " + fullBean);
			FullBeanView fullBeanView = new FullBeanViewImpl(fullBean);
			model.setFullBeanView(fullBeanView);
		} catch (SolrTypeException e) {
			log.severe("SolrTypeException: " + e.getMessage());
			e.printStackTrace();
		} catch (EuropeanaQueryException e) {
			log.severe("EuropeanaQueryException: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.severe("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		log.info("so far good #2");

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.FULLDOC_HTML);
		log.info("so far good #3");
		try {
			corelib_web_configInterceptor.postHandle(request, response, this, page);
		} catch (Exception e) {
			log.severe("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		log.info("so far good #4");

		return page;
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
			return query.contains("\"")
					&& StringUtils.indexOf(query, '\"') == StringUtils.lastIndexOf(query, '\"');
		}
		return false;
	}

}