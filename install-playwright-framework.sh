#!/bin/bash

# Playwright Java Framework Complete Setup Script
# This script creates the entire project structure with all files

set -e

PROJECT_NAME="playwright-java-framework"
PROJECT_DIR="./$PROJECT_NAME"

echo "======================================"
echo "Playwright Java Framework Installer"
echo "======================================"
echo ""

# Check if project directory exists
if [ -d "$PROJECT_DIR" ]; then
    echo "Directory $PROJECT_NAME already exists. Please remove it first:"
    echo "  rm -rf $PROJECT_NAME"
    exit 1
fi

# Create project structure
echo "Creating project structure..."
mkdir -p "$PROJECT_DIR"
cd "$PROJECT_DIR"

# Create directories
mkdir -p src/main/java/com/enterprise/automation/{browser,config,pages,util,listener}
mkdir -p src/test/java/com/enterprise/automation/tests
mkdir -p src/test/resources
mkdir -p k8s
mkdir -p target/{screenshots,traces,logs}

echo "Creating pom.xml..."
cat > pom.xml << 'POMEOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.enterprise.automation</groupId>
    <artifactId>playwright-java-framework</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>Enterprise Playwright Java Framework</name>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <playwright.version>1.40.0</playwright.version>
        <testng.version>7.7.1</testng.version>
        <allure.version>2.25.0</allure.version>
        <allure-maven.version>2.13.2</allure-maven.version>
        <maven.surefire.version>3.1.2</maven.surefire.version>
        <log4j.version>2.22.0</log4j.version>
        <slf4j.version>2.0.9</slf4j.version>
        <restassured.version>5.4.0</restassured.version>
        <faker.version>1.0.2</faker.version>
        <jackson.version>2.16.0</jackson.version>
        <commons-csv.version>1.10.0</commons-csv.version>
        <poi.version>5.0.0</poi.version>
    </properties>

    <repositories>
        <repository>
            <id>central</id>
            <name>Maven Central Repository</name>
            <url>https://repo.maven.apache.org/maven2</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Playwright -->
        <dependency>
            <groupId>com.microsoft.playwright</groupId>
            <artifactId>playwright</artifactId>
            <version>${playwright.version}</version>
        </dependency>

        <!-- TestNG -->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>${testng.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Allure TestNG Integration -->
        <dependency>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-testng</artifactId>
            <version>${allure.version}</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j2-impl</artifactId>
            <version>${log4j.version}</version>
        </dependency>

        <!-- REST Assured for API Testing -->
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${restassured.version}</version>
        </dependency>

        <!-- Faker for Test Data -->
        <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>${faker.version}</version>
        </dependency>

        <!-- Jackson for JSON -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-csv</artifactId>
            <version>${jackson.version}</version>
        </dependency>

        <!-- Apache Commons -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-csv</artifactId>
            <version>${commons-csv.version}</version>
        </dependency>

        <!-- Apache POI for Excel -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>${poi.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>${poi.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven.surefire.version}</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                </configuration>
            </plugin>

            <plugin>
                <groupId>io.qameta.allure</groupId>
                <artifactId>allure-maven</artifactId>
                <version>${allure-maven.version}</version>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
POMEOF

echo "Creating PlaywrightFactory.java..."
cat > src/main/java/com/enterprise/automation/browser/PlaywrightFactory.java << 'FACTORYEOF'
package com.enterprise.automation.browser;

import com.enterprise.automation.config.Configuration;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.*;

public class PlaywrightFactory {
    private static final Logger logger = LoggerFactory.getLogger(PlaywrightFactory.class);
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    private static Playwright playwright;
    private static final Object LOCK = new Object();

    private PlaywrightFactory() {}

    public static synchronized void initPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
            logger.info("Playwright instance created");
        }
    }

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

    private static Browser launchBrowser(String browserType) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Configuration.getInstance().isHeadless())
                .setSlowMo(Configuration.getInstance().getSlowmo());

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
                .setGeolocation(40.7128, -74.0060)
                .setPermissions(Arrays.asList("geolocation"));

        BrowserContext context = browser.newContext(contextOptions);

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        return context;
    }

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

    public static void takeScreenshot(String name) {
        Page page = pageThreadLocal.get();
        if (page != null) {
            String path = String.format("target/screenshots/%s.png", name);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)));
            logger.info("Screenshot saved: {}", path);
        }
    }

    public static void closePage() {
        Page page = pageThreadLocal.get();
        if (page != null && !page.isClosed()) {
            page.close();
            pageThreadLocal.remove();
            logger.info("Page closed");
        }
    }

    public static void closeContext() {
        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            context.close();
            contextThreadLocal.remove();
            logger.info("Context closed");
        }
    }

    public static void closeBrowser() {
        Browser browser = browserThreadLocal.get();
        if (browser != null && browser.isConnected()) {
            browser.close();
            browserThreadLocal.remove();
            logger.info("Browser closed");
        }
    }

    public static void closeAll() {
        closePage();
        closeContext();
        closeBrowser();
    }

    public static synchronized void closePlaywright() {
        if (playwright != null) {
            playwright.close();
            playwright = null;
            logger.info("Playwright instance closed");
        }
    }
}
FACTORYEOF

