package com.convozen.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
	private static Map<Long, ExtentTest> extentTestMap = new HashMap<>();
	private static ExtentReports extent = ExtentManager.getReporter();

	public static synchronized ExtentTest getTest() {
		return extentTestMap.get(Thread.currentThread().getId());
	}

	public static synchronized void startTest(String testName, String desc) {
		ExtentTest test = extent.createTest(testName, desc);
		extentTestMap.put(Thread.currentThread().getId(), test);
	}

	public static synchronized void endTest() {
		extent.removeTest(extentTestMap.get(Thread.currentThread().getId()));
		extentTestMap.remove(Thread.currentThread().getId());
	}
}
