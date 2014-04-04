package eu.europeana.portal2.web.model.facets;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LabelFrequencyTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstuctors() {
		LabelFrequency labelFrequency;

		labelFrequency = new LabelFrequency();
		assertEquals(0, labelFrequency.getCount());
		assertEquals(null, labelFrequency.getLabel());
		assertEquals("LabelFrequency [label=null, count=0]", labelFrequency.toString());

		labelFrequency = new LabelFrequency();
		labelFrequency.setLabel("Szentendre");
		labelFrequency.setCount(23);
		assertEquals(23, labelFrequency.getCount());
		assertEquals("Szentendre", labelFrequency.getLabel());
		assertEquals("LabelFrequency [label=Szentendre, count=23]", labelFrequency.toString());

		labelFrequency = new LabelFrequency();
		labelFrequency.label = "Szentendre";
		labelFrequency.count = 23;
		assertEquals(23, labelFrequency.getCount());
		assertEquals("Szentendre", labelFrequency.getLabel());
		assertEquals("LabelFrequency [label=Szentendre, count=23]", labelFrequency.toString());

		new LabelFrequency("Szentendre", 23);
		assertEquals(23, labelFrequency.getCount());
		assertEquals("Szentendre", labelFrequency.getLabel());
		assertEquals("LabelFrequency [label=Szentendre, count=23]", labelFrequency.toString());
	}
}