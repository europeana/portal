package eu.europeana.portal2.selenium.page;

import org.openqa.selenium.WebDriver;

import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;

import java.io.File;
import java.io.FileReader;
import java.util.prefs.Preferences;


import org.ini4j.Ini;
import org.ini4j.IniPreferences;


public class IndexPage extends Portal2Page {
	
	
    static String url;
    static String urlSuffix;
    static String port;
	static {
		try{
			Ini ini = new Ini( new FileReader(new File("conf.ini")) );
			Preferences prefs = new IniPreferences( ini );
			
			url			= prefs.node("server").get("url",        "http://www.europeana.eu/");
			urlSuffix	= prefs.node("server").get("url-suffix", "/portal");
			port		= prefs.node("server").get("port",       "");
		}
		catch(Exception e){
			url			= "http://www.europeana.eu/";
			urlSuffix	= "/portal";
			port		= "";
		}
		
	}
	
	// PAGE defaults to "http://www.europeana.eu/portal/";
	
	final static String PAGE = url + (port.length() > 0 ? (":" + port) : "") + urlSuffix;
   
	
	public static IndexPage openPage(WebDriver driver) {
		driver.get(PAGE);
		IndexPage page = new IndexPage(driver);
		return page;
	}
	
	private IndexPage(WebDriver driver) {
		super(driver);
	}
	
	public int countBlogItems() {
		// TODO
		return -1;
	}

}
