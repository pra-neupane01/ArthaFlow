package com.arthaflow.util;

import java.time.LocalDate;

public final class DateRangeHelper {
    private DateRangeHelper() {}

    /**
     * @param range "15", "30", "custom", or null/other for full history
     * @return null = no date filter (all); otherwise [from, to] inclusive as yyyy-MM-dd
     */
    public static String[] resolve(String range, String from, String to) {
        LocalDate end = LocalDate.now();
        if ("custom".equalsIgnoreCase(range) && from != null && to != null && !from.isBlank() && !to.isBlank()) {
            return new String[]{from.trim(), to.trim()};
        }
        if ("15".equals(range)) {
            return new String[]{end.minusDays(15).toString(), end.toString()};
        }
        if ("30".equals(range)) {
            return new String[]{end.minusDays(30).toString(), end.toString()};
        }
        return null;
    }
}
