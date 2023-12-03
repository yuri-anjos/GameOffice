package br.com.yurianjos.gameoffice.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utils {
    public static Long calculateDaysBetween(LocalDateTime d1, LocalDateTime d2) {
        return ChronoUnit.DAYS.between(d1, d2);
    }
}
