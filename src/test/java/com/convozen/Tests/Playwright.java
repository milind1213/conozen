
package com.convozen.Tests;

import static com.convozen.Utils.FileUtil.getProperty;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.convozen.CommonConstants;
import com.convozen.Pages.Playwrights.ConvozenWebDashboard;
import com.convozen.TestBase.BaseTest;
import com.convozen.Utils.TestListeners;

@Listeners(TestListeners.class)
public class Playwright extends BaseTest {
	private ConvozenWebDashboard user;

	@BeforeMethod
	public void setUp() {
		if (page == null) {
			getPlaywrightBrowser();
		}
	}

	public ConvozenWebDashboard getConvozenWebLogin() {
		String webUrl = getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_WEBURL);
		String userName = getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_USERNAME);
		String password = getProperty(CommonConstants.CONVOZEN, CommonConstants.CONVOZEN_PASSWORD);

		page.navigate(webUrl);
		log("Navigated to URL: " + webUrl);
		user = new ConvozenWebDashboard(page);

		log("Entering Email and Password in input field");
		user.getWebLogin(userName, password);
		log("Successfully logged in with Email : " + userName);

		return user;
	}

	@Test
	public void callFIlters() {
		ConvozenWebDashboard user = getConvozenWebLogin();
		log("Successfully logged in with Email : ");
	}

	@Test
	public void callFIlters1() {
		log("Successfully logged in with Email : ");
	}

	@Test
	public void callFIlters2() {
		log("Successfully logged in with Email : ");
	}

	@Test
	public void callFIlters3() {
		log("Successfully logged in with Email : ");
	}

	@AfterMethod
	public void tearDown() {
		if (newPage.get() != null) {
			newPage.get().close();
		}
	}

}
