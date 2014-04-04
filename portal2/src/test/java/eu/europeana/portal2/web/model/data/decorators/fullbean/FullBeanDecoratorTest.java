package eu.europeana.portal2.web.model.data.decorators.fullbean;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
import eu.europeana.corelib.utils.DateUtils;
import eu.europeana.portal2.web.model.json.Json2FullBeanConverter;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanDecorator;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/portal2-test.xml"})
public class FullBeanDecoratorTest {

	private FullBeanDecorator decorator;
	private FullBean bean;

	@Before
	public void setUp() throws Exception {
		try {
			String fileName = "src/test/resources/json/fullbeans/2026110.Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339.json";
			String json = FileUtils.readFileToString(new File(fileName));
			Json2FullBeanConverter converter = new Json2FullBeanConverter(json);
			bean = converter.extractFullBean();
			decorator = new FullBeanDecorator(bean, "en");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Test
	public void testConstructor() {
		assertNotNull(decorator);
	}

	@Test
	public void testFullbeanFields() {
		assertEquals("/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				decorator.getAbout());
		assertArrayEquals(new String[]{"Ex libris - Otto und Lilli Wolfskehl"}, decorator.getTitle());
		assertArrayEquals(new String[]{"hu"}, decorator.getLanguage());
		assertNull(decorator.getYear());
		assertNull(decorator.getProvider());
		assertEquals(DocType.IMAGE, decorator.getType());
		assertEquals(0, decorator.getEuropeanaCompleteness());
		assertNull(decorator.getId());
		assertEquals(
			"http://www.europeana.eu/portal/record/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339.html",
			decorator.getEdmLandingPage());
		assertArrayEquals(new String[]{"2026110_Ag_EU_PartagePlus_1023"},
			decorator.getEuropeanaCollectionName());
		assertNull(decorator.getTimestamp());
		assertNull(decorator.getCountry());
		assertNull(decorator.getUserTags());
		assertEquals("2014-02-01T15:34:49.462Z", DateUtils.format(decorator.getTimestampCreated()));
		assertEquals("2014-02-01T15:34:49.462Z", DateUtils.format(decorator.getTimestampUpdated()));
	}

	@Test
	public void testShortcutEdmFields() {
		assertEquals("Wenig, Bernhard", decorator.getPostAuthor());

		// expected = new String[]{"hungary"};
		assertArrayEquals(new String[]{"hungary"}, decorator.getEdmCountry());

		assertArrayEquals(new String[]{"Museum of Applied Arts, Budapest"}, 
				decorator.getEdmDataProvider());

		assertArrayEquals(new String[]{"hu"}, decorator.getEdmLanguage());
		assertNull(decorator.getEdmPlaceLatitude());
		assertNull(decorator.getEdmPlaceLongitude());
		assertEquals(Arrays.asList("http://www.europeana.eu/rights/rr-f/"), decorator.getEdmRights());
		assertEquals("IMAGE", decorator.getEdmType());
	}

	@Test
	public void testShortcutDcFields() {
		assertNull(decorator.getDcDate());
		//assertArrayEquals(new String[]{"hungary"}, decorator.getDcDate());

		assertNull(decorator.getDcDescription());
		//assertArrayEquals(new String[]{"hungary"}, decorator.getDcDescription());

		assertNull(decorator.getDcDescriptionCombined());
		// assertEquals("IMAGE", decorator.getDcDescriptionCombined());

		assertArrayEquals(new String[]{"klise"}, decorator.getDcFormat());

		assertNull(decorator.getDcLanguage());
		// assertArrayEquals(new String[]{"hungary"}, decorator.getDcLanguage());

		assertNull(decorator.getDcRights());
		// assertArrayEquals(new String[]{"hungary"}, decorator.getDcRights());

		assertNull(decorator.getDcSubject());
		// assertArrayEquals(new String[]{"hungary"}, decorator.getDcSubject());

		assertArrayEquals(new String[]{"Ex libris - Otto und Lilli Wolfskehl"}, decorator.getDcTitle());
		assertEquals("Ex libris - Otto und Lilli Wolfskehl", decorator.getDcTitleCombined());
		
		String[] dcType = new String[]{"http://partage.vocnet.org/part00042",
				"http://partage.vocnet.org/part00141",
				"http://partage.vocnet.org/part00353"};
		assertArrayEquals(dcType, decorator.getDcType());

		assertNull(decorator.getDctermsHasVersion());
		// assertArrayEquals(new String[]{"hungary"}, decorator.getDctermsHasVersion());

		assertNull(decorator.getDctermsIsFormatOf());
		// assertArrayEquals(new String[]{"hungary"}, decorator.getDctermsIsFormatOf());
	}

}
