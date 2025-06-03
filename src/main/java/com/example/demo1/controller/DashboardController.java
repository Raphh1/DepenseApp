package com.example.demo1.controller;

import com.example.demo1.dao.ExpenseDAO;
import com.example.demo1.model.Expense;
import com.example.demo1.utils.ChartUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @FXML private DatePicker monthDatePicker;
    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private BarChart<String, Number> expensesVsRevenuesChart;

    @FXML
    public void initialize() {
        logger.info("Initialisation du tableau de bord");

        // Définir la date par défaut (mois actuel)
        monthDatePicker.setValue(LocalDate.now());
        
        // Mettre à jour les graphiques avec la date actuelle
        updateCharts(LocalDate.now());

        // Écouter les changements de date
        monthDatePicker.setOnAction(e -> {
            LocalDate selectedDate = monthDatePicker.getValue();
            if (selectedDate != null) {
                updateCharts(selectedDate);
            }
        });

        loadExpensesVsRevenuesChart();
    }

    private void updateCharts(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String monthKey = selectedDate.format(formatter); // Format: "2025-06"
        
        logger.info("Mise à jour des graphiques pour le mois : {}", monthKey);

        // Chercher les dépenses pour ce mois
        List<Expense> expenses = ExpenseDAO.findByMonth(monthKey);
        logger.info("Nombre de dépenses trouvées pour '{}': {}", monthKey, expenses.size());
        
        // Si pas de données trouvées pour le format yyyy-MM, essayer les anciens formats
        if (expenses.isEmpty()) {
            logger.info("Aucune donnée trouvée pour le format {}, recherche dans toutes les données...", monthKey);
            List<Expense> allExpenses = ExpenseDAO.findAll();
            logger.info("Total des dépenses dans la base : {}", allExpenses.size());
            
            // Afficher toutes les périodes disponibles pour debug
            for (Expense exp : allExpenses) {
                logger.info("Période disponible : '{}'", exp.getPeriode());
            }
        }
        
        if (!expenses.isEmpty()) {
            // Agrégation des dépenses pour le mois (si plusieurs entrées)
            Expense aggregatedExpense = aggregateExpenses(expenses, monthKey);
            logger.info("Dépenses agrégées: Total={}, Logement={}, Nourriture={}", 
                       aggregatedExpense.getTotal(), aggregatedExpense.getLogement(), aggregatedExpense.getNourriture());
            ChartUtils.populatePieChart(pieChart, aggregatedExpense);
        } else {
            logger.info("Aucune dépense trouvée pour '{}', affichage d'un message", monthKey);
            pieChart.getData().clear();
            // Ajouter un placeholder quand pas de données
            pieChart.getData().add(new javafx.scene.chart.PieChart.Data("Aucune donnée pour " + monthKey, 1));
        }

        // Pour le LineChart, afficher l'évolution des 12 derniers mois
        updateLineChart();
    }
    
    /**
     * Agrège plusieurs dépenses pour le même mois
     */
    private Expense aggregateExpenses(List<Expense> expenses, String periode) {
        if (expenses.isEmpty()) {
            return null;
        }
        
        if (expenses.size() == 1) {
            return expenses.get(0);
        }
        
        // Agrégation de toutes les dépenses pour le mois
        double totalLogement = expenses.stream().mapToDouble(Expense::getLogement).sum();
        double totalNourriture = expenses.stream().mapToDouble(Expense::getNourriture).sum();
        double totalSorties = expenses.stream().mapToDouble(Expense::getSorties).sum();
        double totalTransport = expenses.stream().mapToDouble(Expense::getTransport).sum();
        double totalVoyage = expenses.stream().mapToDouble(Expense::getVoyage).sum();
        double totalImpots = expenses.stream().mapToDouble(Expense::getImpots).sum();
        double totalAutres = expenses.stream().mapToDouble(Expense::getAutres).sum();
        double total = totalLogement + totalNourriture + totalSorties + totalTransport + totalVoyage + totalImpots + totalAutres;
        
        return new Expense(periode, total, totalLogement, totalNourriture, totalSorties, 
                          totalTransport, totalVoyage, totalImpots, totalAutres);
    }
    
    /**
     * Met à jour le LineChart avec l'évolution des 12 derniers mois
     */
    private void updateLineChart() {
        List<Expense> allExpenses = ExpenseDAO.findAll();
        ChartUtils.populateLineChart(lineChart, allExpenses);
    }

    /**
     * Charge le graphique comparatif des dépenses et des revenus
     */
    private void loadExpensesVsRevenuesChart() {
        XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Dépenses");
        // Ajouter les données des dépenses (exemple statique)
        expensesSeries.getData().add(new XYChart.Data<>("Janvier", 500));
        expensesSeries.getData().add(new XYChart.Data<>("Février", 600));

        XYChart.Series<String, Number> revenuesSeries = new XYChart.Series<>();
        revenuesSeries.setName("Revenus");
        // Ajouter les données des revenus (exemple statique)
        revenuesSeries.getData().add(new XYChart.Data<>("Janvier", 800));
        revenuesSeries.getData().add(new XYChart.Data<>("Février", 700));

        expensesVsRevenuesChart.getData().addAll(expensesSeries, revenuesSeries);
    }
}
