package com.convozen.Tests;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.annotations.Listeners;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.CommonConstants.RANDOM_NAME;
import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class TestCheckListCRUD extends BaseTest {
    private List<String> campaignNames,modeOfCalling,processNames,callerTypes,momentList;
    String startDate = CommonConstants.BEFORE_3_DATE(),
            endDate = CommonConstants.CURRENT_DATE();

    public DashboardWeb getLoginInstance() throws Exception {
        ConvozenWebLogin webLogin = getWebLogin();
        DashboardWeb dashboard = webLogin.convozenLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
        dashboard.getWebLogins().selectPage(CHECKLIST.VALUE());
        Assert.assertTrue(dashboard.getWebLogins().isPageOpenSuccessfully(), "Failed to load " + CALLS.VALUE() + " page.");
        log("Navigated on the " + CHECKLIST.VALUE() + " Page");
        return dashboard;
    }

    public ConvozenWebLogin getWebLogin() {
        getPlaywrightBrowser();
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        page.navigate(getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new ConvozenWebLogin(page);
    }

    @Test(priority = 1)
    public void VerifyingCreateChecklistWithTeams() throws Exception {
        String checklistName = "Test Checklist " + RANDOM_NAME();
        DashboardWeb user = getLoginInstance();

        user.getChecklistPage().createCheckList(checklistName, TEAM.VALUE());
        log("Entered checklist name: [" + checklistName + "] and selected checklist type: [" + TEAM.VALUE() + "]");

        List<String> teamsList = user.getChecklistPage().getTeamList();
        user.getChecklistPage().selectFilterScope(teamsList);
        log("Selected checklist type: [" + TEAM.VALUE() + "] and teams");

        List<String> momentList = user.getChecklistPage().getMoments("99", "global");
        log("Entered weightage: [99] and clicked on [" + GLOBL.VALUE() + "]");

        user.getChecklistPage().addMoments(momentList);
        log("Successfully Searched and selected moments");

        String actualResponse = user.getChecklistPage().checklistTagging(startDate, endDate);
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the checklist tagged.");
        log("Successfully validated the checklist tagging with the response: [" + actualResponse + "]");
    }

    @Test(priority = 2)
    public void VerifyDeactivateDeleteChecklistFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();
        log("Deactivating the [Test Checklist]");
        user.getChecklistPage().deactivateTestChecklist();

        String actualDeleteResponse = user.getChecklistPage().deleteTestChecklist();
        Assert.assertEquals(actualDeleteResponse, "Checklist Deleted Successfully");
        log("Validated 'Checklist Deleted Successfully' pop-up message");
    }

    @Test(priority = 3)
    public void VerifyingCreateChecklistWithFilteredFilters() throws Exception {
        String checklistName = "Test Checklist " + RANDOM_NAME();
        DashboardWeb user = getLoginInstance();

        user.getChecklistPage().createCheckList(checklistName, FILTER.VALUE());
        log("Entered checklist name: [" + checklistName + "] and selected checklist type: [" + FILTER.VALUE() + "]");

        campaignNames = user.getChecklistPage().getCallFilteredDataList(SELECT_CAMPAIGN_NAME.VALUE());
        user.getChecklistPage().selectDropdownValues(campaignNames);

        modeOfCalling = user.getChecklistPage().getCallFilteredDataList(SELECT_CALLING_MODE.VALUE());
        user.getChecklistPage().selectDropdownValues(modeOfCalling);

        processNames = user.getChecklistPage().getCallFilteredDataList(SELECT_PROCESS_NAME.VALUE());
        user.getChecklistPage().selectDropdownValues(processNames);

        callerTypes = user.getChecklistPage().getCallFilteredDataList(SELECT_CALLER_TYPE.VALUE());
        user.getChecklistPage().selectDropdownValues(callerTypes);
        user.getChecklistPage().clickNext();

        momentList = user.getChecklistPage().getMoments("30", "filter");
        log("Entered weightage: [30] and clicked on [" + FILTER.VALUE() + "]");

        user.getChecklistPage().addMoments(momentList);
        log("Successfully searched and selected moments: " + momentList);

        String actualResponse = user.getChecklistPage().checklistTagging(startDate, endDate);
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the checklist tagged.");
        log("Successfully validated the checklist tagging with the response: [" + actualResponse + "]");
    }


    @Test(priority = 4)
    public void VerifyProcessNames_CampaignNames_ModeOfCalling_UpdateFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();

        user.getChecklistPage().searchDropDowns(campaignNames, modeOfCalling, processNames, callerTypes);
        log("Searched and selected Campaign Names: " + campaignNames + ", Mode of Calling: " + modeOfCalling + ", Process Names: " + processNames + ", Caller Types: " + callerTypes);
        log("Successfully updated the [Campaign Names], [Mode of Calling], [Process Names], and [Caller Types]");
    }


    @Test(priority = 5)
    public void VerifyTrackerChecklistAndUIPriorityUpdateFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();

        user.getChecklistPage().taggingAttributes(MOMENTS.VALUE(), TAGGING_ATTRIBUTE.VALUE(), "99");
        log("Successfully Updated the checklist[Tracker] [Show Only Tagged] and [UIPriority] Attributes ");
    }

    @Test(priority = 6)
    public void VerifyBulkTagUntagChecklistFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();
        String actualUntaggingResponse = user.getChecklistPage().tagUntagChecklist(MOMENTS.VALUE(), TAGGING_ATTRIBUTE.VALUE(), BULK_TAG.VALUE(), startDate, endDate);
        Assert.assertEquals(actualUntaggingResponse, "Bulk job submitted please wait for sometime to see the checklist tagged.");
        log("Successfully validated checklist Untagging from " + startDate + " to " + endDate + ".");

        String ActualTaggingResponse = user.getChecklistPage().tagUntagChecklist(MOMENTS.VALUE(), TAGGING_ATTRIBUTE.VALUE(), BULK_UNTAG.VALUE(), startDate, endDate);
        Assert.assertEquals(ActualTaggingResponse, "Checklist Untagging in progress");
        log("Successfully validated checklist tagging from [" + startDate + "] to [" + endDate + "]");
    }

    @Test(priority = 7)
    public void VerifyDeactivateAndDeleteFilteredChecklistFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();
        log("Deactivating the [Test Checklist]");
        user.getChecklistPage().deactivateTestChecklist();

        String actualDeleteResponse = user.getChecklistPage().deleteTestChecklist();
        Assert.assertEquals(actualDeleteResponse, "Checklist Deleted Successfully");
        log("Validated '" + actualDeleteResponse + " Pop Up Message");
    }


    @Test(priority = 8)
    public void VerifyMomentAndCheckListMomentsCount() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<Integer> DisplayAddedMomentCount = user.getChecklistPage().getMomentCounts();
        int displaySum = 0;
        for (int num : DisplayAddedMomentCount) {
            displaySum += num;
        }

        log("Successfully Extracted the  Displayed Moment Count : " + DisplayAddedMomentCount);
        String taggedList = user.getChecklistPage().getMomentOfChecklists();
        String[] elements = taggedList.trim().split(",");
        List<String> ActualElements = new ArrayList<>();
        for (String element : elements) {
            String trimmedElement = element.trim();
            if (!trimmedElement.isEmpty() && !trimmedElement.equals("or")) {
                if (trimmedElement.startsWith("or")) {
                    ActualElements.add(trimmedElement.substring(2));
                } else {
                    ActualElements.add(trimmedElement);
                }
            }
        }
        Assert.assertEquals(ActualElements.size(), displaySum, "Not Matched the Moment Count");
        log("Successfully Validated Display Moment Total Count [" + displaySum + "] with Actual Total Count [" + ActualElements.size() + "]");
    }

    @Test(priority = 9)
    public void VerifyDisplayingMomentsCountWithActualChecklistMoments() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<Integer> displayAddedMomentCountList = user.getChecklistPage().getMomentCountsOfChecklist();
        log("Displaying Moments Count: " + displayAddedMomentCountList);

        // Extract the actual integer count from the list
        int displayAddedMomentCount = displayAddedMomentCountList.get(0);

        List<String> actualMoments = user.getChecklistPage().getMomentsChecklists();
        log("Successfully Extracted the Checklist Moments " + actualMoments.size());

        // Compare the integer count with the size of actualMoments
        Assert.assertEquals(displayAddedMomentCount, actualMoments.size(), "Not Matched the Moment Count");
        log("Successfully Validated the Displayed Moment Number with Actual Moments");
    }


    @Test(priority = 10)
    public void VerifyingChecklistCallsCoveragePercentage() throws Exception {
        DashboardWeb user = getLoginInstance();
        Map<String, Object> callCoverage = user.getChecklistPage().getCallsCoverage();
        for (Map.Entry<String, Object> entry : callCoverage.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
            Assert.assertNotNull(entry.getValue());
            log("Successfully Validated the Call Coverage Percentage");
        }
    }

    @Test(priority = 11)
    public void VerifyCallsOccurredInChecklist() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> callOccurred = user.getChecklistPage().getCallsOccurred();
        log("Calls in Last 7 Days : " + callOccurred);

        Assert.assertNotNull(callOccurred);
        log("Successfully Validated the Calls Occurred in Checklist");
    }

    @Test(priority = 12)
    public void VerifyViewCallsNavigation() throws Exception {
        DashboardWeb user = getLoginInstance();

        int callsCount = user.getChecklistPage().viewCalls();
        Assert.assertTrue(callsCount > 0, "The number of calls should be greater than 0, but was " + callsCount);

        log("Successfully Validated the [view Calls] Navigation");
    }


    // Not in Scope now
    //@Test(priority = 5, dependsOnMethods = "create_Checklist_with_Filtered_filter")
    public void VerifyUpdateSearchAddDeleteMarkOptionalCriticalAndCreateGroups() throws Exception {
        String Weightage = "30";
        int momentCount = 5;

        DashboardWeb user = getLoginInstance();
        user.getChecklistPage().updateExistingMoment(MOMENTS.VALUE(), Weightage);
        log("Successfully Entered  Moment Weightage :" + Weightage);

        List<String> momentList = user.getChecklistPage().getMoments(GLOBL.VALUE());
        user.getChecklistPage().searchSelectAddMoments(momentList, momentCount);
        log("Searched,Selected the Moments " + momentList);

        user.getChecklistPage().removeMomentsFromChecklist(DELET.VALUE());
        log("Successfully Deleted [" + momentList + "]the Moments ");

        int markedOptionalCount = user.getChecklistPage().markAsMomentsOptional(MARK_AS_OPTIONAL.VALUE());
        int actualOptionalCount = user.getChecklistPage().getOptionalMomentCounts();
        Assert.assertEquals(markedOptionalCount, actualOptionalCount, "Not Matched the Optional Count");
        log("Successfully Validated the Marked [" + markedOptionalCount + "] with Actual counts[" + actualOptionalCount + "]");

        user.getChecklistPage().createMomentGroup(CREATE_GROUP.VALUE(), momentList, momentCount);
        log("Successfully Created the Moment Group");
        // log("Successfully Validated [Search],[Add],[Delete],[Mark Optional[,[Mark Critical],[Create Groups] functionality");
    }
}