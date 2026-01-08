package com.enterprise.automation.pages;

import com.enterprise.automation.config.Configuration;
import com.enterprise.automation.util.AssertionHelper;
import com.enterprise.automation.util.WaitHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Home Page Object - Example implementation
 */
public class HomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    // Selectors
    private static final String HEADER = "header";
    private static final String USER_MENU = "button[aria-label='user-menu']";
    private static final String LOGOUT_BUTTON = "a[href='/logout']";
    private static final String WELCOME_MESSAGE = ".welcome-text";
    private static final String DASHBOARD_CONTENT = ".dashboard-content";

    public HomePage() {
        super();
    }

    /**
     * Navigate to home page
     */
    public HomePage navigateToHomePage() {
        String homeUrl = Configuration.getInstance().getBaseUrl();
        navigate(homeUrl);
        logger.info("Navigated to home page");
        return this;
    }

    /**
     * Verify home page loaded
     */
    public void verifyHomePageLoaded() {
        WaitHelper.waitForElementVisible(DASHBOARD_CONTENT);
        AssertionHelper.assertElementVisible(HEADER);
        logger.info("Home page loaded successfully");
    }

    /**
     * Get welcome message
     */
    public String getWelcomeMessage() {
        return getText(WELCOME_MESSAGE);
    }

    /**
     * Click user menu
     */
    public HomePage clickUserMenu() {
        click(USER_MENU);
        logger.info("Clicked user menu");
        return this;
    }

    /**
     * Logout
     */
    public void logout() {
        clickUserMenu();
        click(LOGOUT_BUTTON);
        logger.info("Logged out successfully");
    }

    /**
     * Verify user is logged in
     */
    public void verifyUserLoggedIn(String userName) {
        String welcomeMsg = getWelcomeMessage();
        AssertionHelper.assertElementTextContains(WELCOME_MESSAGE, userName);
        logger.info("User verified logged in: {}", userName);
    }
}
