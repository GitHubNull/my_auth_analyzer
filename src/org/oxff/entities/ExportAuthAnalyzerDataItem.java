package org.oxff.entities;

import burp.IHttpRequestResponse;
import org.oxff.util.HttpMessageUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

public class ExportAuthAnalyzerDataItem {
    private int id;
    private String method;
    private String host;
    private int port;
    private String path;

    private String requestHeaders;

    private int requestContentLength;

    private String requestBody;
    private Boolean  requestBodyIsBase64;


    // response data

    private String responseHeaders;
    private int responseContentLength;
    private String responseBody;
    private Boolean  responseBodyIsBase64;
    private int responseStatusCode;

    private String comment;

//    private HTTPData rawHTTPData;

    private List<SessionHTTPData> sessionHTTPDataList;

    public ExportAuthAnalyzerDataItem(){

    }

    public ExportAuthAnalyzerDataItem(IHttpRequestResponse rawHttpRequestResponse, int id, String method, String host, int port, String path,
                                      List<String> requestHeaderList, byte[] requestBodyBytes,
                                      int requestContentLength,
                                      List<String> responseHeaderList, byte[] responseBodyBytes,
                                      int responseContentLength, int responseStatusCode,
                                      String comment) {
        this.id = id;
        this.method = method;
        this.host = host;
        this.path = path;
//        this.rawHTTPData = rawHTTPData;
        this.port = port;
        this.comment = comment;


        // write data from headerList to headers
        StringBuilder requestHeadersSb = new StringBuilder();
        for (String header : requestHeaderList) {
            requestHeadersSb.append(header);
            requestHeadersSb.append("\n");
        }
        this.requestHeaders = requestHeadersSb.toString();

        // Check if Content-Type header exists and indicates binary data
        String requestContentTypeHeader = getHeaderValue(requestHeaderList, "Content-Type");
        if (HttpMessageUtil.requestContainsFile(rawHttpRequestResponse)) {
            // Binary data, encode bodyBytes using Base64
            this.requestBody = Base64.getEncoder().encodeToString(requestBodyBytes);
            requestBodyIsBase64 = true;
        } else {
            // Text data, convert bodyBytes to string with proper encoding
            this.requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
            requestBodyIsBase64 = false;
        }

        this.requestContentLength = requestContentLength;


        // write response data to responseHeaders and responseBody
        StringBuilder responseHeadersSb = new StringBuilder();
        for (String header : responseHeaderList) {
            responseHeadersSb.append(header);
            responseHeadersSb.append("\n");
        }
        this.responseHeaders = responseHeadersSb.toString();

        // Check if Content-Type header exists and indicates binary data
        String responseContentTypeHeader = getHeaderValue(responseHeaderList, "Content-Type");
        if (HttpMessageUtil.responseContainsFile(rawHttpRequestResponse)) {
            this.responseBody = Base64.getEncoder().encodeToString(responseBodyBytes);
            responseBodyIsBase64 = true;
        }else{
            // Text data, convert bodyBytes to string with proper encoding
            this.responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);
            responseBodyIsBase64 = false;
        }

        this.responseContentLength =  responseBodyBytes.length;
        this.responseStatusCode = responseStatusCode;
    }

    private String getHeaderValue(List<String> headerList, String headerName) {
        for (String header : headerList) {
            if (header.toLowerCase(Locale.ROOT).startsWith(headerName.toLowerCase(Locale.ROOT) + ":")) {
                return header.substring(headerName.length() + 1).trim();
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(List<String> requestHeaderList) {
        StringBuilder requestHeadersSb = new StringBuilder();
        for (String header : requestHeaderList) {
            requestHeadersSb.append(header);
            requestHeadersSb.append("\n");
        }
        this.requestHeaders = requestHeadersSb.toString();
    }

    public int getRequestContentLength() {
        return requestContentLength;
    }

    public void setRequestContentLength(int requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(List<String> responseHeaderList) {
        StringBuilder responseHeadersSb = new StringBuilder();
        for (String header : responseHeaderList) {
            responseHeadersSb.append(header);
            responseHeadersSb.append("\n");
        }
        this.responseHeaders = responseHeadersSb.toString();
    }

    public int getResponseContentLength() {
        return responseContentLength;
    }

    public void setResponseContentLength(int responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SessionHTTPData> getSessionsHTTPDataList() {
        return sessionHTTPDataList;
    }

    public void setSessionsHTTPDataList(List<SessionHTTPData> sessionHTTPData) {
        this.sessionHTTPDataList = sessionHTTPData;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Boolean getRequestBodyIsBase64() {
        return requestBodyIsBase64;
    }

    public void setRequestBodyIsBase64(Boolean requestBodyIsBase64) {
        this.requestBodyIsBase64 = requestBodyIsBase64;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Boolean getResponseBodyIsBase64() {
        return responseBodyIsBase64;
    }

    public void setResponseBodyIsBase64(Boolean responseBodyIsBase64) {
        this.responseBodyIsBase64 = responseBodyIsBase64;
    }

    /**
     * Get the full URL by combining host, port, and path
     */
    public String getFullUrl() {
        StringBuilder url = new StringBuilder();
        url.append("http://"); // Default to http, could be enhanced to detect https

        if (host != null && !host.trim().isEmpty()) {
            url.append(host);
        } else {
            url.append("localhost");
        }

        if (port != 80 && port != 443 && port > 0) {
            url.append(":").append(port);
        }

        if (path != null && !path.trim().isEmpty()) {
            if (!path.startsWith("/")) {
                url.append("/");
            }
            url.append(path);
        } else {
            url.append("/");
        }

        return url.toString();
    }

    /**
     * Extract protocol from the path (if it contains a full URL)
     */
    public String getProtocol() {
        if (path != null && path.startsWith("https://")) {
            return "https";
        }
        return "http"; // Default to http
    }

    /**
     * Extract host from path if it's a full URL, otherwise use the host field
     */
    public String getEffectiveHost() {
        if (path != null && (path.startsWith("http://") || path.startsWith("https://"))) {
            try {
                java.net.URL url = new java.net.URL(path);
                return url.getHost();
            } catch (Exception e) {
                // If parsing fails, use the host field
            }
        }
        return host;
    }

    /**
     * Extract path without host if path contains full URL
     */
    public String getEffectivePath() {
        if (path != null && (path.startsWith("http://") || path.startsWith("https://"))) {
            try {
                java.net.URL url = new java.net.URL(path);
                String pathOnly = url.getPath();
                if (pathOnly == null || pathOnly.isEmpty()) {
                    return "/";
                }
                if (url.getQuery() != null) {
                    return pathOnly + "?" + url.getQuery();
                }
                return pathOnly;
            } catch (Exception e) {
                // If parsing fails, use the path as is
            }
        }
        return path;
    }

    /**
     * Get effective port number
     */
    public int getEffectivePort() {
        if (path != null && (path.startsWith("http://") || path.startsWith("https://"))) {
            try {
                java.net.URL url = new java.net.URL(path);
                int urlPort = url.getPort();
                if (urlPort != -1) {
                    return urlPort;
                }
                return "https".equals(url.getProtocol()) ? 443 : 80;
            } catch (Exception e) {
                // If parsing fails, use the port field
            }
        }
        return port;
    }
}
