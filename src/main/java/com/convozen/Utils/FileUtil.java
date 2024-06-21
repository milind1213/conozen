package com.convozen.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class FileUtil {

	public static String getProperty(String propFile, String key) {
		Properties prop = null;
		String propertiesPath = "./PropDirectory/" + propFile + ".properties";
		prop = new Properties();
		try {
			prop.load(new FileInputStream(propertiesPath));
		} catch (Exception e) {
			System.out.println("either key or value is missing at : [" + propertiesPath + "] path..");
		}
		return prop.getProperty(key);
	}

	public static void createDirectoryIfNotExists(Path dirPath) {
		try {
			if (Files.notExists(dirPath)) {
				Files.createDirectories(dirPath);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to create report directory: " + dirPath, e);
		}
	}

	public static void deleteOldReports(Path dirPath) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "Automation_Report_*.html")) {
			for (Path entry : stream) {
				if (!Files.isDirectory(entry)) {
					Files.delete(entry);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to delete old reports in directory: " + dirPath, e);
		}
	}

	public static String generateReportFilePath(String reportDirectoryPath) {
		String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
		String reportFileName = "Automation_Report_" + timestamp + ".html";
		return Paths.get(reportDirectoryPath, reportFileName).toString();
	}

	public static String enhancedMethodName(String str) {
		StringBuilder sb = new StringBuilder();
		boolean capitalizeNext = true;
		for (char c : str.toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				sb.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
				capitalizeNext = false;
			} else if (sb.length() > 0) {
				sb.append(' ');
				capitalizeNext = true;
			}
		}
		return sb.toString().trim();
	}

    public static InputStream readFileAsStream(String fileFromClasspath) throws Exception {
        System.out.println("Loading the file " + fileFromClasspath);
        InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(fileFromClasspath);
        if (in == null) {
            throw new Exception("Unable to load the file " + fileFromClasspath);
        }
        return in;
    }

    public static Properties readFileFromExternalPath(String fileDiskPath) throws Exception {
        System.out.println("Loading the properties from " + fileDiskPath);
        Properties properties = null;
        File filePath = new File(fileDiskPath);
        try {
            if (filePath.exists()) {
                System.out.println("Properties file @ " + filePath);
                properties.load(new FileInputStream(filePath));
            }
        } catch (IOException e) {
            System.out.println("Unable to properties read the file from path " + fileDiskPath);
            throw new Exception(e.getCause());
        }
        return properties;
    }



    public static void writeToFile(String filepath, String content, boolean append) {
        try {
            String encoding = null;
            FileUtils.writeStringToFile(new File(filepath), content, encoding, append);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reportBackup(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);
        String currentOS = System.getProperty("os.name");
        File destFile = null;
        String dir = null;
        if (currentOS.toLowerCase().contains("windows")) {
            dir = System.getProperty("user.home") + "\\Desktop\\automation_report_backup";
            Path path = Paths.get(dir);
            if (!(Files.isDirectory(path))) {
                Files.createDirectories(path);
                destFile = new File((dir));
                System.out.println("Directory is created at : " + dir);
            } else {
                destFile = new File((dir));
                System.out.println("Directory is already created at " + dir);
            }
        } else {
            dir = System.getProperty("user.home") + "/Desktop/automation_report_backup";
            Path path = Paths.get(dir);
            if (!(Files.isDirectory(path))) {
                Files.createDirectories(path);
                destFile = new File((dir));
                System.out.println("Directory is created at : " + dir);
            } else {
                destFile = new File((dir));
                System.out.println("Directory is already created at " + dir);
            }
        }
        if (sourceFile.exists()) {
            FileUtils.copyDirectory(sourceFile, new File((dir)));
            System.out.println("report backup is done !!!");
        }
    }

}