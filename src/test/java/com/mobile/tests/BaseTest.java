package com.mobile.tests;

import com.mobile.framework.config.ConfigReader;
import com.mobile.framework.listeners.TestListener;
import com.mobile.framework.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

/**
 * BaseTest - Base class for all test classes
 *
 * This class provides common setup and teardown methods for all test classes.
 * It initializes the Appium driver before tests and performs cleanup after tests.
 *
 * Features:
 * - Driver initialization and cleanup
 * - Platform configuration from system properties
 * - Automatic listener registration
 * - Comprehensive logging
 *
 * Usage: Extend this class in your test classes
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
@Listeners(TestListener.class)
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ConfigReader configReader = ConfigReader.getInstance();
    protected String platform;

    /**
     * Executed before test suite starts
     * Logs suite initialization
     *
     * @param suiteName Name of the test suite
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(@Optional("Mobile Test Suite") String suiteName) {
        logger.info("================================================");
        logger.info("Starting Test Suite: {}", suiteName);
        logger.info("================================================");
    }

    /**
     * Executed after test suite finishes
     * Logs suite completion
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("================================================");
        logger.info("Test Suite Completed");
        logger.info("================================================");
    }

    /**
     * Executed before each test class
     * Determines platform from system properties or parameters
     *
     * @param platformParam Platform parameter from testng.xml (optional)
     */
    @BeforeClass(alwaysRun = true)
    @Parameters({"platform"})
    public void beforeClass(@Optional String platformParam) {
        // Priority: 1. System Property, 2. TestNG Parameter, 3. Default (android)
        platform = System.getProperty("platform", platformParam != null ? platformParam : "android");
        logger.info("Test Class Initialized with Platform: {}", platform);
    }

    /**
     * Executed after each test class
     * Cleanup operations
     */
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        logger.info("Test Class Finished: {}", this.getClass().getSimpleName());
    }

    /**
     * Executed before each test method
     * Initializes Appium driver for the specified platform
     *
     * This method is crucial for test execution as it:
     * 1. Initializes the Appium driver
     * 2. Configures platform-specific capabilities
     * 3. Establishes connection with Appium server
     *
     * @throws MalformedURLException if Appium server URL is invalid
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) throws MalformedURLException {
        logger.info("========== Test Setup Started ==========");
        logger.info("Initializing driver for platform: {}", platform);

        try {
            // Get descriptive test name from @Test annotation's testName attribute
            String testName = method.getName(); // Default to method name
            Test testAnnotation = method.getAnnotation(Test.class);

            if (testAnnotation != null && testAnnotation.testName() != null && !testAnnotation.testName().isEmpty()) {
                testName = testAnnotation.testName();
                logger.info("Using descriptive test name from annotation: {}", testName);
            } else {
                logger.info("No testName attribute found, using method name: {}", testName);
            }

            // Set test name for BrowserStack session naming
            DriverManager.setTestName(testName);
            logger.info("Test name set to: {}", testName);

            // Initialize driver using DriverManager
            DriverManager.initializeDriver(platform);
            logger.info("Driver initialized successfully");

            // Additional setup can be added here
            // Example: Reset app state, clear app data, etc.

        } catch (Exception e) {
            logger.error("Failed to initialize driver: {}", e.getMessage());
            throw e;
        }

        logger.info("========== Test Setup Completed ==========");
    }

    /**
     * Executed after each test method
     * Performs cleanup operations including driver quit
     *
     * This method ensures:
     * 1. Proper driver cleanup
     * 2. Session termination
     * 3. Resource deallocation
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("========== Test Teardown Started ==========");

        try {
            // Quit driver and clean up resources
            if (DriverManager.isDriverInitialized()) {
                DriverManager.quitDriver();
                logger.info("Driver quit successfully");
            } else {
                logger.warn("Driver was not initialized, skipping quit");
            }

            // Clear test name from ThreadLocal
            DriverManager.clearTestName();

        } catch (Exception e) {
            logger.error("Error during teardown: {}", e.getMessage());
        }

        logger.info("========== Test Teardown Completed ==========");
    }

    /**
     * Gets the current platform being tested
     *
     * @return Platform name (android/ios)
     */
    protected String getPlatform() {
        return platform;
    }

    /**
     * Checks if current platform is Android
     *
     * @return true if Android, false otherwise
     */
    protected boolean isAndroid() {
        return "android".equalsIgnoreCase(platform);
    }

    /**
     * Checks if current platform is iOS
     *
     * @return true if iOS, false otherwise
     */
    protected boolean isIOS() {
        return "ios".equalsIgnoreCase(platform);
    }

    /**
     * Logs test step information
     * Helper method for logging test steps in reports
     *
     * @param stepDescription Description of the test step
     */
    protected void logTestStep(String stepDescription) {
        logger.info("TEST STEP: {}", stepDescription);
        TestListener.logInfo(stepDescription);
    }

    /**
     * Logs test assertion
     * Helper method for logging assertions in reports
     *
     * @param assertionDescription Description of the assertion
     * @param passed Whether the assertion passed
     */
    protected void logAssertion(String assertionDescription, boolean passed) {
        if (passed) {
            logger.info("ASSERTION PASSED: {}", assertionDescription);
            TestListener.logPass(assertionDescription);
        } else {
            logger.error("ASSERTION FAILED: {}", assertionDescription);
            TestListener.logFail(assertionDescription);
        }
    }

    /**
     * Waits for specified duration
     * Use sparingly - prefer explicit waits in page objects
     *
     * @param milliseconds Time to wait in milliseconds
     */
    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
