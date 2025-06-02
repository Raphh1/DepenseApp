package com.example.demo1.dao;

import com.example.demo1.model.Expense;
import com.example.demo1.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

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
        } catch (SQLException e) {
            e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}