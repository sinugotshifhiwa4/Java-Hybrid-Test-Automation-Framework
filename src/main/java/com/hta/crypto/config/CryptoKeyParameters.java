package com.hta.crypto.config;

public enum CryptoKeyParameters {

    GCM_TAG_KEY_SIZE(128),
    SECRET_KEY_SIZE(32),
    IV_SIZE(16),
    SALT_SIZE(32);

    private final int keySize;

    CryptoKeyParameters(int keySize) {
        this.keySize = keySize;
    }

    public int getKeySize() {
        return keySize;
    }
}