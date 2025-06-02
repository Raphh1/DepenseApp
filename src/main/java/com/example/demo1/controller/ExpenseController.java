package com.example.demo1.controller;

import com.example.demo1.dao.ExpenseDAO;
import com.example.demo1.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class ExpenseController {

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
    @FXML private Button addButton;



    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();

    @FXML
    private void handleAddExpense() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/add-expense-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nouvelle dépense");
            dialog.setDialogPane(dialogPane);

            AddExpenseDialogController controller = loader.getController();

            // 👇 IMPORTANT : récupère le vrai bouton avec son texte
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

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture du formulaire", e.getMessage());
        }
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    public void initialize() {
        periodeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPeriode()));
        totalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotal()).asObject());
        logementColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getLogement()).asObject());
        nourritureColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getNourriture()).asObject());
        sortiesColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getSorties()).asObject());
        transportColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTransport()).asObject());
        voyageColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getVoyage()).asObject());
        impotsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getImpots()).asObject());
        autresColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAutres()).asObject());
        expenseTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        expenses.addAll(ExpenseDAO.findAll());




        expenseTable.setItems(expenses);
    }
}
