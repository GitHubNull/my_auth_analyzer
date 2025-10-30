package com.protect7.authanalyzer.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oxff.entities.ExportAuthAnalyzerDataItem;
import org.oxff.entities.SessionHTTPData;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.protect7.authanalyzer.util.PostmanConstants;

/**
 * Converter for transforming HTTP data into Postman collection items
 *
 * @author Claude Code
 */
public class PostmanItemConverter {

    /**
     * Convert ExportAuthAnalyzerDataItem to Postman request object
     */
    public static JsonObject convertToPostmanRequest(ExportAuthAnalyzerDataItem exportItem) {
        if (exportItem == null) {
            return null;
        }

        JsonObject request = new JsonObject();

        // Set method
        request.addProperty("method", exportItem.getMethod());

        // Set headers
        JsonArray headers = convertHeaders(exportItem.getRequestHeaders());
        request.add("header", headers);

        // Set URL
        String urlString = exportItem.getFullUrl();
        JsonObject url = buildUrlObject(urlString);
        request.add("url", url);

        // Set body if present
        if (exportItem.getRequestBody() != null && !exportItem.getRequestBody().trim().isEmpty()) {
            JsonObject body = buildBodyObject(exportItem.getRequestHeaders(), exportItem.getRequestBody());
            if (body != null) {
                request.add("body", body);
            }
        }

        return request;
    }

    /**
     * Convert SessionHTTPData to Postman request object
     */
    public static JsonObject convertToPostmanRequest(SessionHTTPData sessionData, String host, int port, String path) {
        if (sessionData == null) {
            return null;
        }

        JsonObject request = new JsonObject();

        // Use the dedicated method field
        String method = sessionData.getMethod();
        request.addProperty("method", method != null ? method : "GET");

        // Set headers
        JsonArray headers = convertHeaders(sessionData.getRequestHeaders());
        request.add("header", headers);

        // Set URL
        String urlString = buildUrlString(host, port, path);
        JsonObject url = buildUrlObject(urlString);
        request.add("url", url);

        // Set body if present
        if (sessionData.getRequestBody() != null && !sessionData.getRequestBody().trim().isEmpty()) {
            JsonObject body = buildBodyObject(sessionData.getRequestHeaders(), sessionData.getRequestBody());
            if (body != null) {
                request.add("body", body);
            }
        }

        return request;
    }

    /**
     * Extract method from request headers
     */
    private static String extractMethodFromHeaders(String requestHeaders) {
        if (requestHeaders == null || requestHeaders.trim().isEmpty()) {
            return "GET";
        }

        String[] lines = requestHeaders.split("\n");
        if (lines.length > 0) {
            String firstLine = lines[0].trim();
            String[] parts = firstLine.split(" ");
            if (parts.length > 0) {
                return parts[0];
            }
        }

        return "GET";
    }

    /**
     * Convert headers string to Postman header array
     */
    private static JsonArray convertHeaders(String headersString) {
        JsonArray headers = new JsonArray();

        if (headersString == null || headersString.trim().isEmpty()) {
            return headers;
        }

        String[] lines = headersString.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }

            // Skip HTTP request line (e.g., "GET /path HTTP/1.1")
            if (line.matches("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|TRACE|CONNECT)\\s+.*\\s+HTTP/[0-9.]+$")) {
                continue;
            }

            // Skip lines that don't contain a colon (not valid headers)
            if (!line.contains(":")) {
                continue;
            }

            int colonIndex = line.indexOf(":");
            String name = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();

