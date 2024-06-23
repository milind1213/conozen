package com.convozen.Tests;

import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.convozen.CommonConstants;
import com.convozen.Pages.Selenium.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;

import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class Selenium extends BaseTest {
	private ConvozenWebLogin convozenUser;
	
	@BeforeMethod
	public void setUpTestMethod() {
		if (driver == null) {
			getSeleniumBrowser();
		}
	}
	
	@AfterMethod
	public void tearDownTestMethod() {
		if (driver != null) {
			driver.quit();
			webDriver.remove();
		}
	}

	public ConvozenWebLogin getConvozenWebLogin() {
	    String url = getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL);
	    String userName = getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME);
	    String password = getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD);

	    driver.get(url);
	    log("Navigated to URL: " + url);
	    convozenUser = new ConvozenWebLogin(driver);
	    
	    log("Entering Email and Password in input field");
	    convozenUser.webLogin(userName, password);
	    log("Successfully logged in with Email : " + userName);
	    return convozenUser;
	}

	
	@Test
	public void callsFilters() {
	  ConvozenWebLogin convozenUser = getConvozenWebLogin();
	  convozenUser.clickSingInButton();
      log("Clicked On Sign in Button ");
	}
	
	
}
