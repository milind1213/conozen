package com.convozen.Pages.Playwrights;
import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import org.testng.Assert;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static com.convozen.Pages.Playwrights.Locators.*;

public class ConvozenMoments extends CommonPlaywright {
    public Page page;
    public ConvozenMoments(Page page) {
        super(page);
        this.page = page;
    }

    public String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a", Locale.ENGLISH);
        String nowDataTime = now.format(formatter);
        return nowDataTime;
    }

    public void deactivateTestMoments() {
        waitFor(3);
        page.waitForSelector(momentNameList);
        Locator momentNames = page.locator(momentNameList);
        boolean momentFound = false;
        for (int i = 0; i < momentNames.count(); i++) {
            momentNames.nth(i).scrollIntoViewIfNeeded();
            String nameTxt = momentNames.nth(i).innerText();
            if (nameTxt.startsWith("Test Symentic") || nameTxt.startsWith("Test KeyWord") || nameTxt.startsWith("Test Instructional")) {
                momentFound = true;
                log("Active [Test Moment :" + nameTxt + "]");

                waitFor(1);
                click(activeMometns + "[" + (i + 1) + "]");
                log("Clicking on [Confirm] button");

                click(confirmBtn);
                log("Successfully Deactivated Moment[" + nameTxt + "]");
                waitFor(5);
            }
        }
        if (!momentFound) {
            log("Not Found Activate MomentName Starting With[Test Symentic],[Test KeyWord], or [Test Instructional].");
        }
    }

    public String deleteTestMoments() {

        waitFor(2);
        String lastDeleteSuccessMsg = "";
        waitFor(3);
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 5)) {
            click(defaultCallFilterCross.replace("txt", "Active"));
        }
        click(reloadBtn);
        log("Clicking on Reload button");

        waitFor(2);
        waitForSelector(page, momentNameList, 5);
        boolean momentFound = false;

        while (true) {
            Locator momentNames = page.locator(momentNameList);
            int momentCount = momentNames.count();
            boolean foundInCurrentIteration = false;

            for (int i = 0; i < momentCount; i++) {
                String nameTxt = momentNames.nth(i).innerText();
                if (nameTxt.startsWith("Test Symentic") || nameTxt.startsWith("Test KeyWord") || nameTxt.startsWith("Test Instructional")) {
                    momentFound = true;
                    foundInCurrentIteration = true;
                    log("Found the Test Moment[" + nameTxt + "]");
                    waitFor(1);
                    click("//tbody/tr[" + (i + 1) + "]/td[6]//button");

                    click(deleteMomentBt);
                    String str = getText(deleteMomentNameTxt);
                    fill(textBox, str);

                    click(confirmBtn);
                    log("Clicked on [Accordion], [Delete] button, and Entered [" + nameTxt + "] and [Delete Confirmed]");
                    waitFor(3); // Wait for 3 seconds to allow the delete action to complete
                    lastDeleteSuccessMsg = page.locator(deleteSuccessMsg).innerText(); // Capture the success message
                    page.reload();
                    log("Reloading the Page");
                    waitFor(5);

                    if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 5)) {
                        click(defaultCallFilterCross.replace("txt", "Active"));
                    }
                    waitFor(3);
                    break;
                }
            }
            if (!foundInCurrentIteration) {
                break;
            }
        }
        if (!momentFound) {
            log("No Moments found Starting with [Test Symentic], [Test KeyWord], or [Test Instructional].");
        }
        return lastDeleteSuccessMsg;
    }


    public void deleteMoments(String name) {
        click(reloadBtn);
        waitFor(5);
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 2)) {
            click(defaultCallFilterCross.replace("txt", "Active"));
        }
        try {
            click(editAccordion);
            click(deleteMomentBt);
            fill(textBox, name);
            click(confirmBtn);
            log("Clicked on[Accordion] and [Delete] button,Entered [" + name + "] and [confirmed]");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }


    public List<String> EnterKeywordDetails(String type, List<String> keywords) {
        List<String> result = new ArrayList<String>();

        page.waitForSelector(momentType.replace("name", type));
        click(momentType.replace("name", type));
        try {
            for (int i = 0; i < keywords.size(); i++) {
                String keyword = keywords.get(i); // Get the keyword at index i
                fill(keywordSearchBar, keyword);
                page.keyboard().press("Enter");
                result.add(keyword);
            }
        } catch (Exception e) {
            log("Failed to Enter Keywords in SearchBar :" + e.getMessage());
        }
        return result;
    }

    public List<String> getKeywordSearchDetails() {
        waitFor(2);
        click(keywordSamplePhraseBtn);
        try {
            waitFor(5);
            Locator samplePhrases = page.locator(keywordSamplePhraseList);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(samplePhrases.nth(i).innerText());
            }
            clickNext();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public void bulkMomentTagging1(String startDate, String endDate) {
        log("Clicking on Tag from Now Button");
        click(tagFromNowBtm);

        LocalDate parsedStartDate = LocalDate.parse(startDate.substring(0, 8), DateTimeFormatter.ofPattern("ddMMyyyy"));
        LocalDate parsedEndDate = LocalDate.parse(endDate.substring(0, 8), DateTimeFormatter.ofPattern("ddMMyyyy"));
        while (true) {
            log("Entering from Bulk Tagging " + startDate);
            type(bulkTagFromDateTime, startDate);
            waitFor(1);
            log("Entering from Bulk Tagging To" + endDate);
            type(bulkTagToDateTime, endDate);

            if (isLocatorPresent(page, totalCallToBeAffected, 3)) {
                int call = Integer.parseInt(getText(totalCallToBeAffected));
                if (call == 0) {
                    parsedStartDate = parsedStartDate.minusDays(1);
                    startDate = parsedStartDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + startDate.substring(8);
                    endDate = parsedEndDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + endDate.substring(8);
                } else {
                    break; // Exit the loop if call is not 0
                }
            } else {
                String warningMsg = getText(callLimitWarnings);
                String text = warningMsg.replaceAll("[^0-9,]", "");
                String[] str = text.split(",");
                int totalCalls = Integer.parseInt(str[0]);
                int callLimit = Integer.parseInt(str[1]);
                if (totalCalls > callLimit) {
                    parsedStartDate = parsedStartDate.plusDays(1);
                    startDate = parsedStartDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + startDate.substring(8);
                } else {
                    click(submitBtn);
                    log("Clicked on [Submit] Button");
                }
            }
        }
        click(submitBtn);
        log("Clicked on [Submit] Button");
        clickFinish();
    }

    public void createMoment(String MomentName, String Global, String emotion) {
        page.waitForSelector(createMomentBtn);
        click(createMomentBtn);
        log("Clicked on [Create Moment] Button");
        try {
            fill(momentNameTxt, MomentName);
            log("Entered Moment Name [" + MomentName + "]in text Field");

            click(momentCreatBtns.replace("txt", Global));
            log("Clicked on [" + Global + "] Button");

            click(momentCreatBtns.replace("txt", emotion));
            log("Clicked on [" + emotion + "] Button");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Buttons");
        }
        try {
            click(selectConversationType);
            log("Clicked on [Select Conversation] Element ");

            click(conversationTypeDrp);
            log("Clicked on [Call] DropDown Options");

            click(callerType);
            log("Clicking on [Select Dropdown]");

            click(selectAllCallerType);
            log("Clicking on [Select All]");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Selecting filter values");
        }
        click(sideEle);
        // conditionalMoment();
        clickNext();
    }


    public void createInstructionalMoment(String MomentName, String Global, String emotion) {
        page.waitForSelector(createMomentBtn);
        click(createMomentBtn);
        log("Clicked on [Create Moment] Button");
        try {
            fill(momentNameTxt, MomentName);
            log("Entered Moment Name [" + MomentName + "]in text Field");

            click(momentCreatBtns.replace("txt", Global));
            log("Clicked on [" + Global + "] Button");

            click(momentCreatBtns.replace("txt", emotion));
            log("Clicked on [" + emotion + "] Button");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Buttons");
        }
        try {
            click(selectConversationType);
            log("Clicked on [Select Conversation] Element ");

            click(conversationTypeDrp);
            log("Clicked on [Call] DropDown Options");

            click(callerType);
            log("Clicking on [Select Dropdown]");

            click(selectAllCallerType);
            log("Clicking on [Select All]");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Selecting filter values");
        }
        click(sideEle);
        clickNext();
    }

    public String getCDROutputText(String result) throws Exception {
        StringBuilder sb = new StringBuilder();
        waitFor(2);
        Locator loc = page.locator(presentTxt.replace("txt", result));
        for (int i = 0; i < loc.count(); i++) {
            sb.append(loc.nth(i).innerText()).append(",");
        }
        clickNotifyMe();
        return sb.toString();
    }

    public void clickNotifyMe() throws Exception {
        waitFor(1);
        clickNext();
        waitFor(2);
        page.waitForSelector(notifyBtn);
        Thread.sleep(3000);
        click(notifyBtn);
        log("Clicked on 'Notify Me' Button");
    }


    public void enterNexMomentDetails(String types, List<String> searchTextList, List<String> samplePhrases, String url) {
        page.waitForSelector(momentType.replace("name", types));
        click(momentType.replace("name", types));
        waitFor(1);

        click(isNex);
        log("Clicked  on the [Nex] checkbox");

        Locator element = page.locator(searchTextBoxInstructional);
        int searchTextSize = searchTextList.size();
        for (int i = 0; i < searchTextSize; i++) {
            String txt = searchTextList.get(i);
            fill(searchTextBoxInstructional, txt);
            waitFor(1);
            if (!isLocatorPresent(page, urlInputField, 2)) {
                click(addMoreAudioBtn);
            }
            fill(urlInputField, url);
            click(submitBtn);
            waitFor(3);
            if (i != searchTextSize - 1) {
                element.press("Control+A");
                element.press("Backspace");
                log("Selected and cleared [" + txt + "] ");
            }
            waitFor(2);
        }
        for (String phrase : samplePhrases) {

            if (isLocatorPresent(page, samplePhraseInput, 2)) {
                fill(samplePhraseInput, phrase);
            }
            if (isLocatorPresent(page, samplePhraseInput2, 2)) {
                fill(samplePhraseInput2, phrase);
            }
            boolean enabledPlusBtn = page.isEnabled(plusIconSamplePhrase);
            if (enabledPlusBtn) {
                click(plusIconSamplePhrase);
                log("Entered [" + phrase + "] and Clicked on [+]");
            }
            waitFor(2);
        }
    }


    public void enterPrimeMomentDetails(String types, List<String> searchTextList, List<String> keywords, String url) {
        page.waitForSelector(momentType.replace("name", types));
        click(momentType.replace("name", types));
        Locator element = page.locator(searchTextBoxInstructional);
        int searchTextSize = searchTextList.size();
        for (int i = 0; i < searchTextSize; i++) {
            String txt = searchTextList.get(i);
            fill(searchTextBoxInstructional, txt);
            waitFor(1);
            if (isLocatorPresent(page, urlInputField, 2)) {
                fill(urlInputField, url);
                click(submitBtn);
            } else {
                click(addMoreAudioBtn);
                fill(urlInputField, url);
                click(submitBtn);
            }
            if (i != searchTextSize - 1) {
                element.press("Control+A");
                element.press("Backspace");
            }
        }
        for (String keyword : keywords) {
            fill(additionalTextbox, keyword);
            waitFor(1);
            page.keyboard().press("Enter");
        }
    }


    public List<String> EnterKeywordDetails1(String type, List<String> keywords) throws Exception {
        List<String> result = new ArrayList<String>();
        page.waitForSelector(momentType.replace("name", type));
        click(momentType.replace("name", type));
        try {
            click(".css-1wy0on6");
            log("Clicked on the 'Search Accordion'");

            List<ElementHandle> list = page.querySelectorAll(keywordSearchType);
            for (ElementHandle element : list) {
                String text = element.innerText();
                if (text.equals("Contains All")) {
                    element.click();
                }
            }
        } catch (Exception e) {
            log("Failed to Select Dropdown Options");
        }
        try {
            for (int i = 0; i < keywords.size(); i++) {
                String keyword = keywords.get(i); // Get the keyword at index i
                fill(keywordSearchBar, keyword);
                page.keyboard().press("Enter");
                result.add(keyword);
            }
        } catch (Exception e) {
            log("Failed to Enter Keywords in SearchBar :" + e.getMessage());
        }
        return result;
    }


    public void reloadPage() {
        try {
            isLocatorPresent(page, reloadBtn, 3);
            log("Clicking on the [ reload ] Button");
            click(reloadBtn);
        } catch (Exception e) {
            log("Error : Failed ot reload page");
        }
    }

    public void smartMomentFilter(String filterName, String filterCheckbox) {
        page.waitForSelector(mainFilterBtn);
        try {
            click(mainFilterBtn);
            log("Clicked on 'Main Filter' Button");
            click(filterNameBtn.replace("txt", filterName));

            click(label.replace("txt", filterCheckbox));
            click(submitBtn);
            log("Clicked on [" + filterName + "] and Entered [" + filterCheckbox + "]and checked and clicked on [Submit] button");
            waitFor(2);
        } catch (Exception e) {
            log("Error : An error occurred while selecting " + filterCheckbox);
        }
    }

    public List<String> getSmartCluster() {
        List<String> smartClusterList = new ArrayList<>();
        isLocatorPresent(page, activeStatusCrossBtn, 3);
        click(activeStatusCrossBtn);
        waitFor(2);
        List<ElementHandle> list = page.querySelectorAll(sourceColumn);
        for (int i = 1; i < list.size(); i++) {
            String text = list.get(i).innerText();
            smartClusterList.add(text);
        }
        return smartClusterList;
    }

    public void momentMStatusDetailsFilters(String filterName, String dropdown, String status) {
        try {
            if (isLocatorPresent(page, mainFilterBtn, 2)) {
                page.click(mainFilterBtn);
                log("Clicked on Main Filter Button");
            }
            click(filterNameBtn.replace("txt", filterName));
            click(drpActive.replace("txt", dropdown));
            page.fill(searchFilters, status);
            click(drpOptionsBtn);
            log("Clicked on [" + filterName + " And " + dropdown + "] and Entered [" + status + "]and Selected ");

            click(submitBtn);
            log("click on[Submit] button");

            waitFor(2);
        } catch (Exception e) {
            log("Error in momentMetaDataFilters: " + e.getMessage());
        }
    }

    public int getStatusCount(String status) {
        if (status.equals("Active")) {
            Locator locator = page.locator(activeMomentList);
            return locator.count();
        } else if (status.equals("Inactive")) {
            Locator locator = page.locator(inActiveMomentList);
            return locator.count();
        } else if (status.equals("Drafted")) {
            Locator locator = page.locator(draftMomentList);
            return locator.count();
        }
        return 0;
    }


    public void momentMetaDataFilter(String filterName, String type, String dropValue) {
        try {
            if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
                click(activeStatusCrossBtn);
                log("Clicked on [ Cross ] Button");
            }

            if (isLocatorPresent(page, mainFilterBtn, 2)) {
                page.click(mainFilterBtn);
                log("Clicked on Main Filter Button");
            }
            if(isLocatorPresent(page,filterNameBtn1.replace("txt", filterName),2)){
              click(filterNameBtn1.replace("txt", filterName));
            } else if (isLocatorPresent(page,filterNameBtn.replace("txt", filterName),2)){
                click(filterNameBtn.replace("txt", filterName));
            }

            if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
                click(activeStatusCrossBtn);
                log("Clicked on [ Cross ] Button");
            }

            click(drpTxt.replace("txt", type));
            page.fill(searchFilters, dropValue);
            click(drpOptionsBtn);
            log("Clicked on [" + filterName + " And " + type + "] and Entered [" + dropValue + "]and Selected ");

            click(submitBtn);
            waitFor(2);

        } catch (Exception e) {
            log("Error in momentMetaDataFilters: " + e.getMessage());
        }
    }

    public List<String> getMomentType(String type) {
        List<String> list = new ArrayList<>();
        page.waitForSelector(searchTypeList);
        waitFor(2);
        Locator loc = page.locator(searchTypeList);
        for (int i = 0; i < loc.count(); i++) {
            String momentType = loc.nth(i).innerText();
            if (type.equals(momentType)) {
                list.add(momentType);
            }
        }
        return list;
    }

    public List<String> getMomentSourceType(String type) {
        List<String> list = new ArrayList<>();
        waitFor(5);
        Locator loc = page.locator("//tbody//tr//td[7]");
        for (int i = 0; i < loc.count(); i++) {
            String momentType = loc.nth(i).innerText();
            if (type.equals(momentType)) {
                list.add(momentType);
            }
        }
        return list;
    }

    public List<String> getMomentStatus(String type) {
        List<String> list = new ArrayList<>();
        Locator loc = page.locator("//tbody//tr//td[5]");
        for (int i = 0; i < loc.count(); i++) {
            String momentType = loc.nth(i).innerText();
            if (type.equals(momentType)) {
                list.add(momentType);
            }
        }
        return list;
    }


    public List<String> getMomentMetaDataDropdownValues(String filterName, String selectValue, int count) {
        List<String> metaDataList = new ArrayList<>();
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 2)) {
            click(defaultCallFilterCross.replace("txt", "active"));
        }
        try {
            click(mainFilterBtn);
            click(filterNameBtn.replace("txt", filterName));
            click(drpTxt.replace("txt", selectValue));
            page.waitForSelector(campaignsNameList);
            waitFor(2);
            List<ElementHandle> list = page.querySelectorAll(campaignsNameList);
            int eleCount = 0;
            for (ElementHandle ele : list) {
                if (eleCount >= count) {
                    break; // Exit the loop once we have 5 campaign names
                }
                String eleText = ele.innerText();
                metaDataList.add(eleText);
                eleCount++;
            }
            waitFor(1);
            click(filterNameBtn.replace("txt", filterName));
            waitFor(2);
            click(submitBtn);
            return metaDataList;
        } catch (Exception e) {
            log("Error : Exception Occurred While extracting campaign names");
        }
        return null;
    }


    public List<String> momentMetaDataFilters(String filterName, String selectValue, List<String> dropdownList) {
        List<String> rowData = new ArrayList<>();
        try {
            if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
                click(activeStatusCrossBtn);
                log("Clicked on [ Cross ] Button");
            }
            for (String value : dropdownList) {
                if (isLocatorPresent(page, mainFilterBtn, 2)) {
                    page.click(mainFilterBtn);
                }
                click(filterNameBtn.replace("txt", filterName));
                click(drpTxt.replace("txt", selectValue));
                page.fill(searchFilters, value);
                click(dropElementList.replace("txt", value));
                click(submitBtn);
                waitFor(2);

                String selector = null;
                switch (filterName) {
                    case "Campaign Name":
                        selector = campaignNameEleList;
                        break;
                    case "Mode of calling":
                        selector = modeOfEleList;
                        break;
                    case "Process Name":
                        selector = processNameEleList;
                        break;
                    default:
                        log("Unsupported filter: " + filterName);
                        return null;
                }
                waitFor(2);
                click(selector);
                if (isLocatorPresent(page, taggedEle, 1)) {
                    waitFor(1);
                    String taggedText = getText(taggedEle);
                    rowData.add(taggedText);
                } else {
                    waitFor(1);
                    String taggedText = getText(selector);
                    rowData.add(taggedText);
                }
                click(defaultCallFilterCross.replace("txt", value));
                waitFor(1);
            }
            return rowData;
        } catch (Exception e) {
            log("Error in momentMetaDataFilters: " + e.getMessage());
        }
        return null;
    }

    public List<String> getFilterData(String filter) {
        List<String> rowData = new ArrayList<>();
        String selector = null;
        switch (filter) {
            case "Campaign Name":
                selector = campaignNameEleList;
                break;
            case "Mode of calling":
                selector = modeOfEleList;
                break;
            case "Process Name":
                selector = processNameEleList;
                break;
            default:
                log("Unsupported filter: " + filter);
                return null;
        }
        try {
            waitFor(2);
            click(selector);
            if (isLocatorPresent(page, taggedEle, 1)) {
                String taggedText = getText(taggedEle);
                rowData.add(taggedText);
            } else {
                String taggedText = getText(selector);
                rowData.add(taggedText);
            }
            return rowData;
        } catch (Exception e) {
            log("Error getting row data: " + e.getMessage());
            return null;
        }
    }

    public void bulkTagActions(List<String> momentList, String bulkTagStartDate, String bulkTagEndDate) throws Exception {
        performCommonActions(momentList);
        try {
            click(ActionCheckBox);
            log("Clicked on the [ Action CheckBox ] Button");

            waitFor(1);
            List<ElementHandle> checkedBoxesList = page.querySelectorAll(checkedBoxes);
            if (checkedBoxesList.size() == 1) {
                log("Only one checkbox found. Ignoring the Unchecking.");
            } else {
                for (int i = 1; i < checkedBoxesList.size(); i++) {
                    checkedBoxesList.get(i).click();
                    log("Clicked on the checkbox " + (i + 1));
                }
            }

            click(bulkTagActionBtn);
            log("Clicked on the [Action Button ] Button");
            Locator locator = page.locator(bulkActionBtnList);
            locator.nth(0).click();

            if (isLocatorPresent(page, momentError, 2)) {
                log("Error : An error occurred [ Moment In process ] ");
                click(cancelBtn);
                log("Clicked on the [ Cancel Button ]");
                page.reload();
            } else {
                if (bulkTagStartDate != null && bulkTagEndDate != null) {
                    String res = bulkTagging(bulkTagStartDate, bulkTagEndDate);
                    Assert.assertEquals(res, "Process has been Initiated...");
                }
            }
        } catch (Exception e) {
            log("Error : An Error occurred while bulk tagging the moments :" + e.getMessage());
        }
    }

    public String bulkUntagActions(List<String> momentList, String startDate, String endDate) throws Exception {
        performCommonActions(momentList);
        try {
            click(ActionCheckBox);
            log("Clicked on the [ Action CheckBox ] Button");
            List<ElementHandle> checkedBoxesList = page.querySelectorAll(checkedBoxes);

            if (checkedBoxesList.size() == 1) {
                log("Only one checkbox found. Ignoring the Unchecking.");
            } else {
                for (int i = 1; i < checkedBoxesList.size(); i++) {
                    checkedBoxesList.get(i).click();
                    log("Clicked on the checkbox " + (i + 1));
                }
            }
            click(bulkTagActionBtn);
            log("Clicked on the [Action Button ] Button");
            Locator locator = page.locator(bulkActionBtnList);
            locator.nth(1).click();

            if (isLocatorPresent(page, momentError, 2)) {
                log("Error : An error occurred [ Moment In process ] ");

                String confirmationPopUp = getText(untagConfMsg);
                Assert.assertEquals(confirmationPopUp, "Bulk Untag cannot be executed");

                click(cancelBtn);
                log("Clicked on the [Action Button]");
            } else {
                log("Entering from Bulk Tagging Date :" + startDate);
                type(bulkTagFromDateTimeLoc, startDate);

                log("Entering from Bulk Tagging To :" + endDate);
                type(bulkTagToDateTimeLoc, endDate);

                click(confirmBtn);
                log("Clicked on [ Confirm ] Button");
                page.waitForSelector(processInitiated);

                String response = getText(processInitiated);
                return response;
            }
        } catch (Exception e) {
            log("Error : An Error occurred while processing the untag moments :" + e.getMessage());
        }
        return "";
    }

    public String bulkTagging(String startDate, String endDate) {
        LocalDate parsedStartDate = LocalDate.parse(startDate.substring(0, 8), DateTimeFormatter.ofPattern("ddMMyyyy"));
        LocalDate parsedEndDate = LocalDate.parse(endDate.substring(0, 8), DateTimeFormatter.ofPattern("ddMMyyyy"));

        while (true) {
            log("Entering from Bulk Tagging Date: " + startDate);
            type(bulkTagFromDateTimeLoc, startDate);

            log("Entering from Bulk Tagging To: " + endDate);
            type(bulkTagToDateTimeLoc, endDate);

            waitFor(1);
            if (isLocatorPresent(page, totalCallToBeAffected, 2)) {
                String text = page.innerText(totalCallToBeAffected);
                int call = extractNumber(text);

                if (call == 0) {
                    parsedStartDate = parsedStartDate.minusDays(1);
                    startDate = parsedStartDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + startDate.substring(8);
                    endDate = parsedEndDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + endDate.substring(8);
                } else {
                    break; // Exit the loop if call is not 0
                }
            } else if (isLocatorPresent(page, callLimitWarning, 3)) {
                String warningMsg = page.locator(callLimitWarning).innerText();
                int totalCalls = extractNumber(warningMsg.split(",")[0]);
                int callLimit = extractNumber(warningMsg.split(",")[1]);

                if (totalCalls > callLimit) {
                    parsedStartDate = parsedStartDate.plusDays(1);
                    startDate = parsedStartDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + startDate.substring(8);
                    endDate = parsedEndDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + endDate.substring(8);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        click("//button[.='Confirm']");
        log("Clicked on [confirm ] Button");
        waitFor(2);
        page.waitForSelector(processInitiated);
        String response = getText(processInitiated);
        return response;
    }

    private int extractNumber(String text) {
        String num = text.replaceAll("[^0-9,]", "");
        return Integer.parseInt(num);
    }

    public void performCommonActions(List<String> momentList) throws InterruptedException {
        if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
            click(activeStatusCrossBtn);
        }
        if (isLocatorPresent(page, searchBtn, 2)) {
            click(searchBtn);
            log("Clicked on the 'Search' Button");
            waitFor(1);
        }

        if (isLocatorPresent(page, searchCrossIcon, 3)) {
            click(searchCrossIcon);
            log("Clicked on the [ Cross ] icon");
        }
        for (String momentName : momentList) {
            fill(momentSearchbar, momentName);
            page.keyboard().press("Enter");
            log("Entered the MomentName '" + momentName + "' in Searchbar and Pressed 'Enter Key'");
            waitFor(1);
            break;
        }
    }

    public String momentActivation(List<String> momentList) throws InterruptedException {
        if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
            click(activeStatusCrossBtn);
        }
        if (isLocatorPresent(page, searchBtn, 2)) {
            click(searchBtn);
            log("Clicked on the [ Search ] Button");
            waitFor(1);
        }

        for (String momentName : momentList) {
            fill(momentSearchbar, momentName);
            page.keyboard().press("Enter");

            page.waitForSelector(ActionCheckBox);
            waitFor(2);
            if (!isLocatorPresent(page, checkedActionButton, 3)) {
                clickWithForce(ActionCheckBox);
                log("Clicked on the [ Action CheckBox ] Button");
            }
            page.waitForSelector(checkedBoxes);
            List<ElementHandle> checkedBoxesList = page.querySelectorAll(checkedBoxes);
            if (checkedBoxesList.size() == 1) {
                log("Only one checkbox found. Ignoring the Unchecking.");
            } else {
                for (int i = 1; i < checkedBoxesList.size(); i++) {
                    checkedBoxesList.get(i).isEnabled();
                    checkedBoxesList.get(i).click();
                    waitFor(500);
                }
            }
            click(bulkTagActionBtn);
            log("Clicked on the [Action Button ] Button");

            Locator locator = page.locator(bulkActionBtnList);
            locator.nth(2).click();
            log("Clicked on the [Activate] Button");

            if (isLocatorPresent(page, momentError, 3)) {
                log("Error : An Error Occurred ");
                if (isLocatorPresent(page, proceedWithoutCheckBox, 3)) {
                    click(proceedWithoutCheckBox);
                    click(confirmBtn);
                }

            }
            boolean confirm = page.isEnabled(confirmBtn);
            if (confirm) {
                click(confirmBtn);
                page.waitForSelector(activationCompleted);
                return page.locator(activationCompleted).innerText();
            } else {
                click(cancelBtn);
                log("Clicked on the [Cancel] button");
            }

            click(searchCrossIcon);
            log("Clicked on the [ Cross ] icon");
            waitFor(2);
        }
        return null;
    }

    public String momentDeactivation(List<String> momentList) throws InterruptedException {
        if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
            click(activeStatusCrossBtn);
        }
        if (isLocatorPresent(page, searchBtn, 2)) {
            click(searchBtn);
            waitFor(1);
        }
        for (String momentName : momentList) {
            fill(momentSearchbar, momentName);
            page.keyboard().press("Enter");
            page.waitForSelector(ActionCheckBox);
            waitFor(2);
            if (!isLocatorPresent(page, checkedActionButton, 3)) {
                clickWithForce(ActionCheckBox);
            }
            page.waitForSelector(checkedBoxes);
            List<ElementHandle> checkedBoxesList = page.querySelectorAll(checkedBoxes);
            if (checkedBoxesList.size() == 1) {
            } else {
                for (int i = 1; i < checkedBoxesList.size(); i++) {
                    checkedBoxesList.get(i).isEnabled();
                    checkedBoxesList.get(i).click();
                    waitFor(500);
                }
            }
            click(bulkTagActionBtn);
            Locator locator = page.locator(bulkActionBtnList);
            locator.nth(3).click();
            if (isLocatorPresent(page, checkListAffectedTxt, 3)) {
                click(confirmBtn);
            }
            click(confirmBtn);
            waitFor(2);
            return page.locator(deactivationCompleted).innerText();
        }
        return null;
    }


    public void removeColumns(List<String> columns) throws Exception {
        click(momentColumnFilterBtn);
        log("Clicking on [ Column Filter ] Button");
        try {
            List<ElementHandle> columnHandleList = page.querySelectorAll(columnNameList);
            for (String col : columns) {
                for (ElementHandle columnHandle : columnHandleList) {
                    String columName = columnHandle.innerText();
                    if (col.equals(columName)) {
                        columnHandle.click();
                        log("Clicking on '" + columName + "' Column");
                        Thread.sleep(500);
                    }
                }
            }
        } catch (Exception e) {
            log("Error: An error occurred while removing columns: " + e.getMessage());
        }
        click("//thead/tr/th[1]/div");
    }

    public List<String> getMomentTableDefaultColumns() {
        List<String> columnNameList = new ArrayList<>();
        click("//thead/tr/th[1]/div");
        try {
            List<ElementHandle> columnNames = page.querySelectorAll("//thead/tr/th/div");
            int count = columnNames.size();

            for (int i = 0; i < count; i++) {
                String name = columnNames.get(i).innerText();
                columnNameList.add(name);
                waitFor(1);
            }
        } catch (Exception e) {
            log("Error: An error occurred while getting column names: " + e.getMessage());
        }
        return columnNameList;
    }


    public List<String> getTableColumns() {
        List<String> columnNameList = new ArrayList<>();
        click("//thead/tr/th[1]/div");
        try {
            List<ElementHandle> columnNames = page.querySelectorAll("//thead/tr/th/div");
            int count = columnNames.size();

            for (int i = 0; i < count; i++) {
                String name = columnNames.get(i).innerText();
                columnNameList.add(name);
                waitFor(1);
            }
        } catch (Exception e) {
            log("Error: An error occurred while getting column names: " + e.getMessage());
        }
        return columnNameList;
    }

    public List<String> getAllMomentColumnNames() {
        List<String> allCols = new ArrayList<>();
        try {
            click(momentColumnFilterBtn);
            log("Clicking on [ Column Filter ] Button");
            page.waitForSelector(columnNameList);
            List<ElementHandle> columnHandleList = page.querySelectorAll(columnNameList);
            for (ElementHandle columnHandle : columnHandleList) {
                allCols.add(columnHandle.innerText());
            }
        } catch (Exception e) {
            log("Error: An error occurred while getting column names: " + e.getMessage());
        }
        return allCols;
    }

    public List<String> getUncheckedMomentColumnFilterNames() {
        List<String> unCheckedcolumnList = new ArrayList<>();
        List<ElementHandle> columnHandleList = page.querySelectorAll(columnNameList);
        List<String> allcolumnList = new ArrayList<>();
        for (ElementHandle columnHandle : columnHandleList) {
            allcolumnList.add(columnHandle.innerText());
        }
        List<ElementHandle> checkedColumEleList = page.querySelectorAll(checkedMomentTableColumns);
        List<String> CheckedcolumnList = new ArrayList<>();
        for (ElementHandle columnHandle : checkedColumEleList) {
            CheckedcolumnList.add(columnHandle.innerText());
        }
        List<String> tempList = new ArrayList<>(allcolumnList);
        tempList.retainAll(CheckedcolumnList);
        allcolumnList.removeAll(tempList);
        unCheckedcolumnList.addAll(allcolumnList);

        return unCheckedcolumnList;
    }

    public void searchSelectAddColumn(List<String> columNameList) {
        for (String columName : columNameList) {
            fill(searchFilters, columName);
            click(columnNameList);
            waitFor(1);
            click(searchCross);
        }
    }

    public void clickColumnFilter() {
        page.reload();
        waitFor(3);
        click(momentColumnFilterBtn);
        log("Clicking on [ Column Filter ] Button");
    }


    public void removeDefaultFilter(String str) {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", str), 2)) {
            click(defaultCallFilterCross.replace("txt", str));
            waitFor(3);        }
    }

    public void bulkMomentTagging(String startDate, String endDate) throws InterruptedException {
        //  log("Clicking on Tag from Now Button");
        // click(tagFromNowBtm);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        LocalDateTime parsedStartDateTime = LocalDateTime.parse(startDate, dateFormatter);
        LocalDateTime parsedEndDateTime = LocalDateTime.parse(endDate, dateFormatter);
        boolean isFromDataEnabled = page.isEnabled(bulkTagFromDateTimeLoc);
        if (isFromDataEnabled) {
            while (true) {
                log("Entering from Bulk Tagging Date: " + startDate);
                type(bulkTagFromDateTimeLoc, startDate);

                log("Entering from Bulk Tagging To: " + endDate);
                type(bulkTagToDateTimeLoc, endDate);
                waitFor(1);

                if (isLocatorPresent(page, totalCallToBeAffected, 2)) {
                    int call = Integer.parseInt(page.innerText(totalCallToBeAffected));
                    if (call == 0) {
                        if (parsedStartDateTime.toLocalDate().isEqual(parsedEndDateTime.toLocalDate())) {
                            // Reduce time by one hour
                            parsedStartDateTime = parsedStartDateTime.minusHours(1);
                            parsedEndDateTime = parsedEndDateTime.minusHours(1);
                            startDate = parsedStartDateTime.format(dateFormatter);
                            endDate = parsedEndDateTime.format(dateFormatter);
                        } else {
                            // Reduce the date by one day
                            parsedStartDateTime = parsedStartDateTime.minusDays(1);
                            startDate = parsedStartDateTime.format(dateFormatter);
                            endDate = parsedEndDateTime.format(dateFormatter);
                        }
                    } else {
                        break; // Exit the loop if call is not zero
                    }
                } else {
                    String warningMsg = page.locator(callLimitWarnings).innerText();
                    System.out.println("Warning Message: " + warningMsg); // Logging warning message

                    String text = warningMsg.replaceAll("[^0-9,]", "");
                    System.out.println("Text after replacing: " + text); // Logging text after replacing

                    String[] str = text.split(",");
                    System.out.println("Array length: " + str.length); // Logging array length

                    int totalCalls = Integer.parseInt(str[0]);
                    int callLimit = Integer.parseInt(str[1]);
                    if (totalCalls > callLimit) {
                        parsedStartDateTime = parsedStartDateTime.plusDays(1);
                        startDate = parsedStartDateTime.format(dateFormatter);
                    } else {
                        break;
                    }
                }
            }
            click(submitBtn);
            log("Clicked on [ submit ] Button");
        }
    }


    public String getBulkJobSubmitText() {
        if (isLocatorPresent(page, bulkJobSubmitTxt, 1)) {
            String msg = page.locator(bulkJobSubmitTxt).innerText();
            return msg;
        }
        click(finishBtn);
        log("Clicked on [Finish] Button");
        return null;
    }


    public String getMomentDetails() throws Exception {
        waitFor(3);
        if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
            click(activeStatusCrossBtn);
            waitFor(1);
        }
        Locator columns = page.locator(momentsTable);
        waitFor(3);
        StringBuilder rowData = new StringBuilder();
        for (int i = 1; i < columns.count(); i++) {
            String a = page.locator("//tbody/tr[1]/td["+i+"]").innerText();
            rowData.append(a).append(",");
        }
        return rowData.toString();
    }

    public void deleteDraftMoments() {
        //  click("//button[@class='nb__38cLT']//*[name()='svg']");
        log("Clicked on the Remove [ Active Filters ] Button");

        click(editAccordion);
        log("Clicked on the 'Accordion' Button");

        click(deleteMomentBt);
        log("Clicked on the 'Delete' Button");

        String name = page.locator(deleteNameText).innerText();
        System.out.println(name);
        fill(textBox, name);
        log("Entered the 'MomentName' in TextBox");

        click(confirmBtn);
        log("Clicked on 'Confirm' button");
    }


    public void deactivateMoments() {
        waitFor(1);
        try {
            if (isLocatorPresent(page, activateFirstMomentBtn, 1)) {
                click(activateFirstMomentBtn);
                log("Clicked and Deactivated the Moment");
            } else {
                log("Moment is Already Deactivated");
            }
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }


    public void editKeywordMomentDetail() {
        waitFor(3);

        // Check if the "Active" cross button is present and reload if it is
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 2)) {
            click(activeStatusCrossBtn);
            click(reloadBtn);
        }

        waitFor(5);
        waitForSelector(page, momentNameList, 3);

        // Locate all moment names
        Locator momentNames = page.locator(momentNameList);
        for (int i = 0; i < momentNames.count(); i++) {
            momentNames.nth(i).scrollIntoViewIfNeeded();
            String nameTxt = momentNames.nth(i).innerText().trim();
            System.out.println("=============" + nameTxt);

            // Check if the moment name starts with "Test KeyWord"
            if (nameTxt.startsWith("Test KeyWord")) {
                log("Found the Test Moment [" + nameTxt + "]");
                waitFor(1);

                // Click on the corresponding accordion and Edit/View button
                click("//tbody/tr[" + (i + 1) + "]/td[6]//button");
                click(editMomentBtn);
                log("Clicked on [Accordion] Of [" + nameTxt + "] and Clicked On [Edit/View] button");
                waitFor(3);
                break;
            }
            waitFor(2);
        }
    }

    public void editInstructionalMomentDetail() {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 2)) {
            click(activeStatusCrossBtn);
        }
        page.locator(reloadBtn);
        waitFor(2);
        waitForSelector(page, momentNameList, 2);
        Locator momentNames = page.locator(momentNameList);
        System.out.println(momentNames);
        for (int i = 0; i < momentNames.count(); i++) {
            String nameTxt = momentNames.nth(i).innerText();
            if (nameTxt.startsWith("Test Instructional")) {
                log("Found the Test Moment[" + nameTxt + "]");
                waitFor(1);
                click("//tbody/tr[" + (i+1) + "]/td[6]//button"); // Using i+1 because XPath is 1-based

                click(editMomentBtn);
                log("Clicked on [Accordion] Of [" + nameTxt + "] and Clicked On [Edit/View] button ");
                waitFor(3);
                break;
            }
            waitFor(2);
        }
    }


    public void EditMomentDetails() {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 2)) {
            click(activeStatusCrossBtn);
            click(reloadBtn);
        }
        waitFor(2);
        waitForSelector(page, momentNameList, 2);
        Locator momentNames = page.locator(momentNameList);

        for (int i = 0; i < momentNames.count(); i++) {
            String nameTxt = momentNames.nth(i).innerText();
            if (nameTxt.startsWith("Test Symentic") || nameTxt.startsWith("Test KeyWord") || nameTxt.startsWith("Test Instructional")) {
                log("Found the Test Moment[" + nameTxt + "]");
                waitFor(1);
                click("//tbody/tr[" + (i + 1) + "]/td[6]//button"); // Using i+1 because XPath is 1-based

                click(editMomentBtn);
                log("Clicked on [Accordion] Of [" + nameTxt + "] and Clicked On [Edit/View] button ");
                waitFor(3);
                break;
            }
        }
    }


    public List<String> searchMomentByName(List<String> names) {
        List<String> searchResults = new ArrayList<>();
        if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
            click(activeStatusCrossBtn);
            log("Clicked on the [ Active ] Cross Icon");
        }
        page.waitForSelector(searchBtn);
        click(searchBtn);
        log("Clicked on the [ Search ] Button");
        for (String name : names) {
            try {
                fill(momentSearchbar, name);
                page.keyboard().press("Enter");
                waitFor(2);
                String searchName = getText(momentNameLoc);
                searchResults.add(searchName);
                click(searchMonetClearIcon);
            } catch (Exception e) {
                log("Error searching for moment name '" + name + "': " + e.getMessage());
                searchResults.add("N/A"); // Add placeholder for failed search
            }
        }
        return searchResults;
    }

    public List<String> getMomentNames() {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 2)) {
            click(activeStatusCrossBtn);
        }
        List<String> momentNameList = new ArrayList<>();
        waitFor(2);
        Locator loc = page.locator(momentNames);
        for (int i = 1; i < loc.count(); i++) {
            try {
                loc.nth(i).scrollIntoViewIfNeeded();
                String moment = page.locator("//tbody/tr[" + i + "]/td[1]").innerText();
                if (moment.startsWith("Test Symentic") || moment.startsWith("Test KeyWord") || moment.startsWith("Test Instructional")) {
                    momentNameList.add(moment);
                    waitFor(1);
                }
            } catch (Exception e) {
                System.out.println("Error retrieving moment name at row " + i + ": " + e.getMessage());
            }
        }
        return momentNameList;
    }


    public void updateKeywordMoment(List<String> KeywordList) {
        page.waitForSelector(definition);
        click(definition);

        log("Clicked on the 'Definition' Tab");
        waitFor(2);

        page.waitForSelector(selectedKeywordList);
        List<ElementHandle> deleteList = page.querySelectorAll(selectedKeywordList);
        for (int i = 0; i < deleteList.size(); i++) {
            deleteList.get(i).click();
            log("Clicked on [Cross] Button ");
            waitFor(1);
        }
        for (String keyword : KeywordList) {
            fill(searchKeyword, keyword);
            page.keyboard().press("Enter");
        }
    }


    public void updateInstructionalMoment(List<String> List, String url) {
        page.waitForSelector(definition);
        click(definition);

        log("Clicked on the 'Definition' Tab");
        waitFor(2);

        boolean isNexElement = page.isChecked(isNex);
        if (isNexElement) {
            click(isNex);
            log("Clicked on the [Is Next] Checkbox");
        }
        Locator element = page.locator(searchTextBoxInstructional);
        int searchTextSize = List.size();
        for (int i = 0; i < searchTextSize; i++) {
            String txt = List.get(i);
            element.press("Control+A");
            element.press("Backspace");
            fill(searchTextBoxInstructional, txt);
            waitFor(1);
            if (isLocatorPresent(page, urlInputField, 2)) {
                fill(urlInputField, url);
                click(submitBtn);
            } else {
                click(addMoreAudioBtn);
                fill(urlInputField, url);
                click(submitBtn);
            }
        }
    }


    public void updateExitingSymenticMomentDetails(int count) {
        try {
            page.waitForSelector(definition);
            click(definition);
            log("Clicked on the 'Definition' Tab");
            waitFor(2);
            page.waitForSelector(deleteIcons);
            List<ElementHandle> deleteList = page.querySelectorAll(deleteIcons);
            int elementsToDelete = Math.min(count, deleteList.size());
            for (int i = 0; i < elementsToDelete; i++) {
                deleteList.get(i).click();
                log("Clicked on 'Delete' Button ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> getTeams() {
        List<String> teamList = new ArrayList<>();
        page.waitForSelector(selectTeamDrp);
        click(selectTeamDrp);
        waitFor(1);
        log("Clicked on the 'Team' Dropdown");
        List<ElementHandle> teams = page.querySelectorAll(ListDrp);
        for (ElementHandle handle : teams) {
            String team = handle.innerText();
            teamList.add(team);
        }
        return teamList;
    }

    public List<String> searchTeamFilter(List<String> teams) {
        List<String> momentList = new ArrayList<>();
        try {
            for (String team : teams) {
                fill(teamSearchbar, team);
                click(searchTeamResult.replace("txt", team));
                waitFor(2);
                for (int i = 1; i <= 2; i++) { // Changed i < 10 to i <= 10 to include the 10th row
                    String momentName = page.locator("//tbody/tr[" + i + "]/td[1]").innerText();
                    momentList.add(momentName);
                }
                waitFor(1);
                click(crossTeamIcon);
            }
        } catch (Exception e) {
            log("Error: An error occurred searching for teams '" + teams + "': " + e.getMessage());
        }
        return momentList;
    }


    public void updateMomentFeedback() {
        page.waitForSelector(FeedbackTab);
        click(FeedbackTab);
        log("Clicked on the 'Feedback' Tab");
        if (!shouldProcessEditIconBlock()) {
            processFeedbackEditIcons();
        }
        clickNext();
    }

    private boolean shouldProcessEditIconBlock() {
        return true;
    }

    private void processFeedbackEditIcons() {
        page.waitForSelector(feedbackEditIconList);
        List<ElementHandle> editIconList = page.querySelectorAll(feedbackEditIconList);
        List<ElementHandle> negativeFeedbackList = page.querySelectorAll(editNegativeFeedbackList);
        for (ElementHandle edit : editIconList) {
            edit.click();
            for (int i = 0; i < negativeFeedbackList.size(); i += 2) {
                negativeFeedbackList.get(i).click();
                log("Clicked on 'Negative Feedback' Button ");
                waitFor(1);
                click(ext1);
            }
            waitForSelector(page, submitBtn1, 10);
            clickWithForce(submitBtn1);
        }
    }

    public void untagMoment(String startDate, String endDate) throws InterruptedException {
        log("Entering from Bulk Tagging Date :" + startDate);
        type(unTagFromDateTimeLoc, startDate);

        log("Entering from Bulk Tagging To :" + endDate);
        type(unTagToDateTimeLoc, endDate);

        click(submitBtn);
        log("Clicked on submit Button");
    }

    public String getUntagMomentText() {
        String msg = page.locator(untagSubmitTxt).innerText();
        clickFinish();
        return msg;
    }

    public void searchSelectSemanticPhrases(String sentence, int count, String action) {
        page.waitForSelector(searchBar);
        fill(searchBar, sentence);
        page.keyboard().press("Enter");
        log("Pressed the 'Enter' Key ");
        if (isLocatorPresent(page, tryDeepSearchLoc, 3)) {
            log("Phrases Not Found");
            click(tryDeepSearchLoc);
            log("Clicked on 'Try Deep Search' Button ");
        }
        try {
            waitFor(2);
            page.waitForSelector(suggetionsList);
            List<ElementHandle> list = page.querySelectorAll(suggetionsList);
            int elementsToClick = Math.min(count, list.size());
            for (int i = 0; i < elementsToClick; i++) {
                list.get(i).click();
                log("Clicking on 'Phrases Add' Button ");
            }
        } catch (Exception e) {
            log("Error : An Error Occurred while Adding Phrases : ");
            e.printStackTrace();
        }
        handlePhraseTooLongErrors1();
        clickNext();
        waitFor(2);
        try {
            HandlePhraseMatchingErrors(action);
            HandleClusterError();
        } catch (Exception e) {
            log("Exception occurred in Handling 'Cluster and Phrase Matching Errors' : " + e.getMessage());
        }
        clickNext();
    }


    public String getDeletedText() throws Exception {
        String msg = page.locator(deleteSuccessMsg).innerText();
        return msg;
    }

    public void selectPhrases(String types, String sentence, int selectCount, String action) throws Exception {
        waitForElement(page, momentType.replace("name", types), 5);
        clickWithForce(momentType.replace("name", types));
        fill(searchBar, sentence);
        log("Clicked on '" + types + "' and Entered the search " + sentence);

        page.keyboard().press("Enter");
        log("Pressed the [Enter] Key ");

        if (isLocatorPresent(page, tryDeepSearchLoc, 3)) {
            log("Phrases Not Found");
            clickWithForce(tryDeepSearchLoc);
            log("Clicked on [Try Deep Search] Button ");
        }

        waitFor(5);
        waitForLocatorClickable(page, suggetionsList, 120);
        Locator suggestionListLoc = page.locator(suggetionsList);
        int elementsToClick = Math.min(selectCount, suggestionListLoc.count());
        for(int i=0; i<elementsToClick;i++){
            suggestionListLoc.nth(i).scrollIntoViewIfNeeded();
            suggestionListLoc.nth(i).click();
            log("Clicked on 'Phrases Add' Button ");
        }
        //page.evaluate("window.scrollTo(0, -500);");
        handlePhraseTooLongErrors1();
        clickNext();
        waitFor(2);
        try {
            HandlePhraseMatchingErrors(action);
            HandleClusterError();
        } catch (Exception e) {
            log("Exception occurred in Handling 'Cluster and Phrase Matching Errors' : " + e.getMessage());
        }
        clickNext();
    }

    public String getMomentCreatedText() {
        isLocatorPresent(page, momentCreatedSuccessfullyLLoc, 3);
        String text = page.locator(momentCreatedSuccessfullyLLoc).innerText();
        return text;
    }

    public void submitMomentFeedback() {
        page.waitForSelector(giveFeedBack);
        clickWithForce(giveFeedBack);
        log("Clicked on [Feedback] button");
        waitFor(2);
        if (page.isEnabled(NextBtn)) {
            clickNext();
        } else {
            submitPositiveFeedback();
            waitFor(2);
            if (isLocatorPresent(page, skipFeedBack, 2)) {
                clickWithForce(skipFeedBack);
            } else {
                page.waitForSelector(feedbackSubmittedSuccessfully);
                String msg = page.locator(feedbackSubmittedSuccessfully).innerText();
                Assert.assertEquals(msg, "Success! Moment feedback submitted successfully");
            }
            clickWithForce(NextBtn);
        }
    }

    public void submitPositiveFeedback() {
        page.waitForSelector(feedbackPositivePositive);
        boolean moreLists = true;
        do {
            Locator loc = page.locator(feedbackPositivePositive);
            for (int i = 0; i < loc.count(); i++) {
                loc.nth(i).click();
            }
            click(submitBtn);
            log("Clicked on [Submit] button");
            waitFor(5);
            if (isLocatorPresent(page, secondFeedbackPageEle, 2)) {
                click(secondFeedbackPageEle);
                List<ElementHandle> nextList = page.querySelectorAll(feedbackPositivePositive);
                moreLists = !nextList.isEmpty();
            }
        } while (moreLists);
    }

    public void handlePhraseTooLongErrors() {
        if (isLocatorPresent(page, highlightCross, 3)) {  // Check for element within 5 seconds
            log("'Highlight' Button Appeared");
            List<ElementHandle> highlightCrosses = page.querySelectorAll(highlightCross);
            for (int i = 0; i < highlightCrosses.size(); i++) {
                ElementHandle highlightCross = highlightCrosses.get(i);
                try {
                    if (!highlightCross.isVisible() || !highlightCross.isEnabled()) {
                        log("Cross icon number " + (i + 1) + " not yet visible or clickable");
                        highlightCross.scrollIntoViewIfNeeded(); // Scroll to make it visible
                    }
                    highlightCross.click();
                    log("Clicked on Cross icon number " + (i + 1));
                } catch (Exception e) {
                    log("Error occurred while handling Cross icon number " + (i + 1) + ": " + e.getMessage());
                    try {
                        highlightCross.scrollIntoViewIfNeeded();
                    } catch (Exception scrollException) {
                        log("Error occurred while scrolling to Cross icon number " + (i + 1) + ": " + scrollException.getMessage());
                    }
                    try {
                        highlightCross.click();
                        log("Clicked on Cross icon number " + (i + 1) + " after retrying.");
                    } catch (Exception clickException) {
                        if (clickException.getMessage().contains("Element is not attached to the DOM")) {
                            log("Element is not attached to the DOM while clicking on Cross icon number " + (i + 1));
                        } else {
                            log("Error occurred while clicking on Cross icon number " + (i + 1) + " after retrying: " + clickException.getMessage());
                        }
                    }
                }
            }
        } else {
            log("'Highlight' Button not found");
        }
    }

    public void handlePhraseTooLongErrors1() {
        try {
            if (isLocatorPresent(page, highlight, 3)) {
                page.waitForSelector(highlightCross);
                List<ElementHandle> list = page.querySelectorAll(highlightCross);
                for (ElementHandle element : list) {
                    try {
                        element.scrollIntoViewIfNeeded();
                        element.click();
                        log("Clicked on the 'Highlight Cross' Button");

                    } catch (Exception clickException) {
                        log("Error occurred while clicking on the 'Highlight Cross' Button: " + clickException.getMessage());
                    }
                }
            } else {
                log("Highlight Button not present");
            }
        } catch (Exception e) {
            log("Error occurred while handling the 'Highlight Button': " + e.getMessage());
        }

    }


    public void HandlePhraseMatchingErrors(String action) {
        page.waitForSelector(takeActionBtn);
        if (isLocatorPresent(page, takeActionBtn, 3)) {
            log("'Phrase does not match.'Take Action button appeared.");
            List<ElementHandle> takeActionList = page.querySelectorAll(takeActionBtn);
            for (ElementHandle takeActionElement : takeActionList) {
                takeActionElement.scrollIntoViewIfNeeded();
                page.waitForTimeout(500);
                takeActionElement.click();
                log("Clicked on 'Take Action' Button successfully ");
                waitFor(1);
                List<ElementHandle> takeActionOptionList = page.querySelectorAll(takeActionOptions);
                for (ElementHandle ActionOption : takeActionOptionList) {
                    if (ActionOption.innerText().equals(action)) {
                        ActionOption.scrollIntoViewIfNeeded();
                        ActionOption.click();
                        log("Clicked on '" + action + "' Action Button successfully");
                    } else {
                        if (ActionOption.innerText().equals(action)) { // Use else if here
                            ActionOption.click();
                            waitForLocatorClickable(page, suggestSimplerPhraseOptions, 120);
                            List<ElementHandle> phraseList = page.querySelectorAll(suggestSimplerPhraseOptions);
                            for (ElementHandle phrase : phraseList) {
                                phrase.click();
                            }
                        }
                    }
                }
            }
        }
    }

    public void HandleClusterError() {
        if (isLocatorPresent(page, clusterError, 5)) {
            log("'Error : Cluster Error Occurred'");
            page.waitForSelector(addAnywayBtn);
            List<ElementHandle> addButtons = page.querySelectorAll(addAnywayBtn);
            for (ElementHandle handle : addButtons) {
                handle.scrollIntoViewIfNeeded();
                handle.click();
                log("Clicked on 'Add Anyway' Button successfully");
            }
        }
    }

    public void createGlobalMoment(String MomentName, String momentType, String emotion) {
        click(createMomentBtn);
        log("Clicked on the 'Create Moment' Button successfully");
        try {
            fill(momentNameTxt, MomentName);
            click(momentCreatBtns.replace("txt", momentType));
            click(momentCreatBtns.replace("txt", emotion));
            log("Entered[" + MomentName + "],Clicked [" + momentType + "] and [" + emotion + "]");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Buttons");
        }
        try {
            click(selectConversationType);
            click(conversationTypeDrp);
            click(callerType);
            log("Clicked [Select Conversation],[Call] and [Selected Caller] ");

            click(selectAllCallerType);
            log("Clicking on 'Select All' Successfully");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Selecting filter values");
        }
        click(sideEle);
        conditionalMoment();
        clickNext();
    }

    public void createFilteredMoment(String MomentName, String type, String emotion) {
        click(createMomentBtn);
        log("Clicked on [Create Moment] Button Successfully");
        try {
            fill(momentNameTxt, MomentName);
            log("Entered Moment Name [ " + MomentName + " ] in text Field");

            click(filterBtn.replace("type", type));
            log("Clicked on [" + type + "] Button");

            click(filterBtn.replace("type", emotion));
            log("Clicked on [ " + emotion + " ] Button Successfully");
        } catch (Exception e) {
            log("Error: An unexpected error occurred while Buttons");
        }
    }


    public List<String> getCampaignNamesDropdownValues(String scope, String selectType) {
        List<String> campaignNames = new ArrayList<String>();
        try {
            click(selectConversationType);
            click(conversationTypeDrp);
            log("Clicked on [Select Conversation ] and [Call] Dropdown ");

            click(filterBtn.replace("type", scope));
            click(callFilterDrp.replace("txt", selectType));
            log("Clicked on[ " + scope + " ] and [ " + selectType + " ] ");
            waitFor(2);

            page.waitForSelector(campaignNameList);
            Locator locators = page.locator(campaignNameList);

            for (int i = 0; i < locators.count(); i++) {
                locators.nth(i).scrollIntoViewIfNeeded();
                String txt = locators.nth(i).innerText();
                campaignNames.add(txt);
            }
        } catch (Exception e) {
            log("Error : An Error occurred while Extracting 'CampaignName Filter' : " + e.getMessage());
        }
        return campaignNames;
    }


    public void selectCampaginNamesAndCallingMode(List<String> campaignNames, int count) {
        try {
            page.waitForSelector(searchFilters);
            // Iterate over the first 5 elements of the list
            for (int i = 0; i < Math.min(campaignNames.size(), count); i++) {
                String option = campaignNames.get(i);
                fill(searchFilters, option);
                log("Entering in Searchbar [ " + option + " ]in the text Field");

                click(labelSearch.replace("txt", option));
                log("Clicked on [ " + option + " ]");
            }
        } catch (Exception e) {
            log("Error: An error occurred while selecting 'CampaignName Filter': " + e.getMessage());
        }
        click(callFilterDrp.replace("txt", "Select Mode of calling"));
        log("Clicked on 'Select Mode of calling' Dropdown");

        waitFor(2);
        log("Clicking on 'Select All'");
        click(selectAllCallerType);
    }


    public List<String> getProcessNames(String process) {
        List<String> processNames = new ArrayList<String>();
        waitFor(2);
        click(callFilterDrp.replace("txt", process));
        page.waitForSelector(processNameList);
        Locator locators = page.locator(processNameList);

        for (int i = 0; i < locators.count(); i++) {
            locators.nth(i).scrollIntoViewIfNeeded();
            String txt = locators.nth(i).innerText();
            processNames.add(txt);
        }
        return processNames;
    }


    public void selectProcessNameAndCallerType(List<String> processNames, int count) {
        try {
            for (int i = 0; i < Math.min(processNames.size(), count); i++) {
                String option = processNames.get(i);
                fill(searchFilters, option);
                log("Entering in Searchbar '" + option + "' in text Field");

                page.waitForSelector(labelSearch.replace("txt", option));
                click(labelSearch.replace("txt", option));
                log("Clicked on ProcessName: '" + option + "'");

                click(searchCross);
                log("Cleared the ProcessName: '" + option + "'");
            }
        } catch (Exception e) {
            log("Error: An error occurred while selecting 'processName Filter': " + e.getMessage());
        }

        click(callFilterDrp.replace("txt", "Select Caller Type"));
        log("Clicking on 'Select Mode of calling' Dropdown");
        waitFor(2);

        click(selectAllCallerType);
        log("Clicking on 'Select All'");
        click("//p[.='Campaign Name *']");

        click(NextBtn);
    }


    public void filteredTeamsDropdowns(String scope, List<String> teamNames) {
        try {
            click(filterBtn.replace("type", scope));
            log("Clicking on '" + scope + "'Button");

            click(selectYourTeamDrp);
            log("Clicking on 'Select Team' Dropdown'");
            waitFor(2);
            List<ElementHandle> teams = page.querySelectorAll(teamList);
            for (int i = 0; i < teams.size(); i++) {
                String name = teams.get(i).innerText();
                if (teamNames.contains(name)) {
                    teams.get(i).click();
                    log("Selecting '" + name + "'Team");
                }
            }
            click(callerType);
            log("Clicking on 'Select Caller Type' Dropdown'");
            waitFor(1);

            click(selectAllCallerType);
            log("Clicking on 'Select All'");
        } catch (Exception e) {
            log("Error : An Error occurred while Selecting Team Filters : " + e.getMessage());
        }
    }

    public void clickNext() {
        if(isLocatorPresent(page,NextBtn,2)){
            try {
                page.waitForSelector(NextBtn);
                click(NextBtn);
                log("Clicked on 'Next' Button ");
            } catch (Exception e) {
                log("Error : An Error occurred while Clicking on 'Next' Button : " + e.getMessage());
            }
        }
    }

    public void clickFinish() {
        if(isLocatorPresent(page,finishBtn,2)) {
            try {
                page.waitForSelector(finishBtn);
                click(finishBtn);
                log("Clicked on 'Next' Button ");
            } catch (Exception e) {
                log("Error : An Error occurred while Clicking on 'Next' Button : " + e.getMessage());
            }
        }
    }

    public void conditionalMoment() {
        if (!isLocatorPresent(page, conditionalCheckBox, 5)) {
            scrollIntoView(page, conditionalCheckBox);
        }
        try {
            click(conditionalCheckBox);
            log("Clicked on the [Conditional Moment] checkbox");

            click(selectParentMomentDrp);
            log("Clicked on the [Select Parent Moment] dropdown");

            Locator parentMomentList = page.locator(parentList);
            parentMomentList.nth(0).click();
            log("Clicked on the first [parent moment]");
        } catch (Exception e) {
            log("Error : An Error occurred while selecting on the [Conditional Moments ] : " + e.getMessage());
        }
    }

    public void login(String email, String password) {
        page.reload();
        waitFor(2);
        try {
            fill(emailField, email);
            log("Entered Email: [" + email + "]");

            fill(passwordField, password);
            log("Entered Password: [" + password + "]");

            click(loginBtn);
            log("Clicked on[Login] Button");

            waitFor(2);
            if (isLocatorPresent(page, loginBtn, 2)) {
                clickWithForce(loginBtn);
                log("Clicked on[Login] Button");
            }

        } catch (Exception e) {
            log("Error : An Error occurred while Login : " + e.getMessage());
        }
    }

    public void loginWithGmail(String email, String password) {
        try {
            waitFor(2);
            clickWithForce(signInGoogleBtn);
            log("Clicked on [Sign In with Google] Button");

            fill(gmailInputField,email);
            clickNext();

            fill(gmailPasswordInputField,password);
            clickNext();

        } catch (Exception e) {
            log("Error: An error occurred while logging in: " + e.getMessage());
        }
    }


    public String getCheckedColumns() throws InterruptedException {
        click(columnFilterBtn);
        Locator loc = page.locator(checkedColumns);
        StringBuilder sb = new StringBuilder();
        try {
            page.waitForSelector(checkedColumns);
            List<String> all = loc.allInnerTexts();
            System.out.println("Number of elements: " + all.size());
            for (String s : all) {
                sb.append(s).append(",");
            }
        } catch (PlaywrightException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getAgentTableColumnsNames() {
        StringBuilder sb = new StringBuilder();
        String checklist = page.locator(checkListCoverageColumn).innerText();
        String actions = page.locator(actionsColumn).innerText();
        sb.append(checklist).append(",");
        sb.append(actions).append(",");
        for (int i = 1; i <= 7; i++) {
            String columnName = page.locator("//tr/th[" + i + "]//p").innerText();
            sb.append(columnName).append(",");
        }
        return sb.toString();
    }

    public String selectHeaderOption(String header) {
        String headerLocators = headerLocator.replace("menu", header);
        try {
            click(headerLocators);
            return "Header option '" + header + "' selected successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to select header option '" + header + "'.";
        }
    }

    public void searchColumnFilter(String... columns) throws InterruptedException {
        click(columnFilterBtn);
        log("Clicking on Column Filter Button");
        for (String col : columns) {
            fill(SearchBar, col);
            log("Entering ColumnName '" + col + "' in Searchbar");
            click("//input[@type='checkbox']");
            log("CLicking on Checkbox");
            click(clearTextIcon);
            log("Clearing the text from Searchbar");
        }
        click(columnFilterBtn);
        log("Clicking on Column Filter Button");
    }

    public void removeColumnFilter(String... columns) throws InterruptedException {
        click(columnFilterBtn);
        log("Clicking on Column Filter Button");
        for (String col : columns) {
            fill(SearchBar, col);
            log("Entering ColumnName '" + col + "' in Searchbar");
            click("//input[@type='checkbox']");
            log("CLicking on Checkbox");
            click(clearTextIcon);
            log("Clearing the text from Searchbar");
        }
        click(columnFilterBtn);
        log("Clicking on Column Filter Button");
    }


}
