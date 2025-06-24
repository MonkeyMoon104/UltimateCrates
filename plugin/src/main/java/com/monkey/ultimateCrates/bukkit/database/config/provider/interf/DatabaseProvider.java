package com.monkey.ultimateCrates.bukkit.database.config.provider.interf;

import java.sql.Connection;

public interface DatabaseProvider {
    Connection connect();
}
