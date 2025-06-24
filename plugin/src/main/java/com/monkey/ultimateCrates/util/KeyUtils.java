package com.monkey.ultimateCrates.util;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.util.FireworkUtil;
import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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

    public static void rewardFunc(Player player, Crate crate, int amount) {
        var plugin = UltimateCrates.getInstance();
        int rewardEvery = crate.getRewardEvery();
        if (rewardEvery > 0) {
            boolean rewardGiven = plugin.getDatabaseManager().getCrateStatisticStorage()
                    .checkRewardAndReset(player.getName(), crate.getId(), rewardEvery, amount);

            if (rewardGiven) {
                ItemStack rewardItem = parseRewardPrize(crate.getRewardItem(), crate.getRewardAmount());
                if (rewardItem != null) {
                    player.getInventory().addItem(rewardItem);
                    player.sendMessage(plugin.getMessagesManager().getMessage(
                            "messages.crate.reward_given",
                            rewardEvery,
                            crate.getDisplayName()
                    ));
                }

                boolean animationEnabled = plugin.getCratesConfigManager().getCratesConfig()
                        .getBoolean("crates." + crate.getId() + ".reward.animation.enabled", false);

                if (animationEnabled) {
                    int repeat = plugin.getCratesConfigManager().getCratesConfig()
                            .getInt("crates." + crate.getId() + ".reward.animation.repeat", 1);

                    FireworkUtil.startRewardFirework(player, plugin, repeat);
                }
            }
        }
    }

    private static ItemStack parseRewardPrize(String materialName, int amount) {
        if (materialName == null) return null;

        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) return null;

        return new ItemStack(material, amount);
    }
}
