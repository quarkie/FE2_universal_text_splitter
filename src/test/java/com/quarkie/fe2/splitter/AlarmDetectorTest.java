package com.quarkie.fe2.splitter;

import static org.junit.jupiter.api.Assertions.*;

import de.alamos.fe2.external.enums.EAlarmDataEntries;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AlarmDetectorTest {

  @Test
  public void testEmailDetection() {
    String emailText = "Einsatzanlass: Feuer\n" + "Stichwort F BR2 Türe öffnen/verschliessen\n";

    Map<String, String> result = new AlarmDetector().extract(emailText);
    assertEquals("BR2", result.get(EAlarmDataEntries.KEYWORD.getKey()));
  }

  @Test
  public void testDicalDetection() {
    String dicalText = "DICAL RED#TODO\n" + "Location: Sample Street 10\n" + "Details: Fire";

    Map<String, String> result = new AlarmDetector().extract(dicalText);
    assertEquals("DICAL RED Alarm", result.get("DICAL"));
  }

  @Test
  public void testLardisDetection() {
    String lardisText = "SDS LARDIS#TODO\n" + "Details: Emergency situation";

    Map<String, String> result = new AlarmDetector().extract(lardisText);
    assertEquals("LARDIS Alarm", result.get("LARDIS"));
  }
}
