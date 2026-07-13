package com.atlas.core.workflow;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkflowStep {
    private final String id;
    private final String name;
    private final String agentName;
    private final int order;
    private final StepType type;
    private final String condition;

    public enum StepType {
        ACTION,
        CONDITION,
        PARALLEL,
        LOOP
    }
}
