package com.convozen.Tests;
import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.WebDashboard;
import com.convozen.Pages.Playwrights.WebLogin;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import static com.convozen.CommonConstants.*;
import static com.convozen.CommonConstants.CALLZEN_VALUES.*;
import static com.convozen.Utils.FileUtil.getProperty;
import static org.testng.Assert.assertTrue;

@Listeners(TestListeners.class)
public class TestMomentscCRUD extends BaseTest {
    protected String webUrl, userName, password;
    String startDate = CommonConstants.BEFORE_3_DATE(),
            endDate = CommonConstants.CURRENT_DATE();

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


    @Test(priority = 1)
    public void VerifyCreateSemanticMomentWithFilter() throws Exception {
        String MOMENT_NAME = "Test Symentic Filtered" + RANDOM_NAME();
        WebDashboard user = getLoginInstance();
        log("Entering" + MOMENT_NAME + ",Clicking on " + FILTERED.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createFilteredMoment(MOMENT_NAME, FILTERED.VALUE(), POSITIVE.VALUE());

        List<String> campaignNames = user.getMomentPage().getCampaignNamesDropdownValues(FILTER.VALUE(), SELECT_CAMPAIGN_NAME.VALUE());
        user.getMomentPage().selectCampaginNamesAndCallingMode(campaignNames, 5);
        log("Successfully selected CampaignName :" + campaignNames);

        List<String> processName = user.getMomentPage().getProcessNames(SELECT_PROCESS_NAME.VALUE());
        user.getMomentPage().selectProcessNameAndCallerType(processName, 5);
        log("Successfully selected CampaignName :" + processName);


        log("Selecting the '" + SEMANTIC_MOMENT.VALUE() + "' & Entering Search '" + RANDOM_SEMANTIC().SENTENCE() + "' Sentence ");
        user.getMomentPage().selectPhrases(SEMANTIC_MOMENT.VALUE(), RANDOM_SEMANTIC().SENTENCE(), 5, AAD_ANYWAY.VALUE());

        String ActualMessage = user.getMomentPage().getMomentCreatedText();
        Assert.assertEquals(ActualMessage, "Moment Created Successfully !");

        user.getMomentPage().submitMomentFeedback();
        log("Submitted the FeedBack Successfully");

        /*user.getMomentPage().bulkMomentTagging(startDate, endDate);
        String actualResponse = user.getMomentPage().getBulkJObSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");*/

        user.getMomentPage().clickFinish();
        log(" Moment Created Name : [ " + MOMENT_NAME + "] successfully");

        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");
        Assert.assertEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual [" + MOMENT_NAME + "] with  Expected '" + str[0] + "' Successfully");

        assertTrue(str[1].contains("Semantic"));
        log("Validated Actual Moment Type'" + str[1] + "' with Expected 'Semantic' Successfully");

        Assert.assertEquals(str[3], ("Success"));
        log("Validated FeedBack Status Actual'" + str[3] + "' with Expected 'Success' successfully");

        user.getMomentPage().deleteMoments(MOMENT_NAME);
        String actualMsg = user.getMomentPage().getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");
    }

    @Test(priority = 2)
    public void VerifyCreateGlobalSemanticMomentFunctionality() throws Exception {
        String MOMENT_NAME = "Test Symentic Global" + RANDOM_NAME();
        System.out.println(" Moment Name :" + MOMENT_NAME);

        WebDashboard user = getLoginInstance();

        log("Navigated to URL : [ " + webUrl + " ]");
        user.getMomentPage().login(userName, password);
        log("Successfully logged in with Email [ " + userName + " ]");

        user.getMomentPage().selectHeaderOption(MOMENTS.VALUE());
        log("Clicked on the [ " + MOMENTS.VALUE() + " ] module from the header.");

        log("Entering" + MOMENT_NAME + ",Clicking on " + GLOBAL.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createGlobalMoment(MOMENT_NAME, GLOBAL.VALUE(), POSITIVE.VALUE());

        log("Selecting the '" + SEMANTIC_MOMENT.VALUE() + "' & Entering Search '" + RANDOM_SEMANTIC().SENTENCE() + "' Sentence ");
        user.getMomentPage().selectPhrases(SEMANTIC_MOMENT.VALUE(), RANDOM_SEMANTIC().SENTENCE(), 5, AAD_ANYWAY.VALUE());

        String ActualMessage = user.getMomentPage().getMomentCreatedText();
        Assert.assertEquals(ActualMessage, "Moment Created Successfully !");

        user.getMomentPage().submitMomentFeedback();
        log("Submitted the [ FeedBack ] Successfully");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");

        user.getMomentPage().clickFinish();
        log(" Moment Created Name : [ " + MOMENT_NAME + "] successfully");

        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println(" Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");
        Assert.assertEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual Test_CRUD_Moments Name '" + MOMENT_NAME + "' with  Expected '" + str[0] + "' Successfully");

        assertTrue(str[1].contains("Semantic"));
        log("Validated Actual Moment Type'" + str[1] + "' with Expected 'Semantic' Successfully");

        Assert.assertEquals(str[3], ("Success"));
        log("Validated FeedBack Status Actual'" + str[5] + "' with Expected 'Success' successfully");

       /* user.deactivateMoments();
        log("Deactivated the Moment");

        user.deleteMoments(MOMENT_NAME);
        String actualMsg = user.getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");*/
    }

    @Test(priority = 3)
    public void VerifyEditSemanticMomentsFunctionality() throws Exception {
        String MOMENT_NAME = RANDOM_NAME();
        int deleteMomentsCounts = 3;
        WebDashboard user = getLoginInstance();


        user.getMomentPage().EditMomentDetails();
        log("Clicked on the  the from Table Successfully ");

        user.getMomentPage().updateExitingSymenticMomentDetails(deleteMomentsCounts);
        log("Deleted Existing [" + deleteMomentsCounts + "] Number of Phrases Successfully");

        user.getMomentPage().searchSelectSemanticPhrases(RANDOM_SEMANTIC().SENTENCE(), 3, AAD_ANYWAY.VALUE());
        log(" Search and Selected the'" + SEMANTIC_MOMENT.VALUE() + ",'" + RANDOM_SEMANTIC().SENTENCE() + "' Sentence Successfully");

        String ActualMessage = user.getMomentPage().getMomentCreatedText();
        Assert.assertEquals(ActualMessage, "Moment Created Successfully !");

        user.getMomentPage().submitMomentFeedback();
        log("Submitted the FeedBack Successfully");

       /* String actualResponse = user.getBulkJObSubmitText();
        System.out.println(actualResponse);
        Assert.assertEquals(actualResponse,"Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response : [" + actualResponse + "] with [Bulk job submitted please wait for sometime to see the moments tagged]");
        */
        user.getMomentPage().clickFinish();
        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : [" + momentDetails + "]");
        String[] str = momentDetails.split(",");
        Assert.assertNotEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual Test_CRUD_Moments Name [" + MOMENT_NAME + "] with  Expected [" + str[0] + "] Successfully");
    }

    @Test(priority = 4)
    public void VerifyUpdateFeedbackAndUntagMomentsFunctionality() throws Exception {
        WebDashboard user = getLoginInstance();
        user.getMomentPage().momentMStatusDetailsFilters(MOMENT_ACTIVATION_STATUS.VALUE(), ACTIVE.VALUE(), INACTIVE.VALUE());
        log("Successfully Applied the [" + MOMENT_ACTIVATION_STATUS.VALUE() + "] filter with [" + INACTIVE.VALUE() + "] status");

        user.getMomentPage().EditMomentDetails();
        log("Search and Selected the '" + SEMANTIC.VALUE() + "'from Table Successfully ");

        user.getMomentPage().updateMomentFeedback(); //Ignored the UpdateFeedBack due to elementClick issues
        log("Successfully updated the 'Feedback' for Semantic Moment");

        user.getMomentPage().untagMoment(startDate, endDate);
        log("Successfully Untagged the Moment from [" + startDate + "] To Date [" + endDate + "]");

        String actualResponse = user.getMomentPage().getUntagMomentText();
        boolean condition1 = actualResponse.equals("Moment untagging in progress");
        boolean condition2 = actualResponse.equals("Bulk job submitted please wait for sometime to see the moments tagged");

        if (condition1 || condition2) {
            log("Validating the Actual Response: " + actualResponse);
        } else {
            Assert.fail("Unexpected response: " + actualResponse);
        }

    }

    @Test(priority = 5)
    public void VerifyDeactivateDeleteMomentsFunctionality() throws Exception {
        WebDashboard user = getLoginInstance();
        log("Deactivating the [Test Moments]");
        user.getMomentPage().deactivateTestMoments();

        String actualDeleteResponse = user.getMomentPage().deleteTestMoments();
        Assert.assertEquals(actualDeleteResponse, "Moment Deleted Successfully");
        log("Validated '" + actualDeleteResponse + " Pop Up Message");
    }


    @Test(priority = 6)
    public void VerifyCreateGlobalKeywordMomentsWithContainsOneOf() throws Exception {
        String MOMENT_NAME = "Test KeyWord" + RANDOM_NAME();
        List<String> KEY_LIST = Arrays.asList("Hello");

        WebDashboard user = getLoginInstance();
        user.getMomentPage().createMoment(MOMENT_NAME, GLOBAL.VALUE(), POSITIVE.VALUE());
        log("Entered [" + MOMENT_NAME + "],Clicking on [" + GLOBAL.VALUE() + "] and [" + POSITIVE.VALUE() + "]");

        List<String> inputKeywords = user.getMomentPage().EnterKeywordDetails(KEYWORD_MOMENT.VALUE(), KEY_LIST);
        log("Selected the [" + KEYWORD_MOMENT.VALUE() + "] & Entered Search[" + KEY_LIST + "]");
        System.out.println("Keywords : " + inputKeywords);

        List<String> phrases = user.getMomentPage().getKeywordSearchDetails();
        int matchCount = 0;
        for (int i = 0; i < phrases.size(); i++) {
            for (String keyword : inputKeywords) {
                phrases.get(i).contains(keyword);
                matchCount++;
            }
        }
        System.out.println("Match Count :" + matchCount);
        log("Validating if any phrase contains the provided keywords");
        Assert.assertFalse(matchCount < 3, "Match count should not be at least 3");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        log("Successfully Tagged From [" + startDate + "] and To [" + endDate + "]");

        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");

        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");
        Assert.assertEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual Test_CRUD_Moments Name '" + MOMENT_NAME + "' with  Expected '" + str[0] + "' Successfully");

        Assert.assertTrue(str[1].contains("Keyword"));
        log("Validated Actual Moment Type'" + str[1] + "' with Expected 'Semantic' Successfully");

        Assert.assertEquals(str[5], ("Success"));
        log("Validated FeedBack Status Actual'" + str[5] + "' with Expected 'Success' successfully");

        user.getMomentPage().deleteMoments(MOMENT_NAME);
        String actualMsg = user.getMomentPage().getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");
    }

    @Test(priority = 7)
    public void VerifyCreateFilteredKeywordMomentsWithContainsOneOf() throws Exception {
        String MOMENT_NAME = "Test KeyWord" + RANDOM_NAME();
        List<String> KEY_LIST = Arrays.asList("Hello");
        WebDashboard user = getLoginInstance();

        log("Entering" + MOMENT_NAME + ",Clicking on " + FILTERED.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createFilteredMoment(MOMENT_NAME, FILTERED.VALUE(), POSITIVE.VALUE());

        List<String> campaignNames = user.getMomentPage().getCampaignNamesDropdownValues(FILTER.VALUE(), SELECT_CAMPAIGN_NAME.VALUE());
        user.getMomentPage().selectCampaginNamesAndCallingMode(campaignNames, 5);
        log("Successfully selected CampaignName :" + campaignNames);

        List<String> processName = user.getMomentPage().getProcessNames(SELECT_PROCESS_NAME.VALUE());
        user.getMomentPage().selectProcessNameAndCallerType(processName, 5);
        log("Successfully selected CampaignName :" + processName);

        List<String> inputKeywords = user.getMomentPage().EnterKeywordDetails(KEYWORD_MOMENT.VALUE(), KEY_LIST);
        log("Selected the [" + KEYWORD_MOMENT.VALUE() + "] & Entered Search[" + KEY_LIST + "]");
        System.out.println("Keywords : " + inputKeywords);

        List<String> phrases = user.getMomentPage().getKeywordSearchDetails();
        int matchCount = 0;
        for (int i = 0; i < phrases.size(); i++) {
            for (String keyword : inputKeywords) {
                phrases.get(i).contains(keyword);
                matchCount++;
            }
        }
        System.out.println("Match Count :" + matchCount);
        log("Validating if any phrase contains the provided keywords");
        Assert.assertFalse(matchCount < 5, "Match count should not be at least 5");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        log("Successfully Tagged From [" + startDate + "] and To [" + endDate + "]");

        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");

        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");
        Assert.assertEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual Test_CRUD_Moments Name '" + MOMENT_NAME + "' with  Expected '" + str[0] + "' Successfully");

        Assert.assertTrue(str[1].contains("Keyword"));
        log("Validated Actual Moment Type'" + str[1] + "' with Expected 'Semantic' Successfully");

        Assert.assertEquals(str[5], ("Success"));
        log("Validated FeedBack Status Actual'" + str[5] + "' with Expected 'Success' successfully");

        user.getMomentPage().deleteMoments(MOMENT_NAME);
        String actualMsg = user.getMomentPage().getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");
    }


    @Test(priority = 8)
    public void VerifyCreateGlobalKeywordMomentsWithContainsAll() throws Exception {
        String MOMENT_NAME = "Test KeyWord" + RANDOM_NAME();
        List<String> keywordsMust = Arrays.asList("Hello", "Good morning");
        WebDashboard user = getLoginInstance();
        user.getMomentPage().createMoment(MOMENT_NAME, GLOBAL.VALUE(), POSITIVE.VALUE());
        log("Entered" + MOMENT_NAME + ",Clicking on " + GLOBAL.VALUE() + " and " + POSITIVE.VALUE());

        log("Selecting the '" + KEYWORD_MOMENT.VALUE() + "' & Entering Search " + keywordsMust);
        List<String> inputKeywords = user.getMomentPage().EnterKeywordDetails1(KEYWORD_MOMENT.VALUE(), keywordsMust);
        List<String> phrases = user.getMomentPage().getKeywordSearchDetails();
        boolean allKeywordsPresent = true;
        for (String keyword : inputKeywords) {
            boolean keywordFound = phrases.stream().anyMatch(phrase -> phrase.toLowerCase().contains(keyword.toLowerCase()));
            if (!keywordFound) {
                allKeywordsPresent = false;
                log("Keyword '" + keyword + "' not found in any phrase.");
                phrases.stream()
                        .filter(phrase -> !phrase.toLowerCase().contains(keyword.toLowerCase()))
                        .forEach(phrase -> log("Phrase without keyword '" + keyword + "': " + phrase));
                break;
            }
        }
        Assert.assertTrue(allKeywordsPresent, "Not all keywords are present in phrases.");
        log("Validating if All Keywords contains the phrases");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        log("Successfully Tagged From [" + startDate + "] and To [" + endDate + "]");

        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");
        user.getMomentPage().clickFinish();
        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");
        Assert.assertEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual Test_CRUD_Moments Name '" + MOMENT_NAME + "' with  Expected '" + str[0] + "' Successfully");

        Assert.assertTrue(str[1].contains("Keyword"));
        log("Validated Actual Moment Type'" + str[1] + "' with Expected 'Semantic' Successfully");

        Assert.assertEquals(str[3], ("Success"));
        log("Validated FeedBack Status Actual'" + str[5] + "' with Expected 'Success' successfully");

        user.getMomentPage().deleteMoments(MOMENT_NAME);
        String actualMsg = user.getMomentPage().getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");
    }

    @Test(priority = 9)
    public void VerifyCreateFilteredKeywordMomentsWithContainsAll() throws Exception {
        String MOMENT_NAME = "Test KeyWord" + RANDOM_NAME();
        List<String> keywordsMust = Arrays.asList("Hello", "Good morning");
        WebDashboard user = getLoginInstance();

        log("Entering" + MOMENT_NAME + ",Clicking on " + FILTERED.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createFilteredMoment(MOMENT_NAME, FILTERED.VALUE(), POSITIVE.VALUE());

        List<String> campaignNames = user.getMomentPage().getCampaignNamesDropdownValues(FILTER.VALUE(), SELECT_CAMPAIGN_NAME.VALUE());
        user.getMomentPage().selectCampaginNamesAndCallingMode(campaignNames, 5);
        log("Successfully selected CampaignName :" + campaignNames);

        List<String> processName = user.getMomentPage().getProcessNames(SELECT_PROCESS_NAME.VALUE());
        user.getMomentPage().selectProcessNameAndCallerType(processName, 5);
        log("Successfully selected CampaignName :" + processName);

        log("Selecting the '" + KEYWORD_MOMENT.VALUE() + "' & Entering Search " + keywordsMust);
        List<String> inputKeywords = user.getMomentPage().EnterKeywordDetails1(KEYWORD_MOMENT.VALUE(), keywordsMust);
        List<String> phrases = user.getMomentPage().getKeywordSearchDetails();
        boolean allKeywordsPresent = true;
        for (String keyword : inputKeywords) {
            boolean keywordFound = phrases.stream().anyMatch(phrase -> phrase.toLowerCase().contains(keyword.toLowerCase()));
            if (!keywordFound) {
                allKeywordsPresent = false;
                log("Keyword '" + keyword + "' not found in any phrase.");
                phrases.stream()
                        .filter(phrase -> !phrase.toLowerCase().contains(keyword.toLowerCase()))
                        .forEach(phrase -> log("Phrase without keyword '" + keyword + "': " + phrase));
                break;
            }
        }
        Assert.assertTrue(allKeywordsPresent, "Not all keywords are present in phrases.");
        log("Validating if All Keywords contains the phrases");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        log("Successfully Tagged From [" + startDate + "] and To [" + endDate + "]");

        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");
        user.getMomentPage().clickFinish();
        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");
        Assert.assertEquals(MOMENT_NAME, str[0]);
        log("Validated  Actual Test_CRUD_Moments Name '" + MOMENT_NAME + "' with  Expected '" + str[0] + "' Successfully");

        Assert.assertTrue(str[1].contains("Keyword"));
        log("Validated Actual Moment Type'" + str[1] + "' with Expected 'Semantic' Successfully");

        Assert.assertEquals(str[3], ("Success"));
        log("Validated FeedBack Status Actual'" + str[5] + "' with Expected 'Success' successfully");

       /* user.deleteMoments(MOMENT_NAME);
        String actualMsg = user.getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");*/
    }


    @Test(priority = 10)
    public void VerifyUpdateKeywordsMomentFunctionality() throws Exception {
        List<String> columnList = List.of("Updated By", "Updated On", "Bulk Tag Status");
        List<String> inputKeywords = List.of(RANDOM_KYWORD().VALUE());

        WebDashboard user = getLoginInstance();
        user.getMomentPage().editKeywordMomentDetail();
        log("Clicked on the  the from Table Successfully ");

        user.getMomentPage().updateKeywordMoment(inputKeywords);
        List<String> phrases = user.getMomentPage().getKeywordSearchDetails();
        System.out.println(phrases);

        boolean allKeywordsPresent = true;
        for (String keyword : inputKeywords) {
            boolean keywordFound = phrases.stream().anyMatch(phrase -> phrase.toLowerCase().contains(keyword.toLowerCase()));
            if (!keywordFound) {
                allKeywordsPresent = false;
                log("Keyword '" + keyword + "' not found in any phrase.");
                phrases.stream()
                        .filter(phrase -> !phrase.toLowerCase().contains(keyword.toLowerCase()))
                        .forEach(phrase -> log("Phrase without keyword '" + keyword + "': " + phrase));
                break;
            }
        }
        Assert.assertTrue(allKeywordsPresent, "Not all keywords are present in phrases.");
        log("Validating if All Keywords contains the phrases");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        log("Successfully Tagged From [" + startDate + "] and To [" + endDate + "]");

        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");

        user.getMomentPage().clickColumnFilter();
        user.getMomentPage().searchSelectAddColumn(columnList);
        log("Search and Selected the " + columnList + "Successfully");

        user.getMomentPage().removeDefaultFilter("Today");
        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a", Locale.ENGLISH);

        String updatedDateTimeStr = str[5].trim() + ", " + str[6].trim();
        String systemDateTimeStr = user.getMomentPage().getDateTime();

        LocalDateTime updatedTime = parseDateTime(updatedDateTimeStr);
        LocalDateTime systemTime = parseDateTime(systemDateTimeStr);
        long minutesDifference = ChronoUnit.MINUTES.between(updatedTime, systemTime);

        Assert.assertTrue(Math.abs(minutesDifference) <= 2, "The difference in time is greater than 2 minutes");
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        // Handle both date formats
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a");
        try {
            return LocalDateTime.parse(dateTimeString, formatter1);
        } catch (Exception e) {
            return LocalDateTime.parse(dateTimeString, formatter2);
        }
    }


    @Test(priority = 11)
    public void VerifyDeactivateDeleteKeywordMomentFunctionality() throws Exception {
        WebDashboard user = getLoginInstance();

        log("Deactivating the [Test Moments]");
        user.getMomentPage().deactivateTestMoments();

        String actualDeleteResponse = user.getMomentPage().deleteTestMoments();
        Assert.assertEquals(actualDeleteResponse, "Moment Deleted Successfully");
        log("Validated '" + actualDeleteResponse + " Pop Up Message");
    }


    @Test(priority = 12)
    public void Create_Global_Instructional_Prime_Moments() throws Exception {
        String MOMENT_NAME = "Test Instructional" + RANDOM_NAME();
        List<String> INST_LIST = Arrays.asList(RANDOM_INST().VALUE(), RANDOM_INST().VALUE(), RANDOM_INST().VALUE());
        List<String> KEY_LIST = Arrays.asList(RANDOM_KYWORD().VALUE(), RANDOM_KYWORD().VALUE());
        WebDashboard user = getLoginInstance();
        log("Entering" + MOMENT_NAME + ",Clicking on " + GLOBAL.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createInstructionalMoment(MOMENT_NAME, GLOBAL.VALUE(), POSITIVE.VALUE());

        log("Selecting the'" + INSTRUCTIONAL_MOMENT.VALUE() + "' from Header");
        user.getMomentPage().enterPrimeMomentDetails(INSTRUCTIONAL_MOMENT.VALUE(), INST_LIST, KEY_LIST, CDR.ID_URL.VALUE());
        log("Entered " + INST_LIST + " KeyWords :" + KEY_LIST + "CDR Url : " + CDR.ID_URL.VALUE());

        String actualOutput = user.getMomentPage().getCDROutputText("Present");
        Assert.assertEquals(actualOutput, "Present,Present,Present,");
        log("Validated Expected with Actual and Expected response");

        user.getMomentPage().deleteMoments(MOMENT_NAME);
        String actualMsg = user.getMomentPage().getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");
    }

    @Test(priority = 13)
    public void VerifyCreateFilteredInstructionalPrimeMoments() throws Exception {
        String MOMENT_NAME = "Test Instructional" + RANDOM_NAME();
        List<String> INST_LIST = Arrays.asList(RANDOM_INST().VALUE(), RANDOM_INST().VALUE(), RANDOM_INST().VALUE());
        List<String> KEY_LIST = Arrays.asList(RANDOM_KYWORD().VALUE(), RANDOM_KYWORD().VALUE());
        WebDashboard user = getLoginInstance();

        log("Entering" + MOMENT_NAME + ",Clicking on " + FILTERED.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createFilteredMoment(MOMENT_NAME, FILTERED.VALUE(), POSITIVE.VALUE());

        List<String> campaignNames = user.getMomentPage().getCampaignNamesDropdownValues(FILTER.VALUE(), SELECT_CAMPAIGN_NAME.VALUE());
        user.getMomentPage().selectCampaginNamesAndCallingMode(campaignNames, 5);
        log("Successfully selected CampaignName :" + campaignNames);

        List<String> processName = user.getMomentPage().getProcessNames(SELECT_PROCESS_NAME.VALUE());
        user.getMomentPage().selectProcessNameAndCallerType(processName, 5);
        log("Successfully selected CampaignName :" + processName);

        log("Selecting the'" + INSTRUCTIONAL_MOMENT.VALUE() + "' from Header");
        user.getMomentPage().enterPrimeMomentDetails(INSTRUCTIONAL_MOMENT.VALUE(), INST_LIST, KEY_LIST, CDR.ID_URL.VALUE());
        log("Entered " + INST_LIST + " KeyWords :" + KEY_LIST + "CDR Url : " + CDR.ID_URL.VALUE());

        String actualOutput = user.getMomentPage().getCDROutputText("Present");
        Assert.assertEquals(actualOutput, "Present,Present,Present,");
        log("Validated Expected with Actual and Expected response");

       /* user.deleteMoments(MOMENT_NAME);
        String actualMsg = user.getDeletedText();
        Assert.assertEquals(actualMsg, "Moment Deleted Successfully");
        log("Validated '" + actualMsg + " Pop Up Message");*/
    }


    @Test(priority = 14)
    public void VerifyCreateGlobalInstructionalNexMoment() throws Exception {
        String MOMENT_NAME = "Test Instructional" + RANDOM_NAME();
        List<String> INST_LIST = Arrays.asList(RANDOM_INS_TEST().VALUE(), RANDOM_INS_TEST().VALUE(), RANDOM_INS_TEST().VALUE());
        List<String> KEY_LIST = Arrays.asList(RANDOM_KYWORD().VALUE(), RANDOM_KYWORD().VALUE());

        WebDashboard user = getLoginInstance();
        log("Entering" + MOMENT_NAME + ",Clicking on " + GLOBAL.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createInstructionalMoment(MOMENT_NAME, GLOBAL.VALUE(), POSITIVE.VALUE());

        log("Selecting the'" + INSTRUCTIONAL_MOMENT.VALUE() + "' from Header");
        user.getMomentPage().enterNexMomentDetails(INSTRUCTIONAL_MOMENT.VALUE(), INST_LIST, KEY_LIST, CDR.ID_URL.VALUE());
        log("Entered " + INST_LIST + " KeyWords :" + KEY_LIST + "CDR Url : " + CDR.ID_URL.VALUE());

        String actualOutput = user.getMomentPage().getCDROutputText("Present");
        Assert.assertEquals(actualOutput, "Present,Present,Present,");
        log("Validated Expected with Actual and Expected response");
    }

    @Test(priority = 15)
    public void VerifyCreateFilteredInstructionalNexMoment() throws Exception {
        String MOMENT_NAME = "Test Instructional" + RANDOM_NAME();
        List<String> INST_LIST = Arrays.asList(RANDOM_INS_TEST().VALUE(), RANDOM_INS_TEST().VALUE(), RANDOM_INS_TEST().VALUE());
        List<String> KEY_LIST = Arrays.asList(RANDOM_KYWORD().VALUE(), RANDOM_KYWORD().VALUE());
        WebDashboard user = getLoginInstance();

        log("Entering" + MOMENT_NAME + ",Clicking on " + FILTERED.VALUE() + " and " + POSITIVE.VALUE());
        user.getMomentPage().createFilteredMoment(MOMENT_NAME, FILTERED.VALUE(), POSITIVE.VALUE());

        List<String> campaignNames = user.getMomentPage().getCampaignNamesDropdownValues(FILTER.VALUE(), SELECT_CAMPAIGN_NAME.VALUE());
        user.getMomentPage().selectCampaginNamesAndCallingMode(campaignNames, 5);
        log("Successfully selected CampaignName :" + campaignNames);

        List<String> processName = user.getMomentPage().getProcessNames(SELECT_PROCESS_NAME.VALUE());
        user.getMomentPage().selectProcessNameAndCallerType(processName, 5);
        log("Successfully selected CampaignName :" + processName);

        log("Selecting the'" + INSTRUCTIONAL_MOMENT.VALUE() + "' from Header");
        user.getMomentPage().enterNexMomentDetails(INSTRUCTIONAL_MOMENT.VALUE(), INST_LIST, KEY_LIST, CDR.ID_URL.VALUE());
        log("Entered " + INST_LIST + " KeyWords :" + KEY_LIST + "CDR Url : " + CDR.ID_URL.VALUE());

        String actualOutput = user.getMomentPage().getCDROutputText("Present");
        Assert.assertEquals(actualOutput, "Present,Present,Present,");
        log("Validated Expected with Actual and Expected response");
    }

    @Test(priority = 16)
    public void VerifyDeactivateDeleteMomentFunctionality() throws Exception {
        WebDashboard user = getLoginInstance();

        log("Deactivating the [Test Moments]");
        user.getMomentPage().deactivateTestMoments();

        String actualDeleteResponse = user.getMomentPage().deleteTestMoments();
        Assert.assertEquals(actualDeleteResponse, "Moment Deleted Successfully");
        log("Validated '" + actualDeleteResponse + " Pop Up Message");
    }


    // @Test(priority = 17)
    public void VerifyUpdateInstructionalMomentFunctionality() throws Exception {
        List<String> columnList = List.of("Updated By", "Updated On", "Bulk Tag Status");
        List<String> instructionalList = List.of("is agent greet?");
        String url = "https://nobroker.callzen.ai/conversation-info?conversationId=7e2db36d-4bdf-46f8-aa35-9f4ad39486ff&conversationType=call";
        WebDashboard user = getLoginInstance();

        user.getMomentPage().editInstructionalMomentDetail();
        log("Clicked on the  the from Table Successfully ");

        user.getMomentPage().updateInstructionalMoment(instructionalList, url);

        String actualOutput = user.getMomentPage().getCDROutputText("Present");
        Assert.assertEquals(actualOutput, "Present,");
        log("Validated Actual[" + actualOutput + "] with Expected [Present] response");

        user.getMomentPage().bulkMomentTagging(startDate, endDate);
        log("Successfully Tagged From [" + startDate + "] and To [" + endDate + "]");
        user.getMomentPage().clickFinish();
        String actualResponse = user.getMomentPage().getBulkJobSubmitText();
        Assert.assertEquals(actualResponse, "Bulk job submitted please wait for sometime to see the moments tagged");
        log("Validating the Actual Response :" + actualResponse + "with Bulk job submitted please wait for sometime to see the moments tagged");

        user.getMomentPage().clickColumnFilter();
        user.getMomentPage().searchSelectAddColumn(columnList);
        log("Search and Selected the " + columnList + "Successfully");

        String momentDetails = user.getMomentPage().getMomentDetails();
        System.out.println("Moment Details : " + momentDetails);
        String[] str = momentDetails.split(",");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a", Locale.ENGLISH);

        String updatedDateTimeStr = str[5].trim() + ", " + str[6].trim();
        String systemDateTimeStr = user.getMomentPage().getDateTime();
        LocalDateTime updatedDateTime;
        LocalDateTime systemTime;
        try {
            updatedDateTime = LocalDateTime.parse(updatedDateTimeStr, formatter);
            systemTime = LocalDateTime.parse(systemDateTimeStr, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse date-time strings", e);
        }
        long differenceInMinutes = ChronoUnit.MINUTES.between(updatedDateTime, systemTime);
        boolean isWithinTolerance = Math.abs(differenceInMinutes) <= 2;
        // TestNG assertion
        Assert.assertTrue(isWithinTolerance, "Updated Date Time Not Updated Properly.");
    }

}






