package com.hta.utils;


import com.hta.utils.logging.ErrorHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDirectoryManager {
    /**
     * Creates a directory if it does not already exist.
     *
     * @param dirPath The path to the directory to create.
     * @throws IOException If the directory cannot be created due to an I/O error.
     */
    public static void createDirIfNotExists(String dirPath) throws IOException {
        if (dirPath == null || dirPath.isBlank()) {
            throw new IllegalArgumentException("Directory path cannot be null or empty.");
        }
        try {
            Path dir = Paths.get(dirPath);
            Files.createDirectories(dir);
        } catch (Exception error) {
            ErrorHandler.logError(
                    error,
                    "createDirIfNotExists",
                    "Failed to ensure directory exists: " + dirPath
            );
            throw error;
        }
    }

    /**
     * Creates a file in a specified directory if it does not already exist.
     *
     * @param dirPath  The path to the directory to create the file in.
     * @param fileName The name of the file to create.
     * @throws IOException If the file cannot be created due to an I/O error.
     */
    public static void createFileIfNotExists(String dirPath, String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }
        try {
            createDirIfNotExists(dirPath);
            Path filePath = Paths.get(dirPath, fileName);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (Exception error) {
            ErrorHandler.logError(
                    error,
                    "createFileIfNotExists",
                    "Failed to ensure file exists: " + fileName + " in " + dirPath
            );
            throw error;
        }
    }

    /**
     * Utility method to check if a file exists at the given path.
     *
     * @param filePath The path of the file to check.
     * @return true if the file exists, false otherwise.
     */
    public static boolean doesFileExist(String filePath) {
        return filePath != null && Files.exists(Paths.get(filePath));
    }

    public static void ensureDirectoryAndFileExists(String directoryPath, String filename) throws IOException {
        try {
            FileDirectoryManager.createDirIfNotExists(directoryPath);
            FileDirectoryManager.createFileIfNotExists(directoryPath, filename);
        } catch (Exception error){
            ErrorHandler.logError(error, "ensureDirectoryAndFileExist",
                    "Failed to ensure directory: " + "'" + directoryPath + "'" + " and file exists: " + "'" + filename + "'");
            throw error;
        }
    }
}
