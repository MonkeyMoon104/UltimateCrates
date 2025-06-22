package com.monkey.ultimateCrates.database.config.provider.interf;

import java.sql.Connection;

public interface DatabaseProvider {
    Connection connect();
}
