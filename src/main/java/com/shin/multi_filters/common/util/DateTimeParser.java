package com.shin.multi_filters.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class DateTimeParser {
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX";
    public static final String ISO_DATE_FORMAT_WITH_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";
    public static final String ISO_DATE_FORMAT_WITH_XXX = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String ISO_DATE_FORMAT_WITH_Z_SIMPLE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DEFAULT_DATE_FORMAT_WITH_MILLIS = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";
    public static final String DEFAULT_DATE_FORMAT_SIMPLE = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT_WITH_HOURS_AND_MINUTES = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_DATE_FORMAT_WITH_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String RFC_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    public static final String RFC_DATE_FORMAT_SIMPLE = "EEE, dd MMM yyyy HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD_YYYY_HH_MM = "MM/dd/yyyy HH:mm";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM_SS = "MM-dd-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM = "MM-dd-yyyy HH:mm";
    public static final String DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS = "MM-dd-yyyy";

    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT_WITH_Z),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT_WITH_XXX),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT_WITH_Z_SIMPLE),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_WITH_MILLIS),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_SIMPLE),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_WITH_HOURS_AND_MINUTES),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_WITH_YEAR_MONTH_DAY),
            DateTimeFormatter.ofPattern(RFC_DATE_FORMAT),
            DateTimeFormatter.ofPattern(RFC_DATE_FORMAT_SIMPLE),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_HH_MM_SS),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_HH_MM),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM_SS),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS)
    };

    public static LocalDateTime parseLocalDateTime(String dateString) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
            }
        }
        throw new DateTimeParseException("Unable to parse date: " + dateString, dateString, 0);
    }

    public static LocalDate parseLocalDate(String dateString) {
        for (DateTimeFormatter formatter : getLocalDateFormatters()) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
            }
        }
        throw new DateTimeParseException("Unable to parse date: " + dateString, dateString, 0);
    }

    private static DateTimeFormatter[] getLocalDateFormatters() {
        return Arrays.stream(FORMATTERS)
                .filter(formatter ->
                        !formatter.toString().contains("HH")
                                && !formatter.toString().contains("mm")
                                && !formatter.toString().contains("ss"))
                .toArray(DateTimeFormatter[]::new);
    }
}
