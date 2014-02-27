package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SitemapPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy.DatasetContainer;
import eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy.ProviderItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy.DataProviderItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.datahierarchy.Dataset;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class DataHierarchy {

	@Log
	private Logger log;

	@Resource
	private SearchService searchService;

	private final static List<String> collectionFacets = Arrays.asList(new String[]{"PROVIDER", "DATA_PROVIDER"});
	private List<ProviderItem> providerItems;

	@RequestMapping(value = "/data-hierarchy.xml", produces = MediaType.TEXT_XML_VALUE)
	@ResponseBody
	public ModelAndView hierarcyInXml(
			@RequestParam(value = "prefix", required = false) String prefix,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) 
					throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Content-Type", "text/xml; charset=UTF-8");

		SitemapPage<ProviderItem> model = new SitemapPage<ProviderItem>();
		model.setResults(getProviders());
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.DATA_HIERARCHY_XML);
		return page;
	}

	@RequestMapping(value = "/data-hierarchy.txt", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public ModelAndView hierarcyInTxt(
			@RequestParam(value = "prefix", required = false) String prefix,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) 
					throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain; charset=UTF-8");
		response.setHeader("Content-Type", "text/plain; charset=UTF-8");

		SitemapPage<ProviderItem> model = new SitemapPage<ProviderItem>();
		model.setResults(getProviders());
		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.DATA_HIERARCHY_TXT);
		return page;
	}

	private List<ProviderItem> getProviders() {

		if (providerItems == null) {
			providerItems = new ArrayList<ProviderItem>();
			try {
				List<Count> providers = searchService.createCollections("PROVIDER", "*:*");
				for (Count provider : providers) {
					ProviderItem providerItem = new ProviderItem(provider.getName(), provider.getCount());
					providerItems.add(providerItem);

					List<Count> rawDataProviders = searchService.createCollections(
							"DATA_PROVIDER", "*:*", "PROVIDER:\"" + escape(provider.getName()) + "\"");
					if (rawDataProviders.size() > 0) {
						for (Count dataProvider : rawDataProviders) {
							DataProviderItem dataProviderItem = new DataProviderItem(dataProvider.getName(), dataProvider.getCount());
							providerItem.addDataProvider(dataProviderItem);

							List<Count> collectionNames;
							if (dataProvider.getCount() > 0) {
								collectionNames = searchService.createCollections(
										"europeana_collectionName",
										"*:*",
										"PROVIDER:\"" + escape(provider.getName()) + "\"",
										"DATA_PROVIDER:\"" + escape(dataProvider.getName()) + "\"");
							} else {
								collectionNames = searchService.createCollections(
										"europeana_collectionName",
										"*:*",
										"PROVIDER:\"" + escape(provider.getName()) + "\"");
							}

							setDatasets(dataProviderItem, collectionNames);
						}
					} else {
						List<Count> collectionNames = searchService.createCollections(
								"europeana_collectionName",
								"*:*",
								"PROVIDER:\"" + escape(provider.getName()) + "\"");
						setDatasets(providerItem, collectionNames);
					}
				}
			} catch (SolrTypeException e1) {
				e1.printStackTrace();
			}
		}

		return providerItems;
	}

	private void setDatasets(DatasetContainer container, List<Count> collectionNames) throws SolrTypeException {
		List<Dataset> datasets = new ArrayList<Dataset>();
		Long count = 0L;
		for (Count collection : collectionNames) {
			count += collection.getCount();
			Dataset dataset = new Dataset(collection.getName(), collection.getCount());
			datasets.add(dataset);

			Query collectionQuery = new Query("europeana_collectionName:\"" + escape(collection.getName()) + "\"")
				.setAllowFacets(true)
				.setFacets(collectionFacets)
				.setPageSize(0)
				.setAllowSpellcheck(false)
				.setParameter("facet.mincount", "1")
				;
			ResultSet<BriefBean> collectionResponse = searchService.search(BriefBean.class, collectionQuery);
			for (FacetField facetField : collectionResponse.getFacetFields()) {
				if (facetField.getName().equalsIgnoreCase("PROVIDER")) {
					dataset.setProviderCount(facetField.getValues().size());
				} else if (facetField.getName().equalsIgnoreCase("DATA_PROVIDER")) {
					dataset.setDataProviderCount(facetField.getValues().size());
				}
			}
		}
		container.setDatasets(datasets);
		container.setTotalDatasetCount(count);
	}

	private String escape(String text) {
		return text.replace("\"", "\\\"").replace("/", "\\/");
	}
}
