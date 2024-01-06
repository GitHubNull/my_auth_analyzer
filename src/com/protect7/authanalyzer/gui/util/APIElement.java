package com.protect7.authanalyzer.gui.util;

import com.protect7.authanalyzer.entities.API_ELEMENT;

public class APIElement {
    String method;
    String path;
    String queryParams;
    String httpVersion;

    public APIElement(String method, String path, String queryParams, String httpVersion) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.httpVersion = httpVersion;
    }

    public APIElement() {

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getAPIElementByAPIElementEnum(API_ELEMENT api_element) {
        switch (api_element) {
            case METHOD:
                return this.method;
            case PATH:
                return this.path;
            case QUERY_PARAMS:
                return this.queryParams;
            case HTTP_VERSION:
                return this.httpVersion;
            default:
                return null;
        }
    }
}
