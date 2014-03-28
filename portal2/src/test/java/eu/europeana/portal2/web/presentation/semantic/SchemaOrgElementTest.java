package eu.europeana.portal2.web.presentation.semantic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class SchemaOrgElementTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructors() {
		SchemaOrgElement element1 = new SchemaOrgElement("dc:title");
		assertNotNull(element1);
		assertNotNull(element1.getElement());
		assertNull(element1.getEdmElement());
		assertEquals("dc:title", element1.getElement().getQualifiedName());
		assertEquals(0, element1.getParents().size());

		SchemaOrgElement element2 = new SchemaOrgElement("dc:title", new String[] {"dc:creator"});
		assertEquals("dc:title", element2.getElement().getQualifiedName());
		assertEquals(1, element2.getParents().size());
		assertEquals("dc:creator", element2.getParents().get(0).getQualifiedName());
	}
}
