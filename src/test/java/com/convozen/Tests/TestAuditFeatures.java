package com.convozen.Tests;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class TestAuditFeatures extends BaseTest {
    public DashboardWeb getLoginInstance() throws Exception {
        ConvozenWebLogin webLogin = getWebLogin();
        DashboardWeb dashboard = webLogin.convozenLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
        dashboard.getWebLogins().selectPage(AUDIT.VALUE());
        Assert.assertTrue(dashboard.getWebLogins().isAuditPageOpenSuccessfully(), "Failed to load " + AUDIT.VALUE() + " page.");
        log("Navigated on the " + AUDIT.VALUE() + " Page");
        return dashboard;
    }

    public ConvozenWebLogin getWebLogin() {
        getPlaywrightBrowser();
        page.navigate(getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new ConvozenWebLogin(page);
    }

    @Test(priority = 1)
    public void VerifyingNumberOfAudits() throws Exception {
        int allCounts = 0, todoCount = 0, ongoingCount = 0, completeCount = 0, overdueCount = 0;
        DashboardWeb auditor = getLoginInstance();
        allCounts = auditor.getAudit().getAuditStatusCounts(CommonConstants.STATUS.ALL.VALUE());
        log("Fetched 'All' status Number of Audits  : " + allCounts);

        todoCount = auditor.getAudit().getAuditStatusCounts(CommonConstants.STATUS.TO_DO.VALUE());
        log("Fetched 'To Do' status Number of Audits : " + todoCount);

        ongoingCount = auditor.getAudit().getAuditStatusCounts(CommonConstants.STATUS.ONGOING.VALUE());
        log("Fetched 'Ongoing' status Number of Audits : " + ongoingCount);

        completeCount = auditor.getAudit().getAuditStatusCounts(CommonConstants.STATUS.COMPLETED.VALUE());
        log("Fetched 'Completed' status Number of Audits : " + completeCount);

        overdueCount = auditor.getAudit().getAuditStatusCounts(CommonConstants.STATUS.OVERDUE.VALUE());
        log("Fetched 'Overdue' status Number of Audits : " + overdueCount);

        int calculatedTotalAuditCount = todoCount + ongoingCount + completeCount + overdueCount;
        log("Calculated total Number of Audit Status  : " + calculatedTotalAuditCount);

        Assert.assertEquals(allCounts, calculatedTotalAuditCount);
        log("Successfully validated the Total Audit Counts "+allCounts+ " With Calculated Status Counts : "+calculatedTotalAuditCount);
    }

    @Test(priority = 2)
    public void VerifyManualAuditingFLow() throws Exception {
        DashboardWeb auditor = getLoginInstance();
        auditor.getAudit().selectAuditFromToDo("CS General Call Audits");
        int auditCalls = auditor.getAudit().getAuditCalls();
        System.out.println(auditCalls);
        auditor.getAudit().submitQuestion("Yes");
    }



}
