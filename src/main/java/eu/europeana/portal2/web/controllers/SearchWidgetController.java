package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.solr.Facet;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.definitions.users.Role;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.SearchWidgetPage;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class SearchWidgetController {

	@Resource private SearchService searchService;

	@Resource(name="corelib_db_userService") private UserService userService;

	@RequestMapping("/search-widget.html")
	public ModelAndView searchWidget(Locale locale) throws Exception {

		/**
		 * Obtain the current logged in user and check for access role
		 */
		User user = ControllerUtil.getUser(userService);
		if ((user == null) || (user.getRole() == Role.ROLE_USER)) {
			throw new Exception("Illegal attempt to access remote search widget form");
		}

		SearchWidgetPage model = new SearchWidgetPage();

		/**
		 * An alphabetically ordered list of content providers the user can show
		 * data for in their plugin.
		 */
		List<String> providers = new ArrayList<String>();
		Query query = new Query("*:*")
						.setFacets(new Facet[]{Facet.PROVIDER})
						.setPageSize(0)
						.setAllowSpellcheck(false);
		// List<Count> collections = IngestionUtils.getCollectionsFromSolr(beanQueryModelFactory, "PROVIDER", "*:*");
		ResultSet<BriefBean> results = searchService.search(BriefBean.class, query);
		for (FacetField facetField : results.getFacetFields()) {
			for (Count count : facetField.getValues()) {
				providers.add(count.getName());
			}
		}
		Collections.sort(providers);
		model.setProviders(providers);

		return ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.SEARCH_WIDGET);
	}
}
