package com.convozen.Tests;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.Utils.FileUtil.getProperty;
@Listeners(TestListeners.class)
public class TestAgentsFilters extends BaseTest {

    public DashboardWeb getLoginInstance() throws Exception {
        ConvozenWebLogin webLogin = getWebLogin();
        DashboardWeb dashboard = webLogin.convozenLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
        dashboard.getWebLogins().selectPage(AGENTS.VALUE());
        Assert.assertTrue(dashboard.getWebLogins().isPageOpenSuccessfully(), "Failed to load " + AGENTS.VALUE() + " page.");
        log("Navigated on the " + AGENTS.VALUE() + " Page");
        return dashboard;
    }

    public ConvozenWebLogin getWebLogin() {
        getPlaywrightBrowser();
        page.navigate(getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new ConvozenWebLogin(page);
    }

    @Test(priority = 1)
    public void VerifyingTeamSearchAgentFilter() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> teams = user.getAgentsPage().getTeaNames();
        log("Retrieved teams: " + teams);

        user.getAgentsPage().searchValidate(teams);
        log("Successfully Search and selected teams");
    }
    @Test(priority = 2)
    public void VerifyingSearchSelectAddRemoveColumnsFilter() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> origenalColumnList = user.getAgentsPage().getCallsTableDefaultColumns();
        log("Original  : " + origenalColumnList);

        List<String> totalColumns = user.getAgentsPage().getAllCallsColumnNames();
        log("Extracted the Moment Table Filter Columns" + totalColumns);

        List<String> columnList = user.getAgentsPage().getUncheckedCallsColumnFilterNames();
        log("Extracted the unchecked " + columnList + " Successfully");

        user.getAgentsPage().searchSelectAddColumn(columnList);
        log("Search and Selected the" + columnList + " Successfully");

        List<String> actualTotalColumns = user.getAgentsPage().getTableColumns();
        Assert.assertEquals(totalColumns.size(), actualTotalColumns.size() - 2, "Total columns do not match");

        user.getAgentsPage().removeColumns(columnList);
        List<String> columnsAfterRemoving = user.getAgentsPage().getTableColumns();
        Assert.assertEquals(origenalColumnList.size(), columnsAfterRemoving.size(), "Total columns do not match");
        log("Successfully Validated the column number [ Before  and After ] Applying column Filters");
    }

    @Test(priority = 3)
    public void VerifyViewByAgentFilterFunctionality() throws Exception {
        List<String> filterByList = Arrays.asList("Today", "Yesterday", "Last Week", "Last 1 Month", "Last 2 Months", "Last 3 Months");
        DashboardWeb user = getLoginInstance();
        List<Integer> callCount = user.getAgentsPage().viewByFilters(filterByList);
        Assert.assertTrue(callCount.size() >= 2, "Insufficient call count data");
        for (int i = 1; i < callCount.size(); i++) {
            int currentCount = callCount.get(i);
            int previousCount = callCount.get(i - 1);
            log("Previous Count : " + previousCount + " Current Count : " + currentCount);
            Assert.assertTrue(currentCount >= previousCount, "Call counts are not in incrementing order.");
        }
        log("Successfully validated the view By Filters");
    }


    @Test(priority = 4 )
    public void VerifyingAgentMadeCallsAndCustomersReachedCalls() {
        try {
            List<String> filterByList = Arrays.asList("Today", "Yesterday", "Last Week", "Last 1 Month", "Last 2 Months", "Last 3 Months");
            DashboardWeb user = getLoginInstance();
            int tableCalls = user.getAgentsPage().getAgentTableDetails(filterByList);
            System.out.println("Table Calls: " + tableCalls);

            String agentDetails = user.getAgentsPage().rowAgentsRowData();
            System.out.println("Extracted the Agent: " + agentDetails);

            List<Integer> agentCallReachedList = user.getAgentsPage().getAgentCallAndCustomerReached();
            for (int callNum : agentCallReachedList) {
                if (callNum != tableCalls) {
                    log("Validation Failed: Expected calls [" + tableCalls + "] but found [" + callNum + "]");
                    Assert.fail("Validation Failed: Expected calls [" + tableCalls + "] but found [" + callNum + "]");
                }
            }
            log("Successfully Validated Table [Made Calls] with [Made Calls & Customer Reach]");
            Map<String, String> checklistDetails = user.getAgentsPage().getAgentTaggedChecklist();
            for (Map.Entry<String, String> entry : checklistDetails.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {
            log("Test Failed: " + e.getMessage());
            Assert.fail("Test Failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 5 )
    public void VerifyingAgentStatisticCallData() throws Exception {
        List<String> filterByList = Arrays.asList("Today", "Yesterday", "Last Week", "Last 1 Month", "Last 2 Months", "Last 3 Months");
        DashboardWeb user = getLoginInstance();
        user.getAgentsPage().getAgentTableDetails(filterByList);
        String agentDetails = user.getAgentsPage().rowAgentsRowData();
        log("Extracted the Agent : " + agentDetails);

        List<Integer> agentCallList = user.getAgentsPage().getAgentCallAndCustomerReachedData(filterByList);
        log("Extracted the Agent : " + agentCallList);
        Assert.assertTrue(agentCallList.size() >= 3, "Insufficient call count data");
        for (int i = 2; i < agentCallList.size(); i++) {
            if (agentCallList.get(i) == 0) {
                continue; // Skip to the next iteration if the current element is zero
            }
            int previousCount = agentCallList.get(i - 1);
            int currentCount = agentCallList.get(i);
            log("Previous Count : " + previousCount + " Current Count : " + currentCount);
            Assert.assertTrue(currentCount >= previousCount, "Call counts are not in incrementing order.");
        }
        log("Successfully validated in the Agent Statistics [ViewBy Dropdown] Filters");
    }


    @Test(priority = 6)
    public void longHoldPercentageCalculations() throws Exception {
        DashboardWeb user = getLoginInstance();
        Double callCount = user.getAgentsPage().getAgentCallCount();
        Double longSilenceCount = user.getAgentsPage().getLongSilenceCount();
        double expectedLongHoldPercentage = longSilenceCount * 100 / callCount;

        Double actualLongHoldPercentage = user.getAgentsPage().getAgentLongHoldPercentage();
        Assert.assertEquals(actualLongHoldPercentage, expectedLongHoldPercentage, 0.01);
        log("Successfully Validated the LongHold percentage " + actualLongHoldPercentage + " with " + expectedLongHoldPercentage);
    }

}
