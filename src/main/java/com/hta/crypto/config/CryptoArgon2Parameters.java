package com.hta.crypto.config;

public enum CryptoArgon2Parameters {

    ITERATIONS(3),
    MEMORY(65536),  // 32768 -> 32 MB and 65536 -> 64 MB
    PARALLELISM(4);

    private final int parameterValue;

    CryptoArgon2Parameters(int parameterValue) {
        this.parameterValue = parameterValue;
    }

    public int getParameterValue() {
        return parameterValue;
    }
}