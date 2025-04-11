package com.shin.gud_filters.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateTimeParser {
    // Date-Time format string constants

    public static final String ISO_DATE_TIME_BASIC = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO_DATE_TIME_IN_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX";
    public static final String ISO_DATE_FORMAT_WITH_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'";
    public static final String ISO_DATE_FORMAT_WITH_XXX = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String ISO_DATE_FORMAT_WITH_Z_SIMPLE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DEFAULT_DATE_FORMAT_WITH_MILLIS = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";

    public static final String DEFAULT_DATE_FORMAT_SIMPLE = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT_WITH_HOURS_AND_MINUTES = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD_YYYY_HH_MM = "MM/dd/yyyy HH:mm";
    public static final String DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM_SS = "MM-dd-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM = "MM-dd-yyyy HH:mm";

    // Date-only format string constants
    public static final String ISO_LOCAL_DATE = "yyyy-MM-dd";
    public static final String US_DATE_SLASH = "MM/dd/yyyy";
    public static final String US_DATE_HYPHEN = "MM-dd-yyyy";
    public static final String EU_DATE_SLASH = "dd/MM/yyyy";
    public static final String EU_DATE_HYPHEN = "dd-MM-yyyy";
    public static final String LONG_MONTH_DD_YYYY = "dd MMMM yyyy";
    public static final String SHORT_MONTH_DD_YYYY = "dd MMM yyyy";
    public static final String LONG_MONTH_DD_COMMA_YYYY = "MMMM dd, yyyy";
    public static final String SHORT_MONTH_DD_COMMA_YYYY = "MMM dd, yyyy";
    public static final String DAY_SHORT_MONTH = "EEE, dd MMM yyyy";
    public static final String DAY_LONG_MONTH = "EEEE, dd MMMM yyyy";
    public static final String COMPACT_YYYYMMDD = "yyyyMMdd";
    public static final String COMPACT_DDMMYYYY = "ddMMyyyy";

    // DateTimeFormatters
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern(ISO_DATE_TIME_BASIC),
            DateTimeFormatter.ofPattern(ISO_DATE_TIME_IN_MILLIS),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT_WITH_Z),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT_WITH_XXX),
            DateTimeFormatter.ofPattern(ISO_DATE_FORMAT_WITH_Z_SIMPLE),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_WITH_MILLIS),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_SIMPLE),
            DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_WITH_HOURS_AND_MINUTES),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_HH_MM_SS),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_HH_MM),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM_SS),
            DateTimeFormatter.ofPattern(DATE_FORMAT_MM_DD_YYYY_WITH_HYPHENS_HH_MM)
    };

    private static final DateTimeFormatter[] DATE_ONLY_FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern(ISO_LOCAL_DATE),
            DateTimeFormatter.ofPattern(US_DATE_SLASH),
            DateTimeFormatter.ofPattern(US_DATE_HYPHEN),
            DateTimeFormatter.ofPattern(EU_DATE_SLASH),
            DateTimeFormatter.ofPattern(EU_DATE_HYPHEN),
            DateTimeFormatter.ofPattern(LONG_MONTH_DD_YYYY),
            DateTimeFormatter.ofPattern(SHORT_MONTH_DD_YYYY),
            DateTimeFormatter.ofPattern(LONG_MONTH_DD_COMMA_YYYY),
            DateTimeFormatter.ofPattern(SHORT_MONTH_DD_COMMA_YYYY),
            DateTimeFormatter.ofPattern(DAY_SHORT_MONTH),
            DateTimeFormatter.ofPattern(DAY_LONG_MONTH),
            DateTimeFormatter.ofPattern(COMPACT_YYYYMMDD),
            DateTimeFormatter.ofPattern(COMPACT_DDMMYYYY)
    };

    public static Optional<LocalDateTime> tryParseDateTime(String input) {
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return Optional.of(LocalDateTime.parse(input, formatter));
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    public static Optional<LocalDate> tryParseDate(String input) {
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
            try {
                return Optional.of(LocalDate.parse(input, formatter));
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    public static Comparable<?> smartParse(String input) {
        return tryParseDateTime(input).<Comparable<?>>map(dt -> dt)
                .or(() -> tryParseDate(input).map(date -> date))
                .orElseThrow(() -> new IllegalArgumentException("Invalid date format: " + input));
    }
}
