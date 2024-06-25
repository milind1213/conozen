package com.convozen.Pages.Playwrights;

import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public class ConvozenAudits extends CommonPlaywright {
   Page page;
    public ConvozenAudits(Page page) {
        super(page);
        this.page = page;
    }

    String listAuditsBtn = "//*[contains(@class,'nb__3flxl') and .='List']";
    String listDashboardBtn = "//*[contains(@class,'nb__3flxl') and .='Board']";
    String statusBtn ="//*[@class='nb__1vYnE']//*[@class='nb__1lqJQ' and normalize-space(text())='btn']";
    String columnCount = ".nb__3AgiK";
    String tableTotalCount ="//div[@class='nb__2XrMQ']";

    String toDoDashboardAudits = "//*[@class='nb__3T3LE']//*[@class='nb__1XFl9'][1]//p[@class='nb__Hjc4U']";
    String ongoingDashboardAudits = "//*[@class='nb__3T3LE']//*[@class='nb__1XFl9'][2]//p[@class='nb__Hjc4U']";
    String completedDashboardAudits = "//*[@class='nb__3T3LE']//*[@class='nb__1XFl9'][3]//p[@class='nb__Hjc4U']";
    String overdueDashboardAudits = "//*[@class='nb__3T3LE']//*[@class='nb__1XFl9'][4]//p[@class='nb__Hjc4U']";
    String auditList = ".nb__2tVd9";
    String responseBtn = "//*[@class='nb__1t7V5']";
    String responseTextBtn ="//*[@class='nb__1t7V5']//button[text()='btn']";
    String unselectedTextBtn ="(//*[@class='nb__1t7V5']//button[text()='btn' and @data-selected='false'])[1]";
    String questionList = ".nb__2y8kK";
    String firstQuestion ="(//*[@class='nb__2y8kK'])[1]";
    String submitBtn  ="//button[@class='nb__21EzD']";
    String updateBtn = ".nb__21EzD";

    public int getAuditCalls() {
        waitFor(2);
        return page.locator(auditList).count();
    }

    public void submitQuestion(String btn) {
        List<ElementHandle> audioList = page.querySelectorAll(auditList);
        for (ElementHandle audioElement : audioList) {
            audioElement.click();
            click(firstQuestion);
            List<ElementHandle> questions = page.querySelectorAll(questionList);
            for (ElementHandle question : questions) {
                question.click();
                while (isLocatorPresent(page, unselectedTextBtn.replace("btn", btn), 2)) {
                    click(unselectedTextBtn.replace("btn", btn));
                    waitFor(1);
                }
                question.click();
                if (page.isEnabled(submitBtn)) {
                    click(submitBtn);
                }
            }
        }
        if (page.isEnabled(updateBtn)) {
            click(updateBtn);
        }
    }

    public void selectAuditFromToDo(String auditName) {
        try {
            page.waitForSelector(toDoDashboardAudits);
            waitFor(2);
            Locator todoList = page.locator(toDoDashboardAudits);
            boolean auditFound = false;
            for (int i = 0; i < todoList.count(); i++) {
                String auditText = todoList.nth(i).innerText();
                if (auditText.equals(auditName)) {
                    todoList.nth(i).click();
                    log("Clicked on audit: " + auditName);
                    auditFound = true;
                    break;
                }
            }
            if (!auditFound) {
                log("Audit name '" + auditName + "' not found in ToDo list. Test passes as no action is needed.");
            }
        } catch (Exception e) {
            System.err.println("Error occurred while selecting audit: " + e.getMessage());
        }
    }
    
    public int getColumnCount()
    {
        waitFor(2);
        try {
            page.waitForSelector(columnCount);
            return page.locator(columnCount).count();
        } catch (Exception e) {
            System.err.println("Failed to get Column numbers");
        }
        return 0;
    }

    public int getDataTableCount()
    {
        waitFor(2);
        try {
            page.waitForSelector(tableTotalCount);
            return page.locator(tableTotalCount).count();
        } catch (Exception e) {
            System.err.println("Failed to get Column numbers");
        }
        return 0;
    }

    public int getAuditStatusCounts(String status)
    {
        try {
            click(listAuditsBtn);
            click(statusBtn.replace("btn", status));
            int columnCount = getColumnCount();
            if (columnCount == 0) {
                return 0;
            }
            int totalCount = getDataTableCount();
            if (totalCount == 0) {
                return 0;
            }
            return totalCount / columnCount;
        } catch (Exception e) {
            System.err.println("Failed to Get Audit status counts for [" + status + "]: " + e.getMessage());
        }
        return 0;
    }







}