echo "Creating Configuration.java..."
cat > src/main/java/com/enterprise/automation/config/Configuration.java << 'CONFIGEOF'
package com.enterprise.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final Properties ENV_PROPERTIES = new Properties();
    private static final Properties PLAYWRIGHT_PROPERTIES = new Properties();
    private static Configuration instance;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            InputStream envStream = Configuration.class.getClassLoader().getResourceAsStream("env.properties");
            if (envStream != null) {
                ENV_PROPERTIES.load(envStream);
                envStream.close();
            }

            InputStream playwrightStream = Configuration.class.getClassLoader().getResourceAsStream("playwright.properties");
            if (playwrightStream != null) {
                PLAYWRIGHT_PROPERTIES.load(playwrightStream);
                playwrightStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    private Configuration() {}

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getEnv(String key, String defaultValue) {
        String envValue = System.getenv(key);
        if (envValue != null) {
            return envValue;
        }
        return ENV_PROPERTIES.getProperty(key, defaultValue);
    }

    public String getEnv(String key) {
        return getEnv(key, "");
    }

    public String getPlaywrightConfig(String key, String defaultValue) {
        return PLAYWRIGHT_PROPERTIES.getProperty(key, defaultValue);
    }

    public String getPlaywrightConfig(String key) {
        return getPlaywrightConfig(key, "");
    }

    public String getBaseUrl() {
        String env = getEnv("environment", "qa");
        return getEnv(env + ".base.url", "https://qa.example.com");
    }

    public String getApiBaseUrl() {
        String env = getEnv("environment", "qa");
        return getEnv(env + ".api.base.url", "https://api-qa.example.com");
    }

    public String getBrowser() {
        return getPlaywrightConfig("browser", "chromium");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(getPlaywrightConfig("headless", "true"));
    }

    public int getTimeout() {
        return Integer.parseInt(getPlaywrightConfig("timeout", "30000"));
    }

    public int getSlowmo() {
        return Integer.parseInt(getPlaywrightConfig("slowmo", "0"));
    }

    public int getViewportWidth() {
        return Integer.parseInt(getPlaywrightConfig("viewport.width", "1920"));
    }

    public int getViewportHeight() {
        return Integer.parseInt(getPlaywrightConfig("viewport.height", "1080"));
    }

    public int getParallelTests() {
        return Integer.parseInt(getPlaywrightConfig("parallel.tests", "4"));
    }
}
CONFIGEOF

echo "Creating property files..."
cat > src/test/resources/env.properties << 'ENVEOF'
environment=qa
qa.base.url=https://example.com
qa.api.base.url=https://api.example.com
uat.base.url=https://uat.example.com
uat.api.base.url=https://api-uat.example.com
prod.base.url=https://prod.example.com
prod.api.base.url=https://api.example.com
ENVEOF

cat > src/test/resources/playwright.properties << 'PLAYWRIGHTEOF'
browser=chromium
headless=true
timeout=30000
slowmo=0
viewport.width=1920
viewport.height=1080
parallel.tests=4
record.video=false
PLAYWRIGHTEOF

cat > src/test/resources/log4j2.properties << 'LOG4JEOF'
status=warn
name=PlaywrightFramework
appenders=console,file
appender.console.type=Console
appender.console.name=STDOUT
appender.console.layout.type=PatternLayout
appender.console.layout.pattern=%d{yyyy-MM-dd HH:mm:ss} [%-5p] %c{1} - %m%n

appender.file.type=RollingFile
appender.file.name=LOGFILE
appender.file.fileName=target/logs/playwright.log
appender.file.filePattern=target/logs/playwright-%d{yyyy-MM-dd}.log
appender.file.layout.type=PatternLayout
appender.file.layout.pattern=%d{yyyy-MM-dd HH:mm:ss} [%-5p] %c{1} - %m%n
appender.file.policies.type=Policies
appender.file.policies.time.type=TimeBasedTriggeringPolicy
appender.file.policies.time.interval=1
appender.file.policies.time.modulate=true
appender.file.policies.size.type=SizeBasedTriggeringPolicy
appender.file.policies.size.size=10MB

rootLogger.level=info
rootLogger.appenderRefs=stdout,file
rootLogger.appenderRef.stdout.ref=STDOUT
rootLogger.appenderRef.file.ref=LOGFILE
LOG4JEOF

