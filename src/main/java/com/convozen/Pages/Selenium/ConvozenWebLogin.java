package com.convozen.Pages.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.convozen.CommonUtils.CommonSelenium;

public class ConvozenWebLogin extends CommonSelenium {

	WebDriver driver;

	public ConvozenWebLogin(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	By login = By.xpath("//button[@type='submit' and text()='Sign In']");

	public void webLogin(String email, String password) {
		waitFor(2);
		sendKeys(By.id("email"), email);
		sendKeys(By.id("password"), password);
		click(login);
	}

	

	public void clickSingInButton() {
		log("Sucerssfully clikconed on BUtton ");
		click(login);
	
	}

}
