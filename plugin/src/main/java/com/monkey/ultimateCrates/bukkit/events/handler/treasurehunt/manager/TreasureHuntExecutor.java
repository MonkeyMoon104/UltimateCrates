package com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.manager;

import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.func.TreasureHuntClearHandler;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.func.TreasureHuntEndHandler;
import com.monkey.ultimateCrates.bukkit.events.handler.treasurehunt.helper.func.TreasureHuntStartHandler;
import com.monkey.ultimateCrates.bukkit.events.helper.TreasureHuntEvent;
import org.bukkit.Location;

public class TreasureHuntExecutor {

    private static Location currentTreasureLocation;
    private static Crate currentTreasureCrate;
    private static TreasureHuntEvent currentTreasureEvent;
    private static boolean running = false;

    public static void start(TreasureHuntEvent event, int durationMinutes) {
        if (running) return;
        TreasureHuntStartHandler.execute(event, durationMinutes);
    }

    public static void end(boolean foundByPlayer) {
        if (!running) return;
        TreasureHuntEndHandler.execute(foundByPlayer);
        running = false;
    }

    public static void clear() {
        TreasureHuntClearHandler.execute();
    }

    public static Location getCurrentTreasureLocation() {
        return currentTreasureLocation;
    }

    public static Crate getCurrentTreasureCrate() {
        return currentTreasureCrate;
    }

    public static boolean isRunning() {
        return running;
    }

    public static TreasureHuntEvent getCurrentTreasureEvent() {
        return currentTreasureEvent;
    }

    public static void setCurrentState(Location loc, Crate crate, TreasureHuntEvent event) {
        currentTreasureLocation = loc;
        currentTreasureCrate = crate;
        currentTreasureEvent = event;
        running = true;
    }

    public static void resetState() {
        currentTreasureLocation = null;
        currentTreasureCrate = null;
        currentTreasureEvent = null;
    }
}
