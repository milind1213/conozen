package com.convozen.Pages.Selenium;

import org.openqa.selenium.WebDriver;

import com.convozen.CommonUtils.CommonSelenium;

public class ConvozenChecklists extends CommonSelenium{
WebDriver driver;
	public ConvozenChecklists(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

}
