package com.quarkie.fe2.splitter;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;


import static org.junit.jupiter.api.Assertions.*;

public class UtilityTest {

    @Test
    public void testExtractValue() {
        String text = """
                Incident Reason Fire
                Street Main Street
                House 15
                """;

        String result = Utility.extractValue(text, "Street");
        assertEquals("Main Street", result);

        result = Utility.extractValue(text, "Incident Reason");
        assertEquals("Fire", result);

        result = Utility.extractValue(text, "Nonexistent Key");
        assertNull(result);
    }

    @Test
    void testExtractPattern() {
        // Pattern to match a word after "Example:" (e.g., "Example: Word")
        Pattern pattern = Pattern.compile("Example:\\s*(\\w+)");

        // Test with a matching pattern
        String text = "This is an Example: Word in a sentence.";
        String expected = "Word";
        assertEquals(expected, Utility.extractPattern(text, pattern));

        // Test with multiple matches (should return the first match)
        text = "Example: First Match Example: Second Match";
        expected = "First";
        assertEquals(expected, Utility.extractPattern(text, pattern));

        // Test with no match
        text = "No example keyword here.";
        assertNull(Utility.extractPattern(text, pattern));

        // Test with only whitespace
        text = "    ";
        assertNull(Utility.extractPattern(text, pattern));

        // Test with null input
        text = null;
        assertNull(Utility.extractPattern(text, pattern));

        // Test with an empty string
        text = "";
        assertNull(Utility.extractPattern(text, pattern));

        // Test with a pattern that has a capturing group for digits (e.g., "ID: 12345")
        pattern = Pattern.compile("ID:\\s*(\\d+)");
        text = "User ID: 12345";
        expected = "12345";
        assertEquals(expected, Utility.extractPattern(text, pattern));

        // Test with a pattern that includes special characters
        pattern = Pattern.compile("WGS\\s+([NS]\\d+\\.\\d+)");
        text = "WGS N49.00187 E10.05223";
        expected = "N49.00187";
        assertEquals(expected, Utility.extractPattern(text, pattern));
    }


    @Test
    void testReplaceWhitespaceWithUnderscore() {
        // Test with regular whitespace
        String input = "FL Rosenberg 1/19-1";
        String expected = "FL_Rosenberg_1/19-1";
        assertEquals(expected, Utility.replaceWhitespaceWithUnderscore(input));

        // Test with multiple whitespace characters
        input = "FL   Rosenberg\t1/19-1";
        expected = "FL___Rosenberg_1/19-1";
        assertEquals(expected, Utility.replaceWhitespaceWithUnderscore(input));

        // Test with no whitespace
        input = "FLRosenberg1/19-1";
        expected = "FLRosenberg1/19-1";
        assertEquals(expected, Utility.replaceWhitespaceWithUnderscore(input));

        // Test with only whitespace
        input = "   ";
        expected = "___";
        assertEquals(expected, Utility.replaceWhitespaceWithUnderscore(input));

        // Test with null input
        input = null;
        assertNull(Utility.replaceWhitespaceWithUnderscore(input));

        // Test with empty string
        input = "";
        expected = "";
        assertEquals(expected, Utility.replaceWhitespaceWithUnderscore(input));
    }

}