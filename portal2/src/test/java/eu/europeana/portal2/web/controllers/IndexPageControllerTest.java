package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class IndexPageControllerTest {

	@Test
	public void testRandomThings() {
		for (int i = 0, max = 100; i < max; i++) {
			assertEquals(RandomUtils.nextInt(1), 0);
			assertNotSame(RandomUtils.nextInt(2), 2);
		}
	}

}
