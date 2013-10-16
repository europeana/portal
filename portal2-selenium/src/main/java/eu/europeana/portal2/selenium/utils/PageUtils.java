package eu.europeana.portal2.selenium.utils;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class PageUtils {
	
	protected WebDriver driver;
	
	public PageUtils(WebDriver driver) {
		this.driver = driver;
	}
	
	// PROTECTED METHODS FOR USE IN PAGE OBJECTS ONLY
	
	protected WebElement findById(String id) {
		return driver.findElement(By.id(id));
	}
	
	protected List<WebElement> findByClass(String className) {
		return driver.findElements(By.className(className));
	}

	protected WebElement findOneByCss(String query) {
		return driver.findElement(By.cssSelector(query));
	}
	
	protected List<WebElement> findByCss(String query) {
		return driver.findElements(By.cssSelector(query));
	}
	
	protected List<WebElement> findByXPath(String xpath) {
		return driver.findElements(By.xpath(xpath));
	}
	
	protected int countByClass(String className) {
		List<WebElement> list = findByClass(className);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	
	protected int countByCss(String query) {
		List<WebElement> list = findByCss(query);
		if (list != null) {
			return list.size();
		}
		return 0;
	}
	
	public void takeScreenshot(File file) {
		// TODO
	}

	protected String normaliseWhitespace(String input){
		return input != null ? input.replaceAll("\\s+", " ").trim() : null;
	}
	
	
	//////////////// PASTED 
	
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

	//////////////// END PASTED 

	
}
