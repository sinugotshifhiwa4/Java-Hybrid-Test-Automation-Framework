package com.hta.config.properties;

public enum PropertiesFilePaths {

    GLOBAL("global-config.properties"),
    DEVELOPMENT("config-dev.properties"),
    UAT("config-uat.properties"),
    PRODUCTION("config-prod.properties");

    private static final String ROOT_PATH = "src/main/resources/config/";
    private final String filename;

    PropertiesFilePaths(String filename) {
        this.filename = filename;
    }

    public String getPropertiesFilePath() {
        return ROOT_PATH + filename;
    }
}