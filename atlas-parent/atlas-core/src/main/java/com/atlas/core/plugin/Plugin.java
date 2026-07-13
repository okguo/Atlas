package com.atlas.core.plugin;

public interface Plugin {
    String getName();
    String getVersion();
    void initialize();
    boolean isAvailable();
}
