package com.monkey.ultimateCrates.util;

import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KeyUtils {

    public static ItemStack createPhysicalKey(Crate crate, int amount) {
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK, amount);

        NBTItem nbtKey = new NBTItem(key);
        nbtKey.setString("crate_key", crate.getId());

        ItemMeta meta = nbtKey.getItem().getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', crate.getKeyName()));

            if (crate.isEnchanted()) {
                meta.addEnchant(Enchantment.MENDING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            nbtKey.getItem().setItemMeta(meta);
        }

        return nbtKey.getItem();
    }
}
