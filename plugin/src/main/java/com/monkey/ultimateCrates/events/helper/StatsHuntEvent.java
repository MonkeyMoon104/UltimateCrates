package com.monkey.ultimateCrates.events.helper;

import java.util.List;

public class StatsHuntEvent {
    private final List<String> crateIds;
    private final int amount;

    public StatsHuntEvent(List<String> crateIds, int amount) {
        this.crateIds = crateIds;
        this.amount = amount;
    }


    public List<String> getCrateIds() {
        return crateIds;
    }

    public int getAmount() {
        return amount;
    }
}
