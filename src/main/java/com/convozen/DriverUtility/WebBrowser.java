package com.convozen.DriverUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class WebBrowser {
	protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
	protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
	protected static ThreadLocal<Page> newPage = new ThreadLocal<>();
	protected static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
	private static final List<WebDriver> webDriverList = Collections.synchronizedList(new ArrayList<>());

	public void getSeleniumDriver(String browserType, boolean isHeadless) {
		if (browserType.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			if (isHeadless) {
				options.addArguments("--headless");
				options.addArguments("--window-size=1920,1080");
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

		} else if (browserType.equalsIgnoreCase("firefox")) {
			WebDriverManager.chromedriver().setup();
			FirefoxOptions option = new FirefoxOptions();
			option.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			if (isHeadless) {
				option.addArguments("--headless");
				option.addArguments("--window-size=1920,1080");
			}
			option.addArguments("--no-sandbox");
			option.addArguments("--disable-dev-shm-usage");
			option.addArguments("--disable-extensions");
			option.addArguments("--dns-prefetch-disable");
			option.addArguments("--disable-gpu");
			option.addArguments("--start-maximized");
			option.addArguments("--disable-web-security");
			option.addArguments("--no-proxy-server");
			option.setPageLoadStrategy(PageLoadStrategy.EAGER);

			WebDriver driver = new FirefoxDriver(option);
			webDriver.set(driver);
			webDriverList.add(driver);
		}
		webDriver.get();
	}


	public void getPlaywrightBrowser(String browserType, boolean isHeadless) {
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
		newPage.set(browser.get().newPage());
	}


	
}
