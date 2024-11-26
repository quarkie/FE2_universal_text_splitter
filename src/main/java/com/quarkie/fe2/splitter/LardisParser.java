package com.quarkie.fe2.splitter;

import de.alamos.fe2.external.enums.EAlarmDataEntries;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LardisParser {
  private static final Logger logger = LoggerFactory.getLogger(LardisParser.class);

  public static final String GEMEINDE_PREFIX = "Gemeinde:";
  public static final String ORTSTEIL_PREFIX = "Ortsteil:";
  public static final String STRASSE_PREFIX = "Strasse:";
  public static final String OBJEKT_PREFIX = "Objekt:";
  public static final String GPS_PREFIX = "$GPS";
  private static final Pattern GPS_PATTERN =
      Pattern.compile("\\$GPSN(-?\\d{1,3},\\d+)E(-?\\d{1,3},\\d+)");
  private static final Map<String, Pattern> FIELD_PATTERNS = new HashMap<>();

  static {
    FIELD_PATTERNS.put("Gemeinde:", Pattern.compile("Gemeinde:\\s*([^|]+)"));
    FIELD_PATTERNS.put("Ortsteil:", Pattern.compile("Ortsteil:\\s*([^|]+)"));
    FIELD_PATTERNS.put("Strasse:", Pattern.compile("Strasse:\\s*([^|]+)"));
    FIELD_PATTERNS.put("Objekt:", Pattern.compile("Objekt:\\s*([^|]+)"));
  }

  public static void parseAlarmText(Map<String, String> resultMap) {
    String text = resultMap.get("origin");
    if (text.contains("ILS") && AaoKeyword.contains(text)) {
      logger.info("Parsing ILS alert message...");
      resultMap.put("ALERT_TYPE", "LARDIS_ALERT");
      extractIlsAlertParams(text, resultMap);
    } else if (text.contains("#")) {
      logger.info("Parsing LARDIS status message...");
      resultMap.put("ALERT_TYPE", "LARDIS_STATUS");
      // TODO
    } else {
      logger.error("Unknown format encountered.");
      resultMap.put("ERROR", "Unknown format");
    }
  }

  public static void extractIlsAlertParams(String message, Map<String, String> result) {
    String[] parts = message.split("\\|\\|");

    // Extract the AAO keyword and text
    String firstPart = parts[0].trim();
    Optional<AaoKeyword> keywordOpt = AaoKeyword.extractFirst(firstPart);
    if (keywordOpt.isPresent()) {
      String keyword = keywordOpt.get().name();
      result.put(EAlarmDataEntries.KEYWORD.getKey(), keyword);
      int textStartIndex = firstPart.indexOf(keyword) + keyword.length() + 2;
      if (textStartIndex < firstPart.length()) {
        String text = firstPart.substring(textStartIndex).trim();
        result.put(EAlarmDataEntries.TEXT.getKey(), text);
      }
      logger.debug("Extracted keyword: {}", keyword);
    }

    // Extract values based on field prefixes using constants
    for (int i = 1; i < parts.length; i++) {
      String part = parts[i].trim();
      if (part.startsWith(GEMEINDE_PREFIX)) {
        result.put(
            EAlarmDataEntries.CITY.getKey(), part.substring(GEMEINDE_PREFIX.length()).trim());
        logger.debug("Extracted Gemeinde: {}", part.substring(GEMEINDE_PREFIX.length()).trim());
      } else if (part.startsWith(ORTSTEIL_PREFIX)) {
        result.put(
            EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey(),
            part.substring(ORTSTEIL_PREFIX.length()).trim());
        logger.debug("Extracted Ortsteil: {}", part.substring(ORTSTEIL_PREFIX.length()).trim());
      } else if (part.startsWith(STRASSE_PREFIX)) {
        result.put(
            EAlarmDataEntries.STREET.getKey(), part.substring(STRASSE_PREFIX.length()).trim());
        logger.debug("Extracted Strasse: {}", part.substring(STRASSE_PREFIX.length()).trim());
      } else if (part.startsWith(OBJEKT_PREFIX)) {
        String buildingName = part.substring(OBJEKT_PREFIX.length()).trim();
        if (!buildingName.isEmpty()) {
          result.put(EAlarmDataEntries.BUILDING_NAME.getKey(), buildingName);
          logger.debug("Extracted Objekt: {}", buildingName);
        }
      } else if (part.startsWith(GPS_PREFIX)) {
        Matcher gpsMatcher = GPS_PATTERN.matcher(part);
        if (gpsMatcher.find()) {
          result.put(EAlarmDataEntries.LAT.getKey(), gpsMatcher.group(1)); // Latitude
          result.put(EAlarmDataEntries.LNG.getKey(), gpsMatcher.group(2)); // Longitude
          logger.debug(
              "Extracted GPS coordinates: Latitude: {}, Longitude: {}",
              gpsMatcher.group(1),
              gpsMatcher.group(2));
        }
      }
    }
  }
}
