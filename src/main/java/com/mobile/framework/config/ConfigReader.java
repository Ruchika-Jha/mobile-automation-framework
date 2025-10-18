package com.mobile.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Singleton class for reading configuration properties
 *
 * This class provides centralized access to all configuration properties
 * defined in config.properties file. It uses the Singleton pattern to ensure
 * only one instance exists throughout the test execution lifecycle.
 *
 * Features:
 * - Thread-safe singleton implementation
 * - Lazy initialization
 * - Comprehensive error handling
 * - Support for system property overrides
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    /**
     * Private constructor to prevent instantiation
     * Loads properties from config.properties file
     */
    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Thread-safe singleton instance retrieval
     * Uses double-checked locking pattern for performance optimization
     *
     * @return ConfigReader singleton instance
     */
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /**
     * Loads properties from the configuration file
     * Handles both file-based and classpath-based property loading
     */
    private void loadProperties() {
        InputStream inputStream = null;
        try {
            // First try to load from file system
            inputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(inputStream);
            logger.info("Configuration properties loaded successfully from: {}", CONFIG_FILE_PATH);
        } catch (IOException e) {
            logger.warn("Failed to load from file path, trying classpath...");
            try {
                // Fallback to classpath loading
                inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
                if (inputStream != null) {
                    properties.load(inputStream);
                    logger.info("Configuration properties loaded successfully from classpath");
                } else {
                    logger.error("Configuration file not found in classpath");
                }
            } catch (IOException ex) {
                logger.error("Failed to load configuration properties: {}", ex.getMessage());
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Failed to close input stream: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Retrieves property value by key
     * Checks system properties first for runtime overrides
     *
     * @param key Property key
     * @return Property value or null if not found
     */
    public String getProperty(String key) {
        // Check system properties first (allows runtime override)
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            logger.debug("Using system property for key '{}': {}", key, systemValue);
            return systemValue;
        }

        // Return from config file
        String value = properties.getProperty(key);
        if (value != null) {
            logger.debug("Retrieved property '{}': {}", key, value);
        } else {
            logger.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }

    /**
     * Retrieves property value with a default fallback
     *
     * @param key Property key
     * @param defaultValue Default value if key is not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null) ? value : defaultValue;
    }

    // ========== Appium Server Configuration ==========

    public String getAppiumServerUrl() {
        return getProperty("appium.server.url", "http://127.0.0.1:4723");
    }

    public String getAppiumServerHost() {
        return getProperty("appium.server.host", "127.0.0.1");
    }

    public int getAppiumServerPort() {
        return Integer.parseInt(getProperty("appium.server.port", "4723"));
    }

    // ========== Android Configuration ==========

    public String getAndroidPlatformName() {
        return getProperty("android.platform.name", "Android");
    }

    public String getAndroidPlatformVersion() {
        return getProperty("android.platform.version");
    }

    public String getAndroidDeviceName() {
        return getProperty("android.device.name");
    }

    public String getAndroidAutomationName() {
        return getProperty("android.automation.name", "UiAutomator2");
    }

    public String getAndroidAppPath() {
        return getProperty("android.app.path");
    }

    public String getAndroidAppPackage() {
        return getProperty("android.app.package");
    }

    public String getAndroidAppActivity() {
        return getProperty("android.app.activity");
    }

    // ========== iOS Configuration ==========

    public String getIOSPlatformName() {
        return getProperty("ios.platform.name", "iOS");
    }

    public String getIOSPlatformVersion() {
        return getProperty("ios.platform.version");
    }

    public String getIOSDeviceName() {
        return getProperty("ios.device.name");
    }

    public String getIOSAutomationName() {
        return getProperty("ios.automation.name", "XCUITest");
    }

    public String getIOSAppPath() {
        return getProperty("ios.app.path");
    }

    public String getIOSBundleId() {
        return getProperty("ios.bundle.id");
    }

    public String getIOSUDID() {
        return getProperty("ios.udid", "auto");
    }

    // ========== Common Capabilities ==========

    public boolean getNoReset() {
        return Boolean.parseBoolean(getProperty("app.no.reset", "false"));
    }

    public boolean getFullReset() {
        return Boolean.parseBoolean(getProperty("app.full.reset", "false"));
    }

    public int getNewCommandTimeout() {
        return Integer.parseInt(getProperty("new.command.timeout", "300"));
    }

    public boolean getAutoGrantPermissions() {
        return Boolean.parseBoolean(getProperty("auto.grant.permissions", "true"));
    }

    // ========== Wait Configuration ==========

    public int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "30"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "60"));
    }

    // ========== Test Execution Configuration ==========

    public String getExecutionMode() {
        return getProperty("execution.mode", "local");
    }

    public int getThreadCount() {
        return Integer.parseInt(getProperty("thread.count", "3"));
    }

    public int getRetryCount() {
        return Integer.parseInt(getProperty("retry.count", "1"));
    }

    // ========== Reporting Configuration ==========

    public String getReportPath() {
        return getProperty("report.path", "reports/");
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path", "reports/screenshots/");
    }

    public String getExtentReportName() {
        return getProperty("extent.report.name", "Mobile-Automation-Test-Report");
    }

    public String getExtentReportTitle() {
        return getProperty("extent.report.title", "Mobile Test Execution Report");
    }

    // ========== Logging Configuration ==========

    public String getLogLevel() {
        return getProperty("log.level", "INFO");
    }

    public String getLogPath() {
        return getProperty("log.path", "logs/");
    }

    public String getLogFileName() {
        return getProperty("log.file.name", "mobile-automation.log");
    }

    // ========== Test Data Configuration ==========

    public String getTestDataPath() {
        return getProperty("test.data.path", "src/test/resources/testdata/");
    }

    // ========== BrowserStack Configuration ==========

    public String getBrowserStackUsername() {
        return getProperty("browserstack.username");
    }

    public String getBrowserStackAccessKey() {
        return getProperty("browserstack.access.key");
    }

    public String getBrowserStackHubUrl() {
        return getProperty("browserstack.hub.url", "https://hub-cloud.browserstack.com/wd/hub");
    }

    public String getBrowserStackProject() {
        return getProperty("browserstack.project", "Mobile Automation Framework");
    }

    public String getBrowserStackBuild() {
        return getProperty("browserstack.build", "Build 1.0");
    }

    public String getBrowserStackName() {
        return getProperty("browserstack.name", "Mobile Test Execution");
    }

    public boolean getBrowserStackDebug() {
        return Boolean.parseBoolean(getProperty("browserstack.debug", "true"));
    }

    public boolean getBrowserStackNetworkLogs() {
        return Boolean.parseBoolean(getProperty("browserstack.network.logs", "true"));
    }

    public boolean getBrowserStackVideo() {
        return Boolean.parseBoolean(getProperty("browserstack.video", "true"));
    }

    public String getBrowserStackConsole() {
        return getProperty("browserstack.console", "errors");
    }

    public String getBrowserStackTimezone() {
        return getProperty("browserstack.timezone", "UTC");
    }

    public String getBrowserStackAndroidAppUrl() {
        return getProperty("browserstack.android.app.url");
    }

    public String getBrowserStackIOSAppUrl() {
        return getProperty("browserstack.ios.app.url");
    }

    public String getBrowserStackAndroidDevice() {
        return getProperty("browserstack.android.device", "Google Pixel 7");
    }

    public String getBrowserStackAndroidOSVersion() {
        return getProperty("browserstack.android.os.version", "13.0");
    }

    public String getBrowserStackIOSDevice() {
        return getProperty("browserstack.ios.device", "iPhone 14");
    }

    public String getBrowserStackIOSOSVersion() {
        return getProperty("browserstack.ios.os.version", "16");
    }
}
