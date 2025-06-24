package com.monkey.ultimateCrates.events;

import com.monkey.ultimateCrates.events.handler.KeyHuntExecutor;
import com.monkey.ultimateCrates.events.handler.StatsHuntExecutor;
import com.monkey.ultimateCrates.events.handler.TreasureHuntExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CrateEventsManager {

    private final boolean eventsEnabled;
    private final boolean randomSelectEnabled;
    private final int delayMinutes;
    private final List<String> selectedActiveEvents;

    private final Map<String, TreasureHuntEvent> treasureHunts = new HashMap<>();
    private final Map<String, KeyHuntEvent> keyHunts = new HashMap<>();
    private final Map<String, StatsHuntEvent> statsHunts = new HashMap<>();
    private BukkitTask eventTask;

    public CrateEventsManager(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("events");
        if (section == null) {
            this.eventsEnabled = false;
            this.randomSelectEnabled = false;
            this.delayMinutes = 0;
            this.selectedActiveEvents = Collections.emptyList();
            return;
        }

        this.eventsEnabled = section.getBoolean("enabled", false);
        this.delayMinutes = section.getInt("delay", 5);
        ConfigurationSection randomSelect = section.getConfigurationSection("random_select_event");
        this.randomSelectEnabled = randomSelect != null && randomSelect.getBoolean("enabled", false);
        this.selectedActiveEvents = randomSelect != null
                ? randomSelect.getStringList("selected_active_events")
                : Collections.emptyList();

        ConfigurationSection listSection = section.getConfigurationSection("list");
        if (listSection != null) {
            for (String key : listSection.getKeys(false)) {
                ConfigurationSection eventSection = listSection.getConfigurationSection(key);
                if (eventSection == null) continue;

                switch (key) {
                    case "treasure_hunt":
                        String crateId = eventSection.getString("crate_id");
                        if (crateId != null) {
                            treasureHunts.put(key, new TreasureHuntEvent(crateId));
                        }
                        break;

                    case "key_hunt":
                        String keyName = eventSection.getString("key_name");
                        int amount = eventSection.getInt("amount", 1);
                        if (keyName != null) {
                            keyHunts.put(key, new KeyHuntEvent(keyName, amount));
                        }
                        break;

                    case "stats_hunt":
                        String materialName = eventSection.getString("block");
                        List<String> crateIds = eventSection.getStringList("stats_increment");

                        Material block = null;
                        try {
                            block = Material.valueOf(materialName);
                        } catch (Exception ignored) {}

                        if (block != null && !crateIds.isEmpty()) {
                            statsHunts.put(key, new StatsHuntEvent(block, crateIds));
                        }
                        break;
                }
            }
        }
    }

    public void scheduleRandomEvent(Plugin plugin) {
        if (!eventsEnabled || !randomSelectEnabled || selectedActiveEvents.isEmpty()) return;

        if (eventTask != null && !eventTask.isCancelled()) return;

        eventTask = new BukkitRunnable() {
            @Override
            public void run() {
                eventTask = null;
                executeRandomEvent(plugin);
            }
        }.runTaskLater(plugin, delayMinutes * 60L * 20L);
    }

    private void executeRandomEvent(Plugin plugin) {
        String selected = getRandomSelectedEvent().orElse(null);
        if (selected == null) return;

        Bukkit.getLogger().info("[UltimateCrates] Avvio evento: " + selected);

        switch (selected) {
            case "key_hunt":
                getKeyHuntEvent().ifPresent(event -> KeyHuntExecutor.start(event, delayMinutes));
                break;
            case "treasure_hunt":
                getTreasureHuntEvent().ifPresent(event -> TreasureHuntExecutor.start(event, delayMinutes));
                break;
        }
    }

    public boolean isEventsEnabled() {
        return eventsEnabled;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public List<String> getSelectedActiveEvents() {
        return selectedActiveEvents;
    }

    public Optional<TreasureHuntEvent> getTreasureHuntEvent() {
        return treasureHunts.values().stream().findFirst();
    }

    public Optional<KeyHuntEvent> getKeyHuntEvent() {
        return keyHunts.values().stream().findFirst();
    }

    public Optional<StatsHuntEvent> getStatsHuntEvent() {
        return statsHunts.values().stream().findFirst();
    }

    public Optional<String> getRandomSelectedEvent() {
        if (!randomSelectEnabled || selectedActiveEvents.isEmpty()) return Optional.empty();
        List<String> shuffled = new ArrayList<>(selectedActiveEvents);
        Collections.shuffle(shuffled);
        return Optional.of(shuffled.get(0));
    }

    public void cancelScheduledEvent() {
        if (eventTask != null && !eventTask.isCancelled()) {
            eventTask.cancel();
            eventTask = null;
        }
    }

}
