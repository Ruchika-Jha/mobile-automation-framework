package com.mobile.framework.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * FormPage - Page Object for Form Screen
 *
 * This class represents a form screen with various input types.
 * It demonstrates handling different form elements like text fields,
 * dropdowns, checkboxes, and buttons.
 *
 * Features:
 * - Form field interactions
 * - Dropdown selection
 * - Checkbox handling
 * - Form submission
 * - Form validation
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class FormPage extends BasePage {

    // ========== PAGE ELEMENTS ==========

    /**
     * Form title/header
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/formTitle")
    @iOSXCUITFindBy(accessibility = "formTitle")
    private WebElement formTitle;

    /**
     * Name input field
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/nameField")
    @iOSXCUITFindBy(accessibility = "nameField")
    private WebElement nameField;

    /**
     * Email input field
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/emailField")
    @iOSXCUITFindBy(accessibility = "emailField")
    private WebElement emailField;

    /**
     * Phone number input field
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/phoneField")
    @iOSXCUITFindBy(accessibility = "phoneField")
    private WebElement phoneField;

    /**
     * Address input field
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/addressField")
    @iOSXCUITFindBy(accessibility = "addressField")
    private WebElement addressField;

    /**
     * Country dropdown
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/countryDropdown")
    @iOSXCUITFindBy(accessibility = "countryDropdown")
    private WebElement countryDropdown;

    /**
     * Terms and conditions checkbox
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/termsCheckbox")
    @iOSXCUITFindBy(accessibility = "termsCheckbox")
    private WebElement termsCheckbox;

    /**
     * Newsletter subscription checkbox
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/newsletterCheckbox")
    @iOSXCUITFindBy(accessibility = "newsletterCheckbox")
    private WebElement newsletterCheckbox;

    /**
     * Submit button
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/submitButton")
    @iOSXCUITFindBy(accessibility = "submitButton")
    private WebElement submitButton;

    /**
     * Cancel button
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/cancelButton")
    @iOSXCUITFindBy(accessibility = "cancelButton")
    private WebElement cancelButton;

    /**
     * Success message
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/successMessage")
    @iOSXCUITFindBy(accessibility = "successMessage")
    private WebElement successMessage;

    /**
     * Error message for name field
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/nameError")
    @iOSXCUITFindBy(accessibility = "nameError")
    private WebElement nameErrorMessage;

    /**
     * Error message for email field
     */
    @AndroidFindBy(id = "com.example.sampleapp:id/emailError")
    @iOSXCUITFindBy(accessibility = "emailError")
    private WebElement emailErrorMessage;

    // ========== PAGE ACTIONS ==========

    /**
     * Enters name in the name field
     *
     * @param name Name to enter
     * @return FormPage instance for method chaining
     */
    public FormPage enterName(String name) {
        logger.info("Entering name: {}", name);
        sendKeys(nameField, name);
        return this;
    }

    /**
     * Enters email in the email field
     *
     * @param email Email to enter
     * @return FormPage instance for method chaining
     */
    public FormPage enterEmail(String email) {
        logger.info("Entering email: {}", email);
        sendKeys(emailField, email);
        return this;
    }

    /**
     * Enters phone number in the phone field
     *
     * @param phone Phone number to enter
     * @return FormPage instance for method chaining
     */
    public FormPage enterPhone(String phone) {
        logger.info("Entering phone: {}", phone);
        sendKeys(phoneField, phone);
        return this;
    }

    /**
     * Enters address in the address field
     *
     * @param address Address to enter
     * @return FormPage instance for method chaining
     */
    public FormPage enterAddress(String address) {
        logger.info("Entering address: {}", address);
        sendKeys(addressField, address);
        return this;
    }

    /**
     * Selects country from dropdown
     *
     * @param country Country name to select
     * @return FormPage instance for method chaining
     */
    public FormPage selectCountry(String country) {
        logger.info("Selecting country: {}", country);
        click(countryDropdown);
        // Platform-specific dropdown handling would go here
        // This is a simplified example
        return this;
    }

    /**
     * Checks/unchecks terms and conditions checkbox
     *
     * @param check true to check, false to uncheck
     * @return FormPage instance for method chaining
     */
    public FormPage setTermsCheckbox(boolean check) {
        logger.info("Setting terms checkbox to: {}", check);
        boolean isChecked = termsCheckbox.getAttribute("checked").equals("true");
        if (check != isChecked) {
            click(termsCheckbox);
        }
        return this;
    }

    /**
     * Checks/unchecks newsletter subscription checkbox
     *
     * @param check true to check, false to uncheck
     * @return FormPage instance for method chaining
     */
    public FormPage setNewsletterCheckbox(boolean check) {
        logger.info("Setting newsletter checkbox to: {}", check);
        boolean isChecked = newsletterCheckbox.getAttribute("checked").equals("true");
        if (check != isChecked) {
            click(newsletterCheckbox);
        }
        return this;
    }

    /**
     * Clicks submit button
     *
     * @return FormPage instance
     */
    public FormPage clickSubmit() {
        logger.info("Clicking submit button");
        hideKeyboard();
        click(submitButton);
        return this;
    }

    /**
     * Clicks cancel button
     *
     * @return HomePage instance
     */
    public HomePage clickCancel() {
        logger.info("Clicking cancel button");
        click(cancelButton);
        return new HomePage();
    }

    /**
     * Fills complete form with all details
     * This is a composite action combining multiple steps
     *
     * @param name Name
     * @param email Email
     * @param phone Phone number
     * @param address Address
     * @param country Country
     * @return FormPage instance
     */
    public FormPage fillForm(String name, String email, String phone, String address, String country) {
        logger.info("Filling form with provided details");
        enterName(name);
        enterEmail(email);
        enterPhone(phone);
        enterAddress(address);
        selectCountry(country);
        setTermsCheckbox(true);
        return this;
    }

    /**
     * Submits form after filling all details
     *
     * @param name Name
     * @param email Email
     * @param phone Phone number
     * @param address Address
     * @param country Country
     * @return FormPage instance
     */
    public FormPage submitForm(String name, String email, String phone, String address, String country) {
        logger.info("Submitting form");
        fillForm(name, email, phone, address, country);
        return clickSubmit();
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Verifies form page is displayed
     *
     * @return true if form page is displayed
     */
    public boolean isFormPageDisplayed() {
        logger.info("Verifying form page is displayed");
        return isElementDisplayed(formTitle);
    }

    /**
     * Checks if submit button is enabled
     *
     * @return true if submit button is enabled
     */
    public boolean isSubmitButtonEnabled() {
        return isElementEnabled(submitButton);
    }

    /**
     * Checks if success message is displayed
     *
     * @return true if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }

    /**
     * Gets success message text
     *
     * @return Success message text
     */
    public String getSuccessMessage() {
        logger.info("Getting success message");
        return getText(successMessage);
    }

    /**
     * Checks if name error message is displayed
     *
     * @return true if name error is displayed
     */
    public boolean isNameErrorDisplayed() {
        return isElementDisplayed(nameErrorMessage);
    }

    /**
     * Gets name error message text
     *
     * @return Name error message
     */
    public String getNameErrorMessage() {
        logger.info("Getting name error message");
        return getText(nameErrorMessage);
    }

    /**
     * Checks if email error message is displayed
     *
     * @return true if email error is displayed
     */
    public boolean isEmailErrorDisplayed() {
        return isElementDisplayed(emailErrorMessage);
    }

    /**
     * Gets email error message text
     *
     * @return Email error message
     */
    public String getEmailErrorMessage() {
        logger.info("Getting email error message");
        return getText(emailErrorMessage);
    }

    /**
     * Verifies all form fields are displayed
     *
     * @return true if all fields are visible
     */
    public boolean verifyFormFieldsDisplayed() {
        logger.info("Verifying all form fields are displayed");
        return isFormPageDisplayed() &&
                isElementDisplayed(nameField) &&
                isElementDisplayed(emailField) &&
                isElementDisplayed(phoneField) &&
                isElementDisplayed(addressField) &&
                isElementDisplayed(countryDropdown) &&
                isElementDisplayed(termsCheckbox) &&
                isElementDisplayed(submitButton);
    }

    /**
     * Clears all form fields
     *
     * @return FormPage instance
     */
    public FormPage clearForm() {
        logger.info("Clearing all form fields");
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        return this;
    }

    /**
     * Scrolls to submit button
     *
     * @return FormPage instance
     */
    public FormPage scrollToSubmitButton() {
        logger.info("Scrolling to submit button");
        scrollToElement(submitButton, 5);
        return this;
    }
}
