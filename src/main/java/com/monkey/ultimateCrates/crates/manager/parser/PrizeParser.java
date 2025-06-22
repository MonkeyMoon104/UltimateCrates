package com.monkey.ultimateCrates.crates.manager.parser;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrizeParser {

    @SuppressWarnings("unchecked")
    public static List<ItemStack> parsePrizes(ConfigurationSection crateSection) {
        List<ItemStack> prizes = new ArrayList<>();
        if (crateSection.isList("prizes")) {
            for (Map<?, ?> map : crateSection.getMapList("prizes")) {
                String typeStr = (String) map.get("type");
                if (typeStr == null) continue;

                Material material = Material.getMaterial(typeStr.toUpperCase());
                if (material == null) continue;

                int amount = map.get("amount") instanceof Number ? ((Number) map.get("amount")).intValue() : 1;
                ItemStack item = new ItemStack(material, amount);

                if (map.containsKey("name") || map.containsKey("lore")) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        if (map.containsKey("name")) {
                            meta.setDisplayName(((String) map.get("name")).replace("&", "§"));
                        }
                        if (map.containsKey("lore")) {
                            List<String> rawLore = (List<String>) map.get("lore");
                            List<String> coloredLore = new ArrayList<>();
                            for (String line : rawLore) {
                                coloredLore.add(line.replace("&", "§"));
                            }
                            meta.setLore(coloredLore);
                        }
                        item.setItemMeta(meta);
                    }
                }
                prizes.add(item);
            }
        }
        return prizes;
    }
}
