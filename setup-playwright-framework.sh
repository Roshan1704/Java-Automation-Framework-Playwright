#!/bin/bash

# Playwright Java Framework Setup Script
# This script creates the complete project structure with all files

PROJECT_DIR="playwright-java-framework"
mkdir -p "$PROJECT_DIR"
cd "$PROJECT_DIR"

echo "🚀 Setting up Playwright Java Framework..."

# Create directory structure
mkdir -p src/main/java/com/enterprise/automation/{browser,config,pages,util,listener}
mkdir -p src/test/java/com/enterprise/automation/tests
mkdir -p src/test/resources
mkdir -p k8s
mkdir -p .github/workflows
mkdir -p scripts

echo "📁 Directory structure created"

# pom.xml
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.enterprise</groupId>
    <artifactId>playwright-automation</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Playwright Automation Framework</name>
    <description>Enterprise-grade Playwright Java automation framework</description>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <playwright.version>1.48.0</playwright.version>
        <testng.version>7.10.2</testng.version>
        <allure.version>2.30.0</allure.version>
        <rest-assured.version>5.4.0</rest-assured.version>
        <log4j.version>2.23.1</log4j.version>
    </properties>

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

        <!-- Allure -->
        <dependency>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-testng</artifactId>
            <version>${allure.version}</version>
        </dependency>

        <!-- REST Assured -->
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>

        <!-- Logging -->
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

        <!-- Faker for Test Data -->
        <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>1.0.2</version>
        </dependency>

        <!-- JSON Processing -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>

        <!-- Apache Commons -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.14.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                </configuration>
            </plugin>

            <plugin>
                <groupId>io.qameta.allure</groupId>
                <artifactId>allure-maven</artifactId>
                <version>2.13.0</version>
            </plugin>
        </plugins>
    </build>
</project>
EOF

echo "✅ pom.xml created"

# Configuration files
cat > src/test/resources/env.properties << 'EOF'
# Environment URLs
app.url.qa=https://qa.example.com
app.url.uat=https://uat.example.com
app.url.prod=https://www.example.com
EOF

cat > src/test/resources/playwright.properties << 'EOF'
# Playwright Configuration
browser=chromium
headless=true
slowmo=0
timeout=30000
navigation_timeout=30000
EOF

cat > src/test/resources/log4j2.properties << 'EOF'
status=warn
name=PlaywrightFramework

appender.console.type=Console
appender.console.name=STDOUT
appender.console.layout.type=PatternLayout
appender.console.layout.pattern=%d{ISO8601} [%t] %-5p %c{1}:%L - %m%n

appender.file.type=File
appender.file.name=FILE
appender.file.fileName=logs/test-execution.log
appender.file.layout.type=PatternLayout
appender.file.layout.pattern=%d{ISO8601} [%t] %-5p %c{1}:%L - %m%n

rootLogger.level=info
rootLogger.appenderRefs=stdout,file
rootLogger.appenderRef.stdout.ref=STDOUT
rootLogger.appenderRef.file.ref=FILE
EOF

echo "✅ Configuration files created"

# Java source files
cat > src/main/java/com/enterprise/automation/config/Configuration.java << 'EOF'
package com.enterprise.automation.config;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final Properties props = new Properties();
    private static final Properties playwrightProps = new Properties();

    static {
        try {
            props.load(Configuration.class.getClassLoader().getResourceAsStream("env.properties"));
            playwrightProps.load(Configuration.class.getClassLoader().getResourceAsStream("playwright.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getAppUrl() {
        return props.getProperty("app.url.qa");
    }

    public static String getBrowser() {
        return playwrightProps.getProperty("browser", "chromium");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(playwrightProps.getProperty("headless", "true"));
    }

    public static int getTimeout() {
        return Integer.parseInt(playwrightProps.getProperty("timeout", "30000"));
    }
}
EOF

cat > src/main/java/com/enterprise/automation/browser/PlaywrightFactory.java << 'EOF'
package com.enterprise.automation.browser;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.enterprise.automation.config.Configuration;

public class PlaywrightFactory {
    private static final Logger logger = LogManager.getLogger(PlaywrightFactory.class);
    private static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThread = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThread = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThread = new ThreadLocal<>();

    public static void initBrowser() {
        try {
            Playwright playwright = Playwright.create();
            playwrightThread.set(playwright);

            Browser browser = createBrowser(playwright);
            browserThread.set(browser);

            BrowserContext context = browser.newContext();
            contextThread.set(context);

            Page page = context.newPage();
            pageThread.set(page);

            logger.info("Browser initialized: {}", Configuration.getBrowser());
        } catch (Exception e) {
            logger.error("Failed to initialize browser", e);
            throw e;
        }
    }

    private static Browser createBrowser(Playwright playwright) {
        return switch (Configuration.getBrowser().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(Configuration.isHeadless()));
            case "webkit" -> playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(Configuration.isHeadless()));
            default -> playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(Configuration.isHeadless()));
        };
    }

    public static Page getPage() {
        return pageThread.get();
    }

    public static BrowserContext getContext() {
        return contextThread.get();
    }

    public static Browser getBrowser() {
        return browserThread.get();
    }

    public static void closeBrowser() {
        try {
            if (pageThread.get() != null) pageThread.get().close();
            if (contextThread.get() != null) contextThread.get().close();
            if (browserThread.get() != null) browserThread.get().close();
            if (playwrightThread.get() != null) playwrightThread.get().close();
            logger.info("Browser closed successfully");
        } catch (Exception e) {
            logger.error("Error closing browser", e);
        } finally {
            pageThread.remove();
            contextThread.remove();
            browserThread.remove();
            playwrightThread.remove();
        }
    }
}
EOF

echo "✅ Core Java classes created"

# Create README
cat > README.md << 'EOF'
# Playwright Java Automation Framework

Enterprise-grade testing framework for web automation using Playwright and Java.

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

### Setup
```bash
mvn clean install
