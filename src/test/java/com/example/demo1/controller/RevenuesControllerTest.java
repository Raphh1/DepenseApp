package com.example.demo1.controller;

import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class RevenuesControllerTest extends ApplicationTest {

    private RevenuesController controller;

    @BeforeEach
    public void setUp() {
        controller = new RevenuesController();
    }

    @Test
    public void testRevenuesTableInitialization() {
        TableView<?> table = controller.revenuesTable;
        assertNotNull(table, "La table des revenus doit être initialisée.");
    }

    @Test
    public void testOpenAddRevenueDialog() {
        assertDoesNotThrow(() -> controller.openAddRevenueDialog(), "La méthode pour ouvrir la fenêtre modale ne doit pas lever d'exception.");
    }
}
