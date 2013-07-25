package eu.europeana.portal2.web.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FullBeanDecoratorTest {

	@Test
	public void test() {
		List<String> descriptions = Arrays.asList(new String[]{"a\nb"});
		for (int i=0, l=descriptions.size(); i < l; i++) {
			descriptions.set(i, descriptions.get(i).replace("\n", "<br/>\n"));
		}
		assertEquals("a<br/>\nb", descriptions.get(0));
	}
}
