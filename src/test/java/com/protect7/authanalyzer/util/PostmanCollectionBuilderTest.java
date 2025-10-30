package com.protect7.authanalyzer.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.oxff.entities.ExportAuthAnalyzerDataItem;
import org.oxff.entities.SessionHTTPData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.protect7.authanalyzer.util.BypassConstants;

/**
 * Unit tests for PostmanCollectionBuilder
 */
public class PostmanCollectionBuilderTest {

    @Test
    public void testBasicCollectionCreation() {
        PostmanCollectionBuilder builder = new PostmanCollectionBuilder();

        // Set collection info
        builder.setCollectionInfo("Test Collection", "Test Description");

        // Create empty list
        List<ExportAuthAnalyzerDataItem> exportData = new ArrayList<>();
        builder.addExportData(exportData);

        // Build collection
        String collectionJson = builder.build();

        // Verify it's valid JSON
        assertNotNull(collectionJson);
        assertFalse(collectionJson.isEmpty());

        // Parse and verify structure
        Gson gson = new Gson();
        JsonObject collection = gson.fromJson(collectionJson, JsonObject.class);

        assertTrue(collection.has("info"));
        assertTrue(collection.has("item"));

        JsonObject info = collection.getAsJsonObject("info");
        assertEquals("Test Collection", info.get("name").getAsString());
        assertEquals("Test Description", info.get("description").getAsString());
        assertEquals(PostmanConstants.SCHEMA_URL, info.get("schema").getAsString());
    }

    @Test
    public void testValidation() {
        PostmanCollectionBuilder builder = new PostmanCollectionBuilder();

        // Empty collection should still be valid
        assertTrue(builder.validateCollection());

        // After setting info, should still be valid
        builder.setCollectionInfo("Test", "Test");
        assertTrue(builder.validateCollection());
    }

    @Test
    public void testStatistics() {
        PostmanCollectionBuilder builder = new PostmanCollectionBuilder();

        // Get stats for empty collection
        java.util.Map<String, Object> stats = builder.getStatistics();
        assertEquals(0, stats.get("totalFolders"));
        assertEquals(0, stats.get("totalItems"));
        assertEquals(true, stats.get("includeOriginalRequests"));

        // Change include original requests setting
        builder.setIncludeOriginalRequests(false);
        stats = builder.getStatistics();
        assertEquals(false, stats.get("includeOriginalRequests"));
    }

    @Test
    public void testBuildAsJsonObject() {
        PostmanCollectionBuilder builder = new PostmanCollectionBuilder();

        JsonObject collection = builder.buildAsJsonObject();

        assertNotNull(collection);
        assertTrue(collection.has("info"));
        assertTrue(collection.has("item"));
    }
}