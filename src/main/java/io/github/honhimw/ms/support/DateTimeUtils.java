package io.github.honhimw.ms.support;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author hon_him
 * @since 2022-05-31
 */
@SuppressWarnings("unused")
public class DateTimeUtils {

    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();

    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.ofHours(8);

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String DEFAULT_DATE_PATTERN = "yyyyMMdd";

    public static final String DEFAULT_TIME_PATTERN = "HHmmss";

    public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO_DATE_TIME_PATTERN_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss+08:00";

    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);

    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);

    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * ==================================================================================
     * 将时间对象转为字符串
     * ==================================================================================
     */

    public static String format() {
        return format(LocalDateTime.now());
    }

    public static String format(LocalTime localTime) {
        return format(localTime, DEFAULT_TIME_FORMATTER);
    }

    public static String format(LocalDate localDate) {
        return format(localDate, DEFAULT_DATE_FORMATTER);
    }

    public static String format(LocalDate localDate, String pattern) {
        return format(localDate, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalTime localTime, String pattern) {
        return format(localTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String format(LocalDate localDate, DateTimeFormatter formatter) {
        return localDate.format(formatter);
    }

    public static String format(LocalTime localTime, DateTimeFormatter formatter) {
        return localTime.format(formatter);
    }

    public static String format(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    /**
     * ==================================================================================
     * 将字符串转为时间对象
     * ==================================================================================
     */
    public static LocalDateTime parseBasicLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseBasicLocalDateTime(Date date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String dateTimeString= simpleFormat.format(date);
        return LocalDateTime.parse(dateTimeString, DEFAULT_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseIsoLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, ISO_DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String dateString, String pattern) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeString, String pattern) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
    }

    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }

    public static Date toDate(LocalDateTime localDate) {
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

}
