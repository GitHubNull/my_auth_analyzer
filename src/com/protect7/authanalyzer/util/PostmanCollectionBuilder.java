package com.protect7.authanalyzer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oxff.entities.ExportAuthAnalyzerDataItem;
import org.oxff.entities.SessionHTTPData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.protect7.authanalyzer.entities.AnalyzerRequestResponse;
import com.protect7.authanalyzer.entities.OriginalRequestResponse;
import com.protect7.authanalyzer.util.PostmanConstants;
import com.protect7.authanalyzer.util.PostmanItemConverter;

/**
 * Builder class for creating Postman Collection v2.1 format
 *
 * @author Claude Code
 */
public class PostmanCollectionBuilder {

    private JsonObject collection;
    private Gson gson;
    private boolean includeOriginalRequests;

    public PostmanCollectionBuilder() {
        this.gson = new Gson();
        this.includeOriginalRequests = true; // Default to including original requests
        initializeCollection();
    }

    /**
     * Initialize the basic Postman collection structure
     */
    private void initializeCollection() {
        collection = new JsonObject();

        // Info section
        JsonObject info = new JsonObject();
        info.addProperty("name", PostmanConstants.DEFAULT_COLLECTION_NAME);
        info.addProperty("description", PostmanConstants.DEFAULT_COLLECTION_DESCRIPTION);
        info.addProperty("schema", PostmanConstants.SCHEMA_URL);
        collection.add("info", info);

        // Empty items array
        collection.add("item", new JsonArray());

        // Auth (empty for now)
        collection.add("auth", new JsonObject());

        // Event (empty for now)
        collection.add("event", new JsonArray());

        // Variables (empty for now)
        collection.add("variable", new JsonArray());
    }

    /**
     * Set whether to include original requests in the collection
     */
    public PostmanCollectionBuilder setIncludeOriginalRequests(boolean includeOriginalRequests) {
        this.includeOriginalRequests = includeOriginalRequests;
        return this;
    }

    /**
     * Set custom collection name and description
     */
    public PostmanCollectionBuilder setCollectionInfo(String name, String description) {
        JsonObject info = collection.getAsJsonObject("info");
        if (name != null) {
            info.addProperty("name", name);
        }
        if (description != null) {
            info.addProperty("description", description);
        }
        return this;
    }

    /**
     * Add export data items to the collection
     */
    public PostmanCollectionBuilder addExportData(List<ExportAuthAnalyzerDataItem> exportData) {
        if (exportData == null || exportData.isEmpty()) {
            return this;
        }

        // Group items by session name
        Map<String, List<ExportAuthAnalyzerDataItem>> sessionGroups = groupBySession(exportData);

        // Create folders for each session
        JsonArray items = collection.getAsJsonArray("item");
        for (Map.Entry<String, List<ExportAuthAnalyzerDataItem>> entry : sessionGroups.entrySet()) {
            String sessionName = entry.getKey();
            List<ExportAuthAnalyzerDataItem> sessionItems = entry.getValue();

            JsonObject sessionFolder = createSessionFolder(sessionName, sessionItems);
            items.add(sessionFolder);
        }

        return this;
    }

    /**
     * Group export data items by session name
     */
    private Map<String, List<ExportAuthAnalyzerDataItem>> groupBySession(List<ExportAuthAnalyzerDataItem> exportData) {
        Map<String, List<ExportAuthAnalyzerDataItem>> sessionGroups = new HashMap<>();

        for (ExportAuthAnalyzerDataItem item : exportData) {
            List<SessionHTTPData> sessionDataList = item.getSessionsHTTPDataList();
            if (sessionDataList != null && !sessionDataList.isEmpty()) {
                for (SessionHTTPData sessionData : sessionDataList) {
                    String sessionName = sessionData.getSessionName();
                    if (sessionName == null || sessionName.trim().isEmpty()) {
                        sessionName = "Unknown Session";
                    }

                    sessionGroups.computeIfAbsent(sessionName, k -> new ArrayList<>()).add(item);
                }
            } else {
                // Items without session data go to a default folder
                sessionGroups.computeIfAbsent("No Session", k -> new ArrayList<>()).add(item);
            }
        }

        return sessionGroups;
    }

    /**
     * Create a folder for a specific session
     */
    private JsonObject createSessionFolder(String sessionName, List<ExportAuthAnalyzerDataItem> sessionItems) {
        JsonObject folder = new JsonObject();

        // Folder info
        folder.addProperty("name", PostmanConstants.FOLDER_PREFIX + sessionName);
        folder.addProperty("description", "Requests from session: " + sessionName);

        // Folder items
        JsonArray folderItems = new JsonArray();

        // Process each export data item
        for (ExportAuthAnalyzerDataItem exportItem : sessionItems) {
            List<SessionHTTPData> sessionDataList = exportItem.getSessionsHTTPDataList();

            // Find the session data that matches this folder
            SessionHTTPData matchingSessionData = null;
            for (SessionHTTPData sessionData : sessionDataList) {
                if (sessionName.equals(sessionData.getSessionName())) {
                    matchingSessionData = sessionData;
                    break;
                }
            }

            if (matchingSessionData != null) {
                JsonObject postmanItem = createPostmanItem(exportItem, matchingSessionData);
                if (postmanItem != null) {
                    folderItems.add(postmanItem);
                }
            }
        }

        folder.add("item", folderItems);

        return folder;
    }

