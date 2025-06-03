package com.example.demo1.controller;

import com.example.demo1.db.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AddRevenueDialogController {

    @FXML
    private TextField typeField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    @FXML
    public void addRevenue() {
        String type = typeField.getText();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();

        if (type.isEmpty() || amountText.isEmpty() || date == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            Database db = new Database();
            db.insertRevenue(type, amount, date.toString());
            showAlert("Succès", "Revenu ajouté avec succès.");
            typeField.clear();
            amountField.clear();
            datePicker.setValue(null);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
