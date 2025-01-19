package com.backend.farmon.converter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// CreatedAt 포맷 클래스
public class ConvertTime {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public static String convertLocalDatetimeToTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        long diffTime = localDateTime.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -

        if (diffTime < SEC)
            return diffTime + "초";

        diffTime = diffTime / SEC;
        if (diffTime < MIN)
            return diffTime + "분";

        diffTime = diffTime / MIN;
        if (diffTime < HOUR)
            return diffTime + "시간";

        diffTime = diffTime / HOUR;
        if (diffTime < DAY)
            return diffTime + "일";

        diffTime = diffTime / DAY;
        if (diffTime < MONTH)
            return diffTime + "개월";

        diffTime = diffTime / MONTH;
        return diffTime + "년";
    }

    // LocalDateTime을 날짜 "03.11" 형식의 문자열로 파싱
    public static String convertToDate(LocalDateTime now) {
        // LocalDateTime 객체를 "MM.dd" 형식의 문자열로 변환
        return now.format(DateTimeFormatter.ofPattern("MM.dd"));
    }

    // LocalDateTime을 "12:00" 형식의 문자열로 파싱
    public static String convertToTime(LocalDateTime now) {
        // LocalDateTime 객체를 "HH:mm" 형식으로 변환
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    // 24시간 형식을 오전/오후 형식으로 변환
    public static String convertToAmPmFormat(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("a h:mm"));
    }
}
