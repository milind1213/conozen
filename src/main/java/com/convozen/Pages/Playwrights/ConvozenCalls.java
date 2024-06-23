package com.convozen.Pages.Playwrights;

import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.convozen.Pages.Playwrights.Locators.*;

public class ConvozenCalls extends CommonPlaywright {
    Page page;

    public ConvozenCalls(Page page) {
        super(page);
        this.page =page;
    }

    public void actionOnCallRecording() throws InterruptedException {
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        page.waitForSelector(audioBtns);
        Locator loc = page.locator(audioBtns);

        for (int i = 0; i < 1; i++) {
            Locator audioLocator = loc.nth(i);
            Page popup = page.waitForPopup(() -> {
                audioLocator.scrollIntoViewIfNeeded();
                audioLocator.click();
            });
            handleAudioPlayback(popup);
            log("Verified [Audio] Button functionality");

            handleAudioMuteUnmute(popup);
            log("Verified Audio [Mute/UnMute] functionality");

            changePlaybackSpeed(popup);
            log("Verified Playback Speed functionality");

            String callRecordId = getCallRecordId(popup);
            log("Call Record ID: " + callRecordId);

            downloadAudio(popup, callRecordId);
            log("Verified the [Audio File] Download functionality");
        }
    }

    private void handleAudioPlayback(Page popup) throws InterruptedException {
        boolean isAudioEnabled = popup.locator(audioPlayButton).isEnabled();
        log("Audio Playback is Enabled: " + isAudioEnabled);

        popup.locator(audioPlayButton).click();
        log("Clicked on [Play] Button");
        Thread.sleep(5000);
    }

    private void handleAudioMuteUnmute(Page popup) {
        boolean isSpeakerEnabled = popup.locator(speakerBtn).isEnabled();
        log("Speaker is  enabled: " + isSpeakerEnabled);

        popup.locator(speakerBtn).click();
        log("Clicked on Audio [mute/unmute]");
    }

    private void changePlaybackSpeed(Page popup) {
        popup.locator(speed).click();
        popup.locator(speedList).nth(1).click();
        log("Changed playback speed");
    }

    private String getCallRecordId(Page popup) {
        String txt = popup.locator(cdrId).innerText();
        return txt.substring(4);
    }

    private void downloadAudio(Page popup, String callRecordId) throws InterruptedException {
        popup.locator(downloadBtn).click();
        log("Clicked on Download Button");

        waitFor(2);
        popup.reload();

        waitFor(5);
        Path downloadedFilePath = Path.of("/home/milind/Downloads/" + callRecordId + ".mp3");
        System.out.println("Downloading file: " + downloadedFilePath);
        if (Files.exists(downloadedFilePath)) {
           System. out.println("File downloaded successfully.");
        } else {
            System.out.println("File download failed or not completed yet.");
        }
    }



    public boolean generateCallSummary(String columnName) {
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        page.waitForSelector(audioBtns);
        Locator loc = page.locator(audioBtns);
        for (int i = 0; i < 1; i++) {
            Locator audioLocator = loc.nth(i);
            Page popup = page.waitForPopup(() -> {
                audioLocator.scrollIntoViewIfNeeded();
                audioLocator.click();
            });
            String columnLocator = callOverviewColumns.replace("txt", columnName);
            popup.locator(columnLocator).click();
            popup.waitForSelector(generateSummaryBtn);
            popup.locator(generateSummaryBtn).click();
            waitFor(1);
            if (isLocatorPresent(popup, summarySuccess, 3)) {
                log("Successfully to Generated Call Summary");
                return true;
            } else {
                log("Error : Failed To generated summary");
                return false;
            }
        }
        return false;
    }


