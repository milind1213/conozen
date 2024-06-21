package com.convozen.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

public class ExtentManager {

	private static ExtentReports extentReports;

	public static synchronized ExtentReports createInstance(String fileName, String reportName, String documentTitle) {
		ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(fileName);
		extentSparkReporter.viewConfigurer().viewOrder().as(new ViewName[] { ViewName.DASHBOARD, ViewName.TEST,
				ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.DEVICE, ViewName.AUTHOR }).apply();

		extentSparkReporter.config().setReportName(reportName);
		extentSparkReporter.config().setDocumentTitle(documentTitle);
		extentSparkReporter.config().setTheme(Theme.STANDARD);
		extentSparkReporter.config().setEncoding("utf-8");

		extentReports = new ExtentReports();
		extentReports.attachReporter(extentSparkReporter);
		extentReports.setSystemInfo("Username", System.getProperty("user.name"));
		extentReports.setSystemInfo("OS", System.getProperty("os.name"));
		extentReports.setSystemInfo("OS Version", System.getProperty("os.version"));
		extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
		extentReports.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extentReports.setSystemInfo("Author", "Convozen");

		return extentReports;
	}
}
