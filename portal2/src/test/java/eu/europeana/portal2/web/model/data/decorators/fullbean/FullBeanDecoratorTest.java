package eu.europeana.portal2.web.model.data.decorators.fullbean;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.definitions.solr.entity.WebResource;
import eu.europeana.corelib.solr.entity.WebResourceImpl;
import eu.europeana.corelib.utils.DateUtils;
import eu.europeana.portal2.web.model.json.Json2FullBeanConverter;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanDecorator;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual.ContextualItemDecorator;

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

		decorator = new FullBeanDecorator(bean, "en");
		assertNotNull(decorator);

		decorator = new FullBeanDecorator(bean);
		assertNotNull(decorator);
	}

	@Test
	public void testFullbeanFields() {
		assertEquals("/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				decorator.getAbout());
		assertArrayEquals(new String[]{"Ex libris - Otto und Lilli Wolfskehl"}, decorator.getTitle());
		assertEquals("Ex libris - Otto und Lilli Wolfskehl", decorator.getPostTitle());

		assertArrayEquals(new String[]{"hu"}, decorator.getLanguage());
		assertNull(decorator.getYear());
		assertNull(decorator.getProvider());
		assertEquals(DocType.IMAGE, decorator.getType());
		assertEquals("IMAGE", decorator.getEdmType());
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
		assertEquals("2014-02-01T15:34:49.462Z", decorator.getTimestampCreatedString());

		Date timestampCreated = decorator.getTimestampCreated();
		decorator.setTimestampCreated(null);
		assertEquals(new Date(0), decorator.getTimestampCreated());
		assertEquals("1970-01-01T00:00:00.000Z", DateUtils.format(decorator.getTimestampCreated()));
		assertEquals("1970-01-01T00:00:00.000Z", decorator.getTimestampCreatedString());
		decorator.setTimestampCreated(timestampCreated);

		assertEquals("2014-02-01T15:34:49.462Z", DateUtils.format(decorator.getTimestampUpdated()));
		assertEquals("2014-02-01T15:34:49.462Z", decorator.getTimestampUpdatedString());

		Date timestampUpdated = decorator.getTimestampUpdated();
		decorator.setTimestampUpdated(null);
		assertEquals(new Date(0), decorator.getTimestampUpdated());
		assertEquals("1970-01-01T00:00:00.000Z", DateUtils.format(decorator.getTimestampUpdated()));
		assertEquals("1970-01-01T00:00:00.000Z", decorator.getTimestampUpdatedString());
		decorator.setTimestampUpdated(timestampUpdated);

		assertEquals("http://www.europeana.eu/resolve/record/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				decorator.getCannonicalUrl());

		assertNotNull(decorator.getProvidedCHOs());
		assertEquals(1, decorator.getProvidedCHOs().size());
		assertEquals("/item/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				decorator.getProvidedCHOs().get(0).getAbout());

		assertFalse(decorator.isOptedOut());
		assertFalse(decorator.getOptedOut());
	}

	@Test
	public void testShortcutEdmFields() {
		assertEquals("Wenig, Bernhard", decorator.getPostAuthor());

		// expected = new String[]{"hungary"};
		assertArrayEquals(new String[]{"hungary"}, decorator.getEdmCountry());

		assertArrayEquals(new String[]{"Museum of Applied Arts, Budapest"},
				decorator.getEdmDataProvider());

		assertArrayEquals(new String[]{"hu"}, decorator.getEdmLanguage());
		assertNull(bean.getPlaces().get(0).getLatitude());
		assertNull(decorator.getPlaces().get(0).getLatitude());
		assertNotNull(bean.getPlaces().get(1).getLatitude());
		assertNotNull(decorator.getPlaces().get(1).getLatitude());

		assertNotNull(decorator.getEdmPlaceLatitude());
		assertArrayEquals(new Float[]{51.05089F}, decorator.getEdmPlaceLatitude());
		assertNotNull(decorator.getEdmPlaceLongitude());
		assertArrayEquals(new Float[]{13.73832F}, decorator.getEdmPlaceLongitude());
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

	@Test
	public void testGetEuropeanaProxy() {
		Proxy proxy = decorator.getEuropeanaProxy();
		assertNotNull(proxy);
		assertEquals("/proxy/europeana/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				proxy.getAbout());

		List<Proxy> proxies = decorator.getProvidedProxies();
		assertNotNull(proxies);
		assertEquals(1, proxies.size());
		assertEquals("/proxy/provider/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				proxies.get(0).getAbout());

		assertTrue(decorator.isSingletonProxy());
	}

	@Test
	public void testGetProvidedProxies() {
		List<Proxy> proxies = decorator.getProvidedProxies();
		assertNotNull(proxies);
		assertEquals(1, proxies.size());
		assertEquals("/proxy/provider/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				proxies.get(0).getAbout());

		Proxy proxy = decorator.getEuropeanaProxy();
		assertNotNull(proxy);
		assertEquals("/proxy/europeana/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				proxy.getAbout());

		assertTrue(decorator.isSingletonProxy());
	}

	@Test
	public void testIsSingletonProxy() {
		assertTrue(decorator.isSingletonProxy());

		List<Proxy> proxies = decorator.getProvidedProxies();
		assertNotNull(proxies);
		assertEquals(1, proxies.size());
		assertEquals("/proxy/provider/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				proxies.get(0).getAbout());

		Proxy proxy = decorator.getEuropeanaProxy();
		assertNotNull(proxy);
		assertEquals("/proxy/europeana/2026110/Partage_Plus_ProvidedCHO_Museum_of_Applied_Arts__Budapest_7339", 
				proxy.getAbout());
	}

	@Test
	public void testGetDecoratedAgents() {
		assertNotNull(decorator.getAgents());
		assertNotNull(decorator.getDecoratedAgents());
		assertEquals(1, decorator.getAgents().size());
		assertEquals(1, decorator.getDecoratedAgents().size());
		assertEquals(1, decorator.getNumberOfUnreferencedAgents());

		bean.setAgents(null);
		decorator = new FullBeanDecorator(bean, "en");
		assertNull(decorator.getAgents());
		assertNotNull(decorator.getDecoratedAgents());
		assertEquals(0, decorator.getDecoratedAgents().size());
	}

	@Test
	public void testGetDecoratedConcepts() {
		assertNotNull(decorator.getConcepts());
		assertNotNull(decorator.getDecoratedConcepts());
		assertEquals(4, decorator.getConcepts().size());
		assertEquals(4, decorator.getDecoratedConcepts().size());
		assertEquals(4, decorator.getNumberOfUnreferencedConcepts());

		bean.setConcepts(null);
		decorator = new FullBeanDecorator(bean, "en");
		assertNull(decorator.getConcepts());
		assertNotNull(decorator.getDecoratedConcepts());
		assertEquals(0, decorator.getDecoratedConcepts().size());
	}

	@Test
	public void testGetDecoratedPlaces() {
		assertNotNull(decorator.getPlaces());
		assertNotNull(decorator.getDecoratedPlaces());
		assertEquals(2, decorator.getPlaces().size());
		assertEquals(2, decorator.getDecoratedPlaces().size());
		assertEquals(2, decorator.getNumberOfUnreferencedPlaces());

		bean.setPlaces(null);
		decorator = new FullBeanDecorator(bean, "en");
		assertNull(decorator.getPlaces());
		assertNotNull(decorator.getDecoratedPlaces());
		assertEquals(0, decorator.getDecoratedPlaces().size());
	}

	@Test
	public void testGetDecoratedTimespans() {
		assertNotNull(decorator.getTimespans());
		assertNotNull(decorator.getDecoratedTimespans());
		assertEquals(2, decorator.getDecoratedTimespans().size());
		assertEquals(2, decorator.getNumberOfUnreferencedTimespans());

		bean.setTimespans(null);
		decorator = new FullBeanDecorator(bean, "en");
		assertNull(decorator.getTimespans());
		assertNotNull(decorator.getDecoratedTimespans());
		assertEquals(0, decorator.getDecoratedTimespans().size());
	}

	@Test
	public void testGetAgentByURI() {
		Agent item;
		item = decorator.getAgentByURI("http://dbpedia.org/resource/Wols");
		assertNotNull(item);
		assertEquals("http://dbpedia.org/resource/Wols", item.getAbout());

		item = decorator.getAgentByURI("http://example.com/fake");
		assertNull(item);
	}

	@Test
	public void testGetConceptByURI() {
		Concept item;
		item = decorator.getConceptByURI("http://partage.vocnet.org/part00042");
		assertNotNull(item);
		assertEquals("http://partage.vocnet.org/part00042", item.getAbout());

		item = decorator.getConceptByURI("http://partage.vocnet.org/part00141");
		assertNotNull(item);
		assertEquals("http://partage.vocnet.org/part00141", item.getAbout());

		item = decorator.getConceptByURI("http://partage.vocnet.org/part00353");
		assertNotNull(item);
		assertEquals("http://partage.vocnet.org/part00353", item.getAbout());

		item = decorator.getConceptByURI("http://partage.vocnet.org/part00575");
		assertNotNull(item);
		assertEquals("http://partage.vocnet.org/part00575", item.getAbout());

		item = decorator.getConceptByURI("http://example.com/fake");
		assertNull(item);
	}

	@Test
	public void testGetPlaceByURI() {
		Place item;
		item = decorator.getPlaceByURI("http://sws.geonames.org/2921044/");
		assertNotNull(item);
		assertEquals("http://sws.geonames.org/2921044/", item.getAbout());

		item = decorator.getPlaceByURI("http://sws.geonames.org/2935022/");
		assertNotNull(item);
		assertEquals("http://sws.geonames.org/2935022/", item.getAbout());

		item = decorator.getPlaceByURI("http://example.com/fake");
		assertNull(item);
	}

	@Test
	public void testGetTimespanByURI() {
		Timespan item;
		item = decorator.getTimespanByURI("http://semium.org/time/20xx_1_third");
		assertNotNull(item);
		assertEquals("http://semium.org/time/20xx_1_third", item.getAbout());

		item = decorator.getTimespanByURI("http://semium.org/time/2001");
		assertNotNull(item);
		assertEquals("http://semium.org/time/2001", item.getAbout());

		item = decorator.getTimespanByURI("http://example.com/fake");
		assertNull(item);
	}

	@Test
	public void testGetWebResourceByURI() {
		WebResource item;
		item = decorator.getWebResourceByUrl("http://gyujtemeny.imm.hu/web/kis/MLT_1687_I_008_a.jpg");
		assertNotNull(item);
		assertEquals("http://gyujtemeny.imm.hu/web/kis/MLT_1687_I_008_a.jpg", item.getAbout());

		item = decorator.getWebResourceByUrl("http://gyujtemeny.imm.hu/gyujtemeny/ex-libris-otto-und-lilli-wolfskehl/7339");
		assertNotNull(item);
		assertEquals("http://gyujtemeny.imm.hu/gyujtemeny/ex-libris-otto-und-lilli-wolfskehl/7339", item.getAbout());

		item = decorator.getWebResourceByUrl("http://example.com/fake");
		assertNull(item);

		List<WebResource> webResources = new ArrayList<WebResource>();
		WebResource item2 = new WebResourceImpl();
		item2.setAbout("http://example.com/fake");
		webResources.add(item2);
		bean.getEuropeanaAggregation().setWebResources(webResources);
		decorator = new FullBeanDecorator(bean, "en");

		item = decorator.getWebResourceByUrl("http://example.com/fake");
		assertNotNull(item);
		assertEquals("http://example.com/fake", item.getAbout());
		assertEquals(item2, item);
	}

	@Test
	public void testGetWebResourceEdmRightsByURI() {
		String item;
		item = decorator.getWebResourceEdmRightsByUrl("http://example.com/fake");
		assertNull(item);

		item = decorator.getWebResourceEdmRightsByUrl("http://gyujtemeny.imm.hu/web/kis/MLT_1687_I_008_a.jpg");
		assertNull(item);

		List<WebResource> webResources = new ArrayList<WebResource>();
		WebResource webResource = new WebResourceImpl();
		webResource.setAbout("http://example.com/fake");
		Map<String, List<String>> webResourceEdmRights = new HashMap<String, List<String>>();
		webResourceEdmRights.put("def", Arrays.asList(new String[]{"http://www.europeana.eu/rights/rr-f/"}));
		webResource.setWebResourceEdmRights(webResourceEdmRights);
		webResources.add(webResource);
		bean.getEuropeanaAggregation().setWebResources(webResources);
		decorator = new FullBeanDecorator(bean, "en");

		item = decorator.getWebResourceEdmRightsByUrl("http://example.com/fake");
		assertNotNull(item);
		assertEquals("http://www.europeana.eu/rights/rr-f/", item);

		webResourceEdmRights.put("def", Arrays.asList(new String[]{}));
		bean.getEuropeanaAggregation().setWebResources(webResources);
		decorator = new FullBeanDecorator(bean, "en");
		item = decorator.getWebResourceEdmRightsByUrl("http://example.com/fake");
		assertNull(item);

		webResourceEdmRights.put("def", Arrays.asList(new String[]{""}));
		bean.getEuropeanaAggregation().setWebResources(webResources);
		decorator = new FullBeanDecorator(bean, "en");
		item = decorator.getWebResourceEdmRightsByUrl("http://example.com/fake");
		assertNull(item);

		webResourceEdmRights.put("def", Arrays.asList(new String[]{null}));
		bean.getEuropeanaAggregation().setWebResources(webResources);
		decorator = new FullBeanDecorator(bean, "en");
		item = decorator.getWebResourceEdmRightsByUrl("http://example.com/fake");
		assertNull(item);
	}

	@Test
	public void testGetContextualConnections() {
		ContextualItemDecorator entity;

		entity = decorator.getContextualConnections(FullBeanDecorator.ContextualEntity.ALL,
				"http://dbpedia.org/resource/Wols", null);
		assertEquals(FullBeanDecorator.ContextualEntity.AGENT, entity.getEntityType());

		entity = decorator.getContextualConnections(FullBeanDecorator.ContextualEntity.ALL,
				"http://partage.vocnet.org/part00042", null);
		assertEquals(FullBeanDecorator.ContextualEntity.CONCEPT, entity.getEntityType());

		entity = decorator.getContextualConnections(FullBeanDecorator.ContextualEntity.ALL,
				"http://sws.geonames.org/2921044/", null);
		assertEquals(FullBeanDecorator.ContextualEntity.PLACE, entity.getEntityType());

		entity = decorator.getContextualConnections(FullBeanDecorator.ContextualEntity.ALL,
				"http://semium.org/time/20xx_1_third", null);
		assertEquals(FullBeanDecorator.ContextualEntity.TIMESPAN, entity.getEntityType());

		entity = decorator.getContextualConnections(FullBeanDecorator.ContextualEntity.ALL,
				"http://example.com/fake", null);
		assertNull(entity);
	}
}
