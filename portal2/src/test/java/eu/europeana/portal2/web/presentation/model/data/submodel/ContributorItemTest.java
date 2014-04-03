package eu.europeana.portal2.web.presentation.model.data.submodel;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.web.controllers.SitemapController;
import eu.europeana.portal2.web.presentation.model.data.submodel.ContributorItem.DataProviderItem;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class ContributorItemTest {

	@Resource
	private Configuration config;

	private ContributorItem contributorItem;

	@Before
	public void setUp() throws Exception {
		String portalServer = new StringBuilder(config.getPortalServer()).append(config.getPortalName()).toString();
		String providerQueryFormat = String.format("%s/search.html?query=*:*&qf=PROVIDER:", portalServer) + "%s";
		String queryString = null;
		try {
			queryString = StringEscapeUtils.escapeXml(String.format(providerQueryFormat,
					SitemapController.convertProviderToUrlParameter("Linked Heritage")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		contributorItem = new ContributorItem(queryString, "Linked Heritage",2611168, portalServer);
		List<DataProviderItem> dataProviders = new ArrayList<DataProviderItem>();
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "Yigal Allon Center", 1124578));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "Arts and Theatre Institute", 178111));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "Consiglio nazionale delle ricerche", 142458));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "Internet Culturale", 122447));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "askaboutireland.ie", 97875));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "Eesti Rahvusringhaaling", 95232));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "Bildarchiv Volkskundliche Kommision fur Westfalen", 91255));
		dataProviders.add(contributorItem.new DataProviderItem(
				contributorItem, "National Szechenyi Library - Digital Archive of Pictures", 80552));
		contributorItem.setDataProviders(dataProviders);
	}

	@Test
	public void testNameEncoding() {

		assertNotNull(contributorItem);
		assertNotNull(contributorItem.getDataProviders());
		assertEquals(8, contributorItem.getDataProviders().size());
		assertEquals("Yigal Allon Center", contributorItem.getDataProviders().get(0).getName());
		assertEquals("Yigal+Allon+Center", contributorItem.getDataProviders().get(0).getEncodedName());

		assertEquals("Bildarchiv Volkskundliche Kommision fur Westfalen",
				contributorItem.getDataProviders().get(6).getName());
		assertEquals("Bildarchiv+Volkskundliche+Kommision+fur+Westfalen",
				contributorItem.getDataProviders().get(6).getEncodedName());

		assertEquals("National Szechenyi Library - Digital Archive of Pictures",
				contributorItem.getDataProviders().get(7).getName());
		assertEquals("National+Szechenyi+Library+-+Digital+Archive+of+Pictures",
				contributorItem.getDataProviders().get(7).getEncodedName());
	}
}
