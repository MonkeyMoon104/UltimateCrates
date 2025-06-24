package com.monkey.ultimateCrates.events.logic.keyhunt;

import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.events.helper.KeyHuntEvent;
import org.bukkit.Location;

public class KeyHuntState {

    private static Location currentLocation;
    private static Crate currentCrate;
    private static KeyHuntEvent currentEvent;
    private static boolean running = false;

    public static void set(Crate crate, KeyHuntEvent event, Location location) {
        currentCrate = crate;
        currentEvent = event;
        currentLocation = location;
        running = true;
    }

    public static void reset() {
        currentCrate = null;
        currentEvent = null;
        currentLocation = null;
    }

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean state) {
        running = state;
    }

    public static Location getLocation() {
        return currentLocation;
    }

    public static Crate getCrate() {
        return currentCrate;
    }

    public static KeyHuntEvent getEvent() {
        return currentEvent;
    }
}
