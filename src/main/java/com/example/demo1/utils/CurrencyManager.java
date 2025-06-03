package com.example.demo1.utils;

import com.example.demo1.model.Currency;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.application.Platform;

import java.util.Map;

public class CurrencyManager {
    private static CurrencyManager instance;
    private final ObjectProperty<Currency> currentCurrency;
    private final ExchangeRateClient exchangeRateClient;

    private CurrencyManager() {
        this.currentCurrency = new SimpleObjectProperty<>(Currency.EUR);
        this.exchangeRateClient = ExchangeRateClient.getInstance();
        
        // Initialiser les taux de change
        loadExchangeRates();
    }

    public static synchronized CurrencyManager getInstance() {
        if (instance == null) {
            instance = new CurrencyManager();
        }
        return instance;
    }

    public ObjectProperty<Currency> currentCurrencyProperty() {
        return currentCurrency;
    }

    public Currency getCurrentCurrency() {
        return currentCurrency.get();
    }

    public void setCurrentCurrency(Currency currency) {
        this.currentCurrency.set(currency);
    }

    public void loadExchangeRates() {
        exchangeRateClient.fetchExchangeRates().thenAccept(rates -> {
            Platform.runLater(() -> {
                System.out.println("Taux de change mis à jour: " + rates);
            });
        }).exceptionally(throwable -> {
            System.err.println("Erreur lors du chargement des taux: " + throwable.getMessage());
            return null;
        });
    }

    public double convertAmount(double amount, Currency fromCurrency, Currency toCurrency) {
        return exchangeRateClient.convertAmount(amount, fromCurrency.getCode(), toCurrency.getCode());
    }

    public String formatAmount(double amount, Currency currency) {
        return currency.format(amount);
    }

    public String formatAmountInCurrentCurrency(double amountInEur) {
        Currency current = getCurrentCurrency();
        double convertedAmount = convertAmount(amountInEur, Currency.EUR, current);
        return current.format(convertedAmount);
    }

    public Map<String, Double> getCurrentRates() {
        return exchangeRateClient.getCachedRates();
    }
}