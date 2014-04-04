package eu.europeana.portal2.web.model.facets;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class FacetTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUsage() {
		Facet facet;

		facet = new Facet();
		facet.name = "RIGHTS";
		assertEquals("RIGHTS", facet.getName());
		assertEquals("RIGHTS", facet.name);

		assertNotNull(facet.getFields());
		assertNotNull(facet.fields);
		assertEquals(0, facet.getFields().size());
		facet.fields = new ArrayList<LabelFrequency>();
		facet.fields.add(new LabelFrequency("http://www.europeana.eu/rights/rr-f/", 23));
		facet.fields.add(new LabelFrequency("http://www.europeana.eu/rights/rr-r/", 13));
		assertEquals(2, facet.getFields().size());

		assertFalse(facet.selected);
		assertFalse(facet.isSelected());
		facet.selected = true;
		assertTrue(facet.selected);
		assertTrue(facet.isSelected());
	}

}
