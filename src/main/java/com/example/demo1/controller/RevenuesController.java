package com.example.demo1.controller;

import com.example.demo1.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.demo1.db.Database;
import com.example.demo1.model.Revenue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RevenuesController {

    private static final Logger logger = LoggerFactory.getLogger(RevenuesController.class);

    @FXML
    TableView<Revenue> revenuesTable;

    @FXML
    private TableColumn<Revenue, String> typeColumn;

    @FXML
    private TableColumn<Revenue, Double> amountColumn;

    @FXML
    private TableColumn<Revenue, String> dateColumn;

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        loadRevenues();
    }

    private void loadRevenues() {
        Database db = new Database();
        ObservableList<Revenue> revenues = FXCollections.observableArrayList();
        try (var resultSet = db.getRevenues()) {
            while (resultSet.next()) {
                revenues.add(new Revenue(
                        resultSet.getString("type"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("date")
                ));
            }
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des revenus", e);
        }
        revenuesTable.setItems(revenues);
    }

    @FXML
    public void openAddRevenueDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/add-revenue-dialog.fxml"));
            Parent root = loader.load();

            AddRevenueDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter un revenu");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Recharger les données après la fermeture de la fenêtre modale
            loadRevenues();
        } catch (IOException e) {
            logger.error("Erreur lors de l'ouverture de la fenêtre modale", e);
        }
    }

    @FXML
    public void goToDashboard() {
        SceneManager.switchTo("dashboard.fxml");
    }

    @FXML
    public void goToExpenses() {
        SceneManager.switchTo("expense.fxml");
    }
}
