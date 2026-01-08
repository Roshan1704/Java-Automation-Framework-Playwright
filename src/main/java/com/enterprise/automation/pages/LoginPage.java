package com.enterprise.automation.pages;

import com.enterprise.automation.config.Configuration;
import com.enterprise.automation.util.AssertionHelper;
import com.enterprise.automation.util.WaitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Login Page Object - Example implementation
 * Demonstrates modern POM pattern with fluent API and action methods
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Selectors
    private static final String EMAIL_INPUT = "input[name='email']";
    private static final String PASSWORD_INPUT = "input[name='password']";
    private static final String LOGIN_BUTTON = "button[type='submit']";
    private static final String ERROR_MESSAGE = ".error-message";
    private static final String REMEMBER_ME_CHECKBOX = "input[name='rememberMe']";
    private static final String FORGOT_PASSWORD_LINK = "a[href='/forgot-password']";

    public LoginPage() {
        super();
    }

    /**
     * Navigate to login page
     */
    public LoginPage navigateToLoginPage() {
        String loginUrl = Configuration.getInstance().getBaseUrl() + "/login";
        navigate(loginUrl);
        logger.info("Navigated to login page");
        return this;
    }

    /**
     * Enter email address
     */
    public LoginPage enterEmail(String email) {
        typeText(EMAIL_INPUT, email);
        logger.info("Entered email: {}", email);
        return this;
    }

    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        typeText(PASSWORD_INPUT, password);
        logger.info("Entered password");
        return this;
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
        logger.info("Clicked login button");
    }

    /**
     * Perform login action (fluent API)
     */
    public void login(String email, String password) {
        enterEmail(email)
                .enterPassword(password);
        clickLoginButton();
        logger.info("Login completed for user: {}", email);
    }

    /**
     * Set remember me checkbox
     */
    public LoginPage setRememberMe(boolean remember) {
        setCheckbox(REMEMBER_ME_CHECKBOX, remember);
        return this;
    }

    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        click(FORGOT_PASSWORD_LINK);
        logger.info("Clicked forgot password link");
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        WaitHelper.waitForElementVisible(ERROR_MESSAGE);
        return getText(ERROR_MESSAGE);
    }

    /**
     * Verify login page elements are present
     */
    public void verifyLoginPageElements() {
        AssertionHelper.assertElementVisible(EMAIL_INPUT);
        AssertionHelper.assertElementVisible(PASSWORD_INPUT);
        AssertionHelper.assertElementVisible(LOGIN_BUTTON);
        logger.info("Login page elements verified");
    }

    /**
     * Verify error message is displayed
     */
    public void verifyErrorMessage(String expectedMessage) {
        AssertionHelper.assertElementVisible(ERROR_MESSAGE);
        AssertionHelper.assertElementText(ERROR_MESSAGE, expectedMessage);
        logger.info("Error message verified: {}", expectedMessage);
    }
}
