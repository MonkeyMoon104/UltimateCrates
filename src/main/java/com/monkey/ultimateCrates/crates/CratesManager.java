package com.monkey.ultimateCrates.crates;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import com.monkey.ultimateCrates.crates.model.ParticleEffectConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CratesManager {

    private final UltimateCrates plugin;
    private final Map<String, Crate> crates = new HashMap<>();

    public CratesManager(UltimateCrates plugin) {
        this.plugin = plugin;
    }

    public void loadCrates() {
        crates.clear();

        ConfigurationSection section = plugin.getConfigManager().getCratesConfig().getConfigurationSection("crates");
        if (section == null) {
            plugin.getLogger().warning("Nessuna crate configurata in crates.yml");
            return;
        }

        for (String key : section.getKeys(false)) {
            ConfigurationSection crateSection = section.getConfigurationSection(key);
            if (crateSection == null) continue;

            String displayName = crateSection.getString("name", "Crate");
            List<String> hologram = crateSection.getStringList("hologram");
            List<String> animations = crateSection.getStringList("animations");

            ParticleEffectConfig particleEffectConfig = null;
            ConfigurationSection particleSection = crateSection.getConfigurationSection("particle_effect");
            if (particleSection != null) {
                boolean enabled = particleSection.getBoolean("enabled", false);
                String type = particleSection.getString("effect", "heart");
                String effect = particleSection.getString("style", "sphere");

                particleEffectConfig = new ParticleEffectConfig(enabled, type, effect);
            } else {
                particleEffectConfig = new ParticleEffectConfig(false, "heart", "sphere");
            }

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

            ConfigurationSection keysSection = crateSection.getConfigurationSection("keys");
            Crate.KeyType keyType = Crate.KeyType.PHYSIC;
            String keyName = null;
            boolean enchanted = false;

            if (keysSection != null) {
                String typeStr = keysSection.getString("type", "physic").toUpperCase();
                try {
                    keyType = Crate.KeyType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Tipo key non valido per crate " + key + ", imposto PHYSIC di default");
                    keyType = Crate.KeyType.PHYSIC;
                }

                if (keyType == Crate.KeyType.PHYSIC) {
                    keyName = keysSection.getString("name", "Crate Key");
                }
                enchanted = keysSection.getBoolean("enchanted", false);
            }

            Crate crate = new Crate(key, displayName, hologram, animations, prizes, particleEffectConfig, keyType, keyName, enchanted);
            crates.put(key, crate);
        }

        plugin.getLogger().info("Caricate " + crates.size() + " crate.");
    }

    public Optional<Crate> getCrate(String id) {
        return Optional.ofNullable(crates.get(id));
    }

    public Collection<Crate> getCrates() {
        return crates.values();
    }

    public List<String> getAllCrateIds() {
        return new ArrayList<>(crates.keySet());
    }
}