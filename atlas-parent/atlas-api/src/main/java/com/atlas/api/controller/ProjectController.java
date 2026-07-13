package com.atlas.api.controller;

import com.atlas.api.dto.CreateProjectRequest;
import com.atlas.api.dto.ProjectResponse;
import com.atlas.core.agent.Agent;
import com.atlas.core.agent.AgentContext;
import com.atlas.core.artifact.Artifact;
import com.atlas.core.project.Project;
import com.atlas.core.task.Task;
import com.atlas.storage.service.ArtifactService;
import com.atlas.storage.service.ProjectService;
import com.atlas.storage.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ArtifactService artifactService;
    
    private final Map<String, Agent> agentRegistry = new HashMap<>();

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody CreateProjectRequest request) {
        log.info("Creating project: {}", request.getName());

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        projectService.save(project);

        if (request.getInputs() != null && request.getInputs().containsKey("topic")) {
            executeScriptWorkflow(project, request.getInputs());
        }

        return ResponseEntity.ok(toProjectResponse(project));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String projectId) {
        return projectService.findById(projectId)
                .map(this::toProjectResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listProjects() {
        List<ProjectResponse> responses = projectService.findAll().stream()
                .map(this::toProjectResponse)
                .toList();
        log.info("Listing {} projects", responses.size());
        return ResponseEntity.ok(responses);
    }

    private void executeScriptWorkflow(Project project, Map<String, Object> inputs) {
        log.info("Executing script workflow for project: {}", project.getId());

        projectService.updateStatus(project.getId(), Project.ProjectStatus.RUNNING);

        String topic = (String) inputs.get("topic");
        
        Task scriptTask = Task.builder()
                .projectId(project.getId())
                .workflowId("script_workflow")
                .agentName("ScriptAgent")
                .inputs(inputs)
                .build();

        taskService.save(scriptTask);

        AgentContext context = AgentContext.builder()
                .projectId(project.getId())
                .workflowId("script_workflow")
                .variables(inputs)
                .build();

        try {
            Agent scriptAgent = agentRegistry.get("ScriptAgent");
            if (scriptAgent == null) {
                scriptAgent = new com.atlas.agent.ScriptAgent();
            }

            Artifact scriptArtifact = scriptAgent.execute(scriptTask, context);

            String scriptContent = generateMockScript(topic, project.getId());
            byte[] scriptBytes = scriptContent.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            
            scriptArtifact.setMetadata(Map.of(
                "content", scriptContent,
                "contentLength", scriptBytes.length
            ));
            
            artifactService.save(scriptArtifact);

            Map<String, Object> outputs = new HashMap<>();
            outputs.put("artifactId", scriptArtifact.getId());
            outputs.put("filePath", scriptArtifact.getStoragePath());
            outputs.put("content", scriptContent);
            scriptTask.setOutputs(outputs);
            taskService.update(scriptTask);

            projectService.updateStatus(project.getId(), Project.ProjectStatus.COMPLETED);

            log.info("Script workflow completed successfully for project: {}", project.getId());

        } catch (Exception e) {
            taskService.updateError(scriptTask.getId(), e.getMessage());
            taskService.updateStatus(scriptTask.getId(), Task.TaskStatus.FAILED);
            projectService.updateStatus(project.getId(), Project.ProjectStatus.FAILED);
            log.error("Script workflow failed: {}", e.getMessage(), e);
        }
    }

    private String generateMockScript(String topic, String projectId) {
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
            """, topic, topic, topic, projectId, LocalDateTime.now());
    }

    private ProjectResponse toProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus().name())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
