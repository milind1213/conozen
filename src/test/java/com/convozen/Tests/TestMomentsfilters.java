package com.convozen.Tests;
import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.WebDashboard;
import com.convozen.Pages.Playwrights.WebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.List;
import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.CommonConstants.MOMENT_VALUES.KEYWORD;
import static com.convozen.CommonConstants.MOMENT_VALUES.SEMANTIC_;
import static com.convozen.Utils.FileUtil.getProperty;
@Listeners(TestListeners.class)
public class TestMomentsfilters extends BaseTest {
    public WebDashboard getLoginInstance() throws Exception {
        WebLogin webLogin = getWebLogin();
        WebDashboard dashboard = webLogin.convozenLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
        dashboard.getWebLogins().selectPage(MOMENTS.VALUE());
        Assert.assertTrue(dashboard.getWebLogins().isPageOpenSuccessfully(), "Failed to load " + CALLS.VALUE() + " page.");
        log("Navigated on the " + MOMENTS.VALUE() + " Page");
        return dashboard;
    }

    public WebLogin getWebLogin() {
        getPlaywrightBrowser();
        page.navigate(getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new WebLogin(page);
    }

    @Test(priority = 1 )
    public void VerifyingMomentsByTeamSearchFilters() throws Exception {
        WebDashboard user = getLoginInstance();
        List<String> teams = user.getMomentPage().getTeams();
        log("Extracted the TeamNames");

        List<String> searchResultList = user.getMomentPage().searchTeamFilter(teams);
        Assert.assertEquals(searchResultList.size(), teams.size() * 2);
        log("Validating the Searched and Actual Teams");
    }

    @Test(priority = 2)
    public void VerifyingSearchMomentsByNameFunctionality() throws Exception {
        WebDashboard user = getLoginInstance();
        List<String> momentNameList = user.getMomentPage().getMomentNames();
        log("Extracted the momentNames from the MomentTable");

        List<String> searchResultList = user.getMomentPage().searchMomentByName(momentNameList);
        log("Extracted the Searched momentNames from the MomentTable");

        Assert.assertEquals(searchResultList.size(), momentNameList.size());
        log("Successfully validated the "+searchResultList.size()+" with "+ momentNameList.size());

        for (String momentName : momentNameList) {
            boolean isMomentFound = searchResultList.stream()
                    .map(String::trim) // Trim each string
                    .anyMatch(result -> result.trim().equals(momentName.trim()));
            Assert.assertTrue(isMomentFound, momentName + " Not found in search results");
        }
    }

    @Test(priority = 3)
    public void VerifyingMomentsColumnFilter_Search_Select_Add_Remove() throws Exception {
        WebDashboard user = getLoginInstance();
        List<String> origenalColumnList = user.getMomentPage().getMomentTableDefaultColumns();
        List<String> totalColumns = user.getMomentPage().getAllMomentColumnNames();
        log("Extracted the Moments Table Filter Columns Successfully");

        List<String> columnList = user.getMomentPage().getUncheckedMomentColumnFilterNames();
        log("Extracted the unchecked Columns Successfully");

        user.getMomentPage().searchSelectAddColumn(columnList);
        log("Search and Selected the Columns Successfully");

        List<String> actualTotalColumns = user.getMomentPage().getTableColumns();
        Assert.assertEquals(totalColumns.size(), actualTotalColumns.size(), "Total columns do not match");

        user.getMomentPage().removeColumns(columnList);
        List<String> columnsAfterRemoving = user.getMomentPage().getTableColumns();
        Assert.assertEquals(origenalColumnList.size(), columnsAfterRemoving.size(), "Total columns do not match");
        log("Successfully Validated the column number [ Before And After ] Applying column Filters");
    }

    @Test(priority =4)
    public void VerifyingReloadButtonFunctionality() throws Exception {
        WebDashboard user = getLoginInstance();

        user.getMomentPage().reloadPage();
        log("Successfully Reloaded the Moment Table");
    }

    @Test(priority =5)
    public void VerifyingActivateDeactivateActionFilter() throws Exception {
        WebDashboard user = getLoginInstance();
        List<String> momentNameList = user.getMomentPage().getMomentNames();
        log("Extracted the momentNames from the MomentTable");

        String activationConfirmation = user.getMomentPage().momentActivation(momentNameList);
        if (!"Activation completed!".equals(activationConfirmation) && activationConfirmation != null) {
            Assert.fail("activationConfirmation is neither 'Activation completed!' nor null");
        }

        String deactivationConfirmation = user.getMomentPage().momentDeactivation(momentNameList);
        if (!"Deactivation Completed!".equals(deactivationConfirmation) && deactivationConfirmation != null) {
            Assert.fail("activationConfirmation is neither 'Activation completed!' nor null");
        }
    }

    public void VerifyingBulktagMomentActionFilter() throws Exception {
        String startDate = "150320241610", endDate = "040420241602";
        WebDashboard user = getLoginInstance();
        List<String> momentNameList = user.getMomentPage().getMomentNames();
        log("Extracted the momentNames from the MomentTable");

        user.getMomentPage().bulkTagActions(momentNameList, startDate, endDate);
        log("Successfully validated [ Bulk tagging and Process has been Initiated...] Pop Up Message");
    }

    public void VerifyingBulkUntagMomentActionFilter() throws Exception {
        String startDate = "150320241610", endDate = "040420241602";
        WebDashboard user = getLoginInstance();

        List<String> momentNameList = user.getMomentPage().getMomentNames();
        log("Extracted the momentNames from the MomentTable");

        String response=  user.getMomentPage().bulkUntagActions(momentNameList, startDate, endDate);
        Assert.assertEquals(response, "Process has been Initiated...");
        log("Successfully validated [ Untagging and Process has been Initiated... ] Pop Up Message");
    }

    @Test(priority = 9)
    public void VerifyingMetaDataDropdownByCallingMode_CampaignName_ProcessName_Filters() throws Exception {
        WebDashboard user = getLoginInstance();
        List<String> totalColumns = user.getMomentPage().getAllMomentColumnNames();
        log("Extracted the Moment Table Filter Columns " + totalColumns);

        List<String> columnList = user.getMomentPage().getUncheckedMomentColumnFilterNames();
        log("Extracted the unchecked Columns Successfully");

        user.getMomentPage().searchSelectAddColumn(columnList);
        log("Search and Selected the columns Successfully");

        // BY CAMPAIGN NAME
        List<String> campaignNames =  user.getMomentPage().getMomentMetaDataDropdownValues(CAMPAIGN_NAME.VALUE(),SELECT_CAMPAIGN_NAME.VALUE(),5);
        List<String> searchedList = user.getMomentPage().momentMetaDataFilters(CAMPAIGN_NAME.VALUE(), SELECT_CAMPAIGN_NAME.VALUE(), campaignNames);
        boolean allPresent = true;
        for (String campaignName : campaignNames) {
            boolean found = false;
            for (String searchedElement : searchedList) {
                if (searchedElement.contains(campaignName) || searchedElement.contains("Global Moment")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                allPresent = false;
                break;
            }
        }
        Assert.assertTrue(allPresent,"All Campaign Names are present in the searched elements.");

        //BY CALLING MODE
        List<String> callingModes =  user.getMomentPage().getMomentMetaDataDropdownValues(MODE_OF_CALLING.VALUE(),SELECT_CALLING_MODE.VALUE(),5);
        List<String> searchedList1 =  user.getMomentPage().momentMetaDataFilters(MODE_OF_CALLING.VALUE(),SELECT_CALLING_MODE.VALUE(), callingModes);
        boolean allCallingModePresent = true;
        for (String mode : callingModes) {
            boolean found = false;
            for (String searchedElement : searchedList1) {
                if (searchedElement.contains(mode) || searchedElement.contains("Global Moment")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                allCallingModePresent = false;
                break;
            }
        }
        Assert.assertTrue(allCallingModePresent,"All Calling Modes are present in the searched elements.");

        // BY PROCESS NAME
        List<String> processNames =  user.getMomentPage().getMomentMetaDataDropdownValues(PROCESSNAME.VALUE(),SELECT_PROCESS_NAME.VALUE(),5);
        List<String> searchResults2 = user.getMomentPage().momentMetaDataFilters(PROCESSNAME.VALUE(), SELECT_PROCESS_NAME.VALUE(),processNames);
        boolean allProcessNamePresent = true;
        for (String processName : processNames) {
            boolean found = false;
            for (String searchedElement : searchResults2) {
                if (searchedElement.contains(processName) || searchedElement.contains("Global Moment")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                allProcessNamePresent = false;
                break;
            }
        }
        Assert.assertTrue(allProcessNamePresent,"All Process Names are present in the searched elements.");
    }

    @Test(priority = 10)
    public void VerifyingMomentsDetailsActivationStatusFilter() throws Exception {
        WebDashboard user = getLoginInstance();
        List<String> columnList = user.getMomentPage().getUncheckedMomentColumnFilterNames();;
        user.getMomentPage().searchSelectAddColumn(columnList);

        // ACTIVE
        user.getMomentPage().momentMStatusDetailsFilters(MOMENT_ACTIVATION_STATUS.VALUE(),ACTIVE.VALUE(), ACTIVE.VALUE());
        log("Successfully Applied the [" + MOMENT_ACTIVATION_STATUS.VALUE() + "] filter with the [" + ACTIVE.VALUE() + "] status");
        int actualActiveCount = user.getMomentPage().getStatusCount(ACTIVE.VALUE());
        Assert.assertTrue(actualActiveCount > 0, "Actual count is not greater than 0: " + actualActiveCount);
        log("Successfully validated "+ACTIVE.VALUE()+" actual count (" + actualActiveCount + ") is greater than 0");

        // INACTIVE
        user.getMomentPage().momentMStatusDetailsFilters(MOMENT_ACTIVATION_STATUS.VALUE(),ACTIVE.VALUE(),INACTIVE.VALUE());
        log("Successfully Applied the [" + MOMENT_ACTIVATION_STATUS.VALUE() + "] filter with ["+INACTIVE.VALUE()+"] status");
        int actualInActiveCount = user.getMomentPage().getStatusCount(INACTIVE.VALUE());
        Assert.assertTrue(actualInActiveCount > 0, "Actual count is not greater than 0: " + actualActiveCount);
        log("Successfully validated "+actualActiveCount+" actual count (" + actualInActiveCount + ") is greater than 0");

        // DRAFTED
        user.getMomentPage().momentMStatusDetailsFilters(MOMENT_ACTIVATION_STATUS.VALUE(),INACTIVE.VALUE(),DRAFTED.VALUE());
        log("Successfully Applied the [" + MOMENT_ACTIVATION_STATUS.VALUE() + "] filter with ["+DRAFTED.VALUE()+"] status");
        int actualDraftCount = user.getMomentPage().getStatusCount(DRAFTED.VALUE());
        Assert.assertTrue(actualDraftCount > 0, "Actual count is not greater than 0: " + actualActiveCount);
        log("Successfully validated "+actualActiveCount+" actual count (" + actualDraftCount + ") is greater than 0");
    }

    @Test(priority = 11)
    public void VerifyingMomentDetailsBYMomentTypeDropdownFilters() throws Exception {
        WebDashboard user = getLoginInstance();
        user.getMomentPage().momentMetaDataFilter(MOMENT_TYPE.VALUE(), SELECT_MOMENT_TYPE.VALUE(), KEYWORD.getKey());
        List<String> keywordList = user.getMomentPage().getMomentType(KEYWORD.getKey());
        if (keywordList == null || keywordList.isEmpty()) {
            Assert.fail("Keyword list is null or empty. Failed to retrieve keywords.");
        }
        boolean allKeywordsPresent = true;
        for (String keyword : keywordList) {
            if (!keyword.equals(KEYWORD.getKey())) {
                allKeywordsPresent = false;
                break;
            }
        }
        Assert.assertTrue(allKeywordsPresent, "Not all["+KEYWORD_MOMENT.VALUE()+"] are present");
        log("Successfully validated all["+KEYWORD_MOMENT.VALUE()+"] in the KeywordList");

        user.getMomentPage().momentMetaDataFilter(MOMENT_TYPE.VALUE(),SELECT_MOMENT_TYPE.VALUE(), SEMANTIC_.getKey());
        List<String> semanticList = user.getMomentPage().getMomentType(SEMANTIC_.getKey());
        if (semanticList == null || semanticList.isEmpty()) {
            Assert.fail("Semantic list is null or empty. Failed to retrieve data.");
        }
        boolean allSementicPresent = true;
        for (String moment : semanticList) {
            if (!moment.equals(SEMANTIC_.getKey())) {
                allSementicPresent = false;
                break;
            }
        }
        Assert.assertTrue(allSementicPresent, "Not all [ "+ SEMANTIC_MOMENT.VALUE()+" ] are present");
        log("Successfully validated all [ "+SEMANTIC_MOMENT.VALUE()+"]in the KeywordList");


        user.getMomentPage().momentMetaDataFilter(MOMENT_TYPE.VALUE(),SELECT_MOMENT_TYPE.VALUE(),INSTRUCTIONAL_MOMENT.VALUE());
        List<String> instructionalList = user.getMomentPage().getMomentType(INSTRUCTIONAL.VALUE());
        if (instructionalList == null || instructionalList.isEmpty()) {
            Assert.fail("Instructional list is null or empty. Failed to retrieve data.");
        }
        boolean allInstructionalPresent = true;
        for (String moment : instructionalList) {
            if (!moment.equals(INSTRUCTIONAL.VALUE())) {
                allInstructionalPresent = false;
                break;
            }
        }
        Assert.assertTrue(allInstructionalPresent, "Not all ["+INSTRUCTIONAL_MOMENT.VALUE()+"] are present");
        log("Successfully validated all ["+INSTRUCTIONAL_MOMENT.VALUE()+"] in the KeywordList");
    }

    @Test(priority = 12)
    public void VerifyingMomentDetailsSourceDropdownFilter() throws Exception {
        WebDashboard user = getLoginInstance();
        user.getMomentPage().getAllMomentColumnNames();
        List<String> columnList = user.getMomentPage().getUncheckedMomentColumnFilterNames();;
        user.getMomentPage().searchSelectAddColumn(columnList);
        log("Search,selected and Added  Moment Columns successfully");

        user.getMomentPage().momentMetaDataFilter(SOURCE.VALUE(),SELECT_SOURCE.VALUE(),MOMENTS_SCREEN.VALUE());
        List<String> sourceList = user.getMomentPage().getMomentSourceType(MOMENT_SCREEN.VALUE());
        System.out.println("Source type: " + sourceList.size());

        if (sourceList.isEmpty()) {
            Assert.fail("Instructional list is null or empty. Failed to retrieve data.");
        }
        boolean allSourcePresent = true;
        for (String moment : sourceList) {
            if (!moment.equals(MOMENT_SCREEN.VALUE())) {
                allSourcePresent = false;
                break;
            }
        }
        Assert.assertTrue(allSourcePresent, "Not all ["+MOMENT_SCREEN.VALUE()+"] are present");
        log("Successfully validated all ["+MOMENT_SCREEN.VALUE()+"] in the table");
    }

    @Test(priority = 13)
    public void VerifyingSmartMomentFilter() throws Exception {
        WebDashboard user = getLoginInstance();
        user.getMomentPage().smartMomentFilter(SMARTMOMENTS.VALUE(), SHOW_ONLY_SMARTMOMENTS.VALUE());
        List<String> filterResult =  user.getMomentPage().getSmartCluster();

        System.out.println(filterResult);

        boolean allPreset = true;
        for(String filter : filterResult){
            if (!filter.equals(SMART_CLUSTER_SCREEN.VALUE())) {
                allPreset = false;
                break;
            }
        }
        Assert.assertTrue(allPreset, "Not all ["+SMART_CLUSTER_SCREEN.VALUE()+"] are present");
        log("Successfully validated all ["+SMARTCLUSTER_SCREEN.VALUE()+"] in the table");
    }
}
