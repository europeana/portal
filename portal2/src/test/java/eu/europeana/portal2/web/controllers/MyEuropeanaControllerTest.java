package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.corelib.db.entity.relational.SavedSearchImpl;
import eu.europeana.corelib.definitions.db.entity.relational.SavedSearch;
import eu.europeana.portal2.web.controllers.user.MyEuropeanaController;

public class MyEuropeanaControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCleanSavedSearch() {
		MyEuropeanaController controller = new MyEuropeanaController();
		SavedSearch savedSearch = new SavedSearchImpl();

		savedSearch.setQuery("piepel");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("piepel", savedSearch.getQuery());

		savedSearch.setQuery("piepel&qf=peach");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("piepel&qf=peach", savedSearch.getQuery());

		savedSearch.setQuery("piepel&qf=text:peach");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("piepel&qf=text%3Apeach", savedSearch.getQuery());

		savedSearch.setQuery("piepel&qf=who:\"Buffet-Crampon Cie\"");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("piepel&qf=who%3A%22Buffet-Crampon+Cie%22", savedSearch.getQuery());

		savedSearch.setQuery("piepel&qf=who:\"Buffet-Crampon & Cie\"");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("piepel&qf=who%3A%22Buffet-Crampon+%26+Cie%22", savedSearch.getQuery());

		savedSearch.setQuery("*:*&qf=DATA_PROVIDER:\"Tyne & Wear Archives & Museums\"&qf=TYPE:IMAGE");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("*%3A*&qf=DATA_PROVIDER%3A%22Tyne+%26+Wear+Archives+%26+Museums%22&qf=TYPE%3AIMAGE", savedSearch.getQuery());

		savedSearch.setQuery("*:*&qf=TYPE:IMAGE&qf=TYPE:TEXT&qf=TYPE:SOUND&qf=TYPE:3D&qf=TYPE:VIDEO&qf=LANGUAGE:de&qf=LANGUAGE:fr&qf=LANGUAGE:mul&qf=LANGUAGE:nl&qf=LANGUAGE:es&qf=LANGUAGE:en&qf=LANGUAGE:sv&qf=LANGUAGE:it");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("*%3A*&qf=TYPE%3AIMAGE&qf=TYPE%3ATEXT&qf=TYPE%3ASOUND&qf=TYPE%3A3D&qf=TYPE%3AVIDEO&qf=LANGUAGE%3Ade&qf=LANGUAGE%3Afr&qf=LANGUAGE%3Amul&qf=LANGUAGE%3Anl&qf=LANGUAGE%3Aes&qf=LANGUAGE%3Aen&qf=LANGUAGE%3Asv&qf=LANGUAGE%3Ait", savedSearch.getQuery());

		savedSearch.setQuery("what%3ACom%C3%A9die+Fran%C3%A7aise+OR+dc_type%3ACom%C3%A9die+Fran%C3%A7aise");
		controller.cleanSavedSearch(savedSearch);
		assertEquals("what%3ACom%C3%A9die+Fran%C3%A7aise+OR+dc_type%3ACom%C3%A9die+Fran%C3%A7aise", savedSearch.getQuery());
	}
}
