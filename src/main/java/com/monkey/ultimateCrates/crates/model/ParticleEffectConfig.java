package com.monkey.ultimateCrates.crates.model;

public class ParticleEffectConfig {

    private final boolean enabled;
    private final String color;
    private final double size;
    private final String type;
    private final String effect;

    public ParticleEffectConfig(boolean enabled, String color, double size, String type, String effect) {
        this.enabled = enabled;
        this.color = color;
        this.size = size;
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
