package com.atlas.storage.service;

import com.atlas.core.project.Project;
import com.atlas.storage.repository.ProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProjectService {
    
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Transactional
    public Project save(Project project) {
        log.info("Saving project: {}", project.getName());
        projectMapper.insert(project);
        return project;
    }

    public Optional<Project> findById(String id) {
        Project project = projectMapper.selectById(id);
        return Optional.ofNullable(project);
    }

    public Optional<Project> findByName(String name) {
        Project project = projectMapper.selectByName(name);
        return Optional.ofNullable(project);
    }

    public List<Project> findAll() {
        log.info("Finding all projects");
        return projectMapper.selectList(null);
    }

    public List<Project> findByWorkspaceId(String workspaceId) {
        return projectMapper.selectByWorkspaceId(workspaceId);
    }

    public List<Project> findByStatus(Project.ProjectStatus status) {
        return projectMapper.selectByStatus(status);
    }

    @Transactional
    public boolean delete(String id) {
        log.info("Deleting project: {}", id);
        int rows = projectMapper.deleteById(id);
        return rows > 0;
    }

    @Transactional
    public boolean update(Project project) {
        log.info("Updating project: {}", project.getId());
        int rows = projectMapper.updateById(project);
        return rows > 0;
    }

    @Transactional
    public boolean updateStatus(String projectId, Project.ProjectStatus status) {
        log.info("Updating project status: {} -> {}", projectId, status);
        Project project = projectMapper.selectById(projectId);
        if (project != null) {
            project.setStatus(status);
            return projectMapper.updateById(project) > 0;
        }
        return false;
    }
}
