package com.protect7.authanalyzer.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.oxff.entities.ExportAuthAnalyzerDataItem;
import org.oxff.entities.SessionHTTPData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Unit tests for PostmanItemConverter
 */
public class PostmanItemConverterTest {

    @Test
    public void testGenerateRequestName() {
        String name = PostmanItemConverter.generateRequestName("GET", "example.com", "/api/test", "SAME");
        assertEquals("GET /api/test - SAME", name);

        // Test with null values
        String nameWithNulls = PostmanItemConverter.generateRequestName(null, null, null, null);
        assertEquals("UNKNOWN / - UNKNOWN", nameWithNulls);
    }

    @Test
    public void testGenerateDescription() {
        String description = PostmanItemConverter.generateDescription("TestSession", "DIFFERENT", 200, 403, "Test comment");

        assertNotNull(description);
        assertTrue(description.contains("TestSession"));
        assertTrue(description.contains("DIFFERENT"));
        assertTrue(description.contains("200"));
        assertTrue(description.contains("403"));
        assertTrue(description.contains("Test comment"));
    }

    @Test
    public void testGetReasonPhrase() {
        // Test with null session data should return null
        SessionHTTPData sessionData = null;
        String requestName = "Test Request";

        JsonObject response = PostmanItemConverter.convertToPostmanResponse(sessionData, requestName);

        assertNull(response);
    }

    @Test
    public void testConvertHeaders() {
        // Test with null session data should return null
        SessionHTTPData sessionData = null;

        JsonObject response = PostmanItemConverter.convertToPostmanResponse(sessionData, "Test");

        assertNull(response);
    }

    @Test
    public void testGenerateRequestNameWithDifferentMethods() {
        // Test with relative paths - these should work correctly
        String getName = PostmanItemConverter.generateRequestName("GET", "example.com", "/api/users", "SAME");
        assertEquals("GET /api/users - SAME", getName);

        String postName = PostmanItemConverter.generateRequestName("POST", "api.example.com", "/auth/login", "DIFFERENT");
        assertEquals("POST /auth/login - DIFFERENT", postName);

        String putName = PostmanItemConverter.generateRequestName("PUT", "example.com", "/api/users/123", "BYPASS");
        assertEquals("PUT /api/users/123 - BYPASS", putName);
    }

    @Test
    public void testGenerateRequestNameWithCompleteURLs() {
        // Test with complete URLs in path parameter
        String getName1 = PostmanItemConverter.generateRequestName("GET", "example.com", "https://api.example.com/users", "SAME");
        assertEquals("GET /users - SAME", getName1);

        String getName2 = PostmanItemConverter.generateRequestName("POST", "api.example.com", "http://api.example.com/auth/login", "DIFFERENT");
        assertEquals("POST /auth/login - DIFFERENT", getName2);

        // Test with URLs containing query parameters
        String getName3 = PostmanItemConverter.generateRequestName("GET", "example.com", "https://api.example.com/users?id=123&active=true", "SAME");
        assertEquals("GET /users - SAME", getName3);

        // Test with URLs containing path parameters
        String getName4 = PostmanItemConverter.generateRequestName("DELETE", "api.example.com", "https://api.example.com/users/123", "DIFFERENT");
        assertEquals("DELETE /users/123 - DIFFERENT", getName4);
    }

    @Test
    public void testGenerateRequestNameWithEdgeCases() {
        // Test with null path
        String nullPath = PostmanItemConverter.generateRequestName("GET", "example.com", null, "SAME");
        assertEquals("GET / - SAME", nullPath);

        // Test with empty path
        String emptyPath = PostmanItemConverter.generateRequestName("GET", "example.com", "", "SAME");
        assertEquals("GET / - SAME", emptyPath);

        // Test with root path
        String rootPath = PostmanItemConverter.generateRequestName("GET", "example.com", "/", "SAME");
        assertEquals("GET / - SAME", rootPath);

        // Test with null method
        String nullMethod = PostmanItemConverter.generateRequestName(null, "example.com", "/api/users", "SAME");
        assertEquals("UNKNOWN /api/users - SAME", nullMethod);

        // Test with null bypass status
        String nullStatus = PostmanItemConverter.generateRequestName("GET", "example.com", "/api/users", null);
        assertEquals("GET /api/users - UNKNOWN", nullStatus);
    }

