package io.github.honhimw.ms.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author hon_him
 * @since 2022-05-31
 */
@SuppressWarnings("unused")
public class DateTimeUtils {

    public static final String RFC_3339 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";
    public static final DateTimeFormatter RFC_3339_FORMATTER = DateTimeFormatter.ofPattern(RFC_3339);

    public static String format(LocalDateTime time) {
        return time.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public static LocalDateTime parse(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.RFC_1123_DATE_TIME);
    }

}
