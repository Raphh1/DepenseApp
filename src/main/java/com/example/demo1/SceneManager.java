package com.example.demo1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SceneManager {

    private static final Logger logger = LoggerFactory.getLogger(SceneManager.class);

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        logger.info("Stage principal enregistré.");
    }

    public static void switchTo(String fxmlFile) {
        try {
            logger.info("Changement de scène vers : {}", fxmlFile);
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // Appliquer le CSS à chaque changement de scène
            var cssUrl = SceneManager.class.getResource("CSS/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                logger.info("CSS appliqué avec succès");
            } else {
                logger.warn("Fichier CSS non trouvé");
            }
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de la scène : {}", fxmlFile, e);
        }
    }
}
