package com.mobile.framework.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * LoginPage - Page Object for Login Screen
 *
 * This class represents the login screen of the mobile application.
 * It demonstrates the Page Object Model pattern with mobile-specific
 * element locators using Appium's @AndroidFindBy and @iOSXCUITFindBy annotations.
 *
 * Features:
 * - Platform-specific element locators
 * - Reusable login methods
 * - Validation methods
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class LoginPage extends BasePage {

    // ========== PAGE ELEMENTS ==========
    // Sauce Labs Demo App Locators

    /**
     * Username input field
     * Sauce Labs app locators for Android and iOS
     */
    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private WebElement usernameField;

    /**
     * Password input field
     */
    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private WebElement passwordField;

    /**
     * Login button
     */
    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    /**
     * Error message text (displayed on invalid login)
     */
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(accessibility = "test-Error message")
    private WebElement errorMessage;

    /**
     * App logo/title (Swag Labs logo)
     */
    @AndroidFindBy(xpath = "//android.widget.ImageView[contains(@content-desc, 'Swag')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage[contains(@name, 'Swag')]")
    private WebElement appLogo;

    // ========== PAGE ACTIONS ==========

    /**
     * Enters username in the username field
     *
     * @param username Username to enter
     * @return LoginPage instance for method chaining
     */
    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        sendKeys(usernameField, username);
        return this;
    }

    /**
     * Enters password in the password field
     *
     * @param password Password to enter
     * @return LoginPage instance for method chaining
     */
    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        sendKeys(passwordField, password);
        return this;
    }

    /**
     * Clicks the login button
     *
     * @return HomePage instance (next page after successful login)
     */
    public HomePage clickLoginButton() {
        logger.info("Clicking login button");
        click(loginButton);
        return new HomePage();
    }

    /**
     * Performs complete login action with username and password
     * This is a composite action combining multiple steps
     *
     * @param username Username
     * @param password Password
     * @return HomePage instance
     */
    public HomePage login(String username, String password) {
        logger.info("Performing login with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        hideKeyboard(); // Hide keyboard before clicking login
        return clickLoginButton();
    }


    // ========== VALIDATION METHODS ==========

    /**
     * Checks if login page is displayed by verifying app logo
     *
     * @return true if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        logger.info("Verifying login page is displayed");
        try {
            // Wait for app logo to be visible (more reliable)
            waitForElementToBeVisible(appLogo, 20);
            return true;
        } catch (Exception e) {
            logger.warn("App logo not found, checking for username field instead");
            // Fallback: Check if username field is displayed
            try {
                waitForElementToBeVisible(usernameField, 10);
                return true;
            } catch (Exception ex) {
                logger.error("Login page elements not found: {}", ex.getMessage());
                return false;
            }
        }
    }

    /**
     * Checks if username field is displayed
     *
     * @return true if username field is displayed
     */
    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(usernameField);
    }

    /**
     * Checks if password field is displayed
     *
     * @return true if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField);
    }

    /**
     * Checks if login button is enabled
     *
     * @return true if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        return isElementEnabled(loginButton);
    }

    /**
     * Checks if error message is displayed
     *
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    /**
     * Gets the error message text
     *
     * @return Error message text
     */
    public String getErrorMessage() {
        logger.info("Getting error message text");
        return getText(errorMessage);
    }

    /**
     * Verifies login page elements are loaded
     *
     * @return true if all critical elements are displayed
     */
    public boolean verifyLoginPageElements() {
        logger.info("Verifying all login page elements");
        return isLoginPageDisplayed() &&
                isUsernameFieldDisplayed() &&
                isPasswordFieldDisplayed() &&
                isElementDisplayed(loginButton);
    }

    /**
     * Clears username field
     *
     * @return LoginPage instance
     */
    public LoginPage clearUsername() {
        logger.info("Clearing username field");
        waitForElementToBeVisible(usernameField);
        usernameField.clear();
        return this;
    }

    /**
     * Clears password field
     *
     * @return LoginPage instance
     */
    public LoginPage clearPassword() {
        logger.info("Clearing password field");
        waitForElementToBeVisible(passwordField);
        passwordField.clear();
        return this;
    }

    /**
     * Clears both username and password fields
     *
     * @return LoginPage instance
     */
    public LoginPage clearLoginFields() {
        logger.info("Clearing login fields");
        clearUsername();
        clearPassword();
        return this;
    }

    /**
     * Clicks forgot password link
     * Note: This is a placeholder - update with actual app locator if available
     *
     * @return LoginPage instance
     */
    public LoginPage clickForgotPassword() {
        logger.info("Clicking forgot password link");
        // Placeholder: Sauce Labs demo app may not have forgot password
        logger.warn("Forgot password functionality not available in this app");
        return this;
    }
}
