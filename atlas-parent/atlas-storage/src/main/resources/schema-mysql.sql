-- MySQL 数据库初始化脚本
-- 执行前请确保 MySQL 8.0+ 已安装并运行

CREATE DATABASE IF NOT EXISTS atlas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE atlas;

-- Project 表
CREATE TABLE IF NOT EXISTS project (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    workspace_id VARCHAR(64),
    INDEX idx_status (status),
    INDEX idx_workspace_id (workspace_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Task 表
CREATE TABLE IF NOT EXISTS task (
    id VARCHAR(64) PRIMARY KEY,
    project_id VARCHAR(64) NOT NULL,
    workflow_id VARCHAR(255) NOT NULL,
    agent_name VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    inputs JSON,
    outputs JSON,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    retry_count INT DEFAULT 0,
    parent_task_id VARCHAR(64),
    INDEX idx_project_id (project_id),
    INDEX idx_workflow_id (workflow_id),
    INDEX idx_status (status),
    INDEX idx_agent_name (agent_name),
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Artifact 表
CREATE TABLE IF NOT EXISTS artifact (
    id VARCHAR(64) PRIMARY KEY,
    project_id VARCHAR(64) NOT NULL,
    task_id VARCHAR(64),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    mime_type VARCHAR(100),
    storage_path VARCHAR(500),
    size BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metadata JSON,
    INDEX idx_project_id (project_id),
    INDEX idx_task_id (task_id),
    INDEX idx_type (type),
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
