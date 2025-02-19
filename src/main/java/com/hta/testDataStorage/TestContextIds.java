package com.hta.testDataStorage;

public enum TestContextIds {
    BOOKING_TEST_ID_ONE("BOOKING_ID_ONE");

    private final String value;

    TestContextIds(String value) {
        this.value = value;
    }

    /**
     * Retrieves the string value of the test ID.
     *
     * @return the test ID as a string
     */
    public String getTestId() {
        return value;
    }
}
