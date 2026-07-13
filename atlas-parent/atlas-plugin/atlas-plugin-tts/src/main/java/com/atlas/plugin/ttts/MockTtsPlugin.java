package com.atlas.plugin.tts;

import com.atlas.plugin.api.TtsPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Slf4j
@Component
public class MockTtsPlugin implements TtsPlugin {
    
    private boolean initialized = false;

    @PostConstruct
    public void initialize() {
        this.initialized = true;
        log.info("Mock TTS Plugin initialized");
    }

    @Override
    public String getName() {
        return "MockTtsPlugin";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean isAvailable() {
        return initialized;
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down Mock TTS Plugin");
        this.initialized = false;
    }

    @Override
    public byte[] generate(String text) {
        return generate(text, "default");
    }

    @Override
    public byte[] generate(String text, String voice) {
        log.info("Generating TTS for text ({} chars) with voice: {}", text.length(), voice);
        
        return String.format("[模拟音频数据] 文本长度：%d 字符，语音：%s", text.length(), voice)
                .getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public boolean supportsVoice(String voice) {
        return "default".equals(voice) || "female".equals(voice) || "male".equals(voice);
    }
}
