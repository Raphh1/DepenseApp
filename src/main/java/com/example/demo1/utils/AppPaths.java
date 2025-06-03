package com.example.demo1.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppPaths {

    private static final Logger logger = LoggerFactory.getLogger(AppPaths.class);

    public static Path getDatabasePath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        Path dbPath;

        if (os.contains("win")) {
            dbPath = Paths.get(System.getenv("APPDATA"), "DemoApp", "data.db");
        } else if (os.contains("mac")) {
            dbPath = Paths.get(userHome, "Library", "Application Support", "DemoApp", "data.db");
        } else {
            dbPath = Paths.get(userHome, ".demoapp", "data.db");
        }

        try {
            Files.createDirectories(dbPath.getParent());
            logger.info("Répertoire de base de données créé ou déjà existant : {}", dbPath.getParent());
        } catch (IOException e) {
            logger.error("Échec de création du répertoire : {}", dbPath.getParent(), e);
        }

        logger.info("Chemin final de la base de données : {}", dbPath);
        return dbPath;
    }
}
