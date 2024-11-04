package com.quarkie.fe2.splitter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class LardisParserTest {

  @Test
  public void testParseLardisText() {
    String lardisText =
        "SDS LARDIS Alarm\n"
            + "Location: Emergency Zone\n"
            + "Details: Urgent assistance required.";

    Map<String, String> result = LardisParser.parseAlarmText(lardisText);
    assertEquals("LARDIS Alarm", result.get("LARDIS"));
    // Add more assertions as needed for additional parameters
  }
}
