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

    /**
     * Welcome message/header
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/welcomeText")
    @iOSXCUITFindBy(accessibility = "welcomeText")
    private WebElement welcomeMessage;

    /**
     * User profile icon
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/profileIcon")
    @iOSXCUITFindBy(accessibility = "profileIcon")
    private WebElement profileIcon;

    /**
     * Menu/hamburger icon
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/menuIcon")
    @iOSXCUITFindBy(accessibility = "menuIcon")
    private WebElement menuIcon;

    /**
     * Search bar
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/searchBar")
    @iOSXCUITFindBy(accessibility = "searchBar")
    private WebElement searchBar;

    /**
     * Navigation tabs - Dashboard
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/dashboardTab")
    @iOSXCUITFindBy(accessibility = "dashboardTab")
    private WebElement dashboardTab;

    /**
     * Navigation tabs - Forms
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/formsTab")
    @iOSXCUITFindBy(accessibility = "formsTab")
    private WebElement formsTab;

    /**
     * Navigation tabs - Settings
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/settingsTab")
    @iOSXCUITFindBy(accessibility = "settingsTab")
    private WebElement settingsTab;

    /**
     * Logout button
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/logoutButton")
    @iOSXCUITFindBy(accessibility = "logoutButton")
    private WebElement logoutButton;

    /**
     * Notification icon
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/notificationIcon")
    @iOSXCUITFindBy(accessibility = "notificationIcon")
    private WebElement notificationIcon;

    /**
     * Notification badge count
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/notificationBadge")
    @iOSXCUITFindBy(accessibility = "notificationBadge")
    private WebElement notificationBadge;

    /**
     * List of items on home screen
     */
    @AndroidFindBy(xpath = "//android.widget.ListView[@resource-id='com.example.sampleapp:id/itemList']/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTable/XCUIElementTypeCell")
    private List<WebElement> homeItemsList;

    // ========== PAGE ACTIONS ==========

    /**
     * Gets welcome message text
     *
     * @return Welcome message string
     */
    public String getWelcomeMessage() {
        logger.info("Getting welcome message");
        return getText(welcomeMessage);
    }

    /**
     * Clicks on profile icon
     *
     * @return Current HomePage instance
     */
    public HomePage clickProfileIcon() {
        logger.info("Clicking profile icon");
        click(profileIcon);
        return this;
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
     * Performs search action
     *
     * @param searchText Text to search
     * @return Current HomePage instance
     */
    public HomePage search(String searchText) {
        logger.info("Searching for: {}", searchText);
        sendKeys(searchBar, searchText);
        hideKeyboard();
        return this;
    }

    /**
     * Navigates to Dashboard tab
     *
     * @return Current HomePage instance
     */
    public HomePage navigateToDashboard() {
        logger.info("Navigating to Dashboard");
        click(dashboardTab);
        return this;
    }

    /**
     * Navigates to Forms tab
     *
     * @return FormPage instance
     */
    public FormPage navigateToForms() {
        logger.info("Navigating to Forms");
        click(formsTab);
        return new FormPage();
    }

    /**
     * Navigates to Settings tab
     *
     * @return Current HomePage instance
     */
    public HomePage navigateToSettings() {
        logger.info("Navigating to Settings");
        click(settingsTab);
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
        click(logoutButton);
        return new LoginPage();
    }

    /**
     * Clicks notification icon
     *
     * @return Current HomePage instance
     */
    public HomePage clickNotificationIcon() {
        logger.info("Clicking notification icon");
        click(notificationIcon);
        return this;
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Verifies home page is displayed
     *
     * @return true if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        logger.info("Verifying home page is displayed");
        return isElementDisplayed(welcomeMessage);
    }

    /**
     * Verifies user is logged in by checking welcome message
     *
     * @param expectedUsername Expected username in welcome message
     * @return true if username matches
     */
    public boolean verifyUserLoggedIn(String expectedUsername) {
        logger.info("Verifying user logged in: {}", expectedUsername);
        String welcomeText = getWelcomeMessage();
        return welcomeText.contains(expectedUsername);
    }

    /**
     * Checks if notification badge is displayed
     *
     * @return true if notification badge is visible
     */
    public boolean isNotificationBadgeDisplayed() {
        return isElementDisplayed(notificationBadge);
    }

    /**
     * Gets notification count from badge
     *
     * @return Notification count
     */
    public int getNotificationCount() {
        if (isNotificationBadgeDisplayed()) {
            String badgeText = getText(notificationBadge);
            try {
                return Integer.parseInt(badgeText);
            } catch (NumberFormatException e) {
                logger.warn("Unable to parse notification count: {}", badgeText);
                return 0;
            }
        }
        return 0;
    }

    /**
     * Verifies all navigation tabs are displayed
     *
     * @return true if all tabs are visible
     */
    public boolean verifyNavigationTabsDisplayed() {
        logger.info("Verifying navigation tabs");
        return isElementDisplayed(dashboardTab) &&
                isElementDisplayed(formsTab) &&
                isElementDisplayed(settingsTab);
    }

    /**
     * Gets count of items in home list
     *
     * @return Number of items
     */
    public int getHomeItemsCount() {
        logger.info("Getting home items count");
        return homeItemsList.size();
    }

    /**
     * Clicks on a specific item from home list by index
     *
     * @param index Index of item to click (0-based)
     * @return Current HomePage instance
     */
    public HomePage clickHomeItemByIndex(int index) {
        logger.info("Clicking home item at index: {}", index);
        if (index >= 0 && index < homeItemsList.size()) {
            click(homeItemsList.get(index));
        } else {
            logger.error("Invalid index: {}. List size: {}", index, homeItemsList.size());
        }
        return this;
    }

    /**
     * Verifies home page loaded completely
     *
     * @return true if page is fully loaded
     */
    public boolean verifyHomePageLoaded() {
        logger.info("Verifying home page loaded");
        return isHomePageDisplayed() &&
                isElementDisplayed(profileIcon) &&
                verifyNavigationTabsDisplayed();
    }
}
