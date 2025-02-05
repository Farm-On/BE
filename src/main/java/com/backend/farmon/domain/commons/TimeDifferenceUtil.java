package com.backend.farmon.domain.commons;

    import java.time.Duration;
import java.time.LocalDateTime;

    public class TimeDifferenceUtil {

        public static String calculateTimeDifference(LocalDateTime createdAt) {
            // 현재 시간
            LocalDateTime now = LocalDateTime.now();

            // 두 날짜 사이의 차이 계산
            Duration duration = Duration.between(createdAt, now);

            // 초, 분, 시간 단위로 변환
            long seconds = duration.getSeconds();
            long minutes = seconds / 60;
            long hours = minutes / 60;

           if (hours > 0) {
                return hours + "시간 전";
            } else if (minutes > 0) {
                return minutes + "분 전";
            } else {
                return seconds + "초 전";
            }
        }
}
