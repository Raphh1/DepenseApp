package com.example.demo1.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    /**
     * Retourne les 12 derniers mois sous forme de "MMMM yyyy" (ex: "juin 2025").
     */
    public static List<String> getLast12Months() {
        List<String> months = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);

        for (int i = 0; i < 12; i++) {
            String formatted = LocalDate.now().minusMonths(i).format(formatter);
            months.add(formatted);
        }

        return months;
    }
}
