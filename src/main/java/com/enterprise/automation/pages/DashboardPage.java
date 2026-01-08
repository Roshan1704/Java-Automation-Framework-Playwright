package com.enterprise.automation.pages;

import com.enterprise.automation.util.AssertionHelper;
import com.enterprise.automation.util.WaitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dashboard Page Object - Advanced example with dynamic elements
 */
public class DashboardPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(DashboardPage.class);

    // Selectors
    private static final String DASHBOARD_TITLE = "h1";
    private static final String SIDEBAR = "nav.sidebar";
    private static final String MENU_ITEMS = "nav.sidebar a";
    private static final String SEARCH_BOX = "input[placeholder='Search...']";
    private static final String TABLE_ROWS = "table tbody tr";
    private static final String LOAD_MORE_BUTTON = "button[data-action='load-more']";

    public DashboardPage() {
        super();
    }

    /**
     * Verify dashboard title
     */
    public void verifyDashboardTitle(String expectedTitle) {
        AssertionHelper.assertElementText(DASHBOARD_TITLE, expectedTitle);
        logger.info("Dashboard title verified: {}", expectedTitle);
    }

    /**
     * Search for item
     */
    public DashboardPage searchForItem(String searchTerm) {
        typeText(SEARCH_BOX, searchTerm);
        WaitHelper.waitForNavigation();
        logger.info("Searched for: {}", searchTerm);
        return this;
    }

    /**
     * Click menu item by text
     */
    public DashboardPage clickMenuItemByText(String menuText) {
        page.locator(MENU_ITEMS + ":has-text('" + menuText + "')").click();
        WaitHelper.waitForNavigation();
        logger.info("Clicked menu item: {}", menuText);
        return this;
    }

    /**
     * Get table row count
     */
    public int getTableRowCount() {
        WaitHelper.waitForElementVisible(TABLE_ROWS);
        int count = page.locator(TABLE_ROWS).count();
        logger.info("Table row count: {}", count);
        return count;
    }

    /**
     * Verify table has rows
     */
    public void verifyTableHasRows() {
        AssertionHelper.assertTrue(getTableRowCount() > 0, "Table should have at least one row");
        logger.info("Table has rows verified");
    }

    /**
     * Load more results
     */
    public DashboardPage clickLoadMore() {
        click(LOAD_MORE_BUTTON);
        WaitHelper.waitForNavigation();
        logger.info("Clicked load more button");
        return this;
    }
}
