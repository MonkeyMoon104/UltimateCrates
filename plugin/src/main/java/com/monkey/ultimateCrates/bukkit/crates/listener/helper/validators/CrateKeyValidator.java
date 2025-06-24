package com.monkey.ultimateCrates.bukkit.crates.listener.helper.validators;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class CrateKeyValidator {

    public boolean isCrateKey(ItemStack item) {
        if (item == null) return false;

        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasTag("crate_key");
    }
}
