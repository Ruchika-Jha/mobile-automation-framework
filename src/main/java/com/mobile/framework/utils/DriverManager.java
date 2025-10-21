package com.mobile.framework.utils;

import com.mobile.framework.config.ConfigReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DriverManager - Manages Appium driver lifecycle
 *
 * This class handles the initialization, configuration, and cleanup of Appium drivers
 * for both Android and iOS platforms. It uses ThreadLocal to ensure thread-safety
 * for parallel test execution.
 *
 * Features:
 * - Thread-safe driver management using ThreadLocal
 * - Support for both Android and iOS platforms
 * - Automatic capability configuration from config.properties
 * - Proper driver cleanup and resource management
 * - Comprehensive logging
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();

    // ThreadLocal to maintain separate driver instance for each thread
    // This is crucial for parallel test execution
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    // ThreadLocal to store test name for BrowserStack session naming
    private static ThreadLocal<String> currentTestName = new ThreadLocal<>();

    // Build name with timestamp - created once per suite execution
    private static String suiteExecutionBuildName = null;

    /**
     * Private constructor to prevent instantiation
     * This class uses static methods and doesn't need to be instantiated
     */
    private DriverManager() {
    }

    /**
     * Initializes Appium driver based on platform type
     *
     * @param platform Platform name (android or ios)
     * @return AppiumDriver instance
     * @throws MalformedURLException if Appium server URL is invalid
     */
    public static AppiumDriver initializeDriver(String platform) throws MalformedURLException {
        logger.info("Initializing driver for platform: {}", platform);
        logger.info("Execution mode: {}", configReader.getExecutionMode());

        AppiumDriver appiumDriver = null;

        // Check execution mode
        String executionMode = configReader.getExecutionMode();
        boolean isBrowserStack = "browserstack".equalsIgnoreCase(executionMode);

        // Determine platform and initialize appropriate driver
        if (platform.equalsIgnoreCase("android")) {
            appiumDriver = isBrowserStack ? initializeBrowserStackAndroidDriver() : initializeAndroidDriver();
        } else if (platform.equalsIgnoreCase("ios")) {
            appiumDriver = isBrowserStack ? initializeBrowserStackIOSDriver() : initializeIOSDriver();
        } else {
            logger.error("Invalid platform specified: {}. Use 'android' or 'ios'", platform);
            throw new IllegalArgumentException("Invalid platform: " + platform);
        }

        // Set implicit wait timeout
        appiumDriver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(configReader.getImplicitWait()));

        // Store driver in ThreadLocal
        driver.set(appiumDriver);

        logger.info("Driver initialized successfully for platform: {} in {} mode", platform, executionMode);
        return appiumDriver;
    }

    /**
     * Initializes Android driver with UiAutomator2 options
     *
     * @return AndroidDriver instance
     * @throws MalformedURLException if Appium server URL is invalid
     */
    private static AndroidDriver initializeAndroidDriver() throws MalformedURLException {
        logger.info("Setting up Android driver with UiAutomator2");

        // UiAutomator2Options - Modern way of setting capabilities for Android
        UiAutomator2Options options = new UiAutomator2Options();

        // Platform capabilities
        options.setPlatformName(configReader.getAndroidPlatformName());
        options.setPlatformVersion(configReader.getAndroidPlatformVersion());
        options.setDeviceName(configReader.getAndroidDeviceName());
        options.setAutomationName(configReader.getAndroidAutomationName());

        // App capabilities
        String appPath = configReader.getAndroidAppPath();
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            if (appFile.exists()) {
                options.setApp(appFile.getAbsolutePath());
                logger.info("Android app path set to: {}", appFile.getAbsolutePath());
            } else {
                logger.warn("Android app file not found at: {}. Using app package instead.", appPath);
                // Use app package and activity if app file doesn't exist
                options.setAppPackage(configReader.getAndroidAppPackage());
                options.setAppActivity(configReader.getAndroidAppActivity());
            }
        } else {
            // If no app path, use package and activity
            options.setAppPackage(configReader.getAndroidAppPackage());
            options.setAppActivity(configReader.getAndroidAppActivity());
        }

        // Additional Android capabilities
        options.setNoReset(configReader.getNoReset());
        options.setFullReset(configReader.getFullReset());
        options.setNewCommandTimeout(Duration.ofSeconds(configReader.getNewCommandTimeout()));
        options.setAutoGrantPermissions(configReader.getAutoGrantPermissions());

        // Performance optimization
        options.setCapability("skipServerInstallation", true);
        options.setCapability("skipDeviceInitialization", true);

        // Create Appium server URL
        URL serverUrl = new URL(configReader.getAppiumServerUrl());

        logger.info("Android capabilities: {}", options.asMap());
        return new AndroidDriver(serverUrl, options);
    }

    /**
     * Initializes iOS driver with XCUITest options
     *
     * @return IOSDriver instance
     * @throws MalformedURLException if Appium server URL is invalid
     */
    private static IOSDriver initializeIOSDriver() throws MalformedURLException {
        logger.info("Setting up iOS driver with XCUITest");

        // XCUITestOptions - Modern way of setting capabilities for iOS
        XCUITestOptions options = new XCUITestOptions();

        // Platform capabilities
        options.setPlatformName(configReader.getIOSPlatformName());
        options.setPlatformVersion(configReader.getIOSPlatformVersion());
        options.setDeviceName(configReader.getIOSDeviceName());
        options.setAutomationName(configReader.getIOSAutomationName());

        // App capabilities
        String appPath = configReader.getIOSAppPath();
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            if (appFile.exists()) {
                options.setApp(appFile.getAbsolutePath());
                logger.info("iOS app path set to: {}", appFile.getAbsolutePath());
            } else {
                logger.warn("iOS app file not found at: {}. Using bundle ID instead.", appPath);
                options.setBundleId(configReader.getIOSBundleId());
            }
        } else {
            options.setBundleId(configReader.getIOSBundleId());
        }

        // Device UDID (for real device testing)
        String udid = configReader.getIOSUDID();
        if (udid != null && !udid.equalsIgnoreCase("auto")) {
            options.setUdid(udid);
        }

        // Additional iOS capabilities
        options.setNoReset(configReader.getNoReset());
        options.setFullReset(configReader.getFullReset());
        options.setNewCommandTimeout(Duration.ofSeconds(configReader.getNewCommandTimeout()));

        // iOS specific settings
        options.setWdaLaunchTimeout(Duration.ofSeconds(120));
        options.setCapability("showXcodeLog", true);

        // Create Appium server URL
        URL serverUrl = new URL(configReader.getAppiumServerUrl());

        logger.info("iOS capabilities: {}", options.asMap());
        return new IOSDriver(serverUrl, options);
    }

    /**
     * Gets the driver instance for the current thread
     *
     * @return AppiumDriver instance or null if not initialized
     */
    public static AppiumDriver getDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver == null) {
            logger.error("Driver is not initialized. Call initializeDriver() first.");
            throw new IllegalStateException("Driver not initialized. Call initializeDriver() first.");
        }
        return currentDriver;
    }

    /**
     * Quits the driver and removes it from ThreadLocal
     * Should be called in @AfterMethod or @AfterClass
     */
    public static void quitDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                logger.info("Quitting driver for thread: {}", Thread.currentThread().getId());
                currentDriver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver: {}", e.getMessage());
            } finally {
                // Remove driver from ThreadLocal to prevent memory leaks
                driver.remove();
            }
        } else {
            logger.warn("No driver instance found to quit for thread: {}", Thread.currentThread().getId());
        }
    }

    /**
     * Checks if driver is initialized for current thread
     *
     * @return true if driver is initialized, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }

    /**
     * Sets the current test name for BrowserStack session naming
     *
     * @param testName Name of the test
     */
    public static void setTestName(String testName) {
        currentTestName.set(testName);
        logger.debug("Test name set to: {}", testName);
    }

    /**
     * Gets the current test name
     *
     * @return Current test name or default name if not set
     */
    public static String getTestName() {
        String testName = currentTestName.get();
        return testName != null ? testName : configReader.getBrowserStackName();
    }

    /**
     * Clears the test name from ThreadLocal
     */
    public static void clearTestName() {
        currentTestName.remove();
    }

    /**
     * Generates a dynamic build name with comprehensive information
     * Format: Build_[Version]_[Platform]_[Device]_[Date]_[Time]
     * Example: Build_1.0_Android_Pixel7_20251018_143045
     *
     * @param platform Platform name (android/ios)
     * @param device Device name
     * @return Formatted build name
     */
    private static String generateBuildName(String platform, String device) {
        // Use cached build name for entire suite execution
        if (suiteExecutionBuildName != null) {
            return suiteExecutionBuildName;
        }

        // Generate timestamp
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(dateFormatter);

        // Get build version from config
        String buildVersion = configReader.getBrowserStackBuild(); // e.g., "Build 1.0"

        // Clean device name (remove spaces and special characters)
        String cleanDevice = device.replaceAll("[^a-zA-Z0-9]", "");

        // Clean platform name
        String cleanPlatform = platform.substring(0, 1).toUpperCase() + platform.substring(1).toLowerCase();

        // Format: Build_1.0_Android_Pixel7_20251018_143045
        suiteExecutionBuildName = String.format("%s_%s_%s_%s",
            buildVersion.replace(" ", "_"),
            cleanPlatform,
            cleanDevice,
            timestamp
        );

        logger.info("Generated build name: {}", suiteExecutionBuildName);
        return suiteExecutionBuildName;
    }

    /**
     * Restarts the application
     * Useful for resetting app state between tests
     */
    public static void restartApp() {
        logger.info("Restarting application");
        AppiumDriver currentDriver = getDriver();
        if (currentDriver != null) {
            String appIdentifier = getAppIdentifier();
            ((InteractsWithApps) currentDriver).terminateApp(appIdentifier);
            ((InteractsWithApps) currentDriver).activateApp(appIdentifier);
            logger.info("Application restarted successfully");
        }
    }

    /**
     * Gets the app identifier (package name for Android, bundle ID for iOS)
     *
     * @return App identifier string
     */
    private static String getAppIdentifier() {
        AppiumDriver currentDriver = getDriver();
        if (currentDriver instanceof AndroidDriver) {
            return configReader.getAndroidAppPackage();
        } else if (currentDriver instanceof IOSDriver) {
            return configReader.getIOSBundleId();
        }
        return null;
    }

    /**
     * Closes the current app
     */
    public static void closeApp() {
        logger.info("Closing application");
        AppiumDriver currentDriver = getDriver();
        if (currentDriver != null) {
            ((InteractsWithApps) currentDriver).terminateApp(getAppIdentifier());
            logger.info("Application closed successfully");
        }
    }

    /**
     * Launches the app
     */
    public static void launchApp() {
        logger.info("Launching application");
        AppiumDriver currentDriver = getDriver();
        if (currentDriver != null) {
            ((InteractsWithApps) currentDriver).activateApp(getAppIdentifier());
            logger.info("Application launched successfully");
        }
    }

    /**
     * Initializes Android driver for BrowserStack cloud execution
     *
     * @return AndroidDriver instance configured for BrowserStack
     * @throws MalformedURLException if BrowserStack hub URL is invalid
     */
    private static AndroidDriver initializeBrowserStackAndroidDriver() throws MalformedURLException {
        logger.info("Setting up Android driver for BrowserStack execution");

        UiAutomator2Options options = new UiAutomator2Options();

        // Device capabilities
        String deviceName = configReader.getBrowserStackAndroidDevice();
        options.setCapability("deviceName", deviceName);
        options.setCapability("platformName", "Android");
        options.setCapability("platformVersion", configReader.getBrowserStackAndroidOSVersion());

        // App capability
        String appUrl = configReader.getBrowserStackAndroidAppUrl();
        if (appUrl != null && !appUrl.isEmpty()) {
            options.setCapability("app", appUrl);
            logger.info("BrowserStack Android app URL: {}", appUrl);
        } else {
            logger.warn("BrowserStack Android app URL not configured. Upload your app to BrowserStack first.");
        }

        // BrowserStack specific capabilities with dynamic build name
        options.setCapability("project", configReader.getBrowserStackProject());
        options.setCapability("build", generateBuildName("android", deviceName));
        options.setCapability("name", getTestName()); // Use dynamic test name
        options.setCapability("browserstack.debug", configReader.getBrowserStackDebug());
        options.setCapability("browserstack.networkLogs", configReader.getBrowserStackNetworkLogs());
        options.setCapability("browserstack.video", configReader.getBrowserStackVideo());
        options.setCapability("browserstack.console", configReader.getBrowserStackConsole());
        options.setCapability("browserstack.timezone", configReader.getBrowserStackTimezone());

        // Common capabilities
        options.setAutomationName("UiAutomator2");
        options.setNoReset(configReader.getNoReset());
        options.setFullReset(configReader.getFullReset());
        options.setNewCommandTimeout(Duration.ofSeconds(configReader.getNewCommandTimeout()));

        // Create BrowserStack URL with embedded credentials
        // Format: https://username:accessKey@hub-cloud.browserstack.com/wd/hub
        String username = configReader.getBrowserStackUsername();
        String accessKey = configReader.getBrowserStackAccessKey();
        String hubUrl = configReader.getBrowserStackHubUrl();

        // Build URL with credentials
        String browserStackUrlString = hubUrl.replace("https://", "https://" + username + ":" + accessKey + "@");
        URL browserStackUrl = new URL(browserStackUrlString);

        logger.info("BrowserStack Android capabilities: {}", options.asMap());
        logger.info("Connecting to BrowserStack at: {}", hubUrl);
        return new AndroidDriver(browserStackUrl, options);
    }

    /**
     * Initializes iOS driver for BrowserStack cloud execution
     *
     * @return IOSDriver instance configured for BrowserStack
     * @throws MalformedURLException if BrowserStack hub URL is invalid
     */
    private static IOSDriver initializeBrowserStackIOSDriver() throws MalformedURLException {
        logger.info("Setting up iOS driver for BrowserStack execution");

        XCUITestOptions options = new XCUITestOptions();

        // Device capabilities
        String deviceName = configReader.getBrowserStackIOSDevice();
        options.setCapability("deviceName", deviceName);
        options.setCapability("platformName", "iOS");
        options.setCapability("platformVersion", configReader.getBrowserStackIOSOSVersion());

        // App capability
        String appUrl = configReader.getBrowserStackIOSAppUrl();
        if (appUrl != null && !appUrl.isEmpty()) {
            options.setCapability("app", appUrl);
            logger.info("BrowserStack iOS app URL: {}", appUrl);
        } else {
            logger.warn("BrowserStack iOS app URL not configured. Upload your app to BrowserStack first.");
        }

        // BrowserStack specific capabilities with dynamic build name
        options.setCapability("project", configReader.getBrowserStackProject());
        options.setCapability("build", generateBuildName("ios", deviceName));
        options.setCapability("name", getTestName()); // Use dynamic test name
        options.setCapability("browserstack.debug", configReader.getBrowserStackDebug());
        options.setCapability("browserstack.networkLogs", configReader.getBrowserStackNetworkLogs());
        options.setCapability("browserstack.video", configReader.getBrowserStackVideo());
        // Note: browserstack.console is not supported for iOS app automation
        options.setCapability("browserstack.timezone", configReader.getBrowserStackTimezone());

        // Common capabilities
        options.setAutomationName("XCUITest");
        options.setNoReset(configReader.getNoReset());
        options.setFullReset(configReader.getFullReset());
        options.setNewCommandTimeout(Duration.ofSeconds(configReader.getNewCommandTimeout()));

        // Create BrowserStack URL with embedded credentials
        // Format: https://username:accessKey@hub-cloud.browserstack.com/wd/hub
        String username = configReader.getBrowserStackUsername();
        String accessKey = configReader.getBrowserStackAccessKey();
        String hubUrl = configReader.getBrowserStackHubUrl();

        // Build URL with credentials
        String browserStackUrlString = hubUrl.replace("https://", "https://" + username + ":" + accessKey + "@");
        URL browserStackUrl = new URL(browserStackUrlString);

        logger.info("BrowserStack iOS capabilities: {}", options.asMap());
        logger.info("Connecting to BrowserStack at: {}", hubUrl);
        return new IOSDriver(browserStackUrl, options);
    }
}
