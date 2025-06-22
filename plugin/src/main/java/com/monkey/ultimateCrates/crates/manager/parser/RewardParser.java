package com.monkey.ultimateCrates.crates.manager.parser;

import org.bukkit.configuration.ConfigurationSection;

public class RewardParser {

    public record RewardData(int every, String item, int amount) {}

    public static RewardData parseReward(ConfigurationSection crateSection) {
        ConfigurationSection rewardSection = crateSection.getConfigurationSection("reward");
        int rewardEvery = 0;
        String rewardItem = null;
        int rewardAmount = 0;

        if (rewardSection != null) {
            rewardEvery = rewardSection.getInt("every", 0);
            ConfigurationSection prizeConf = rewardSection.getConfigurationSection("prizeconf");
            if (prizeConf != null) {
                rewardItem = prizeConf.getString("item", null);
                rewardAmount = prizeConf.getInt("amount", 1);
            }
        }
        return new RewardData(rewardEvery, rewardItem, rewardAmount);
    }
}
