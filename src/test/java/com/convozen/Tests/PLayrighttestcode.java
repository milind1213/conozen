package com.convozen.Tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PLayrighttestcode {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)); // Set headless to false for debugging
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    public void testExample() {
        try {
            System.out.println("Navigating to the login page...");
            page.navigate("https://nobroker.convozen.ai/");
            page.waitForLoadState(LoadState.NETWORKIDLE); // Ensure the page is fully loaded

            System.out.println("Checking if email input field is present...");
            Locator emailLocator = page.locator("//*[@id='email' or @placeholder='example@abc.com']");
            boolean isVisible = emailLocator.isVisible();
            System.out.println("Email input field visibility: " + isVisible);

            Assert.assertTrue(isVisible, "Email input field should be visible");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void teardown() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }
}
