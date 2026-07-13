# Atlas Project

AI Content Production Operating System

## 项目结构

```
atlas-parent/
├── atlas-core/          # 核心领域模型
├── atlas-workflow/      # Workflow 引擎
├── atlas-agent/         # Agent 实现
├── atlas-plugin/        # Plugin 系统
│   ├── atlas-plugin-api/
│   ├── atlas-plugin-llm/
│   └── atlas-plugin-tts/
├── atlas-storage/       # 数据持久化
└── atlas-api/           # REST API
```

## 快速开始

### 前置要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+ (可选，MVP 阶段可使用 SQLite)

### 构建项目

```bash
cd atlas-parent
mvn clean install
```

### 运行应用

```bash
cd atlas-api
mvn spring-boot:run
```

### API 测试

创建项目并生成脚本：

```bash
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "丰田发展史",
    "description": "制作一期关于丰田发展史的视频",
    "workflowType": "SCRIPT_CREATION",
    "inputs": {
      "topic": "丰田汽车发展史"
    }
  }'
```

## 开发计划

### V1 MVP (当前阶段)

- [x] 项目结构搭建
- [x] 核心领域模型
- [x] ScriptAgent 实现
- [x] 基础 REST API
- [ ] Workflow 引擎集成 (Temporal)
- [ ] LLM Plugin 完整实现
- [ ] 数据持久化

### V2

- [ ] 资料搜集 Agent
- [ ] 事实校验 Agent
- [ ] 配音生成

### V3+

- [ ] 自动视频生成
- [ ] 多平台发布
- [ ] 数据分析

## 技术栈

- **语言**: Java 17
- **框架**: Spring Boot 3.2
- **Workflow**: Temporal.io (计划)
- **AI**: LangChain4j
- **数据库**: MySQL + SQLite
- **构建**: Maven

## 许可证

MIT License
