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
}