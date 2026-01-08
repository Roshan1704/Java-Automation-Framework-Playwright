package com.enterprise.automation.util;

import com.enterprise.automation.browser.PlaywrightFactory;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assertion utilities with automatic screenshots on failure
 */
public class AssertionHelper {
    private static final Logger logger = LoggerFactory.getLogger(AssertionHelper.class);

    private AssertionHelper() {}

    /**
     * Assert element is visible
     */
    public static void assertElementVisible(String selector) {
        Page page = PlaywrightFactory.getPage();
        Locator element = page.locator(selector);
        
        if (!element.isVisible()) {
            PlaywrightFactory.takeScreenshot("assertion-failed-element-not-visible");
            throw new AssertionError("Element not visible: " + selector);
        }
        logger.info("Assertion passed: element visible: {}", selector);
    }

    /**
     * Assert element is hidden
     */
    public static void assertElementHidden(String selector) {
        Page page = PlaywrightFactory.getPage();
        Locator element = page.locator(selector);
        
        if (element.isVisible()) {
            PlaywrightFactory.takeScreenshot("assertion-failed-element-visible");
            throw new AssertionError("Element is visible: " + selector);
        }
        logger.info("Assertion passed: element hidden: {}", selector);
    }

    /**
     * Assert element is enabled
     */
    public static void assertElementEnabled(String selector) {
        Page page = PlaywrightFactory.getPage();
        Locator element = page.locator(selector);
        
        if (element.isDisabled()) {
            PlaywrightFactory.takeScreenshot("assertion-failed-element-disabled");
            throw new AssertionError("Element is disabled: " + selector);
        }
        logger.info("Assertion passed: element enabled: {}", selector);
    }

    /**
     * Assert element text equals expected
     */
    public static void assertElementText(String selector, String expectedText) {
        Page page = PlaywrightFactory.getPage();
        String actualText = page.locator(selector).textContent();
        
        if (!actualText.equals(expectedText)) {
            PlaywrightFactory.takeScreenshot("assertion-failed-text-mismatch");
            throw new AssertionError("Expected text: " + expectedText + ", but got: " + actualText);
        }
        logger.info("Assertion passed: element text matches: {}", expectedText);
    }

    /**
     * Assert element text contains substring
     */
    public static void assertElementTextContains(String selector, String substring) {
        Page page = PlaywrightFactory.getPage();
        String actualText = page.locator(selector).textContent();
        
        if (!actualText.contains(substring)) {
            PlaywrightFactory.takeScreenshot("assertion-failed-text-not-contains");
            throw new AssertionError("Text does not contain: " + substring + ", but got: " + actualText);
        }
        logger.info("Assertion passed: element text contains: {}", substring);
    }

    /**
     * Assert element attribute value
     */
    public static void assertElementAttribute(String selector, String attribute, String expectedValue) {
        Page page = PlaywrightFactory.getPage();
        String actualValue = page.locator(selector).getAttribute(attribute);
        
        if (!actualValue.equals(expectedValue)) {
            PlaywrightFactory.takeScreenshot("assertion-failed-attribute-mismatch");
            throw new AssertionError("Expected " + attribute + ": " + expectedValue + ", but got: " + actualValue);
        }
        logger.info("Assertion passed: element attribute {} = {}", attribute, expectedValue);
    }

    /**
     * Assert current URL
     */
    public static void assertCurrentUrl(String expectedUrl) {
        Page page = PlaywrightFactory.getPage();
        String currentUrl = page.url();
        
        if (!currentUrl.equals(expectedUrl)) {
            PlaywrightFactory.takeScreenshot("assertion-failed-url-mismatch");
            throw new AssertionError("Expected URL: " + expectedUrl + ", but got: " + currentUrl);
        }
        logger.info("Assertion passed: current URL = {}", expectedUrl);
    }

    /**
     * Assert current URL contains substring
     */
    public static void assertCurrentUrlContains(String urlPart) {
        Page page = PlaywrightFactory.getPage();
        String currentUrl = page.url();
        
        if (!currentUrl.contains(urlPart)) {
            PlaywrightFactory.takeScreenshot("assertion-failed-url-not-contains");
            throw new AssertionError("URL does not contain: " + urlPart + ", but got: " + currentUrl);
        }
        logger.info("Assertion passed: current URL contains: {}", urlPart);
    }

    /**
     * Assert page title
     */
    public static void assertPageTitle(String expectedTitle) {
        Page page = PlaywrightFactory.getPage();
        String actualTitle = page.title();
        
        if (!actualTitle.equals(expectedTitle)) {
            PlaywrightFactory.takeScreenshot("assertion-failed-title-mismatch");
            throw new AssertionError("Expected title: " + expectedTitle + ", but got: " + actualTitle);
        }
        logger.info("Assertion passed: page title = {}", expectedTitle);
    }

    /**
     * Assert element count
     */
    public static void assertElementCount(String selector, int expectedCount) {
        Page page = PlaywrightFactory.getPage();
        int actualCount = page.locator(selector).count();
        
        if (actualCount != expectedCount) {
            PlaywrightFactory.takeScreenshot("assertion-failed-count-mismatch");
            throw new AssertionError("Expected count: " + expectedCount + ", but got: " + actualCount);
        }
        logger.info("Assertion passed: element count = {}", expectedCount);
    }

    /**
     * Generic assertion for custom conditions
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            PlaywrightFactory.takeScreenshot("assertion-failed-" + message);
            throw new AssertionError(message);
        }
        logger.info("Assertion passed: {}", message);
    }
}
