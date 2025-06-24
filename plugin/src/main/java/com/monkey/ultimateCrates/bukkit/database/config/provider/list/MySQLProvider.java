package com.monkey.ultimateCrates.bukkit.database.config.provider.list;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.database.config.provider.interf.DatabaseProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQLProvider implements DatabaseProvider {

    private final UltimateCrates plugin;

    public MySQLProvider(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    @Override
    public Connection connect() {
        try {
            String host = plugin.getConfig().getString("db_central.host", "localhost");
            int port = plugin.getConfig().getInt("db_central.port", 3306);
            String database = plugin.getConfig().getString("db_central.database", "ultimatecrates");
            String username = plugin.getConfig().getString("db_central.username", "root");
            String password = plugin.getConfig().getString("db_central.password", "");

            String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&autoReconnect=true", host, port, database);
            Connection connection = DriverManager.getConnection(url, username, password);

            plugin.getLogger().info("Connessione MySQL stabilita.");
            return connection;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Errore nella connessione MySQL: ", e);
            return null;
        }
    }
}
