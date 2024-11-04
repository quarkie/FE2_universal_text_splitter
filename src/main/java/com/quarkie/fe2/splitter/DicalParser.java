package com.quarkie.fe2.splitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DicalParser {
  public static Map<String, String> parseAlarmText(String text) {
    Map<String, String> resultMap = new HashMap<>();
    // TODO
    resultMap.put("DICAL", "DICAL RED Alarm");
    resultMap.values().removeIf(Objects::isNull);
    return resultMap;
  }
}
