/*
 * package com.convozen.Reportings;
 * 
 * import com.aventstack.extentreports.AnalysisStrategy; import
 * com.aventstack.extentreports.ExtentReports; import
 * com.aventstack.extentreports.reporter.ExtentSparkReporter; import
 * com.aventstack.extentreports.reporter.configuration.ViewName;
 * 
 * import java.io.File; import java.io.IOException; import java.sql.Timestamp;
 * 
 * public class ExtentManager { public static String filepath = "", suiteName =
 * ""; static ExtentSparkReporter spark = null; public static ExtentReports
 * extent; private static final boolean customConfigFlag = true;
 * 
 * public static String projectName = null; public static String
 * testSuiteNameForKlov = "";
 * 
 * public synchronized static ExtentReports getReporter() { if (extent == null)
 * { extent = new ExtentReports(); filepath = System.getProperty("user.dir") +
 * "/ExtentReports/" + "ExtentReportResults_" + new
 * Timestamp(System.currentTimeMillis()).toString().replaceAll(":",
 * "_").replaceAll(" ", "_") + ".html";
 * 
 * spark = new ExtentSparkReporter(filepath);
 * spark.viewConfigurer().viewOrder().as(new ViewName[] { ViewName.DASHBOARD,
 * ViewName.TEST, ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.DEVICE,
 * ViewName.LOG, ViewName.AUTHOR,
 * 
 * }).apply();
 * 
 * if (customConfigFlag) { loadCustomConfig(new
 * File(System.getProperty("user.dir") + "/resource/spark-config.xml"));
 * useManualConfig(true); }
 * 
 * extent.setSystemInfo("Username", System.getProperty("user.name"));
 * extent.setSystemInfo("OS", System.getProperty("os.name"));
 * extent.setSystemInfo("OS Version", System.getProperty("os.version"));
 * extent.setSystemInfo("Java Version", System.getProperty("java.version"));
 * extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
 * extent.setSystemInfo("User Language", System.getProperty("user.language"));
 * extent.setSystemInfo("Author", "Milind"); extent.attachReporter(spark);
 * extent.setAnalysisStrategy(AnalysisStrategy.CLASS); } return extent; }
 * 
 * private static void loadCustomConfig(File configFile) { try {
 * spark.loadXMLConfig(configFile);
 * extent.setReportUsesManualConfiguration(true); } catch (IOException e) {
 * e.printStackTrace(); System.err.println("Failed to load custom config"); } }
 * 
 * private static void useManualConfig(boolean flag) {
 * extent.setReportUsesManualConfiguration(flag); } }
 */