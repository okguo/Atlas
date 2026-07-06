# Atlas Agent 体系设计

> Version: v1.0  
> Status: Core Design Document（核心设计文档）  
> Last Updated: 2026-07-06  

---

# 一、文档目标

本文档定义 Atlas 中 Agent 的体系结构，包括：

- Agent 是什么
- Agent 在系统中的作用
- Agent 如何与 Workflow / Plugin 协作
- Agent 的生命周期
- Agent 的设计规范
- Agent 的运行模型
- Agent 的工程实现方式（Java）

---

# 二、Agent 的本质定义

在 Atlas 中：

> **Agent 是执行 Task 的最小智能单元**

但需要强调：

👉 Agent 不是“智能体系统”
👉 Agent 不是“LLM封装”
👉 Agent 不是“聊天机器人”

Agent 的本质是：

> **在 Workflow 驱动下，完成单一职责任务的执行器（Executor）**

---

# 三、Agent 在 Atlas 架构中的位置

```
Project
   │
   ▼
Workflow
   │
   ▼
Task
   │
   ▼
Agent  ← 核心执行层
   │
   ▼
Plugin
   │
   ▼
Artifact
```

---

# 四、Agent 核心设计原则

## 4.1 单一职责原则（Single Responsibility）

一个 Agent 只能做一件事：

例如：

- ResearchAgent → 搜集资料
- ScriptAgent → 生成脚本
- SEOAgent → 生成标题与描述
- PublishAgent → 发布内容

❌ 不允许：

- 一个 Agent 既写脚本又生成视频
- 一个 Agent 同时做 Research + SEO

---

## 4.2 无状态原则（Stateless）

Agent 不保存任何长期状态。

所有状态必须来自：

> Workflow Context（工作流上下文）

Agent 每次执行：

```
Input → Process → Output
```

不依赖历史运行记忆。

---

## 4.3 可替换原则（Replaceable）

Agent 内部不绑定：

- 某个 LLM
- 某个 API
- 某个 Prompt

所有能力通过 Plugin 提供。

---

## 4.4 可组合原则（Composable）

Agent 可以被 Workflow 任意组合：

```
Research → Script → Voice → Subtitle
```

也可以：

```
Research → Script → SEO → Publish
```

---

## 4.5 可观测原则（Observable）

每次 Agent 执行必须记录：

- 输入
- 输出
- 使用的 Plugin
- Token 消耗
- 执行时间
- 错误日志

---

# 五、Agent 类型分类

Atlas 中 Agent 分为三类：

---

## 5.1 生成型 Agent（Generator Agent）

负责“创造内容”

例如：

- ScriptAgent
- TitleAgent
- SubtitleAgent
- DescriptionAgent

---

## 5.2 处理型 Agent（Processor Agent）

负责“加工内容”

例如：

- SEO优化 Agent
- 内容改写 Agent
- 格式转换 Agent

---

## 5.3 执行型 Agent（Executor Agent）

负责“外部执行”

例如：

- PublishAgent
- UploadAgent
- RenderVideoAgent

---

# 六、Agent 标准执行模型

所有 Agent 必须遵循统一执行模型：

```
1. Receive Task
2. Load Context
3. Call Plugin
4. Generate Output
5. Return Artifact
```

---

## 6.1 标准执行流程

```
Task → Agent → Plugin → Result → Artifact
```

---

## 6.2 输入结构（Input）

```json
{
  "projectId": "xxx",
  "workflowId": "xxx",
  "taskId": "xxx",
  "input": {
    "topic": "Toyota 为什么成功"
  },
  "context": {
    "previousArtifacts": [],
    "workflowState": {}
  }
}
```

---

## 6.3 输出结构（Output）

```json
{
  "agentId": "ScriptAgent",
  "taskId": "xxx",
  "artifact": {
    "type": "script",
    "content": "...",
    "metadata": {
      "model": "gpt-4",
      "tokens": 1234
    }
  }
}
```

---

# 七、Agent 生命周期

Agent 在 Atlas 中具有明确生命周期：

