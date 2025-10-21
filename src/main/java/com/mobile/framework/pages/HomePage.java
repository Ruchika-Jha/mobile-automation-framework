package com.mobile.framework.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * HomePage - Page Object for Home Screen
 *
 * This class represents the home screen that appears after successful login.
 * It demonstrates navigation, menu interactions, and list handling.
 *
 * Features:
 * - Navigation to different sections
 * - Profile management
 * - Menu interactions
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class HomePage extends BasePage {

    // ========== PAGE ELEMENTS ==========
    // Sauce Labs Demo App - Products Page Locators

    /**
     * Products page header/title (after login)
     */
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='PRODUCTS']")
    @iOSXCUITFindBy(accessibility = "PRODUCTS")
    private WebElement productsHeader;

    /**
     * Menu/hamburger icon (for accessing logout)
     */
    @AndroidFindBy(accessibility = "test-Menu")
    @iOSXCUITFindBy(accessibility = "test-Menu")
    private WebElement menuIcon;

    /**
     * Cart icon
     */
    @AndroidFindBy(accessibility = "test-Cart")
    @iOSXCUITFindBy(accessibility = "test-Cart")
    private WebElement cartIcon;

    /**
     * Logout button (in menu)
     */
    @AndroidFindBy(accessibility = "test-LOGOUT")
    @iOSXCUITFindBy(accessibility = "test-LOGOUT")
    private WebElement logoutButton;

    /**
     * Product items list
     */
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Item']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-Item']")
    private List<WebElement> productItemsList;

    /**
     * First product - Sauce Labs Backpack
     */
    @AndroidFindBy(xpath = "(//android.view.ViewGroup[@content-desc='test-Item'])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeOther[@name='test-Item'])[1]")
    private WebElement firstProduct;

    /**
     * Sort dropdown button
     */
    @AndroidFindBy(accessibility = "test-Modal Selector Button")
    @iOSXCUITFindBy(accessibility = "test-Modal Selector Button")
    private WebElement sortButton;

    // ========== PAGE ACTIONS ==========

    /**
     * Gets products header text
     *
     * @return Products header string
     */
    public String getProductsHeader() {
        logger.info("Getting products header");
        return getText(productsHeader);
    }

    /**
     * Opens menu by clicking hamburger icon
     *
     * @return Current HomePage instance
     */
    public HomePage openMenu() {
        logger.info("Opening menu");
        click(menuIcon);
        return this;
    }

    /**
     * Clicks cart icon
     *
     * @return Current HomePage instance
     */
    public HomePage clickCart() {
        logger.info("Clicking cart icon");
        click(cartIcon);
        return this;
    }

    /**
     * Clicks logout button and returns to login page
     *
     * @return LoginPage instance
     */
    public LoginPage logout() {
        logger.info("Logging out");
        openMenu();
        waitForElementToBeVisible(logoutButton);
        click(logoutButton);
        return new LoginPage();
    }

    /**
     * Clicks on first product
     *
     * @return Current HomePage instance
     */
    public HomePage clickFirstProduct() {
        logger.info("Clicking first product");
        click(firstProduct);
        return this;
    }

    /**
     * Clicks sort button
     *
     * @return Current HomePage instance
     */
    public HomePage clickSortButton() {
        logger.info("Clicking sort button");
        click(sortButton);
        return this;
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Verifies products page is displayed
     *
     * @return true if products page is displayed
     */
    public boolean isHomePageDisplayed() {
        logger.info("Verifying products page is displayed");
        return isElementDisplayed(productsHeader);
    }

    /**
     * Verifies products header contains expected text
     *
     * @return true if header shows "PRODUCTS"
     */
    public boolean verifyProductsHeader() {
        logger.info("Verifying products header");
        String headerText = getProductsHeader();
        return headerText.equals("PRODUCTS");
    }

    /**
     * Gets count of product items
     *
     * @return Number of products
     */
    public int getProductItemsCount() {
        logger.info("Getting product items count");
        return productItemsList.size();
    }

    /**
     * Clicks on a specific product by index
     *
     * @param index Index of product to click (0-based)
     * @return Current HomePage instance
     */
    public HomePage clickProductByIndex(int index) {
        logger.info("Clicking product at index: {}", index);
        if (index >= 0 && index < productItemsList.size()) {
            click(productItemsList.get(index));
        } else {
            logger.error("Invalid index: {}. Product list size: {}", index, productItemsList.size());
        }
        return this;
    }

    /**
     * Verifies products page loaded completely
     *
     * @return true if page is fully loaded
     */
    public boolean verifyHomePageLoaded() {
        logger.info("Verifying products page loaded");
        return isHomePageDisplayed() &&
                isElementDisplayed(menuIcon) &&
                isElementDisplayed(cartIcon) &&
                getProductItemsCount() > 0;
    }

    // ========== NAVIGATION METHODS (App-Specific Placeholders) ==========

    /**
     * Gets welcome message (if available in the app)
     * Note: This is a placeholder - update with actual app locator
     *
     * @return Welcome message text
     */
    public String getWelcomeMessage() {
        logger.info("Getting welcome message");
        // Placeholder: Return products header as welcome message
        return getProductsHeader();
    }

    /**
     * Navigates to Forms page
     * Note: This is a placeholder - update with actual app navigation
     *
     * @return FormPage instance
     */
    public FormPage navigateToForms() {
        logger.info("Navigating to forms");
        // Placeholder: For Sauce Labs app, navigate to menu
        openMenu();
        return new FormPage();
    }

    /**
     * Verifies navigation tabs are displayed
     * Note: This is a placeholder - update with actual app locators
     *
     * @return true if navigation tabs are displayed
     */
    public boolean verifyNavigationTabsDisplayed() {
        logger.info("Verifying navigation tabs displayed");
        // Placeholder: Check if menu and cart icons are displayed
        return isElementDisplayed(menuIcon) && isElementDisplayed(cartIcon);
    }

    /**
     * Navigates to Dashboard
     * Note: This is a placeholder - update with actual app navigation
     *
     * @return HomePage instance
     */
    public HomePage navigateToDashboard() {
        logger.info("Navigating to dashboard");
        // Placeholder: Stay on current page
        return this;
    }

    /**
     * Navigates to Settings
     * Note: This is a placeholder - update with actual app navigation
     *
     * @return HomePage instance
     */
    public HomePage navigateToSettings() {
        logger.info("Navigating to settings");
        // Placeholder: Open menu (settings might be in menu)
        openMenu();
        return this;
    }

    /**
     * Clicks profile icon
     * Note: This is a placeholder - update with actual app locator
     *
     * @return HomePage instance
     */
    public HomePage clickProfileIcon() {
        logger.info("Clicking profile icon");
        // Placeholder: Click menu icon
        click(menuIcon);
        return this;
    }

    /**
     * Performs search with given query
     * Note: This is a placeholder - update with actual app search functionality
     *
     * @param query Search query
     * @return HomePage instance
     */
    public HomePage search(String query) {
        logger.info("Searching for: {}", query);
        // Placeholder: No search in Sauce Labs demo app
        logger.warn("Search functionality not implemented for this app");
        return this;
    }

    /**
     * Clicks notification icon
     * Note: This is a placeholder - update with actual app locator
     *
     * @return HomePage instance
     */
    public HomePage clickNotificationIcon() {
        logger.info("Clicking notification icon");
        // Placeholder: Click cart icon
        click(cartIcon);
        return this;
    }

    /**
     * Checks if notification badge is displayed
     * Note: This is a placeholder - update with actual app locator
     *
     * @return true if notification badge is displayed
     */
    public boolean isNotificationBadgeDisplayed() {
        logger.info("Checking notification badge");
        // Placeholder: Return false
        return false;
    }

    /**
     * Gets notification count
     * Note: This is a placeholder - update with actual app logic
     *
     * @return Notification count
     */
    public int getNotificationCount() {
        logger.info("Getting notification count");
        // Placeholder: Return 0
        return 0;
    }
}
