package com.monkey.ultimateCrates.bukkit.events.logic.keyhunt;

import com.monkey.ultimateCrates.bukkit.crates.model.Crate;
import com.monkey.ultimateCrates.bukkit.events.util.EventLocationUtils;
import com.monkey.ultimateCrates.bukkit.events.util.RegionUtils;
import org.bukkit.Location;
import org.bukkit.World;

public class KeyHuntLocationPicker {

    public static Location pickValidLocation(Crate crate) {
        World world = EventLocationUtils.getConfiguredWorld();
        for (int i = 0; i < 100; i++) {
            Location attempt = EventLocationUtils.getRandomLocationInsideWorldBorder(world);
            if (attempt != null && !RegionUtils.isInRegion(attempt)) {
                return attempt;
            }
        }
        return null;
    }
}
