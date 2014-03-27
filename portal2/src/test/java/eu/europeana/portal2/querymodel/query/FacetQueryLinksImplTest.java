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
@ContextConfiguration({"/portal2-test.xml"})
public class FacetQueryLinksImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
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

		Query query = new Query("*:*");
		List<FacetQueryLinks> links = FacetQueryLinksImpl.createDecoratedFacets(facetFields, query);

		assertEquals(2, links.size());

		// the first facet
		FacetQueryLinks link = links.get(0);
		assertEquals("COUNTRY", link.getType());

		List<FacetCountLink> counts = link.getLinks();

		assertEquals(2, counts.size());
		FacetCountLink count = counts.get(0);
		assertEquals(23, count.getCount());
		assertEquals("/portal/search.html?query=*%3A*&rows=12&qf=COUNTRY%3A%22united+kingdom%22", count.getUrl());

		count = counts.get(1);
		assertEquals(13, count.getCount());
		assertEquals("/portal/search.html?query=*%3A*&rows=12&qf=COUNTRY%3A%22the+netherlands%22", count.getUrl());

		// the second facet
		link = links.get(1);
		assertEquals("RIGHTS", link.getType());

		counts = link.getLinks();

		count = counts.get(0);
		assertEquals(23, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=RIGHTS%3Ahttp%3A%2F%2Fwww.europeana.eu%2Frights%2Frr-f%2F*",
			count.getUrl());

		count = counts.get(1);
		assertEquals(13, count.getCount());
		assertEquals(
			"/portal/search.html?query=*%3A*&rows=12&qf=RIGHTS%3Ahttp%3A%2F%2Fwww.europeana.eu%2Frights%2Frr-r%2F*",
			count.getUrl());
	}

}
