package com.atlas.core.agent;

import com.atlas.core.task.Task;
import com.atlas.core.artifact.Artifact;
import java.util.Map;

public interface Agent {
    String getName();
    
    Artifact execute(Task task, AgentContext context);
    
    default boolean canHandle(Task task) {
        return task.getAgentName().equals(getName());
    }
}
