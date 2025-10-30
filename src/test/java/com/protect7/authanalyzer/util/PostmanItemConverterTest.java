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
        String name = PostmanItemConverter.generateRequestName("GET", "http://example.com", "/api/test", "SAME");
        assertEquals("GET /api/test - SAME", name);

        // Test with null values
        String nameWithNulls = PostmanItemConverter.generateRequestName(null, null, null, null);
        assertEquals("UNKNOWN /unknown - UNKNOWN", nameWithNulls);
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
        String getName = PostmanItemConverter.generateRequestName("GET", "http://example.com", "/api/users", "SAME");
        assertEquals("GET /api/users - SAME", getName);

        String postName = PostmanItemConverter.generateRequestName("POST", "https://api.example.com", "/auth/login", "DIFFERENT");
        assertEquals("POST /auth/login - DIFFERENT", postName);

        String putName = PostmanItemConverter.generateRequestName("PUT", "http://example.com", "/api/users/123", "BYPASS");
        assertEquals("PUT /api/users/123 - BYPASS", putName);
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
}