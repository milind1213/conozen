package com.convozen.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.convozen.CommonUtils.CommonSelenium;

public class ConvozenDashboard extends CommonSelenium {
	WebDriver driver;
	  ExtentTest test;

	public ConvozenDashboard(WebDriver driver) {
		super(driver);
		this.driver = driver; 
	}

	By email = By.id("email");
	By password = By.id("password");
	By login = By.xpath("//button[@type='submit' and text()='Sign In']");

	public void webLogin() {
		log(" Milind ddddddddddddddddddddddddddd");
		sendKeys(email, "milind.ghongade@nobroker.in");
      
		log(" Milind ddddddddddddddddddddddddddd");
		sendKeys(password, "admin");
		
		log(" Milind ddddddddddddddddddddddddddd");
		click(login);
	
		log(" Milind ddddddddddddddddddddddddddd");
		click(By.cssSelector("sdsfsddfsf"));

		
	}

}
