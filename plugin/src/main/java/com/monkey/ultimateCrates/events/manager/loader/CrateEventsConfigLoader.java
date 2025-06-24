package com.monkey.ultimateCrates.events.manager.loader;

import com.monkey.ultimateCrates.events.helper.KeyHuntEvent;
import com.monkey.ultimateCrates.events.helper.StatsHuntEvent;
import com.monkey.ultimateCrates.events.helper.TreasureHuntEvent;
import com.monkey.ultimateCrates.events.manager.data.CrateEventsData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class CrateEventsConfigLoader {

    public static CrateEventsData load(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("events");

        if (section == null) {
            return new CrateEventsData(false, false, 0, Collections.emptyList(), null,
                    new HashMap<>(), new HashMap<>(), new HashMap<>());
        }

        boolean enabled = section.getBoolean("enabled", false);
        int delay = section.getInt("delay", 5);

        ConfigurationSection random = section.getConfigurationSection("random_select_event");
        boolean randomEnabled = random != null && random.getBoolean("enabled", false);
        List<String> selected = random != null ? random.getStringList("selected_active_events") : Collections.emptyList();

        ConfigurationSection list = section.getConfigurationSection("list");
        Map<String, TreasureHuntEvent> treasure = new HashMap<>();
        Map<String, KeyHuntEvent> key = new HashMap<>();
        Map<String, StatsHuntEvent> stats = new HashMap<>();

        if (list != null) {
            for (String keyName : list.getKeys(false)) {
                ConfigurationSection eSection = list.getConfigurationSection(keyName);
                if (eSection == null) continue;

                switch (keyName) {
                    case "treasure_hunt" -> {
                        String crateId = eSection.getString("crate_id");
                        if (crateId != null) treasure.put(keyName, new TreasureHuntEvent(crateId));
                    }
                    case "key_hunt" -> {
                        String k = eSection.getString("key_name");
                        int amount = eSection.getInt("amount", 1);
                        if (k != null) key.put(keyName, new KeyHuntEvent(k, amount));
                    }
                    case "stats_hunt" -> {
                        List<String> crateIds = eSection.getStringList("stats_increment");
                        int inc = eSection.getInt("increment_amount");
                        if (!crateIds.isEmpty()) stats.put(keyName, new StatsHuntEvent(crateIds, inc));
                    }
                }
            }
        }

        return new CrateEventsData(enabled, randomEnabled, delay, selected, list, treasure, key, stats);
    }
}
