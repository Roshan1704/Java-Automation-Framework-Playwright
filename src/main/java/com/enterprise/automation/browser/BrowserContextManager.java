package com.enterprise.automation.browser;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Advanced context management for complex scenarios
 * Supports multiple contexts, session management, and network interception
 */
public class BrowserContextManager {
    private static final Logger logger = LoggerFactory.getLogger(BrowserContextManager.class);
    private static final ThreadLocal<Map<String, BrowserContext>> contextMap = ThreadLocal.withInitial(HashMap::new);
    private static final ThreadLocal<String> activeContext = ThreadLocal.withInitial(() -> "default");

    private BrowserContextManager() {}

    /**
     * Create named context for specific test scenarios
     */
    public static BrowserContext createNamedContext(String contextName) {
        Map<String, BrowserContext> contexts = contextMap.get();
        
        BrowserContext context = PlaywrightFactory.getBrowser()
                .newContext(new Browser.NewContextOptions()
                        .setViewportSize(1920, 1080)
                        .setIgnoreHTTPSErrors(true)
                        .setAcceptDownloads(true)
                );
        
        contexts.put(contextName, context);
        logger.info("Named context created: {}", contextName);
        return context;
    }

    /**
     * Switch active context
     */
    public static void switchContext(String contextName) {
        Map<String, BrowserContext> contexts = contextMap.get();
        if (contexts.containsKey(contextName)) {
            activeContext.set(contextName);
            logger.info("Switched to context: {}", contextName);
        } else {
            logger.warn("Context not found: {}", contextName);
        }
    }

    /**
     * Get active context
     */
    public static BrowserContext getActiveContext() {
        Map<String, BrowserContext> contexts = contextMap.get();
        String active = activeContext.get();
        return contexts.getOrDefault(active, PlaywrightFactory.getContext());
    }

    /**
     * Setup network interception for API mocking
     */
    public static void interceptNetworkRequests(String urlPattern, String responseBody) {
        BrowserContext context = getActiveContext();
        Page page = context.newPage();
        
        page.route(urlPattern, route -> {
            route.abort();
            logger.info("Request intercepted and aborted: {}", urlPattern);
        });
    }

    /**
     * Mock API responses
     */
    public static void mockApiResponse(String urlPattern, String responseJson, int statusCode) {
        BrowserContext context = getActiveContext();
        Page page = context.newPage();
        
        page.route(urlPattern, route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(statusCode)
                    .setContentType("application/json")
                    .setBody(responseJson)
            );
        });
        
        logger.info("API response mocked for: {} with status: {}", urlPattern, statusCode);
    }

    /**
     * Clear all contexts
     */
    public static void clearAllContexts() {
        Map<String, BrowserContext> contexts = contextMap.get();
        contexts.values().forEach(BrowserContext::close);
        contexts.clear();
        contextMap.remove();
        activeContext.remove();
        logger.info("All contexts cleared");
    }
}
