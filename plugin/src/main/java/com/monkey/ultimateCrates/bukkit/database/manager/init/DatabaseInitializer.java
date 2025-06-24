package com.monkey.ultimateCrates.bukkit.database.manager.init;

import com.monkey.ultimateCrates.bukkit.UltimateCrates;
import com.monkey.ultimateCrates.bukkit.database.config.DatabaseConfig;
import com.monkey.ultimateCrates.bukkit.database.func.sstorage.interf.CrateStatisticStorage;
import com.monkey.ultimateCrates.bukkit.database.func.sstorage.dbs.MySQLCrateStatisticStorage;
import com.monkey.ultimateCrates.bukkit.database.func.sstorage.dbs.SQLiteCrateStatisticStorage;
import com.monkey.ultimateCrates.bukkit.database.func.vkeys.dbs.MySQLVirtualKeyStorage;
import com.monkey.ultimateCrates.bukkit.database.func.vkeys.dbs.SQLiteVirtualKeyStorage;
import com.monkey.ultimateCrates.bukkit.database.func.vkeys.interf.VirtualKeyStorage;

import java.sql.Connection;
import java.util.logging.Logger;

public class DatabaseInitializer {

    private final UltimateCrates plugin;
    private final DatabaseConfig databaseConfig;
    private final Logger logger;

    private Connection connection;
    private VirtualKeyStorage virtualKeyStorage;
    private CrateStatisticStorage crateStatisticStorage;

    public DatabaseInitializer(UltimateCrates plugin, DatabaseConfig config, Logger logger) {
        this.plugin = plugin;
        this.databaseConfig = config;
        this.logger = logger;
    }

    public boolean initialize() {
        connection = databaseConfig.connect();
        if (connection == null) {
            logger.severe("Impossibile stabilire connessione al database");
            return false;
        }

        String dbType = plugin.getConfig().getString("db_central.type", "sqlite").toLowerCase();

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
        return true;
    }

    public Connection getConnection() {
        return connection;
    }

    public VirtualKeyStorage getVirtualKeyStorage() {
        return virtualKeyStorage;
    }

    public CrateStatisticStorage getCrateStatisticStorage() {
        return crateStatisticStorage;
    }
}
