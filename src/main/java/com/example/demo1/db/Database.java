package com.example.demo1.db;

import com.example.demo1.utils.AppPaths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Path;

public class Database {
    private static final String URL;

    static {
        Path dbPath = AppPaths.getDatabasePath(); // Récupération du chemin dynamique
        URL = "jdbc:sqlite:" + dbPath.toString();
    }

    public static Connection getConnection() throws SQLException {
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
