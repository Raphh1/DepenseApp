package com.example.demo1;

import com.example.demo1.db.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlUrl = HelloApplication.class.getResource("expense.fxml");
        if (fxmlUrl == null) {
            throw new IOException("❌ Le fichier 'expense.fxml' est introuvable.");
        }

        if (!Database.initialize()) {
            System.err.println("Erreur de base de données. Fermeture.");
            Platform.exit();
            return;
        }


        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load());

        URL cssUrl = HelloApplication.class.getResource("CSS/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("⚠️ Le fichier 'style.css' est introuvable.");
        }

        URL dialogUrl = HelloApplication.class.getResource("/com/example/demo1/add-expense-dialog.fxml");
        if (dialogUrl == null) {
            System.err.println("⚠️ Le fichier 'add-expense-dialog.fxml' est introuvable.");
        } else {
            System.out.println("✅ Fichier 'add-expense-dialog.fxml' détecté.");
        }

        stage.setTitle("Suivi des Dépenses");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
