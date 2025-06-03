package com.example.demo1.utils;

import com.example.demo1.model.Expense;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ChartUtils {

    /**
     * Remplit un PieChart avec les données de dépenses.
     */
    public static void populatePieChart(PieChart pieChart, Expense e) {
        pieChart.getData().clear();
        if (e == null) return;

        // Ajouter seulement les catégories avec des valeurs > 0
        if (e.getLogement() > 0) {
            pieChart.getData().add(new PieChart.Data("Logement", e.getLogement()));
        }
        if (e.getNourriture() > 0) {
            pieChart.getData().add(new PieChart.Data("Nourriture", e.getNourriture()));
        }
        if (e.getSorties() > 0) {
            pieChart.getData().add(new PieChart.Data("Sorties", e.getSorties()));
        }
        if (e.getTransport() > 0) {
            pieChart.getData().add(new PieChart.Data("Transport", e.getTransport()));
        }
        if (e.getVoyage() > 0) {
            pieChart.getData().add(new PieChart.Data("Voyage", e.getVoyage()));
        }
        if (e.getImpots() > 0) {
            pieChart.getData().add(new PieChart.Data("Impôts", e.getImpots()));
        }
        if (e.getAutres() > 0) {
            pieChart.getData().add(new PieChart.Data("Autres", e.getAutres()));
        }
        
        // Si aucune donnée n'est disponible, afficher un message
        if (pieChart.getData().isEmpty()) {
            pieChart.getData().add(new PieChart.Data("Aucune donnée", 1));
        }
    }

    /**
     * Remplit un LineChart avec une série de dépenses par mois.
     */
    public static void populateLineChart(LineChart<String, Number> lineChart, List<Expense> expenses) {
        lineChart.getData().clear();
        if (expenses.isEmpty()) return;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total mensuel");

        for (Expense e : expenses) {
            series.getData().add(new XYChart.Data<>(e.getPeriode(), e.getTotal()));
        }

        lineChart.getData().add(series);
    }
}
