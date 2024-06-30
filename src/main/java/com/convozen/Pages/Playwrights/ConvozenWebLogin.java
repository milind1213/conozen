package com.convozen.Pages.Playwrights;

import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.Page;

import static com.convozen.Pages.Playwrights.Locators.*;
import static com.convozen.Pages.Playwrights.Locators.buttonText;

public class ConvozenWebLogin extends CommonPlaywright {
    public Page page;

    public ConvozenWebLogin(Page page) {
        super(page);
        this.page = page;
    }

    protected String signWithGoogleBtn = "//button[normalize-space()='Sign In With Google']//*[name()='svg']";
    protected String inputEmail = "//input[@type='email'  and @id='identifierId']";
    protected String nextBtn = "//span[contains(text(),'Next')]";
    protected String passwordInput = "//input[@name='Passwd']";

    public DashboardWeb convozenLogin(String email, String password) {
       page.reload();
       waitFor(2);
        page.waitForSelector(emailField);
        waitFor(5);
        try {
            page.waitForSelector("//*[@id='email' or @placeholder='example@abc.com']", new Page.WaitForSelectorOptions().setTimeout(60000));
            log("Entering username: "+email);
            fill(emailField, email);

            log("Entering password: ********");
            fill(passwordField, password);
            waitFor(2);

            log("Clicking on Sign In button");
            clickWithForce(loginBtn);
            log("Successfully logged in");

            waitFor(3);
            if (isLocatorPresent(page, buttonText.replace("btn", "Sign In"), 5)) {
                click(buttonText.replace("btn", "Sign In"));
                log("Clicking on Sign In button");
            }
        } catch (Exception e) {
            log("Error: An error in login : " + e.getMessage());
        }
        return new DashboardWeb(page); // Return the dashboard instance after login
    }

    public boolean isPageOpenSuccessfully() {
        try {
            page.waitForSelector(".nb__1BVSZ");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAuditPageOpenSuccessfully() {
        try {
            page.waitForSelector(".nb__pFmBT");
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    public void selectPage(String title) {
        waitFor(3);
        String headerLocators = headerLocator.replace("menu", title);
        try {
            page.waitForSelector(headerLocators);
            click(headerLocators);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginWithGoogle(String email, String password) {
        page.waitForSelector(signWithGoogleBtn);
        click(signWithGoogleBtn);
        fill(inputEmail, email);
        click(nextBtn);
        fill(passwordInput, password);
        click(nextBtn);
    }




}


