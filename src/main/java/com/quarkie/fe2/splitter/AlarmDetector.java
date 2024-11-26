package com.quarkie.fe2.splitter;

import de.alamos.fe2.external.ExtractorObject;
import de.alamos.splitting.api.AbstractAlarmExtractorV2;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmDetector extends AbstractAlarmExtractorV2 {

  private static final Logger logger = LoggerFactory.getLogger(AlarmDetector.class);

  public static final String ILS_AA_EMAIL = "#ils_aa_email#";
  public static final String DICAL_POCSAG_ALERT = "#dical_pocsac_alert#";
  public static final String LARDIS_TETRA_SDS = "#lardis_tetra_sds#";
  public static final String ORIGIN = "origin";

  // TODO add those Prefixes and keywords
  public static ParserType detectSource(Map<String, String> map) {
    String origin = map.get(ORIGIN); // Use the constant for 'origin'
    logger.info("Detecting alarm source for origin: {}", origin); // Log the origin value

    // Step 1: Check for test case keywords and replace if found
    if (origin != null) {
      if (origin.contains(ILS_AA_EMAIL)) {
        map.put(ORIGIN, origin.replace(ILS_AA_EMAIL, ""));
        logger.info("Detected test case: ILS_AA_EMAIL");
        return ParserType.ILS_AA_EMAIL;
      } else if (origin.contains(DICAL_POCSAG_ALERT)) {
        map.put(ORIGIN, origin.replace(DICAL_POCSAG_ALERT, ""));
        logger.info("Detected test case: DICAL_POCSAG_ALERT");
        return ParserType.DICAL_POCSAG_ALERT;
      } else if (origin.contains(LARDIS_TETRA_SDS)) {
        map.put(ORIGIN, origin.replace(LARDIS_TETRA_SDS, ""));
        logger.info("Detected test case: LARDIS_TETRA_SDS");
        return ParserType.LARDIS_TETRA_SDS;
      }
    }

    // Step 2: If no test case keyword found, process normal alarm data
    if (Objects.equals(map.get("alarmType"), "MAIL")) {
      logger.info("Detected normal alarm type: MAIL");
      return ParserType.ILS_AA_EMAIL;
    } else if (Objects.equals(map.get("alarmType"), "TODO#DICAL")) {
      logger.info("Detected normal alarm type: TODO#DICAL");
      return ParserType.DICAL_POCSAG_ALERT;
    } else if (Objects.equals(map.get("alarmType"), "ALARM")) {
      logger.info("Detected normal alarm type: ALARM");
      return ParserType.LARDIS_TETRA_SDS;
    }

    // Log the error before throwing an exception
    logger.error("Unknown alarm source detected for map: {}", map);
    throw new IllegalArgumentException("Unknown alarm source");
  }

  @Override
  public ExtractorObject extractFromMap(Map<String, String> map) {
    Map<String, String> clonedMap =
        map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    ParserType source = detectSource(clonedMap);
    ExtractorObject extractorObject = new ExtractorObject();
    extractorObject.setData(clonedMap);

    logger.info("Parsing alarm data using the source: {}", source);

    switch (source) {
      case ILS_AA_EMAIL -> {
        logger.info("Parsing ILS_AA_EMAIL alarm text.");
        IlsAaEmailParser.parseAlarmText(clonedMap);
      }
      case DICAL_POCSAG_ALERT -> {
        logger.info("Parsing DICAL_POCSAG_ALERT alarm text.");
        DicalParser.parseAlarmText(clonedMap);
      }
      case LARDIS_TETRA_SDS -> {
        logger.info("Parsing LARDIS_TETRA_SDS alarm text.");
        LardisParser.parseAlarmText(clonedMap);
      }
      default -> {
        logger.error("No parser available for source: {}", source);
        throw new IllegalArgumentException("No parser available for source: " + source);
      }
    }

    extractorObject.setComplete(true);
    logger.info("Alarm extraction complete.");
    return extractorObject;
  }
}
