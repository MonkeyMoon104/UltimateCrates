package com.monkey.ultimatecrates.api;

public class MissingImplementationException extends IllegalStateException {
    public MissingImplementationException() {
        super("UltimateCrates not loaded: API still not initializated.");
    }
}
