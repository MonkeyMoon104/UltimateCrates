package com.monkey.ultimateCrates.database.manager;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.database.manager.init.DatabaseInitializer;
import com.monkey.ultimateCrates.database.config.DatabaseConfig;
import com.monkey.ultimateCrates.database.func.sstorage.interf.CrateStatisticStorage;
import com.monkey.ultimateCrates.database.func.vkeys.interf.VirtualKeyStorage;

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
        var initializer = new DatabaseInitializer(plugin, databaseConfig, logger);
        if (!initializer.initialize()) return;

        this.connection = initializer.getConnection();
        this.virtualKeyStorage = initializer.getVirtualKeyStorage();
        this.crateStatisticStorage = initializer.getCrateStatisticStorage();
    }

    public void close() {
        databaseConfig.close();
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
