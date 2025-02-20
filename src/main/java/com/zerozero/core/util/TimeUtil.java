package com.zerozero.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    private static final DateTimeFormatter YYYY_MM_DD_DOT_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static String toDotFormattedString(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(YYYY_MM_DD_DOT_FORMATTER);
    }
}
