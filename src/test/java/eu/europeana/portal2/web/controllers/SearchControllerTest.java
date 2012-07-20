package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import org.apache.commons.lang.ArrayUtils;
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
	
	@Test
	public void refinementTest() {
		String[] numbers = new String[]{"1", "2", "3"};
		String[] words = new String[]{"one", "two", "three"};
		
		String[] all = (String[]) ArrayUtils.addAll(numbers, words);
		assertEquals(3, numbers.length);
		assertEquals(6, all.length);
		assertArrayEquals(new String[]{"1", "2", "3", "one", "two", "three"}, all);
		testFunction(all);
	}
	
	private void testFunction(String... strings) {
		// do nothing
	}

}
