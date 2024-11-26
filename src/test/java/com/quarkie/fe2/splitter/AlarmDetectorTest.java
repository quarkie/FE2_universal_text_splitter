package com.quarkie.fe2.splitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.alamos.fe2.external.ExtractorObject;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AlarmDetectorTest {

  @Test
  public void testEmailDetection() {
    Map<String, String> testData = new HashMap<>();
    testData.put(
        "origin", "Einsatzanlass: Feuer\n" + "Stichwort F BR2 Türe öffnen/verschliessen\n");
    testData.put("alarmType", "MAIL");
    ExtractorObject result = new AlarmDetector().extractFromMap(testData);
    assertEquals("ILS_MAIL_ALERT", result.getData().get("ALERT_TYPE"));
  }

  @Test
  public void testEmailDetectionNoAlarmtype() {
    Map<String, String> testData = new HashMap<>();
    testData.put(
        "origin", "Einsatzanlass: Feuer\n" + "Stichwort F BR2 Türe öffnen/verschliessen\n");
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ExtractorObject result = new AlarmDetector().extractFromMap(testData);
            });
    // Assert that the exception message matches the expected message
    assertEquals("Unknown alarm source", exception.getMessage());
  }

  @Test
  public void testDicalDetection() {
    Map<String, String> testData = new HashMap<>();
    testData.put("alarmType", "TODO#DICAL");
    testData.put("ALERT_TYPE", "DICAL_RED_ALERT");
    testData.put("origin", "DICAL RED#TODO\n" + "Location: Sample Street 10\n" + "Details: Fire");
    ExtractorObject result = new AlarmDetector().extractFromMap(testData);
    assertEquals("DICAL_RED_ALERT", result.getData().get("ALERT_TYPE"));
  }

  @Test
  public void testLardisDetection() {

    Map<String, String> testData = new HashMap<>();
    testData.put("ALERT_TYPE", "LARDIS_ALERT");
    testData.put("alarmType", "ALARM");
    testData.put(
        "origin",
        "BW   AA      ILS        BR5 - BRAND Kleinbrand Hecke / Wiese Personen Keine"
            + "||Gemeinde: Rosenberg||Ortsteil: Rosenberg||Strasse: Mühlweg 22"
            + "||Objekt: ||$GPSN49,01693391E10,04199505");

    ExtractorObject result = new AlarmDetector().extractFromMap(testData);
    assertEquals("LARDIS_ALERT", result.getData().get("ALERT_TYPE"));
  }

  // Helper method to execute the test case and check the result
  private void runTest(Map<String, String> map, ParserType expectedType, String expectedOrigin) {
    ParserType result = AlarmDetector.detectSource(map);
    assertEquals(expectedType, result, "The ParserType should match the expected test case.");
    assertEquals(
        expectedOrigin,
        map.get(AlarmDetector.ORIGIN),
        "The origin value should be correctly cleaned up.");
  }

  @Test
  public void testILS_AA_EMAIL_TestCase() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "This is a test #ils_aa_email# message");
    map.put("alarmType", "MAIL");

    // Test case with ILS_AA_EMAIL
    runTest(map, ParserType.ILS_AA_EMAIL, "This is a test  message");
  }

  @Test
  public void testDICAL_POCSAG_ALERT_TestCase() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "This is a test #dical_pocsac_alert# message");
    map.put("alarmType", "TODO#DICAL");

    // Test case with DICAL_POCSAG_ALERT
    runTest(map, ParserType.DICAL_POCSAG_ALERT, "This is a test  message");
  }

  @Test
  public void testLARDIS_TETRA_SDS_TestCase() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "This is a test #lardis_tetra_sds# message");
    map.put("alarmType", "ALARM");

    // Test case with LARDIS_TETRA_SDS
    runTest(map, ParserType.LARDIS_TETRA_SDS, "This is a test  message");
  }

  @Test
  public void testRealData_ILS_AA_EMAIL() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "Real data with MAIL alarm");
    map.put("alarmType", "MAIL");

    // Normal data, should process correctly
    runTest(map, ParserType.ILS_AA_EMAIL, "Real data with MAIL alarm");
  }

  @Test
  public void testRealData_DICAL_POCSAG_ALERT() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "Real data with DICAL alarm");
    map.put("alarmType", "TODO#DICAL");

    // Normal data for DICAL
    runTest(map, ParserType.DICAL_POCSAG_ALERT, "Real data with DICAL alarm");
  }

  @Test
  public void testRealData_LARDIS_TETRA_SDS() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "Real data with ALARM type");
    map.put("alarmType", "ALARM");

    // Normal data for LARDIS
    runTest(map, ParserType.LARDIS_TETRA_SDS, "Real data with ALARM type");
  }

  @Test
  public void testNoMatch() {
    Map<String, String> map = new HashMap<>();
    map.put(AlarmDetector.ORIGIN, "Some random data");
    map.put("alarmType", "UNKNOWN");

    // No match for test cases or alarmType
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ParserType result = AlarmDetector.detectSource(map);
            });
    // Assert that the exception message matches the expected message
    assertEquals("Unknown alarm source", exception.getMessage());
  }
}
