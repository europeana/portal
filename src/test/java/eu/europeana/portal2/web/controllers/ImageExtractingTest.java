package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import eu.europeana.portal2.web.controllers.utils.RSSImage;
import eu.europeana.portal2.web.controllers.utils.RSSImageExtractor;

public class ImageExtractingTest {

	@Test
	public void test() {

		String blog = "<p><a href=\"http://www.europeana.eu/portal/record/9200103/BB8B037F8D49A1A10BDB5C5C18C861005BDBE9F3.html\" target=\"_blank\"><img class=\"alignnone size-full wp-image-7728\" title=\"Antoine-Jean Gros, 'Napoleon Bonaparte : bataille d'Arcole', French National Library\" src=\"http://blog.europeana.eu/wp-content/uploads/2012/08/battle-of-arcole.jpg\" alt=\"Antoine-Jean Gros, 'Napoleon Bonaparte : bataille d'Arcole', French National Library\" width=\"182\" height=\"283\" /></a>";
		// testing to find where the utf-8 failure in hudson is
		// <a href=\"http://www.europeana.eu/portal/record/9200103/B67F2D7063672BEDC3CAD554F6494BA84909B26C.html\" target=\"_blank\">   <img class=\"alignnone  wp-image-7727\" title=\"'Napoleon Bonaparte, sketched at Saint Helena by a British Officier in May last', French National Library\" src=\"http://blog.europeana.eu/wp-content/uploads/2012/08/portraitsketch.jpg\" alt=\"'Napoleon Bonaparte, sketched at Saint Helena by a British Officier in May last', French National Library\" width=\"182\" height=\"283\" /></a></p>";
		
		List<RSSImage> images = RSSImageExtractor.extractImages(blog);
		assertEquals(2, images.size());

		assertEquals("http://blog.europeana.eu/wp-content/uploads/2012/08/battle-of-arcole.jpg", images.get(0).getSrc());
		assertEquals("Antoine-Jean Gros, 'Napoleon Bonaparte : bataille d'Arcole', French National Library", images.get(0).getTitle());

		assertEquals("http://blog.europeana.eu/wp-content/uploads/2012/08/portraitsketch.jpg", images.get(1).getSrc());
		assertEquals("'Napoleon Bonaparte, sketched at Saint Helena by a British Officier in May last', French National Library", images.get(1).getTitle());
		
	}
}
