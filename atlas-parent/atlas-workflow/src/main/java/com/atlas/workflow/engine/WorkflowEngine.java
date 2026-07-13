package com.atlas.workflow.engine;

import com.atlas.core.workflow.Workflow;
import com.atlas.core.task.Task;
import com.atlas.core.artifact.Artifact;
import java.util.Map;

public interface WorkflowEngine {
    String startWorkflow(Workflow workflow, Map<String, Object> inputs);
    void pauseWorkflow(String workflowId);
    void resumeWorkflow(String workflowId);
    void cancelWorkflow(String workflowId);
    WorkflowStatus getWorkflowStatus(String workflowId);
}
