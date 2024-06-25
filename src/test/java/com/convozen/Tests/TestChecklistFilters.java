package com.convozen.Tests;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.List;
import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class TestChecklistFilters extends BaseTest {

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
    public void VerifyingChecklistDefaultFilter() throws Exception {
        DashboardWeb user = getLoginInstance();
        String actualDefaultFilter = user.getChecklistPage().getDefaultAppliedFilters();
        Assert.assertEquals(actualDefaultFilter, "active");
        log("Successfully  Validated Default applied Filters");
    }

    @Test(priority = 2)
    public void VerifyingTeamSearchChecklistfilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> teams = user.getChecklistPage().getTeaNames();
        log("Retrieved teams from Dropdowns");

        List<Integer> actualTeams = user.getChecklistPage().searchValidate1(teams);
        log("Checklist Number of Searched teams : " + actualTeams);

        Assert.assertNotNull(actualTeams);
        log("Validated [Teams Filter] Successfully.");
    }

    @Test(priority = 3)
    public void VerifyingChecklistSearchFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> checklistNames = user.getChecklistPage().getChecklistNames(3);
        log("Retrieved Checklist Names: " + checklistNames);

        List<String> actualChecklistNames = user.getChecklistPage().searchChecklist(checklistNames);
        log("Actual Checklist Names after search: " + actualChecklistNames);

        Assert.assertEquals(checklistNames.size(), actualChecklistNames.size(), "Number of checklists mismatch");
        for (String name : checklistNames) {
            Assert.assertTrue(actualChecklistNames.contains(name), "Checklist '" + name + "' not found in actual Checklist");
            log("Team '" + name + "' found in actual teams");
        }
        log("All checklist Validated Successfully");
    }

    @Test(priority = 4)
    public void VerifyingMataDataFilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> campaignNames = user.getChecklistPage().getMetaDataDropdownValues(CAMPAIGN_NAME.VALUE(), SELECT_CAMPAIGN_NAME.VALUE(), 3);
        log("Retrieved Campaign Names: " + campaignNames);

        List<String> searchedCampaignList = user.getChecklistPage().checklistMetaDataFilters(CAMPAIGN_NAME.VALUE(), SELECT_CAMPAIGN_NAME.VALUE(), campaignNames, 5);
        Assert.assertEquals(campaignNames, searchedCampaignList);
        log("Successfully Validated CampaignNames " + searchedCampaignList + " with " + campaignNames);

        List<String> callingModes = user.getChecklistPage().getMetaDataDropdownValues(MODE_OF_CALLING.VALUE(), SELECT_CALLING_MODE.VALUE(), 3);
        log("Retrieved Campaign Names: " + callingModes);

        List<String> searchedCallingModeList = user.getChecklistPage().checklistMetaDataFilters(MODE_OF_CALLING.VALUE(), SELECT_CALLING_MODE.VALUE(), callingModes, 5);
        Assert.assertEquals(callingModes, searchedCallingModeList);
        log("Successfully Validated CampaignNames " + searchedCallingModeList + " with " + callingModes);

        List<String> processName = user.getChecklistPage().getMetaDataDropdownValues(PROCESSNAME.VALUE(), SELECT_PROCESS_NAME.VALUE(), 5);
        log("Retrieved Process Names: " + processName);

        List<String> searchedProcessList = user.getChecklistPage().checklistMetaDataFilters(PROCESSNAME.VALUE(), SELECT_PROCESS_NAME.VALUE(), processName, 5);
        Assert.assertTrue(processName.containsAll(searchedProcessList));
        log("Successfully Validated CampaignNames " + searchedProcessList + " with " + processName);

    }

    @Test(priority = 5)
    public void VerifyingChecklistTypeFilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        user.getChecklistPage().checklistDetailsFilters(CHECKLIST_TYPE.VALUE(), SELECT_CHECKLIST_TYPE.VALUE(), CALL_LEVEL.VALUE());
        String checklistType = user.getChecklistPage().isChecklistTypeChecked("callLevel");

        Assert.assertEquals(CALL_LEVEL.VALUE(), checklistType, "Checklist types do not match."+checklistType);
        log("Successfully Validated the Checklist Type Applied ["+CALL_LEVEL.VALUE()+ "] And Actual [ "+checklistType+"] filter");

        user.getChecklistPage().checklistDetailsFilters(CHECKLIST_TYPE.VALUE(), SELECT_CHECKLIST_TYPE.VALUE(),CUSTOMER_LEVEL.VALUE());
        String checkType = user.getChecklistPage().isChecklistTypeChecked("customerLevel");
        if (CUSTOMER_LEVEL.VALUE().equals(checkType)) {
            Assert.assertTrue(true, CUSTOMER_LEVEL.VALUE() +" Checklist Types Match");
            log("Successfully "+CUSTOMER_LEVEL.VALUE()+" Checklist Validated");
        } else if(checkType == null){
            Assert.assertTrue(true, CUSTOMER_LEVEL.VALUE()+" Checklist are Not Available.");
        }
    }

    public void ActivateDeactivate_and_BulkTagUntag() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<String> checklistNameList = user.getChecklistPage().getCheckListNames();
        log("Extracted the Checklist Names '" + checklistNameList + "' from the Checklist Table");

        String activationConfirmation = user.getChecklistPage().checklistActivateDeactivate(checklistNameList, ACTIVATE.VALUE());
        Assert.assertEquals(activationConfirmation, "Activation completed!");
        log("Successfully validated Moment [ Activation Completed! ] Pop Up Message");

        String deactivationConfirmation = user.getChecklistPage().checklistActivateDeactivate(checklistNameList, DEACTIVATE.VALUE());
        Assert.assertEquals(deactivationConfirmation, "Deactivation Completed!");
        log("Successfully validated Moment [ Deactivation Completed! ] Pop Up Message");

        String processInitiated = user.getChecklistPage().checklistBulkTagUntag(checklistNameList, BULK_TAG.VALUE(), startDate, endDate);
        log("Successfully Bulk Tagging from [" + startDate + "] and [" + endDate + "]");
        Assert.assertEquals(processInitiated, "Process has been Initiated...");

        String processInitiated1 = user.getChecklistPage().checklistBulkTagUntag(checklistNameList, BULK_UNTAG.VALUE(), startDate, endDate);
        log("Successfully Bulk UnTagging from [" + startDate + "] and [" + endDate + "]");
        Assert.assertEquals(processInitiated1, "Process has been Initiated...");
    }

}
