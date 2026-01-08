package com.enterprise.automation.tests;

import com.enterprise.automation.browser.PlaywrightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base Test class with common setup/teardown for all test classes
 */
public abstract class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        logger.info("=== Test Suite Started ===");
        PlaywrightFactory.initPlaywright();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        logger.info("=== Test Suite Completed ===");
        PlaywrightFactory.closePlaywright();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        logger.info("Starting test: {}", method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        logger.info("Test result: {} - Status: {}", result.getName(), 
                result.isSuccess() ? "PASSED" : "FAILED");
        
        if (!result.isSuccess()) {
            PlaywrightFactory.takeScreenshot("test-failure-" + result.getName());
            PlaywrightFactory.saveTrace("trace-" + result.getName());
            logger.error("Test failed: {}. Error: {}", result.getName(), result.getThrowable());
        }
        
        PlaywrightFactory.closeAll();
    }
}
