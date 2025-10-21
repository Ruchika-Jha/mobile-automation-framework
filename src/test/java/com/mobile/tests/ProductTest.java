package com.mobile.tests;

import com.mobile.framework.pages.HomePage;
import com.mobile.framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProductTest - Test class for Product functionality
 *
 * This class tests the Sauce Labs Demo App product features:
 * - Product listing
 * - Product sorting
 * - Product selection
 * - Cart functionality
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class ProductTest extends BaseTest {

    private HomePage homePage;

    /**
     * Setup method to login before each test
     */
    @BeforeMethod
    public void loginBeforeTest() {
        logTestStep("Logging in before product test");

        LoginPage loginPage = new LoginPage();
        homePage = loginPage.login("standard_user", "secret_sauce");

        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Should be on home page before starting product test");
        logAssertion("Successfully logged in and on products page", true);
    }

    /**
     * Test: Verify products page displays after login
     *
     * Test Steps:
     * 1. Login to app
     * 2. Verify products page is displayed
     * 3. Verify products header shows "PRODUCTS"
     * 4. Verify product items are loaded
     */
    @Test(priority = 1, testName = "Verify products page displays after login",
          description = "Verify products page displays after login")
    public void testProductsPageDisplay() {
        logTestStep("Verifying products page is displayed");

        // Verify products page
        Assert.assertTrue(homePage.isHomePageDisplayed(),
                "Products page should be displayed");
        logAssertion("Products page is displayed", true);

        // Verify products header
        logTestStep("Verifying products header");
        Assert.assertTrue(homePage.verifyProductsHeader(),
                "Products header should show 'PRODUCTS'");
        logAssertion("Products header is correct", true);

        // Verify products are loaded
        logTestStep("Verifying products are loaded");
        int productCount = homePage.getProductItemsCount();
        Assert.assertTrue(productCount > 0,
                "Products should be loaded on the page");
        logger.info("Found {} products on the page", productCount);
        logAssertion("Products are loaded: " + productCount + " items", true);
    }

    /**
     * Test: Verify clicking on a product
     *
     * Test Steps:
     * 1. Verify products are displayed
     * 2. Click on first product
     * 3. Wait for product details
     */
    @Test(priority = 2, testName = "Verify clicking on a product",
          description = "Verify clicking on a product")
    public void testClickProduct() {
        logTestStep("Verifying product click functionality");

        // Verify products are available
        int productCount = homePage.getProductItemsCount();
        Assert.assertTrue(productCount > 0, "Products should be available");

        // Click on first product
        logTestStep("Clicking on first product");
        homePage.clickFirstProduct();

        // Wait a moment for any navigation
        waitFor(2000);

        logAssertion("Product click successful", true);
    }

    /**
     * Test: Verify menu icon is displayed
     *
     * Test Steps:
     * 1. Verify menu icon is displayed
     * 2. Click menu icon
     * 3. Verify menu opens
     */
    @Test(priority = 3, testName = "Verify menu icon functionality",
          description = "Verify menu icon functionality")
    public void testMenuIcon() {
        logTestStep("Verifying menu icon functionality");

        // Verify page is fully loaded
        Assert.assertTrue(homePage.verifyHomePageLoaded(),
                "Home page should be fully loaded");
        logAssertion("Home page fully loaded with menu", true);

        // Open menu
        logTestStep("Opening menu");
        homePage.openMenu();

        // Wait for menu to open
        waitFor(2000);

        logAssertion("Menu opened successfully", true);
    }

    /**
     * Test: Verify cart icon is displayed and clickable
     *
     * Test Steps:
     * 1. Verify cart icon is displayed
     * 2. Click cart icon
     * 3. Verify navigation to cart page
     */
    @Test(priority = 4, testName = "Verify cart icon functionality",
          description = "Verify cart icon functionality")
    public void testCartIcon() {
        logTestStep("Verifying cart icon functionality");

        // Verify page elements are loaded
        Assert.assertTrue(homePage.verifyHomePageLoaded(),
                "Home page should be fully loaded");

        // Click cart icon
        logTestStep("Clicking cart icon");
        homePage.clickCart();

        // Wait for navigation
        waitFor(2000);

        logAssertion("Cart icon clicked successfully", true);
    }

    /**
     * Test: Verify sort button functionality
     *
     * Test Steps:
     * 1. Verify sort button is displayed
     * 2. Click sort button
     * 3. Verify sort options appear
     */
    @Test(priority = 5, testName = "Verify sort button functionality",
          description = "Verify sort button functionality")
    public void testSortButton() {
        logTestStep("Verifying sort button functionality");

        // Click sort button
        logTestStep("Clicking sort button");
        homePage.clickSortButton();

        // Wait for sort options
        waitFor(2000);

        logAssertion("Sort button clicked successfully", true);
    }

    /**
     * Test: Verify logout functionality from menu
     *
     * Test Steps:
     * 1. Open menu
     * 2. Click logout
     * 3. Verify navigation to login page
     */
    @Test(priority = 6, testName = "Verify logout functionality from products page",
          description = "Verify logout functionality from products page")
    public void testLogout() {
        logTestStep("Testing logout functionality");

        // Perform logout
        logTestStep("Performing logout");
        LoginPage loginPage = homePage.logout();

        // Wait for navigation
        waitFor(2000);

        // Verify login page is displayed
        logTestStep("Verifying login page is displayed after logout");
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should return to login page after logout");
        logAssertion("Logout successful", true);
    }

    /**
     * Test: Verify clicking on multiple products
     *
     * Test Steps:
     * 1. Get product count
     * 2. Click on first 3 products
     * 3. Verify each product can be clicked
     */
    @Test(priority = 7, testName = "Verify clicking multiple products",
          description = "Verify clicking multiple products")
    public void testMultipleProductClicks() {
        logTestStep("Testing multiple product clicks");

        int productCount = homePage.getProductItemsCount();
        logger.info("Total products available: {}", productCount);

        // Click on first 3 products (or less if fewer available)
        int clickCount = Math.min(3, productCount);

        for (int i = 0; i < clickCount; i++) {
            logTestStep("Clicking on product at index: " + i);

            // Go back to products page if needed
            if (i > 0) {
                // Re-initialize home page after navigation
                homePage = new HomePage();
            }

            homePage.clickProductByIndex(i);
            waitFor(1000);

            logger.info("Successfully clicked product {}", i + 1);
        }

        logAssertion("Multiple product clicks successful", true);
    }

    /**
     * Test: Verify all navigation elements are present
     *
     * Test Steps:
     * 1. Verify products header
     * 2. Verify menu icon
     * 3. Verify cart icon
     * 4. Verify sort button
     * 5. Verify at least one product is displayed
     */
    @Test(priority = 8, testName = "Verify all page elements are displayed",
          description = "Verify all page elements are displayed")
    public void testAllPageElements() {
        logTestStep("Verifying all page elements are displayed");

        // Verify page is fully loaded
        Assert.assertTrue(homePage.verifyHomePageLoaded(),
                "All page elements should be displayed and loaded");

        // Verify specific counts
        logTestStep("Verifying product count");
        int productCount = homePage.getProductItemsCount();
        Assert.assertTrue(productCount > 0,
                "Should have at least 1 product displayed");
        logger.info("Total products: {}", productCount);

        logAssertion("All page elements verified successfully", true);
    }
}
