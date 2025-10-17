package com.mobile.framework.utils;

import com.mobile.framework.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDataProvider - Utility class for providing test data to test methods
 *
 * This class reads test data from JSON files and provides it to TestNG tests
 * using the @DataProvider annotation. It supports data-driven testing by
 * allowing multiple test scenarios from external data files.
 *
 * Features:
 * - JSON file parsing
 * - Dynamic data provider creation
 * - Support for multiple test data sets
 * - Error handling and logging
 *
 * @author Mobile Automation Team
 * @version 1.0
 */
public class TestDataProvider {

    private static final Logger logger = LogManager.getLogger(TestDataProvider.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();

    /**
     * Private constructor to prevent instantiation
     */
    private TestDataProvider() {
    }

    /**
     * Data Provider for login test data
     * Reads login credentials from loginData.json
     *
     * @return Object array containing login test data
     */
    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() {
        String filePath = configReader.getTestDataPath() + "loginData.json";
        return readJsonTestData(filePath);
    }

    /**
     * Data Provider for form test data
     * Reads form data from formData.json
     *
     * @return Object array containing form test data
     */
    @DataProvider(name = "formData")
    public static Object[][] getFormData() {
        String filePath = configReader.getTestDataPath() + "formData.json";
        return readJsonTestData(filePath);
    }

    /**
     * Generic method to read test data from JSON file
     * Parses JSON array and converts to Object[][]
     *
     * @param filePath Path to JSON file
     * @return Object array containing test data
     */
    private static Object[][] readJsonTestData(String filePath) {
        logger.info("Reading test data from: {}", filePath);

        List<Object[]> testDataList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON file
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Convert each JSON object to Object array
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                testDataList.add(new Object[]{jsonObject});
            }

            logger.info("Successfully loaded {} test data sets from {}", testDataList.size(), filePath);

        } catch (Exception e) {
            logger.error("Failed to read test data from {}: {}", filePath, e.getMessage());
            // Return empty array to prevent test execution failure
            return new Object[0][0];
        }

        // Convert List to 2D array
        return testDataList.toArray(new Object[testDataList.size()][]);
    }

    /**
     * Generic data provider that accepts file name as parameter
     * Allows flexible data provider usage
     *
     * @param fileName Name of the JSON file (without path)
     * @return Object array containing test data
     */
    public static Object[][] getTestData(String fileName) {
        String filePath = configReader.getTestDataPath() + fileName;
        return readJsonTestData(filePath);
    }

    /**
     * Helper method to extract string value from JSON object
     *
     * @param jsonObject JSON object
     * @param key Key to extract
     * @return String value or empty string if key not found
     */
    public static String getString(JSONObject jsonObject, String key) {
        Object value = jsonObject.get(key);
        return value != null ? value.toString() : "";
    }

    /**
     * Helper method to extract boolean value from JSON object
     *
     * @param jsonObject JSON object
     * @param key Key to extract
     * @return Boolean value or false if key not found
     */
    public static boolean getBoolean(JSONObject jsonObject, String key) {
        Object value = jsonObject.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    /**
     * Helper method to extract integer value from JSON object
     *
     * @param jsonObject JSON object
     * @param key Key to extract
     * @return Integer value or 0 if key not found
     */
    public static int getInt(JSONObject jsonObject, String key) {
        Object value = jsonObject.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
}
