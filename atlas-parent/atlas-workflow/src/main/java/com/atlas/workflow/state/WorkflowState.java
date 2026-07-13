package com.atlas.workflow.state;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class WorkflowState {
    private final String workflowId;
    private final String projectId;
    private final String status;
    private final Map<String, Object> variables;
    private final int currentStep;
    private final String lastError;
}
