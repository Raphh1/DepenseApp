package com.example.demo1.controller;

import com.example.demo1.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddExpenseDialogController {

    private static final Logger logger = LoggerFactory.getLogger(AddExpenseDialogController.class);

    @FXML private DatePicker periodeDatePicker;
    @FXML private TextField logementField;
    @FXML private TextField nourritureField;
    @FXML private TextField sortiesField;
    @FXML private TextField transportField;
    @FXML private TextField voyageField;
    @FXML private TextField impotsField;
    @FXML private TextField autresField;

    private Expense createdExpense;

    public Expense getCreatedExpense() {
        return createdExpense;
    }

    /**
     * Active le bouton Ajouter uniquement si la période est renseignée.
     */
    public void bindValidation(Button addButton) {
        Runnable validate = () -> {
            boolean valid = periodeDatePicker.getValue() != null;
            addButton.setDisable(!valid);
        };

        periodeDatePicker.setOnAction(e -> validate.run());
        validate.run(); // initialisation
    }

    /**
     * Crée un objet Expense à partir des champs (avec 0 pour les champs vides).
     */
    public void createExpense() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String periode = periodeDatePicker.getValue().format(formatter); // Format: "2025-06"
        
        double logement = parse(logementField, "logement");
        double nourriture = parse(nourritureField, "nourriture");
        double sorties = parse(sortiesField, "sorties");
        double transport = parse(transportField, "transport");
        double voyage = parse(voyageField, "voyage");
        double impots = parse(impotsField, "impots");
        double autres = parse(autresField, "autres");

        double total = logement + nourriture + sorties + transport + voyage + impots + autres;

        createdExpense = new Expense(
                periode,
                total,
                logement,
                nourriture,
                sorties,
                transport,
                voyage,
                impots,
                autres
        );

        logger.info("Nouvelle dépense créée pour la période '{}', total = {}", periode, total);
    }

    /**
     * Pré-remplit les champs avec les données d'une dépense existante (pour l'édition).
     */
    public void setExpense(Expense expense) {
        if (expense != null) {
            // Convertir la période string vers LocalDate
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                LocalDate date = LocalDate.parse(expense.getPeriode() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                periodeDatePicker.setValue(date);
            } catch (Exception e) {
                logger.warn("Impossible de parser la période '{}', utilisation de la date actuelle", expense.getPeriode());
                periodeDatePicker.setValue(LocalDate.now());
            }
            
            logementField.setText(String.valueOf(expense.getLogement()));
            nourritureField.setText(String.valueOf(expense.getNourriture()));
            sortiesField.setText(String.valueOf(expense.getSorties()));
            transportField.setText(String.valueOf(expense.getTransport()));
            voyageField.setText(String.valueOf(expense.getVoyage()));
            impotsField.setText(String.valueOf(expense.getImpots()));
            autresField.setText(String.valueOf(expense.getAutres()));
            
            logger.info("Champs pré-remplis avec les données de la dépense : {}", expense.getPeriode());
        }
    }

    private double parse(TextField tf, String label) {
        try {
            return Double.parseDouble(tf.getText());
        } catch (NumberFormatException e) {
            logger.warn("Champ '{}' invalide ou vide. Remplacé par 0.", label);
            return 0;
        }
    }
}
