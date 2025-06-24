package com.monkey.ultimateCrates.bukkit.crates.model;

public class PCE {

    private final boolean enabled;
    private final String type;
    private final String effect;

    public PCE(boolean enabled, String type, String effect) {
        this.enabled = enabled;
        this.type = type;
        this.effect = effect;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getType() {
        return type;
    }

    public String getEffect() {
        return effect;
    }
}
