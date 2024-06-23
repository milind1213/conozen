package com.convozen.Pages.Playwrights;
import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.Page;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import java.util.*;

import static com.convozen.Pages.Playwrights.Locators.*;

public class ConvozenAgents extends CommonPlaywright {
  public Page page;
    public ConvozenAgents(Page page) {
        super(page);
        this.page = page;
    }

    public Double getAgentCallCount() {
        String callCount = getText(agentCallCount);
        return Double.parseDouble(callCount);
    }

    public Double getLongSilenceCount() {
        String longSilence = getText(agentLongSilenceCount);
        return Double.parseDouble(longSilence);
    }

    public Map<String, String> getAgentTaggedChecklist() {
        Map<String, String> map = new LinkedHashMap<>();
        click(agentStatisticNextArrow);
        List<ElementHandle> taggedChechklist = page.querySelectorAll(agentTaggedChecklist);
        for (ElementHandle handle : taggedChechklist) {
            String str = handle.innerText();
            String a[] = str.split("\\n");
            String name = a[0];
            String percentage = a[2].substring(0, a[2].length()-1);
            map.put(name,percentage);
        }
        return map;
    }


    public List<Double> longHoldPercentage(List<String> filterByList) {
        List<Double> callLongHoldList = new ArrayList<>();
        clickWithForce(closePopUpCross);
        click(firstAgentDetailsBtn);
        log("Closed and Clicked on Agent[ viewBox]Button");

        click(agentStatisticNextArrow);
        log("Clicked on Agent [Next] Arrow");

        click(viewByAgentStatistics);
        log("Clicked on the [viewBy] dropdown button");
        waitFor(2);
        Locator viewByOptions = page.locator(viewByList);
        for (String filter : filterByList) {
            for (int i = 0; i < viewByOptions.count(); i++) {
                String option = viewByOptions.nth(i).innerText();
                if (filter.equals(option)) {
                    viewByOptions.nth(i).click();
                    waitFor(2);

                    String madeCalls = getText("(//p[contains(@class,'nb__3qb7_') and contains(text(),'%')])[3]");
                    callLongHoldList.add(Double.parseDouble(madeCalls.substring(0, madeCalls.length()-1)));

                    click(previousSelectedViewByAgent);
                    log("Clicked on 'View By' Button");
                }
            }
        }

        return callLongHoldList;
    }

    public Double getAgentLongHoldPercentage() {
        click(firstAgentDetailsBtn);
        click(agentStatisticNextArrow);
        log("Clicked on Agent [ viewBox] and [Next] page arrow");

        page.waitForSelector(longHoldPercentageLoc);
        String madeCalls = getText(longHoldPercentageLoc);
        return Double.parseDouble(madeCalls.substring(0, madeCalls.length() - 1));
    }


    public List<Integer> getAgentCallAndCustomerReachedData(List<String> filterByList) {
        List<Integer> callList = new ArrayList<>();
        click(firstAgentDetailsBtn);
        log("Clicked on Agent [ viewBox] Button");

        click(viewByAgentStatistics);
        log("Clicked on the [viewBy] dropdown button");
        waitFor(2);
        Locator viewByOptions = page.locator(viewByList);
        for (String filter : filterByList) {
            for (int i = 0; i < viewByOptions.count(); i++) {
                String option = viewByOptions.nth(i).innerText();
                if (filter.equals(option)) {
                    viewByOptions.nth(i).click();
                    waitFor(2);
                    page.waitForSelector(madeCallsNumber);
                    waitFor(5);
                    String madeCalls = getText(madeCallsNumber);
                    System.out.println(madeCalls);
                    callList.add(Integer.parseInt(madeCalls));

                    click(previousSelectedViewByAgent);
                    log("Clicked on 'View By' Button");
                }
            }
        }
        return callList;
    }


    public List<Integer> getAgentCallAndCustomerReached() {
        List<Integer> call = new ArrayList<>();
        click(firstAgentDetailsBtn);
        waitFor(5);
        page.waitForSelector(agentStaticDataList);
        Locator loc = page.locator(agentStaticDataList);
        boolean isFirstLoop = true; // Flag to indicate the first loop
        for (int i = 0; i < 3; i++) {
            String text = loc.nth(i).innerText();
            String nums = text.replaceAll("[^0-9]", "").trim();
            if (!nums.isEmpty()) {
                int currentValue;
                if (isFirstLoop) {
                    currentValue = Integer.parseInt(nums);
                } else {
                    currentValue = nums.length() > 1 ? Integer.parseInt(nums.substring(0, nums.length() / 2)) : 0;
                    // Add first half of digits if nums length is greater than 1
                }
                call.add(currentValue);
                isFirstLoop = false; // Update isFirstLoop flag
            }
        }
        return call;
    }

