package com.example.demo1.controller;

import com.example.demo1.model.Currency;
import com.example.demo1.utils.CurrencyManager;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CurrencySwitch extends HBox {

    private final CurrencyManager currencyManager = CurrencyManager.getInstance();
    private final Currency[] currencies = Currency.values();
    private int currentIndex = 0;
    private final Button switchToggle;
    private final Rectangle switchThumb;
    private final Label[] currencyLabels;

    public CurrencySwitch() {
        super(15);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5));
        getStyleClass().add("currency-switch");

        // Conteneur principal du switch
        HBox switchContainer = new HBox();
        switchContainer.getStyleClass().add("currency-switch-container");
        switchContainer.setPrefWidth(220);
        switchContainer.setPrefHeight(35);
        switchContainer.setAlignment(Pos.CENTER_LEFT);

        // Background du switch
        Rectangle switchBackground = new Rectangle(220, 35);
        switchBackground.getStyleClass().add("currency-switch-background");
        switchBackground.setArcWidth(18);
        switchBackground.setArcHeight(18);

        // Thumb (bouton mobile)
        switchThumb = new Rectangle(42, 31);
        switchThumb.getStyleClass().add("currency-switch-thumb");
        switchThumb.setArcWidth(15);
        switchThumb.setArcHeight(15);
        switchThumb.setTranslateX(2);

        // Labels pour chaque devise
        currencyLabels = new Label[currencies.length];
        HBox labelsContainer = new HBox();
        labelsContainer.setPrefWidth(220);
        labelsContainer.setPrefHeight(35);
        labelsContainer.setAlignment(Pos.CENTER);
        labelsContainer.setSpacing(0);

        for (int i = 0; i < currencies.length; i++) {
            Label label = new Label(currencies[i].getSymbol());
            label.getStyleClass().add("currency-switch-label");
            label.setPrefWidth(220.0 / currencies.length);
            label.setAlignment(Pos.CENTER);
            label.setTooltip(new Tooltip(currencies[i].getName()));
            currencyLabels[i] = label;
            labelsContainer.getChildren().add(label);
        }

        // StackPane pour superposer les éléments
        StackPane switchPane = new StackPane();
        switchPane.getChildren().addAll(switchBackground, switchThumb, labelsContainer);

        // Bouton invisible pour capturer les clics
        switchToggle = new Button();
        switchToggle.setOpacity(0);
        switchToggle.setPrefSize(220, 35);
        switchToggle.setOnAction(e -> switchToNext());
        switchToggle.setTooltip(new Tooltip("Cliquer pour changer de devise"));

        StackPane buttonContainer = new StackPane();
        buttonContainer.getChildren().addAll(switchPane, switchToggle);

        getChildren().add(buttonContainer);

        // Initialiser l'affichage
        updateDisplay();

        // Écouter les changements de devise
        currencyManager.currentCurrencyProperty().addListener((obs, oldCurrency, newCurrency) -> {
            updateDisplay();
        });
    }

    private void switchToNext() {
        currentIndex = (currentIndex + 1) % currencies.length;
        currencyManager.setCurrentCurrency(currencies[currentIndex]);
        animateThumb();
    }

    private void animateThumb() {
        double thumbWidth = 42;
        double containerWidth = 220;
        double sectionWidth = containerWidth / currencies.length;
        double targetX = 2 + (currentIndex * sectionWidth) + (sectionWidth - thumbWidth) / 2;

        TranslateTransition transition = new TranslateTransition(Duration.millis(300), switchThumb);
        transition.setToX(targetX);
        transition.play();
    }

    private void updateDisplay() {
        Currency current = currencyManager.getCurrentCurrency();

        // Trouver l'index actuel
        for (int i = 0; i < currencies.length; i++) {
            if (currencies[i] == current) {
                currentIndex = i;
                break;
            }
        }

        // Mettre à jour les styles des labels
        for (int i = 0; i < currencyLabels.length; i++) {
            currencyLabels[i].getStyleClass().removeAll("active");
            if (i == currentIndex) {
                currencyLabels[i].getStyleClass().add("active");
            }
        }

        // Animer le thumb vers la bonne position
        animateThumb();
    }
}