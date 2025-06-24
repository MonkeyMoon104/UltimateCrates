package com.monkey.ultimateCrates.events.manager.data;

import com.monkey.ultimateCrates.events.helper.KeyHuntEvent;
import com.monkey.ultimateCrates.events.helper.StatsHuntEvent;
import com.monkey.ultimateCrates.events.helper.TreasureHuntEvent;
import com.monkey.ultimateCrates.events.manager.executor.CrateEventsExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CrateEventsData {

    private final boolean eventsEnabled;
    private final boolean randomSelectEnabled;
    private final int delayMinutes;
    private final List<String> selectedActiveEvents;
    private final ConfigurationSection eventsListSection;
    private final Map<String, TreasureHuntEvent> treasureHunts;
    private final Map<String, KeyHuntEvent> keyHunts;
    private final Map<String, StatsHuntEvent> statsHunts;
    private BukkitTask eventTask;

    public CrateEventsData(boolean eventsEnabled, boolean randomSelectEnabled, int delayMinutes,
                           List<String> selectedActiveEvents, ConfigurationSection eventsListSection,
                           Map<String, TreasureHuntEvent> treasureHunts,
                           Map<String, KeyHuntEvent> keyHunts,
                           Map<String, StatsHuntEvent> statsHunts) {
        this.eventsEnabled = eventsEnabled;
        this.randomSelectEnabled = randomSelectEnabled;
        this.delayMinutes = delayMinutes;
        this.selectedActiveEvents = selectedActiveEvents;
        this.eventsListSection = eventsListSection;
        this.treasureHunts = treasureHunts;
        this.keyHunts = keyHunts;
        this.statsHunts = statsHunts;
    }

    public void scheduleRandomEvent(Plugin plugin) {
        if (!eventsEnabled || !randomSelectEnabled || selectedActiveEvents.isEmpty()) return;
        if (eventTask != null && !eventTask.isCancelled()) return;

        eventTask = new BukkitRunnable() {
            @Override
            public void run() {
                eventTask = null;
                CrateEventsExecutor.executeRandomEvent(CrateEventsData.this, plugin);
            }
        }.runTaskLater(plugin, delayMinutes * 60L * 20L);
    }

    public void cancelScheduledEvent() {
        if (eventTask != null && !eventTask.isCancelled()) {
            eventTask.cancel();
            eventTask = null;
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

    public Optional<String> getPlayerAnimationForEvent(String eventName) {
        if (eventName == null || eventName.isEmpty()) return Optional.empty();
        if (eventsListSection == null) return Optional.empty();
        return Optional.ofNullable(eventsListSection.getString(eventName + ".player_animation"));
    }

    public Optional<String> getCratesAnimationForEvent(String eventName) {
        if (eventName == null || eventName.isEmpty()) return Optional.empty();
        if (eventsListSection == null) return Optional.empty();
        return Optional.ofNullable(eventsListSection.getString(eventName + ".open_animation"));
    }
}
