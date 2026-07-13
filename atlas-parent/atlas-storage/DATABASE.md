# Atlas 数据库配置说明

## 支持的数据库

Atlas 支持两种数据库模式：

### 1. MySQL（生产环境推荐）

**适用场景：** 多用户、高并发、生产环境

**初始化步骤：**

1. 确保 MySQL 8.0+ 已安装并运行
2. 执行初始化脚本：
   ```bash
   mysql -u root -p < atlas-storage/src/main/resources/schema-mysql.sql
   ```

**配置方式：**
```properties
atlas.db.type=mysql
atlas.db.primary.url=jdbc:mysql://localhost:3306/atlas?useSSL=false&serverTimezone=UTC&characterEncoding=utf8mb4
atlas.db.primary.username=root
atlas.db.primary.password=your_password
```

---

### 2. SQLite（开发/测试环境）

**适用场景：** 本地开发、单用户测试、快速原型

**初始化步骤：**

1. 创建数据目录：
   ```bash
   mkdir -p data
   ```

2. 创建 SQLite 数据库文件：
   ```bash
   sqlite3 data/atlas.db < atlas-storage/src/main/resources/schema-sqlite.sql
   ```

**配置方式：**
```properties
atlas.db.type=sqlite
atlas.db.secondary.path=./data/atlas.db
```

---

## 数据库表结构

### Project 表
存储内容创作项目信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(64) | 主键（UUID） |
| name | VARCHAR(255) | 项目名称 |
| description | TEXT | 项目描述 |
| status | VARCHAR(50) | 状态（CREATED/RUNNING/COMPLETED/FAILED） |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |
| workspace_id | VARCHAR(64) | 工作区 ID |

### Task 表
存储工作流任务信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(64) | 主键（UUID） |
| project_id | VARCHAR(64) | 项目 ID（外键） |
| workflow_id | VARCHAR(255) | 工作流 ID |
| agent_name | VARCHAR(100) | 执行的 Agent 名称 |
| status | VARCHAR(50) | 状态（PENDING/RUNNING/COMPLETED/FAILED） |
| inputs | JSON | 输入参数 |
| outputs | JSON | 输出结果 |
| error_message | TEXT | 错误信息 |
| created_at | TIMESTAMP | 创建时间 |
| started_at | TIMESTAMP | 开始时间 |
| completed_at | TIMESTAMP | 完成时间 |
| retry_count | INT | 重试次数 |
| parent_task_id | VARCHAR(64) | 父任务 ID |

### Artifact 表
存储生成的内容产物

| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(64) | 主键（UUID） |
| project_id | VARCHAR(64) | 项目 ID（外键） |
| task_id | VARCHAR(64) | 任务 ID（外键） |
| name | VARCHAR(255) | 产物名称 |
| type | VARCHAR(50) | 类型（SCRIPT/VOICE/SUBTITLE 等） |
| mime_type | VARCHAR(100) | MIME 类型 |
| storage_path | VARCHAR(500) | 存储路径 |
| size | BIGINT | 文件大小（字节） |
| created_at | TIMESTAMP | 创建时间 |
| metadata | JSON | 元数据 |

---

## 快速开始（SQLite 模式）

1. 创建数据目录：
   ```bash
   mkdir -p data
   ```

2. 初始化 SQLite 数据库：
   ```bash
   sqlite3 data/atlas.db < atlas-storage/src/main/resources/schema-sqlite.sql
   ```

3. 修改配置文件 `application.properties`：
   ```properties
   atlas.db.type=sqlite
   atlas.db.secondary.path=./data/atlas.db
   ```

4. 启动应用：
   ```bash
   cd atlas-api
   mvn spring-boot:run
   ```

---

## 迁移说明

### 从 SQLite 迁移到 MySQL

1. 在 MySQL 中执行 `schema-mysql.sql` 创建表结构
2. 使用数据迁移工具（如 Flyway 或 Liquibase）迁移数据
3. 更新配置文件指向 MySQL

### 数据备份

**SQLite:**
```bash
cp data/atlas.db data/atlas_backup_$(date +%Y%m%d).db
```

**MySQL:**
```bash
mysqldump -u root -p atlas > atlas_backup_$(date +%Y%m%d).sql
```

---

## 注意事项

1. **MySQL 配置：**
   - 确保字符集为 `utf8mb4`
   - 建议开启连接池配置
   - 生产环境建议配置主从复制

2. **SQLite 限制：**
   - 不支持高并发写入
   - 适合单用户使用
   - 定期备份数据库文件

3. **JSON 字段：**
   - MySQL 8.0+ 原生支持 JSON
   - SQLite 使用 TEXT 存储 JSON 字符串
   - MyBatis-Plus 使用 JacksonTypeHandler 自动转换
