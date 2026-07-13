package com.atlas.plugin.api;

public interface Plugin {
    String getName();
    String getVersion();
    void initialize();
    boolean isAvailable();
    void shutdown();
}
