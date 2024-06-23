package com.convozen.Pages.Playwrights;

import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static com.convozen.Pages.Playwrights.Locators.*;

public class ConvozenChecklists extends CommonPlaywright {
    public Page page;

    public ConvozenChecklists(Page page) {
        super(page);
        this.page = page;
    }

    public int viewCalls() {
        int callsOccurred = 0;
        waitFor(2);
        page.waitForSelector(checklistNameList);
        Locator loc = page.locator(checklistNameList);
        for (int i = 1; i < 2; i++) {
            click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
            page.waitForSelector(viewCallsBtn);
            waitFor(5);

            Page popup = page.waitForPopup(() -> {
                click(viewCallsBtn);
                log("Clicked on [Accordian ],[view Calls]");
            });

            popup.click(todayCross);
            waitFor(5);

            Locator pageLocators = popup.locator(audioBtns);
            callsOccurred = pageLocators.count();
            System.out.println("Calls occurred : " + callsOccurred);
            popup.close();
        }
        return callsOccurred;
    }


    public Map<String, Object> getCallsCoverage() {
        List<String> callCoverage = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
            click(defaultCallFilterCross.replace("txt", "active"));
            log("Clicked on [Active] cross button");
        }
        waitFor(2);
        page.waitForSelector(checklistNameList);
        for (int i = 1; i < 2; i++) {
            click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
            log("Clicked on [Accordian] button");
            page.waitForSelector(taggedCoverage);
            waitFor(5);
            Locator locator = page.locator(taggedCoverage);
            for (int j = 0; j < locator.count(); j++) {
                String text = locator.nth(j).innerText();
                String[] str = text.split("\\n");
                String percentage = str[0].replace("%", "");
                callCoverage.add(percentage);
            }
            waitFor(1);
        }
        map.put("Calls Full Coverage", callCoverage.get(0));
        map.put("Calls Partial Coverage", callCoverage.get(1));
        map.put("Calls Null Coverage", callCoverage.get(2));

