package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class SearchControllerTest {

	@Test
	public void test() {
		SearchController controller = new SearchController();
		String[] qf = new String[]{""};
		try {
			/*
			ModelAndView modelAndView = controller.searchHtml("ebook", "", "", "", "", "", qf, "", "", 10, 12, 
					"portal",
					null, null, null);
			assertEquals("search", modelAndView.getViewName());
			assertNull(modelAndView.getView());
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exception happened");
		}
	}

}
