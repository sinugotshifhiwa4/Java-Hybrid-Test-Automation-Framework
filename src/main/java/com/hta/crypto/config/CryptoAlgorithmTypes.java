package com.hta.crypto.config;

public enum CryptoAlgorithmTypes {

    AES("AES"),
    AES_GCM_NO_PADDING("AES/GCM/NoPadding"),
    AES_CBC_PKCS5("AES/CBC/PKCS5Padding"),
    PBKDF2("PBKDF2WithHmacSHA256"),
    HMAC_SHA256("HmacSHA256");

    private final String algorithmName;

    CryptoAlgorithmTypes(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }
}
