package com.atlas.core.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@TableName("project")
public class Project {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private final String id;
    
    private final String name;
    
    private final String description;
    
    private ProjectStatus status;
    
    private final LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private final String workspaceId;

    public enum ProjectStatus {
        CREATED,
        RUNNING,
        PAUSED,
        COMPLETED,
        FAILED
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
