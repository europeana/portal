package eu.europeana.portal2.web.presentation.model.data.submodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldCleanerTest {

	@Test
	public void test() {
		assertNull(FieldCleaner.clean(null));

		assertEquals("line 1<br/>line 2", FieldCleaner.clean("line 1&lt;br&gt;line 2"));
		assertEquals("line 1<br/>line 2", FieldCleaner.clean("line 1&lt;BR&gt;line 2"));

		assertEquals("line 1<i>line 2</i>", FieldCleaner.clean("line 1&lt;i&gt;line 2&lt;/i&gt;"));
		assertEquals("line 1<i>line 2</i>", FieldCleaner.clean("line 1&lt;I&gt;line 2&lt;/I&gt;"));

		assertEquals("line 1<p>line 2</p>", FieldCleaner.clean("line 1&lt;p&gt;line 2&lt;/p&gt;"));
		assertEquals("line 1<p>line 2</p>", FieldCleaner.clean("line 1&lt;P&gt;line 2&lt;/P&gt;"));

		assertEquals("line 1 line 2", FieldCleaner.clean("line 1 &lt;li&gt;line 2&lt;/li&gt;"));
		assertEquals("line 1 line 2", FieldCleaner.clean("line 1 &lt;LI&gt;line 2&lt;/LI&gt;"));

		assertEquals("line 1&rarr;line 2", FieldCleaner.clean("line 1>>line 2"));

		assertEquals("line 1\"line 2", FieldCleaner.clean("line 1&amp;quot;line 2"));
	}
}
