package com.convozen.Tests;
import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.DashboardWeb;
import com.convozen.Pages.Playwrights.ConvozenWebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.Utils.FileUtil.getProperty;

@Listeners(TestListeners.class)
public class TestCallpageFilters extends BaseTest {
    public DashboardWeb getLoginInstance() throws Exception {
        ConvozenWebLogin webLogin = getWebLogin();
        DashboardWeb dashboard = webLogin.convozenLogin(
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME),
                getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD)
        );
        dashboard.getWebLogins().selectPage(CALLS.VALUE());
        Assert.assertTrue(dashboard.getWebLogins().isPageOpenSuccessfully(), "Failed to load " + CALLS.VALUE() + " page.");
        log("Navigated on the " + CALLS.VALUE() + " Page");
        return dashboard;
    }

    @Test(priority = 1)
    public void VerifyingDefaultAppliedCallsFilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        String defaultFilters =  user.getCallPage().getDefaultAppliedFilters();
        boolean conditionMet = defaultFilters.equals("Call Duration:> 30 secs,View By:Yesterday,Transcribed Calls,") ||
                defaultFilters.equals("Call Duration:> 30 secs,View By:Today,Transcribed Calls,Processed,");

        Assert.assertTrue(conditionMet, "Default applied filters are not as expected. Actual: " + defaultFilters);
        log("Successfully validated the default applied filters: [" + defaultFilters + "]");
    }


    @Test(priority = 2)
    public void VerifyingColumnFiltersFunctionality() throws Exception {
        List<String> columnList = List.of("Campaign Name", "Mode of Calling", "Process Name");

        DashboardWeb user = getLoginInstance();
        List<String> originalColumnList = user.getCallPage().getCallsTableDefaultColumns();
        user.getCallPage().searchSelectAddColumn(columnList);
        log("Searched, selected and Added the columns " + columnList);

        List<String> actualTotalColumns = user.getCallPage().getTableColumns();
        Assert.assertEquals(actualTotalColumns.size(), originalColumnList.size() + columnList.size(), "Total columns do not match after addition.");
        log("Successfully validated the Actual and Expected Column Counts");

        user.getCallPage().removeColumns(columnList);
        log("Searched, selected and Removed the columns " + columnList);

        List<String> columnsAfterRemoving = user.getCallPage().getTableColumns();
        log("Columns after removal: " + columnsAfterRemoving);

        Assert.assertEquals(originalColumnList.size(), columnsAfterRemoving.size(), "Total columns do not match after removal.");
        log("Successfully validated the column count before and after applying column filters.");
    }

    @Test(priority = 3)
    public void VerifyingSortCallsByDurationFilterFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();
        int initialCallDuration = user.getCallPage().getCallDuration(TODAY.VALUE());
        log("Extracted the default call duration Successfully: " + initialCallDuration + " seconds");

        int afterTimeFilter = user.getCallPage().getSortByCallTimeFilter(CALL_TIME.VALUE());
        log("Sorted calls by time using filter [" + CALL_TIME.VALUE() + "] Successfully");
        Assert.assertTrue(afterTimeFilter > initialCallDuration, "Validated that call duration after applying time filter is greater than initial duration");

        int afterDuration = user.getCallPage().getSortByCallDurationFilter(CALL_DURATION.VALUE());
        log("Sorted calls by duration using filter [" + CALL_DURATION.VALUE() + "] Successfully");
        Assert.assertTrue(afterDuration > afterTimeFilter, "Validated that call duration after applying duration filter is greater than after applying time filter");
    }


    @Test(priority = 4)
    public void VerifyingViewByCallFilters() throws Exception {
        List<String> filterByList = Arrays.asList("Today", "Yesterday", "Last Week", "Last 1 Month", "Last 2 Months", "Last 3 Months");

        DashboardWeb user = getLoginInstance();
        List<Integer> callCount = user.getCallPage().viewByFilters(filterByList);
        Assert.assertTrue(callCount.size() >= 2, "Insufficient call count data");
        for (int i = 1; i < callCount.size(); i++) {
            int currentCount = callCount.get(i);
            int previousCount = callCount.get(i - 1);
            Assert.assertTrue(currentCount >= previousCount, "Call counts are not in incrementing order.");
            log("current count :" + currentCount + " previous count :" + previousCount);
        }
        log("Successfully validated the view By Filters");
    }


    @Test(priority = 5)
    public void VerifyingCallerDetailsFiltersByCustomerID_PhoneNumber_AgentName_CustomerMail() throws Exception {
        List<String> columnList = List.of("Campaign Name", "Mode of Calling", "Process Name", "Customer Id", "Customer Email");
        DashboardWeb user = getLoginInstance();

        user.getCallPage().searchSelectAddColumn(columnList);
        log("Search and Selected the " + columnList + "Successfully");

        List<String> customerIDs = user.getCallPage().getCustomerIds();
        System.out.println("Customer IDs: " + customerIDs);

        List<String> searchResult = user.getCallPage().callDetailsFilters(CUSTOMER_ID.VALUE(), ENTER_CUSTOMER_ID.VALUE(), customerIDs);
        Assert.assertEquals(searchResult, customerIDs, "Customer Id Filter Failed");
        log("Successfully validate the Actual " + searchResult + ", With Expected " + customerIDs);

        List<String> searchByPhone = user.getCallPage().callDetailsFilters(CUSTOMER_PHONE.VALUE(), ENTER_CUSTOMER_PHONE.VALUE(), Arrays.asList("914525288430"));
        String endingDigit = searchByPhone.toString().substring(11, 13);

        Assert.assertTrue("914525288430".contains(endingDigit), "Phone number ending with '" + endingDigit + "' exists in the list");
        log("Successfully validate the Actual " + "914525288430" + ", With Expected Ending" + endingDigit);

        List<String> agentNames = user.getCallPage().getAgentNames();
        List<String> agentNameResult = user.getCallPage().callDetailsFilters(AGENT_NAME.VALUE(), ENTER_AGENT_NAME.VALUE(), agentNames);

        Assert.assertEquals(agentNameResult, agentNames, "Agent Name Filter Failed");
        log("Successfully validate the Actual " + agentNameResult + ", With Expected " + agentNames);

        List<String> customerEmails = user.getCallPage().getCustomerEmails();
        System.out.println("  EMails : " + customerEmails);

        List<String> customerEmailResult = user.getCallPage().callDetailsFilters(CUSTOMER_EMAIL.VALUE(), ENTER_CUSTOMER_EMAIL.VALUE(), customerEmails);
        Assert.assertEquals(customerEmailResult, customerEmails, "Customer Email Filter Failed");
        log("Successfully validate the Actual " + customerEmailResult + ", With Expected " + customerEmails);
    }

    @Test(priority = 6)
    public void VerifyingCallsByCallIDFilter() throws Exception {
        DashboardWeb user = getLoginInstance();
        user.getCallPage().removeTableCols();

        List<String> callIdList = user.getCallPage().getCallRecordIds();
        List<String> searchedCallIDs = user.getCallPage().getCallDetailsByCallId(CALL_ID.VALUE(), callIdList);

        Assert.assertEquals(searchedCallIDs, callIdList, "Call Id Filter Failed");
        Assert.assertNotNull(searchedCallIDs);
        log("Successfully validated " + callIdList + "With Searched " + searchedCallIDs);
    }


    @Test(priority = 7)
    public void VerifyingCallsTalkTimeFilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        String timeText = user.getCallPage().applyTalktimeOverlapSilenceFilter(TALKTIME.VALUE(), GRETER_THAN_OR_EQUAL_TO.VALUE(), "05", "30", null, null);
        String timeText1 = timeText.replaceAll("\\s", "").substring(0, timeText.length() - 4);

        LocalTime EnteredTime = LocalTime.parse("05:30", formatter);
        LocalTime ActualTime = parseTime(timeText1.trim(), formatter);
        Assert.assertNotNull(ActualTime, "Actual time parsing failed.");
        Assert.assertTrue(ActualTime.isAfter(EnteredTime), "Actual time should be greater than or equal to the Entered time.");
        log("Successfully validated the Actual [" + EnteredTime + " with " + ActualTime + "]");

        String timeMin = user.getCallPage().applyTalktimeOverlapSilenceFilter(TALKTIME.VALUE(), LESS_THAN_OR_EQUAL_TO.VALUE(), "05", "30", null, null);
        String timeMin1 = timeMin.replaceAll("\\s", "").substring(0, timeMin.length() - 4);
        LocalTime ActualTimeLessThanOrEqualTo = parseTime("00:" + timeMin1, formatter);
        Assert.assertNotNull(ActualTimeLessThanOrEqualTo, "Actual time parsing failed.");
        Assert.assertTrue(ActualTimeLessThanOrEqualTo.isBefore(EnteredTime), "Actual time should be less than or Equal to entered time.");
        log("Successfully validated the Actual [" + EnteredTime + " with " + ActualTimeLessThanOrEqualTo + "]");

        String betweenTime = user.getCallPage().applyTalktimeOverlapSilenceFilter(TALKTIME.VALUE(), BETWEEN.VALUE(), "05", "30", "7", "30");
        String betweenTime1 = betweenTime.replaceAll("\\s", "").substring(0, betweenTime.length() - 4);

        LocalTime EnteredBeetFrom = LocalTime.parse("05:30", formatter);
        LocalTime EnteredBeetTo = LocalTime.parse("07:30", formatter);
        LocalTime ActualBetweenTime = parseTime(betweenTime1.trim(), formatter);
        Assert.assertNotNull(ActualBetweenTime, "Actual time parsing failed.");
        Assert.assertTrue(ActualBetweenTime.isAfter(EnteredBeetFrom) && ActualBetweenTime.isBefore(EnteredBeetTo), "ActualTime should be between EnteredFrom and EnteredTo");
        log("Successfully validated TalkTime filter with [" + BETWEEN.VALUE() + "] Condition.");
    }

    private LocalTime parseTime(String timeText, DateTimeFormatter formatter) {
        try {
            return LocalTime.parse(timeText, formatter);
        } catch (DateTimeParseException e) {
            log("Failed to parse time: " + timeText + " with error: " + e.getMessage());
            return null;
        }
    }

    @Test(priority = 8)
    public void VerifyingCallsOverLapCallDurationFilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        String overLapGreaterThanEqualTo = user.getCallPage().applyTalktimeOverlapSilenceFilter(OVERLAP_DURATION.VALUE(), GRETER_THAN_OR_EQUAL_TO.VALUE(), "01", "30", null, null);
        Assert.assertNotNull(overLapGreaterThanEqualTo, "Overlap Call Duration should not be null");
        log("Successfully validated Overlap Call Duration Filter with [" + GRETER_THAN_OR_EQUAL_TO.VALUE() + "] Condition.");

        String overLapLessThanEqualTo = user.getCallPage().applyTalktimeOverlapSilenceFilter(OVERLAP_DURATION.VALUE(), LESS_THAN_OR_EQUAL_TO.VALUE(), "01", "30", null, null);
        Assert.assertNotNull(overLapLessThanEqualTo, "Overlap Call Duration should not be null");
        log("Successfully validated Overlap Call Duration Filter with [" + LESS_THAN_OR_EQUAL_TO.VALUE() + "] Condition.");

        String overLapBetween = user.getCallPage().applyTalktimeOverlapSilenceFilter(OVERLAP_DURATION.VALUE(), BETWEEN.VALUE(), "01", "30", "2", "30");
        Assert.assertNotNull(overLapBetween, "Overlap Call Duration should not be null");
        log("Successfully validated Overlap Call Duration Filter with [" + BETWEEN.VALUE() + "] Condition.");
    }

    @Test(priority = 9)
    public void VerifyingCallsBySilenceDurationFilter() throws Exception {
        DashboardWeb user = getLoginInstance();
        String silenceGreaterThanEqualTO = user.getCallPage().applyTalktimeOverlapSilenceFilter(SILENCE_DURATION.VALUE(), GRETER_THAN_OR_EQUAL_TO.VALUE(), "1", "30", null, null);
        Assert.assertNotNull(silenceGreaterThanEqualTO, "silence Duration Call Duration should not be null");

        String silenceLessThanEqualTO = user.getCallPage().applyTalktimeOverlapSilenceFilter(SILENCE_DURATION.VALUE(), LESS_THAN_OR_EQUAL_TO.VALUE(), "1", "30", null, null);
        Assert.assertNotNull(silenceLessThanEqualTO, "silence Duration Call Duration should not be null");

        String overLapBetween = user.getCallPage().applyTalktimeOverlapSilenceFilter(SILENCE_DURATION.VALUE(), BETWEEN.VALUE(), "01", "30", "2", "30");
        Assert.assertNotNull(overLapBetween, "Silence Call Duration should not be null");
        log("Successfully validated Silence Call Duration Filter with [" + BETWEEN.VALUE() + "] Condition.");
    }

    @Test(priority = 10)
    public void VerifyingCallsByTranscriptionStatusAndCallProgressFilters() throws Exception {
        DashboardWeb user = getLoginInstance();
        String actualTranscribedText = user.getCallPage().getTranscribedProcessedCalls(TRANSCRIPTION_STATUS.VALUE());
        Assert.assertEquals(actualTranscribedText, ("Transcribed Calls"), "Applied Filter text not found in" + actualTranscribedText);
        log("Successfully validated the[" + TRANSCRIPTION_STATUS.VALUE() + "] Filter");

        String processedText = user.getCallPage().getTranscribedProcessedCalls(CALL_PROGRESS.VALUE());
        Assert.assertEquals(processedText, ("Processed"), "Applied Filter text not found in" + processedText);
        log("Successfully validated the[" + CALL_PROGRESS.VALUE() + "] Filter");
    }

    @Test(priority = 11)//,retryAnalyzer = RetryAnalyzer.class)
    public void VerifyingByMomentNameByHighlight_Language_checklistName_CallScore_Filters() throws Exception {
        String score = "30", endScore = "70";
        List<String> momentsList = Arrays.asList("Salutation");
        DashboardWeb user = getLoginInstance();

        user.getCallPage().insightsFilters(MOMENTS.VALUE(), SELECT_MOMENT_NAME.VALUE(), momentsList);
        List<String> taggedMoments = user.getCallPage().getTaggedMoments();
        Assert.assertTrue(taggedMoments.containsAll(momentsList), "Not all moments are present in tagged moments.");
        log("Successfully Validated the Selected " + momentsList + " and Tagged " + taggedMoments);

        user.getCallPage().insightsFilters(HEIGHLIGHTS.VALUE(), SELECT_HEIGHLIGHTS.VALUE(), List.of("Long Silence"));
        List<String> silenceList = user.getCallPage().getLongSilence();
        Assert.assertTrue(silenceList.containsAll(List.of("Long Silence")), "Not all moments are present in long silence.");
        Assert.assertNotNull(silenceList);

        user.getCallPage().insightsFilters(LANGUAGE.VALUE(), SELECT_LANGUAGE.VALUE(), List.of("Hindi"));
        List<String> searchLanguages = user.getCallPage().getLanguages();
        Assert.assertTrue(searchLanguages.containsAll(List.of("Hindi")), "Not found language in search Result.");

        List<String> checklistsName = user.getCallPage().getCallScoreChecklist(CALL_SCORE.VALUE(), GRETER_THAN_OR_EQUAL_TO.VALUE(), score);
        user.getCallPage().insightsFilters(CHECKKISTS.VALUE(), SELECT_CHECKKIST_NAME.VALUE(), checklistsName);

        System.out.println(checklistsName + " -----------------------");

        List<String> taggedCheklist = user.getCallPage().getTaggedChecklist();
        Assert.assertNotNull(taggedCheklist);
        Assert.assertTrue(taggedCheklist.containsAll(checklistsName), "Not found checklist in search Result.");

        String callScore = user.getCallPage().getCallScore(CALL_SCORE.VALUE(), GRETER_THAN_OR_EQUAL_TO.VALUE(), score, null);
        Assert.assertTrue(Integer.parseInt(callScore) >= Integer.parseInt(score), "Score must be greater than or equal to");
        log("Successfully validated Call Score with [" + GRETER_THAN_OR_EQUAL_TO.VALUE() + "] condition input [" + score + "],Output[" + callScore + "]");

        String callScores = user.getCallPage().getCallScore(CALL_SCORE.VALUE(), LESS_THAN_OR_EQUAL_TO.VALUE(), score, null);
        Assert.assertTrue(Integer.parseInt(callScores) <= Integer.parseInt(score), "Score must be Less than or equal to");
        log("Successfully validated Call Score with [" + GRETER_THAN_OR_EQUAL_TO.VALUE() + "] condition input [" + score + "], Output[" + callScores + "]");

        String betweenCallScore = user.getCallPage().getCallScore(CALL_SCORE.VALUE(), BETWEEN.VALUE(), score, endScore);
        System.out.println("betweenCallScore" + betweenCallScore);
        int betweenScore = Integer.parseInt(betweenCallScore), startRange = Integer.parseInt(score), endRange = Integer.parseInt(endScore);
        Assert.assertTrue(betweenScore >= startRange && betweenScore <= endRange, "Score must be between " + startRange + " and " + endRange);
        log("Successfully validated Call Score with [" + BETWEEN.VALUE() + "] condition input [" + score + " - " + endScore + "], Output[" + betweenCallScore + "]");
    }

    @Test(priority = 12)
    public void VerifyingMetaDataFiltersByCampaignName_CallingMode_ProcessNane_DisposeName() throws Exception {
        List<String> columnList = List.of("Campaign Name", "Mode of Calling", "Process Name");
        DashboardWeb user = getLoginInstance();

        user.getCallPage().searchSelectAddColumn1(columnList);
        log("Successfully Selected the columns :  " + columnList);

        List<String> campaignNameList =   user.getCallPage().getMetaDataDropdownValues(CAMPAIGN_NAME.VALUE(), SELECT_CAMPAIGN_NAME.VALUE(), 5);
        List<String> searchedCampaignList =   user.getCallPage().callsMetaDataFilters(CAMPAIGN_NAME.VALUE(), SELECT_CAMPAIGN_NAME.VALUE(), campaignNameList, 5);
        Assert.assertTrue(campaignNameList.containsAll(searchedCampaignList));
        log("Successfully Validated CampaignNames " + searchedCampaignList + " with " + campaignNameList);

        List<String> callingModeList =   user.getCallPage().getMetaDataDropdownValues(MODE_OF_CALLING.VALUE(), SELECT_CALLING_MODE.VALUE(), 5);
        List<String> searchedCallingMode =   user.getCallPage().callsMetaDataFilters(MODE_OF_CALLING.VALUE(), SELECT_CALLING_MODE.VALUE(), callingModeList, 5);
        Assert.assertTrue(callingModeList.containsAll(searchedCallingMode));
        log("Successfully Validated CampaignNames" + callingModeList + " with " + searchedCallingMode);

        List<String> processNameList =   user.getCallPage().getMetaDataDropdownValues(PROCESSNAME.VALUE(), SELECT_PROCESS_NAME.VALUE(), 5);
        List<String> searchedprocessNameList =   user.getCallPage().callsMetaDataFilters(PROCESSNAME.VALUE(), SELECT_PROCESS_NAME.VALUE(), processNameList, 5);
        Assert.assertTrue(processNameList.containsAll(searchedprocessNameList));
        log("Successfully Validated CampaignNames" + searchedprocessNameList + " with " + searchedprocessNameList);
    }

    @Test(priority = 13)
    public void VerifyingTaggedMomentCount() throws Exception {
        DashboardWeb user = getLoginInstance();

        List<Integer> tableTaggedMomentCount = user.getCallPage().getTableMomentEmotions();
        List<Integer> actualCallMomentTotalCount = user.getCallPage().getCallMomentsEmotions();
        System.out.println("Table Moment Count: " + tableTaggedMomentCount);
        System.out.println("Actual Moment Count: " + actualCallMomentTotalCount);

        Assert.assertEquals(tableTaggedMomentCount, actualCallMomentTotalCount, "Actual Moment Count Not Matched ");
        log("Successfully Validated " + actualCallMomentTotalCount + " with " + tableTaggedMomentCount);
    }


    @Test(priority = 14)
    public void VerifyingCallScoreOfTaggedChecklist() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<Integer> callScoresList = user.getCallPage().getTableCallScore(5);
        List<Integer> list = user.getCallPage().getChecklistScore(5);

        Assert.assertEquals(callScoresList, list, "All call scores are Not equal");
        log("Successfully validated Displayed " + callScoresList + " call Score Number and calculated call Score" + list);
    }

    @Test(priority = 15)
    public void VerifyingTaggedMomentsInChecklistOfCallTableDataWithActualCallVerifying() throws Exception {
        DashboardWeb user = getLoginInstance();

        user.getCallPage().applyViewByFilter("Last Week");
        List<String> checklistCallTable = user.getCallPage().getCallTableChecklists();
        List<String> taggedChecklistInCall = user.getCallPage().getTaggedChecklists();
        Assert.assertEquals(checklistCallTable, taggedChecklistInCall);
        log("Successfully Validated checklists " + checklistCallTable + " with " + taggedChecklistInCall);

        List<String> callTableMoments = user.getCallPage().getMomentsFromCallTable();
        List<String> taggedMoments = user.getCallPage().getTaggedMomentCount();
        Assert.assertEquals(callTableMoments.size(), taggedMoments.size());
        log("Successfully Validated moments " + callTableMoments + " with " + taggedMoments);
    }

    @Test(priority = 16)
    public void VerifyingGenerateCallSummary() throws Exception {
        DashboardWeb user = getLoginInstance();
        boolean ans = user.getCallPage().generateCallSummary(SUMMARY.VALUE());
        Assert.assertFalse(ans, "Failed to Generate Summary");
        log("Successfully Generated the Call Summary");
    }

    @Test(priority = 17)
    public void VerifyingGenerateCallSummary_under_Interaction_History() throws Exception {
        DashboardWeb user = getLoginInstance();
        List<Boolean> isInteractionSummaryGenerated = user.callsPage.generateIntractionHistoryCallSummary(INTRACTION_HISTORY.VALUE());
        for (Boolean element : isInteractionSummaryGenerated) {
            if (!element) {
                Assert.fail("Failed to Generate Summary");
            }
        }
        log("Successfully Generated the Call Summary");
    }

    @Test(priority = 18)
    public void VerifyingCallRecordingAudioAndOtherButtonFunctionality() throws Exception {
        DashboardWeb user = getLoginInstance();
        user.getCallPage().actionOnCallRecording();
        log("Successfully verified the Call Recording options");
    }


    public ConvozenWebLogin getWebLogin() {
        getPlaywrightBrowser();
        page.navigate(getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        log("Opening URL: " + getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL));
        return new ConvozenWebLogin(page);
    }
}
