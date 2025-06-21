package com.monkey.ultimateCrates.database;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.database.impl.*;

import java.sql.Connection;
import java.util.logging.Logger;

public class DatabaseManager {

    private final UltimateCrates plugin;
    private final Logger logger;

    private final DatabaseConfig databaseConfig;
    private Connection connection;
    private VirtualKeyStorage virtualKeyStorage;
    private CrateStatisticStorage crateStatisticStorage;

    public DatabaseManager(UltimateCrates plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.databaseConfig = new DatabaseConfig(plugin);
    }

    public void setup() {
        connection = databaseConfig.connect();
        if (connection == null) {
            logger.severe("Impossibile stabilire connessione al database");
            return;
        }

        String dbType = plugin.getConfig().getString("db_virtual.type", "sqlite").toLowerCase();

        if (dbType.equals("mysql")) {
            virtualKeyStorage = new MySQLVirtualKeyStorage(connection, logger);
            crateStatisticStorage = new MySQLCrateStatisticStorage(connection, logger);
        } else {
            virtualKeyStorage = new SQLiteVirtualKeyStorage(connection, logger);
            crateStatisticStorage = new SQLiteCrateStatisticStorage(connection, logger);
        }

        virtualKeyStorage.createTable();
        crateStatisticStorage.createTable();

        logger.info("Database " + dbType.toUpperCase() + " inizializzato correttamente.");
    }

    public Connection getConnection() {
        return connection;
    }

    public VirtualKeyStorage getVirtualKeyStorage() {
        return virtualKeyStorage;
    }

    public void close() {
        databaseConfig.close();
    }

    public CrateStatisticStorage getCrateStatisticStorage() {
        return crateStatisticStorage;
    }
}
