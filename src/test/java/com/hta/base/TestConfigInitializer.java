package com.hta.base;

import com.hta.config.environments.EnvironmentConfigManager;
import com.hta.config.environments.EnvironmentFileAlias;
import com.hta.config.environments.EnvironmentFilePaths;
import com.hta.config.paths.JsonDataFilePaths;
import com.hta.config.properties.PropertiesConfigManager;
import com.hta.config.properties.PropertiesFileAlias;
import com.hta.config.properties.PropertiesFilePaths;
import com.hta.testDataStorage.TestContextIds;
import com.hta.testDataStorage.TestContextStore;
import com.hta.utils.jacksonUtils.JsonConverter;
import com.hta.utils.jacksonUtils.JsonDataReader;
import com.hta.utils.logging.ErrorHandler;
import com.hta.utils.logging.LoggerUtils;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TestConfigInitializer {

    private static final Logger logger = LoggerUtils.getLogger(TestConfigInitializer.class);

    /**
     * Initializes both property and environment configurations.
     * This method loads configurations from files and handles any exceptions that occur during the process.
     */
    public static void initializeConfigurations() {
        try {
            loadPropertyConfigurations();
            loadEnvironmentConfigurations();
        } catch (Exception error) {
            ErrorHandler.logError(error, "initializeConfigurations", "Failed to initialize configurations");
            throw error;
        }
    }

    private static void loadPropertyConfigurations() {
        Map<PropertiesFileAlias, PropertiesFilePaths> configMap = Map.of(
                PropertiesFileAlias.GLOBAL, PropertiesFilePaths.GLOBAL,
                PropertiesFileAlias.UAT, PropertiesFilePaths.UAT
        );

        // Check if all required files exist
        List<String> missingFiles = configMap.values().stream()
                .map(PropertiesFilePaths::getPropertiesFilePath)
                .filter(path -> !Files.exists(Paths.get(path)))
                .toList();

        if (!missingFiles.isEmpty()) {
            logger.error("Missing required property configuration files: {}", missingFiles);
            throw new IllegalStateException("Missing required property configuration files: " + missingFiles);
        }

        // Load all configurations since all files exist
        loadConfigurations(
                configMap,
                (alias, path) -> PropertiesConfigManager.loadConfiguration(
                        alias.getConfigurationAlias(),
                        path.getPropertiesFilePath()
                )
        );
    }


    private static void loadEnvironmentConfigurations() {
        Map<EnvironmentFileAlias, EnvironmentFilePaths> configMap = Map.of(
                EnvironmentFileAlias.BASE, EnvironmentFilePaths.BASE,
                EnvironmentFileAlias.UAT, EnvironmentFilePaths.UAT
        );

        // Check if all required files exist
        List<String> missingFiles = configMap.values().stream()
                .map(EnvironmentFilePaths::getEnvironmentFileFullPath)
                .filter(path -> !Files.exists(Paths.get(path)))
                .toList();

        if (!missingFiles.isEmpty()) {
            logger.error("Missing required property environment files: {}", missingFiles);
            throw new IllegalStateException("Missing required environment configuration files: " + missingFiles);
        }

        // Load all configurations since all files exist
        loadConfigurations(
                configMap,
                (alias, filePath) -> EnvironmentConfigManager.loadConfiguration(
                        alias.getEnvironmentAlias(),
                        filePath.getEnvironmentFilename()
                )
        );
    }


    /**
     * Generic method to load configurations using a provided loader function.
     *
     * @param configMap A map of configuration aliases to paths.
     * @param loader A BiConsumer that takes an alias and path to load the configuration.
     * @param <ConfigAlias> The type of the configuration alias.
     * @param <ConfigPath> The type of the configuration path.
     */
    private static <ConfigAlias, ConfigPath> void loadConfigurations(
            Map<ConfigAlias, ConfigPath> configMap,
            BiConsumer<ConfigAlias, ConfigPath> loader) {
        configMap.forEach(loader);
    }

    /**
     * Initializes test contexts for the provided test context IDs.
     * It also initializes the JSON mapper used for serialization and deserialization.
     *
     * @param testContextIds Varargs of TestContextIds to initialize.
     */
    public static void initializeTestContexts(TestContextIds... testContextIds) {
        try {
            // Initialize JSON Mapper
            JsonConverter.initJsonMapper();

            // Initialize test contexts
            for (TestContextIds testContextId : testContextIds) {
                TestContextStore.initializeContext(testContextId.getTestId());
            }
        } catch (Exception error) {
            ErrorHandler.logError(error, "initializeTestContexts", "Failed to initialize test data test contexts");
            throw error;
        }
    }

    /**
     * Cleans up test contexts for the provided test context IDs.
     *
     * @param testContextIds Varargs of TestContextIds to clean up.
     */
    public static void cleanUpTestContexts(TestContextIds... testContextIds){
        try {
            // Cleanup test contexts
            for (TestContextIds testContextId : testContextIds) {
                TestContextStore.cleanupTestContext(testContextId.getTestId());
            }
        } catch (Exception error){
            ErrorHandler.logError(error, "cleanUpTestContexts", "Failed to clean up test data test contexts");
            throw error;
        }
    }

    public static JsonDataReader createJsonReader(JsonDataFilePaths filePath) {
        return new JsonDataReader(filePath.getFullPath());
    }
}