package com.convozen.Pages.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.convozen.CommonUtils.CommonSelenium;

public class LoginHome extends CommonSelenium {

	WebDriver driver;

	public LoginHome(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	By login = By.xpath("//button[@type='submit' and text()='Sign In']");
	By pageElement = By.cssSelector(".nb__1BVSZ");

	public ConvozenWebDashboard webLogin(String email, String password) {
		waitFor(2);
		waitForElementClickable(driver.findElement(By.id("email")),10);
		sendKeys(By.id("email"), email);
		log("Entered Email ID");

		sendKeys(By.id("password"), password);
		log("Entered Password");

		click(login);
		log("Clicked on  Sign In Button");
		return new ConvozenWebDashboard(driver);
	}

	public boolean isPageOpenSuccessfully() {
		WebElement ele = driver.findElement(pageElement);
		try {
		waitForElementClickable(ele,2);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
