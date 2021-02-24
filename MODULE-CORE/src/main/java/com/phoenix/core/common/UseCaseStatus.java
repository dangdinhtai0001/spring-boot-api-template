package com.phoenix.core.common;

public enum UseCaseStatus {
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private String value;

    UseCaseStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
