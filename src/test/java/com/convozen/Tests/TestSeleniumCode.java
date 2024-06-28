package com.convozen.Tests;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.ConvozenCalls;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Selenium.ConvozenWebDashboard;
import com.convozen.Pages.Selenium.LoginHome;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.convozen.CommonConstants.CALLZEN_VALUES.CALLS;
import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class TestSeleniumCode extends BaseTest {
    protected ConvozenCalls user = new ConvozenCalls(page);

    public void getLoginInstance() throws Exception {
        LoginHome Login = getWebLogin();
        ConvozenWebDashboard dashboard = Login.webLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
    }

    @Test
    public void test() throws Exception {
        getLoginInstance();
    }


    public LoginHome getWebLogin() {
        getSeleniumBrowser();
        driver.get("https://nobroker.convozen.ai/");
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new LoginHome(driver);
    }
}
