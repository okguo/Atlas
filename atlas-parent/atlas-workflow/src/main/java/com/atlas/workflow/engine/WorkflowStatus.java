package com.atlas.workflow.engine;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorkflowStatus {
    private final String workflowId;
    private final String status;
    private final List<TaskStatus> taskStatuses;
    private final String errorMessage;

    @Getter
    @Builder
    public static class TaskStatus {
        private final String taskId;
        private final String agentName;
        private final String status;
        private final String errorMessage;
    }
}
