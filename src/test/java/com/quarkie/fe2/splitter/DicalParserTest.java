package com.quarkie.fe2.splitter;


import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DicalParserTest {

    @Test
    public void testParseDicalText() {
        String dicalText = "DICAL RED Alarm\n" +
                "Location: Sample Street 10\n" +
                "Details: Fire at the location.";

        Map<String, String> result = DicalParser.parseAlarmText(dicalText);
        assertEquals("DICAL RED Alarm", result.get("DICAL"));
        // Add more assertions as needed for additional parameters
    }
}