package com.quarkie.fe2.splitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.alamos.fe2.external.enums.EAlarmDataEntries;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class LardisParserTest {

  @Test
  void testParseAlarmTextIlsFormat() {
    String inputMessage =
        "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine||Gemeinde: Rosenberg||Ortsteil: Rosenberg||Strasse: Mühlweg 22||Objekt: ||$GPSN49,01693391E10,04199505";

    Map<String, String> inputMap = new HashMap<>();
    inputMap.put("origin", inputMessage);

    // Parse the alarm text
    LardisParser.parseAlarmText(inputMap);

    // Expected values
    assertEquals(
        "BRAND Kleinbrand Hecke / Wiese Personen Keine",
        inputMap.get(EAlarmDataEntries.TEXT.getKey()));
    assertEquals("Rosenberg", inputMap.get(EAlarmDataEntries.CITY.getKey()));
    assertEquals("Rosenberg", inputMap.get(EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey()));
    assertEquals("Mühlweg 22", inputMap.get(EAlarmDataEntries.STREET.getKey()));
    assertNull(inputMap.get(EAlarmDataEntries.BUILDING_NAME.getKey())); // Empty in this case
    assertEquals("49,01693391", inputMap.get(EAlarmDataEntries.LAT.getKey()));
    assertEquals("10,04199505", inputMap.get(EAlarmDataEntries.LNG.getKey()));
  }

  @Test
  void testParseAlarmTextWithMissingFields() {
    String inputMessage =
        "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine||Gemeinde: Rosenberg||Ortsteil: ||Strasse: Mühlweg 22||Objekt: ||$GPSN49,01693391E10,04199505";

    Map<String, String> inputMap = new HashMap<>();
    inputMap.put("origin", inputMessage);

    // Parse the alarm text
    LardisParser.parseAlarmText(inputMap);

    // Expected values
    assertEquals(
        "BRAND Kleinbrand Hecke / Wiese Personen Keine",
        inputMap.get(EAlarmDataEntries.TEXT.getKey()));
    assertEquals("Rosenberg", inputMap.get(EAlarmDataEntries.CITY.getKey()));
    assertEquals(
        "", inputMap.get(EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey())); // Ortsteil is missing
    assertEquals("Mühlweg 22", inputMap.get(EAlarmDataEntries.STREET.getKey()));
    assertNull(inputMap.get(EAlarmDataEntries.BUILDING_NAME.getKey())); // Empty in this case
    assertEquals("49,01693391", inputMap.get(EAlarmDataEntries.LAT.getKey()));
    assertEquals("10,04199505", inputMap.get(EAlarmDataEntries.LNG.getKey()));
  }

  @Test
  void testParseAlarmTextWithUnknownFormat() {
    String inputMessage = "Unknown format with no  or  delimiter";
    Map<String, String> inputMap = new HashMap<>();
    inputMap.put("origin", inputMessage);
    // Parse the alarm text
    LardisParser.parseAlarmText(inputMap);

    // Assert that the error message is returned
    assertEquals("Unknown format", inputMap.get("ERROR"));
  }

  @Test
  void testParseAlarmTextVariant2() {
    String inputMessage = "Some header text # Additional details about the alarm";

    Map<String, String> inputMap = new HashMap<>();
    inputMap.put("origin", inputMessage);

    // Parse the alarm text
    LardisParser.parseAlarmText(inputMap);

    // Assert that both header and details are correctly parsed
    assertEquals("Some header text", inputMap.get("Header"));
    assertEquals("Additional details about the alarm", inputMap.get("Details"));
  }

  @Test
  void testParseAlarmTextMissingGPS() {
    String inputMessage =
        "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine||Gemeinde: Rosenberg||Ortsteil: Rosenberg||Strasse: Mühlweg 22||Objekt: ||$GPSN";

    Map<String, String> inputMap = new HashMap<>();
    inputMap.put("origin", inputMessage);

    // Parse the alarm text
    LardisParser.parseAlarmText(inputMap);

    // Assert that the GPS values are missing
    assertNull(inputMap.get(EAlarmDataEntries.LAT.getKey()));
    assertNull(inputMap.get(EAlarmDataEntries.LNG.getKey()));
  }

  @Test
  public void testExtractIlsAlertParams_withValidMessage() {
    String message =
        "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine"
            + "||Gemeinde: Rosenberg||Ortsteil: Rosenberg||Strasse: Mühlweg 22"
            + "||Objekt: ||$GPSN49,01693391E10,04199505";
    Map<String, String> resultMap = new HashMap<>();

    LardisParser.extractIlsAlertParams(message, resultMap);

    assertEquals(
        "BRAND Kleinbrand Hecke / Wiese Personen Keine",
        resultMap.get(EAlarmDataEntries.TEXT.getKey()));
    assertEquals("Rosenberg", resultMap.get(EAlarmDataEntries.CITY.getKey()));
    assertEquals("Rosenberg", resultMap.get(EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey()));
    assertEquals("Mühlweg 22", resultMap.get(EAlarmDataEntries.STREET.getKey()));
    assertEquals("BR5", resultMap.get(EAlarmDataEntries.KEYWORD.getKey()));
    assertNull(
        resultMap.get(
            EAlarmDataEntries.BUILDING_NAME.getKey())); // should be null since the value is empty
    assertEquals("49,01693391", resultMap.get(EAlarmDataEntries.LAT.getKey()));
    assertEquals("10,04199505", resultMap.get(EAlarmDataEntries.LNG.getKey()));
  }
}
