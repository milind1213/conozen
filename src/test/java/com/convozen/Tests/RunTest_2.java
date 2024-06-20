
package com.convozen.Tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.convozen.TestBase.BaseTest;
import com.microsoft.playwright.Page;

public class RunTest_2 extends BaseTest {

	@Test
	public void testPageTitle() {
		getPlaywrightBrowser();
		Page user = page.get();
		user.navigate("https://beta.convozen.ai/");
	}

	@AfterMethod
	public void tearDown() {
		if (page.get() != null) {
			page.get().close();
		}
	}

}
