package eu.europeana.portal2.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.europeana.portal2.web.controllers.utils.ApiFulldocParser;

public class WebUtilTest {

	@Test
	public void test() {
		//WebUtil.requestApiSession("http://localhost:8080/api2", "api2demo", "verysecret");
		WebUtil.getApiSession("http://localhost:8080/api2", "api2demo", "verysecret");
		ApiFulldocParser parser = new ApiFulldocParser("http://localhost:8080/api2", "api2demo", "verysecret", null);
		parser.getFullBean("91627", "7E8AAB01E1C2AD825615C3153CF82C1B2D39B224");
	}

}
