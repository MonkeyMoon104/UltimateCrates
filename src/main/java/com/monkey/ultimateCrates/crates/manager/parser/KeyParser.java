package com.monkey.ultimateCrates.crates.manager.parser;

import com.monkey.ultimateCrates.crates.model.Crate;
import org.bukkit.configuration.ConfigurationSection;

public class KeyParser {

    public record KeyData(Crate.KeyType keyType, String keyName, boolean enchanted, boolean canBuyEnabled, double canBuyCost) {}

    public static KeyData parseKeys(ConfigurationSection crateSection) {
        ConfigurationSection keysSection = crateSection.getConfigurationSection("keys");
        Crate.KeyType keyType = Crate.KeyType.PHYSIC;
        String keyName = null;
        boolean enchanted = false;
        boolean canBuyEnabled = false;
        double canBuyCost = 0.0;

        if (keysSection != null) {
            String typeStr = keysSection.getString("type", "physic").toUpperCase();
            try {
                keyType = Crate.KeyType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Tipo key non valido per crate, imposto PHYSIC di default");
                keyType = Crate.KeyType.PHYSIC;
            }

            if (keyType == Crate.KeyType.PHYSIC) {
                keyName = keysSection.getString("name", "Crate Key");
            }
            enchanted = keysSection.getBoolean("enchanted", false);

            ConfigurationSection canBuySection = keysSection.getConfigurationSection("canbuy");
            if (canBuySection != null) {
                canBuyEnabled = canBuySection.getBoolean("enabled", false);
                canBuyCost = canBuySection.getDouble("cost", 0.0);
            }
        }
        return new KeyData(keyType, keyName, enchanted, canBuyEnabled, canBuyCost);
    }
}
