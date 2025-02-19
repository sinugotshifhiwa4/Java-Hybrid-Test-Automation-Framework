package com.hta.crypto.services;

import com.hta.config.environments.EnvironmentConfigManager;
import com.hta.config.environments.EnvironmentFileAlias;
import com.hta.config.environments.EnvironmentFilePaths;
import com.hta.config.properties.PropertiesConfigManager;
import com.hta.config.properties.PropertiesFileAlias;
import com.hta.utils.FileDirectoryManager;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.CryptoException;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for managing crypto operations on environment variables
 */
public class CryptoOperationsManager {

    private static final Logger logger = LoggerUtils.getLogger(CryptoOperationsManager.class);
    private static final String ENCRYPTION_ERROR = "Failed to encrypt variable: ";
    private static final String DECRYPTION_ERROR = "Failed to decrypt key: ";
    private static final String VARIABLE_UPDATE_ERROR = "Failed to update environment variable: ";
    private static final String ENCRYPTED_LENGTH_THRESHOLD = "ENCRYPTED_LENGTH_THRESHOLD";
    private static final int ENCRYPTION_LENGTH_THRESHOLD = getEncryptedLengthThreshold();

    private CryptoOperationsManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Encrypts multiple environment variables
     *
     * @param filePath                 path to the environment file
     * @param aliasName                alias name for the environment
     * @param environmentSecretKeyType type of secret key used for encryption
     * @param envVariables             variables to encrypt
     * @throws CryptoException if encryption fails
     */
    public static void encryptEnvironmentVariables(
            String filePath,
            String aliasName,
            String environmentSecretKeyType,
            String... envVariables
    ) throws CryptoException {
        try {
            for (String envVariable : envVariables) {
                encryptEnvironmentVariables(filePath, aliasName, environmentSecretKeyType, envVariable);
            }
        } catch (Exception error) {
            ErrorHandler.logError(error, "encryptEnvironmentVariables", "Failed to encrypt multiple variables");
            throw error;
        }
    }

    /**
     * Encrypts a single environment variable
     *
     * @param filePath                 path to the environment file
     * @param aliasName                alias name for the environment
     * @param environmentSecretKeyType type of secret key used for encryption
     * @param envVariable              variable to encrypt
     * @throws CryptoException if encryption fails
     */
    public static void encryptEnvironmentVariables(
            String filePath,
            String aliasName,
            String environmentSecretKeyType,
            String envVariable
    ) throws CryptoException {
        try {
            // Get current value
            String currentValue = getEnvironmentVariable(aliasName, envVariable);

            if (isAlreadyEncrypted(currentValue)) {
                logger.info("Skipping encryption: Environment variable '{}' is already encrypted. Provide a plain-text value if re-encryption is required.", envVariable);
                return;
            }

            String encryptedValue = encryptValue(environmentSecretKeyType, currentValue);
            updateEnvironmentVariable(filePath, envVariable, encryptedValue);
            logger.info("Variable '{}' encrypted successfully.", envVariable);

        } catch (Exception error) {
            ErrorHandler.logError(error, "encryptEnvironmentVariables", ENCRYPTION_ERROR + envVariable);
            throw error;
        }
    }

    /**
     * Check if a value appears to be already encrypted based on length
     *
     * @param value the value to check
     * @return true if the value is likely already encrypted
     */
    private static boolean isAlreadyEncrypted(String value) {
        return value.length() > ENCRYPTION_LENGTH_THRESHOLD;
    }

    /**
     * Retrieves an environment variable value
     *
     * @param aliasName   alias name for the environment
     * @param envVariable variable name to retrieve
     * @return the value of the environment variable
     */
    private static String getEnvironmentVariable(String aliasName, String envVariable) {
        try {
            String envValue = EnvironmentConfigManager.getEnvironmentKeyFromCache(aliasName, envVariable);
            if (envValue == null) {
                throw new IllegalArgumentException("Environment variable '" + envVariable + "' is null");
            }
            return envValue;
        } catch (Exception error) {
            ErrorHandler.logError(error, "getEnvironmentVariable", "Failed to get environment variable: " + envVariable);
            throw error;
        }
    }

