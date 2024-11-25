package com.quarkie.fe2.splitter;

import de.alamos.fe2.external.enums.EAlarmDataEntries;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IlsAaEmailParser {

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

  public static Map<String, String> parseAlarmText(String text) {
    Map<String, String> resultMap = new HashMap<>();
    // General parameters
    resultMap.put(EAlarmDataEntries.TEXT.getKey(), Utility.extractValue(text, "Einsatzanlass"));
    resultMap.put(EAlarmDataEntries.STREET.getKey(), Utility.extractValue(text, "Straße"));
    resultMap.put(EAlarmDataEntries.HOUSE.getKey(), Utility.extractPattern(text, HOUSE_PATTERN));
    resultMap.put(EAlarmDataEntries.CITY.getKey(), Utility.extractPattern(text, CITY_PATTERN));
    resultMap.put(
        EAlarmDataEntries.POSTALCODE.getKey(), Utility.extractPattern(text, POSTAL_CODE_PATTERN));
    resultMap.put(
        EAlarmDataEntries.BUILDING_NAME.getKey(), Utility.extractValue(text, "Ortszusatz"));
    resultMap.put(
        EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey(), Utility.extractValue(text, "Ortsteil"));
    resultMap.put(EAlarmDataEntries.CALLER.getKey(), Utility.extractValue(text, "Meldender"));
    resultMap.put(EAlarmDataEntries.CALLER_CONTACT.getKey(), Utility.extractValue(text, "Rückruf"));
    resultMap.put("caller_remark", Utility.extractValue(text, "Bemerkung"));
    resultMap.put(EAlarmDataEntries.EXTERNAL_ID.getKey(), Utility.extractValue(text, "EinsatzNrn"));
    resultMap.put(
        EAlarmDataEntries.KEYWORD.getKey(), Utility.extractPattern(text, KEYWORD_PATTERN));
    resultMap.put(
        EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey(), Utility.extractValue(text, "Meldebild"));
    resultMap.put(
        EAlarmDataEntries.KEYWORD_DESCRIPTION.getKey(), Utility.extractValue(text, "Stichwort"));
    resultMap.put(
        EAlarmDataEntries.KEYWORD_IDENTIFICATION.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(
        EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey(), Utility.extractValue(text, "Zusatz"));
    resultMap.put(EAlarmDataEntries.KEYWORD_CATEGORY.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(EAlarmDataEntries.EINSATZMITTEL.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(EAlarmDataEntries.CITY_ABBR.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(EAlarmDataEntries.ABEK.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(EAlarmDataEntries.GK_X.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(EAlarmDataEntries.GK_Y.getKey(), Utility.extractValue(text, "TODO"));
    resultMap.put(EAlarmDataEntries.DESTINATION.getKey(), Utility.extractValue(text, "TODO"));

    // Extract coordinates and assign to LAT and LNG
    String[] coordinates = extractCoordinates(text);
    if (coordinates != null) {
      resultMap.put(EAlarmDataEntries.LAT.getKey(), coordinates[0]);
      resultMap.put(EAlarmDataEntries.LNG.getKey(), coordinates[1]);
    }

    // Extract units
    List<String> unitsList = extractUnits(text);
    resultMap.put(EAlarmDataEntries.EINSATZMITTEL.getKey(), String.join("|", unitsList));
    resultMap.put("units", resultMap.get(EAlarmDataEntries.EINSATZMITTEL.getKey()));
    addIncrementedParams(resultMap, unitsList, "unit");
    addValueParams(resultMap, unitsList, "");

    // Extract vehicles
    List<String> vehiclesList = extractVehicles(text);
    resultMap.put(EAlarmDataEntries.VEHICLES.getKey(), String.join("|", vehiclesList));
    addIncrementedParams(resultMap, vehiclesList, "vehicle");
    addValueParams(resultMap, vehiclesList, "");
    resultMap.values().removeIf(Objects::isNull);
    return resultMap;
  }

  private static List<String> extractUnits(String text) {
    Matcher matcher = UNITS_PATTERN.matcher(text);
    List<String> unitsList = new ArrayList<>();
    while (matcher.find()) {
      unitsList.add(
          Utility.removeNewlines(matcher.group(0))
              .split("\\s+\\d{2}:\\d{2}:\\d{2}|--:--:--")[0]
              .trim());
    }
    return unitsList;
  }

  private static List<String> extractVehicles(String text) {
    Matcher matcher = VEHICLES_PATTERN.matcher(text);
    List<String> vehiclesList = new ArrayList<>();

    while (matcher.find()) {
      vehiclesList.add(
          Utility.removeNewlines(matcher.group(0))
              .split("\\s+\\d{2}:\\d{2}:\\d{2}|--:--:--")[0]
              .trim());
    }
    return vehiclesList;
  }

  private static String[] extractCoordinates(String text) {
    Matcher matcher = COORDINATES_PATTERN.matcher(text);
    if (matcher.find()) {
      // Extract latitude and longitude without direction characters
      String latitude = matcher.group(1);
      String longitude = matcher.group(2);
      return new String[] {latitude, longitude};
    }
    return null; // No coordinates found
  }

  private static void addIncrementedParams(
      Map<String, String> resultMap, List<String> items, String prefix) {
    for (int i = 0; i < items.size(); i++) {
      resultMap.put(prefix + "_" + (i + 1), items.get(i));
    }
  }

  private static void addValueParams(
      Map<String, String> resultMap, List<String> items, String prefix) {
    for (String item : items) {
      resultMap.put(Utility.replaceWhitespaceWithUnderscore(item), item);
    }
  }
}
