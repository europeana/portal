package eu.europeana.portal2.selenium.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import eu.europeana.portal2.selenium.Pages;
import eu.europeana.portal2.selenium.model.search.Facet;
import eu.europeana.portal2.selenium.page.abstracts.Portal2Page;
import eu.europeana.portal2.selenium.utils.PatternUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyEuropeanaPage extends Portal2Page {

	// constructors
	public static MyEuropeanaPage openPage(WebDriver driver, String hashString) {
		return openPage(driver, hashString, null);
	}
	
	public static MyEuropeanaPage openPage(WebDriver driver, String hashString, String paramString) {
		String url = Pages.MYEU + (hashString != null ? hashString : "") + (paramString != null ? ("?" + paramString) : "");
		driver.get(url);
		MyEuropeanaPage page = new MyEuropeanaPage(driver);
		
		return page;
	}


	
	public static MyEuropeanaPage checkPage(WebDriver driver) {
		if (StringUtils.startsWithIgnoreCase(driver.getCurrentUrl(), Pages.MYEU)) {
			MyEuropeanaPage page = new MyEuropeanaPage(driver);
			return page;
		}
		return null;
	}

	private MyEuropeanaPage(WebDriver driver) {
		super(driver);
		waitFor(new WaitCondition() {
			@Override
			public boolean condition() {
				return findByClass("footer-funded-by").size() > 0;
			}
		});
	}

	// util 
	
	public void login() {
		WebElement username = findByXPath("//form[@class=\"login-box\"]//input[@name=\"j_username\"]").get(0);
		WebElement password = findByXPath("//form[@class=\"login-box\"]//input[@name=\"j_password\"]").get(0);
		WebElement submit   = findByXPath("//form[@class=\"login-box\"]//input[@name=\"submit\"]").get(0);
		
		username.sendKeys("andy" + "j" + "maclean" + "@" + "gmail.com");
		password.sendKeys("andy" + "123");
		submit.submit();
	}
	
	public void setLanguageOptions() {
		
		List<WebElement> checkboxes = getCheckboxesByName( Arrays.asList("it", "nl"));
		
		for(WebElement checkbox : checkboxes){
			checkbox.click();
		}
		
		try{
			Thread.sleep(2000);
		}
		catch(Exception e){}

		getLanguageSubmit().click();
		
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.presenceOfElementLocated(By.id("ajax-feedback")));
		
	}
	
	

	// getters

	public List<WebElement> getCheckboxesByName(List<String> names) {
		List<WebElement> res = new ArrayList<WebElement>();
		for(String name : names){
			res.addAll(findByXPath("//input[@value=\"" + name + "\"]"));
		}
		return res;
	}
	
	public WebElement getLanguageSubmit() {
		return findByXPath("//div[@id=\"language-settings\"]//input[@name=\"submit\"]").get(0);
	}
	
}
