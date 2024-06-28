package com.convozen.Pages.Selenium;

import org.openqa.selenium.WebDriver;

import com.convozen.CommonUtils.CommonSelenium;

public class ConvozenWebDashboard extends CommonSelenium {
	public WebDriver driver;

	private final ConovenMoments moments;
	private final ConvozenAgents agents;
	private final ConvozenCalls calls;
	private final ConvozenChecklists checklists;
	private final LoginHome weblogin;

	public ConvozenWebDashboard(WebDriver driver) {
		super(driver);
		this.driver = driver;

		moments = new ConovenMoments(driver);
		agents = new ConvozenAgents(driver);
		calls = new ConvozenCalls(driver);
		checklists = new ConvozenChecklists(driver);
		weblogin = new LoginHome(driver);
	}

	public LoginHome getWebLogin() {
		return weblogin;
	}

	public ConovenMoments getMoment() {
		return moments;
	}

	public ConvozenAgents getAgent() {
		return agents;
	}

	public ConvozenCalls getCall() {
		return calls;
	}

	public ConvozenChecklists getChecklist() {
		return checklists;
	}

}
