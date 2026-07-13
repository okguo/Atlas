package com.atlas.core.agent;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class AgentContext {
    private final String projectId;
    private final String workflowId;
    private final Map<String, Object> variables;
    private final Map<String, String> agentOutputs;

    public Object getVariable(String key) {
        return variables != null ? variables.get(key) : null;
    }

    public String getAgentOutput(String agentName) {
        return agentOutputs != null ? agentOutputs.get(agentName) : null;
    }
}
