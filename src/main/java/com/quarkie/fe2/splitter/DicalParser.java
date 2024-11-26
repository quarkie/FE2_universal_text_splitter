package com.quarkie.fe2.splitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DicalParser {

  private static final Logger logger = LoggerFactory.getLogger(DicalParser.class);

  public static Map<String, String> parseAlarmText(Map<String, String> map) {
    logger.info("TODO Parsing DICAL alarm text with input map: {}", map);

    Map<String, String> resultMap = new HashMap<>();
    // TODO
    resultMap.put("ALERT_TYPE", "DICAL_RED_ALERT");
    resultMap.values().removeIf(Objects::isNull);
    return resultMap;
  }
}