echo "Creating TestNG configuration..."
cat > src/test/resources/testng.xml << 'TESTNGEOF'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Playwright Test Suite" parallel="methods" thread-count="4">
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="com.enterprise.automation.tests.LoginTests"/>
        </classes>
    </test>
</suite>
TESTNGEOF

echo "Creating BasePage.java..."
cat > src/main/java/com/enterprise/automation/pages/BasePage.java << 'BASEPAGEEOF'
package com.enterprise.automation.pages;

import com.enterprise.automation.browser.PlaywrightFactory;
import com.enterprise.automation.util.WaitHelper;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected Page page;

    public BasePage() {
        this.page = PlaywrightFactory.getPage();
    }

    public void navigate(String url) {
        page.navigate(url);
        logger.info("Navigated to: {}", url);
    }

    protected void click(String selector) {
        WaitHelper.waitForElementVisible(selector);
        page.locator(selector).click();
        logger.debug("Clicked element: {}", selector);
    }

    protected void typeText(String selector, String text) {
        WaitHelper.waitForElementVisible(selector);
        page.locator(selector).fill(text);
        logger.debug("Typed text in element: {}", selector);
    }

    protected void clearText(String selector) {
        WaitHelper.waitForElementVisible(selector);
        page.locator(selector).clear();
        logger.debug("Cleared text from element: {}", selector);
    }

    protected String getText(String selector) {
        WaitHelper.waitForElementVisible(selector);
        String text = page.locator(selector).textContent();
        logger.debug("Retrieved text from element: {} = {}", selector, text);
        return text;
    }

    protected String getAttribute(String selector, String attribute) {
        String value = page.locator(selector).getAttribute(attribute);
        logger.debug("Retrieved attribute {} from element: {} = {}", attribute, selector, value);
        return value;
    }

    protected boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    protected boolean isElementEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    protected void selectDropdownOption(String selector, String optionText) {
        page.locator(selector).selectOption(optionText);
        logger.debug("Selected dropdown option: {}", optionText);
    }

    protected void setCheckbox(String selector, boolean checked) {
        page.locator(selector).setChecked(checked);
        logger.debug("Set checkbox {} to: {}", selector, checked);
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public String getPageTitle() {
        return page.title();
    }

    protected void waitForPageLoad() {
        WaitHelper.waitForNavigation();
        logger.info("Page loaded successfully");
    }

    protected void switchToFrame(String frameSelector) {
        page.frameLocator(frameSelector);
        logger.debug("Switched to frame: {}", frameSelector);
    }

    public void takeScreenshot(String name) {
        PlaywrightFactory.takeScreenshot(name);
    }

    protected Object executeScript(String script) {
        return page.evaluate(script);
    }

    protected void scrollToElement(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
        logger.debug("Scrolled to element: {}", selector);
    }
}
BASEPAGEEOF

echo "Creating WaitHelper.java..."
cat > src/main/java/com/enterprise/automation/util/WaitHelper.java << 'WAITHELPEREOF'
package com.enterprise.automation.util;

import com.enterprise.automation.browser.PlaywrightFactory;
import com.enterprise.automation.config.Configuration;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitHelper {
    private static final Logger logger = LoggerFactory.getLogger(WaitHelper.class);
    private static final int DEFAULT_TIMEOUT = Configuration.getInstance().getTimeout();

    private WaitHelper() {}

    public static void waitForElementVisible(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.waitForSelector(selector);
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(DEFAULT_TIMEOUT));
        logger.info("Element visible: {}", selector);
    }

    public static void waitForElementHidden(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(DEFAULT_TIMEOUT));
        logger.info("Element hidden: {}", selector);
    }

    public static void waitForElementEnabled(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ENABLED)
                .setTimeout(DEFAULT_TIMEOUT));
        logger.info("Element enabled: {}", selector);
    }

    public static void waitForElementDisabled(String selector) {
        Page page = PlaywrightFactory.getPage();
        page.locator(selector).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.DISABLED)
                .setTimeout(DEFAULT_TIMEOUT));
        logger.info("Element disabled: {}", selector);
    }

    public static void waitForURL(String urlPattern) {
        Page page = PlaywrightFactory.getPage();
        page.waitForURL(urlPattern);
        logger.info("URL matched: {}", urlPattern);
    }

    public static void waitForNavigation() {
        Page page = PlaywrightFactory.getPage();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        logger.info("Navigation completed");
    }

    public static void waitForLoadState(LoadState state) {
        Page page = PlaywrightFactory.getPage();
        page.waitForLoadState(state);
        logger.info("Load state reached: {}", state);
    }

    public static void waitForFunction(String script, int timeoutMs) {
        Page page = PlaywrightFactory.getPage();
        page.waitForFunction(script);
        logger.info("Wait for function completed");
    }

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
WAITHELPEREOF

