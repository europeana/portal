package eu.europeana.portal2.web.controllers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Suite.class)
@SuiteClasses({ BlogTest.class, ImageExtractingTest.class,
		ObjectControllerTest.class, PinterestTest.class,
		SearchControllerTest.class, SuggestionControllerTest.class })
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class AllTests {

}
