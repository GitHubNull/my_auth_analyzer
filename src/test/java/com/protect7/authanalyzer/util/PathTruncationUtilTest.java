package com.protect7.authanalyzer.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for PathTruncationUtil
 */
public class PathTruncationUtilTest {

    @Test
    public void testTruncatePathWithShortPaths() {
        // Test paths shorter than 8 characters - should not be truncated
        assertEquals("/", PathTruncationUtil.truncatePath("/", 64));
        assertEquals("/api", PathTruncationUtil.truncatePath("/api", 64));
        assertEquals("/user", PathTruncationUtil.truncatePath("/user", 64));
        assertEquals("/users", PathTruncationUtil.truncatePath("/users", 64));
        assertEquals("/test", PathTruncationUtil.truncatePath("/test", 64));
        assertEquals("/a/b", PathTruncationUtil.truncatePath("/a/b", 64));
        assertEquals("/123", PathTruncationUtil.truncatePath("/123", 64));
    }

    @Test
    public void testTruncatePathWithExactMaxLength() {
        // Test paths that exactly match max length
        String path8Chars = "/api/users";
        assertEquals("/api/...", PathTruncationUtil.truncatePath(path8Chars, 8)); // 9 chars > 8, so gets truncated

        // Create a 64-character path for testing
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append("/api/v1/users/");
        for (int i = 0; i < 41; i++) { // 41 chars to make total 64
            pathBuilder.append("a");
        }
        String path64Chars = pathBuilder.toString();
        assertEquals(path64Chars, PathTruncationUtil.truncatePath(path64Chars, 64));
    }

    @Test
    public void testTruncatePathWithMediumLength() {
        // Test medium length paths within allowed range
        String path30Chars = "/api/v1/users/profile/settings";
        assertEquals(path30Chars, PathTruncationUtil.truncatePath(path30Chars, 64));

        String path50Chars = "/api/v1/users/12345678901234567890123456789012345678901234567890";
        assertEquals(path50Chars, PathTruncationUtil.truncatePath(path50Chars, 64));
    }

    @Test
    public void testTruncatePathWithLongPaths() {
        // Test long paths that need truncation
        String longPath = "/api/v1/users/12345678-1234-1234-1234-123456789012/profile/settings/security/two-factor/setup/configure";

        // With 64 char limit
        String truncated64 = PathTruncationUtil.truncatePath(longPath, 64);
        assertEquals(64, truncated64.length());
        assertTrue(truncated64.endsWith("..."));

        // With 32 char limit
        String truncated32 = PathTruncationUtil.truncatePath(longPath, 32);
        assertEquals(32, truncated32.length());
        assertTrue(truncated32.endsWith("..."));

        // With 8 char limit (minimum)
        String truncated8 = PathTruncationUtil.truncatePath(longPath, 8);
        assertEquals(8, truncated8.length());
        assertEquals("/api/...", truncated8);
    }

    @Test
    public void testTruncatePathWithVeryShortLimit() {
        // Test with very short limits
        String path = "/api/v1/users";

        // Limit of 4 characters
        assertEquals("/...", PathTruncationUtil.truncatePath(path, 4));

        // Limit of 3 characters
        assertEquals("...", PathTruncationUtil.truncatePath(path, 3));

        // Limit of 2 characters
        assertEquals("...", PathTruncationUtil.truncatePath(path, 2));

        // Limit of 1 character
        assertEquals("...", PathTruncationUtil.truncatePath(path, 1));
    }

    @Test
    public void testTruncatePathWithNullAndEmpty() {
        // Test null and empty paths
        assertEquals("/", PathTruncationUtil.truncatePath(null, 64));
        assertEquals("/", PathTruncationUtil.truncatePath("", 64));
        assertEquals("/", PathTruncationUtil.truncatePath("   ", 64)); // whitespace
    }

    @Test
    public void testTruncatePathWithSpecialCharacters() {
        // Test paths with special characters
        String pathWithSpaces = "/api/v1/users/123456789012345678901234567890123456789012345678901234567890";
        String truncated = PathTruncationUtil.truncatePath(pathWithSpaces, 64);
        assertTrue(truncated.endsWith("..."));

        String pathWithUnicode = "/api/用户/123456789012345678901234567890123456789012345678901234567890";
        String truncatedUnicode = PathTruncationUtil.truncatePath(pathWithUnicode, 64);
        assertTrue(truncatedUnicode.endsWith("..."));
    }

