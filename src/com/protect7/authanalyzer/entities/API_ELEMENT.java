package com.protect7.authanalyzer.entities;

public enum API_ELEMENT {
    METHOD,
    PATH,
    QUERY_PARAMS,
    HTTP_VERSION;

    public static API_ELEMENT fromString(String string) {
        if (string == null) {
            return null;
        }
        return valueOf(string.toUpperCase());
    }
}
