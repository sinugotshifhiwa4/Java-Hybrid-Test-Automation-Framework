package com.hta.utils.jacksonUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonDataReader {

    private static final Logger logger = LoggerUtils.getLogger(JsonDataReader.class);
    private final JsonNode jsonData;

    /**
     * Constructs a JsonDataReader with the specified file path.
     *
     * @param filePath the path to the JSON file
     * @throws BookingDataReaderException if the JSON file cannot be loaded
     */
    public JsonDataReader(String filePath) {
        this.jsonData = loadJson(filePath);
    }

    private static JsonNode loadJson(String filePath) {
        try {
            return JsonConverter.getObjectMapper().readTree(new File(filePath));
        } catch (IOException error) {
            String errorMsg = "Failed to load JSON file: " + filePath;
            ErrorHandler.logError(error, "loadJson", errorMsg);
            throw new BookingDataReaderException(errorMsg, error);
        }
    }

    private <T> Optional<T> getData(String section, String key, Class<T> type) {
        try {
            JsonNode node = jsonData.path(section).path(key);
            if (node.isMissingNode() || node.isNull()) {
                return Optional.empty();
            }
            return Optional.ofNullable(JsonConverter.getObjectMapper().convertValue(node, type));
        } catch (Exception error) {
            ErrorHandler.logError(error, "getData", "Failed to retrieve data for section: " + section + ", key: " + key);
            return Optional.empty();
        }
    }

    public String getString(String section, String key) {
        return getData(section, key, String.class)
                .orElseThrow(() -> new IllegalArgumentException("Missing String value for key: " + key));
    }

    public int getInt(String section, String key) {
        return getData(section, key, Integer.class)
                .orElseThrow(() -> new IllegalArgumentException("Missing int value for key: " + key));
    }

    public boolean getBoolean(String section, String key) {
        return getData(section, key, Boolean.class)
                .orElseThrow(() -> new IllegalArgumentException("Missing boolean value for key: " + key));
    }

    private <T> T getDataByIndex(String section, int index, Class<T> type) {
        try {
            JsonNode node = jsonData.path(section);
            if (node.isArray() && index >= 0 && index < node.size()) {
                return JsonConverter.getObjectMapper().convertValue(node.get(index), type);
            }
        } catch (Exception error) {
            ErrorHandler.logError(error, "getDataByIndex", "Failed to retrieve data from section: " + section + " at index: " + index);
        }
        throw new IllegalArgumentException("Invalid index or section: " + section);
    }

    public String getStringByIndex(String section, int index) {
        return getDataByIndex(section, index, String.class);
    }

    public int getIntByIndex(String section, int index) {
        return getDataByIndex(section, index, Integer.class);
    }

    public boolean getBooleanByIndex(String section, int index) {
        return getDataByIndex(section, index, Boolean.class);
    }

    public  <T> List<T> getAllData(String section, Class<T> type) {
        List<T> values = new ArrayList<>();
        try {
            JsonNode node = jsonData.path(section);
            if (node.isArray()) {
                for (JsonNode item : node) {
                    values.add(JsonConverter.getObjectMapper().convertValue(item, type));
                }
            }
        } catch (Exception error) {
            ErrorHandler.logError(error, "getAllData", "Failed to retrieve all data from section: " + section);
        }
        return values;
    }

    public List<String> getAllStrings(String section) {
        return getAllData(section, String.class);
    }

    public List<Integer> getAllIntegers(String section) {
        return getAllData(section, Integer.class);
    }

    public List<Boolean> getAllBooleans(String section) {
        return getAllData(section, Boolean.class);
    }

    /**
     * Custom exception for JSON data reading errors.
     */
    public static class BookingDataReaderException extends RuntimeException {
        public BookingDataReaderException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
