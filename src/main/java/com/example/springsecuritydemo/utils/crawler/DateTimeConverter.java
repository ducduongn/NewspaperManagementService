package com.example.springsecuritydemo.utils.crawler;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ducduongn
 */
@Slf4j
public class DateTimeConverter {
    public static LocalDateTime convertDateTimeStringToLocalDateTime(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-M-yyyy HH:mm:ss");

        String shortenDateTimeString = extractDateTimeFromElement(dateTimeString);

        LocalDateTime dateTime = LocalDateTime.parse(shortenDateTimeString, dateTimeFormatter);

        log.info("Date time: " + dateTime);

        return dateTime;
    }

    public static String extractDateTimeFromElement(String dateTimeString) {

        String[] strings = dateTimeString.split(", ");

        StringBuilder newDateTimeString = new StringBuilder();

        for (int i = 1; i < strings.length; i++) {
            newDateTimeString.append(strings[i] + " ");
        }
        return newDateTimeString.toString()
                .replace("(GMT+7)", "").trim()
                .concat(":00")
                .replace("/", "-");
    }
}