        return map;
    }


    public List<String> getCallsOccurred() {
        List<String> callsOccurred = new ArrayList<>();
        waitFor(2);
        page.waitForSelector(checklistNameList);
        Locator loc = page.locator(checklistNameList);
        for (int i = 1; i < loc.count(); i++) {
            click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
            log("Clicked on [Accordian] button");

            page.waitForSelector(occurredCalls);
            waitFor(5);

            String taggedCalls = getText(".nb__3_72a");
            System.out.println(" Counts : " + taggedCalls);

            waitFor(1);
            callsOccurred.add(taggedCalls);
        }
        return callsOccurred;
    }


    private void addCoverageValue(Map<String, List<String>> callCoverage, String key, String value) {
        callCoverage.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }


    public void selectDropdownValues(List<String> list) {
        waitFor(2);
        int count = 0;
        for (String value : list) {
            fill(searchFild, value);
            click(searchResultCh.replace("txt", value));
            clickWithForce(searchCrossCamp);
            log("Entered,Clicked and Cleared the [" + value + "]");

            waitFor(1);
            count++;
            if (count == 5) {
                break;
            }
        }
    }

    public String isChecklistTypeChecked(String type) {
        if (isLocatorPresent(page, checklistAccordianFirst, 2)) {
            try {
                click(checklistAccordianFirst);
                click(editChecklistBtn);
                waitFor(3);
                String checklistType = page.locator("//*[@class='nb__24RxP']").innerText();
                page.reload();
                waitFor(3);
                if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
                    click(defaultCallFilterCross.replace("txt", "active"));
                    log("Clicked on [Active] cross button");
                }
                if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", type), 5)) {
                    click(defaultCallFilterCross.replace("txt", type));
                    log("Clicked on [" + type + "] cross button");
                }
                return checklistType;
            } catch (Exception e) {
               System.err.println("Error : An Error Occurred"+e.getMessage());
            }
        }
        return null;
    }

    public void checklistDetailsFilters(String filterName, String selectValue, String type) {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
            click(defaultCallFilterCross.replace("txt", "active"));
            log("Clicked on [Active] cross button");
        }
        waitFor(3);
        page.waitForSelector(mainFilterBtn);
        click(mainFilterBtn);
        try {
            click(filterNameBtn.replace("txt", filterName));
            click(drpTxt.replace("txt", selectValue));
            waitFor(2);
            click(checklistType.replace("txt",type));
            log("Clicked on ["+filterName+"],["+selectValue+"]");
            click(submitChecklistBtn);
            log("Clicked on ["+type+"] dropdown And [Submit] button");
        } catch (Exception e) {
            log(" Error : Error Occurred while applying " + selectValue + " filter");
        }
    }

    public String deleteTestChecklist() {
        waitFor(2);
        String lastDeleteSuccessMsg = "";
        waitFor(3);
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
            click(defaultCallFilterCross.replace("txt", "active"));
            log("Clicked on [Active] cross button");
        }
        click(reloadBtn);
        log("Clicking on [Reload] button");

        waitFor(2);
        waitForSelector(page, checklistNameList, 5);
        boolean checklistFound = false;

        while (true) {
            Locator checklists = page.locator(checklistNameList);
            int counts = checklists.count();
            boolean foundInCurrentIteration = false;

            for (int i = 0; i < counts; i++) {
                String nameTxt = checklists.nth(i).innerText();
                if (nameTxt.startsWith("Test Checklist")) {
                    checklistFound = true;
                    foundInCurrentIteration = true;
                    log("Found the Test checklist[" + nameTxt + "]");
                    waitFor(1);
                    click("//tbody/tr[" + (i + 1) + "]/td[10]//button");

                    click(deleteChecklistBtn);
                    String str = getText(deletechecklistNameTxt);
                    fill(textBox, str);

                    click(confirmBtn);
                    log("Clicked on [Accordion], [Delete] button, and Entered [" + nameTxt + "] and [Delete Confirmed]");
                    waitFor(3); // Wait for 3 seconds to allow the delete action to complete
                    lastDeleteSuccessMsg = page.locator(deleteSuccessMsg).innerText(); // Capture the success message
                    page.reload();
                    log("Reloading the Page");
                    waitFor(5);

                    if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
                        click(defaultCallFilterCross.replace("txt", "active"));
                    }
                    waitFor(3);
                    break;
                }
            }
            if (!foundInCurrentIteration) {
                break;
            }
        }
        if (!checklistFound) {
            log("No Checklist found Starting with [Test Checklist]");
            System.exit(0); // Exit the program with status code 0
        }

        return lastDeleteSuccessMsg;
    }

    public void deactivateTestChecklist() {
        waitFor(5);
        page.waitForSelector(checklistNameList);

        Locator checklistNames = page.locator(checklistNameList);
        boolean checklistFound = false;

        for (int i = 0; i < checklistNames.count(); i++) {
            String nameTxt = checklistNames.nth(i).innerText();
            if (nameTxt.startsWith("Test Checklist")) {
                checklistFound = true;
                log("Active [Test Moment :" + nameTxt + "]");

                waitFor(1);
                click(activeChecklist + "[" + (i + 1) + "]");
                log("Clicking on [Confirm] button");

                click(confirmBtn);
                log("Successfully Deactivated Moment[" + nameTxt + "]");
                page.reload();

                waitFor(3);
                checklistNames = page.locator(checklistNameList);
            }
        }
        if (!checklistFound) {
            log("Not Found Activate Checklist Starting With[Test Checklist]");
        }
    }

    public void deleteChecklist(String name) {
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


    public List<Integer> getMomentCounts() throws Exception {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 2)) {
            click(defaultCallFilterCross.replace("txt", "active"));
        }
        waitFor(2);
        List<Integer> counts = new ArrayList<Integer>();
        for (int i = 1; i < 3; i++) {
            click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
            page.waitForSelector(viewAll);
            waitFor(2);
            String text = page.innerText(addedMomentText);
            String str = text.replaceAll("[^0-9]", "");
            int momentCount = Integer.parseInt(str);
            counts.add(momentCount);
            refreshAndWait(page, 2);
        }
        return counts;
    }


    public List<Integer> getMomentCountsOfChecklist() throws Exception {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 2)) {
            click(defaultCallFilterCross.replace("txt", "active"));
        }
        waitFor(2);
        List<Integer> counts = new ArrayList<Integer>();
        for (int i = 0; i < 1; i++) {
            click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
            page.waitForSelector(viewAll);
            waitFor(2);
            String text = page.innerText(addedMomentText);
            String str = text.replaceAll("[^0-9]", "");
            int momentCount = Integer.parseInt(str);
            counts.add(momentCount);
            refreshAndWait(page, 2);
        }
        return counts;
    }


    public List<Integer> getMomentOfChecklist() {
        List<Integer> momentCount = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            click("//tbody/tr[" + i + "]/td[9]//button");
            waitFor(2);
            Locator loc = page.locator(addedMomentList);
            int count = loc.count();
            momentCount.add(count);
            click("//tbody/tr[" + i + "]/td[9]//button");
        }
        return momentCount;
    }


    public String getMomentOfChecklists() throws Exception {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 2)) {
            click(defaultCallFilterCross.replace("txt", "active"));
        }
        waitFor(2);
        StringBuilder sb = new StringBuilder(0);
        page.waitForSelector(accordianChecklist);
        waitFor(2);
        for (int i = 1; i < 3; i++) {
            click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
            waitFor(2);
            page.waitForSelector(excludeInactiveMoments);
            Locator loc = page.locator(excludeInactiveMoments);
            for (int j = 0; j < loc.count(); j++) {
                loc.nth(j).scrollIntoViewIfNeeded();
                String txt = loc.nth(j).innerText();
                sb.append(txt.trim()).append(",");
            }
            if (isLocatorPresent(page, inactiveMomentsInChecklists, 2)) {
                page.locator(inactiveMomentsInChecklists).scrollIntoViewIfNeeded();
                String a = page.locator(inactiveMomentsInChecklists).innerText();
                sb.append(a.trim()).append(",");
            }
            refreshAndWait(page, 2);
        }
        return sb.toString();
    }

    public List<String> getMomentsChecklists() throws Exception {
        waitFor(2);
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 2)) {
            click(defaultCallFilterCross.replace("txt", "active"));
        }
        List<String> momentsChecklists = new ArrayList<String>();
        page.waitForSelector(accordianChecklist);
        waitFor(2);
        Locator accordianList = page.locator(accordianChecklist);
        for (int i = 0; i < 1; i++) {
            accordianList.nth(i).click();
            waitFor(2);

            click(viewAll);
            log("Clicked on the [viewAll] button");

            waitFor(2);

            if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Active"), 3)) {
                click(defaultCallFilterCross.replace("txt", "Active"));
            }

            page.waitForSelector(momentNames);
            waitFor(2);
            Locator loc = page.locator(momentNames);
            for (int j = 0; j < loc.count(); j++) {
                loc.nth(j).scrollIntoViewIfNeeded();
                String txt = loc.nth(j).innerText();
                momentsChecklists.add(txt);
                waitFor(1);
            }

            waitFor(2);
            if (page.locator(nextPagination).isEnabled()) {
                click(nextPagination);
                log("Clicked on the [Next] page]");

                waitFor(2);
                page.waitForSelector(momentNames);
                Locator loc1 = page.locator(momentNames);
                for (int j = 0; j < loc1.count(); j++) {
                    loc1.nth(j).scrollIntoViewIfNeeded();
                    String txt = loc1.nth(j).innerText();
                    momentsChecklists.add(txt);
                    waitFor(1);
                }
            }
            click(headerLocator.replace("menu", "Checklist"));
            log("Clicked on the [Checklist] header option");
            waitFor(2);
        }
        return momentsChecklists;
    }

    public String tagUntagChecklist(String value, String value1, String actionType, String startDate, String endDate) throws Exception {
        waitFor(2);
        waitForSelector(page, checklistNameList, 5);
        String responseText = "";

        boolean checklistFound = false;
        int iterationLimit = 10; // Add an iteration limit to prevent infinite looping
        int currentIteration = 0;

        while (!checklistFound && currentIteration < iterationLimit) {
            Locator checklists = page.locator(checklistNameList);
            int counts = checklists.count();

            for (int i = 0; i < counts; i++) {
                String nameTxt = checklists.nth(i).innerText();
                if (nameTxt.startsWith("Test Checklist")) {
                    checklistFound = true;
                    log("Found the Test checklist[" + nameTxt + "]");
                    waitFor(1);
                    click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
                    click(editChecklistBtn);
                    log("Clicked on the Checklist [Accordion] and [Edit] Button");

                    click(headerLoc.replace("txt", value));
                    click(headerLoc.replace("txt", value1));

                    if (actionType.equals("Bulk tag")) {
                        type(checklistDateFrom, startDate);
                        type(checklistDateTo, endDate);
                        log("Entered Tagged from [" + startDate + "] to [" + endDate + "] date");

                        clickSubmit();
                        page.waitForSelector(checklistTagError);
                        responseText = getText(checklistTagError);

                    } else if (actionType.equals("Bulk untag")) {
                        type(checklistDateFrom2, startDate);
                        type(checklistDateTo2, endDate);
                        log("Entered Untagged from [" + startDate + "] to [" + endDate + "] date");

                        clickSubmit();
                        page.waitForSelector(checklistResponseTxt);
                        responseText = getText(checklistResponseTxt);
                    }
                    clickFinish();
                    refreshAndWait(page, 3);
                    return responseText;
                }
            }

            if (!checklistFound) {
                waitFor(2); // Wait before the next iteration to avoid tight loop
            }
            currentIteration++;
        }

        if (!checklistFound) {
            log("No checklist starting with 'Test Checklist' was found after " + iterationLimit + " iterations.");
        }
        return responseText; // Return responseText instead of null for consistency
    }


    public void taggingAttributes(String value, String value1, String priority) {
        waitFor(2);
        waitForSelector(page, checklistNameList, 5);

        boolean checklistFound = false;
        int iterationLimit = 10; // Add an iteration limit to prevent infinite looping
        int currentIteration = 0;

        while (!checklistFound && currentIteration < iterationLimit) {
            Locator checklists = page.locator(checklistNameList);
            int counts = checklists.count();

            for (int i = 0; i < counts; i++) {
                String nameTxt = checklists.nth(i).innerText();
                if (nameTxt.startsWith("Test Checklist")) {
                    checklistFound = true;
                    log("Found the Test checklist[" + nameTxt + "]");
                    waitFor(1);
                    click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
                    click(editChecklistBtn);
                    log("Clicked on the Checklist [Accordion] and [Edit] Button");

                    click(headerLoc.replace("txt", value));
                    click(headerLoc.replace("txt", value1));

                    log("Clicked on the [" + value + "] and [" + value1 + "] from Header");

                    addTrackerAndShowOnlyTagged(priority);
                    break; // Exit the for loop once the checklist is found and processed
                }
            }
            if (!checklistFound) {
                waitFor(2); // Wait before the next iteration to avoid tight loop
            }
            currentIteration++;
        }
        if (!checklistFound) {
            log("No checklist starting with 'Test Checklist' was found after " + iterationLimit + " iterations.");
        }
    }

    public void addTrackerAndShowOnlyTagged(String priority) {
        if (!isElementPresent(page, checkedTrackerLoc)) {
            click(trackerLocatorCheckBox);
            log("Clicked on [Tracker]");
        }
        if (!isElementPresent(page, checkedShowOnlyTagged)) {
            click(showOnlyTagged);
            log("Clicked [ShowOnlyTagged] Checkbox");

            fill(UIPriority, priority);
            log("Entered the UI Priority [" + priority + "] ");
        }
    }


    public String checklistActivateDeactivate(List<String> checkList, String action) throws Exception {
        waitFor(2);
        String Confirmation = "";

        for (String name : checkList) {
            click(searchBtn);
            log("Clicked on the [Search] Button");

            if (!isLocatorPresent(page, getEmptySearchBarChecklist, 3)) {
                click(searchCrossIcon);
                log("Clicked on the [Cross] icon");
            }
            fill(checklistSearchBar, name);
            page.keyboard().press("Enter");
            log("Entered the checkList [" + name + "] in Searchbar and Pressed 'Enter Key'");
            waitFor(3);

            click(chkListActionCheckbox);
            log("Clicked on the [Action CheckBox] Button");

            page.waitForSelector(chkCheckedBoxes);
            Locator checkedCheckBoxes = page.locator(chkCheckedBoxes);
            for (int i = 0; i < checkedCheckBoxes.count(); i++) {
                if (i == 0) continue;
                checkedCheckBoxes.nth(i).click();
                log("Clicked on checkbox " + (i + 1));
                checkedCheckBoxes = page.locator(chkCheckedBoxes);
            }

            click(bulkTagActionBtn);
            log("Clicked on the [Action Button] Button");
            List<ElementHandle> locatorList = page.querySelectorAll(bulkActionBtnList);
            for (ElementHandle locator : locatorList) {
                String locatorText = locator.innerText();
                if (locatorText.equals(action)) {
                    locator.scrollIntoViewIfNeeded();
                    locator.click();
                    log("Clicked on the '" + action + "' Action Button");
                    clickConfirm();
                    if (locatorText.equals("Activate")) {
                        page.waitForSelector(activationCompleted);
                        Confirmation = page.locator(activationCompleted).innerText();
                    } else if (locatorText.equals("Deactivate")) {
                        page.waitForSelector(deactivationCompleted);
                        Confirmation = page.locator(deactivationCompleted).innerText();
                    }
                }
            }
            refreshAndWait(page, 3);

            click(defaultCallFilterCross.replace("txt", name));
            log("Clicked on the [" + name + "] Applied Filter Cross button");
            waitFor(2);
        }
        return Confirmation;
    }


    public String checklistActivateDeactivate1(List<String> checkList, String action, String startDate, String endDate) throws Exception {
        waitFor(2);
        String Confirmation = "";

        page.waitForSelector(activeStatusCrossBtn);
        click(activeStatusCrossBtn);
        log("Clicked on the [ Active ] Status Cross Button");

        for (String name : checkList) {
            click(searchBtn);
            log("Clicked on the [Search] Button");

            if (!isLocatorPresent(page, getEmptySearchBarChecklist, 3)) {
                click(searchCrossIcon);
                log("Clicked on the [Cross] icon");
            }
            fill(checklistSearchBar, name);
            page.keyboard().press("Enter");
            log("Entered the checkList [" + name + "] in Searchbar and Pressed 'Enter Key'");
            waitFor(3);

            click(chkListActionCheckbox);
            log("Clicked on the [Action CheckBox] Button");

            page.waitForSelector(chkCheckedBoxes);
            Locator checkedCheckBoxes = page.locator(chkCheckedBoxes);
            for (int i = 0; i < checkedCheckBoxes.count(); i++) {
                if (i == 0) continue;
                checkedCheckBoxes.nth(i).click();
                log("Clicked on checkbox " + (i + 1));
                checkedCheckBoxes = page.locator(chkCheckedBoxes);
            }

            click(bulkTagActionBtn);
            log("Clicked on the [Action Button] Button");
            List<ElementHandle> locatorList = page.querySelectorAll(bulkActionBtnList);
            for (ElementHandle locator : locatorList) {
                String locatorText = locator.innerText();
                if (locatorText.equals(action)) {
                    locator.scrollIntoViewIfNeeded();
                    locator.click();
                    log("Clicked on the '" + action + "' Action Button");
                    clickConfirm();
                    if (locatorText.equals("Bulk tag")) {
                        locator.click();
                        waitFor(2);
                        type(toChecklistDate, startDate);
                        type(fromChecklistDate, endDate);
                    } else if (locatorText.equals("Bulk untag")) {
                        locator.click();
                        type(toChecklistDate, startDate);
                        type(fromChecklistDate, endDate);
                    }
                }
                clickConfirm();
                Confirmation = getText(processInitiated1);
            }
            refreshAndWait(page, 3);
        }
        return Confirmation;
    }


    public String checklistBulkTagUntag(List<String> checkList, String action, String startDate, String endDate) {
        try {
            performCommonActions(checkList);
            click(chkListActionCheckbox);
            log("Clicked on the [Action CheckBox] Button");
            page.waitForSelector(chkCheckedBoxes);
            List<ElementHandle> checkedBoxesList = page.querySelectorAll(chkCheckedBoxes);
            if (checkedBoxesList.size() == 1) {
                log("Only one checkbox found. Ignoring the Unchecking.");
            } else {
                for (int i = 1; i < checkedBoxesList.size(); i++) {
                    if (checkedBoxesList.get(i).isEnabled()) {
                        checkedBoxesList.get(i).click();
                        log("Clicked on the checkbox " + (i + 1));
                        waitFor(500);
                    } else {
                        log("Checkbox " + (i + 1) + " is not enabled.");
                    }
                }
            }
            click(bulkTagActionBtn);
            log("Clicked on the [Action Button] Button");
            Locator locators = page.locator(bulkActionBtnList);
            if (action.equals("Bulk tag")) {
                locators.nth(0).click();
                waitFor(2);
                type(toChecklistDate, startDate);
                type(fromChecklistDate, endDate);
            } else if (action.equals("Bulk untag")) {
                locators.nth(1).click();
                type(toChecklistDate, startDate);
                type(fromChecklistDate, endDate);
            }
            clickConfirm();
            String text = getText(processInitiated1);
            return text;
        } catch (Exception e) {
            log("Error : An Error occurred in : [" + action + "]");
        }
        return null;
    }


    public void performCommonActions(List<String> checkList) throws InterruptedException {
        waitFor(2);
        page.waitForSelector(activeStatusCrossBtn);
        click(activeStatusCrossBtn);
        log("Clicked on the [Active Status Cross Button]");
        click(searchBtn);
        log("Clicked on the [Search] Button");

        if (!isLocatorPresent(page, getEmptySearchBarChecklist, 3)) {
            click(searchCrossIcon);
            log("Clicked on the [Cross] icon");
        }
        for (String name : checkList) {
            fill(checklistSearchBar, name);
            page.keyboard().press("Enter");
            log("Entered the checkList [" + name + "] in Searchbar and Pressed 'Enter Key'");
            waitFor(1);

        }
    }


    public List<String> getCheckListNames() {
        if (isLocatorPresent(page, activeStatusCrossBtn, 2)) {
            click(activeStatusCrossBtn);
        }
        waitFor(2);
        List<String> momentNameList = new ArrayList<>();
        waitFor(2);
        for (int i = 1; i < 2; i++) {
            String momentName = page.locator("//tbody/tr[" + i + "]/td[1]").innerText();
            waitFor(1);
            if (momentName.startsWith("Test Checklist")) {
                momentNameList.add(momentName);
            }
        }
        return momentNameList;
    }

    public void updateExistingMoment(String value, String weightage) {
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
            click(defaultCallFilterCross.replace("txt", "active"));
            log("Clicked on [Active] cross button");
        }
        waitFor(2);
        page.waitForSelector(checklistNameList);
        Locator checklists = page.locator(checklistNameList);
        int counts = checklists.count();

        for (int i = 0; i < counts; i++) {
            String nameTxt = checklists.nth(i).innerText();
            if (nameTxt.startsWith("Test Checklist")) {
                waitFor(1);
                click("//tbody/tr[" + (i + 1) + "]/td[10]//button");

                click(editChecklistBtn);
                log("Clicked on the Checklist [Accordion] and [Edit] Button");
                waitFor(2);

                click(headerLoc.replace("txt", value));
                log("Clicked on the [" + value + "] from Header");

                fill(checklistWeight, weightage);
                log("Entered the [" + weightage + "] ");
            }
            waitFor(2);
        }
    }


    public void searchSelectAddMoments(List<String> moments, int momentCount) {
        int count = 0;
        for (String value : moments) {
            if (count >= momentCount) {
                break; // Exit the loop if we've processed 5 elements
            }
            fill(searchMoment, value);
            click(momentListLoc);
            fill(searchMoment, "");
            log("Entered, Clicked, and cleared [" + value + "]");
        }
    }


    public void removeMomentsFromChecklist(String action) {
        try {
            int initialCount = page.locator(edit3DotBtn).count();
            int iterations = Math.max(initialCount - 5, 0);
            for (int i = 0; i < iterations; i++) {
                click(edit3DotFirst);
                waitFor(1);
                List<ElementHandle> actionList = page.querySelectorAll(actList);
                for (ElementHandle eleAction : actionList) {
                    if (eleAction.innerText().equals(action)) {
                        eleAction.click();
                        log("Clicked on the [Delete] Button");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log("Error : An error occurred while deleting the moments");
        }
    }


    public int markAsMomentsOptional(String action) {
        int count = 0;
        waitFor(2);
        int initialCount = page.locator(edit3DotBtn).count();
        for (int i = 0; i < initialCount; i += 3) {
            Locator locator = page.locator(edit3DotBtn);
            locator.nth(i).click();
            waitFor(1);
            List<ElementHandle> actionList = page.querySelectorAll(actList);
            for (ElementHandle eleAction : actionList) {
                if (eleAction.innerText().equals(action)) {
                    count++;
                    eleAction.click();
                    log("Clicked on the [" + action + "] Button");
                    break;
                }
            }
            waitFor(2);
        }
        return count;
    }

    public void createMomentGroup(String action, List<String> momentList, int groupCount) {
        try {
            waitFor(2);
            Locator edit3DotLocator = page.locator(edit3DotBtn);
            int initialCount = edit3DotLocator.count();

            for (int i = 0; i < initialCount; i += 3) {
                edit3DotLocator.nth(i).click();
                waitFor(1);
                List<ElementHandle> actionList = page.querySelectorAll(actList);
                for (ElementHandle eleAction : actionList) {
                    if (eleAction.innerText().equals(action)) {
                        eleAction.click();
                        waitFor(1);
                        log("Clicked on the [" + action + "] Button");
                        int count = 0; // Reset count for each action
                        for (String moment : momentList) {
                            fill(searchMoment, moment);
                            waitFor(1);
                            click(momentListLoc);
                            fill(searchMoment, "");
                            log("Entered, Clicked, and cleared [" + moment + "]");
                            count++;
                            if (count >= groupCount) {
                                break;
                            }
                        }
                        clickFinish();
                        break;
                    }
                }
                waitFor(2);
            }
            clickNext();
            clickUpdateCheckList();
            clickFinish();
        } catch (Exception e) {
            log("Error : An error occurred while deleting the moments");
        }
    }

    public int getOptionalMomentCounts() {
        waitFor(2);
        int count = 0;
        if (isLocatorPresent(page, optionalMomentLoc, 2)) {
            Locator loc = page.locator(optionalMomentLoc);
            count = loc.count();
        }
        return count;
    }

    public List<String> getMoments(String type) {
        List<String> momentslist = new ArrayList<>();
        click(radioBtn.replace("txt", type));
        log("Clicked on the [" + type + "] Radio button");

        waitFor(2);
        page.waitForSelector(momentListLoc);

        Locator loc = page.locator(momentListLoc);
        for (int i = 0; i < loc.count(); i++) {
            String text = loc.nth(i).innerText();
            momentslist.add(text);
        }
        return momentslist;
    }

    public void clicksNextUpdated() {
        try {
            clickNext();
            waitFor(3);
            clickNext();
            waitFor(3);
            click(updateChecklistBtn);
            waitFor(2);
            clickFinish();
        } catch (Exception e) {
            log("Error : An error occurred" + e.getMessage());
        }
    }


    public void searchDropDowns(List<String> campaignList, List<String> callingModes, List<String> processNames, List<String> callerTypes) {
        page.reload();
        waitFor(3);
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "active"), 5)) {
            click(defaultCallFilterCross.replace("txt", "active"));
        }
        page.waitForSelector(checklistNameList);
        Locator checklists = page.locator(checklistNameList);

        int counts = checklists.count();
        log("Total checklists found: " + counts);
        int count = 0;
        for (int i = 0; i < counts; i++) {
            checklists.nth(i).scrollIntoViewIfNeeded();
            String nameTxt = checklists.nth(i).innerText();
            if (nameTxt.startsWith("Test Checklist")) {

                waitFor(1);
                click("//tbody/tr[" + (i + 1) + "]/td[10]//button");
                click(editChecklistBtn);
                log("Clicked on the Checklist [Accordion] and [Edit] Button");

                waitFor(2);
                clickWithForce(accordianMetaData + "[3]");
                waitFor(2);

                selectDropdownValues(campaignList, 3);
                if (isDropdownEnabled("Select Mode of calling")) {
                    log("Dropdown 'Select Mode of calling' is enabled");
                    selectDropdown("Select Mode of calling", callingModes, 2);
                }
                if (isDropdownEnabled("Select Process Name")) {
                    log("Dropdown 'Select Process Name' is enabled");
                    selectDropdown("Select Process Name", processNames, 2);
                }
                if (isDropdownEnabled("Select Caller Type")) {
                    log("Dropdown [Select Caller Type] is enabled");
                    selectDropdown("Select Caller Type", callerTypes, 2);
                }
                waitFor(2);
                click(modeOfCallingTxt);
                clicksNextUpdated();
            }
            count++;
            if (count == 1) {
                break;
            }
        }
    }

    private void selectDropdownValues(List<String> values, int limit) {
        waitFor(2);
        int count = 0;
        for (String value : values) {
            clickWithForce(drpLists.replace("txt", value));
            log("clicked on [" + value + "]");
            waitFor(1);
            count++;
            if (count == limit) {
                log("Reached limit of " + limit + " clicks for values.");
                break;
            }
            waitFor(2);
        }
    }

    private boolean isDropdownEnabled(String dropdownText) {
        boolean enabled = page.isEnabled(selectDrpText.replace("txt", dropdownText));
        log("Dropdown [" + dropdownText + "] enabled: " + enabled);
        return enabled;
    }

    private void selectDropdown(String dropdownText, List<String> values, int limit) {
        waitFor(2);
        page.waitForSelector(selectDrpText.replace("txt", dropdownText));
        click(selectDrpText.replace("txt", dropdownText));
        log("Clicked on dropdown: " + dropdownText);

        waitFor(2);
        selectDropdownValues(values, limit);
    }


    public void clickFinish() {
        waitFor(1);
        try {
            clickWithForce(finishBtn);
            log("Clicked on [Finish] button");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    public void clickConfirm() {
        waitFor(1);
        try {
            clickWithForce(confirmBtn);
            log("Clicked on [Confirm] button");
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    public void clickNext() {
        if (isLocatorPresent(page, modeOfCallingTxt, 1)) {
            click(modeOfCallingTxt);
        }
        try {
            waitFor(1);
            clickWithForce(NextBtn);
            log("Clicked on [next] button");
        } catch (Exception e) {
            log("Error : " + e.getMessage());
        }
    }

    public void clickSubmit() {
        try {
            waitFor(1);
            clickWithForce(submitBtn);
            log("Clicked on [Submit] button");
        } catch (Exception e) {
            log("Error : " + e.getMessage());
        }
    }

    public void clickUpdateCheckList() {
        try {
            waitFor(1);
            clickWithForce(updateChecklistBtn);
            log("Clicked on [update Checklist] button");
        } catch (Exception e) {
            log("Error : " + e.getMessage());
        }
    }

    public List<String> getMetaDataValues(String filerName) {
        List<String> listData = new ArrayList<>();
        if (isLocatorPresent(page, createChecklistBtn, 5)) {
            click(createChecklistBtn);
            waitFor(2);
        }
        try {
            click(metaDataFilters.replace("txt", filerName));
            page.waitForSelector(filterValuesTxt);
            Locator elements = page.locator(filterValuesTxt);
            waitFor(2);
            for (int i = 0; i < elements.count(); i += 3) {
                String value = elements.nth(i).innerText();
                listData.add(value);
                waitFor(1);
            }
            clickWithForce(checkListSearchCross);
            return listData;
        } catch (Exception e) {
            log("Error : An Error Occurred while extracting " + filerName + "data");
        }
        return null;
    }

    public String getDefaultAppliedFilters() {
        try {
            String str = getText(defaultChecklistAppliedFilter);
            String[] a = str.split(":");
            String status = a[1].trim();
            return status;
        } catch (Exception e) {
            log("Error : Exception Occurred While extracting default applied filters");
        }
        return null;
    }

    public List<String> getMetaDataDropdownValues(String filterName, String selectValue, int count) {
        List<String> campaignNames = new ArrayList<>();
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
                campaignNames.add(eleText);
                eleCount++;
            }
            waitFor(1);
            click(filterNameBtn.replace("txt", filterName));
            waitFor(2);
            click(submitBtn);
            return campaignNames;
        } catch (Exception e) {
            log("Error : Exception Occurred While extracting campaign names");
        }
        return null;
    }


    public List<String> checklistMetaDataFilters(String filterName, String selectValue, List<String> dropdownList, int count) {
        List<String> filterDataList = new ArrayList<>(); // Initialize outside the loop
        try {
            for (String value : dropdownList) {
                if (isLocatorPresent(page, mainFilterBtn, 2)) {
                    page.click(mainFilterBtn);
                }
                if (isLocatorPresent(page, filterNameBtn.replace("txt", filterName), 2)) {
                    click(filterNameBtn.replace("txt", filterName));
                    click(drpTxt.replace("txt", selectValue));
                }
                page.fill(searchFilters, value);
                click(drpEleText.replace("txt", value));

                click(filterNameBtn.replace("txt", filterName));
                click(submitBtn);

                if (filterName.equals("Campaign Name")) {
                    List<String> campaignNames = getCampingNames(value);
                    if (campaignNames != null) {
                        filterDataList.addAll(campaignNames);
                    }
                } else if (filterName.equals("Mode of calling")) {
                    List<String> modeOfCalling = getModeOfCalling(value);
                    if (modeOfCalling != null) {
                        filterDataList.addAll(modeOfCalling);
                    }
                } else if (filterName.equals("Process Name")) {
                    List<String> processNameList = getProcessNames(value);
                    if (processNameList != null) {
                        filterDataList.addAll(processNameList);
                    }
                }
            }
        } catch (Exception e) {
            log("Error: An Exception Occurred: " + e.getMessage());
        }
        return filterDataList;
    }


    public List<String> getProcessNames(String value) {
        List<String> colData = new ArrayList<>();
        try {
            waitFor(2);
            if (isLocatorPresent(page, checklistProcessNameList, 2)) {
                click(checklistProcessNameList);
                log("Clicked on the [Process Name] Column data");

                String str = getText(checklistColsDataList);
                String[] campaignNames = str.split(", ");
                for (String campaignName : campaignNames) {
                    if (campaignName.trim().equals(value)) {
                        colData.add(campaignName.trim());
                    }
                }
            }
            click(defaultCallFilterCross.replace("txt", value));
            log("Clicked on [" + value + "] cross icon");
        } catch (Exception e) {
            log("Error in getCampingNames for value [" + value + "]: " + e.getMessage());
            return null; // Return null if there is an error
        }
        return colData;
    }

    public List<String> getCampingNames(String value) {
        List<String> colData = new ArrayList<>();
        try {
            waitFor(2);
            click(checklistCampaignNameList);
            log("Clicked on the [CampaignName] Column data");

            String str = getText(checklistColsDataList);
            String[] campaignNames = str.split(", ");
            for (String campaignName : campaignNames) {
                if (campaignName.trim().equals(value)) {
                    colData.add(campaignName.trim());
                }
            }
            click(defaultCallFilterCross.replace("txt", value));
            log("Clicked on [" + value + "] cross icon");
        } catch (Exception e) {
            log("Error in getCampingNames for value [" + value + "]: " + e.getMessage());
            return null; // Return null if there is an error
        }
        return colData;
    }

    public List<String> getModeOfCalling(String value) {
        List<String> colData = new ArrayList<>();
        try {
            waitFor(2);
            click(checklistModeOfCallingList);
            log("Clicked on the [CampaignName] Column data");

            String str = getText(checklistColsDataList);
            String[] campaignNames = str.split(", ");
            for (String campaignName : campaignNames) {
                if (campaignName.trim().equals(value)) {
                    colData.add(campaignName.trim());
                }
            }
            click(defaultCallFilterCross.replace("txt", value));
            log("Clicked on [" + value + "] cross icon");
        } catch (Exception e) {
            log("Error in getCampingNames for value [" + value + "]: " + e.getMessage());
            return null; // Return null if there is an error
        }
        return colData;
    }

    public List<String> searchChecklist(List<String> checklistNames) {
        List<String> searchedChecklist = new ArrayList<String>();
        click(searchBtn);
        for (String name : checklistNames) {
            fill(checkListSearch, name);
            page.keyboard().press("Enter");
            log("Enter the checklist [" + name + "]in searchBar ");

            String checklist = getText("(//tbody/tr/td[1]//div[@aria-label='" + name + "'])[1]");
            searchedChecklist.add(checklist);

            click(defaultCallFilterCross.replace("txt", name));
            log("Click on checklist clear icon");
        }
        return searchedChecklist;
    }

    public List<String> getChecklistNames(int count) {
        List<String> checklistNameList = new ArrayList<>();
        waitFor(2);
        Locator locator = page.locator(firstPageChecklistNames);
        for (int i = 0; i < count; i++) {
            String name = locator.nth(i).innerText();
            checklistNameList.add(name);
            waitFor(1);
        }
        return checklistNameList;
    }

    public List<String> getTeaNames() {
        List<String> teams = new ArrayList<String>();
        click(defaultCallFilterCross.replace("txt", "active"));
        waitFor(1);
        click(selectTeamDrp);
        log("Clicked on the[ Select Team ]dropDown");
        waitFor(2);
        List<ElementHandle> teamNames = page.querySelectorAll(ListDrp);
        for (ElementHandle team : teamNames) {
            String text = team.innerText();
            teams.add(text);
        }
        click(selectTeamDrp);
        log("click on the[Select Team]");
        return teams;
    }

    public List<String> searchValidate(List<String> teams) {
        List<String> actualTeams = new ArrayList<>();
        for (String teamName : teams) {
            click(selectTeamDrp);
            log("Clicked on [Select team] Dropdown");
            fill(teamSearchbar, teamName);

            log("Entered [" + teamName + " team] in searchbar");
            click(searchResults.replace("txt", teamName));

            click(clearResultsBtn);
            log("Clicked on [Checklist Accordian]");

            click(editChecklistBtn);
            log("Clicked on [Edit Checklist] button");
            waitFor(2);
            List<ElementHandle> list = page.querySelectorAll(selectedTeamList);
            for (ElementHandle elementHandle : list) {
                String a = elementHandle.innerText();
                if (teamName.equals(a)) {
                    if (!actualTeams.contains(a)) {
                        actualTeams.add(teamName);
                    }
                }
            }
            click(closeCheckList);
            log("Closed edit Checklist Page");

            page.reload();
            waitFor(1);
        }
        return actualTeams;
    }

    public List<Integer> searchValidate1(List<String> teams) {
        List<Integer> actualTeams = new ArrayList<>();
        click(selectTeamDrp);
        log("Clicked on [ Select team ] Dropdown");
        int count = 0;
        for (String teamName : teams) {
            fill(teamSearchbar, teamName);
            click(searchResults.replace("txt", teamName));
            waitFor(2);

            Locator searchTeamSResults = page.locator(firstPageChecklistNames);
            actualTeams.add(searchTeamSResults.count());
            click(selectAll);
            click(crossTeamIcon);

            count++;
            if (count == 5) {
                break;
            }
        }
        return actualTeams;
    }


    // ------------------------------------------------------------------------------------------------
    public String checklistTagging(String startDate, String endDate) {
        String taggedText = "";
        LocalDate parsedStartDate = LocalDate.parse(startDate.substring(0, 8), DateTimeFormatter.ofPattern("ddMMyyyy"));
        LocalDate parsedEndDate = LocalDate.parse(endDate.substring(0, 8), DateTimeFormatter.ofPattern("ddMMyyyy"));

        while (true) {
            log("Entering from Checklist Tagging Date :" + startDate);
            type(tagChecklistDateFrom, startDate);

            log("Entering from Bulk Tagging To :" + endDate);
            type(tagChecklistDateTo, endDate);

            if (isLocatorPresent(page, totalCallToBeAffectedChecklist, 2)) {
                int call = Integer.parseInt(page.innerText(totalCallToBeAffectedChecklist));
                if (call == 0) {
                    parsedStartDate = parsedStartDate.minusDays(1);
                    startDate = parsedStartDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + startDate.substring(8);
                    endDate = parsedEndDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + endDate.substring(8);
                } else {
                    break; // Exit the loop if call is not 0
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
                    parsedStartDate = parsedStartDate.plusDays(1);
                    startDate = parsedStartDate.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + startDate.substring(8);
                } else {
                    break;
                }
            }
            break;
        }
        click(submitBtn);
        log("Clicked on [Submit] Button");

        taggedText = getText(checklistTaggedText);
        return taggedText;
    }


    public List<String> getMoments(String waightage, String momentType) {
        List<String> momentNameList = new ArrayList<>();
        waitFor(3);

        fill(checklistWeight, waightage);
        click(radioBtn.replace("txt", momentType));
        log("Entered Weightage ["+waightage+"] and Clicked on [" + momentType +"]Radio Button");

        waitFor(3);
        page.waitForSelector(moments);
        List<ElementHandle> momentList = page.querySelectorAll(moments);
        for (ElementHandle moment : momentList) {
            moment.scrollIntoViewIfNeeded();
            String momentName = moment.innerText();
            momentNameList.add(momentName);
        }
        return momentNameList;
    }

    public void addMoments(List<String> momentNames) {
        page.waitForSelector(searchMoment);
        int count = 0;
        for (String moment : momentNames) {
            fill(searchMoment, moment);
            Locator loc = page.locator(searchResult);
            loc.nth(0).click();
            count++;
            if (count == 5) {
                break;
            }
        }
        clickNext();
        page.waitForSelector(createChecklist);
        click(createChecklist);
    }

    public void createCheckList(String name, String label) {
        click(createChecklistBtn);
        log("Clicked on Create [Create Checklist] Button");
        try {
            fill(checklistNameTxt, name);
            log("Entered Moment Name [" + name + "] in text Field");

            click(FilterBtn.replace("txt", label));
            log("Clicking on [" + label + "] Button");

            page.waitForSelector(selectConversationType);
            click(selectConversationType);
            log("Clicked on [Select Conversation] Element ");

            click(conversationTypeDrp);
            log("Clicked on [All] DropDown Options");

        } catch (Exception e) {
            log("Error : An Error occurred while Entering [checklist] Details");
        }
    }

    public List<String> getCallFilteredDataList(String selectText) {
        List<String> List = new ArrayList<>();
        if (isLocatorPresent(page, selectChecklistType, 2)) {
            click(selectChecklistType);
            click(callLevalBtn);
            log("Clicked on [Select Checklist Type] and [Call Level] Dropdown'");
        }
        boolean enabledSelect = page.isEnabled(selectDrpText.replace("txt", selectText));
        if (enabledSelect) {
            click(selectDrpText.replace("txt", selectText));
            log("Clicked on [" + selectText + "] Dropdown'");
            waitFor(3);
            List<ElementHandle> drpList = page.querySelectorAll(drpValues);
            for (ElementHandle value : drpList) {
                value.scrollIntoViewIfNeeded();
                String name = value.innerText();
                List.add(name);
            }
        }
        return List;
    }


    public List<String> getTeamList() {
        try {
            List<String> teamList = new ArrayList<>();
            click(selectChecklistType);
            click(callLevalBtn);
            click(selectYourTeamDrp);
            waitFor(3);
            List<ElementHandle> teams = page.querySelectorAll(drpValues);
            for (ElementHandle team : teams) {
                team.scrollIntoViewIfNeeded();
                String name = team.innerText();
                teamList.add(name);
            }
            return teamList;
        } catch (Exception e) {
            System.err.println("Error :An Error Occurred While extracting Teams");
        }
        return null;
    }

    public void selectFilterScope(List<String> teamNames) {
        try {
            waitFor(2);
            List<ElementHandle> teams = page.querySelectorAll(teamList);
            int count = 0;
            for (int i = 0; i < teams.size(); i++) {
                String name = teams.get(i).innerText();
                teams.get(i).click();
                log("Selecting [" + name + "] Team");
                count++;
                if (count == 5) {
                    break;
                }
            }
            click(callerType);
            log("Clicking on 'Select Caller Type' Dropdown'");
            waitFor(1);

            click(selectAllCallerType);
            log("Clicking on [Select All]");

            clickNext();
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
    }

    /* catch (Exception e) {
                log("Error : An Error occurred while Selecting Team Filters : " + e.getMessage());
            }

        } else if (scope.equals("Filter")) {
            try {
                log("Clicking on '" + scope + "'Button");
                click(filterBtn.replace("type", scope));

                click(callFilterDrp.replace("txt", "Select Campaign Name"));
                page.waitForSelector(searchFilters);

                for (String option : campaignNames) {
                    fill(searchFilters, option);
                    log("Entering in Searchbar'" + option + "'in text Field");
                    page.waitForSelector(labelSearch.replace("txt", option));
                    waitFor(1);
                    click(labelSearch.replace("txt", option));
                    log("Clicked the on campaignName : '" + option);

                    waitFor(1);
                    click(searchCross);
                    log("Cleared the ProcessName : '" + option);
                }
            } catch (Exception e) {
                log("Error : An Error occurred while Selecting 'CampaignName Filter' : " + e.getMessage());
            }
            click(callFilterDrp.replace("txt", "Select Mode of calling"));
            log("Clicked on 'Select Mode of calling' Dropdown");
            waitFor(2);
            log("Clicking on 'Select All'");
            click(selectAllCallerType);
            waitFor(2);
            click(callFilterDrp.replace("txt", "Select Process Name"));
            page.waitForSelector(processNameList);
            try {
                for (String option : processNames) {
                    fill(searchFilters, option);
                    log("Entering in Searchbar'" + option + "'in text Field");

                    page.waitForSelector(labelSearch.replace("txt", option));
                    waitFor(1);
                    click(labelSearch.replace("txt", option));
                    log("Clicked the on ProcessName : '" + option);

                    waitFor(1);
                    click(searchCross);
                    log("Cleared the ProcessName : '" + option);
                }
            } catch (Exception e) {
                log("Error : An Error occurred while Selecting 'processName Filter' : " + e.getMessage());
            }
            click(callFilterDrp.replace("txt", "Select Caller Type"));
            log("Clicking on 'Select Mode of calling' Dropdown");
            waitFor(2);
            click(selectAllCallerType);
            log("Clicking on 'Select All'");
            click("//p[.='Campaign Name *']");

            click(NextBtn);
        }*/


    public void login(String email, String password) {
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
}