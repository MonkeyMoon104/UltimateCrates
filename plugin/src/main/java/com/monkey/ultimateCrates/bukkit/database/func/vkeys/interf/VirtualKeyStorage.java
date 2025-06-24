package com.monkey.ultimateCrates.bukkit.database.func.vkeys.interf;

import java.util.Map;

public interface VirtualKeyStorage {

    void createTable();

    void giveKeys(String playerName, String crateId, int amount);

    int getKeys(String playerName, String crateId);

    void takeKeys(String playerName, String crateId, int amount);

    Map<String, Integer> getAllKeys(String playerName);

    void resetKeys(String playerName);

    void resetAllKeys();
}
