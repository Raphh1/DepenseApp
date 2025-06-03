package com.example.demo1.db;

import com.example.demo1.utils.AppPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static void createRevenuesTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS revenues (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    date TEXT NOT NULL
                )
            """);
            logger.info("Table 'revenues' créée ou déjà existante.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de la table 'revenues'", e);
        }
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
            createRevenuesTable(); // Appel pour créer la table 'revenues'
            logger.info("Base de données initialisée avec succès.");
            return true;
        } catch (SQLException e) {
            logger.error("Erreur lors de l'initialisation de la base de données", e);
            return false;
        }
    }

    public void insertRevenue(String type, double amount, String date) {
        String sql = "INSERT INTO revenues(type, amount, date) VALUES(?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'un revenu", e);
        }
    }

    public ResultSet getRevenues() {
        String sql = "SELECT * FROM revenues";
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des revenus", e);
            return null;
        }
    }
}
