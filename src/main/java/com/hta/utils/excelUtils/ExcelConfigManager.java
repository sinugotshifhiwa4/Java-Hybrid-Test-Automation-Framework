package com.hta.utils.excelUtils;

import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.*;

public class ExcelConfigManager {

    private static final Logger logger = LoggerUtils.getLogger(ExcelConfigManager.class);

    /**
     * Reads data from an Excel file and converts it to a list of maps.
     *
     * @param filePath  The path to the Excel file
     * @param sheetName The name of the sheet to read
     * @return List of maps where each map represents a row of data
     * @throws ExcelOperationException if there are issues reading the file
     */
    public static List<Map<String, Object>> loadExcelDataAsList(String filePath, String sheetName) {
        List<Map<String, Object>> dataList = new ArrayList<>();

        try (WorkbookManager workbookManager = new WorkbookManager(filePath)) {
            Sheet sheet = workbookManager.getSheet(sheetName);
            processSheet(sheet, dataList);
        } catch (IOException error) {
            ErrorHandler.logError(error, "loadExcelDataAsList", "Failed to read Excel data");
            throw new ExcelOperationException("Error closing workbook", error);
        }

        return dataList;
    }

    /**
     * Process the data in an Excel sheet and convert it to a list of maps.
     * This method assumes that the first row contains the headers.
     * It processes each row, skipping completely empty rows.
     * For each row with data, it converts the row to a map and adds it to the data list.
     * If there are issues processing the sheet, an ExcelOperationException is thrown.
     *
     * @param sheet    The sheet to process
     * @param dataList The list to which the row data should be added
     * @throws ExcelOperationException if there are issues processing the sheet
     */
    private static void processSheet(Sheet sheet, List<Map<String, Object>> dataList) {
        try {
            int rowCount = sheet.getPhysicalNumberOfRows();
            if (rowCount <= 1) {
                logger.warn("Sheet is empty or contains only headers");
                return;
            }

            Row headerRow = sheet.getRow(0);
            List<String> headers = getHeaders(headerRow);

            // Process each row, skipping completely empty rows
            for (int i = 1; i < rowCount; i++) {
                Row currentRow = sheet.getRow(i);
                if (!isRowEmpty(currentRow)) {
                    Map<String, Object> rowData = processRow(currentRow, headers);
                    if (!rowData.isEmpty()) {
                        dataList.add(rowData);
                    }
                }
            }
        } catch (Exception error) {
            ErrorHandler.logError(error, "processSheet", "Failed to process sheet");
            throw new ExcelOperationException("Error processing sheet", error);
        }
    }

    /**
     * Retrieves the headers from a row, trims the values, and returns them as a list.
     * If there are issues retrieving the headers, an ExcelOperationException is thrown.
     *
     * @param headerRow The row containing the headers
     * @return A list of headers
     * @throws ExcelOperationException if there are issues retrieving the headers
     */
    private static List<String> getHeaders(Row headerRow) {
        try {
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(CellReader.getCellValueAsString(cell).trim());
            }
            return headers;
        } catch (Exception error) {
            ErrorHandler.logError(error, "getHeaders", "Failed to get headers");
            throw new ExcelOperationException("Error processing headers", error);
        }
    }

    /**
     * Processes a row of cells and returns a map of the column names to their respective values.
     * If any value is null, an empty map is returned.
     *
     * @param row     The row to process
     * @param headers List of column names as headers
     * @return A map of the column names to their respective values
     */
    private static Map<String, Object> processRow(Row row, List<String> headers) {
        try {
            Map<String, Object> rowData = new HashMap<>();
            boolean hasValidData = false;

            for (int j = 0; j < headers.size(); j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell != null) {
                    Object cellValue = getCellValue(cell);
                    if (cellValue != null) {
                        rowData.put(headers.get(j), cellValue);
                        hasValidData = true;
                    }
                }
            }

            return hasValidData ? rowData : new HashMap<>();
        } catch (Exception error) {
            ErrorHandler.logError(error, "processRow", "Failed to process row");
            throw new ExcelOperationException("Error processing row", error);
        }
    }

    /**
     * Retrieves the value of the cell as an object.
     *
     * <p>
     * The type of object returned depends on the type of the cell value.
     * If the cell value is a boolean, a boolean is returned.
     * If the cell value is a numeric, a double or int is returned based on whether the value is an integer.
     * If the cell value is a string, a string is returned.
     * If the cell value is a formula, the formula is evaluated and the result is returned.
     * If the cell value is of any other type, null is returned.
     *
     * @param cell The cell to read the value from
     * @return The value of the cell as an object, or null if the cell value cannot be read.
     */
    private static Object getCellValue(Cell cell) {
        try {
            Workbook workbook = cell.getSheet().getWorkbook(); // Get workbook instance
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator(); // Get FormulaEvaluator

            return switch (cell.getCellType()) {
                case BOOLEAN -> CellReader.getCellValueAsBoolean(cell);
                case NUMERIC -> {
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        yield CellReader.getCellValueAsDate(cell);
                    } else {
                        double numericValue = cell.getNumericCellValue(); // Always returns a primitive double
                        boolean isInteger = Double.compare(numericValue, Math.floor(numericValue)) == 0
                                && !Double.isInfinite(numericValue);

                        // Ensure null safety before yielding
                        yield isInteger
                                ? Objects.requireNonNullElse(CellReader.getCellValueAsInteger(cell), (int) numericValue)
                                : Objects.requireNonNullElse(CellReader.getCellValueAsDouble(cell), numericValue);
                    }
                }
                case STRING -> {
                    String stringValue = cell.getStringCellValue().trim();
                    yield stringValue.isEmpty() ? null : stringValue;
                }
                case FORMULA -> {
                    CellValue evaluatedValue = formulaEvaluator.evaluate(cell);
                    yield switch (evaluatedValue.getCellType()) {
                        case BOOLEAN -> evaluatedValue.getBooleanValue();
                        case NUMERIC -> evaluatedValue.getNumberValue();
                        case STRING -> evaluatedValue.getStringValue();
                        default -> null;
                    };
                }
                default -> null;
            };
        } catch (Exception error) {
            ErrorHandler.logError(error, "getCellValue", "Failed to get cell value");
            throw new ExcelOperationException("Error getting cell value", error);
        }
    }


    private static boolean isRowEmpty(Row row) {
        try {
            if (row == null) {
                return true;
            }

            for (Cell cell : row) {
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    return false;
                }
            }
            return true;
        } catch (Exception error) {
            ErrorHandler.logError(error, "isRowEmpty", "Failed to check if row is empty");
            throw new ExcelOperationException("Error checking if row is empty", error);
        }
    }
}