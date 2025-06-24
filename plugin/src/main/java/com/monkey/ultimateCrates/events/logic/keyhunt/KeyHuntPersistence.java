package com.monkey.ultimateCrates.events.logic.keyhunt;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class KeyHuntPersistence {

    public static void markBlockWithNBT(Block block, Crate crate) {
        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setString("ultimatecrates_keyhunt", crate.getId());
    }

    public static void save(Location location, Crate crate) {
        UltimateCrates.getInstance().getEventsDatabaseFunctions().saveKeyHuntChest(
                location.getWorld().getName(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                crate.getId()
        );
    }

    public static void clearBlock() {
        Location loc = KeyHuntState.getLocation();
        if (loc != null) {
            Block block = loc.getBlock();
            if (block.getType() == Material.ENDER_CHEST) {
                block.setType(Material.AIR);
            }
        }
        UltimateCrates.getInstance().getEventsDatabaseFunctions().clearKeyHuntChest();
    }
}
