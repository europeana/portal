package eu.europeana.portal2.web.model.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.FullBean;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class Json2FullBeanConverterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExtraction() {
		FullBean bean = null;
		try {
			String fileName = "src/test/resources/json/fullbeans/2023008.CFD8A333875F65B1D5D4C60D194F24A5C1B6DAC2.json";
			String json = FileUtils.readFileToString(new File(fileName));
			Json2FullBeanConverter converter = new Json2FullBeanConverter(json);
			bean = converter.extractFullBean();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		assertNotNull(bean);
		assertEquals("/2023008/CFD8A333875F65B1D5D4C60D194F24A5C1B6DAC2", bean.getAbout());
		assertEquals(DocType.IMAGE, bean.getType());
		assertEquals(1, bean.getTitle().length);
		assertEquals("Compositie", bean.getTitle()[0]);

		assertEquals(1, bean.getEuropeanaCollectionName().length);
		assertEquals("2023008_Ag_BE_Elocal_ProvincieLimburg", bean.getEuropeanaCollectionName()[0]);

		assertEquals(7, bean.getEuropeanaCompleteness());

		assertNotNull(bean.getProxies());
		assertEquals(2, bean.getProxies().size());

		assertFalse(bean.getProxies().get(0).isEuropeanaProxy());
		assertEquals("/proxy/provider/2023008/CFD8A333875F65B1D5D4C60D194F24A5C1B6DAC2", bean.getProxies().get(0).getAbout());
		assertTrue(bean.getProxies().get(1).isEuropeanaProxy());
		assertEquals("/proxy/europeana/2023008/CFD8A333875F65B1D5D4C60D194F24A5C1B6DAC2", bean.getProxies().get(1).getAbout());

		assertNotNull(bean.getAggregations());
		assertEquals(1, bean.getAggregations().size());

		assertNotNull(bean.getTimespans());
		assertEquals(2, bean.getTimespans().size());

		assertNotNull(bean.getProvidedCHOs());
		assertEquals(1, bean.getProvidedCHOs().size());

		assertNotNull(bean.getEuropeanaAggregation());
		assertNull(bean.getAgents());
		assertNull(bean.getConcepts());
		assertNull(bean.getPlaces());
	}

	@Test
	public void testFileBasedExtraction() throws Exception {
		FullBean bean1 = null;
		FullBean bean2 = null;
		try {
			String fileName = "src/test/resources/json/fullbeans/2023008.CFD8A333875F65B1D5D4C60D194F24A5C1B6DAC2.json";
			File file = new File(fileName);
			String json = FileUtils.readFileToString(file);
			Json2FullBeanConverter converter;

			converter = new Json2FullBeanConverter(json);
			bean1 = converter.extractFullBean();

			converter = new Json2FullBeanConverter(file);
			bean2 = converter.extractFullBean();

			assertEquals(bean1.getAbout(), bean2.getAbout());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
