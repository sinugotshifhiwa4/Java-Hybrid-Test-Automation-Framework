package com.hta.utils.excelUtils;

import com.hta.utils.logging.ErrorHandler;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CellReader {


    /**
     * Retrieves the value of the cell as a string.
     * If the cell is null, an empty string is returned. If the cell type is
     * {@link CellType#STRING}, the string value is returned. If the cell type is
     * {@link CellType#NUMERIC}, the value is formatted to a string. If the cell
     * type is {@link CellType#BOOLEAN}, the boolean value is returned as a
     * string. If the cell type is {@link CellType#FORMULA}, the formula is
     * evaluated and the result is returned as a string. If the cell type is
     * anything else, an empty string is returned.
     *
     * @param cell the cell to read the value from
     * @return the value of the cell as a string
     * @throws RuntimeException if there is an error reading the cell value
     */
    public static String getCellValueAsString(Cell cell) {

        if (cell == null) {
            return "";
        }

        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue();
                case NUMERIC -> formatNumericCell(cell);
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                case FORMULA -> evaluateFormulaCell(cell);
                default -> "";
            };
        } catch (Exception error) {
            ErrorHandler.logError(error, "getCellValueAsString", "Failed to get cell value");
            throw new RuntimeException("Failed to get cell value", error);
        }
    }

    /**
     * Retrieves the value of the cell as an integer.
     * If the cell is null, null is returned. If the cell type is
     * {@link CellType#STRING}, the string is parsed to an integer. If the cell
     * type is {@link CellType#NUMERIC}, the numeric value is formatted to an
     * integer. If the cell type is anything else, null is returned.
     *
     * @param cell the cell to read the value from
     * @return the value of the cell as an integer, or null if the cell value
     * cannot be parsed
     */
    public static Integer getCellValueAsInteger(Cell cell) {
        String stringValue = getCellValueAsString(cell);
        try {
            return stringValue.isEmpty() ? null :
                    new BigDecimal(stringValue.replaceAll("\\.0+$", "")).intValue();
        } catch (NumberFormatException error) {
            ErrorHandler.logError(error, "getCellValueAsInteger", "Failed to get cell value");
            throw new RuntimeException("Failed to get cell value", error);
        }
    }


    /**
     * Retrieves the value of the cell as a double.
     * If the cell is null, null is returned. If the cell type is
     * {@link CellType#STRING}, the string is parsed to a double. If the cell
     * type is {@link CellType#NUMERIC}, the numeric value is formatted to a
     * double. If the cell type is anything else, null is returned.
     *
     * @param cell the cell to read the value from
     * @return the value of the cell as a double, or null if the cell value
     * cannot be parsed
     */
    public static Double getCellValueAsDouble(Cell cell) {
        String stringValue = getCellValueAsString(cell);
        try {
            return stringValue.isEmpty() ? null :
                    new BigDecimal(stringValue.replaceAll(",", "").trim()).doubleValue();
        } catch (NumberFormatException error) {
            ErrorHandler.logError(error, "getCellValueAsDouble", "Failed to get cell value");
            throw new RuntimeException("Failed to get cell value", error);
        }
    }

    /**
     * Retrieves the value of the cell as a Date.
     * If the cell is null, null is returned. If the cell type is
     * {@link CellType#NUMERIC} and the cell is date-formatted, the date
     * value is returned. If the cell type is anything else, null is returned.
     *
     * @param cell the cell to read the value from
     * @return the value of the cell as a Date, or null if the cell value
     * cannot be parsed
     */
    public static Date getCellValueAsDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
            return null;
        } catch (Exception error) {
            ErrorHandler.logError(error, "getCellValueAsDate", "Failed to get cell value");
            throw new RuntimeException("Failed to get cell value", error);
        }
    }

    /**
     * Retrieves the value of the cell as a Boolean.
     * If the cell is null, null is returned. If the cell type is
     * {@link CellType#BOOLEAN}, the boolean value is returned. If the cell
     * type is {@link CellType#STRING}, the string is parsed to a boolean
     * where "true" (case-insensitive) returns true and "false" (case-insensitive)
     * returns false. If the cell value is neither boolean nor parsable
     * string, null is returned.
     *
     * @param cell the cell to read the value from
     * @return the value of the cell as a Boolean, or null if the cell value
     * cannot be parsed as a boolean
     */
    public static Boolean getCellValueAsBoolean(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String cellValue = cell.getStringCellValue().toLowerCase();
                if (cellValue.equals("true")) {
                    return true;
                } else if (cellValue.equals("false")) {
                    return false;
                }
            }
            return null; // In case the cell value is neither boolean nor string
        } catch (Exception error) {
            ErrorHandler.logError(error, "getCellValueAsBoolean", "Failed to get cell value");
            throw new RuntimeException("Failed to get cell value", error);
        }
    }

    /**
     * Formats a numeric cell value as a string, handling special cases for large
     * numbers and date-formatted cells.
     *
     * @param cell the cell to format
     * @return a string representation of the cell value
     */
    private static String formatNumericCell(Cell cell) {
        try {
            if (DateUtil.isCellDateFormatted(cell)) {
                return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
            }

            double value = cell.getNumericCellValue();

            // Handle large numbers that might lose precision
            if (Math.abs(value) > 1e15) {
                return new BigDecimal(value).toPlainString();
            }

            // Handle integers vs decimals
            return (value == Math.floor(value) && !Double.isInfinite(value)) ?
                    Long.toString((long) value) : String.valueOf(value);
        } catch (Exception error) {
            ErrorHandler.logError(error, "formatNumericCell", "Failed to format numeric cell");
            throw new RuntimeException("Failed to format numeric cell", error);
        }
    }

    /**
     * Evaluates a formula cell and returns the result as a string.
     *
     * @param cell the cell to evaluate
     * @return a string representation of the cell value
     */
    private static String evaluateFormulaCell(Cell cell) {
        try {
            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            CellValue cellValue = evaluator.evaluate(cell);
            return switch (cellValue.getCellType()) {
                case STRING -> cellValue.getStringValue();
                case NUMERIC -> formatNumericCell(cell);
                case BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
                default -> "";
            };
        } catch (Exception error) {
            ErrorHandler.logError(error, "evaluateFormulaCell", "Failed to evaluate formula cell");
            throw new RuntimeException("Failed to evaluate formula cell", error);
        }
    }

    /**
     * Determines if a cell is valid.
     * A cell is valid if it is not null and if its type is not {@link CellType#_NONE} or
     * {@link CellType#BLANK}.
     *
     * @param cell the cell to check
     * @return true if the cell is valid, false otherwise
     */
    public static boolean isValidCell(Cell cell) {
        try {
            return cell != null && cell.getCellType() != CellType._NONE && cell.getCellType() != CellType.BLANK;
        } catch (Exception error) {
            ErrorHandler.logError(error, "isValidCell", "Failed to check if cell is valid");
            throw new RuntimeException("Failed to check if cell is valid", error);
        }
    }
}
