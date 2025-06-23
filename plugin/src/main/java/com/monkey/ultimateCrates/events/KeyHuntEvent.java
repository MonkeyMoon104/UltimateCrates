package com.monkey.ultimateCrates.events;

public class KeyHuntEvent {
    private final String keyName;

    public KeyHuntEvent(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
