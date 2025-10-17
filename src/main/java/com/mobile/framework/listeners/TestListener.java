package com.mobile.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.mobile.framework.config.ConfigReader;
import com.mobile.framework.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestListener - Custom TestNG listener for test execution monitoring
 *
 * This listener provides comprehensive test execution monitoring and reporting
 * using ExtentReports. It automatically captures screenshots on test failures
 * and generates detailed HTML reports.
 *
 * Features:
 * - Automatic ExtentReports generation
 * - Screenshot capture on test failure
 * - Test execution time tracking
 * - Detailed test logs and status
 * - HTML report with dashboard
 *
 * Usage: Add @Listeners(TestListener.class) to test class or configure in testng.xml
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ConfigReader configReader = ConfigReader.getInstance();

    /**
     * Executed before test suite starts
     * Initializes ExtentReports with configuration
     *
     * @param suite ISuite instance
     */
    @Override
    public void onStart(ISuite suite) {
        logger.info("========== Test Suite Started: {} ==========", suite.getName());

        // Create report directory if it doesn't exist
        String reportPath = configReader.getReportPath();
        File reportDir = new File(reportPath);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
            logger.info("Created report directory: {}", reportPath);
        }

        // Create screenshot directory
        ScreenshotUtil.createScreenshotDirectory();

        // Initialize ExtentReports
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportFileName = reportPath + configReader.getExtentReportName() + "_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFileName);

        // Configure ExtentSparkReporter
        sparkReporter.config().setDocumentTitle(configReader.getExtentReportTitle());
        sparkReporter.config().setReportName(suite.getName());
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        sparkReporter.config().setEncoding("UTF-8");

        // Initialize ExtentReports and attach reporter
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // Add system information to report
        extentReports.setSystemInfo("Application", "Mobile Test Automation");
        extentReports.setSystemInfo("Environment", configReader.getExecutionMode());
        extentReports.setSystemInfo("User", System.getProperty("user.name"));
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));

        logger.info("ExtentReports initialized successfully: {}", reportFileName);
    }

    /**
     * Executed after test suite finishes
     * Flushes ExtentReports to write all data to report file
     *
     * @param suite ISuite instance
     */
    @Override
    public void onFinish(ISuite suite) {
        logger.info("========== Test Suite Finished: {} ==========", suite.getName());

        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed successfully");
        }
    }

    /**
     * Executed before each test method starts
     * Creates a new test entry in ExtentReports
     *
     * @param result ITestResult instance
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("---------- Test Started: {} ----------", result.getMethod().getMethodName());

        // Create ExtentTest instance for current test
        ExtentTest test = extentReports.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getTestClass().getName());

        // Add test description if available
        String description = result.getMethod().getDescription();
        if (description != null && !description.isEmpty()) {
            test.info(description);
        }

        // Store ExtentTest in ThreadLocal for thread-safe access
        extentTest.set(test);
    }

    /**
     * Executed when a test passes
     * Logs success status in ExtentReports
     *
     * @param result ITestResult instance
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("---------- Test Passed: {} ----------", result.getMethod().getMethodName());

        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS,
                    MarkupHelper.createLabel("Test Passed: " + result.getMethod().getMethodName(), ExtentColor.GREEN));

            // Calculate execution time
            long executionTime = result.getEndMillis() - result.getStartMillis();
            test.info("Execution Time: " + executionTime + " ms");
        }
    }

    /**
     * Executed when a test fails
     * Captures screenshot and logs failure details in ExtentReports
     *
     * @param result ITestResult instance
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("---------- Test Failed: {} ----------", result.getMethod().getMethodName());
        logger.error("Failure Reason: {}", result.getThrowable().getMessage());

        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL,
                    MarkupHelper.createLabel("Test Failed: " + result.getMethod().getMethodName(), ExtentColor.RED));

            // Log failure exception
            test.fail(result.getThrowable());

            // Capture and attach screenshot
            try {
                String screenshotPath = ScreenshotUtil.captureFailureScreenshot(result.getMethod().getMethodName());
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                    logger.info("Screenshot attached to report: {}", screenshotPath);
                }
            } catch (Exception e) {
                logger.error("Failed to attach screenshot to report: {}", e.getMessage());
            }

            // Calculate execution time
            long executionTime = result.getEndMillis() - result.getStartMillis();
            test.info("Execution Time: " + executionTime + " ms");
        }
    }

    /**
     * Executed when a test is skipped
     * Logs skip status in ExtentReports
     *
     * @param result ITestResult instance
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("---------- Test Skipped: {} ----------", result.getMethod().getMethodName());

        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.SKIP,
                    MarkupHelper.createLabel("Test Skipped: " + result.getMethod().getMethodName(), ExtentColor.YELLOW));

            // Log skip reason if available
            if (result.getThrowable() != null) {
                test.skip(result.getThrowable());
            }
        }
    }

    /**
     * Executed when a test fails but within success percentage
     *
     * @param result ITestResult instance
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("---------- Test Failed But Within Success Percentage: {} ----------",
                result.getMethod().getMethodName());

        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.WARNING,
                    MarkupHelper.createLabel("Test Failed But Within Success Percentage: "
                            + result.getMethod().getMethodName(), ExtentColor.ORANGE));
        }
    }

    /**
     * Gets the current ExtentTest instance
     * Thread-safe method to access ExtentTest
     *
     * @return Current ExtentTest instance
     */
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    /**
     * Logs info message to ExtentReports
     *
     * @param message Message to log
     */
    public static void logInfo(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.info(message);
        }
        logger.info(message);
    }

    /**
     * Logs pass message to ExtentReports
     *
     * @param message Message to log
     */
    public static void logPass(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.pass(message);
        }
        logger.info(message);
    }

    /**
     * Logs fail message to ExtentReports
     *
     * @param message Message to log
     */
    public static void logFail(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.fail(message);
        }
        logger.error(message);
    }

    /**
     * Logs warning message to ExtentReports
     *
     * @param message Message to log
     */
    public static void logWarning(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.warning(message);
        }
        logger.warn(message);
    }
}
