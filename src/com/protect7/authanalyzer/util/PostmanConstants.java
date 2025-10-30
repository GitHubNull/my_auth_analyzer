package com.protect7.authanalyzer.util;

/**
 * Constants for Postman Collection v2.1 format
 *
 * @author Claude Code
 */
public class PostmanConstants {

    // Postman Collection Schema
    public static final String SCHEMA_URL = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

    // Collection Info
    public static final String DEFAULT_COLLECTION_NAME = "Auth Analyzer Export";
    public static final String DEFAULT_COLLECTION_DESCRIPTION = "Requests exported from Auth Analyzer for authorization testing";

    // Body Modes
    public static final String BODY_MODE_RAW = "raw";
    public static final String BODY_MODE_FORMDATA = "formdata";
    public static final String BODY_MODE_URLENCODED = "urlencoded";
    public static final String BODY_MODE_FILE = "file";

    // Content Types for raw body
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    // Request Status
    public static final String STATUS_BYPLACED = "BYPLACED";
    public static final String STATUS_SAME = "SAME";
    public static final String STATUS_SIMILAR = "SIMILAR";
    public static final String STATUS_DIFFERENT = "DIFFERENT";
    public static final String STATUS_NA = "NA";

    // Folder naming
    public static final String FOLDER_PREFIX = "Session: ";

    // Request naming patterns
    public static final String REQUEST_NAME_PATTERN = "%s %s - %s";

    // Description templates
    public static final String REQUEST_DESCRIPTION_TEMPLATE = "Session: %s\nBypass Status: %s\nOriginal Status Code: %d\nTest Status Code: %d\nComment: %s";
    public static final String ORIGINAL_REQUEST_NOTE = "Original request (before modification)";
    public static final String MODIFIED_REQUEST_NOTE = "Modified request used for authorization testing";
}