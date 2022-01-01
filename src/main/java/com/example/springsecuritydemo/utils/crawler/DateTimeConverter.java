package com.example.springsecuritydemo.utils.crawler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ducduongn
 */
public class DateTimeConverter {
    public static LocalDateTime convertDateTimeStringToLocalDateTime(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-M-yyyy HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);

        return dateTime;
    }
}
