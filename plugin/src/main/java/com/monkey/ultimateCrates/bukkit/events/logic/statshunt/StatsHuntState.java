package com.monkey.ultimateCrates.bukkit.events.logic.statshunt;

import com.monkey.ultimateCrates.bukkit.events.helper.StatsHuntEvent;
import org.bukkit.Location;

public class StatsHuntState {

    private static StatsHuntEvent currentEvent;
    private static Location currentLocation;
    private static boolean running = false;

    public static void set(StatsHuntEvent event, Location location) {
        currentEvent = event;
        currentLocation = location;
        running = true;
    }

    public static void reset() {
        currentEvent = null;
        currentLocation = null;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean state) {
        running = state;
    }

    public static StatsHuntEvent getCurrentEvent() {
        return currentEvent;
    }

    public static Location getCurrentStatsHuntChestLocation() {
        return currentLocation;
    }
}
