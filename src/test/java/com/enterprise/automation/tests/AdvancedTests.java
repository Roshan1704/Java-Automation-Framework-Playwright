package com.enterprise.automation.tests;

import com.enterprise.automation.browser.BrowserContextManager;
import com.enterprise.automation.browser.PlaywrightFactory;
import com.microsoft.playwright.BrowserContext;
import io.qameta.allure.*;
import org.testng.annotations.Test;

/**
 * Advanced Playwright Testing Patterns
 */
@Feature("Advanced Testing")
public class AdvancedTests extends BaseTest {

    @Test(description = "Test multi-context scenarios")
    @Story("Advanced Context Management")
    @Severity(SeverityLevel.MINOR)
    public void testMultiContextScenario() {
        // Create first context
        BrowserContext context1 = BrowserContextManager.createNamedContext("admin");
        BrowserContext context2 = BrowserContextManager.createNamedContext("user");

        // Switch between contexts
        BrowserContextManager.switchContext("admin");
        logger.info("Switched to admin context");

        BrowserContextManager.switchContext("user");
        logger.info("Switched to user context");

        BrowserContextManager.clearAllContexts();
        logger.info("All contexts cleared");
    }

    @Test(description = "Test network interception")
    @Story("Network Mocking")
    @Severity(SeverityLevel.CRITICAL)
    public void testNetworkInterception() {
        BrowserContextManager.mockApiResponse(
                "https://api.example.com/users",
                "{\"data\": [{\"id\": 1, \"name\": \"Test User\"}]}",
                200
        );
        
        PlaywrightFactory.getPage().navigate("https://example.com");
        logger.info("Network interception test completed");
    }

    @Test(description = "Test screenshot and trace recording")
    @Story("Debugging Features")
    @Severity(SeverityLevel.MINOR)
    public void testScreenshotAndTrace() {
        PlaywrightFactory.getPage().navigate("https://example.com");
        PlaywrightFactory.takeScreenshot("page-snapshot");
        PlaywrightFactory.saveTrace("page-trace");
        logger.info("Screenshot and trace saved");
    }

    @Test(description = "Test JavaScript execution")
    @Story("Page Interaction")
    @Severity(SeverityLevel.MINOR)
    public void testJavaScriptExecution() {
        PlaywrightFactory.getPage().navigate("https://example.com");
        Object result = PlaywrightFactory.getPage().evaluate("() => document.title");
        logger.info("Page title via JavaScript: {}", result);
    }
}
