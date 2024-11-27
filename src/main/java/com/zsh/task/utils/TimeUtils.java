package com.zsh.task.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeUtils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public static String last30dayStr() {
        LocalDate currentDate = LocalDate.now();
        LocalDate localDate = currentDate.minusMonths(1);
        Date var = new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        return SDF.format(var);
    }
    public static Date last30dayDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate localDate = currentDate.minusMonths(1);
        return new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

}
