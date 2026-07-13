package com.atlas.storage.service;

import com.atlas.core.artifact.Artifact;
import com.atlas.storage.repository.ArtifactMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ArtifactService {
    
    private final ArtifactMapper artifactMapper;

    public ArtifactService(ArtifactMapper artifactMapper) {
        this.artifactMapper = artifactMapper;
    }

    @Transactional
    public Artifact save(Artifact artifact) {
        log.info("Saving artifact: {}", artifact.getName());
        artifactMapper.insert(artifact);
        return artifact;
    }

    public Optional<Artifact> findById(String id) {
        Artifact artifact = artifactMapper.selectById(id);
        return Optional.ofNullable(artifact);
    }

    public List<Artifact> findByProjectId(String projectId) {
        log.info("Finding artifacts for project: {}", projectId);
        return artifactMapper.selectByProjectId(projectId);
    }

    public List<Artifact> findByTaskId(String taskId) {
        return artifactMapper.selectByTaskId(taskId);
    }

    public List<Artifact> findByType(Artifact.ArtifactType type) {
        return artifactMapper.selectByType(type);
    }

    @Transactional
    public boolean delete(String id) {
        log.info("Deleting artifact: {}", id);
        int rows = artifactMapper.deleteById(id);
        return rows > 0;
    }

    @Transactional
    public boolean update(Artifact artifact) {
        log.info("Updating artifact: {}", artifact.getId());
        int rows = artifactMapper.updateById(artifact);
        return rows > 0;
    }
}