    public void getAgentStatistics(){
        click(firstAgentDetailsBtn);
        waitFor(2);
        page.waitForSelector(agentStaticDataList);
        Locator loc = page.locator(agentStaticDataList);
        for(int i=0;i<loc.count();i++){
            String text = loc.nth(i).innerText();
            System.out.println(text);
        }
    }

    public int getAgentTableDetails(List<String> filter) {
        int callCount=0;
        click(viewByBtn);
        Locator locator = page.locator(viewByList);
        int count = locator.count();
        for (int i = 0; i < count; i++) {
            String option = locator.nth(i).innerText();
            if (filter.contains(option)) {  // Check if the filter list contains the option
                locator.nth(i).click();
                log("Clicked on [" + option + "] Button");

                waitFor(2);
                page.waitForSelector(agentCallCount);
                String text = getText(agentCallCount);
                callCount = Integer.parseInt(text);

                if (callCount == 0) {
                    click(viewByPreviousSelected);
                    log("Clicked on " + option + " Button");
                } else {
                    break;
                }
            }
        }
        return callCount;
    }

    public String rowAgentsRowData() {
        StringBuilder callData = new StringBuilder();
        Locator eleList = page.locator(agentFirstRow);
        for (int i = 0; i < eleList.count()-1; i++) {
            String value = eleList.nth(i).innerText();
            int index = value.indexOf(" \n");
            if (index != -1) {
                value = value.substring(0, index);
            }
            if (i == 2) {
                waitFor(2);
                Locator list = page.locator(checklistCoverage);
                for (int j=0;j<list.count();j++) {
                    String val = list.nth(j).innerText();
                    callData.append(val).append(",");
                }
            } else {
                callData.append(value).append(",");
            }
        }
        return callData.toString().substring(1, callData.length() -1).trim();
    }


 /*   public Map<String, String> rowAgentsRowData1() {
        Map<String, String> map = new LinkedHashMap<>();
        Locator eleList = page.locator(agentFirstRow);
        for (int i = 0; i < eleList.count(); i++) {
            String value = eleList.nth(i).innerText();
            int index = value.indexOf(" \n");
            if (index != -1) {
                value = value.substring(0, index);
                map.put("Agent Name", value);
                map.put("Calls Made", value);
                map.put("Avg Handling Time", value);
                map.put("Total Talktime", value);
                map.put("Negative Emotion", value);
                map.put("Long Silence", value);
            }
            if (i == 2) {
                Locator list = page.locator(checklistCoverage);
                for (int j = 0; j < list.count(); j++) {
                    String val = list.nth(j).innerText();
                    map.put("Checklist Coverage", val);
                }
            }
        }
        return map;
    }

*/

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
                page.waitForSelector(agentCallCount);
                waitFor(5);

                String text = getText(agentCallCount);
                callCount.add(Integer.parseInt(text));

                click(viewByPreviousSelected);
                log("Clicked on 'View By' Button");
            }
        }
        return callCount;
    }

    public void removeColumns(List<String> columns) throws Exception {
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
        click("//p[.='Agent Name']");
    }

    public List<String> getTableColumns() {
        List<String> columnNameList = new ArrayList<>();
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

    public void searchSelectAddColumn(List<String> columNameList) {
        for (String columName : columNameList) {
            fill(searchFilters, columName);
            log("Entering in Searchbar [" + columName + "] in Search Field");

            click(columnNameList);
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

    public void searchValidate(List<String> teams) {
        try {
            click(selectTeamDrp);
            log("Clicked on [Select team] Dropdown");
            for (String teamName : teams) {
                fill(teamSearchbar, teamName);
                click(searchResults.replace("txt", teamName));
                click(searchCross);
                log("Entered,Clicked and Cleared[" + teamName + "] Icon");
            }
        } catch (Exception e) {
            log("Error : An error occurred while Search Select teams");
        }
    }

    public List<String> getTeaNames() {
        List<String> teams = new ArrayList<String>();
        click(selectTeamDrp);
        log("Clicked on the[Select Team] dropDown");
        waitFor(2);
        List<ElementHandle> teamNames = page.querySelectorAll(ListDrp);
        int count=0;
        for (ElementHandle team : teamNames) {
            String text = team.innerText();
            teams.add(text);
            count++;
            if(count==3){
                break;
            }
        }
        click(selectTeamDrp);
        log("click on the[Select Team]");
        return teams;
    }

    public void login(String email, String password) {
        page.reload();
        waitFor(2);
        try {
            log("Entering Email: [" + email+"]");
            fill(emailField, email);

            log("Entering Password: [" + password+"]");
            fill(passwordField, password);

            log("Clicking on [Sign In] Button");
            click(loginBtn);
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