    /**
     * Encrypts a value using the appropriate secret key
     *
     * @param environmentSecretKeyType type of secret key used for encryption
     * @param envValue                 value to encrypt
     * @return encrypted value
     * @throws CryptoException if encryption fails
     */
    private static String encryptValue(String environmentSecretKeyType, String envValue) throws CryptoException {
        try {
            SecretKey secretKey = getSecretKey(EnvironmentFileAlias.BASE.getEnvironmentAlias(), environmentSecretKeyType);
            String encryptedValue = CryptoOperations.encrypt(secretKey, envValue);
            if (encryptedValue == null) {
                throw new IllegalArgumentException("Failed to encrypt value");
            }
            return encryptedValue;
        } catch (Exception error) {
            ErrorHandler.logError(error, "encryptValue", "Failed to encrypt value");
            throw error;
        }
    }

    /**
     * Saves a secret key in the base environment file
     *
     * @param baseEnvironmentFilePath path to the base environment file
     * @param secretKeyVariable       variable name for the secret key
     * @param encodedSecretKey        encoded secret key value
     * @throws IOException if saving fails
     */
    public static void saveSecretKeyInBaseEnvironment(
            String baseEnvironmentFilePath,
            String secretKeyVariable,
            String encodedSecretKey
    ) throws IOException {
        try {
            ensureBaseEnvironmentFileExists();

            // Check if the secret key is already set
            if (isEnvironmentVariableSet(baseEnvironmentFilePath, secretKeyVariable)) {
                logger.info("The environment secret key '{}' already exists. Please remove it before updating.", secretKeyVariable);
                return; // Stop execution to prevent overwriting
            }

            // Update the environment variable if it doesn't already exist or is empty
            updateEnvironmentVariable(baseEnvironmentFilePath, secretKeyVariable, encodedSecretKey);
            logger.info("Secret key saved for variable '{}'", secretKeyVariable);

        } catch (Exception error) {
            ErrorHandler.logError(error, "saveSecretKeyInBaseEnvironment", "Failed to save secret key in base environment");
            throw error;
        }
    }

    /**
     * Ensures that the base environment file exists
     *
     * @throws IOException if file creation fails
     */
    private static void ensureBaseEnvironmentFileExists() throws IOException {
        try {
            FileDirectoryManager.createDirIfNotExists(EnvironmentFilePaths.getDirectoryPath());
            FileDirectoryManager.createFileIfNotExists(
                    EnvironmentFilePaths.getDirectoryPath(),
                    EnvironmentFilePaths.BASE.getEnvironmentFilename()
            );
        } catch (IOException error) {
            ErrorHandler.logError(error, "ensureEnvironmentFileExists", "Failed to ensure environment file exists");
            throw error;
        }
    }

    /**
     * Checks if an environment variable is set and has a value
     *
     * @param environmentFilePath path to the environment file
     * @param key                 variable name to check
     * @return true if the variable is set and has a value
     * @throws IOException if file reading fails
     */
    private static boolean isEnvironmentVariableSet(String environmentFilePath, String key) throws IOException {
        try {
            List<String> envLines = Files.readAllLines(Paths.get(environmentFilePath));
            return envLines.stream()
                    .anyMatch(line -> line.startsWith(key + "=") && line.length() > (key.length() + 1));
        } catch (Exception error) {
            ErrorHandler.logError(error, "isEnvironmentVariableSet", "Failed to check environment variable: " + key);
            throw error;
        }
    }

    /**
     * Updates an environment variable in the specified file
     *
     * @param filePath    path to the environment file
     * @param envVariable variable name to update
     * @param value       new value for the variable
     */
    private static void updateEnvironmentVariable(String filePath, String envVariable, String value) {
        try {
            Path path = Paths.get(filePath);
            List<String> updatedLines = updateEnvironmentLines(Files.readAllLines(path), envVariable, value);
            Files.write(path, updatedLines);
            logger.info("Environment variable '{}' updated in {}", envVariable, filePath);
        } catch (IOException error) {
            ErrorHandler.logError(error, "updateEnvironmentVariable", VARIABLE_UPDATE_ERROR + envVariable);
            throw new RuntimeException(error);
        }
    }

