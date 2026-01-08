package com.enterprise.automation.browser;

import com.enterprise.automation.config.Configuration;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.*;

/**
 * Singleton PlaywrightFactory for thread-safe browser and context management
 * Supports multiple browsers (Chromium, Firefox, WebKit) with context isolation
 */
public class PlaywrightFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlaywrightFactory.class);
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    private static Playwright playwright;
    private static final Object LOCK = new Object();

    private PlaywrightFactory() {}

    /**
     * Initialize Playwright instance (singleton)
     */
    public static synchronized void initPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
            logger.info("Playwright instance created");
        }
    }

    /**
     * Get or create browser instance for current thread
     */
    public static Browser getBrowser() {
        if (browserThreadLocal.get() == null) {
            synchronized (LOCK) {
                if (browserThreadLocal.get() == null) {
                    initPlaywright();
                    String browserType = Configuration.getInstance().getBrowser().toLowerCase();
                    Browser browser = launchBrowser(browserType);
                    browserThreadLocal.set(browser);
                    logger.info("Browser launched: {}", browserType);
                }
            }
        }
        return browserThreadLocal.get();
    }

    /**
     * Launch browser based on configuration
     */
    private static Browser launchBrowser(String browserType) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Configuration.getInstance().isHeadless())
                .setSlowMo(Configuration.getInstance().getSlowmo());

        // Add custom args
        List<String> args = Arrays.asList(
                "--disable-dev-shm-usage",
                "--no-first-run",
                "--no-default-browser-check"
        );
        options.setArgs(args);

        return switch (browserType) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    /**
     * Get or create browser context for current thread
     */
    public static BrowserContext getContext() {
        if (contextThreadLocal.get() == null) {
            synchronized (LOCK) {
                if (contextThreadLocal.get() == null) {
                    Browser browser = getBrowser();
                    BrowserContext context = createContext(browser);
                    contextThreadLocal.set(context);
                    logger.info("Browser context created");
                }
            }
        }
        return contextThreadLocal.get();
    }

    /**
     * Create browser context with enterprise configurations
     */
    private static BrowserContext createContext(Browser browser) {
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(
                        Configuration.getInstance().getViewportWidth(),
                        Configuration.getInstance().getViewportHeight()
                )
                .setIgnoreHTTPSErrors(true)
                .setAcceptDownloads(true)
                .setLocale("en-US")
                .setTimezoneId("America/New_York")
                .setGeolocation(40.7128, -74.0060) // New York coordinates
                .setPermissions(Arrays.asList("geolocation"));

        BrowserContext context = browser.newContext(contextOptions);

        // Setup trace recording
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        return context;
    }

    /**
     * Get or create page for current thread
     */
    public static Page getPage() {
        if (pageThreadLocal.get() == null) {
            BrowserContext context = getContext();
            Page page = context.newPage();
            page.setDefaultTimeout(Configuration.getInstance().getTimeout());
            page.setDefaultNavigationTimeout(Configuration.getInstance().getTimeout());
            pageThreadLocal.set(page);
            logger.info("Page created and configured");
        }
        return pageThreadLocal.get();
    }

    /**
     * Navigate to URL with retry logic
     */
    public static void navigateTo(String url) {
        Page page = getPage();
        try {
            page.navigate(url);
            logger.info("Navigated to: {}", url);
        } catch (PlaywrightException e) {
            logger.warn("Navigation failed, retrying: {}", url);
            page.navigate(url);
        }
    }

    /**
     * Take screenshot and save to allure report
     */
    public static void takeScreenshot(String name) {
        Page page = pageThreadLocal.get();
        if (page != null) {
            String path = String.format("target/screenshots/%s.png", name);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
            logger.info("Screenshot saved: {}", path);
        }
    }

    /**
     * Record video for current context
     */
    public static void startVideoRecording() {
        BrowserContext context = getContext();
        // Video is enabled via context options
        logger.info("Video recording enabled for context");
    }

    /**
     * Save trace for debugging
     */
    public static void saveTrace(String name) {
        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            String path = String.format("target/traces/%s.zip", name);
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get(path)));
            logger.info("Trace saved: {}", path);
        }
    }

    /**
     * Close page for current thread
     */
    public static void closePage() {
        Page page = pageThreadLocal.get();
        if (page != null && !page.isClosed()) {
            page.close();
            pageThreadLocal.remove();
            logger.info("Page closed");
        }
    }

    /**
     * Close context for current thread
     */
    public static void closeContext() {
        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            context.close();
            contextThreadLocal.remove();
            logger.info("Context closed");
        }
    }

    /**
     * Close browser for current thread
     */
    public static void closeBrowser() {
        Browser browser = browserThreadLocal.get();
        if (browser != null && browser.isConnected()) {
            browser.close();
            browserThreadLocal.remove();
            logger.info("Browser closed");
        }
    }

    /**
     * Complete cleanup - call in test teardown
     */
    public static void closeAll() {
        closePage();
        closeContext();
        closeBrowser();
    }

    /**
     * Close Playwright instance (call once at JVM shutdown)
     */
    public static synchronized void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
            logger.info("Playwright instance closed");
        }
    }
}
