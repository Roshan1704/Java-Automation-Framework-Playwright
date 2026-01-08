package com.enterprise.automation.listener;

import com.enterprise.automation.browser.PlaywrightFactory;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

/**
 * Custom TestNG Listener for test lifecycle events
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName());
        
        String screenshotPath = "target/screenshots/test-failure-" + result.getMethod().getMethodName() + ".png";
        try {
            FileInputStream screenshotFile = new FileInputStream(screenshotPath);
            Allure.addAttachment("Failure Screenshot", "image/png", screenshotFile, ".png");
            screenshotFile.close();
        } catch (FileNotFoundException e) {
            logger.warn("Screenshot not found: {}", screenshotPath);
        } catch (Exception e) {
            logger.error("Error attaching screenshot: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("Test passed within success percentage: {}", result.getMethod().getMethodName());
    }
}
