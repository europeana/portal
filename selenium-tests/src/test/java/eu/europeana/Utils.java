package eu.europeana;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

//import test.java.eu.europeana.fulldoc.WebDriver;


public class Utils {

	
	private static WebElement	sWebElement;	// a static WebElement
	private static String		sString;		// a static String
	private static int			sInt;			// a static int


	public static void searchFor(final WebDriver driver, String term) {
		
		(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return driver.findElement(By.id("query-input")).isDisplayed();
			}
		});

		WebElement searchText = driver.findElement(By.id("query-input"));
		searchText.sendKeys( setString(term) );
		searchText.submit();

		(new WebDriverWait(driver, 20)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().endsWith(getString().toLowerCase().replaceAll("&", "") + " - europeana - search results") && d.findElement(By.cssSelector("#query-input")).isDisplayed();
			}
		});
	}
	
	public static boolean isElementPresent(WebDriver driver, By selector) {
        try {
            driver.findElement(selector);
            return true;
        }
        catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
	

	
	public static Pattern createPattern(String query) {
		
		String s = "";
		for(char c : query.toCharArray()){
			if(c == '*'){
				s += "\\*";
			}
			else{				
				s += c;
			}
		}
		return Pattern.compile("(&|\\?)" + s + "(&|$)");
	}
	

	public static List<Pattern> transformPatterns(List<String> queries) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		for (String query : queries) {
			patterns.add(createPattern(query));
		}
		return patterns;
	}
	
	public static String encodeFix(String text) {
		return text.replace("%3B", ";").replace("%5C", "\\").replace("%2F", "/").replace("%7C", "|").replace("%21", "!");
	}

	public static WebElement getWebElement(){
		return sWebElement;
	}
	
	public static void setWebElement(WebElement e){
		sWebElement = e;
	}
	
	public static String getString(){
		return sString;
	}
	
	public static String setString(String s){
		sString = s;
		return s;
	}
	
	public static int getInt(){
		return sInt;
	}
	
	public static int setInt(int i){
		sInt = i;
		return i;
	}
	
}
