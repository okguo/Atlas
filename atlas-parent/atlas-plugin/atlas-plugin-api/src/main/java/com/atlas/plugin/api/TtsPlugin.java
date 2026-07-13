package com.atlas.plugin.api;

public interface TtsPlugin extends Plugin {
    byte[] generate(String text);
    byte[] generate(String text, String voice);
    boolean supportsVoice(String voice);
}
