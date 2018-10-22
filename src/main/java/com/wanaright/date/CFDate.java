package com.wanaright.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CFDate {
    private static ZoneId ZONE_ID = ZoneId.systemDefault();
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private CFDate() {
    }

    public static void setGlobalZoneId(ZoneId zoneId) {
        ZONE_ID = zoneId;
    }

    public static void setGlobalFormatter(String format) {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(format);
    }

    public static void setGlobalFormatter(DateTimeFormatter dateTimeFormatter) {
        DATE_TIME_FORMATTER = dateTimeFormatter;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, ZONE_ID);
    }

    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static Date toUtilDate(LocalDateTime localDateTime) {
        return toUtilDate(localDateTime, ZONE_ID);
    }

    public static Date toUtilDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    public static LocalDateTime toLocalDateTime(LocalTime localTime) {
        return LocalDateTime.of(LocalDate.EPOCH, localTime);
    }

    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DATE_TIME_FORMATTER);
    }

    public static String format(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return format(localDateTime, dateTimeFormatter);
    }

    public static String format(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(localDateTime);
    }

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parse(String date, String format) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime parse(String date, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public static long getMills(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    public static long getMills(LocalDateTime localDateTime) {
        return getMills(localDateTime, ZONE_ID);
    }

    public static LocalDateTime toStartOfDay(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().atStartOfDay();
    }

    public static long between(LocalDateTime start, LocalDateTime end, ChronoUnit unitType) {
        return start.until(end, unitType);
    }
}