```
Created
   ↓
Scheduled
   ↓
Running
   ↓
Calling Plugin
   ↓
Completed
   ↓
Persisted (Artifact生成)
```

---

# 八、Agent 与 Workflow 的关系

## Workflow 负责

- 定义步骤
- 调度 Task
- 决定执行顺序

## Agent 负责

- 执行 Task
- 生成 Artifact

---

## 关键原则

> Workflow 不知道如何做  
> Agent 不知道下一步做什么

---

# 九、Agent 与 Plugin 的关系

Agent 是 Plugin 的唯一调用者。

### 关系

```
Agent → Plugin → AI能力
```

---

## Plugin 示例

- OpenAI Plugin
- DeepSeek Plugin
- Whisper Plugin
- FFmpeg Plugin
- YouTube API Plugin

---

## 关键原则

> Plugin 不知道 Agent 存在  
> Agent 不直接实现能力  
> 能力必须通过 Plugin 提供

---

# 十、Agent 注册机制（Registry）

所有 Agent 必须注册到系统：

```java
AgentRegistry.register("ScriptAgent", ScriptAgent.class);
```

---

## Agent 元数据

```json
{
  "name": "ScriptAgent",
  "type": "generator",
  "inputSchema": "...",
  "outputSchema": "...",
  "description": "生成视频脚本"
}
```

---

# 十一、Agent 执行模型（Java设计）

## 11.1 BaseAgent 抽象类

```java
public abstract class BaseAgent {

    public final Artifact execute(TaskContext context) {
        preProcess(context);
        PluginResult result = callPlugin(context);
        Artifact artifact = postProcess(result);
        record(context, artifact);
        return artifact;
    }

    protected abstract PluginResult callPlugin(TaskContext context);

    protected void preProcess(TaskContext context) {}

    protected Artifact postProcess(PluginResult result) {
        return new Artifact(result);
    }

    protected void record(TaskContext context, Artifact artifact) {
        // 写入数据库
    }
}
```

---

## 11.2 示例：ScriptAgent

```java
public class ScriptAgent extends BaseAgent {

    @Override
    protected PluginResult callPlugin(TaskContext context) {
        return LLMPlugin.generate(
            context.getInput().getTopic()
        );
    }
}
```

---

# 十二、Agent 状态管理

Agent **不保存状态**

状态由：

> Workflow Context 管理

包括：

- 当前步骤
- 已生成 Artifact
- 执行历史

---

# 十三、Agent 调度模型

Atlas 使用：

> Workflow Driven Scheduling

流程：

```
Workflow → Task → Agent → Plugin
```

---

## 调度特点

- 顺序执行
- 支持重试
- 支持暂停
- 支持人工插入

---

# 十四、错误处理机制

Agent 执行失败：

### 1. 可重试错误

- API失败
- Token超限

→ 自动重试

---

### 2. 不可恢复错误

- Prompt错误
- Schema错误

→ 回退 Workflow

---

# 十五、V1 Agent 范围

V1 仅实现：

- ScriptAgent
- ResearchAgent
- NarrationAgent
- SubtitleAgent

不包含：

- 自学习Agent
- 多Agent协作推理
- 动态Agent生成

---

# 十六、未来演进方向

V2：

- Agent 组合执行
- Multi-Agent 协作

V3：

- Agent 自生成

V4：

- Agent 自动优化

---

# 十七、核心设计总结

Atlas Agent 的本质：

> **Agent = Task Executor + Plugin Caller**

---

## 三大铁律

### 1. Agent 不思考流程

流程属于 Workflow

---

### 2. Agent 不持有能力

能力属于 Plugin

---

### 3. Agent 不保存状态

状态属于 Workflow Context

---

# 十八、最终定义

> **Agent 是 Atlas 中执行 Task 的最小无状态智能单元，通过 Plugin 获得能力，在 Workflow 调度下生成 Artifact。**

---

# 🧭 本章结束语

Atlas 的 Agent 系统是整个内容生产能力的执行核心。

它的设计目标不是“聪明”，而是：

> 稳定、可组合、可替换、可扩展、可追溯。
