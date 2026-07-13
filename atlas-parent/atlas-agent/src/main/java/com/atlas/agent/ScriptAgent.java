package com.atlas.agent;

import com.atlas.core.agent.AgentContext;
import com.atlas.core.artifact.Artifact;
import com.atlas.core.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ScriptAgent extends BaseAgent {

    @Override
    public String getName() {
        return "ScriptAgent";
    }

    @Override
    protected Artifact doExecute(Task task, AgentContext context) {
        String topic = (String) task.getInputs().get("topic");
        
        log.info("Generating script for topic: {}", topic);
        
        String script = generateScript(topic, task.getProjectId());
        
        String fileName = "script_" + task.getId() + ".md";
        
        return Artifact.builder()
                .projectId(task.getProjectId())
                .taskId(task.getId())
                .name(fileName)
                .type(Artifact.ArtifactType.SCRIPT)
                .mimeType("text/markdown")
                .storagePath("/artifacts/" + task.getProjectId() + "/" + fileName)
                .size((long) script.length())
                .metadata(buildMetadata(topic))
                .build();
    }

    private String generateScript(String topic, String projectId) {
        return String.format("""
            # 视频脚本：%s
            
            ## 开场（0:00-0:30）
            [画面：引人入胜的开场画面]
            大家好，今天我们要探讨的主题是：%s
            
            ## 主体内容（0:30-3:00）
            
            ### 第一部分
            [内容要点 1]
            - 关键点 A
            - 关键点 B
            
            ### 第二部分
            [内容要点 2]
            - 关键点 C
            - 关键点 D
            
            ### 第三部分
            [内容要点 3]
            - 关键点 E
            - 关键点 F
            
            ## 总结（3:00-3:30）
            [画面：总结性画面]
            今天我们了解了关于%s的重要内容。
            感谢观看，我们下期再见！
            
            ---
            项目 ID: %s
            生成时间：%s
            状态：已生成
            """, topic, topic, topic, projectId, java.time.LocalDateTime.now());
    }

    private String buildMetadata(String topic) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("topic", topic);
        metadata.put("estimatedDuration", "3:30");
        metadata.put("wordCount", 500);
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(metadata);
        } catch (Exception e) {
            return metadata.toString();
        }
    }
}
