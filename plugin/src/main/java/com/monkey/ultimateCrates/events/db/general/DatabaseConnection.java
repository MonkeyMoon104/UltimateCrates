package com.monkey.ultimateCrates.events.db.general;

import com.monkey.ultimateCrates.UltimateCrates;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection createConnection() {
        try {
            File storageFolder = new File(UltimateCrates.getInstance().getDataFolder(), "storage");
            if (!storageFolder.exists()) {
                storageFolder.mkdirs();
            }

            File dbFile = new File(storageFolder, "events.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            return DriverManager.getConnection(url);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
