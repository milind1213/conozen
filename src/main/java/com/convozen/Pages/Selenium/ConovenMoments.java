package com.convozen.Pages.Selenium;

import org.openqa.selenium.WebDriver;

import com.convozen.CommonUtils.CommonSelenium;

public class ConovenMoments extends CommonSelenium {
	WebDriver driver;

	public ConovenMoments(WebDriver driver) {
		super(driver);
		this.driver = driver;

	}

}
