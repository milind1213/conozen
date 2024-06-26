package com.convozen.Tests;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.ConvozenCalls;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.convozen.CommonConstants.CALLZEN_VALUES.CALLS;
import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class TestNewCode extends BaseTest {
    protected ConvozenCalls user = new ConvozenCalls(page);

    public void getLoginInstance() throws Exception {
        ConvozenWebLogin webLogin = getWebLogin();
        DashboardWeb dashboard = webLogin.convozenLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
        dashboard.getWebLogins().selectPage(CALLS.VALUE());
        Assert.assertTrue(dashboard.getWebLogins().isPageOpenSuccessfully(), "Failed to load " + CALLS.VALUE() + " page.");
        log("Navigated on the " + CALLS.VALUE() + " Page");
    }

    @Test
    public void test() throws Exception {
        getLoginInstance();
        user = new ConvozenCalls(page);

        String defaultFilters = user.getDefaultAppliedFilters();
        boolean conditionMet = defaultFilters.equals("Call Duration:> 30 secs,View By:Yesterday,Transcribed Calls,") ||
                defaultFilters.equals("Call Duration:> 30 secs,View By:Today,Transcribed Calls,Processed,");

        Assert.assertTrue(conditionMet, "Default applied filters are not as expected. Actual: " + defaultFilters);
        log("Successfully validated the default applied filters: [" + defaultFilters + "]");
    }


    public ConvozenWebLogin getWebLogin() {
        getPlaywrightBrowser();
        page.navigate("https://nobroker.convozen.ai/");
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new ConvozenWebLogin(page);
    }
}
