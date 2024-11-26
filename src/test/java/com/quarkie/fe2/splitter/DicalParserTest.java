package com.quarkie.fe2.splitter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DicalParserTest {

  @Test
  public void testParseDicalText() {

    Map<String, String> testData = new HashMap<>();
    testData.put(
        "origin",
        "DICAL RED Alarm\n" + "Location: Sample Street 10\n" + "Details: Fire at the location.");

    Map<String, String> result = DicalParser.parseAlarmText(testData);
    assertEquals("DICAL_RED_ALERT", result.get("ALERT_TYPE"));
    // Add more assertions as needed for additional parameters
  }
}
