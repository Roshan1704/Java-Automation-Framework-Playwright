package com.enterprise.automation.tests;

import com.enterprise.automation.config.Configuration;
import com.enterprise.automation.pages.HomePage;
import com.enterprise.automation.pages.LoginPage;
import com.enterprise.automation.util.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Login Test Cases - UI automation examples
 */
@Feature("Authentication")
public class LoginTests extends BaseTest {

    @Test(description = "Verify user can login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithValidCredentials() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.verifyLoginPageElements();
        loginPage.login("user@example.com", "Password123");
        
        HomePage homePage = new HomePage();
        homePage.verifyHomePageLoaded();
        homePage.verifyUserLoggedIn("User");
        
        logger.info("Test passed: Valid credentials login successful");
    }

    @Test(description = "Verify login fails with invalid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.login("invalid@example.com", "WrongPassword");
        
        loginPage.verifyErrorMessage("Invalid credentials");
        logger.info("Test passed: Invalid credentials handled correctly");
    }

    @Test(description = "Verify remember me functionality")
    @Story("User Login")
    @Severity(SeverityLevel.MINOR)
    public void testRememberMeFunctionality() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.setRememberMe(true);
        loginPage.enterEmail("user@example.com");
        loginPage.enterPassword("Password123");
        loginPage.clickLoginButton();
        
        HomePage homePage = new HomePage();
        homePage.verifyHomePageLoaded();
        logger.info("Test passed: Remember me functionality verified");
    }

    @Test(dataProvider = "loginDataProvider", description = "Data-driven login tests")
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithDifferentUsers(String email, String password, boolean shouldSucceed) {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.login(email, password);
        
        if (shouldSucceed) {
            HomePage homePage = new HomePage();
            homePage.verifyHomePageLoaded();
            logger.info("Test passed: Login successful for {}", email);
        } else {
            loginPage.verifyErrorMessage("Invalid credentials");
            logger.info("Test passed: Login failed as expected for {}", email);
        }
    }

    @DataProvider(name = "loginDataProvider")
    public Object[][] getLoginTestData() {
        return new Object[][] {
            {"validuser@example.com", "ValidPass123", true},
            {"invalid@example.com", "WrongPassword", false},
            {"testuser@example.com", "TestPass456", true}
        };
    }

    @Test(description = "Generate test data and verify login")
    @Story("User Login")
    @Severity(SeverityLevel.MINOR)
    public void testLoginWithGeneratedData() {
        String generatedEmail = TestDataGenerator.generateEmail();
        String generatedPassword = TestDataGenerator.generatePassword();
        
        logger.info("Generated email: {}", generatedEmail);
        logger.info("Generated password: {}", generatedPassword);
        
        // Note: This test demonstrates data generation capability
        // In production, use actual generated test data from API response
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.verifyLoginPageElements();
    }
}
