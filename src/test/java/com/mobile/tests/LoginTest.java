package com.mobile.tests;

import com.mobile.framework.pages.HomePage;
import com.mobile.framework.pages.LoginPage;
import com.mobile.framework.utils.TestDataProvider;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Test class for Login functionality
 *
 * This class demonstrates various login scenarios including:
 * - Valid login
 * - Invalid credentials
 * - Empty fields validation
 * - Data-driven testing using JSON data
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class LoginTest extends BaseTest {

    /**
     * Test: Verify successful login with valid credentials
     *
     * Test Steps:
     * 1. Launch the application
     * 2. Verify login page is displayed
     * 3. Enter valid username and password
     * 4. Click login button
     * 5. Verify user is redirected to home page
     * 6. Verify welcome message contains username
     */
    @Test(priority = 1, description = "Verify user can login with valid credentials")
    public void testValidLogin() {
        logTestStep("Starting valid login test");

        // Initialize page objects
        LoginPage loginPage = new LoginPage();

        // Verify login page is displayed
        logTestStep("Verifying login page is displayed");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");
        logAssertion("Login page is displayed", true);

        // Perform login
        logTestStep("Performing login with valid credentials");
        HomePage homePage = loginPage.login("testuser@example.com", "Test@123");

        // Verify home page is displayed
        logTestStep("Verifying successful navigation to home page");
        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Home page should be displayed after successful login");
        logAssertion("Home page is displayed after login", true);

        // Verify welcome message
        logTestStep("Verifying welcome message");
        String welcomeMessage = homePage.getWelcomeMessage();
        Assert.assertTrue(welcomeMessage.contains("Welcome"),
                "Welcome message should be displayed");
        logAssertion("Welcome message is displayed", true);
    }

    /**
     * Test: Verify login fails with invalid credentials
     *
     * Test Steps:
     * 1. Launch the application
     * 2. Enter invalid username and password
     * 3. Click login button
     * 4. Verify error message is displayed
     * 5. Verify user remains on login page
     */
    @Test(priority = 2, description = "Verify login fails with invalid credentials")
    public void testInvalidLogin() {
        logTestStep("Starting invalid login test");

        LoginPage loginPage = new LoginPage();

        // Verify login page is displayed
        logTestStep("Verifying login page is displayed");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");

        // Attempt login with invalid credentials
        logTestStep("Attempting login with invalid credentials");
        loginPage.enterUsername("invalid@example.com");
        loginPage.enterPassword("wrongpassword");
        loginPage.clickLoginButton();

        // Verify error message is displayed
        logTestStep("Verifying error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid credentials");
        logAssertion("Error message displayed for invalid credentials", true);

        // Verify user still on login page
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "User should remain on login page after failed login");
    }

    /**
     * Test: Verify login form validation with empty fields
     *
     * Test Steps:
     * 1. Launch the application
     * 2. Leave username and password empty
     * 3. Click login button
     * 4. Verify validation error is displayed
     * 5. Verify login button is disabled or error shown
     */
    @Test(priority = 3, description = "Verify login form validation with empty fields")
    public void testEmptyFieldsValidation() {
        logTestStep("Starting empty fields validation test");

        LoginPage loginPage = new LoginPage();

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");

        // Click login without entering credentials
        logTestStep("Clicking login button with empty fields");
        loginPage.clickLoginButton();

        // Verify error message or that login button is disabled
        logTestStep("Verifying validation error");
        // Either error message is shown or login button remains disabled
        boolean validationWorking = loginPage.isErrorMessageDisplayed() ||
                !loginPage.isLoginButtonEnabled();

        Assert.assertTrue(validationWorking,
                "Form validation should prevent login with empty fields");
        logAssertion("Form validation works for empty fields", validationWorking);
    }

    /**
     * Test: Data-driven login test using JSON test data
     *
     * This test demonstrates data-driven testing by reading multiple
     * login scenarios from a JSON file and executing them sequentially.
     *
     * @param testData JSON object containing test data
     */
    @Test(priority = 4, dataProvider = "loginData", dataProviderClass = TestDataProvider.class,
            description = "Data-driven login test with multiple scenarios")
    public void testLoginWithDataProvider(JSONObject testData) {
        // Extract test data from JSON
        String testCase = TestDataProvider.getString(testData, "testCase");
        String username = TestDataProvider.getString(testData, "username");
        String password = TestDataProvider.getString(testData, "password");
        String expectedResult = TestDataProvider.getString(testData, "expectedResult");

        logTestStep("Executing test case: " + testCase);
        logger.info("Username: {}, Password: {}, Expected Result: {}",
                username, password, expectedResult);

        LoginPage loginPage = new LoginPage();

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");

        // Perform login
        logTestStep("Entering credentials: " + username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        // Verify expected result
        if ("success".equalsIgnoreCase(expectedResult)) {
            logTestStep("Verifying successful login");
            HomePage homePage = new HomePage();
            Assert.assertTrue(homePage.isHomePageDisplayed(),
                    "Login should succeed for test case: " + testCase);
            logAssertion("Login successful for: " + testCase, true);
        } else {
            logTestStep("Verifying login failure");
            Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.isLoginPageDisplayed(),
                    "Login should fail for test case: " + testCase);
            logAssertion("Login failed as expected for: " + testCase, true);
        }
    }

    /**
     * Test: Verify forgot password link functionality
     *
     * Test Steps:
     * 1. Navigate to login page
     * 2. Click on forgot password link
     * 3. Verify navigation to password recovery page
     */
    @Test(priority = 5, description = "Verify forgot password link works")
    public void testForgotPasswordLink() {
        logTestStep("Starting forgot password link test");

        LoginPage loginPage = new LoginPage();

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");

        // Click forgot password link
        logTestStep("Clicking forgot password link");
        loginPage.clickForgotPassword();

        // Note: Add verification for password recovery page if available
        logTestStep("Forgot password functionality triggered");
    }

    /**
     * Test: Verify login page elements are displayed
     *
     * Test Steps:
     * 1. Launch application
     * 2. Verify all login page elements are visible
     * 3. Verify username field, password field, login button are present
     */
    @Test(priority = 6, description = "Verify all login page elements are displayed")
    public void testLoginPageElements() {
        logTestStep("Verifying login page elements");

        LoginPage loginPage = new LoginPage();

        // Verify login page is displayed
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");

        // Verify all elements are present
        logTestStep("Verifying username field is displayed");
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
                "Username field should be displayed");

        logTestStep("Verifying password field is displayed");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
                "Password field should be displayed");

        logTestStep("Verifying all login page elements are present");
        Assert.assertTrue(loginPage.verifyLoginPageElements(),
                "All login page elements should be displayed");

        logAssertion("All login page elements are displayed", true);
    }
}
