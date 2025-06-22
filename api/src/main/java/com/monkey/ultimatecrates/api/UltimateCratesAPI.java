package com.monkey.ultimatecrates.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

public final class UltimateCratesAPI {

    @Getter
    @Setter(value = AccessLevel.PRIVATE, onMethod_ = {@Deprecated})
    private static UltimateCratesProvider provider;

    private UltimateCratesAPI() {}

    public static UltimateCratesProvider get() {
        if (provider == null) {
            throw new MissingImplementationException();
        }
        return provider;
    }

    public static boolean isRegistered() {
        return provider != null;
    }

    public static void register(UltimateCratesProvider newProvider, Logger logger) {
        if (provider == null) {
            setProvider(newProvider);
            if (logger != null) logger.info("API registrata con successo");
        } else {
            if (logger != null) logger.warning("API già registrata, operazione ignorata.");
        }
    }
}
