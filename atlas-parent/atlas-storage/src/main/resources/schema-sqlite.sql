-- SQLite 数据库初始化脚本
-- 用于本地开发/测试

PRAGMA foreign_keys = ON;

-- Project 表
CREATE TABLE IF NOT EXISTS project (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL DEFAULT 'CREATED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    workspace_id TEXT
);

CREATE INDEX IF NOT EXISTS idx_project_status ON project(status);
CREATE INDEX IF NOT EXISTS idx_project_workspace_id ON project(workspace_id);
CREATE INDEX IF NOT EXISTS idx_project_created_at ON project(created_at);

-- Task 表
CREATE TABLE IF NOT EXISTS task (
    id TEXT PRIMARY KEY,
    project_id TEXT NOT NULL,
    workflow_id TEXT NOT NULL,
    agent_name TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING',
    inputs TEXT,
    outputs TEXT,
    error_message TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    started_at DATETIME,
    completed_at DATETIME,
    retry_count INTEGER DEFAULT 0,
    parent_task_id TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_task_project_id ON task(project_id);
CREATE INDEX IF NOT EXISTS idx_task_workflow_id ON task(workflow_id);
CREATE INDEX IF NOT EXISTS idx_task_status ON task(status);
CREATE INDEX IF NOT EXISTS idx_task_agent_name ON task(agent_name);

-- Artifact 表
CREATE TABLE IF NOT EXISTS artifact (
    id TEXT PRIMARY KEY,
    project_id TEXT NOT NULL,
    task_id TEXT,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    mime_type TEXT,
    storage_path TEXT,
    size INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    metadata TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_artifact_project_id ON artifact(project_id);
CREATE INDEX IF NOT EXISTS idx_artifact_task_id ON artifact(task_id);
CREATE INDEX IF NOT EXISTS idx_artifact_type ON artifact(type);
