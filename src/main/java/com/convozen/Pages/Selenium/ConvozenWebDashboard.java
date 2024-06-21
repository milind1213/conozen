package com.convozen.Pages.Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.convozen.CommonUtils.CommonSelenium;

public class ConvozenWebDashboard extends CommonSelenium {
	public WebDriver driver;

	private final ConovenMoments moments;
	private final ConvozenAgents agents;
	private final ConvozenCalls calls;
	private final ConvozenChecklists checklists;
	private final ConvozenWebLogin weblogin;

	public ConvozenWebDashboard(WebDriver driver) {
		super(driver);
		this.driver = driver;

		moments = new ConovenMoments(driver);
		agents = new ConvozenAgents(driver);
		calls = new ConvozenCalls(driver);
		checklists = new ConvozenChecklists(driver);
		weblogin = new ConvozenWebLogin(driver);
	}

	public ConvozenWebLogin getWebLogin() {
		return weblogin;
	}

	public ConovenMoments getMoments() {
		return moments;
	}

	public ConvozenAgents getAgents() {
		return agents;
	}

	public ConvozenCalls getCallss() {
		return calls;
	}

	public ConvozenChecklists getChecklistss() {
		return checklists;
	}

}
