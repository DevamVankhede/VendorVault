package com.orion.vendorvault.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static String formatRelativeTime(LocalDateTime dt) {
        if (dt == null) return "";
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dt, now);
        if (minutes < 60) return minutes + " minutes ago";
        long hours = ChronoUnit.HOURS.between(dt, now);
        if (hours < 24) return hours + " hours ago";
        long days = ChronoUnit.DAYS.between(dt, now);
        if (days == 1) return "Yesterday";
        if (days < 30) return days + " days ago";
        return dt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public static String formatIndianDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public static String getFiscalYear(LocalDate date) {
        if (date == null) return "";
        int year = date.getYear();
        if (date.getMonthValue() < 4) {
            return "FY" + (year - 1) + "-" + String.valueOf(year).substring(2);
        } else {
            return "FY" + year + "-" + String.valueOf(year + 1).substring(2);
        }
    }

    public static int calculateDaysUntil(LocalDate target) {
        if (target == null) return 0;
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), target);
    }
}
