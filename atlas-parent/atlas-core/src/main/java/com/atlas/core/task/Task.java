package com.atlas.core.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@TableName("task")
public class Task {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private final String id;
    
    private final String projectId;
    
    private final String workflowId;
    
    private final String agentName;
    
    private TaskStatus status;
    
    private final Map<String, Object> inputs;
    
    private Map<String, Object> outputs;
    
    private String errorMessage;
    
    private final LocalDateTime createdAt;
    
    private LocalDateTime startedAt;
    
    private LocalDateTime completedAt;
    
    private Integer retryCount;
    
    private final String parentTaskId;

    public enum TaskStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED,
        RETRYING
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        if (status == TaskStatus.RUNNING && this.startedAt == null) {
            this.startedAt = LocalDateTime.now();
        }
        if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED) {
            this.completedAt = LocalDateTime.now();
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void incrementRetryCount() {
        this.retryCount = (this.retryCount == null ? 0 : this.retryCount) + 1;
    }
}
