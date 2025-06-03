module com.example.demo1 {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // JDBC / SQLite
    requires java.sql;

    // Logging : SLF4J (API) + Log4j2 (impl)
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    
    // HTTP Client pour les taux de change
    requires java.net.http;
    
    // Jackson pour le parsing JSON
    requires com.fasterxml.jackson.databind;

    // Pour autoriser l'accès par FXMLLoader et la réflexion
    opens com.example.demo1 to javafx.fxml;
    opens com.example.demo1.controller to javafx.fxml;
    opens com.example.demo1.model to javafx.fxml;

    // Pour que d'autres modules puissent utiliser tes classes
    exports com.example.demo1;
    exports com.example.demo1.controller;
    exports com.example.demo1.model;
}
