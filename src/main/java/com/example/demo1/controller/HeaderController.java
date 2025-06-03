package com.example.demo1.controller;

import com.example.demo1.SceneManager;
import com.example.demo1.model.Currency;
import com.example.demo1.utils.CurrencyManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {
    
    @FXML
    private ComboBox<Currency> currencyComboBox;
    
    private final CurrencyManager currencyManager = CurrencyManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (currencyComboBox != null) {
            setupCurrencyComboBox();
        }
    }
    
    private void setupCurrencyComboBox() {
        // Ajouter toutes les devises au ComboBox
        currencyComboBox.getItems().addAll(Currency.values());
        
        // Définir la devise actuelle
        currencyComboBox.setValue(currencyManager.getCurrentCurrency());
        
        // Personnaliser l'affichage des éléments
        currencyComboBox.setCellFactory(new Callback<ListView<Currency>, ListCell<Currency>>() {
            @Override
            public ListCell<Currency> call(ListView<Currency> param) {
                return new ListCell<Currency>() {
                    @Override
                    protected void updateItem(Currency currency, boolean empty) {
                        super.updateItem(currency, empty);
                        if (empty || currency == null) {
                            setText(null);
                        } else {
                            setText(currency.getSymbol() + " " + currency.getName());
                        }
                    }
                };
            }
        });
        
        // Personnaliser l'affichage de l'élément sélectionné
        currencyComboBox.setButtonCell(new ListCell<Currency>() {
            @Override
            protected void updateItem(Currency currency, boolean empty) {
                super.updateItem(currency, empty);
                if (empty || currency == null) {
                    setText(null);
                } else {
                    setText(currency.getSymbol() + " " + currency.getCode());
                }
            }
        });
        
        // Écouter les changements de sélection
        currencyComboBox.setOnAction(e -> {
            Currency selectedCurrency = currencyComboBox.getValue();
            if (selectedCurrency != null) {
                currencyManager.setCurrentCurrency(selectedCurrency);
            }
        });
        
        // Écouter les changements du CurrencyManager
        currencyManager.currentCurrencyProperty().addListener((obs, oldCurrency, newCurrency) -> {
            if (!currencyComboBox.getValue().equals(newCurrency)) {
                currencyComboBox.setValue(newCurrency);
            }
        });
    }

    public void goToDashboard(ActionEvent event) {
        SceneManager.switchTo("dashboard.fxml");
    }

    public void goToExpenses(ActionEvent event) {
        SceneManager.switchTo("expense.fxml");
    }
}
