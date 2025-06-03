package com.example.demo1.dao;

import com.example.demo1.model.Expense;
import com.example.demo1.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseDAO.class);

    public static void insert(Expense expense) {
        String sql = """
            INSERT INTO expenses (periode, total, logement, nourriture, sorties, transport, voyage, impots, autres)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, expense.getPeriode());
            stmt.setDouble(2, expense.getTotal());
            stmt.setDouble(3, expense.getLogement());
            stmt.setDouble(4, expense.getNourriture());
            stmt.setDouble(5, expense.getSorties());
            stmt.setDouble(6, expense.getTransport());
            stmt.setDouble(7, expense.getVoyage());
            stmt.setDouble(8, expense.getImpots());
            stmt.setDouble(9, expense.getAutres());

            stmt.executeUpdate();
            logger.info("Dépense insérée en base : {}", expense);

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion d'une dépense", e);
        }
    }

    public static List<Expense> findAll() {
        List<Expense> result = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM expenses")) {

            while (rs.next()) {
                Expense e = new Expense(
                        rs.getString("periode"),
                        rs.getDouble("total"),
                        rs.getDouble("logement"),
                        rs.getDouble("nourriture"),
                        rs.getDouble("sorties"),
                        rs.getDouble("transport"),
                        rs.getDouble("voyage"),
                        rs.getDouble("impots"),
                        rs.getDouble("autres")
                );
                result.add(e);
            }

            logger.info("{} dépenses récupérées depuis la base de données", result.size());

        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des dépenses", e);
        }

        return result;
    }

    public static List<Expense> findByMonth(String month) {
        List<Expense> result = new ArrayList<>();

        String sql = "SELECT * FROM expenses WHERE periode = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, month);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Expense e = new Expense(
                        rs.getString("periode"),
                        rs.getDouble("total"),
                        rs.getDouble("logement"),
                        rs.getDouble("nourriture"),
                        rs.getDouble("sorties"),
                        rs.getDouble("transport"),
                        rs.getDouble("voyage"),
                        rs.getDouble("impots"),
                        rs.getDouble("autres")
                );
                result.add(e);
            }

            logger.info("{} dépenses récupérées pour le mois '{}'", result.size(), month);

        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des dépenses pour le mois {}", month, e);
        }

        return result;
    }

    public static void update(Expense expense) {
        String sql = """
            UPDATE expenses 
            SET total = ?, logement = ?, nourriture = ?, sorties = ?, transport = ?, voyage = ?, impots = ?, autres = ?
            WHERE periode = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, expense.getTotal());
            stmt.setDouble(2, expense.getLogement());
            stmt.setDouble(3, expense.getNourriture());
            stmt.setDouble(4, expense.getSorties());
            stmt.setDouble(5, expense.getTransport());
            stmt.setDouble(6, expense.getVoyage());
            stmt.setDouble(7, expense.getImpots());
            stmt.setDouble(8, expense.getAutres());
            stmt.setString(9, expense.getPeriode());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Dépense mise à jour : {}", expense);
            } else {
                logger.warn("Aucune dépense trouvée pour la période : {}", expense.getPeriode());
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour de la dépense", e);
        }
    }

    public static void delete(String periode) {
        String sql = "DELETE FROM expenses WHERE periode = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, periode);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Dépense supprimée pour la période : {}", periode);
            } else {
                logger.warn("Aucune dépense trouvée pour la période : {}", periode);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression de la dépense pour la période {}", periode, e);
        }
    }
}
