package eu.europeana.portal2.querymodel.query;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.portal2.web.model.facets.Facet;
import eu.europeana.portal2.web.model.facets.LabelFrequency;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/portal2-test.xml"})
public class FacetQueryLinksImplTest {

	@Before
	public void setUp() throws Exception {
	}

	// @Test
	public void testGeneralFacets() {
		List<Facet> facetFields = new ArrayList<Facet>();
		Facet facet1 = new Facet();
		facet1.name = "COUNTRY";
		facet1.fields = new ArrayList<LabelFrequency>();
		facet1.fields.add(new LabelFrequency("united kingdom", 23));
		facet1.fields.add(new LabelFrequency("the netherlands", 13));
		facetFields.add(facet1);

		Facet facet2 = new Facet();
		facet2.name = "RIGHTS";
		facet2.fields = new ArrayList<LabelFrequency>();
		facet2.fields.add(new LabelFrequency("http://www.europeana.eu/rights/rr-f/", 23));
		facet2.fields.add(new LabelFrequency("http://www.europeana.eu/rights/rr-r/", 13));
		facetFields.add(facet2);

		Facet facet3 = new Facet();
		facet3.name = "DATA_PROVIDER";
		facet3.fields = new ArrayList<LabelFrequency>();
		facet3.fields.add(new LabelFrequency("Dario Fo & Franca Rame Archive, CTFR, Milano, Italia", 11));
		facetFields.add(facet3);

		Facet facet4 = new Facet();
		facet4.name = "YEAR";
		facet4.fields = new ArrayList<LabelFrequency>();
		facet4.fields.add(new LabelFrequency("-1453", 11));
		facet4.fields.add(new LabelFrequency("1453", 11));
		facetFields.add(facet4);

		Query query = new Query("*:*");
		List<FacetQueryLinks> links = FacetQueryLinksImpl.createDecoratedFacets(facetFields, query);

		assertEquals(4, links.size());

		// the first facet
		FacetQueryLinks link = links.get(0);
		assertEquals("COUNTRY", link.getType());

		List<FacetCountLink> counts = link.getLinks();

		assertEquals(2, counts.size());
		FacetCountLink count = counts.get(0);
		assertEquals(23, count.getCount());
		assertEquals("/portal/search.html?query=*%3A*&rows=12&qf=COUNTRY%3A%22united+kingdom%22", count.getUrl());
		assertEquals("&qf=COUNTRY%3A%22united+kingdom%22", count.getParam());

		count = counts.get(1);
		assertEquals(13, count.getCount());
		assertEquals("/portal/search.html?query=*%3A*&rows=12&qf=COUNTRY%3A%22the+netherlands%22", count.getUrl());
		assertEquals("&qf=COUNTRY%3A%22the+netherlands%22", count.getParam());

		// the second facet
		link = links.get(1);
		assertEquals("RIGHTS", link.getType());

		counts = link.getLinks();

		count = counts.get(0);
		assertEquals(23, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=RIGHTS%3Ahttp%3A%2F%2Fwww.europeana.eu%2Frights%2Frr-f%2F*",
			count.getUrl());
		assertEquals("&qf=RIGHTS%3Ahttp%3A%2F%2Fwww.europeana.eu%2Frights%2Frr-f%2F*", count.getParam());

		count = counts.get(1);
		assertEquals(13, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=RIGHTS%3Ahttp%3A%2F%2Fwww.europeana.eu%2Frights%2Frr-r%2F*",
			count.getUrl());
		assertEquals("&qf=RIGHTS%3Ahttp%3A%2F%2Fwww.europeana.eu%2Frights%2Frr-r%2F*", count.getParam());

		// the third facet
		link = links.get(2);
		assertEquals("DATA_PROVIDER", link.getType());

		counts = link.getLinks();

		count = counts.get(0);
		assertEquals(11, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=DATA_PROVIDER%3A%22Dario+Fo+%26+Franca+Rame+Archive%2C+CTFR%2C+Milano%2C+Italia%22",
			count.getUrl());
		assertEquals("&qf=DATA_PROVIDER%3A%22Dario+Fo+%26+Franca+Rame+Archive%2C+CTFR%2C+Milano%2C+Italia%22", count.getParam());

		// the third facet
		link = links.get(3);
		assertEquals("YEAR", link.getType());

		counts = link.getLinks();

		count = counts.get(0);
		assertEquals(11, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=YEAR%3A%22-1453%22",
			count.getUrl());
		assertEquals("&qf=YEAR%3A%22-1453%22", count.getParam());

		count = counts.get(1);
		assertEquals(11, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=YEAR%3A1453",
			count.getUrl());
		assertEquals("&qf=YEAR%3A1453", count.getParam());
	}

	@Test
	public void testNegativeYearFacets() {
		List<Facet> facetFields = new ArrayList<Facet>();
		Facet facet = new Facet();
		facet.name = "YEAR";
		facet.fields = new ArrayList<LabelFrequency>();
		facet.fields.add(new LabelFrequency("-1453", 11));
		facet.fields.add(new LabelFrequency("-1454", 11));
		facet.fields.add(new LabelFrequency("1453", 11));
		facetFields.add(facet);

		Query query = new Query("*:*").addRefinement("YEAR:\"-1453\"").addRefinement("YEAR:-1453");
		List<FacetQueryLinks> facetGroups = FacetQueryLinksImpl.createDecoratedFacets(facetFields, query);

		assertEquals(1, facetGroups.size());

		List<FacetCountLink> facets = facetGroups.get(0).getLinks();
		assertEquals(3, facets.size());
		assertEquals("/portal/search.html?query=*%3A*&rows=12", facets.get(0).getUrl());
		System.out.println(facets.get(0).getValue());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=YEAR%3A%22-1453%22&qf=YEAR%3A%22-1454%22",
			facets.get(1).getUrl());
		System.out.println(facets.get(1).getValue());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=YEAR%3A%22-1453%22&qf=YEAR%3A1453",
			facets.get(2).getUrl());
		System.out.println(facets.get(2).getValue());
	}
}
