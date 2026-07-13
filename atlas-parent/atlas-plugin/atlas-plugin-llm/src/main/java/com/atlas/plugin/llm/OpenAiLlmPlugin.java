package com.atlas.plugin.llm;

import com.atlas.plugin.api.LlmPlugin;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Slf4j
@Component
public class OpenAiLlmPlugin implements LlmPlugin {
    
    @Value("${atlas.llm.api-key:}")
    private String apiKey;
    
    @Value("${atlas.llm.model:gpt-4-turbo}")
    private String modelName;
    
    private ChatLanguageModel model;
    private boolean initialized = false;

    @Override
    public String getName() {
        return "OpenAiLlmPlugin";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @PostConstruct
    public void initialize() {
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("LLM API key not configured, using mock mode");
            this.initialized = true;
            return;
        }
        
        this.model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .timeout(java.time.Duration.ofSeconds(60))
                .build();
        
        this.initialized = true;
        log.info("OpenAI LLM Plugin initialized with model: {}", modelName);
    }

    @Override
    public boolean isAvailable() {
        return initialized && (model != null || apiKey.isEmpty());
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down OpenAI LLM Plugin");
        this.initialized = false;
        this.model = null;
    }

    @Override
    public String generate(String prompt) {
        if (apiKey == null || apiKey.isEmpty()) {
            return generateMockResponse(prompt);
        }
        
        try {
            return model.generate(prompt);
        } catch (Exception e) {
            log.error("LLM generation failed: {}", e.getMessage(), e);
            throw new RuntimeException("LLM generation failed", e);
        }
    }

    @Override
    public String generate(String prompt, Object... parameters) {
        String formattedPrompt = String.format(prompt, parameters);
        return generate(formattedPrompt);
    }

    @Override
    public boolean supportsModel(String modelName) {
        return "gpt-4-turbo".equals(modelName) || "gpt-4".equals(modelName) || "gpt-3.5-turbo".equals(modelName);
    }

    private String generateMockResponse(String prompt) {
        return String.format("""
            [模拟响应]
            
            这是一个模拟的 LLM 响应，因为 API 密钥未配置。
            
            用户输入：%s
            
            要启用真实的 LLM 功能，请在配置文件中设置 atlas.llm.api-key
            
            ---
            这是 Atlas 项目的 MVP 阶段，仅用于验证工作流是否可行。
            """, prompt);
    }
}
