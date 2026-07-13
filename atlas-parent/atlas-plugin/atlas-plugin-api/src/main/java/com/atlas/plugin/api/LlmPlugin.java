package com.atlas.plugin.api;

public interface LlmPlugin extends Plugin {
    String generate(String prompt);
    String generate(String prompt, Object... parameters);
    boolean supportsModel(String modelName);
}
