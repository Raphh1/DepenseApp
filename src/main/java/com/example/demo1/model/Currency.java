package com.example.demo1.model;

import java.text.NumberFormat;
import java.util.Locale;

public enum Currency {
    EUR("Euro", "€", "EUR", Locale.FRANCE),
    USD("Dollar américain", "$", "USD", Locale.US),
    GBP("Livre sterling", "£", "GBP", Locale.UK),
    JPY("Yen japonais", "¥", "JPY", Locale.JAPAN),
    CHF("Franc suisse", "CHF", "CHF", new Locale("de", "CH"));

    private final String name;
    private final String symbol;
    private final String code;
    private final Locale locale;

    Currency(String name, String symbol, String code, Locale locale) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCode() {
        return code;
    }

    public Locale getLocale() {
        return locale;
    }

    public String format(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(amount);
    }

    public String formatWithoutSymbol(double amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount);
    }
}