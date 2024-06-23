package com.convozen.TestBase;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer, IRetryAnalyzer {
	private int retryCount = 1;
	private static final int MAX_RETRY_COUNT = 2; // Maximum number of retries

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < MAX_RETRY_COUNT) {
			retryCount++;
			return true;
		}
		return false;
	}

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		if (annotation.getRetryAnalyzerClass() == null) {
			annotation.setRetryAnalyzer(RetryListener.class);
		}
	}
}
