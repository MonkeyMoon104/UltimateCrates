package com.monkey.ultimateCrates.crates.db;

public class FixedParticleData {
    private final String particleName;
    private final String style;

    public FixedParticleData(String particleName, String style) {
        this.particleName = particleName;
        this.style = style;
    }

    public String getParticleName() {
        return particleName;
    }

    public String getStyle() {
        return style;
    }
}

