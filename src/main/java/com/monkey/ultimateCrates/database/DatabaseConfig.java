package com.monkey.ultimateCrates.database;

import com.monkey.ultimateCrates.UltimateCrates;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseConfig {

    private final UltimateCrates plugin;

    private Connection connection;

    public DatabaseConfig(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public Connection connect() {
        String dbType = plugin.getConfig().getString("db_virtual.type", "sqlite").toLowerCase();

        try {
            if (dbType.equals("mysql")) {
                String host = plugin.getConfig().getString("db_virtual.host", "localhost");
                int port = plugin.getConfig().getInt("db_virtual.port", 3306);
                String database = plugin.getConfig().getString("db_virtual.database", "ultimatecrates");
                String username = plugin.getConfig().getString("db_virtual.username", "root");
                String password = plugin.getConfig().getString("db_virtual.password", "");

                String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&autoReconnect=true", host, port, database);

                connection = DriverManager.getConnection(url, username, password);
                plugin.getLogger().info("Connessione MySQL stabilita.");
            } else {
                File dbFile = new File(plugin.getDataFolder(), "data.db");
                String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

                connection = DriverManager.getConnection(url);
                plugin.getLogger().info("Connessione SQLite stabilita.");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Errore nella connessione al database: ", e);
        }

        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Connessione database chiusa.");
            } catch (SQLException e) {
                plugin.getLogger().warning("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}
