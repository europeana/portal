package eu.europeana;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComputeTest {

	@Test
	public void test() {
		assertEquals("2+2 should be 4", 4, Compute.add(2, 2));
	}

}
