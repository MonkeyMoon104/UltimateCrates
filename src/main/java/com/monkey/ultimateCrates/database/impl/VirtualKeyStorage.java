package com.monkey.ultimateCrates.database.impl;

import java.util.Map;
import java.util.UUID;

public interface VirtualKeyStorage {

    void createTable();

    void giveKeys(UUID playerUUID, String crateId, int amount);

    int getKeys(UUID playerUUID, String crateId);

    void takeKeys(UUID playerUUID, String crateId, int amount);

    Map<String, Integer> getAllKeys(UUID playerUUID);

    boolean hasKeys(UUID playerUUID, String crateId, int requiredAmount);
}
