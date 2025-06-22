package com.monkey.ultimateCrates.crates.model;

import org.bukkit.inventory.ItemStack;

public class CratePrize {

    private final ItemStack item;
    private final double chance;

    public CratePrize(ItemStack item, double chance) {
        this.item = item;
        this.chance = chance;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getChance() {
        return chance;
    }
}
