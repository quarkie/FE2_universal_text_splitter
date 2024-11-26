package com.quarkie.fe2.splitter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AaoKeywordTest {

  @Test
  void testExtractFirstKeywordPresent() {
    String message = "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine";

    // Test that the BR5 keyword is correctly extracted
    assertTrue(AaoKeyword.extractFirst(message).isPresent());
    assertEquals(AaoKeyword.BR5, AaoKeyword.extractFirst(message).get());
  }

  @Test
  void testExtractFirstKeywordNotPresent() {
    String message = "BW   AA      ILS        Some other message without AA keyword";

    // Test that no keyword is extracted if none is present
    assertFalse(AaoKeyword.extractFirst(message).isPresent());
  }

  @Test
  void testKeywordExtractionWithMultipleKeywords() {
    String message =
        "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine and SE1 is also present";

    // Test that it extracts the first keyword (BR5 in this case)
    assertTrue(AaoKeyword.extractFirst(message).isPresent());
    assertEquals(AaoKeyword.BR5, AaoKeyword.extractFirst(message).get());
  }
}
