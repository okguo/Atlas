package com.atlas.storage.repository;

import com.atlas.core.artifact.Artifact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArtifactMapper extends BaseMapper<Artifact> {
    
    List<Artifact> selectByProjectId(String projectId);
    
    List<Artifact> selectByTaskId(String taskId);
    
    List<Artifact> selectByType(Artifact.ArtifactType type);
}
