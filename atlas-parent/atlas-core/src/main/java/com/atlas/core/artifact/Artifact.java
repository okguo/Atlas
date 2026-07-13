package com.atlas.core.artifact;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@TableName("artifact")
public class Artifact {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private final String id;
    
    private final String projectId;
    
    private final String taskId;
    
    private final String name;
    
    private final ArtifactType type;
    
    private final String mimeType;
    
    private final String storagePath;
    
    private final Long size;
    
    private final LocalDateTime createdAt;
    
    private String metadata;

    public enum ArtifactType {
        SCRIPT,
        RESEARCH,
        VOICE,
        SUBTITLE,
        IMAGE,
        VIDEO,
        COVER,
        SEO_META,
        STORYBOARD,
        CONTENT_PACKAGE
    }

    public void setMetadata(Map<String, Object> metadataMap) {
        try {
            this.metadata = new ObjectMapper().writeValueAsString(metadataMap);
        } catch (Exception e) {
            this.metadata = metadataMap.toString();
        }
    }

    public Map<String, Object> getMetadataAsMap() {
        if (metadata == null || metadata.isEmpty()) {
            return Map.of();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(metadata, Map.class);
        } catch (Exception e) {
            return Map.of();
        }
    }
}
