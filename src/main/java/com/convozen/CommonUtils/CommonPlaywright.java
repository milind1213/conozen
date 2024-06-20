package com.convozen.CommonUtils;

import com.microsoft.playwright.Page;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.testng.Reporter;
import com.aventstack.extentreports.Status;
import com.convozen.Utils.ExtentTestManager;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CommonPlaywright {
	public Page page;

	public CommonPlaywright(Page page) {
		this.page = page;
	}

	public void handlePopup(Runnable actionInPopup) {
		page.onPopup((popup) -> {
			try {
				actionInPopup.run();
			} finally {
				popup.close();
			}
		});
	}

	public static boolean isLocatorPresent(Page page, String selector, int sec) {
		try {
			page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(sec * 1000));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isElementPresent(Page page, String locator) {
		try {
			page.waitForSelector(locator);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public void type(String loc, String text) {
		Locator locator = page.locator(loc);
		locator.evaluate("element => element.style.border = '2px solid red'");
		locator.type(text);
	}

	public void clickWithForce(String selector) {
		int retries = 5;
		for (int i = 0; i < retries; i++) {
			try {
				page.click(selector, new Page.ClickOptions().setForce(true));
				break;
			} catch (PlaywrightException e) {
				System.out.println("Attempt " + (i + 1) + " failed: " + e.getMessage());
			}
		}
	}

	public void click(String loc) {
		Locator locator = page.locator(loc);
		locator.evaluate("element => element.style.border = '2px solid red'");
		locator.click();
	}

	public void ClickDouble(String loc) {
		Locator locator = page.locator(loc);
		locator.evaluate("element => element.style.border = '2px solid red'");
		locator.dblclick(); // Perform double-click
	}

	public void clicks(String element) {
		Locator locator = page.locator(element);
		locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
		locator.evaluate("element => element.style.border = '2px solid red'");
		locator.click(new Locator.ClickOptions().setButton(MouseButton.LEFT).setClickCount(1).setDelay(100));
	}

	public void fill(String loc, String text) {
		Locator locator = page.locator(loc);
		locator.evaluate("element => element.style.border = '2px solid red'");
		locator.fill(text);
	}

	public static void waitForSelector(Page page, String selector, int timeoutInSeconds) {
		try {
			page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeoutInSeconds * 1000));
		} catch (PlaywrightException e) {
			System.err.println("Selector \"" + selector + "\" did not appear within " + timeoutInSeconds + " seconds.");
			throw e;
		}
	}

	public void pressEnter() {
		page.keyboard().press("Enter");
	}

	public boolean elementExists(String locatorString) {
		return page.locator(locatorString).first() != null;
	}

	public String getText(String locatorString) {
		return page.locator(locatorString).innerText();
	}

	public void waitFor(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean isLocatorEnabled(Page page, String locator) {
		try {
			return (boolean) page.evaluate("(locator) => { return !document.querySelector(locator).disabled; }",
					locator);
		} catch (PlaywrightException e) {
			System.err.println("Error occurred while checking element enablement: " + e.getMessage());
			return false;
		}
	}

	public static boolean isLocatorClickable(Page page, String locator) {
		try {
			return (boolean) page.evaluate(
					"(locator) => { return document.querySelector(locator).offsetWidth > 0 && document.querySelector(locator).offsetHeight > 0 && !document.querySelector(locator).disabled; }",
					locator);
		} catch (PlaywrightException e) {
			System.err.println("Error occurred while checking element clickability: " + e.getMessage());
			return false;
		}
	}

	public void scrollToElement(ElementHandle element) {
		try {
			element.scrollIntoViewIfNeeded();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void clickWithJS(Page page, String selector) {
		String js = "document.querySelector('" + selector + "').click();";
		page.evaluate(js);
	}

	public static void waitForElement(Page page, String selector, int seconds) throws InterruptedException {
		page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
	}

	public static void scrollIntoView(Page page, String selector) {
		page.evaluate("locator => document.querySelector(locator).scrollIntoView()", selector);
	}

	public static void waitForLocatorClickable(Page page, String selector, int seconds) {
		try {
			page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(seconds * 1000));
		} catch (Exception e) {
			System.err.println("Waited for element [" + selector + "] to be clickable for " + seconds + " seconds");
		}
	}

	public static void refreshAndWait(Page page, int seconds) throws Exception {
		Thread.sleep(1000);
		page.reload();
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
