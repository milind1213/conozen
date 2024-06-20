
/*package com.convozen.Reportings;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
	private static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
	private static Map<Integer, ExtentTest> extentChildTestMap = new HashMap<>();
	private static ExtentReports extent = ExtentManager.getReporter();

	public static synchronized ExtentTest getTest() {
		return extentTestMap.get((int) Thread.currentThread().getId());
	}

	public static synchronized ExtentTest getChildTest() {
		return extentChildTestMap.get((int) Thread.currentThread().getId());
	}

	public static synchronized ExtentTest startTest(String testName, String desc) {
		ExtentTest test = extent.createTest(testName, desc);
		extentTestMap.put((int) Thread.currentThread().getId(), test);
		return test;
	}

	public static synchronized ExtentTest startChildTest(String testName, String desc) {
		ExtentTest childTest = getTest().createNode(testName, desc);
		extentChildTestMap.put((int) Thread.currentThread().getId(), childTest);
		return childTest;
	}

} */
