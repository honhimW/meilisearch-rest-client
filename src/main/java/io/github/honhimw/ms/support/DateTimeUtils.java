package io.github.honhimw.ms.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author hon_him
 * @since 2022-05-31
 */
@SuppressWarnings("unused")
public class DateTimeUtils {

    public static final String RFC_3339 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";
    public static final DateTimeFormatter RFC_3339_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd")
        .appendLiteral('T')
        .appendPattern("HH:mm:ss")
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .appendLiteral('Z')
        .toFormatter();

    public static String format(LocalDateTime time) {
        return time.format(RFC_3339_FORMATTER);
    }

    public static LocalDateTime parse(String time) {
        return LocalDateTime.parse(time, RFC_3339_FORMATTER);
    }

}
