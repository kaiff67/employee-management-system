package com.company.ems.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Reads the local database settings and opens JDBC connections. */
public final class DatabaseConnection {
    private static final Properties SETTINGS = loadSettings();

    private DatabaseConnection() {
    }

    private static Properties loadSettings() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "Missing db.properties. Copy db.properties.example to db.properties and set your MySQL password.");
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read db.properties.", exception);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = SETTINGS.getProperty("db.url");
        String username = SETTINGS.getProperty("db.username");
        String password = SETTINGS.getProperty("db.password");
        if (url == null || username == null || password == null) {
            throw new IllegalStateException("db.properties must contain db.url, db.username, and db.password.");
        }
        return DriverManager.getConnection(url, username, password);
    }
}
