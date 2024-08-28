package com.protect7.authanalyzer.entities;

public enum CopyDestination {
    CLI_BOARD,
    FILE;

    public static CopyDestination fromString(String string) {
        for (CopyDestination copyDestination : CopyDestination.values()) {
            if (copyDestination.name().equalsIgnoreCase(string)) {
                return copyDestination;
            }
        }
        return null;
    }

    public String getString() {
        return this.name().toLowerCase();
    }
}
