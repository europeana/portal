package eu.europeana;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotHelper {

	private WebDriver driver;
	
	public ScreenshotHelper(WebDriver driver) {
		this.driver = driver;
	}

	public void saveScreenshot(String screenshotFileName)
			throws IOException {
		File screenshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(screenshotFileName));
		// JOptionPane.showMessageDialog(new JPanel(),
		// "Saved screenshot to " + new
		// File(screenshotFileName).getAbsolutePath() );
	}
}
