package com.atlas.storage.service;

import com.atlas.core.task.Task;
import com.atlas.storage.repository.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskService {
    
    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Transactional
    public Task save(Task task) {
        log.info("Saving task: {} for agent: {}", task.getId(), task.getAgentName());
        taskMapper.insert(task);
        return task;
    }

    public Optional<Task> findById(String id) {
        Task task = taskMapper.selectById(id);
        return Optional.ofNullable(task);
    }

    public List<Task> findByProjectId(String projectId) {
        log.info("Finding tasks for project: {}", projectId);
        return taskMapper.selectByProjectId(projectId);
    }

    public List<Task> findByWorkflowId(String workflowId) {
        return taskMapper.selectByWorkflowId(workflowId);
    }

    public List<Task> findByStatus(Task.TaskStatus status) {
        return taskMapper.selectByStatus(status);
    }

    @Transactional
    public boolean delete(String id) {
        log.info("Deleting task: {}", id);
        int rows = taskMapper.deleteById(id);
        return rows > 0;
    }

    @Transactional
    public boolean update(Task task) {
        log.info("Updating task: {}", task.getId());
        int rows = taskMapper.updateById(task);
        return rows > 0;
    }

    @Transactional
    public boolean updateStatus(String taskId, Task.TaskStatus status) {
        log.info("Updating task status: {} -> {}", taskId, status);
        Task task = taskMapper.selectById(taskId);
        if (task != null) {
            task.setStatus(status);
            return taskMapper.updateById(task) > 0;
        }
        return false;
    }

    @Transactional
    public boolean updateError(String taskId, String errorMessage) {
        log.info("Updating task error: {} - {}", taskId, errorMessage);
        Task task = taskMapper.selectById(taskId);
        if (task != null) {
            task.setErrorMessage(errorMessage);
            return taskMapper.updateById(task) > 0;
        }
        return false;
    }
}
