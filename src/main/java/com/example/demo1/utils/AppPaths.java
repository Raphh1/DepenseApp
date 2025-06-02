package com.example.demo1.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppPaths {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dbPath;
    }
}
