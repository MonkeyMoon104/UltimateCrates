package com.monkey.ultimateCrates.bukkit.crates.model;

import java.util.List;

public class Crate {
    public enum KeyType {
        VIRTUAL, PHYSIC
    }

    private final String id;
    private final String displayName;
    private final List<String> hologramLines;
    private final List<String> animationTemplates;
    private final PCE particleEffectConfig;

    private final KeyType keyType;
    private final String keyName;
    private final boolean enchanted;

    private final int rewardEvery;
    private final String rewardItem;
    private final int rewardAmount;
    private final List<CratePrize> prizes;
    private final boolean canBuyEnabled;
    private final double canBuyCost;

    public Crate(String id, String displayName, List<String> hologramLines, List<String> animationTemplates, List<CratePrize> prizes, PCE particleEffectConfig, KeyType keyType, String keyName, boolean enchanted, int rewardEvery, String rewardItem, int rewardAmount, boolean canBuyEnabled, double canBuyCost) {
        this.id = id;
        this.displayName = displayName;
        this.hologramLines = hologramLines;
        this.animationTemplates = animationTemplates;
        this.prizes = prizes;
        this.particleEffectConfig = particleEffectConfig;
        this.keyType = keyType;
        this.keyName = keyName;
        this.enchanted = enchanted;
        this.rewardEvery = rewardEvery;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
        this.canBuyEnabled = canBuyEnabled;
        this.canBuyCost = canBuyCost;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getHologramLines() {
        return hologramLines;
    }

    public List<String> getAnimationTemplates() {
        return animationTemplates;
    }

    public List<CratePrize> getPrizes() {
        return prizes;
    }

    public PCE getParticleEffectConfig() {
        return particleEffectConfig;
    }
    public KeyType getKeyType() {
        return keyType;
    }

    public String getKeyName() {
        return keyName;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public int getRewardEvery() {
        return rewardEvery;
    }

    public String getRewardItem() {
        return rewardItem;
    }

    public int getRewardAmount() {
        return rewardAmount;
    }

    public boolean canBuyEnabled() {
        return canBuyEnabled;
    }

    public double getCanBuyCost() {
        return canBuyCost;
    }
}
