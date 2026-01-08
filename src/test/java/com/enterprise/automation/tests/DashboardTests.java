package com.enterprise.automation.tests;

import com.enterprise.automation.pages.DashboardPage;
import com.enterprise.automation.pages.HomePage;
import com.enterprise.automation.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Dashboard Test Cases
 */
@Feature("Dashboard")
public class DashboardTests extends BaseTest {

    private HomePage homePage;
    private DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToLoginPage();
        loginPage.login("user@example.com", "Password123");
        
        homePage = new HomePage();
        homePage.verifyHomePageLoaded();
        
        dashboardPage = new DashboardPage();
        logger.info("User logged in successfully");
    }

    @Test(description = "Verify dashboard title")
    @Story("Dashboard Navigation")
    @Severity(SeverityLevel.MINOR)
    public void testDashboardTitle() {
        dashboardPage.verifyDashboardTitle("Dashboard");
        logger.info("Test passed: Dashboard title verified");
    }

    @Test(description = "Verify search functionality")
    @Story("Dashboard Search")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchFunctionality() {
        dashboardPage.searchForItem("test item");
        dashboardPage.verifyTableHasRows();
        logger.info("Test passed: Search functionality working");
    }

    @Test(description = "Verify table pagination")
    @Story("Dashboard Table")
    @Severity(SeverityLevel.MINOR)
    public void testTablePagination() {
        int initialRowCount = dashboardPage.getTableRowCount();
        dashboardPage.clickLoadMore();
        int updatedRowCount = dashboardPage.getTableRowCount();
        
        assert updatedRowCount >= initialRowCount : "Row count should increase after load more";
        logger.info("Test passed: Pagination works - Initial: {}, Updated: {}", initialRowCount, updatedRowCount);
    }

    @Test(description = "Verify menu navigation")
    @Story("Dashboard Navigation")
    @Severity(SeverityLevel.CRITICAL)
    public void testMenuNavigation() {
        dashboardPage.clickMenuItemByText("Reports");
        dashboardPage.verifyDashboardTitle("Reports");
        logger.info("Test passed: Menu navigation successful");
    }
}
