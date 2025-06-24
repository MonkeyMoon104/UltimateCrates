package com.monkey.ultimateCrates.bukkit.crates.particles.loader;

import dev.esophose.playerparticles.particles.ParticleEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticleEffectsLoader {

    private static final List<String> SUPPORTED_EFFECTS = Arrays.asList(
            "angry_villager", "ash", "block", "block_crumble", "block_marker", "bubble", "bubble_column_up",
            "bubble_pop", "campfire_cosy_smoke", "campfire_signal_smoke", "cherry_leaves", "cloud", "composter",
            "crimson_spore", "crit", "current_down", "damage_indicator", "dolphin", "dragon_breath",
            "dripping_dripstone_lava", "dripping_dripstone_water", "dripping_honey", "dripping_lava",
            "dripping_obsidian_tear", "dripping_water", "dust", "dust_color_transition", "dust_pillar",
            "dust_plume", "egg_crack", "electric_spark", "enchant", "enchanted_hit", "end_rod", "entity_effect",
            "explosion", "explosion_emitter", "falling_dripstone_lava", "falling_dripstone_water",
            "falling_dust", "falling_honey", "falling_lava", "falling_nectar", "falling_obsidian_tear",
            "falling_spore_blossom", "falling_water", "firework", "fishing", "flame", "glow", "glow_squid_ink",
            "gust", "gust_emitter_large", "gust_emitter_small", "happy_villager", "heart", "infested",
            "instant_effect", "item", "item_cobweb", "item_slime", "item_snowball", "landing_honey",
            "landing_lava", "landing_obsidian_tear", "large_smoke", "lava", "mycelium", "nautilus", "note",
            "ominous_spawning", "pale_oak_leaves", "poof", "portal", "raid_omen", "rain", "reverse_portal",
            "scrape", "sculk_charge", "sculk_charge_pop", "sculk_soul", "shriek", "small_flame", "small_gust",
            "smoke", "sneeze", "snowflake", "sonic_boom", "soul", "soul_fire_flame", "spell", "spit", "splash",
            "spore_blossom_air", "squid_ink", "sweep_attack", "totem_of_undying", "trail", "trial_omen",
            "trial_spawner_detection", "trial_spawner_detection_ominous", "underwater", "vault_connection",
            "warped_spore", "wax_off", "wax_on", "white_ash", "witch"
    );

    public static List<ParticleEffect> loadSupportedEffects() {
        List<ParticleEffect> effects = new ArrayList<>();
        for (String name : SUPPORTED_EFFECTS) {
            ParticleEffect effect = ParticleEffect.fromName(name);
            if (effect != null) {
                effects.add(effect);
            }
        }
        return effects;
    }
}
