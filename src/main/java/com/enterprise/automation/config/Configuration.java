package com.enterprise.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized configuration management supporting multi-environment setup
 * Loads properties from env.properties, playwright.properties, and system environment variables
 */
public class Configuration {
    private static final Properties ENV_PROPERTIES = new Properties();
    private static final Properties PLAYWRIGHT_PROPERTIES = new Properties();
    private static Configuration instance;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            // Load environment properties
            InputStream envStream = Configuration.class.getClassLoader().getResourceAsStream("env.properties");
            if (envStream != null) {
                ENV_PROPERTIES.load(envStream);
                envStream.close();
            }

            // Load playwright properties
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

    /**
     * Get environment variable with fallback to properties file
     */
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

    /**
     * Get playwright configuration
     */
    public String getPlaywrightConfig(String key, String defaultValue) {
        return PLAYWRIGHT_PROPERTIES.getProperty(key, defaultValue);
    }

    public String getPlaywrightConfig(String key) {
        return getPlaywrightConfig(key, "");
    }

    // Environment-specific URL getters
    public String getBaseUrl() {
        String env = getEnv("environment", "qa");
        return getEnv(env + ".base.url", "https://qa.example.com");
    }

    public String getApiBaseUrl() {
        String env = getEnv("environment", "qa");
        return getEnv(env + ".api.base.url", "https://api-qa.example.com");
    }

    // Playwright configuration getters
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