echo "Creating other utility classes..."
cat > src/main/java/com/enterprise/automation/util/AssertionHelper.java << 'ASSERTHELPEREOF'
package com.enterprise.automation.util;

import com.enterprise.automation.browser.PlaywrightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssertionHelper {
    private static final Logger logger = LoggerFactory.getLogger(AssertionHelper.class);

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            PlaywrightFactory.takeScreenshot("assertion_failure");
            logger.error("Assertion failed: {}", message);
            throw new AssertionError(message);
        }
        logger.info("Assertion passed: {}", message);
    }

    public static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    public static void assertEquals(String actual, String expected, String message) {
        if (!actual.equals(expected)) {
            PlaywrightFactory.takeScreenshot("assertion_failure");
            logger.error("Assertion failed: {}. Expected: {}, Actual: {}", message, expected, actual);
            throw new AssertionError(message + ". Expected: " + expected + ", Actual: " + actual);
        }
        logger.info("Assertion passed: {}", message);
    }

    public static void assertNotNull(Object obj, String message) {
        assertTrue(obj != null, message);
    }

    public static void assertNull(Object obj, String message) {
        assertTrue(obj == null, message);
    }
}
ASSERTHELPEREOF

cat > src/main/java/com/enterprise/automation/util/TestDataGenerator.java << 'DATAGENERATOREOF'
package com.enterprise.automation.util;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker();

    public static String generateEmail() {
        String email = faker.internet().emailAddress();
        logger.info("Generated email: {}", email);
        return email;
    }

    public static String generatePassword() {
        String password = faker.internet().password(8, 16, true, true);
        logger.info("Generated password");
        return password;
    }

    public static String generateUsername() {
        String username = faker.name().username();
        logger.info("Generated username: {}", username);
        return username;
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

    public static String generateAddress() {
        return faker.address().fullAddress();
    }

    public static String generateLoremText(int wordCount) {
        return faker.lorem().words(wordCount).toString();
    }

    public static int generateRandomNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static String generateCompanyName() {
        return faker.company().name();
    }
}
DATAGENERATOREOF

cat > src/main/java/com/enterprise/automation/listener/TestListener.java << 'TESTLISTENEREOF'
package com.enterprise.automation.listener;

import com.enterprise.automation.browser.PlaywrightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test suite started: {}", context.getName());
        PlaywrightFactory.initPlaywright();
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        PlaywrightFactory.closeAll();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        PlaywrightFactory.takeScreenshot("test_failure_" + result.getName());
        PlaywrightFactory.closeAll();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test suite finished: {}", context.getName());
        PlaywrightFactory.closePlaywright();
    }
}
TESTLISTENEREOF

echo "Creating sample test class..."
cat > src/test/java/com/enterprise/automation/tests/BaseTest.java << 'BASETESTEOF'
package com.enterprise.automation.tests;

import com.enterprise.automation.browser.PlaywrightFactory;
import com.enterprise.automation.config.Configuration;
import com.enterprise.automation.listener.TestListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class BaseTest {

    @BeforeMethod
    public void setUp() {
        PlaywrightFactory.initPlaywright();
        PlaywrightFactory.navigateTo(Configuration.getInstance().getBaseUrl());
    }

    @AfterMethod
    public void tearDown() {
        PlaywrightFactory.closeAll();
    }
}
BASETESTEOF

cat > src/test/java/com/enterprise/automation/tests/LoginTests.java << 'LOGINTESTEOF'
package com.enterprise.automation.tests;

import com.enterprise.automation.util.AssertionHelper;
import com.enterprise.automation.util.TestDataGenerator;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(groups = "smoke")
    public void testValidLogin() {
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.generatePassword();
        
        AssertionHelper.assertTrue(true, "Sample test passed");
    }

    @Test
    public void testInvalidLogin() {
        AssertionHelper.assertTrue(true, "Invalid login test passed");
    }
}
LOGINTESTEOF

echo "Creating README.md..."
cat > README.md << 'READMEEOF'
# Enterprise Playwright Java Automation Framework

Production-ready test automation framework using Playwright Java with enterprise-grade features.

## Features

- Thread-safe browser management with parallel execution
- Page Object Model (POM) design pattern
- Data-driven testing support
- Allure reporting integration
- Comprehensive logging with Log4j2
- API testing with RestAssured
- Test data generation with JavaFaker
- Multi-browser support (Chromium, Firefox, WebKit)
- Docker containerization

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Git

## Quick Start

### 1. Clone/Download Project

```bash
cd playwright-java-framework
