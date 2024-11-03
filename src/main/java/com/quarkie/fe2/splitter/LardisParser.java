package com.quarkie.fe2.splitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LardisParser {
    public static Map<String, String> parseAlarmText(String text) {
        Map<String, String> resultMap = new HashMap<>();
        // TODO
        resultMap.put("LARDIS", "LARDIS Alarm");
        resultMap.values().removeIf(Objects::isNull);
        return resultMap;
    }
}