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

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);

        log.info("Date time: " + dateTime);

        return dateTime;
    }
}
