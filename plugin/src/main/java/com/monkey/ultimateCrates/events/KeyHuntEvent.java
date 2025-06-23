package com.monkey.ultimateCrates.events;

public class KeyHuntEvent {
    private final String keyName;
    private final int amount;

    public KeyHuntEvent(String keyName, int amount) {
        this.keyName = keyName;
        this.amount = amount;
    }

    public String getKeyName() {
        return keyName;
    }

    public int getAmount() {
        return amount;
    }
}
