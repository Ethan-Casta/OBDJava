package com.ethan.obdjava.views;

import java.time.Duration;
import java.time.LocalDateTime;

public class MainViewUtils {
    public static String compareTwoDateTime(LocalDateTime startDate, LocalDateTime endDate) {
        Duration duration = Duration.between(startDate, endDate);

        long seconds = duration.getSeconds();
        int nano = duration.getNano();
        long milliSeconds = nano / 1000000;

        return seconds + "." + milliSeconds + "s";
    }
}
