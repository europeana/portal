package eu.europeana;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import org.ini4j.Ini;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Config {
	
	public static  String	REAL_DATA_SERVER	= "http://test.portal2.eanadev.org";
	public static  String	SERVER				= "http://test.portal2.eanadev.org";
	//private final String SERVER = "http://localhost:8081";
	public static  String	baseUrl				= SERVER + "/portal/";
	public static  String	baseUrlRealData		= REAL_DATA_SERVER + "/portal/";
	public static  String	searchUrl			= baseUrl + "search.html?";
	public static  String	searchUrlRealData	= baseUrlRealData + "search.html?";

	private static	boolean		runLocal	= true;
	private static	URL			webdriverIP	= null;
	
	private static File getAppRoot() {
		try {
			return new File(".").getCanonicalFile();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't get canonical file", e);
		}
	}
	
	private static URL getWebdriverIP(){
		if(runLocal){
			return null;
		}
		if(webdriverIP == null){
			Ini ini = null;
			
			// open .ini
			try{
				ini = new Ini( new File( getAppRoot() + File.separator + "start.ini" ) );
			}
			catch(Exception e){
				e.printStackTrace();
				runLocal = true; 
			}
			
			// read-set webdriver ip
			try{				
				String ip = ini.get("client", "webdriverIP");
				if(ip!=null){
					ip = ip.trim().replace(";", "");
					webdriverIP = new URL(ip);
				}
			}
			catch(Exception e){
				e.printStackTrace(); 
			}
			
			// read-set base url
			try{				
				String portalIPmockData = ini.get("client", "portalIPmockData").trim().replace(";", "");
			
				if(portalIPmockData != null){
					SERVER		= portalIPmockData;
					baseUrl		= SERVER + "/portal/";
					searchUrl	= baseUrl + "search.html?";
				}
				// test the URL
				try{
					new URL(baseUrl);
				}
				catch(MalformedURLException e){
					System.err.println("misconfigured .ini file: please check the value of baseUrlToTest (currently it's " + portalIPmockData + ")");
					System.exit(0);
				}
				
				String portalIPrealData = ini.get("client", "portalIPrealData").trim().replace(";", "");
				
				if(portalIPrealData != null){
					REAL_DATA_SERVER	= portalIPrealData;
					baseUrlRealData		= REAL_DATA_SERVER + "/portal/";
					searchUrlRealData	= baseUrlRealData + "search.html?";
				}
				// test the URL
				try{
					new URL(baseUrl);
				}
				catch(MalformedURLException e){
					System.err.println("misconfigured .ini file: please check the value of baseUrlToTest (currently it's " + portalIPmockData + ")");
					System.exit(0);
				}

			}
			catch(Exception e){
				e.printStackTrace();
				runLocal = true; 
			}

		}
		return webdriverIP;
	}
	
	
	public static WebDriver openBrowser() {
		return openBrowser(false);
	}
	
	public static WebDriver openBrowser(boolean realData) {
		WebDriver driver = null;
		if(getWebdriverIP() == null){
			driver = new FirefoxDriver();
		}
		else{
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setJavascriptEnabled(true);
			driver = new RemoteWebDriver(getWebdriverIP(), capabilities);
		}
		
		if(realData){			
			driver.get(Config.baseUrlRealData);
		}
		else{
			driver.get(Config.baseUrl);			
		}
		return driver;
	}
	
	
	public static void closeBrowser(WebDriver driver) throws IOException {
		//screenshotHelper.saveScreenshot("screenshot.png");
		try{
			driver.close();
			driver.quit();			
		}
		catch(Exception e){
			e.printStackTrace();			
		}
	}
	
}
