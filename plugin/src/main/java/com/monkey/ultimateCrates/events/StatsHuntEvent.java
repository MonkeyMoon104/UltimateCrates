package com.monkey.ultimateCrates.events;

import org.bukkit.Material;
import java.util.List;

public class StatsHuntEvent {
    private final Material block;
    private final List<String> crateIds;

    public StatsHuntEvent(Material block, List<String> crateIds) {
        this.block = block;
        this.crateIds = crateIds;
    }

    public Material getBlock() {
        return block;
    }

    public List<String> getCrateIds() {
        return crateIds;
    }
}
