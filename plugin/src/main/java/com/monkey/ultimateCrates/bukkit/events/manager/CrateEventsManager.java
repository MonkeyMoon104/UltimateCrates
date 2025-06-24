package com.monkey.ultimateCrates.bukkit.events.manager;

import com.monkey.ultimateCrates.bukkit.events.helper.KeyHuntEvent;
import com.monkey.ultimateCrates.bukkit.events.helper.StatsHuntEvent;
import com.monkey.ultimateCrates.bukkit.events.helper.TreasureHuntEvent;
import com.monkey.ultimateCrates.bukkit.events.manager.data.CrateEventsData;
import com.monkey.ultimateCrates.bukkit.events.manager.loader.CrateEventsConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class CrateEventsManager {

    private final CrateEventsData data;

    public CrateEventsManager(FileConfiguration config) {
        this.data = CrateEventsConfigLoader.load(config);
    }

    public void scheduleRandomEvent(Plugin plugin) {
        data.scheduleRandomEvent(plugin);
    }

    public void cancelScheduledEvent() {
        data.cancelScheduledEvent();
    }

    public boolean isEventsEnabled() {
        return data.isEventsEnabled();
    }

    public int getDelayMinutes() {
        return data.getDelayMinutes();
    }

    public List<String> getSelectedActiveEvents() {
        return data.getSelectedActiveEvents();
    }

    public Optional<TreasureHuntEvent> getTreasureHuntEvent() {
        return data.getTreasureHuntEvent();
    }

    public Optional<KeyHuntEvent> getKeyHuntEvent() {
        return data.getKeyHuntEvent();
    }

    public Optional<StatsHuntEvent> getStatsHuntEvent() {
        return data.getStatsHuntEvent();
    }

    public Optional<String> getRandomSelectedEvent() {
        return data.getRandomSelectedEvent();
    }

    public Optional<String> getPlayerAnimationForEvent(String eventName) {
        return data.getPlayerAnimationForEvent(eventName);
    }

    public Optional<String> getCratesAnimationForEvent(String eventName) {
        return data.getCratesAnimationForEvent(eventName);
    }
}
