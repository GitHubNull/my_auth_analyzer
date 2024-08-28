package com.protect7.authanalyzer.entities;

public enum DeduplicationOperation {
    YES,
    NO;

    public static DeduplicationOperation fromString(String value) {
        for (DeduplicationOperation deduplicationOperation : values()) {
            if (deduplicationOperation.name().equalsIgnoreCase(value)) {
                return deduplicationOperation;
            }
        }
        return null;
    }
}
