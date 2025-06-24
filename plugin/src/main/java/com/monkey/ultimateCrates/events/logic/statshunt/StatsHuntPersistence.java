package com.monkey.ultimateCrates.events.logic.statshunt;

import com.monkey.ultimateCrates.UltimateCrates;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class StatsHuntPersistence {

    public static void markBlockWithNBT(Block block, String crateId) {
        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("ultimatecrates_stats_event", crateId);
    }

    public static void save(Location location, String crateId) {
        UltimateCrates.getInstance().getEventsDatabaseFunctions().saveStatsHuntChest(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                crateId
        );
    }

    public static void clearBlock() {
        Location loc = StatsHuntState.getCurrentStatsHuntChestLocation();
        if (loc != null) {
            Block block = loc.getBlock();
            if (block.getType() == Material.ENDER_CHEST) {
                block.setType(Material.AIR);
            }
        }
        UltimateCrates.getInstance().getEventsDatabaseFunctions().clearStatsHuntChests();
    }
}
