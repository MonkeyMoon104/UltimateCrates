package com.monkey.ultimateCrates.database.impl;

import java.util.UUID;

public interface CrateOpenStorage {

    void createTable();

    void addOpen(UUID playerUUID, String crateId);

    int getOpenCount(UUID playerUUID, String crateId);
}
