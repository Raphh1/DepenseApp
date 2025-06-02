package com.example.demo1.controller;

import com.example.demo1.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddExpenseDialogController {

    @FXML private TextField periodeField;
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
            boolean valid = !periodeField.getText().isBlank();
            addButton.setDisable(!valid);
        };

        periodeField.textProperty().addListener((obs, oldVal, newVal) -> validate.run());
        validate.run(); // initialisation
    }

    /**
     * Crée un objet Expense à partir des champs (avec 0 pour les champs vides).
     */
    public void createExpense() {
        double logement = parse(logementField);
        double nourriture = parse(nourritureField);
        double sorties = parse(sortiesField);
        double transport = parse(transportField);
        double voyage = parse(voyageField);
        double impots = parse(impotsField);
        double autres = parse(autresField);

        double total = logement + nourriture + sorties + transport + voyage + impots + autres;

        createdExpense = new Expense(
                periodeField.getText(),
                total,
                logement,
                nourriture,
                sorties,
                transport,
                voyage,
                impots,
                autres
        );
    }

    private double parse(TextField tf) {
        try {
            return Double.parseDouble(tf.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
