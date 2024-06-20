package com.convozen.TestBase;

import com.aventstack.extentreports.Status;
import com.convozen.CommonUtils.CommonSelenium;
import com.convozen.Utils.ExtentTestManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.Reporter;
import static com.convozen.Utils.FileUtil.getProperty;

public class BaseTest {
	protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
	protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
	protected static ThreadLocal<Page> page = new ThreadLocal<>();
	protected static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
	private static List<WebDriver> webDriverList = Collections.synchronizedList(new ArrayList<>());

	public void getSeleniumDriver() {
		String browserType = getProperty("common", "convozen.browser");
		boolean isHeadless = Boolean.parseBoolean(getProperty("common", "convozen.headless"));
		log("Launching the " + (isHeadless ? "Headless " : "") + browserType + " browser");
		if (browserType.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			if (isHeadless) {
				options.addArguments("--headless");
				options.addArguments("--window-size=1920,1080");
			} else {
			}
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-extensions");
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("--disable-gpu");
			options.addArguments("--start-maximized");
			options.addArguments("--disable-web-security");
			options.addArguments("--no-proxy-server");
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			Map<String, Object> prefs = new HashMap<>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", prefs);

			WebDriver driver = new ChromeDriver(options);
			webDriver.set(driver);
			webDriverList.add(driver);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(120));

		} else if (browserType.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			if (isHeadless) {
				options.addArguments("--headless");
				options.addArguments("--window-size=1920,1080");
			} else {
				System.out.println("Firefox Browser Launched...");
			}
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-extensions");
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("--disable-gpu");
			options.addArguments("--start-maximized");
			options.addArguments("--disable-web-security");
			options.addArguments("--no-proxy-server");
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);

			WebDriver driver = new FirefoxDriver(options);
			webDriver.set(driver);
			webDriverList.add(driver);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(120));
		}
		webDriver.get();
	}

	public void getPlaywrightBrowser() {
		String browserType = getProperty("common", "convozen.browser");
		boolean isHeadless = Boolean.parseBoolean(getProperty("common", "convozen.headless"));

		System.out.println("Launching the " + (isHeadless ? "Headless " : "") + browserType + " browser");
		playwright.set(Playwright.create());
		BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(isHeadless);

		switch (browserType.toLowerCase()) {
		case "chrome":
			browser.set(playwright.get().chromium().launch(options));
			break;

		case "firefox":
			browser.set(playwright.get().firefox().launch(options));
			break;
			
		default:
			throw new IllegalArgumentException("Unsupported Browser Type: " + browserType);
		}
		page.set(browser.get().newPage());
	}
	
	public void log(String message) {
		try {
			ExtentTestManager.getTest().log(Status.INFO, message);
			LogManager.getLogger(CommonSelenium.class).info(message);
			Reporter.log(message);
		} catch (Exception e) {
			System.err.println("Log error in " + this.getClass().getName());
		}
		String timestamp = new SimpleDateFormat("h:mm:ss a").format(new Date());
		System.out.println("[" + timestamp + "] " + "INFO: " + message);
	}
}