    /**
     * Create a Postman item from export data and session data
     */
    private JsonObject createPostmanItem(ExportAuthAnalyzerDataItem exportItem, SessionHTTPData sessionData) {
        JsonObject postmanItem = new JsonObject();

        // Request name
        String bypassStatus = getBypassStatusString(sessionData.getStatus());
        String requestName = PostmanItemConverter.generateRequestName(
                sessionData.getMethod() != null ? sessionData.getMethod() : exportItem.getMethod(),
                exportItem.getEffectiveHost(),
                exportItem.getEffectivePath(),
                bypassStatus
        );
        postmanItem.addProperty("name", requestName);

        // Request from session data (modified request)
        JsonObject request = PostmanItemConverter.convertToPostmanRequest(
                sessionData,
                exportItem.getHost(),
                exportItem.getPort(),
                exportItem.getPath()
        );
        postmanItem.add("request", request);

        // Response from session data
        JsonObject response = PostmanItemConverter.convertToPostmanResponse(
                sessionData,
                requestName
        );
        JsonArray responses = new JsonArray();
        responses.add(response);
        postmanItem.add("response", responses);

        // Description with metadata
        String description = PostmanItemConverter.generateDescription(
                sessionData.getSessionName(),
                bypassStatus,
                exportItem.getResponseStatusCode(),
                sessionData.getResponseStatusCode(),
                exportItem.getComment()
        );
        postmanItem.addProperty("description", description);

        // Add original request if option is enabled
        if (includeOriginalRequests) {
            JsonObject originalItem = createOriginalRequestItem(exportItem);
            if (originalItem != null) {
                postmanItem.add("originalRequest", originalItem);
            }
        }

        return postmanItem;
    }

    /**
     * Create an item for the original request
     */
    private JsonObject createOriginalRequestItem(ExportAuthAnalyzerDataItem exportItem) {
        JsonObject originalItem = new JsonObject();
        originalItem.addProperty("name", PostmanItemConverter.generateRequestName(
                exportItem.getMethod(),
                exportItem.getEffectiveHost(),
                exportItem.getEffectivePath(),
                "ORIGINAL"
        ) + " " + PostmanConstants.ORIGINAL_REQUEST_NOTE);

        JsonObject request = PostmanItemConverter.convertToPostmanRequest(exportItem);
        originalItem.add("request", request);

        // Response from original data
        JsonObject response = PostmanItemConverter.convertToPostmanResponse(
                exportItem,
                originalItem.get("name").getAsString()
        );
        JsonArray responses = new JsonArray();
        responses.add(response);
        originalItem.add("response", responses);

        originalItem.addProperty("description", PostmanConstants.ORIGINAL_REQUEST_NOTE);

        return originalItem;
    }

    /**
     * Convert bypass status to string
     */
    private String getBypassStatusString(com.protect7.authanalyzer.util.BypassConstants bypassStatus) {
        if (bypassStatus == null) {
            return PostmanConstants.STATUS_NA;
        }
        return bypassStatus.getName();
    }

    /**
     * Build the final Postman collection JSON
     */
    public String build() {
        return gson.toJson(collection);
    }

    /**
     * Build the Postman collection as JsonObject
     */
    public JsonObject buildAsJsonObject() {
        return collection;
    }

    /**
     * Validate the collection against Postman schema requirements
     */
    public boolean validateCollection() {
        try {
            // Basic validation
            if (!collection.has("info") || !collection.has("item")) {
                return false;
            }

            JsonObject info = collection.getAsJsonObject("info");
            if (!info.has("name") || !info.has("schema")) {
                return false;
            }

            // Check schema URL
            String schema = info.get("schema").getAsString();
            if (!PostmanConstants.SCHEMA_URL.equals(schema)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get collection statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        int totalItems = 0;
        int totalFolders = 0;
        JsonArray items = collection.getAsJsonArray("item");

        for (int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            if (item.has("item") && item.get("item").isJsonArray()) {
                totalFolders++;
                totalItems += item.getAsJsonArray("item").size();
            } else {
                totalItems++;
            }
        }

        stats.put("totalFolders", totalFolders);
        stats.put("totalItems", totalItems);
        stats.put("includeOriginalRequests", includeOriginalRequests);

        return stats;
    }
}