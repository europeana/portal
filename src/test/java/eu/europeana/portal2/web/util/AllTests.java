package eu.europeana.portal2.web.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Suite.class)
@SuiteClasses({ FieldInfoTest.class, FullBeanDecoratorTest.class,
		QueryUtilTest.class, SchemaOrMappingInPortalTest.class,
		SchemaOrMappingTest.class, SearchFilterTest.class, WebUtilTest.class })
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class AllTests {

}
