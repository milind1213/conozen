package com.convozen.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUtil {

	public static final Logger logger = LogManager.getLogger(FileUtil.class);

	public static Properties readFileAsProperties(String fileFromClasspath) throws Exception {
		logger.info("Loading the file " + fileFromClasspath);
		try {
			File file = new File((FileUtil.class.getClassLoader().getResource(fileFromClasspath)).getFile());
			Properties properties = new Properties();
			if (file.exists()) {
				properties.load(ClassLoader.getSystemResourceAsStream(fileFromClasspath));
				return properties;
			} else {
				logger.error("File " + fileFromClasspath + " does not exist");
			}
		} catch (IOException e) {
			logger.error("Failed to read file " + fileFromClasspath);
			throw new Exception(e.getCause());
		}
		return null;
	}

	public static InputStream readFileAsStream(String fileFromClasspath) throws Exception {
		logger.info("Loading the file " + fileFromClasspath);
		InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(fileFromClasspath);
		if (in == null) {
			logger.error("Failed to read file " + fileFromClasspath);
			throw new Exception("Unable to load the file " + fileFromClasspath);
		}
		return in;
	}

	public static Properties readFileFromExternalPath(String fileDiskPath) throws Exception {
		logger.info("Loading the properties from " + fileDiskPath);
		Properties properties = null;
		File filePath = new File(fileDiskPath);
		try {
			if (filePath.exists()) {
				logger.debug("Properties file @ " + filePath);
				properties.load(new FileInputStream(filePath));
			}
		} catch (IOException e) {
			logger.error("Unable to properties read the file from path " + fileDiskPath);
			throw new Exception(e.getCause());
		}
		return properties;
	}

	public static String getProperty(String propFile, String key) {
		Properties prop = null;

		String propertiesPath = "./PropDirectory/" + propFile + ".properties";

		if (prop == null) {
			prop = new Properties();
			try {
				prop.load(new FileInputStream(propertiesPath));
			} catch (Exception e) {
				System.out.println("either key or value is missing at : [" + propertiesPath + "] path..");
			}
		}
		return prop.getProperty(key);
	}

	public static void writeToFile(String filepath, String content, boolean append) {
		try {
			String encoding = null;
			FileUtils.writeStringToFile(new File(filepath), content, encoding, append);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readTemplateAsString(String sourcePath) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(new FileInputStream(new File(sourcePath)), writer, "UTF-8");
		String theString = writer.toString();
		return theString;
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
				logger.info("Directory is created at : " + dir);
			} else {
				destFile = new File((dir));
				logger.info("Directory is already created at " + dir);
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
			logger.info("report backup is done !!!");
		}

	}

	public static boolean deleteFile(String sourcePath, String fileName) {
		try {
			File file = new File(sourcePath);
			File[] files = file.listFiles();
			for (File filename : files) {
				if (filename.getName().equalsIgnoreCase(fileName)) {
					filename.delete();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void createDirectory(String directory) {
		try {
			Files.createDirectories(Paths.get(directory));
		} catch (IOException e) {
			System.err.println("Failed to create a directory");
		}
	}
}