package com.example.demo1.db;

import com.example.demo1.utils.AppPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Path;

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static final String URL;

    static {
        Path dbPath = AppPaths.getDatabasePath(); // Récupération du chemin dynamique
        URL = "jdbc:sqlite:" + dbPath.toString();
        logger.info("Chemin de la base de données SQLite : {}", dbPath);
    }

    public static Connection getConnection() throws SQLException {
        logger.debug("Connexion à la base de données SQLite en cours...");
        return DriverManager.getConnection(URL);
    }

    public static boolean initialize() {
        try (Connection conn = getConnection()) {
            var stmt = conn.createStatement();
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS expenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    periode TEXT,
                    total REAL,
                    logement REAL,
                    nourriture REAL,
                    sorties REAL,
                    transport REAL,
                    voyage REAL,
                    impots REAL,
                    autres REAL
                )
            """);
            logger.info("Base de données initialisée avec succès.");
            return true;
        } catch (SQLException e) {
            logger.error("Erreur lors de l'initialisation de la base de données", e);
            return false;
        }
    }
}
