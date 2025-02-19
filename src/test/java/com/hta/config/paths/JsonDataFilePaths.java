package com.hta.config.paths;

/**
 * Enum representing the paths to various JSON data files used in tests.
 * The paths are relative to the resources root directory.
 */
public enum JsonDataFilePaths {

    BOOKING("booking.json"),
    PAYMENTS("payments.json");

    private static final String ROOT_PATH = "src/test/resources/testData/json/";
    private final String relativeFilePath;

    /**
     * Constructs an enum instance with the specified relative file path.
     *
     * @param relativeFilePath the relative path to the JSON file from the resources root
     */
    JsonDataFilePaths(String relativeFilePath) {
        this.relativeFilePath = relativeFilePath;
    }

    /**
     * Gets the full absolute path to the JSON file.
     *
     * @return the complete path to the JSON file
     */
    public String getFullPath() {
        return ROOT_PATH + relativeFilePath;
    }
}