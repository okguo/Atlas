package com.atlas.agent;

import com.atlas.core.agent.Agent;
import com.atlas.core.agent.AgentContext;
import com.atlas.core.task.Task;
import com.atlas.core.artifact.Artifact;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseAgent implements Agent {

    @Override
    public abstract String getName();

    @Override
    public Artifact execute(Task task, AgentContext context) {
        log.info("Executing agent: {} for task: {}", getName(), task.getId());
        task.setStatus(Task.TaskStatus.RUNNING);

        try {
            Artifact artifact = doExecute(task, context);
            task.setStatus(Task.TaskStatus.COMPLETED);
            log.info("Agent {} completed successfully", getName());
            return artifact;
        } catch (Exception e) {
            task.setStatus(Task.TaskStatus.FAILED);
            task.setErrorMessage(e.getMessage());
            log.error("Agent {} failed: {}", getName(), e.getMessage(), e);
            throw e;
        }
    }

    protected abstract Artifact doExecute(Task task, AgentContext context);
}
