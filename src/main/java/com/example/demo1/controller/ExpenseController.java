package com.example.demo1.controller;

import com.example.demo1.dao.ExpenseDAO;
import com.example.demo1.model.Expense;
import com.example.demo1.model.Currency;
import com.example.demo1.utils.CurrencyManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ExpenseController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final CurrencyManager currencyManager = CurrencyManager.getInstance();

    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> periodeColumn;
    @FXML private TableColumn<Expense, Double> totalColumn;
    @FXML private TableColumn<Expense, Double> logementColumn;
    @FXML private TableColumn<Expense, Double> nourritureColumn;
    @FXML private TableColumn<Expense, Double> sortiesColumn;
    @FXML private TableColumn<Expense, Double> transportColumn;
    @FXML private TableColumn<Expense, Double> voyageColumn;
    @FXML private TableColumn<Expense, Double> impotsColumn;
    @FXML private TableColumn<Expense, Double> autresColumn;
    @FXML private TableColumn<Expense, Void> actionsColumn;
    @FXML private Button addButton;

    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();

    @FXML
    private void handleAddExpense() {
        try {
            logger.info("Ouverture du formulaire d'ajout de dépense");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/add-expense-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nouvelle dépense");
            dialog.setDialogPane(dialogPane);

            AddExpenseDialogController controller = loader.getController();

            ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().setAll(ajouterButtonType, ButtonType.CANCEL);

            Button addButton = (Button) dialog.getDialogPane().lookupButton(ajouterButtonType);
            controller.bindValidation(addButton);

            dialog.showAndWait().ifPresent(result -> {
                if (result == ajouterButtonType) {
                    controller.createExpense();
                    Expense e = controller.getCreatedExpense();
                    if (e != null) {
                        expenses.add(e);
                        ExpenseDAO.insert(e);
                        logger.info("Dépense ajoutée avec succès : {}", e);
                    }
                }
            });

        } catch (Exception e) {
            logger.error("Erreur lors de l'ouverture du formulaire", e);
            showError("Erreur lors de l'ouverture du formulaire", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        logger.warn("Affichage d'une alerte : {} - {}", title, message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleEditExpense(Expense expense) {
        try {
            logger.info("Ouverture du formulaire d'édition pour la dépense : {}", expense.getPeriode());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/add-expense-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Modifier la dépense");
            dialog.setDialogPane(dialogPane);

            AddExpenseDialogController controller = loader.getController();
            controller.setExpense(expense); // Pré-remplir avec les données existantes

            ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().setAll(modifierButtonType, ButtonType.CANCEL);

            Button modifyButton = (Button) dialog.getDialogPane().lookupButton(modifierButtonType);
            controller.bindValidation(modifyButton);

            dialog.showAndWait().ifPresent(result -> {
                if (result == modifierButtonType) {
                    controller.createExpense();
                    Expense modifiedExpense = controller.getCreatedExpense();
                    if (modifiedExpense != null) {
                        // Mettre à jour dans la liste observable
                        int index = expenses.indexOf(expense);
                        if (index >= 0) {
                            expenses.set(index, modifiedExpense);
                        }

                        // Mettre à jour en base de données
                        ExpenseDAO.update(modifiedExpense);
                        logger.info("Dépense modifiée avec succès : {}", modifiedExpense);
                    }
                }
            });

        } catch (Exception e) {
            logger.error("Erreur lors de l'édition de la dépense", e);
            showError("Erreur lors de l'édition", e.getMessage());
        }
    }

    private void handleDeleteExpense(Expense expense) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer cette dépense ?");
        confirmDialog.setContentText("Période : " + expense.getPeriode() + "\nTotal : " + expense.getTotal() + "€");

        confirmDialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    // Supprimer de la liste observable
                    expenses.remove(expense);

                    // Supprimer de la base de données
                    ExpenseDAO.delete(expense.getPeriode());
                    logger.info("Dépense supprimée avec succès : {}", expense.getPeriode());

                } catch (Exception e) {
                    logger.error("Erreur lors de la suppression de la dépense", e);
                    showError("Erreur lors de la suppression", e.getMessage());

                    // Recharger la liste en cas d'erreur
                    expenses.clear();
                    expenses.addAll(ExpenseDAO.findAll());
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initialisation du contrôleur ExpenseController");
        
        // Configuration des colonnes avec formatage en devises
        periodeColumn.setCellValueFactory(new PropertyValueFactory<>("periode"));
        
        // Configuration des colonnes monétaires avec formatage selon la devise sélectionnée
        setupCurrencyColumn(totalColumn, "total");
        setupCurrencyColumn(logementColumn, "logement");
        setupCurrencyColumn(nourritureColumn, "nourriture");
        setupCurrencyColumn(sortiesColumn, "sorties");
        setupCurrencyColumn(transportColumn, "transport");
        setupCurrencyColumn(voyageColumn, "voyage");
        setupCurrencyColumn(impotsColumn, "impots");
        setupCurrencyColumn(autresColumn, "autres");
        
        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(col -> {
            return new TableCell<Expense, Void>() {
                private final Button editButton = new Button("✏️");
                private final Button deleteButton = new Button("🗑️");
                private final javafx.scene.layout.HBox actionBox = new javafx.scene.layout.HBox(5, editButton, deleteButton);
                
                {
                    editButton.getStyleClass().add("btn-edit");
                    deleteButton.getStyleClass().add("btn-delete");
                    editButton.setOnAction(e -> {
                        Expense expense = getTableView().getItems().get(getIndex());
                        handleEditExpense(expense);
                    });
                    deleteButton.setOnAction(e -> {
                        Expense expense = getTableView().getItems().get(getIndex());
                        handleDeleteExpense(expense);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(actionBox);
                    }
                }
            };
        });
        
        expenseTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Charger les données
        try {
            expenses.addAll(ExpenseDAO.findAll());
            logger.info("Dépenses chargées depuis la base de données");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des dépenses", e);
        }

        expenseTable.setItems(expenses);
        
        // Écouter les changements de devise pour mettre à jour l'affichage
        currencyManager.currentCurrencyProperty().addListener((obs, oldCurrency, newCurrency) -> {
            logger.info("Changement de devise détecté: {} -> {}", oldCurrency, newCurrency);
            expenseTable.refresh(); // Force le recalcul des cellules
        });
        
        // Charger les taux de change
        currencyManager.loadExchangeRates();
    }
    
    private void setupCurrencyColumn(TableColumn<Expense, Double> column, String property) {
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setCellFactory(col -> new TableCell<Expense, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Convertir de EUR vers la devise actuelle
                    Currency currentCurrency = currencyManager.getCurrentCurrency();
                    double convertedAmount = currencyManager.convertAmount(item, Currency.EUR, currentCurrency);
                    setText(currentCurrency.format(convertedAmount));
                }
            }
        });
    }
}
