package com.convozen.Pages.Playwrights;

import com.convozen.CommonUtils.CommonPlaywright;
import com.microsoft.playwright.Page;

public class ConvozenWebDashboard extends CommonPlaywright {
	Page page;
	public ConvozenWebDashboard(Page page) {
		super(page);
		this.page = page; 
	}

	public void getWebLogin(String email, String password) {
		fill("#email", email);
		fill("#password", password);
		click("//button[@type='submit' and text()='Sign In']");
	}

}
