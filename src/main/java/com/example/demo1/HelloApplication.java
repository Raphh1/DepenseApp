package com.example.demo1;

import com.example.demo1.db.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);



    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Démarrage de l'application...");

        SceneManager.setPrimaryStage(stage);

        if (!Database.initialize()) {
            logger.error("Échec de l'initialisation de la base de données. Fermeture de l'application.");
            Platform.exit();
            return;
        }

        stage.setTitle("Suivi des Dépenses");
        
        // Démarrer avec le tableau des dépenses
        SceneManager.switchTo("expense.fxml");
        
        logger.info("Fenêtre principale affichée.");
    }

    public static void main(String[] args) {
        launch();
    }
}
