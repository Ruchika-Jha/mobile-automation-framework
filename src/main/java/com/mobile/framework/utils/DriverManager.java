package com.mobile.framework.utils;

import com.mobile.framework.config.ConfigReader;
import io.appium.java_client.AppiumDriver;
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

        AppiumDriver appiumDriver = null;

        // Determine platform and initialize appropriate driver
        if (platform.equalsIgnoreCase("android")) {
            appiumDriver = initializeAndroidDriver();
        } else if (platform.equalsIgnoreCase("ios")) {
            appiumDriver = initializeIOSDriver();
        } else {
            logger.error("Invalid platform specified: {}. Use 'android' or 'ios'", platform);
            throw new IllegalArgumentException("Invalid platform: " + platform);
        }

        // Set implicit wait timeout
        appiumDriver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(configReader.getImplicitWait()));

        // Store driver in ThreadLocal
        driver.set(appiumDriver);

        logger.info("Driver initialized successfully for platform: {}", platform);
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
     * Restarts the application
     * Useful for resetting app state between tests
     */
    public static void restartApp() {
        logger.info("Restarting application");
        AppiumDriver currentDriver = getDriver();
        if (currentDriver != null) {
            currentDriver.terminateApp(getAppIdentifier());
            currentDriver.activateApp(getAppIdentifier());
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
            currentDriver.terminateApp(getAppIdentifier());
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
            currentDriver.activateApp(getAppIdentifier());
            logger.info("Application launched successfully");
        }
    }
}
