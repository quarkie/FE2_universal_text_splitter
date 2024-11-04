package com.quarkie.fe2.splitter;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static String PARAM_PREFIX_CUSTOM = "c_";

    public static String extractValue(String text, String key) {
        Pattern pattern = Pattern.compile(key + "\\s+(.+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    public static String extractPattern(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    public static String replaceWhitespaceWithUnderscore(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s", "_");
    }

}