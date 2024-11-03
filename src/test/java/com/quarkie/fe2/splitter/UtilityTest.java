package com.quarkie.fe2.splitter;
import org.junit.jupiter.api.Test;


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
}