    /**
     * Updates environment lines with a new variable value
     *
     * @param existingLines existing environment file lines
     * @param envVariable   variable name to update
     * @param value         new value for the variable
     * @return updated list of lines
     */
    private static List<String> updateEnvironmentLines(
            List<String> existingLines,
            String envVariable,
            String value
    ) {
        try {
            boolean[] isUpdated = {false};

            List<String> updatedLines = existingLines.stream()
                    .map(line -> {
                        if (line.startsWith(envVariable + "=")) {
                            isUpdated[0] = true;
                            return envVariable + "=" + value;
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            if (!isUpdated[0]) {
                updatedLines.add(envVariable + "=" + value);
            }

            return updatedLines;
        } catch (Exception error) {
            ErrorHandler.logError(error, "updateEnvironmentLines", "Failed to update environment lines");
            throw new RuntimeException(error);
        }
    }

    /**
     * Decrypts multiple environment variables
     *
     * @param aliasName                alias name for the environment
     * @param environmentSecretKeyType type of secret key used for decryption
     * @param requiredKeys             variable names to decrypt
     * @return list of decrypted values
     */
    public static List<String> decryptEnvironmentVariables(
            String aliasName,
            String environmentSecretKeyType,
            String... requiredKeys
    ) {
        if (requiredKeys == null || requiredKeys.length == 0) {
            return Collections.emptyList();
        }

        try {
            SecretKey secretKey = getSecretKey(EnvironmentFileAlias.BASE.getEnvironmentAlias(), environmentSecretKeyType);
            return Arrays.stream(requiredKeys)
                    .map(key -> decryptSingleKey(aliasName, secretKey, key))
                    .collect(Collectors.toList());
        } catch (Exception error) {
            ErrorHandler.logError(error, "decryptEnvironmentVariables", "Failed to decrypt environment variables");
            throw new RuntimeException(error);
        }
    }

    /**
     * Decrypts a single environment variable
     *
     * @param aliasName                alias name for the environment
     * @param environmentSecretKeyType type of secret key used for decryption
     * @param requiredKey              variable name to decrypt
     * @return decrypted value
     */
    public static String decryptEnvironmentVariable(
            String aliasName,
            String environmentSecretKeyType,
            String requiredKey
    ) {
        try {
            SecretKey secretKey = getSecretKey(EnvironmentFileAlias.BASE.getEnvironmentAlias(), environmentSecretKeyType);
            return decryptSingleKey(aliasName, secretKey, requiredKey);
        } catch (Exception error) {
            ErrorHandler.logError(error, "decryptEnvironmentVariable", DECRYPTION_ERROR + requiredKey);
            throw new RuntimeException(error);
        }
    }

    /**
     * Retrieves a secret key for crypto operations
     *
     * @param aliasName                alias name for the environment
     * @param environmentSecretKeyType type of secret key to retrieve
     * @return the secret key
     */
    public static SecretKey getSecretKey(String aliasName, String environmentSecretKeyType) {
        try {
            return EnvironmentConfigManager.getSecretKeyFromCache(aliasName, environmentSecretKeyType);
        } catch (Exception error) {
            ErrorHandler.logError(error, "getSecretKey", "Failed to get secret key: " + environmentSecretKeyType);
            throw new RuntimeException(error);
        }
    }

    /**
     * Decrypts a single key
     *
     * @param aliasName alias name for the environment
     * @param secretKey secret key used for decryption
     * @param key       variable name to decrypt
     * @return decrypted value
     */
    private static String decryptSingleKey(String aliasName, SecretKey secretKey, String key) {
        try {
            String encryptedValue = EnvironmentConfigManager.getEnvironmentKeyFromCache(aliasName, key);
            return CryptoOperations.decrypt(secretKey, encryptedValue);
        } catch (CryptoException error) {
            ErrorHandler.logError(error, "decryptSingleKey", DECRYPTION_ERROR + key);
            throw new RuntimeException(error);
        }
    }

    /**
     * Gets the encrypted length threshold from configuration.
     * If not specified in configuration, returns the default value of 50.
     *
     * @return the encrypted length threshold value
     */
    private static int getEncryptedLengthThreshold() {
        try {
            return PropertiesConfigManager
                    .getConfiguration(PropertiesFileAlias.GLOBAL.getConfigurationAlias())
                    .getProperty(ENCRYPTED_LENGTH_THRESHOLD, Integer.class)
                    .orElse(90);
        } catch (Exception error) {
            ErrorHandler.logError(error, "getEncryptedLengthThreshold", "Failed to get encrypted length threshold");
            throw error;
        }
    }
}