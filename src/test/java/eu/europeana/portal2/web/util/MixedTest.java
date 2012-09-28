package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import java.text.MessageFormat;

import org.junit.Test;

public class MixedTest {

	String UNION_FACETS_FORMAT = "'{'!ex={0}'}'{0}";

	@Test
	public void test() {
		assertEquals("{!ex=COUNTRY}COUNTRY", MessageFormat.format(UNION_FACETS_FORMAT, "COUNTRY"));
	}

}
