package com.backend.farmon.domain.commons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    // 답글 2025년 1월 15일 형식 통일
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }
}