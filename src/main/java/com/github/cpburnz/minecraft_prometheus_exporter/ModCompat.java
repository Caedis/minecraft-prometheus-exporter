package com.github.cpburnz.minecraft_prometheus_exporter;

import java.util.function.Supplier;

import cpw.mods.fml.common.Loader;

public enum ModCompat {

    ServerUtilities("serverutilities");

    public final String modid;
    private final Supplier<Boolean> supplier;
    private Boolean loaded;

    ModCompat(String modid) {
        this.modid = modid;
        this.supplier = null;
    }

    ModCompat(Supplier<Boolean> supplier) {
        this.supplier = supplier;
        this.modid = null;
    }

    public boolean isLoaded() {
        if (loaded == null) {
            if (supplier != null) {
                loaded = supplier.get();
            } else if (modid != null) {
                loaded = Loader.isModLoaded(modid);
            } else loaded = false;
        }
        return loaded;
    }
}