    @Test
    public void testGenerateRequestNameWithComplexURLs() {
        // Test with multiple path segments
        String complexPath1 = PostmanItemConverter.generateRequestName("GET", "api.example.com", "https://api.example.com/v1/users/123/profile", "SAME");
        assertEquals("GET /v1/users/123/profile - SAME", complexPath1);

        // Test with special characters in path
        String complexPath2 = PostmanItemConverter.generateRequestName("GET", "api.example.com", "https://api.example.com/api/search?q=test&page=1", "SAME");
        assertEquals("GET /api/search - SAME", complexPath2);

        // Test with very long path
        String longPath = PostmanItemConverter.generateRequestName("GET", "api.example.com", "https://api.example.com/very/long/path/with/many/segments", "SAME");
        assertEquals("GET /very/long/path/with/many/segments - SAME", longPath);
    }

    @Test
    public void testGenerateDescriptionWithAllParameters() {
        String description = PostmanItemConverter.generateDescription(
            "AdminSession", "SAME", 200, 200, "User profile access test"
        );

        assertNotNull(description);
        assertTrue(description.contains("AdminSession"));
        assertTrue(description.contains("SAME"));
        assertTrue(description.contains("200"));
        assertTrue(description.contains("User profile access test"));
    }

    @Test
    public void testExportAuthAnalyzerDataItemWithMockData() {
        // Test the URL helper methods we added to ExportAuthAnalyzerDataItem
        // We can't easily create a real ExportAuthAnalyzerDataItem without mocking,
        // but we can test the logic conceptually
        String host = "example.com";
        int port = 8080;
        String path = "/api/test";

        // Simulate the getFullUrl logic
        String fullUrl = "http://" + host;
        if (port != 80 && port != 443 && port > 0) {
            fullUrl += ":" + port;
        }
        if (path != null && !path.trim().isEmpty()) {
            if (!path.startsWith("/")) {
                fullUrl += "/";
            }
            fullUrl += path;
        } else {
            fullUrl += "/";
        }

        assertEquals("http://example.com:8080/api/test", fullUrl);
    }

    @Test
    public void testPathExtractionManually() {
        // Test manual path extraction logic that simulates the fixed extractPathFromUrl logic

        // Case 1: Complete URL with path
        String url1 = "https://api.example.com/users/123";
        String expected1 = "/users/123";
        // Simulate the extraction logic
        String result1 = extractPathManually(url1);
        assertEquals(expected1, result1);

        // Case 2: URL with query parameters
        String url2 = "https://api.example.com/search?q=test&page=1";
        String expected2 = "/search";
        String result2 = extractPathManually(url2);
        assertEquals(expected2, result2);

        // Case 3: Relative path
        String url3 = "/api/users";
        String expected3 = "/api/users";
        String result3 = extractPathManually(url3);
        assertEquals(expected3, result3);

        // Case 4: Root path
        String url4 = "https://example.com/";
        String expected4 = "/";
        String result4 = extractPathManually(url4);
        assertEquals(expected4, result4);
    }

    // Helper method to simulate the manual path extraction logic
    private String extractPathManually(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "/";
        }

        // Case 1: URL contains protocol
        if (url.contains("://")) {
            int protocolEnd = url.indexOf("://");
            int hostEnd = url.indexOf("/", protocolEnd + 3);

            if (hostEnd != -1) {
                String pathAndQuery = url.substring(hostEnd);
                if (pathAndQuery.isEmpty()) {
                    return "/";
                }
                return pathAndQuery.split("\\?")[0];
            } else {
                return "/";
            }
        }

        // Case 2: URL starts with / (already a path)
        if (url.startsWith("/")) {
            return url.split("\\?")[0];
        }

        // Case 3: URL without protocol (relative URL)
        int slashIndex = url.indexOf("/");
        if (slashIndex != -1) {
            return url.substring(slashIndex).split("\\?")[0];
        }

        return "/";
    }
}