package com.enterprise.automation.pages;

import com.enterprise.automation.browser.PlaywrightFactory;
import com.enterprise.automation.util.AssertionHelper;
import com.enterprise.automation.util.WaitHelper;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Page Object class with common methods for all page objects
 * Encapsulates Playwright operations and provides a foundation for POM pattern
 */
public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected Page page;

    public BasePage() {
        this.page = PlaywrightFactory.getPage();
    }

    /**
     * Navigate to page URL
     */
    public void navigate(String url) {
        page.navigate(url);
        logger.info("Navigated to: {}", url);
    }

    /**
     * Click element by selector
     */
    protected void click(String selector) {
        WaitHelper.waitForElementVisible(selector);
        page.locator(selector).click();
        logger.debug("Clicked element: {}", selector);
    }

    /**
     * Type text into input field
     */
    protected void typeText(String selector, String text) {
        WaitHelper.waitForElementVisible(selector);
        page.locator(selector).fill(text);
        logger.debug("Typed text in element: {}", selector);
    }

    /**
     * Clear text from input field
     */
    protected void clearText(String selector) {
        WaitHelper.waitForElementVisible(selector);
        page.locator(selector).clear();
        logger.debug("Cleared text from element: {}", selector);
    }

    /**
     * Get element text
     */
    protected String getText(String selector) {
        WaitHelper.waitForElementVisible(selector);
        String text = page.locator(selector).textContent();
        logger.debug("Retrieved text from element: {} = {}", selector, text);
        return text;
    }

    /**
     * Get element attribute
     */
    protected String getAttribute(String selector, String attribute) {
        String value = page.locator(selector).getAttribute(attribute);
        logger.debug("Retrieved attribute {} from element: {} = {}", attribute, selector, value);
        return value;
    }

    /**
     * Check if element is visible
     */
    protected boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    /**
     * Select dropdown option by text
     */
    protected void selectDropdownOption(String selector, String optionText) {
        page.locator(selector).selectOption(optionText);
        logger.debug("Selected dropdown option: {}", optionText);
    }

    /**
     * Check/Uncheck checkbox
     */
    protected void setCheckbox(String selector, boolean checked) {
        page.locator(selector).setChecked(checked);
        logger.debug("Set checkbox {} to: {}", selector, checked);
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return page.url();
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return page.title();
    }

    /**
     * Wait for page load
     */
    protected void waitForPageLoad() {
        WaitHelper.waitForNavigation();
        logger.info("Page loaded successfully");
    }

    /**
     * Switch to iframe
     */
    protected void switchToFrame(String frameSelector) {
        page.frameLocator(frameSelector);
        logger.debug("Switched to frame: {}", frameSelector);
    }

    /**
     * Take screenshot
     */
    public void takeScreenshot(String name) {
        PlaywrightFactory.takeScreenshot(name);
    }

    /**
     * Execute JavaScript
     */
    protected Object executeScript(String script) {
        return page.evaluate(script);
    }

    /**
     * Scroll element into view
     */
    protected void scrollToElement(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
        logger.debug("Scrolled to element: {}", selector);
    }
}