    @Test
    public void testWouldBeTruncated() {
        // Test truncation prediction
        String shortPath = "/api/users";
        assertFalse(PathTruncationUtil.wouldBeTruncated(shortPath, 64));
        assertTrue(PathTruncationUtil.wouldBeTruncated(shortPath, 8));

        String mediumPath = "/api/v1/users/profile";
        assertFalse(PathTruncationUtil.wouldBeTruncated(mediumPath, 64));
        assertTrue(PathTruncationUtil.wouldBeTruncated(mediumPath, 8));

        String longPath = "/api/v1/users/12345678-1234-1234-1234-123456789012/profile/settings/security";
        assertTrue(PathTruncationUtil.wouldBeTruncated(longPath, 64));
        assertFalse(PathTruncationUtil.wouldBeTruncated(longPath, 128));

        assertFalse(PathTruncationUtil.wouldBeTruncated(null, 64));
    }

    @Test
    public void testGetTruncatedLength() {
        // Test truncated length calculation
        assertEquals(6, PathTruncationUtil.getTruncatedLength(6, 8)); // Short path
        assertEquals(10, PathTruncationUtil.getTruncatedLength(10, 64)); // Medium path
        assertEquals(64, PathTruncationUtil.getTruncatedLength(100, 64)); // Long path
        assertEquals(32, PathTruncationUtil.getTruncatedLength(100, 32)); // Long path with smaller limit
        assertEquals(3, PathTruncationUtil.getTruncatedLength(100, 3)); // Very small limit
    }

    @Test
    public void testIsValidTruncationLength() {
        // Test length validation
        assertTrue(PathTruncationUtil.isValidTruncationLength(8, 8, 128)); // Min
        assertTrue(PathTruncationUtil.isValidTruncationLength(64, 8, 128)); // Default
        assertTrue(PathTruncationUtil.isValidTruncationLength(128, 8, 128)); // Max

        assertFalse(PathTruncationUtil.isValidTruncationLength(7, 8, 128)); // Too small
        assertFalse(PathTruncationUtil.isValidTruncationLength(129, 8, 128)); // Too large
    }

    @Test
    public void testGetRecommendedLength() {
        // Test recommended length calculation
        assertEquals(32, PathTruncationUtil.getRecommendedLength(null)); // Null
        assertEquals(32, PathTruncationUtil.getRecommendedLength("")); // Empty
        assertEquals(32, PathTruncationUtil.getRecommendedLength("   ")); // Whitespace

        assertEquals(4, PathTruncationUtil.getRecommendedLength("/api")); // Short
        assertEquals(13, PathTruncationUtil.getRecommendedLength("/api/v1/users")); // Medium
        assertEquals(21, PathTruncationUtil.getRecommendedLength("/api/v1/users/profile")); // Medium-long
        assertEquals(64, PathTruncationUtil.getRecommendedLength("/api/v1/users/123456789012345678901234567890123456789012345678901234567890")); // Long
    }

    @Test
    public void testTruncatePathBoundaryConditions() {
        // Test boundary conditions
        String exact8Chars = "/api/abc"; // exactly 8 chars
        assertEquals(exact8Chars, PathTruncationUtil.truncatePath(exact8Chars, 8)); // 8 chars, no truncation

        String exact9Chars = "/api/users";
        assertEquals(8, PathTruncationUtil.truncatePath(exact9Chars, 8).length());
        assertEquals("/api/...", PathTruncationUtil.truncatePath(exact9Chars, 8));

        // Use the same 64-character path
        StringBuilder pathBuilder2 = new StringBuilder();
        pathBuilder2.append("/api/v1/users/");
        for (int i = 0; i < 41; i++) { // 41 chars to make total 64
            pathBuilder2.append("a");
        }
        String exact64Chars = pathBuilder2.toString();
        assertEquals(exact64Chars, PathTruncationUtil.truncatePath(exact64Chars, 64));

        String exact65Chars = "/api/v1/users/12345678901234567890123456789012345678901234567890123456789012345";
        assertEquals(64, PathTruncationUtil.truncatePath(exact65Chars, 64).length());
        assertTrue(PathTruncationUtil.truncatePath(exact65Chars, 64).endsWith("..."));
    }

    @Test
    public void testTruncatePathPerformance() {
        // Test with very long paths to ensure performance is acceptable
        StringBuilder longPathBuilder = new StringBuilder();
        longPathBuilder.append("/api/v1/");
        for (int i = 0; i < 1000; i++) {
            longPathBuilder.append("very-long-segment-").append(i).append("/");
        }
        String veryLongPath = longPathBuilder.toString();

        // Should not crash and should return truncated result
        String truncated = PathTruncationUtil.truncatePath(veryLongPath, 64);
        assertNotNull(truncated);
        assertEquals(64, truncated.length());
        assertTrue(truncated.endsWith("..."));
    }
}