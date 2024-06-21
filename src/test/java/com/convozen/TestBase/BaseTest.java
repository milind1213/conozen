package com.convozen.TestBase;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.convozen.CommonConstants;
import com.convozen.DriverUtility.WebBrowser;
import com.convozen.Utils.TestListeners;
import com.microsoft.playwright.Page;

import static com.convozen.Utils.FileUtil.getProperty;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest extends WebBrowser {

	protected WebDriver driver;
	protected Page page;

	public void getSeleniumBrowser() {
		String browserType = getProperty(CommonConstants.COMMON, CommonConstants.BROWSER);
		boolean isHeadless = Boolean
				.parseBoolean(getProperty(CommonConstants.COMMON, CommonConstants.RUNMODE_IS_HEADLESS));
		try {
			System.out.println("Launching the " + (isHeadless ? "Headless " : "") + browserType + " browser");
			getSeleniumDriver(browserType, isHeadless);
			driver = webDriver.get();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(120));
		} catch (Exception e) {
			e.printStackTrace();
			log("Failed to launch the Browser: " + e.getMessage());
		}
	}

	public void getPlaywrightBrowser() {
		String browserType = getProperty(CommonConstants.COMMON, CommonConstants.BROWSER);
		boolean isHeadless = Boolean
				.parseBoolean(getProperty(CommonConstants.COMMON, CommonConstants.RUNMODE_IS_HEADLESS));
		try {
			log("Launching the " + (isHeadless ? "Headless " : "") + browserType + " browser");
			getPlaywrightBrowser(browserType, isHeadless);
			page = newPage.get();

		} catch (Exception e) {
			e.printStackTrace();
			log("Failed to launch the browser or navigate to the URL: " + e.getMessage());
		}
	}
	
	

	public void log(String message) {
		try {
			String timestamp = new SimpleDateFormat("h:mm:ss a").format(new Date());
			ExtentTest extentTest = TestListeners.extentTest.get();
			if (extentTest != null) {
				extentTest.log(Status.INFO, message);
			}
			System.out.println("[" + timestamp + "] " + "INFO: " + message);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to log message: " + message);
		}
	}
}