            JsonObject header = new JsonObject();
            header.addProperty("key", name);
            header.addProperty("value", value);
            headers.add(header);
        }

        return headers;
    }

    /**
     * Build URL string from components
     */
    private static String buildUrlString(String host, int port, String path) {
        StringBuilder url = new StringBuilder();

        // Detect protocol from path if it contains a full URL
        String protocol = "http";
        if (path != null && path.startsWith("https://")) {
            protocol = "https";
        }
        url.append(protocol).append("://");

        if (host != null && !host.trim().isEmpty()) {
            url.append(host);
        } else {
            url.append("localhost");
        }

        if (port != 80 && port != 443 && port > 0) {
            url.append(":").append(port);
        }

        if (path != null && !path.trim().isEmpty()) {
            // If path is a full URL, extract just the path part
            if (path.startsWith("http://") || path.startsWith("https://")) {
                try {
                    URL urlObj = new URL(path);
                    String pathOnly = urlObj.getPath();
                    if (pathOnly == null || pathOnly.isEmpty()) {
                        pathOnly = "/";
                    }
                    if (urlObj.getQuery() != null) {
                        pathOnly += "?" + urlObj.getQuery();
                    }
                    url.append(pathOnly);
                } catch (Exception e) {
                    // If parsing fails, use path as is
                    if (!path.startsWith("/")) {
                        url.append("/");
                    }
                    url.append(path);
                }
            } else {
                if (!path.startsWith("/")) {
                    url.append("/");
                }
                url.append(path);
            }
        } else {
            url.append("/");
        }

        return url.toString();
    }

    /**
     * Build Postman URL object from URL string
     */
    private static JsonObject buildUrlObject(String urlString) {
        JsonObject url = new JsonObject();

        if (urlString == null || urlString.trim().isEmpty()) {
            return url;
        }

        try {
            URL urlObj = new URL(urlString);

            // Raw URL
            url.addProperty("raw", urlString);

            // Protocol
            String protocol = urlObj.getProtocol();
            url.addProperty("protocol", protocol);

            // Host
            JsonArray host = new JsonArray();
            String hostName = urlObj.getHost();
            if (hostName != null) {
                String[] hostParts = hostName.split("\\.");
                for (String part : hostParts) {
                    host.add(part);
                }
            }
            url.add("host", host);

            // Port
            int port = urlObj.getPort();
            if (port != -1) {
                url.addProperty("port", port);
            }

            // Path
            JsonArray path = new JsonArray();
            String pathString = urlObj.getPath();
            if (pathString != null && !pathString.isEmpty() && !pathString.equals("/")) {
                pathString = pathString.startsWith("/") ? pathString.substring(1) : pathString;
                String[] pathParts = pathString.split("/");
                for (String part : pathParts) {
                    if (!part.isEmpty()) {
                        path.add(part);
                    }
                }
            }
            url.add("path", path);

            // Query
            String query = urlObj.getQuery();
            if (query != null && !query.isEmpty()) {
                JsonArray queryArray = new JsonArray();
                String[] queryParams = query.split("&");
                for (String param : queryParams) {
                    String[] keyValue = param.split("=", 2);
                    JsonObject queryParam = new JsonObject();
                    queryParam.addProperty("key", keyValue[0]);
                    // URL decode the value for better readability in Postman
                    String value = keyValue.length > 1 ? keyValue[1] : "";
                    try {
                        queryParam.addProperty("value", java.net.URLDecoder.decode(value, "UTF-8"));
                    } catch (Exception e) {
                        queryParam.addProperty("value", value);
                    }
                    queryArray.add(queryParam);
                }
                url.add("query", queryArray);
            }

        } catch (Exception e) {
            // If URL parsing fails, create a simple raw URL object
            url.addProperty("raw", urlString);
        }

        return url;
    }

    /**
     * Build Postman body object based on content type and body content
     */
    private static JsonObject buildBodyObject(String headers, String body) {
        if (body == null || body.trim().isEmpty()) {
            return null;
        }

        JsonObject bodyObj = new JsonObject();
        String contentType = extractContentType(headers);

        if (contentType != null) {
            contentType = contentType.toLowerCase();
            if (contentType.contains(PostmanConstants.CONTENT_TYPE_JSON) ||
                contentType.contains(PostmanConstants.CONTENT_TYPE_XML)) {
                // Raw mode for JSON and XML
                bodyObj.addProperty("mode", PostmanConstants.BODY_MODE_RAW);
                bodyObj.addProperty("options", "{\"raw\": {}}");
                bodyObj.addProperty("raw", body);
            } else if (contentType.contains(PostmanConstants.CONTENT_TYPE_FORM)) {
                // URL-encoded mode
                bodyObj.addProperty("mode", PostmanConstants.BODY_MODE_URLENCODED);
                JsonArray urlencoded = new JsonArray();
                String[] pairs = body.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=", 2);
                    JsonObject param = new JsonObject();
                    param.addProperty("key", keyValue[0]);
                    param.addProperty("value", keyValue.length > 1 ? keyValue[1] : "");
                    urlencoded.add(param);
                }
                bodyObj.add("urlencoded", urlencoded);
            } else if (contentType.contains("multipart/form-data")) {
                // Multipart form data - use raw mode for now
                bodyObj.addProperty("mode", PostmanConstants.BODY_MODE_RAW);
                bodyObj.addProperty("options", "{\"raw\": {}}");
                bodyObj.addProperty("raw", body);
            } else {
                // Default to raw mode
                bodyObj.addProperty("mode", PostmanConstants.BODY_MODE_RAW);
                bodyObj.addProperty("options", "{\"raw\": {}}");
                bodyObj.addProperty("raw", body);
            }
        } else {
            // Default to raw mode if no content type
            bodyObj.addProperty("mode", PostmanConstants.BODY_MODE_RAW);
            bodyObj.addProperty("options", "{\"raw\": {}}");
            bodyObj.addProperty("raw", body);
        }

        return bodyObj;
    }

    /**
     * Extract content type from headers
     */
    private static String extractContentType(String headers) {
        if (headers == null || headers.trim().isEmpty()) {
            return null;
        }

        String[] lines = headers.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.toLowerCase().startsWith("content-type:")) {
                return line.substring("content-type:".length()).trim();
            }
        }

        return null;
    }

    /**
     * Generate request name based on method, path, and bypass status
     */
    public static String generateRequestName(String method, String host, String path, String bypassStatus) {
        String pathOnly = extractPathFromUrl(host + path);
        return String.format(PostmanConstants.REQUEST_NAME_PATTERN,
                           method != null ? method : "UNKNOWN",
                           pathOnly != null ? pathOnly : "/unknown",
                           bypassStatus != null ? bypassStatus : "UNKNOWN");
    }

    /**
     * Generate description for Postman item
     */
    public static String generateDescription(String sessionName, String bypassStatus,
                                           int originalStatusCode, int testStatusCode,
                                           String comment) {
        return String.format(PostmanConstants.REQUEST_DESCRIPTION_TEMPLATE,
                           sessionName != null ? sessionName : "Unknown Session",
                           bypassStatus != null ? bypassStatus : "UNKNOWN",
                           originalStatusCode,
                           testStatusCode,
                           comment != null ? comment : "");
    }

    /**
     * Extract path from URL for naming
     */
    private static String extractPathFromUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "/unknown";
        }

        try {
            URL urlObj = new URL(url);
            String path = urlObj.getPath();
            if (path == null || path.isEmpty() || path.equals("/")) {
                return "/";
            }
            return path;
        } catch (Exception e) {
            // If parsing fails, return a simplified path
            if (url.contains("://")) {
                int protocolEnd = url.indexOf("://");
                int pathStart = url.indexOf("/", protocolEnd + 3);
                if (pathStart != -1) {
                    return url.substring(pathStart);
                }
            }
            return "/unknown";
        }
    }

    /**
     * Create Postman response object from HTTP response data
     */
    public static JsonObject convertToPostmanResponse(SessionHTTPData responseData, String originalRequestName) {
        if (responseData == null) {
            return null;
        }

        JsonObject response = new JsonObject();

        // Basic response info
        response.addProperty("name", "Response for " + originalRequestName);
        response.addProperty("originalRequest", originalRequestName);
        response.addProperty("status", responseData.getResponseStatusCode() + " " + getReasonPhrase(responseData.getResponseStatusCode()));
        response.addProperty("code", responseData.getResponseStatusCode());

        // Headers
        JsonArray headers = convertHeaders(responseData.getResponseHeaders());
        response.add("header", headers);

        // Body
        if (responseData.getResponseBody() != null) {
            response.addProperty("body", responseData.getResponseBody());
        }

        return response;
    }

    /**
     * Create Postman response object from ExportAuthAnalyzerDataItem
     */
    public static JsonObject convertToPostmanResponse(ExportAuthAnalyzerDataItem responseData, String originalRequestName) {
        if (responseData == null) {
            return null;
        }

        JsonObject response = new JsonObject();

        // Basic response info
        response.addProperty("name", "Response for " + originalRequestName);
        response.addProperty("originalRequest", originalRequestName);
        response.addProperty("status", responseData.getResponseStatusCode() + " " + getReasonPhrase(responseData.getResponseStatusCode()));
        response.addProperty("code", responseData.getResponseStatusCode());

        // Headers
        JsonArray headers = convertHeaders(responseData.getResponseHeaders());
        response.add("header", headers);

        // Body
        if (responseData.getResponseBody() != null) {
            response.addProperty("body", responseData.getResponseBody());
        }

        return response;
    }

    /**
     * Get reason phrase for HTTP status code
     */
    private static String getReasonPhrase(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 204: return "No Content";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            default: return "Unknown";
        }
    }
}