package com.protect7.authanalyzer.util;

/**
 * Utility class for truncating long paths in Postman exports
 *
 * @author Claude Code
 */
public class PathTruncationUtil {

    private static final int MIN_PATH_LENGTH_FOR_TRUNCATION = 8;
    private static final String TRUNCATION_INDICATOR = "...";

    /**
     * Truncate a path if it exceeds the specified maximum length
     *
     * @param path The original path to truncate
     * @param maxLength Maximum length for the path
     * @return Truncated path if needed, original path otherwise
     */
    public static String truncatePath(String path, int maxLength) {
        if (path == null) {
            return "/";
        }

        // Remove leading/trailing whitespace
        path = path.trim();

        // If path is empty, return root
        if (path.isEmpty()) {
            return "/";
        }

        // If path is short (<= 8 chars) or within allowed length, return as-is
        if (path.length() <= MIN_PATH_LENGTH_FOR_TRUNCATION || path.length() <= maxLength) {
            return path;
        }

        // For very short maxLength, return just truncation indicator
        if (maxLength < TRUNCATION_INDICATOR.length()) {
            return TRUNCATION_INDICATOR;
        }

        // Truncate path: keep beginning and add "..."
        int keepLength = maxLength - TRUNCATION_INDICATOR.length();
        return path.substring(0, keepLength) + TRUNCATION_INDICATOR;
    }

    /**
     * Check if a path would be truncated given the maximum length
     *
     * @param path The path to check
     * @param maxLength Maximum allowed length
     * @return true if path would be truncated, false otherwise
     */
    public static boolean wouldBeTruncated(String path, int maxLength) {
        if (path == null) {
            return false;
        }

        path = path.trim();

        // Short paths (<= 8 chars) are never truncated
        if (path.length() <= MIN_PATH_LENGTH_FOR_TRUNCATION) {
            return false;
        }

        return path.length() > maxLength;
    }

    /**
     * Get the effective path length after truncation
     *
     * @param originalLength Original path length
     * @param maxLength Maximum allowed length
     * @return Length after applying truncation rules
     */
    public static int getTruncatedLength(int originalLength, int maxLength) {
        if (originalLength <= MIN_PATH_LENGTH_FOR_TRUNCATION || originalLength <= maxLength) {
            return originalLength;
        }

        // Truncated length is always maxLength
        return maxLength;
    }

    /**
     * Validate if the truncation length is within acceptable range
     *
     * @param length The length to validate
     * @param minLength Minimum allowed length
     * @param maxLength Maximum allowed length
     * @return true if valid, false otherwise
     */
    public static boolean isValidTruncationLength(int length, int minLength, int maxLength) {
        return length >= minLength && length <= maxLength;
    }

    /**
     * Get a recommended truncation length based on path characteristics
     *
     * @param path The path to analyze
     * @return Recommended truncation length
     */
    public static int getRecommendedLength(String path) {
        if (path == null || path.trim().isEmpty()) {
            return 32; // Default for empty/null paths
        }

        int length = path.trim().length();

        // For very short paths, no truncation needed
        if (length <= 32) {
            return length;
        }

        // For medium paths, return original length or a reasonable default
        if (length <= 64) {
            return length;
        }

        // For long paths, return a reasonable default
        return 64;
    }
}