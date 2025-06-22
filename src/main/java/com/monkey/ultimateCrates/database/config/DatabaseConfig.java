package com.monkey.ultimateCrates.database.config;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.database.config.provider.list.MySQLProvider;
import com.monkey.ultimateCrates.database.config.provider.list.SQLiteProvider;
import com.monkey.ultimateCrates.database.config.provider.interf.DatabaseProvider;

import java.sql.Connection;

public class DatabaseConfig {

    private final UltimateCrates plugin;
    private Connection connection;

    public DatabaseConfig(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public Connection connect() {
        String dbType = plugin.getConfig().getString("db_central.type", "sqlite").toLowerCase();
        DatabaseProvider provider;

        if (dbType.equals("mysql")) {
            provider = new MySQLProvider(plugin);
        } else {
            provider = new SQLiteProvider(plugin);
        }

        connection = provider.connect();
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("Connessione database chiusa.");
            } catch (Exception e) {
                plugin.getLogger().warning("Errore durante la chiusura della connessione: " + e.getMessage());
            }
        }
    }
}
