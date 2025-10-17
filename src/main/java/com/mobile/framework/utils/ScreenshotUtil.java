package com.mobile.framework.utils;

import com.mobile.framework.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil - Utility class for capturing and managing screenshots
 *
 * This class provides functionality to capture screenshots during test execution,
 * particularly useful for debugging test failures and generating visual evidence
 * in test reports.
 *
 * Features:
 * - Automatic screenshot capture on test failure
 * - Timestamp-based unique file naming
 * - Configurable screenshot directory
 * - Thread-safe screenshot capture
 * - Support for both success and failure scenarios
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();

    /**
     * Private constructor to prevent instantiation
     */
    private ScreenshotUtil() {
    }

    /**
     * Captures screenshot and saves it to the configured location
     *
     * @param testName Name of the test case (used in filename)
     * @return Absolute path of the saved screenshot file
     */
    public static String captureScreenshot(String testName) {
        // Generate unique filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";

        return captureScreenshot(testName, fileName);
    }

    /**
     * Captures screenshot with custom filename
     *
     * @param testName Name of the test case
     * @param fileName Custom filename for the screenshot
     * @return Absolute path of the saved screenshot file
     */
    public static String captureScreenshot(String testName, String fileName) {
        try {
            // Check if driver is initialized
            if (!DriverManager.isDriverInitialized()) {
                logger.error("Cannot capture screenshot - driver not initialized");
                return null;
            }

            // Capture screenshot as file
            TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            // Create screenshot directory if it doesn't exist
            String screenshotDir = configReader.getScreenshotPath();
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
                logger.info("Created screenshot directory: {}", screenshotDir);
            }

            // Define destination file path
            String destinationPath = screenshotDir + fileName;
            File destinationFile = new File(destinationPath);

            // Copy screenshot to destination
            FileUtils.copyFile(sourceFile, destinationFile);

            logger.info("Screenshot captured successfully: {}", destinationPath);
            return destinationFile.getAbsolutePath();

        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}. Error: {}", testName, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while capturing screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Captures screenshot specifically for failed test cases
     *
     * @param testName Name of the failed test
     * @return Absolute path of the saved screenshot file
     */
    public static String captureFailureScreenshot(String testName) {
        logger.info("Capturing failure screenshot for test: {}", testName);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String fileName = testName + "_FAILED_" + timestamp + ".png";

        return captureScreenshot(testName, fileName);
    }

    /**
     * Captures screenshot for successful test cases (optional feature)
     *
     * @param testName Name of the passed test
     * @return Absolute path of the saved screenshot file
     */
    public static String captureSuccessScreenshot(String testName) {
        logger.info("Capturing success screenshot for test: {}", testName);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String fileName = testName + "_PASSED_" + timestamp + ".png";

        return captureScreenshot(testName, fileName);
    }

    /**
     * Captures screenshot as Base64 string for embedding in reports
     *
     * @return Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64() {
        try {
            if (!DriverManager.isDriverInitialized()) {
                logger.error("Cannot capture screenshot - driver not initialized");
                return null;
            }

            TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
            String base64Screenshot = screenshot.getScreenshotAs(OutputType.BASE64);

            logger.debug("Screenshot captured as Base64 string");
            return base64Screenshot;

        } catch (Exception e) {
            logger.error("Failed to capture screenshot as Base64: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Captures screenshot as byte array
     *
     * @return Byte array of the screenshot
     */
    public static byte[] captureScreenshotAsByteArray() {
        try {
            if (!DriverManager.isDriverInitialized()) {
                logger.error("Cannot capture screenshot - driver not initialized");
                return null;
            }

            TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);

            logger.debug("Screenshot captured as byte array");
            return screenshotBytes;

        } catch (Exception e) {
            logger.error("Failed to capture screenshot as byte array: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Cleans up old screenshots from the screenshot directory
     *
     * @param daysToKeep Number of days to keep screenshots (older ones will be deleted)
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        try {
            String screenshotDir = configReader.getScreenshotPath();
            File directory = new File(screenshotDir);

            if (!directory.exists() || !directory.isDirectory()) {
                logger.warn("Screenshot directory does not exist: {}", screenshotDir);
                return;
            }

            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60 * 60 * 1000);
            File[] files = directory.listFiles();

            if (files != null) {
                int deletedCount = 0;
                for (File file : files) {
                    if (file.isFile() && file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            deletedCount++;
                        }
                    }
                }
                logger.info("Cleaned up {} old screenshot(s) older than {} days", deletedCount, daysToKeep);
            }

        } catch (Exception e) {
            logger.error("Error while cleaning up old screenshots: {}", e.getMessage());
        }
    }

    /**
     * Creates screenshot directory if it doesn't exist
     */
    public static void createScreenshotDirectory() {
        String screenshotDir = configReader.getScreenshotPath();
        File directory = new File(screenshotDir);

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("Screenshot directory created: {}", screenshotDir);
            } else {
                logger.error("Failed to create screenshot directory: {}", screenshotDir);
            }
        }
    }

    /**
     * Gets the screenshot directory path
     *
     * @return Screenshot directory path
     */
    public static String getScreenshotDirectory() {
        return configReader.getScreenshotPath();
    }
}
