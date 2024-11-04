package com.quarkie.fe2.splitter;

import de.alamos.fe2.external.interfaces.IAlarmExtractor;
import java.util.Map;

enum ParserType {
  ILS_AA_EMAIL,
  DICAL_POCSAG_ALERT,
  LARDIS_STATUS_SDS
}

public class AlarmDetector implements IAlarmExtractor {

  // TODO add those Prefixes and keywords
  public static ParserType detectSource(String text) {
    if (text.contains("Einsatzanlass")) {
      return ParserType.ILS_AA_EMAIL;
    } else if (text.contains("DICAL RED#TODO")) {
      return ParserType.DICAL_POCSAG_ALERT;
    } else if (text.contains("SDS LARDIS#TODO")) {
      return ParserType.LARDIS_STATUS_SDS;
    }
    throw new IllegalArgumentException("Unknown alarm source");
  }

  @Override
  public Map<String, String> extract(String text) {
    ParserType source = detectSource(text);
    return switch (source) {
      case ILS_AA_EMAIL -> IlsAaEmailParser.parseAlarmText(text);
      case DICAL_POCSAG_ALERT -> DicalParser.parseAlarmText(text);
      case LARDIS_STATUS_SDS -> LardisParser.parseAlarmText(text);
      default -> throw new IllegalArgumentException("No parser available for source: " + source);
    };
  }
}
