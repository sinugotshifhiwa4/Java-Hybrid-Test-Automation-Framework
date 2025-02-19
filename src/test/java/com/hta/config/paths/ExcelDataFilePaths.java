package com.hta.config.paths;

/**
 * Enum representing the paths to various Excel data files used in tests.
 * The paths are relative to the resources root directory.
 */
public enum ExcelDataFilePaths {

    BOOKING("hotelBookingData.xlsx"),
    PAYMENTS("payments.xlsx");

    private static final String ROOT_PATH = "src/test/resources/testData/excel/";
    private final String relativeFilePath;

    /**
     * Constructs an enum instance with the specified relative file path.
     *
     * @param relativeFilePath the relative path to the Excel file from the resources root
     */
    ExcelDataFilePaths(String relativeFilePath) {
        this.relativeFilePath = relativeFilePath;
    }

    /**
     * Gets the full absolute path to the Excel file.
     *
     * @return the complete path to the Excel file
     */
    public String getFullPath() {
        return ROOT_PATH + relativeFilePath;
    }
}