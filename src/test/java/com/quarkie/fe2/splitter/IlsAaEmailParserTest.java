package com.quarkie.fe2.splitter;


import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import de.alamos.fe2.external.enums.EAlarmDataEntries;

public class IlsAaEmailParserTest {

    @Test
    public void testParseEmailText() {
        String emailText = "SE2 / THL Türöffnunggedruckt am 22.Oktober 2024 9:29\n" +
                "\n" +
                "Einsatzanlass\n" +
                "\n" +
                "Stichwort F SE2 Türe öffnen/verschliessen\n" +
                "\n" +
                "Meldebild THL Türöffnung\n" +
                "\n" +
                "Zusatz RD und Pol auf Anfahrt\n" +
                "\n" +
                "Meldender selbst / 07967123\n" +
                "\n" +
                "Rückruf  -  /  -\n" +
                "\n" +
                "Meldeweg 112\n" +
                "\n" +
                "Einsatzort\n" +
                "\n" +
                "Ort Rosenberg [73494]\n" +
                "\n" +
                "Ortsteil Hohenberg\n" +
                "\n" +
                "Straße Blumenstraße 1\n" +
                "\n" +
                "Ortszusatz EFH\n" +
                "\n" +
                "EO Koordinaten\n" +
                "\n" +
                "WGS N49.00187 E10.05223\n" +
                "\n" +
                "EinsatzNrn F1240003249\n" +
                "\n" +
                "Einsatzstatus Zeit\n" +
                "\n" +
                "unerledigt 22.10.2024 09:28:29\n" +
                "\n" +
                "alarmiert 22.10.2024 09:29:02\n" +
                "\n" +
                "EM (Stärke/AGT) Zuteil. Alarm Status 3 Status 4 Status 7 Status 8 Status 1 Ende\n" +
                "\n" +
                "FF Rosenberg\n" +
                "\n" +
                "Feuerwache\n" +
                "\n" +
                "09:29:01 09:29:06 --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:--\n" +
                "\n" +
                "FF Rosenberg Führung\n" +
                "\n" +
                "B\n" +
                "\n" +
                "09:29:01 09:29:06 --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:--\n" +
                "\n" +
                "FF Rosenberg\n" +
                "\n" +
                "Türöffnung\n" +
                "\n" +
                "09:29:01 09:29:07 --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:--\n" +
                "\n" +
                "FL **********\n" +
                "\n" +
                "FAHRZEUGE *****\n" +
                "\n" +
                "09:29:02* 09:29:06 --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:--\n" +
                "\n" +
                "FL Rosenberg 1/19-1 --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:--\n" +
                "\n" +
                "FL Rosenberg 1/43-1 --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:-- --:--:--\n" +
                "\n" +
                "Einsatzmaßnahme Zeitpunkt Status\n" +
                "\n" +
                "Polizei zuteilen 22.10.2024 09:28:59 zugeteilt\n" +
                "\n" +
                "RD-Einsatz Brandwache erzeugen 22.10.2024 09:28:59 zugeteilt\n" +
                "\n" +
                "INFO DME FF OAK Kreisbrandmeister\n" +
                "\n" +
                "Info\n" +
                "\n" +
                "22.10.2024 09:29:02 zugeteilt\n" +
                "\n" +
                "DME FF OAK Kreisbrandmeister INFO 22.10.2024 09:29:02 zugeteilt\n" +
                "\n" +
                "SMS FF OAK Kreisbrandmeister INFO 22.10.2024 09:29:02 zugeteilt\n" +
                "\n" +
                "DME FF Rosenberg DiCalRed 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "DME FF Rosenberg Führung B Tag 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "DME FF Rosenberg Türöffnung Tag 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "INFO Einsatzindikation KBM OAK 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "INFO Polizei verständigen (FLZ Aalen) 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "SDS FL Rosenberg 1/00 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "SMS FF Rosenberg 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "FAX FF Rosenberg 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "MAIL FF Rosenberg 22.10.2024 09:29:07 zugeteilt\n" +
                "\n" +
                "Alarmdruck  1240128515  /  Feuerwehr\n" +
                "\n" +
                "- 1 -";

        Map<String, String> result = IlsAaEmailParser.parseAlarmText(emailText);

        for (Map.Entry<String, String> entry : result.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }


        assertEquals("Blumenstraße 1", result.get(EAlarmDataEntries.STREET.getKey()));
        assertEquals("1", result.get(EAlarmDataEntries.HOUSE.getKey()));
        assertEquals("Rosenberg", result.get(EAlarmDataEntries.CITY.getKey()));
        assertEquals("73494", result.get(EAlarmDataEntries.POSTALCODE.getKey()));
        assertEquals("49.00187", result.get(EAlarmDataEntries.LAT.getKey()));
        assertEquals("10.05223", result.get(EAlarmDataEntries.LNG.getKey()));
        assertEquals("F1240003249", result.get(EAlarmDataEntries.EXTERNAL_ID.getKey()));
        assertEquals("selbst / 07967123", result.get(EAlarmDataEntries.CALLER.getKey()));
        assertEquals("-  /  -", result.get(EAlarmDataEntries.CALLER_CONTACT.getKey()));
        assertEquals("FL Rosenberg 1/19-1", result.get("vehicle_1"));
        assertEquals("FL Rosenberg 1/43-1", result.get("vehicle_2"));
        assertEquals("FL Rosenberg 1/19-1|FL Rosenberg 1/43-1", result.get(EAlarmDataEntries.VEHICLES.getKey()));
        assertEquals("SE2", result.get(EAlarmDataEntries.KEYWORD.getKey()));
        assertEquals("F SE2 Türe öffnen/verschliessen", result.get(EAlarmDataEntries.KEYWORD_DESCRIPTION.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.KEYWORD_IDENTIFICATION.getKey()));
        assertEquals("RD und Pol auf Anfahrt", result.get(EAlarmDataEntries.KEYWORD_ADDITIONAL.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.KEYWORD_CATEGORY.getKey()));
        assertEquals("Stichwort F SE2 Türe öffnen/verschliessen", result.get(EAlarmDataEntries.TEXT.getKey()));
        assertEquals("Hohenberg", result.get(EAlarmDataEntries.LOCATION_ADDITIOnAL.getKey()));
        assertEquals("FF Rosenberg Feuerwache|FF Rosenberg Führung B|FF Rosenberg Türöffnung", result.get(EAlarmDataEntries.EINSATZMITTEL.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.CITY_ABBR.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.ABEK.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.GK_X.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.GK_Y.getKey()));
        //assertEquals("SE2", result.get(EAlarmDataEntries.DESTINATION.getKey()));
        assertEquals("FF Rosenberg Feuerwache", result.get("unit_1"));
        assertEquals("FF Rosenberg Führung B", result.get("unit_2"));
        assertEquals("FF Rosenberg Türöffnung", result.get("unit_3"));
        assertEquals("FF Rosenberg Feuerwache|FF Rosenberg Führung B|FF Rosenberg Türöffnung", result.get("units"));
    }
}