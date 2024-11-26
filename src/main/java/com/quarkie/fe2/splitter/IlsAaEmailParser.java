package com.quarkie.fe2.splitter;

import de.alamos.fe2.external.enums.EAlarmDataEntries;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IlsAaEmailParser {

  private static final Logger logger = LoggerFactory.getLogger(IlsAaEmailParser.class);

  private static final Pattern HOUSE_PATTERN = Pattern.compile("Straße\\s+.+?\\s+(\\d+)");
  private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("\\[(\\d{5})\\]");
  private static final Pattern CITY_PATTERN = Pattern.compile("Ort\\s+([\\w\\s]+)");
  private static final Pattern UNITS_PATTERN =
      Pattern.compile(
          "(FF\\s+.+?)\\n(\\d{2}:\\d{2}:\\d{2})",
          Pattern.DOTALL); // Matches units followed by a timestamp
  private static final Pattern VEHICLES_PATTERN =
      Pattern.compile("FL\\s+[\\w\\s]+\\s+\\d+/\\d+-\\d+");
  private static final Pattern COORDINATES_PATTERN =
      Pattern.compile("WGS\\s+[NS]?(\\d+\\.\\d+)\\s+[EW]?(\\d+\\.\\d+)");
  private static final Pattern KEYWORD_PATTERN = Pattern.compile("Stichwort F (\\S+)");

  public static Map<String, String> parseAlarmText(Map<String, String> map) {
    // Log incoming map for debugging purposes
    logger.info("Starting to parse ILS alarm text. Input map: {}", map);

    map.put("ALERT_TYPE", "ILS_MAIL_ALERT");
    String text = map.get("origin");

    // General parameters - logging each key extraction
    map.put(EAlarmDataEntries.TEXT.getKey(), Utility.extractValue(text, "Einsatzanlass"));
    logger.debug("Extracted TEXT: {}", map.get(EAlarmDataEntries.TEXT.getKey()));

    map.put(EAlarmDataEntries.STREET.getKey(), Utility.extractValue(text, "Straße"));
    logger.debug("Extracted STREET: {}", map.get(EAlarmDataEntries.STREET.getKey()));

    map.put(EAlarmDataEntries.HOUSE.getKey(), Utility.extractPattern(text, HOUSE_PATTERN));
    logger.debug("Extracted HOUSE: {}", map.get(EAlarmDataEntries.HOUSE.getKey()));

    map.put(EAlarmDataEntries.CITY.getKey(), Utility.extractPattern(text, CITY_PATTERN));
    logger.debug("Extracted CITY: {}", map.get(EAlarmDataEntries.CITY.getKey()));

    map.put(
        EAlarmDataEntries.POSTALCODE.getKey(), Utility.extractPattern(text, POSTAL_CODE_PATTERN));
    logger.debug("Extracted POSTALCODE: {}", map.get(EAlarmDataEntries.POSTALCODE.getKey()));

    map.put(EAlarmDataEntries.BUILDING_NAME.getKey(), Utility.extractValue(text, "Ortszusatz"));
    logger.debug("Extracted BUILDING_NAME: {}", map.get(EAlarmDataEntries.BUILDING_NAME.getKey()));

    map.put(EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey(), Utility.extractValue(text, "Ortsteil"));
    logger.debug(
        "Extracted LOCATION_ADDITIONAL: {}",
        map.get(EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey()));

    map.put(EAlarmDataEntries.CALLER.getKey(), Utility.extractValue(text, "Meldender"));
    logger.debug("Extracted CALLER: {}", map.get(EAlarmDataEntries.CALLER.getKey()));

    map.put(EAlarmDataEntries.CALLER_CONTACT.getKey(), Utility.extractValue(text, "Rückruf"));
    logger.debug(
        "Extracted CALLER_CONTACT: {}", map.get(EAlarmDataEntries.CALLER_CONTACT.getKey()));

    map.put("caller_remark", Utility.extractValue(text, "Bemerkung"));
    logger.debug("Extracted caller_remark: {}", map.get("caller_remark"));

    map.put(EAlarmDataEntries.EXTERNAL_ID.getKey(), Utility.extractValue(text, "EinsatzNrn"));
    logger.debug("Extracted EXTERNAL_ID: {}", map.get(EAlarmDataEntries.EXTERNAL_ID.getKey()));

    map.put(EAlarmDataEntries.KEYWORD.getKey(), Utility.extractPattern(text, KEYWORD_PATTERN));
    logger.debug("Extracted KEYWORD: {}", map.get(EAlarmDataEntries.KEYWORD.getKey()));

    map.put(EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey(), Utility.extractValue(text, "Meldebild"));
    logger.debug(
        "Extracted KEYWORD_ADDITIONAL: {}", map.get(EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey()));

    map.put(
        EAlarmDataEntries.KEYWORD_DESCRIPTION.getKey(), Utility.extractValue(text, "Stichwort"));
    logger.debug(
        "Extracted KEYWORD_DESCRIPTION: {}",
        map.get(EAlarmDataEntries.KEYWORD_DESCRIPTION.getKey()));

    map.put(EAlarmDataEntries.KEYWORD_IDENTIFICATION.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug(
        "Extracted KEYWORD_IDENTIFICATION: {}",
        map.get(EAlarmDataEntries.KEYWORD_IDENTIFICATION.getKey()));

    map.put(EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey(), Utility.extractValue(text, "Zusatz"));
    logger.debug(
        "Extracted KEYWORD_ADDITIONAL: {}", map.get(EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey()));

    map.put(EAlarmDataEntries.KEYWORD_CATEGORY.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug(
        "Extracted KEYWORD_CATEGORY: {}", map.get(EAlarmDataEntries.KEYWORD_CATEGORY.getKey()));

    map.put(EAlarmDataEntries.EINSATZMITTEL.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug("Extracted EINSATZMITTEL: {}", map.get(EAlarmDataEntries.EINSATZMITTEL.getKey()));

    map.put(EAlarmDataEntries.CITY_ABBR.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug("Extracted CITY_ABBR: {}", map.get(EAlarmDataEntries.CITY_ABBR.getKey()));

    map.put(EAlarmDataEntries.ABEK.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug("Extracted ABEK: {}", map.get(EAlarmDataEntries.ABEK.getKey()));

    map.put(EAlarmDataEntries.GK_X.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug("Extracted GK_X: {}", map.get(EAlarmDataEntries.GK_X.getKey()));

    map.put(EAlarmDataEntries.GK_Y.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug("Extracted GK_Y: {}", map.get(EAlarmDataEntries.GK_Y.getKey()));

    map.put(EAlarmDataEntries.DESTINATION.getKey(), Utility.extractValue(text, "TODO"));
    logger.debug("Extracted DESTINATION: {}", map.get(EAlarmDataEntries.DESTINATION.getKey()));

    // Extract coordinates and assign to LAT and LNG
    String[] coordinates = extractCoordinates(text);
    if (coordinates != null) {
      map.put(EAlarmDataEntries.LAT.getKey(), coordinates[0]);
      map.put(EAlarmDataEntries.LNG.getKey(), coordinates[1]);
      logger.debug("Extracted coordinates: LAT = {}, LNG = {}", coordinates[0], coordinates[1]);
    }

    // Extract units
    List<String> unitsList = extractUnits(text);
    map.put(EAlarmDataEntries.EINSATZMITTEL.getKey(), String.join("|", unitsList));
    map.put("units", map.get(EAlarmDataEntries.EINSATZMITTEL.getKey()));
    logger.debug("Extracted units: {}", unitsList);

    addIncrementedParams(map, unitsList, "unit");
    addValueParams(map, unitsList, "");

    // Extract vehicles
    List<String> vehiclesList = extractVehicles(text);
    map.put(EAlarmDataEntries.VEHICLES.getKey(), String.join("|", vehiclesList));
    logger.debug("Extracted vehicles: {}", vehiclesList);

    addIncrementedParams(map, vehiclesList, "vehicle");
    addValueParams(map, vehiclesList, "");

    map.values().removeIf(Objects::isNull);

    // Log the final cleaned result
    logger.info("Final parsed and cleaned map: {}", map);
    return map;
  }

  private static List<String> extractUnits(String text) {
    logger.debug("Extracting units from the text...");
    Matcher matcher = UNITS_PATTERN.matcher(text);
    List<String> unitsList = new ArrayList<>();
    while (matcher.find()) {
      unitsList.add(
          Utility.removeNewlines(matcher.group(0))
              .split("\\s+\\d{2}:\\d{2}:\\d{2}|--:--:--")[0]
              .trim());
    }
    logger.debug("Extracted units: {}", unitsList);
    return unitsList;
  }

  private static List<String> extractVehicles(String text) {
    logger.debug("Extracting vehicles from the text...");
    Matcher matcher = VEHICLES_PATTERN.matcher(text);
    List<String> vehiclesList = new ArrayList<>();
    while (matcher.find()) {
      vehiclesList.add(
          Utility.removeNewlines(matcher.group(0))
              .split("\\s+\\d{2}:\\d{2}:\\d{2}|--:--:--")[0]
              .trim());
    }
    logger.debug("Extracted vehicles: {}", vehiclesList);
    return vehiclesList;
  }

  private static String[] extractCoordinates(String text) {
    logger.debug("Extracting coordinates from the text...");
    Matcher matcher = COORDINATES_PATTERN.matcher(text);
    if (matcher.find()) {
      // Extract latitude and longitude without direction characters
      String latitude = matcher.group(1);
      String longitude = matcher.group(2);
      logger.debug("Extracted coordinates: Latitude: {}, Longitude: {}", latitude, longitude);
      return new String[] {latitude, longitude};
    }
    logger.debug("No coordinates found in the text.");
    return null; // No coordinates found
  }

  private static void addIncrementedParams(
      Map<String, String> map, List<String> items, String prefix) {
    logger.debug("Adding incremented parameters...");
    for (int i = 0; i < items.size(); i++) {
      map.put(prefix + "_" + (i + 1), items.get(i));
    }
  }

  private static void addValueParams(Map<String, String> map, List<String> items, String prefix) {
    logger.debug("Adding value parameters...");
    for (String item : items) {
      map.put(Utility.replaceWhitespaceWithUnderscore(item), item);
    }
  }
}
