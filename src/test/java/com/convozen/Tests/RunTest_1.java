package com.convozen.Tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convozen.Pages.ConvozenDashboard;
import com.convozen.TestBase.BaseTest;
import com.convozen.TestBase.TestListeners;
import com.convozen.Utils.ExtentTestManager;


@Listeners(TestListeners.class)
public class RunTest_1 extends BaseTest {
    private ConvozenDashboard user;
    protected WebDriver driver;

	@BeforeMethod
    public void setUp() {
        getSeleniumDriver();
        driver = webDriver.get();
        driver.get("https://beta.convozen.ai/");
        ExtentTestManager.startTest("RunTest_1", "Verify login functionality");
    }

    @Test
    public void loginUser() {
        user = new ConvozenDashboard(driver);
        log("dvdfgfdgdfgd gd g dgd g d");
        user.webLogin();
       
       driver.quit();
      
    }
}
