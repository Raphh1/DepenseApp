package com.example.demo1.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ExchangeRateClient {
    private static final String API_URL = "https://cdn.taux.live/api/ecb.json";
    private static ExchangeRateClient instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private Map<String, Double> exchangeRates;
    private long lastUpdate = 0;
    private static final long CACHE_DURATION = 3600000; // 1 heure en millisecondes

    private ExchangeRateClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.exchangeRates = new HashMap<>();
        // EUR est la devise de base avec un taux de 1.0
        this.exchangeRates.put("EUR", 1.0);
    }

    public static synchronized ExchangeRateClient getInstance() {
        if (instance == null) {
            instance = new ExchangeRateClient();
        }
        return instance;
    }

    public CompletableFuture<Map<String, Double>> fetchExchangeRates() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL))
                        .timeout(Duration.ofSeconds(30))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    return parseExchangeRates(response.body());
                } else {
                    System.err.println("Erreur HTTP: " + response.statusCode());
                    return getDefaultRates();
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Erreur lors de la récupération des taux: " + e.getMessage());
                return getDefaultRates();
            }
        });
    }

    private Map<String, Double> parseExchangeRates(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            Map<String, Double> rates = new HashMap<>();
            
            // EUR est la devise de base
            rates.put("EUR", 1.0);
            
            // Récupérer les taux depuis le JSON
            JsonNode ratesNode = root.get("rates");
            if (ratesNode != null) {
                if (ratesNode.has("USD")) {
                    rates.put("USD", ratesNode.get("USD").asDouble());
                }
                if (ratesNode.has("GBP")) {
                    rates.put("GBP", ratesNode.get("GBP").asDouble());
                }
                if (ratesNode.has("JPY")) {
                    rates.put("JPY", ratesNode.get("JPY").asDouble());
                }
                if (ratesNode.has("CHF")) {
                    rates.put("CHF", ratesNode.get("CHF").asDouble());
                }
            }
            
            this.exchangeRates = rates;
            this.lastUpdate = System.currentTimeMillis();
            return rates;
        } catch (Exception e) {
            System.err.println("Erreur lors du parsing JSON: " + e.getMessage());
            return getDefaultRates();
        }
    }

    private Map<String, Double> getDefaultRates() {
        Map<String, Double> defaultRates = new HashMap<>();
        defaultRates.put("EUR", 1.0);
        defaultRates.put("USD", 1.08);
        defaultRates.put("GBP", 0.86);
        defaultRates.put("JPY", 162.0);
        defaultRates.put("CHF", 0.97);
        return defaultRates;
    }

    public Map<String, Double> getCachedRates() {
        if (exchangeRates.isEmpty() || 
            (System.currentTimeMillis() - lastUpdate) > CACHE_DURATION) {
            // Utiliser les taux par défaut si pas de cache ou cache expiré
            if (exchangeRates.isEmpty()) {
                exchangeRates = getDefaultRates();
            }
        }
        return new HashMap<>(exchangeRates);
    }

    public double convertAmount(double amount, String fromCurrency, String toCurrency) {
        Map<String, Double> rates = getCachedRates();
        
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        
        // Convertir vers EUR d'abord, puis vers la devise cible
        double amountInEur = amount / rates.getOrDefault(fromCurrency, 1.0);
        return amountInEur * rates.getOrDefault(toCurrency, 1.0);
    }
}
