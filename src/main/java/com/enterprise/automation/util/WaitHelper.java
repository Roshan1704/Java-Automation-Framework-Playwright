package com.enterprise.automation.util;

import com.enterprise.automation.browser.PlaywrightFactory;
import com.enterprise.automation.config.Configuration;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Advanced wait utilities leveraging Playwright's built-in auto-waiting
 * Provides explicit waits for complex scenarios beyond Playwright's automatic waiting
 */
public class WaitHelper {
    private static final Logger logger = LoggerFactory.getLogger(WaitHelper.class);
    private static final int DEFAULT_TIMEOUT = Configuration.getInstance().getTimeout();

    private WaitHelper() {}

    /**
     * Wait for element to be visible
     */
    public static void waitForElementVisible(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.waitForSelector(selector);
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(DEFAULT_TIMEOUT));
        logger.info("Element visible: {}", selector);
    }

    /**
     * Wait for element to be hidden
     */
    public static void waitForElementHidden(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(DEFAULT_TIMEOUT));
        logger.info("Element hidden: {}", selector);
    }

    /**
     * Wait for element to be enabled
     */
    public static void waitForElementEnabled(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.waitForFunction(
            "selector => !document.querySelector(selector).disabled",
            selector
        );
        logger.info("Element enabled: {}", selector);
    }

    public static void waitForElementDisabled(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.waitForFunction(
            "selector => document.querySelector(selector).disabled",
            selector
        );
        logger.info("Element disabled: {}", selector);
    }

    /**
     * Wait for URL to match pattern
     */
    public static void waitForURL(String urlPattern) {
        Page page = PlaywrightFactory.getPage();
        page.waitForURL(urlPattern);
        logger.info("URL matched: {}", urlPattern);
    }

    /**
     * Wait for navigation to complete
     */
    public static void waitForNavigation() {
        Page page = PlaywrightFactory.getPage();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logger.info("Navigation completed");
    }

    /**
     * Wait for specific load state
     */
    public static void waitForLoadState(LoadState state) {
        Page page = PlaywrightFactory.getPage();
        page.waitForLoadState(state);
        logger.info("Load state reached: {}", state);
    }

    /**
     * Wait for function to return true (polling)
     */
    public static void waitForFunction(String script, int timeoutMs) {
        Page page = PlaywrightFactory.getPage();
        page.waitForFunction(script);
        logger.info("Wait for function completed");
    }

    /**
     * Wait for element count
     */
    public static void waitForElementCount(String selector, int expectedCount) {
        Page page = PlaywrightFactory.getPage();
        long startTime = System.currentTimeMillis();
        int count = 0;
        
        while ((System.currentTimeMillis() - startTime) < DEFAULT_TIMEOUT) {
            count = page.locator(selector).count();
            if (count == expectedCount) {
                logger.info("Expected element count {} matched for: {}", expectedCount, selector);
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        throw new AssertionError("Element count " + count + " did not match expected " + expectedCount);
    }

    /**
     * Custom explicit wait with polling
     */
    public static void waitUntil(WaitCondition condition, int timeoutMs) {
        long startTime = System.currentTimeMillis();
        
        while ((System.currentTimeMillis() - startTime) < timeoutMs) {
            try {
                if (condition.isSatisfied()) {
                    logger.info("Wait condition satisfied");
                    return;
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        throw new AssertionError("Wait condition not satisfied within " + timeoutMs + "ms");
    }

    @FunctionalInterface
    public interface WaitCondition {
        boolean isSatisfied();
    }
}