    public List<Boolean> generateIntractionHistoryCallSummary(String columnName) {
        List<Boolean> result = new ArrayList<Boolean>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
            click(sec30Cross);
            log("Clicked on[ Today] and [ 3o Sec] default filter");
        }
        page.waitForSelector(audioBtns);
        Locator loc = page.locator(audioBtns);
        for (int i = 0; i < 1; i++) {
            Locator audioLocator = loc.nth(i);
            Page popup = page.waitForPopup(() -> {
                audioLocator.scrollIntoViewIfNeeded();
                audioLocator.click();
                log("Clicked on the [Audion Button]");
            });
            String columnLocator = callOverviewColumns.replace("txt", columnName);
            popup.locator(columnLocator).click();
            log("Clicked on the ["+columnName+"]");
            waitFor(2);
            popup.waitForSelector(generateSummaryBtn);
            Locator locator = popup.locator(generateSummaryBtn);
            System.out.println("Count : "+locator.count());
            for (int j = 0; j < locator.count(); j++) {
                locator.nth(j).click();
                log("Clicked on the [ Generate Summary Button ("+j+1+")]");
                if (isLocatorPresent(popup, summarySuccess, 5)) {
                    result.add(true);
                }
                waitFor(2);
                locator = popup.locator(generateSummaryBtn);
            }
        }
        return result;
    }


    public List<String> getMomentsFromCallTable() {
        List<String> momentList = new ArrayList<>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        waitFor(2);
        for (int i = 1; i <2; i++) {
            click(positiveTaggedMoment + "[" + i + "]");
            Locator loc = page.locator(momentsTagged);
            for (int j = 0; j < loc.count(); j++) {
                String positive = loc.nth(j).innerText().trim();
                momentList.add(positive);
            }
            waitFor(1);
            click(neutralTaggedMoment + "[" + i + "]");
            if (isLocatorPresent(page, momentsTagged, 2)) {
                Locator neutralLoc = page.locator(momentsTagged);
                for (int j = 0; j < neutralLoc.count(); j++) {
                    String neutral = neutralLoc.nth(j).innerText().trim();
                    momentList.add(neutral);
                }
            }
            click(negativeTaggedMoment + "[" + i + "]");
            if (isLocatorPresent(page, momentsTagged, 2)) {
                Locator negativeLoc = page.locator(momentsTagged);
                for (int j = 0; j < negativeLoc.count(); j++) {
                    String negative = negativeLoc.nth(j).innerText().trim();
                    momentList.add(negative);
                }
            }
        }
        return momentList;
    }


    public List<String> getTaggedMomentCount() {
        List<String> momentList = new ArrayList<String>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        waitFor(2);
        page.waitForSelector(audioBtns);
        Locator loc = page.locator(audioBtns);
        for (int i = 0; i <1; i++) {
            Locator audioLocator = loc.nth(i);
            Page popup = page.waitForPopup(() -> {
                audioLocator.scrollIntoViewIfNeeded();
                audioLocator.click();
            });
            popup.locator(callOverviewColumns.replace("txt", "Moments")).click();
            waitFor(2);
            Locator locators = popup.locator(momentTaggedInCall);
            for (int j = 0; j < locators.count(); j++) {
                String str = locators.nth(j).innerText();
                if (!momentList.contains(str)) {
                    momentList.add(str);
                }
            }
            popup.close();
            waitFor(2);
        }
        return momentList;
    }

    public List<String> getCallTableChecklists() {
        waitFor(2);
        List<String> checkList = new ArrayList<>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        page.waitForSelector(callScoreList);
        waitFor(2);
        Locator locator = page.locator(callScoreList);
        for (int i = 0; i <1; i++) {
            locator.nth(i).scrollIntoViewIfNeeded();
            locator.nth(i).click();
            waitFor(1);
            Locator checklist = page.locator(taggedChecklistTable);
            for (int j = 0; j < checklist.count(); j++) {
                String checklistText = checklist.nth(j).innerText().trim();
                checkList.add(checklistText);
                waitFor(1);
            }
        }
        return checkList;
    }


    public List<String> getTaggedChecklists() {
        List<String> checklists = new ArrayList<String>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        page.waitForSelector(audioBtns);
        Locator loc = page.locator(audioBtns);
        for (int i = 0; i <1; i++) {
            Locator audioLocator = loc.nth(i);
            Page popup = page.waitForPopup(() -> {
                audioLocator.scrollIntoViewIfNeeded();
                audioLocator.click();
            });
            waitFor(2);
            String numText = popup.locator(checklistNumberInCall).innerText();
            int num = Integer.parseInt(numText);
            if (num != 0) {
                Locator checklistLocators = popup.locator(taggedChecklistListCall);
                for (int j = 0; j < checklistLocators.count(); j++) {
                    String text = checklistLocators.nth(j).innerText();
                    checklists.add(text);
                }
            }
            popup.close();
            click(reloadBtn);
            waitFor(2);
        }
        return checklists;
    }


    public List<Integer> getTableCallScore(int calls) {

        List<Integer> scoreList = new ArrayList<Integer>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
        }
        click(scoreDescendingOrder);
        waitFor(2);
        page.waitForSelector(callScoreList);
        Locator locator = page.locator(callScoreList);
        for (int i = 0; i <calls; i++) {
            String score = locator.nth(i).innerText();
            int callScore = Integer.parseInt(score);
            scoreList.add(callScore);
        }
        return scoreList;
    }

    public List<Integer> getChecklistScore(int calls) {
        List<Integer> scoreList = new ArrayList<Integer>();
        page.waitForSelector(callScoreList);
        Locator locator = page.locator(callScoreList);
        for (int i = 0; i <calls; i++) {
            locator.nth(i).scrollIntoViewIfNeeded();
            locator.nth(i).click();
            waitFor(2);
            Locator checklist = page.locator(taggedChecklistScore);
            int chkCount = checklist.count();
            int sum = 0;
            int count = 0;
            for (int j = 0; j < checklist.count(); j++) {
                String checklistScoreText = checklist.nth(j).innerText().replaceAll("[^0-9]", "").trim();
                int checklistScore = Integer.parseInt(checklistScoreText);
                sum = sum + checklistScore;
                count++;
            }
            if (chkCount != 0) {
                int actualPercentage = sum / count;
                scoreList.add(actualPercentage);
            } else {
                int actualPercentage = sum / 1;
                scoreList.add(actualPercentage);
            }
        }
        return scoreList;
    }

    public List<Integer> getTableMomentEmotions() {
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
            log("Clicked on the [Today] cross Icon");
        }
        waitFor(2);
        page.waitForSelector(momentsEmotion);
        List<Integer> moments = new ArrayList<>();
        Locator locator = page.locator(momentsEmotion);
        int count = 0;
        for (int i = 0; i <1; i++) {
            String txt = locator.nth(i).innerText();
            if (!txt.equals("In progress")) {
                String[] b = txt.split("\\n");
                int sum = 0;
                for (int j = 0; j < b.length; j += 2) {
                    if (isNumeric(b[j])) {
                        sum += Integer.parseInt(b[j]);
                    }
                }
                moments.add(sum);
            }
            count++;
            if (count ==10) {
                break;
            }
        }
        return moments;
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }


    public List<Integer> getCallMomentsEmotions() {
        List<Integer> moments = new ArrayList<>();
        page.waitForSelector(audioBtns);
        Locator loc = page.locator(audioBtns);
        for (int i = 0; i < loc.count() && i <1; i++) {
            Locator audioLocator = loc.nth(i);
            Page popup = page.waitForPopup(() -> {
                audioLocator.scrollIntoViewIfNeeded();
                audioLocator.click();
                waitFor(1);
            });
            String txt = popup.locator(momentsEmotion1).innerText().replaceAll("[^0-9]", "").trim();
            int momentCount = Integer.parseInt(txt);
            if (momentCount != 0) {
                moments.add(momentCount);
            }
            popup.close();
            click(reloadBtn);
            waitFor(2);
        }
        return moments;
    }

    public String getDefaultAppliedFilters() {
        StringBuilder sb = new StringBuilder(0);
        try {
            waitFor(1);
            Locator locator = page.locator(defaultChecklistAppliedFilter);
            for (int i = 0; i < locator.count(); i++) {
                String str = locator.nth(i).innerText();
                sb.append(str).append(",");
            }
            return sb.toString().strip().replace("\n", "");
        } catch (Exception e) {
            log("Error : Exception Occurred While extracting default applied filters");
        }
        return null;
    }

    public List<String> callsMetaDataFilters(String filterName, String selectValue, List<String> dropdownList, int count) {
        try {
            List<String> colData = new ArrayList<>();
            for (String value : dropdownList) {
                if (isLocatorPresent(page, mainFilterBtn, 2)) {
                    page.click(mainFilterBtn);
                    log("Clicked on Main Filter Button");
                }
                if (isLocatorPresent(page, filterNameBtn.replace("txt", filterName), 2)) {
                    click(filterNameBtn.replace("txt", filterName));
                    click(drpTxt.replace("txt", selectValue));
                    log("Clicked on Main Filter, FilterName [ " + filterName + " ],and  [ " + selectValue + " ] of the MetaData");
                }
                page.fill(searchFilters, value);
                click(dropELeByText.replace("txt", value));
                log("Entered and clicked [" + value + "]");

                click(filterNameBtn.replace("txt", filterName));
                click(submitBtn);
                log("Clicked on [" + filterName + "] and [Summit] button");
                waitFor(2);

                int index = 0;
                if (filterName.equals("Campaign Name")) {
                    index = 2;
                } else if (filterName.equals("Mode of calling")) {
                    index = 3;
                } else if (filterName.equals("Process Name")) {
                    index = 4;
                }
                Locator locator = page.locator(callCampaignNameList);
                int eleCount = 0;
                for (int i = index; i < locator.count(); i += 12) {
                    if (eleCount >= count) {
                        break; // Exit the loop once we have 5 campaign names
                    }
                    String txt = locator.nth(i).innerText();
                    if (!colData.contains(txt)) {
                        colData.add(txt);
                        eleCount++;
                    }
                }
                click(filterCrossText.replace("txt", value));
            }
            return colData;
        } catch (Exception e) {
            log("Error : An Exception Occurred :  " + e.getMessage());
        }
        return null;
    }

    public List<String> getMetaDataDropdownValues(String filterName, String selectValue, int count) {
        try {
            List<String> campaignNames = new ArrayList<>();
            if (isLocatorPresent(page, todayCross, 2)) {
                click(todayCross);
                log("Clicked on the 'Today Cross' Icon");
            }

            if (isLocatorPresent(page, sec30Cross, 2)) {
                click(sec30Cross);
                log("Clicked on the 'Today Cross' Icon");
            }

            click(mainFilterBtn);
            click(filterNameBtn.replace("txt", filterName));
            click(drpTxt.replace("txt", selectValue));
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

    public List<String> getCallTableChecklist(){
        List<String> callTableChecklist = new ArrayList<String>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
            log("Clicked on the 'Today Cross' Icon");
        }
        page.waitForSelector(tableChecklistList);
        waitFor(1);
        List<ElementHandle> checklists = page.querySelectorAll(tableChecklistList);
        for(ElementHandle ele : checklists) {
            ele.scrollIntoViewIfNeeded();
            String checklistText =  ele.innerText();
            System.out.println("Checklist : "+checklistText);
            if(!checklistText.equals("Not applicable")){
                callTableChecklist.add(checklistText);
            }
        }
        return callTableChecklist;
    }

    public void insightsFilters(String filterName, String selectFilter, List<String> dataList) {
        try {
            if (isLocatorPresent(page, todayCross, 2)) {
                click(todayCross);
                log("Clicked on the 'Today Cross' Icon");
            }

            click(mainFilterBtn);
            log("Clicked on the [Main Filter] Button");

            click(insightFilters1.replace("txt", filterName));
            click(filterBtn.replace("type", selectFilter));
            log("Clicked on [" + filterName + "], [" + selectFilter + "]");

            for (String value : dataList) {
                fill(searchFilters, value);
                if (filterName.equals("Language") || filterName.equals("Checklists")) {
                    click(searchLanguageBtn.replace("txt", value));
                } else {
                    click(searchResults.replace("txt", value));
                    log("Clicked [" + value + "]");
                }
                if (isLocatorPresent(page, searchCross, 2)) {
                    click(searchCross);
                    log("Searched and Clicked and Cleared the moment [" + value + "]");
                }
                if (isLocatorPresent(page, filterCrossText.replace("txt", value), 2)) {
                    click(filterCrossText.replace("txt", value));
                }
            }
            click(insightFilters1.replace("txt", filterName));
            click(submitBtn);
            log("Clicked on the [" + filterName + "] and [Submit] button");
        } catch (Exception e) {
            log("Error : An error occurred" + e.getMessage());
        }
    }

    public List<String> getTaggedChecklist() {
        List<String> taggedChecklist = new ArrayList<String>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
            log("Clicked on the [Today] cross Icon");
        }
        page.waitForSelector(taggedChecklistPercentage);
        waitFor(5);
        Locator locator = page.locator(taggedChecklistPercentage);
        for (int i = 0; i < 3; i++) {
            locator.nth(i).click();
            waitFor(1);
            List<ElementHandle> checklistElementList = page.querySelectorAll(taggedChecklistNames);
            for (ElementHandle checklistElement : checklistElementList) {
                String checklist = checklistElement.innerText();
                taggedChecklist.add(checklist);
            }
        }
        return taggedChecklist; // click(taggedChecklistPercentage+"[1]");
    }

    public List<String> getLanguages() {
        List<String> languages = new ArrayList<String>();
        try {
            page.waitForSelector(languageList);
            waitFor(2);
            List<ElementHandle> languagelist = page.querySelectorAll(languageList);
            for (ElementHandle element : languagelist) {
                element.scrollIntoViewIfNeeded();
                String text = element.innerText();
                String[] output = text.split("\n");
                languages.add(output[0]);
            }
            click(appliedFilterCross);
            return languages;
        } catch (Exception e) {
            log("Error : An error occurred" + e.getMessage());
        }
        return languages;
    }


    public List<String> getLongSilence() {
        page.waitForSelector(audioBtns);
        List<ElementHandle> audioButtons = page.querySelectorAll(audioBtns);
        for (int i = 0; i < audioButtons.size() && i < 3; i++) {
            ElementHandle button = audioButtons.get(i);
            Page popup = page.waitForPopup(() -> {
                button.click();
                log("Clicked on the [Audio]");
            });
            waitFor(2);
            popup.click(minimiseLoc);
            log("Minimising the general information");

            List<ElementHandle> numberOfSilence = popup.querySelectorAll(longSilenceList);
            List<String> silencesList = new ArrayList<String>();
            for (ElementHandle silence : numberOfSilence) {
                silence.scrollIntoViewIfNeeded();
                String text = silence.innerText();
                silencesList.add(text);
            }
            waitFor(2);
            popup.close();
            log("Closing the [Pop-Up Window]");

            click(appliedFilterCross);
            log("Clicked on the [Applied Filter Cross]");
            return silencesList;
        }
        return null;
    }

    public List<String> getTaggedMoments() {
        List<String> searchMoments = new ArrayList<>();
        try {
            waitFor(2);
            Locator locator = page.locator(momentPositiveList);
            locator.nth(0).click();
            List<ElementHandle> eleList = page.querySelectorAll(momentCountList);
            for (ElementHandle ele : eleList) {
                String text = ele.innerText();
                searchMoments.add(text);
            }
            click(appliedFilterCross);
            click(reloadBtn);
            return searchMoments;
        } catch (Exception e) {
            log("Error: An Exception Occurred : " + e.getMessage());
        }
        return null;
    }


    public String getTranscribedProcessedCalls(String filterName) {
        StringBuilder sb = new StringBuilder();
        if (isLocatorPresent(page, transcribedCross, 2)) {
            click(transcribedCross);
            log("Clicked on the [Transcribed Calls] icons");
            if (isLocatorPresent(page, todayCross, 1)) {
                click(todayCross);
                log("Clicked on the [Today] icons");
            }
            if (isLocatorPresent(page, processedCross, 2)) {
                click(processedCross);
                log("Clicked on the [Processed] icons");
            }
        }

        click(mainFilterBtn);
        log("Clicked on the [Main Filter] Button");
        try {
            if (filterName.equals("Transcription Status")) {
                click(filterNameBtn.replace("txt", filterName));
                click(text.replace("txt", "Show Transcribed Calls"));
            } else if (filterName.equals("Call Progress")) {
                click(filterNameBtn.replace("txt", filterName));
                click(text.replace("txt", "Show Processed Calls"));
            }
            click(submitBtn);
            log("Clicked [" + filterName + "] checkbox and [Submit] button");
            List<ElementHandle> list = page.querySelectorAll(crossLabelList);
            for (ElementHandle elementHandle : list) {
                String text = elementHandle.innerText();
                sb.append(text).append(",");
            }
            return sb.toString().substring(10, sb.length() - 1);
        } catch (Exception e) {
            log("Error: " + e.getMessage());
        }
        return null;
    }


    public String applyTalktimeOverlapSilenceFilter(String filterName, String condition, String from1, String to1, String from2, String to2) {
        if (isLocatorPresent(page, filterCrossText.replace("txt", "Today"), 2)) {
            click(filterCrossText.replace("txt", "Today"));
            log("Clicked on the 'Today Cross' Icon");
        }

        click(mainFilterBtn);
        log("Clicked on the 'Main Filter' Icon");
        click(filterNameBtn.replace("txt", filterName));

        String locatorConditional;
        int callDetailsDropdownIndex;
        if (filterName.equals("Talktime")) {
            callDetailsDropdownIndex = 1;
            log("Clicked on [Main Filter],[" + filterName + "] And [conditional] dropdown");
        } else if (filterName.equals("Overlap Duration")) {
            callDetailsDropdownIndex = 2;
            log("Clicked on [Main Filter],[" + filterName + "] And [conditional] dropdown");
        } else if (filterName.equals("Silence Duration")) {
            callDetailsDropdownIndex = 3;
            log("Clicked on [Main Filter],[" + filterName + "] And [conditional] dropdown");
        } else {
            throw new IllegalArgumentException("Invalid condition: " + filterName);
        }
        locatorConditional = String.format("(//*[@class='css-adsdiv-singleValue'])[%d]", callDetailsDropdownIndex);
        click(locatorConditional);

        List<ElementHandle> list = page.querySelectorAll(callTalkTimeDrpList);
        for (ElementHandle elementHandle : list) {
            String innerText = elementHandle.innerText();
            if (innerText.equals(condition)) {
                elementHandle.click();
                fillTimeInputs(filterName, condition, from1, to1, from2, to2);
                click(submitBtn);
                log(" Clicked on [" + condition + "], Entered [" + from1 + " min, " + to1 + " sec] and Clicked on [Submit]");
                waitFor(2);
                page.waitForSelector(audioBt);
                String text = getText(audioBt);
                //String result = condition.equals("Less than or equal to") ? text.substring(0, 2).trim() : text.substring(0, 5);

                if (isLocatorPresent(page, appliedOverlapCross, 2)) {
                    click(appliedOverlapCross);
                    log(" Clicked on [Cross] Button");
                }
                return text;
            }
        }
        return "";
    }

    private void fillTimeInputs(String filterName, String condition, String from1, String to1, String from2, String to2) {
        if (filterName.equals("Talktime")) {
            try {
                fill(inputMin + "[1]", from1);
                fill(inputSec + "[1]", to1);
                if (condition.equals("Less than or equal to")) {
                    click(includeZero);
                    log("Clicked on [Include '0'] Checkbox");
                } else if (condition.equals("Between")) {
                    fill(inputMin + "[2]", from2);
                    fill(inputSec + "[2]", to2);
                    log("To Time [" + from2 + " Min] and [" + to2 + " Sec] Checkbox");
                }
            } catch (Exception e) {
                log("Error : An error occurred in [TalkTime]" + e.getMessage());
            }
        } else if (filterName.equals("Overlap Duration")) {
            try {
                fill(inputMin + "[2]", from1);
                fill(inputSec + "[2]", to1);
                if (condition.equals("Between")) {
                    fill(inputMin + "[3]", from2);
                    fill(inputSec + "[3]", to2);
                    log("To Time [" + from2 + " Min] and [" + to2 + " Sec] Checkbox");
                }
            } catch (Exception e) {
                log("Error : An error occurred in [Overlap Duration]" + e.getMessage());
            }
        } else if (filterName.equals("Silence Duration")) {
            try {
                fill(inputMin + "[3]", from1);
                fill(inputSec + "[3]", to1);
                if (condition.equals("Between")) {
                    fill(inputMin + "[4]", from2);
                    fill(inputSec + "[4]", to2);
                    log("To Time [" + from2 + " Min] and [" + to2 + " Sec] Checkbox");
                }
            } catch (Exception e) {
                log("Error : An error occurred in [Between]" + e.getMessage());
            }
        }
    }

    public List<String> getCallScoreChecklist(String filterName, String condition, String callScore) {
        List<String> callCheckList = new ArrayList<String>();
        boolean clickTodayCross = isLocatorPresent(page, todayCross, 2);
        if (clickTodayCross) {
            click(todayCross);
            log("Clicked on the 'Yesterday Cross' Icon");
        }
        click(mainFilterBtn);
        click(callScoreFilterBtn.replace("txt", filterName));
        click(scoreTalkTimeDrp);
        log("Clicked on [Main Filter],[" + filterName + "] And [Call Score] dropdown");
        waitFor(2);
        page.waitForSelector(callTalkTimeDrpList);
        waitFor(3);
        Locator loc = page.locator(callTalkTimeDrpList);
        for (int i = 0; i < loc.count(); i++) {
            if (condition.equals("Greater than or equal to")) {
                loc.nth(0).click();
                fill(enterValue, callScore);
                log("Clicked on [" + condition + "]and Entered the call score [" + callScore + "]");
            }
            click(submitBtn);
            log("Clicked on [Submit] Button");
            waitFor(2);
            Locator locator = page.locator(taggedChecklistPercentage);
            for (int j = 0; j < 3; j++) {
                locator.nth(j).click();
                waitFor(1);
                List<ElementHandle> checklistElementList = page.querySelectorAll(taggedChecklistNames);
                for (ElementHandle checklistElement : checklistElementList) {
                    String checklist = checklistElement.innerText();
                    callCheckList.add(checklist);
                }
            }
            click(appliedFilterCross);
        }
        return callCheckList;
    }


    public String getCallScore(String filterName, String condition, String callScore, String endScore) {
        String percentage = "";
        boolean clickTodayCross = isLocatorPresent(page, todayCross, 2);
        if (clickTodayCross) {
            click(todayCross);
            log("Clicked on the 'Yesterday Cross' Icon");
        }
        click(mainFilterBtn);
        click(callScoreFilterBtn.replace("txt", filterName));
        click(scoreTalkTimeDrp);
        log("Clicked on [Main Filter],[" + filterName + "] And [Call Score] dropdown");
        waitFor(2);
        Locator loc = page.locator(callTalkTimeDrpList);
        for (int i = 0; i < loc.count(); i++) {
            if (condition.equals("Greater than or equal to")) {
                loc.nth(0).click();
                fill(enterValue, callScore);
                log("Clicked on [" + condition + "]and Entered the call score [" + callScore + "]");
            } else if (condition.equals("Less than or equal to")) {
                loc.nth(1).click();
                fill(enterValue, callScore);
                log("Clicked on [" + condition + "]and Entered the call score [" + callScore + "]");
                waitFor(2);
            } else if (condition.equals("Between")) {
                loc.nth(2).click();
                fill(startCallScore, callScore);
                fill(endCallScore, endScore);
                log("Clicked on [Between],Entered Start[" + callScore + "] and end [" + endScore + "] call score");
            }
            click(submitBtn);
            log("Clicked on [Submit] Button");

            Locator locator = page.locator(taggedChecklistPercentage);
            percentage = locator.nth(0).innerText();
            click(appliedFilterCross);
        }
        return percentage;
    }

    public void selectDateTime(String filterName, String start, String end) {
        click(yesterdayCross);
        click(mainFilterBtn);
        try {
            click(filterNameBtn.replace("txt", filterName));
            type(fromDate, start);
            type(toDate, end);
            log("Clicked on [" + filterName + "], Entered From [" + start + "], To  [" + end + "] Date");
            click(includeTime);
        } catch (Exception e) {
            log("Error : An error occurred while selecting date time: " + e.getMessage());
        }
    }


    public void removeTableCols(){
        click(columnFilterBtn);
        page.waitForSelector(checkedCols);
        Locator loc =page.locator(checkedCols);
        for(int i=0; i<loc.count(); i++){
            loc.nth(i).click();
        }
    }

    public List<String> getCallRecordIds() {
        waitFor(2);
        List<String> callRecordIds = new ArrayList<>();
        if (isLocatorPresent(page, todayCross, 2)) {
            click(todayCross);
            log("Clicked on the [ Yesterday Cross ] Button");
        }
        waitFor(2);
        page.waitForSelector(audioBtns);
        Locator locator = page.locator(audioBtns);
        for (int i = 0; i <3; i++) {
            Locator button = locator.nth(i);
            Page popup = page.waitForPopup(() -> {
                button.scrollIntoViewIfNeeded();
                button.click();
            });
            String txt = popup.locator(".nb__OwjWl").innerText();
            String callRecordId = txt.substring(4, txt.length());
            callRecordIds.add(callRecordId);
            popup.close();
            log("Closing the [Pop-Up Window]");
            waitFor(2);
            page.reload();
            waitFor(2);
            if (isLocatorPresent(page, todayCross, 2)) {
                click(todayCross);
                log("Clicked on the [ Yesterday Cross ] Button");
            }
        }
        return callRecordIds;
    }

    public List<String> getCallDetailsByCallId(String filterName, List<String> callIds) {
        List<String> callRecordIds = new ArrayList<>();
        for (String callId : callIds) {
            click(mainFilterBtn);
            log("Clicked on [Main Filter]");
            click(filterNameBtn.replace("txt", filterName));
            fill(inputCallId, callId);
            click(submitBtn);
            log("Clicked on [" + filterName + "] and Entered [" + callId + "] and Clicked on [Submit] button");

            Page popup = page.waitForPopup(() -> {
                click(audioBtns);
            });
            String txt = popup.locator(".nb__OwjWl").innerText();
            String callRecordId = txt.substring(4, txt.length());
            callRecordIds.add(callRecordId);

            popup.close();
            log("Closing the [Pop-Up Window]");
        }
        return callRecordIds;
    }

    public List<String> callDetailsFilters(String filterName, String inputField, List<String> callerDetails) {
        List<String> searchResultList = new ArrayList<>();
        try {
            for (String option : callerDetails) {
                click(mainFilterBtn);
                click(filterNameBtn.replace("txt", filterName));
                fill(customerIdInput.replace("txt", inputField), option);
                click(submitBtn);
                log("Clicked on [ Main Filter " + filterName + "] and Entered [" + option + "] and clicked on [Submit] button");
                String txt = searchResult(filterName);
                if(!txt.equals("-")){
                    searchResultList.add(txt);
                }
                if (isLocatorPresent(page, appliedFilterCross, 3)) {
                    click(appliedFilterCross);
                }
            }
            return searchResultList;
        } catch (Exception e) {
            log("Error: An Error occurred " + e.getMessage());
        }
        return searchResultList;
    }

    public String searchResult(String filterName) {
        waitFor(2);
        Map<String, Integer> indexMap = new HashMap<>();
        indexMap.put("Customer Id", 1);
        indexMap.put("Customer Phone", 2);
        indexMap.put("Customer Email", 4);
        indexMap.put("Agent Name", 3);
        Integer index = indexMap.get(filterName);
        if (index != null) {
            String resultText = getText(searchResults1 + "[" + index + "]");
            return resultText;
        } else {
            System.err.println("Error : An error occurred while searching the search results");
            return null;
        }
    }


    public List<String> getCustomerEmails() {
        List<String> emailIds = new ArrayList<>();
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Yesterday"), 2)) {
            click(defaultCallFilterCross.replace("txt", "Yesterday"));
        }
        for (int i = 4; i < 150; i += 16) {
            String id = getText(customerIdList.replace("num", Integer.toString(i)));
            if (!id.equals("-")) {
                emailIds.add(id);
            }
        }
        return emailIds;
    }

    public List<String> getCustomerIds() {
        List<String> customerIds = new ArrayList<>();
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Today"), 2)) {
            click(defaultCallFilterCross.replace("txt", "Today"));
        }
        for (int i = 1; i < 70; i += 16) {
            String id = getText(customerIdList.replace("num", Integer.toString(i)));
            if (!id.equals("-")) {
                customerIds.add(id);
            }
        }
        return customerIds;
    }

    public List<String> getAgentNames() {
        List<String> agentNames = new ArrayList<>();
        if (isLocatorPresent(page, defaultCallFilterCross.replace("txt", "Today"), 2)) {
            click(defaultCallFilterCross.replace("txt", "Today"));
        }
        for (int i = 3; i < 70; i += 16) {
            String id = getText(customerIdList.replace("num", Integer.toString(i)));
            if (!id.equals("-")) {
                agentNames.add(id);
            }
        }
        return agentNames;
    }

    public List<Integer> viewByFilters(List<String> filter) {
        List<Integer> callCount = new ArrayList<>();
        click(viewByBtn);
        Locator locator = page.locator(viewByList);
        int count = locator.count();
        for (int i = 0; i < count; i++) {
            String option = locator.nth(i).innerText();
            if (filter.contains(option)) {  // Check if the filter list contains the option
                locator.nth(i).click();
                log("Clicked on [" + option + "] Button");

                waitFor(2);

                String text = getText(footerCallCount);
                String totalCall[] = text.split("of");
                callCount.add(Integer.parseInt(totalCall[1].trim()));

                click(viewByPreviousSelected);
                log("Clicked on 'View By' Button");
            }
        }
        return callCount;
    }


    public void applyViewByFilter(String filter) {
        click(viewByBtn);
        Locator locator = page.locator(viewByList);
        int count = locator.count();
        for (int i = 0; i < count; i++) {
            String option = locator.nth(i).innerText();
            if (filter.contains(option)) {  // Check if the filter list contains the option
                locator.nth(i).click();
                log("Clicked on [" + option + "] Button");
                break;
            }
        }
        waitFor(1);
        click(scoreDescendingOrder);
    }


    public int getSortByCallTimeFilter(String filterText) {
        try {
            click(sortFilterBtn);
            List<ElementHandle> list = page.querySelectorAll(sortFilterList);
            for (ElementHandle ele : list) {
                String option = ele.innerText();
                if (option.equals(filterText)) {
                    ele.click();
                }
            }
        } catch (Exception e) {
            log("Error: An error occurred while Selecting [" + filterText + "]filter call duration: " + e.getMessage());
        }
        page.waitForSelector(defaultCallTime);
        String firstCallDuration = getText(defaultCallTime);
        return  getValueInSeconds(firstCallDuration);
    }

    public int getSortByCallDurationFilter(String filterText) {
        try {
            click(sortFilterBtn);
            List<ElementHandle> list = page.querySelectorAll(sortFilterList);
            for (ElementHandle ele : list) {
                String option = ele.innerText();
                if (option.equals(filterText)) {
                    ele.click();
                }
            }
        } catch (Exception e) {
            log("Error: An error occurred while Selecting [" + filterText + "]filter call duration: " + e.getMessage());
        }
        page.waitForSelector(defaultCallTime);
        String firstCallDuration = getText(defaultCallTime);
        return hoursToSeconds(firstCallDuration);
    }


    public int getValueInSeconds(String callTime) {
        try {
            int seconds = Integer.parseInt(callTime.replaceAll("[^\\d]", ""));
            return seconds / 100 * 60 + seconds % 100;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int hoursToSeconds(String hrs) {
        try {
            String[] parts = hrs.split(":");
            int hours = Integer.parseInt(parts[0]);
            return hours * 60 * 60;  // 1 hour = 60 minutes = 60 * 60 seconds
        } catch (Exception e) {
            log("error : occurred  in hoursToSeconds method  : " + e.toString());
        }
        return -1;
    }


    public int getCallDuration(String day) {
        if (day == null || day.isEmpty()) {
            throw new IllegalArgumentException("Invalid input for 'yesterday'.");
        }
        String callFilterLocator = defaultCallFilterCross.replace("txt", day);
        click(callFilterLocator);
        log("Clicked on the [" + day + "] default filter button");

        page.waitForSelector(defaultCallTime);
        String callTimeText = getText(defaultCallTime);
        String[] timeParts = callTimeText.split("\\s");
        try {
            if (timeParts.length > 0) {
                return Integer.parseInt(timeParts[0]);
            } else {
                throw new NumberFormatException("Unexpected format for call time: " + callTimeText);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing call duration: " + e.getMessage());
            return -1;
        }
    }

    public List<String> getTableColumns() {
        List<String> columnNameList = new ArrayList<>();
        try {
            List<ElementHandle> columnNames = page.querySelectorAll(allDefaultColumns);
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

    public void removeColumns(List<String> columns) throws Exception {
        for (String columName : columns) {
            fill(searchFilters, columName);
            click(columnName);
            waitFor(1);
            click(searchCross);
        }
        click("(//*[@class='nb__3AgiK'])[1]");
    }

    public void searchSelectAddColumn(List<String> columNameList) {
        if(isLocatorPresent(page,callColumnFilterBtn,2)){
            click(callColumnFilterBtn);
            log("Clicking on [ Column Filter ] Button");
        }
        for (String columName : columNameList) {
            fill(searchFilters, columName);
            click(columnName);
            click(searchCross);
        }
    }

    public void searchSelectAddColumn1(List<String> columNameList) {
        if(isLocatorPresent(page,callColumnFilterBtn,2)){
            click(callColumnFilterBtn);
        }
        waitFor(2);
        for (String columName : columNameList) {
            fill(searchFilters, columName);
            log("Entering in Searchbar [" + columName + "] in Search Field");

            click(columnName);
            log("Clicked on search [" + columName + "] Element");
            waitFor(1);

            click(searchCross);
            log("Clicked on [ Cross ] icon");
        }
    }

    public List<String> getUncheckedCallsColumnFilterNames() {
        List<String> unCheckedcolumnList = new ArrayList<>();
        List<ElementHandle> columnHandleList = page.querySelectorAll(columnNameList);
        List<String> allcolumnList = new ArrayList<>();

        for (ElementHandle columnHandle : columnHandleList) {
            String columnName =  columnHandle.innerText();
            System.out.println("Unchecked Column Names : "+columnName);
            if(columnName.endsWith("•")){
                allcolumnList.add(columnName.substring(0, columnName.length() -1)); // Subtract 2 instead of 1
            } else {
                allcolumnList.add(columnName);
            }
        }

        List<ElementHandle> checkedColumEleList = page.querySelectorAll(checkedMomentTableColumns);
        List<String> CheckedcolumnList = new ArrayList<>();

        for (ElementHandle columnHandle : checkedColumEleList) {
            String columnName =  columnHandle.innerText();
            System.out.println("checked Column Names : "+columnName);
            if(columnName.endsWith("•")) {
                CheckedcolumnList.add(columnName.substring(0, columnName.length() - 1)); // Subtract 2 instead of 1
            } else {
                CheckedcolumnList.add(columnName);
            }
        }

        List<String> tempList = new ArrayList<>(allcolumnList);
        tempList.retainAll(CheckedcolumnList);
        allcolumnList.removeAll(tempList);
        unCheckedcolumnList.addAll(allcolumnList);

        return unCheckedcolumnList;
    }


    public List<String> getAllCallsColumnNames() {
        List<String> allCols = new ArrayList<>();
        try {
            click(callColumnFilterBtn);
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

    public List<String> getCallsTableDefaultColumns() {
        List<String> columnNameList = new ArrayList<>();
        try {
            List<ElementHandle> columnNames = page.querySelectorAll(allDefaultColumns);
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


    public void selectHeaderOption(String header) {
        String headerLocators = headerLocator.replace("menu", header);
        try {
            click(headerLocators);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String email, String password) {
        page.reload();
        page.waitForSelector(emailField);
        waitFor(5);
        try {
            fill(emailField, email);
            fill(passwordField, password);
            log("Entered Email ID : [ "+email+" ] and  [ Password ]");
            waitFor(2);
            clickWithForce(loginBtn);
            log("Clicked on [ Sign In ] Button");
            waitFor(3);

            if (isLocatorPresent(page, buttonText.replace("btn", "Sign In"), 5)) {
                click(buttonText.replace("btn", "Sign In"));
                log("Clicked on [ Sign In ] Button");
            }
        } catch (Exception e) {
            log("Error: An error in login : " + e.getMessage());
        }
    }

}
