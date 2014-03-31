package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ElementFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNoNamespaces() {
		Element element;
		element = ElementFactory.createElement("fake-title");
		assertNull(element);

		element = ElementFactory.createElement("faketitle");
		assertNull(element);

		element = ElementFactory.createElement("fake/title");
		assertNull(element);
	}

	@Test
	public void testFakeNamespace() {
		Element element;
		element = ElementFactory.createElement("fake:title");
		assertNull(element);
	}
}
