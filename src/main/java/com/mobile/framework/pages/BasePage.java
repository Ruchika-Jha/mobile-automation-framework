package com.mobile.framework.pages;

import com.mobile.framework.config.ConfigReader;
import com.mobile.framework.utils.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * BasePage - Foundation class for all Page Object classes
 *
 * This class provides common mobile actions and utilities that can be reused
 * across all page objects. It implements the Page Object Model (POM) pattern
 * and includes mobile-specific gestures and wait mechanisms.
 *
 * Features:
 * - Reusable mobile gestures (tap, swipe, scroll, long press)
 * - Smart wait mechanisms (explicit waits, fluent waits)
 * - Element visibility and interaction checks
 * - Platform-specific handling (Android/iOS)
 * - Comprehensive logging
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class BasePage {

    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected ConfigReader configReader;

    /**
     * Constructor - Initializes driver and PageFactory
     * Uses AppiumFieldDecorator for mobile element initialization
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.configReader = ConfigReader.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(configReader.getExplicitWait()));

        // Initialize page elements using Appium's PageFactory
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ========== WAIT UTILITIES ==========

    /**
     * Waits for element to be visible
     *
     * @param element WebElement to wait for
     * @param timeoutInSeconds Custom timeout
     * @return WebElement once visible
     */
    protected WebElement waitForElementToBeVisible(WebElement element, int timeoutInSeconds) {
        logger.debug("Waiting for element to be visible");
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for element to be visible (uses default timeout)
     *
     * @param element WebElement to wait for
     * @return WebElement once visible
     */
    protected WebElement waitForElementToBeVisible(WebElement element) {
        return waitForElementToBeVisible(element, configReader.getExplicitWait());
    }

    /**
     * Waits for element to be clickable
     *
     * @param element WebElement to wait for
     * @return WebElement once clickable
     */
    protected WebElement waitForElementToBeClickable(WebElement element) {
        logger.debug("Waiting for element to be clickable");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for element to be present by locator
     *
     * @param by Locator strategy
     * @return WebElement once present
     */
    protected WebElement waitForElementToBePresent(By by) {
        logger.debug("Waiting for element to be present: {}", by);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * Waits for element to disappear
     *
     * @param element WebElement to wait for disappearance
     * @return true if element is no longer visible
     */
    protected boolean waitForElementToDisappear(WebElement element) {
        logger.debug("Waiting for element to disappear");
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    // ========== ELEMENT INTERACTION UTILITIES ==========

    /**
     * Clicks on element with wait
     *
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        try {
            waitForElementToBeClickable(element);
            element.click();
            logger.info("Clicked on element successfully");
        } catch (Exception e) {
            logger.error("Failed to click on element: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Enters text into element with wait and clear
     *
     * @param element WebElement to enter text
     * @param text Text to enter
     */
    protected void sendKeys(WebElement element, String text) {
        try {
            waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text: {}", text);
        } catch (Exception e) {
            logger.error("Failed to enter text: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Gets text from element
     *
     * @param element WebElement to get text from
     * @return Text content of element
     */
    protected String getText(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            String text = element.getText();
            logger.info("Retrieved text: {}", text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if element is displayed
     *
     * @param element WebElement to check
     * @return true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            logger.debug("Element not displayed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Checks if element is enabled
     *
     * @param element WebElement to check
     * @return true if enabled, false otherwise
     */
    protected boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            logger.debug("Element not enabled: {}", e.getMessage());
            return false;
        }
    }

    // ========== MOBILE GESTURE UTILITIES ==========

    /**
     * Performs tap gesture on element
     *
     * @param element WebElement to tap
     */
    protected void tap(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            Point location = element.getLocation();
            Dimension size = element.getSize();

            int centerX = location.getX() + (size.getWidth() / 2);
            int centerY = location.getY() + (size.getHeight() / 2);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(tap));
            logger.info("Performed tap gesture on element");
        } catch (Exception e) {
            logger.error("Failed to perform tap: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Performs long press on element
     *
     * @param element WebElement to long press
     * @param durationInSeconds Duration of long press
     */
    protected void longPress(WebElement element, int durationInSeconds) {
        try {
            waitForElementToBeVisible(element);
            Point location = element.getLocation();
            Dimension size = element.getSize();

            int centerX = location.getX() + (size.getWidth() / 2);
            int centerY = location.getY() + (size.getHeight() / 2);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence longPress = new Sequence(finger, 1);
            longPress.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
            longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            longPress.addAction(finger.createPointerMove(Duration.ofSeconds(durationInSeconds), PointerInput.Origin.viewport(), centerX, centerY));
            longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(longPress));
            logger.info("Performed long press gesture for {} seconds", durationInSeconds);
        } catch (Exception e) {
            logger.error("Failed to perform long press: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Swipes from one element to another
     *
     * @param startElement Starting element
     * @param endElement Ending element
     */
    protected void swipe(WebElement startElement, WebElement endElement) {
        try {
            Point startLocation = startElement.getLocation();
            Point endLocation = endElement.getLocation();

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startLocation.getX(), startLocation.getY()));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endLocation.getX(), endLocation.getY()));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(swipe));
            logger.info("Performed swipe gesture");
        } catch (Exception e) {
            logger.error("Failed to perform swipe: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Scrolls down on the screen
     */
    protected void scrollDown() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.8);
        int endY = (int) (size.getHeight() * 0.2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
        logger.info("Scrolled down");
    }

    /**
     * Scrolls up on the screen
     */
    protected void scrollUp() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.2);
        int endY = (int) (size.getHeight() * 0.8);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
        logger.info("Scrolled up");
    }

    /**
     * Scrolls to element until it's visible
     *
     * @param element Element to scroll to
     * @param maxScrolls Maximum number of scroll attempts
     * @return true if element found, false otherwise
     */
    protected boolean scrollToElement(WebElement element, int maxScrolls) {
        for (int i = 0; i < maxScrolls; i++) {
            if (isElementDisplayed(element)) {
                logger.info("Element found after {} scrolls", i);
                return true;
            }
            scrollDown();
        }
        logger.warn("Element not found after {} scrolls", maxScrolls);
        return false;
    }

    // ========== PLATFORM-SPECIFIC UTILITIES ==========

    /**
     * Checks if current platform is Android
     *
     * @return true if Android, false otherwise
     */
    protected boolean isAndroid() {
        return driver instanceof AndroidDriver;
    }

    /**
     * Checks if current platform is iOS
     *
     * @return true if iOS, false otherwise
     */
    protected boolean isIOS() {
        return driver instanceof IOSDriver;
    }

    /**
     * Hides keyboard if visible
     */
    protected void hideKeyboard() {
        try {
            driver.hideKeyboard();
            logger.info("Keyboard hidden");
        } catch (Exception e) {
            logger.debug("Keyboard not visible or already hidden");
        }
    }
}
