package com.monkey.ultimateCrates.crates.manager.loader;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.manager.parser.KeyParser;
import com.monkey.ultimateCrates.crates.manager.parser.PrizeParser;
import com.monkey.ultimateCrates.crates.manager.parser.RewardParser;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.PCE;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateLoader {

    private final UltimateCrates plugin;

    public CrateLoader(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public Map<String, Crate> load() {
        Map<String, Crate> crates = new HashMap<>();

        ConfigurationSection section = UltimateCrates.getInstance().getCratesConfigManager().getCratesSection();

        if (section == null) {
            plugin.getLogger().warning("Nessuna crate configurata in crates.yml");
            return crates;
        }

        for (String key : section.getKeys(false)) {
            ConfigurationSection crateSection = section.getConfigurationSection(key);
            if (crateSection == null) continue;

            String displayName = crateSection.getString("name", "Crate");
            List<String> hologram = crateSection.getStringList("hologram");
            List<String> animations = crateSection.getStringList("animations");

            PCE particleEffectConfig = parseParticleEffect(crateSection);

            List prizes = PrizeParser.parsePrizes(crateSection);
            Crate.KeyType keyType;
            String keyName;
            boolean enchanted;
            KeyParser.KeyData keyData = KeyParser.parseKeys(crateSection);
            keyType = keyData.keyType();
            keyName = keyData.keyName();
            enchanted = keyData.enchanted();

            RewardParser.RewardData rewardData = RewardParser.parseReward(crateSection);

            Crate crate = new Crate(key, displayName, hologram, animations, prizes, particleEffectConfig, keyType, keyName, enchanted, rewardData.every(), rewardData.item(), rewardData.amount(), keyData.canBuyEnabled(), keyData.canBuyCost());
            crates.put(key, crate);
        }

        return crates;
    }

    private PCE parseParticleEffect(ConfigurationSection crateSection) {
        ConfigurationSection particleSection = crateSection.getConfigurationSection("particle_effect");
        if (particleSection != null) {
            boolean enabled = particleSection.getBoolean("enabled", false);
            String type = particleSection.getString("effect", "heart");
            String effect = particleSection.getString("style", "sphere");
            return new PCE(enabled, type, effect);
        }
        return new PCE(false, "heart", "sphere");
    }
}
