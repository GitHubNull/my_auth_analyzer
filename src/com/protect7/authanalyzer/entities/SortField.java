package com.protect7.authanalyzer.entities;

public enum SortField {
    METHOD,
    PATH,
    QUERY_PARAMS,
    HTTP_VERSION;

    public static SortField fromString(String string) {
        if (string == null) {
            return null;
        }
        return valueOf(string.toUpperCase());
    }
}
