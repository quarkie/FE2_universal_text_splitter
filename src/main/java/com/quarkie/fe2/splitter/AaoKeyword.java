package com.quarkie.fe2.splitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum AaoKeyword {
  BR1,
  BR2,
  BR3,
  BR4,
  BR5,
  BR6,
  TH1,
  TH2,
  TH3,
  TH4,
  TH5,
  TH6,
  SE1,
  SE2,
  SE3,
  SE4,
  SE5,
  SE6,
  ERK;

  private static final Pattern KEYWORD_PATTERN =
      Pattern.compile(
          String.join("|", Arrays.stream(values()).map(Enum::name).toArray(String[]::new)));

  public static boolean contains(String message) {
    return KEYWORD_PATTERN.matcher(message).find();
  }

  // Method to extract the first matched keyword
  public static Optional<AaoKeyword> extractFirst(String message) {
    Matcher matcher = KEYWORD_PATTERN.matcher(message);
    if (matcher.find()) {
      return Optional.of(AaoKeyword.valueOf(matcher.group())); // Convert match to enum
    }
    return Optional.empty(); // No match found
  }

  // Method to extract all matched keywords
  public static List<AaoKeyword> extractAll(String message) {
    Matcher matcher = KEYWORD_PATTERN.matcher(message);
    List<AaoKeyword> matchedKeywords = new ArrayList<>();
    while (matcher.find()) {
      matchedKeywords.add(AaoKeyword.valueOf(matcher.group())); // Convert match to enum
    }
    return matchedKeywords;
  }
}
