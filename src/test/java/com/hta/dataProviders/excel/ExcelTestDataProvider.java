package com.hta.dataProviders.excel;

import com.hta.config.excel.ExcelTestDataCache;
import com.hta.utils.logging.ErrorHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelTestDataProvider {

    /**
     * Gets the value of the specified column for the given index.
     *
     * @param filePath   Path to the Excel file
     * @param sheetName  Name of the sheet to read from
     * @param columnName The name of the column to extract data from
     * @param index      The index of the row to retrieve
     * @return Object[][] containing the value of the specified column for the given index
     */
    public static Object[][] getValueByIndex(String filePath, String sheetName, String columnName, int index) {
        try {
            Map<String, Object> dataMap = ExcelTestDataCache.getTestDataByIndex(filePath, sheetName, index);
            Object value = dataMap.getOrDefault(columnName, "");
            return new Object[][]{{value != null ? value : ""}};
        } catch (Exception error) {
            ErrorHandler.logError(error, "getValueByIndex", "Failed to load test data from file: " + filePath);
            throw new RuntimeException("Failed to load test data", error);
        }
    }

    /**
     * Gets test data from a specified column, filtering out empty and 'Unknown' values.
     *
     * @param filePath   Path to the Excel file
     * @param sheetName  Name of the sheet to read from
     * @param columnName The name of the column to extract data from
     * @return Iterator of test data objects
     */
    public static Iterator<Object[]> getColumnData(String filePath, String sheetName, String columnName) {
        try {
            List<Map<String, Object>> rawData = convertToMapList(
                    ExcelTestDataCache.getTestData(filePath, sheetName)
            );

            List<Object[]> refinedData = new ArrayList<>();
            for (Map<String, Object> row : rawData) {
                Object value = row.get(columnName);
                if (isValidValue(value)) {
                    refinedData.add(new Object[]{value});
                }
            }

            return refinedData.iterator();
        } catch (Exception error) {
            ErrorHandler.logError(error, "getColumnData", "Failed to load test data from file: " + filePath);
            throw new RuntimeException("Failed to load test data", error);
        }
    }

    /**
     * Gets test data from multiple specified columns.
     * Only includes rows where all specified columns have non-empty values.
     *
     * @param filePath    Path to the Excel file
     * @param sheetName   Name of the sheet to read from
     * @param columnNames Array of column names to extract data from each row
     * @return Iterator of test data objects containing values from specified columns
     */
    public static Iterator<Object[]> getMultiColumnData(String filePath, String sheetName, String... columnNames) {
        try {
            List<Map<String, Object>> rawData = convertToMapList(
                    ExcelTestDataCache.getTestData(filePath, sheetName)
            );

            List<Object[]> testData = new ArrayList<>();
            for (Map<String, Object> row : rawData) {
                if (hasAllRequiredData(row, columnNames)) {
                    Object[] rowData = extractColumnValues(row, columnNames);
                    testData.add(rowData);
                }
            }

            return testData.iterator();
        } catch (Exception error) {
            ErrorHandler.logError(error, "getMultiColumnData", "Failed to load test data from file: " + filePath);
            throw new RuntimeException("Failed to load test data", error);
        }
    }

    /**
     * Converts a 2D array of objects into a list of maps.
     * This method assumes that each row of the input array contains a single map.
     * Only rows that have a map as their first element are included in the result.
     *
     * @param rawDataArray A 2D array of objects where each row is expected to contain a single map
     * @return A list of maps extracted from the input array
     */
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> convertToMapList(Object[][] rawDataArray) {
        try {
            List<Map<String, Object>> rawData = new ArrayList<>();
            for (Object[] row : rawDataArray) {
                if (row.length > 0 && row[0] instanceof Map) {
                    Map<String, Object> rowData = (Map<String, Object>) row[0];
                    rawData.add(rowData);
                }
            }
            return rawData;
        } catch (Exception error) {
            ErrorHandler.logError(error, "convertToMapList", "Failed to convert to map list");
            throw new RuntimeException("Failed to convert to map list", error);
        }
    }

    /**
     * Checks if all specified columns in a row contain valid data.
     * This method iterates over the given column names and verifies that each
     * corresponding value in the row is valid according to the isValidValue method.
     * A value is considered valid if it is non-null, non-empty, and not "Unknown".
     *
     * @param row         The map representing a row of data with column names as keys.
     * @param columnNames The column names to check for valid data.
     * @return true if all specified columns contain valid data; false otherwise.
     */
    private static boolean hasAllRequiredData(Map<String, Object> row, String... columnNames) {
        try {
            for (String column : columnNames) {
                if (!isValidValue(row.get(column))) {
                    return false;
                }
            }
            return true;
        } catch (Exception error) {
            ErrorHandler.logError(error, "hasAllRequiredData", "Failed to check for required data");
            throw new RuntimeException("Failed to check for required data", error);
        }
    }

    /**
     * Determines if a value is valid.
     * A value is considered valid if it is non-null, non-empty, and not "Unknown".
     *
     * @param value The value to check.
     * @return true if the value is valid; false otherwise.
     */
    private static boolean isValidValue(Object value) {
        try {
            if (value == null) return false;
            String stringValue = value.toString().trim();
            return !stringValue.isEmpty() && !stringValue.equals("Unknown");
        } catch (Exception error) {
            ErrorHandler.logError(error, "isValidValue", "Failed to check for valid value");
            throw new RuntimeException("Failed to check for valid value", error);
        }
    }

    /**
     * Extracts the values from a row map for the given column names and returns them as an array.
     * If a value is null, an empty string is used instead.
     *
     * @param row         The map representing a row of data with column names as keys.
     * @param columnNames The column names to extract values from the row.
     * @return An array of values in the same order as the column names.
     */
    private static Object[] extractColumnValues(Map<String, Object> row, String... columnNames) {
        try {
            Object[] rowData = new Object[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                Object value = row.get(columnNames[i]);
                rowData[i] = value != null ? value : "";
            }
            return rowData;
        } catch (Exception error) {
            ErrorHandler.logError(error, "extractColumnValues", "Failed to extract column values");
            throw new RuntimeException("Failed to extract column values", error);
        }
    }
}