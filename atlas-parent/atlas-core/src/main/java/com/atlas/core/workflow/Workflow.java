package com.atlas.core.workflow;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Workflow {
    private final String id;
    private final String name;
    private final String description;
    private final WorkflowType type;
    private final List<WorkflowStep> steps;
    private final boolean active;

    public enum WorkflowType {
        SCRIPT_CREATION,
        RESEARCH,
        FULL_PRODUCTION,
        CUSTOM
    }
}
