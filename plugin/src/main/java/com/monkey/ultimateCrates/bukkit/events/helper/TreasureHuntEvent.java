package com.monkey.ultimateCrates.bukkit.events.helper;

public class TreasureHuntEvent {
    private final String crateId;

    public TreasureHuntEvent(String crateId) {
        this.crateId = crateId;
    }

    public String getCrateId() {
        return crateId;
    }
}
