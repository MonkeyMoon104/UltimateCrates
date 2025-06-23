package com.monkey.ultimateCrates.events.util;

import com.monkey.ultimateCrates.UltimateCrates;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.Material;

import java.util.Random;

public class EventLocationUtils {

    private static final Random RANDOM = new Random();

    public static World getConfiguredWorld() {
        String worldName = UltimateCrates.getInstance().getConfig().getString("events.world", "world");
        World world = Bukkit.getWorld(worldName);
        return world != null ? world : Bukkit.getWorlds().get(0);
    }

    public static Location getRandomLocationInsideWorldBorder(World world) {
        WorldBorder border = world.getWorldBorder();
        Location center = border.getCenter();
        double size = border.getSize() / 2;

        for (int i = 0; i < 100; i++) {
            double x = center.getX() + (RANDOM.nextDouble() * 2 - 1) * size;
            double z = center.getZ() + (RANDOM.nextDouble() * 2 - 1) * size;
            Location loc = new Location(world, x, world.getHighestBlockYAt((int) x, (int) z), z);

            if (isSafeBlock(loc.getBlock())) return loc.add(0, 1, 0);
        }
        return null;
    }

    private static boolean isSafeBlock(Block block) {
        switch (block.getType()) {
            case AIR, GRASS_BLOCK, DIRT, STONE, SAND, SANDSTONE:
                return true;
            default:
                return false;
        }
    }
}
