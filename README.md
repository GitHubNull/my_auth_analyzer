# Auth Analyzer - 授权测试与分析工具

[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![Burp Suite](https://img.shields.io/badge/Burp%20Suite-Professional%20%7C%20Community-blue.svg)](https://portswigger.net/burp)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**English Version**: [README_EN.md](README_EN.md)

## 项目简介

**Auth Analyzer** 是一款专业的 Burp Suite 扩展，专为授权测试和安全分析而设计。该工具通过自动重复请求并使用不同的用户会话来检测授权绕过漏洞，帮助安全研究人员和渗透测试人员发现应用程序中的授权缺陷。

### 核心特性

🔒 **多会话管理** - 同时测试多个用户角色和权限级别  
🎯 **智能参数提取** - 自动从响应中提取令牌、Cookie 和参数  
🔧 **高级参数替换** - 支持 JSON Path、Form 数据和 XML 参数操作  
📊 **响应分析对比** - 自动比较原始和修改后的请求响应  
🚨 **绕过检测** - 内置分析引擎识别潜在的授权绕过  
📤 **Postman 导出** - 导出请求和响应到 Postman Collection v2.1 格式  
🎛️ **灵活过滤系统** - 多种过滤器精确控制测试范围  
💾 **配置持久化** - 会话配置自动保存和加载  

## 功能概览

### 🏗️ 技术架构

Auth Analyzer 采用模块化架构设计，主要包含以下核心组件：

- **BurpExtender**: 扩展入口点，实现 IBurpExtender 接口
- **MainPanel**: 主界面容器，采用分割面板布局
- **Session**: 会话实体，管理用户会话和参数配置
- **HttpListener**: HTTP 流量拦截和处理
- **RequestController**: 请求修改和重复执行逻辑

### 🔍 参数提取系统

支持多种参数提取方式：

#### 自动提取
- 从 `Set-Cookie` 头部提取会话 Cookie
- 从 HTML 输入字段提取 CSRF 令牌
- 从 JSON 响应提取动态参数

#### From-To 字符串提取
- 基于起始和结束字符串的通用提取
- 支持从 JavaScript 变量中提取值
- 灵活的定位规则配置

#### 其他提取方式
- 静态值预定义
- 用户交互式输入提示

### 🛠️ 高级参数替换

#### JSON 参数替换
支持标准 JSON Path 语法，提供精确的 JSON 操作能力：

```json
// 示例 JSON Path 表达式
$.user.name              // 获取用户名
$.store.book[0].title    // 获取第一本书的标题
$..price                 // 递归搜索所有价格字段
$.items[*].id            // 获取所有项目的ID
```

**特性**:
- 嵌套对象处理
- 数组元素操作
- 条件过滤支持
- 参数完全移除功能

#### Form 参数替换
支持两种主流表单格式：

- **URL-Encoded Forms**: `application/x-www-form-urlencoded` 格式
- **Multipart Forms**: `multipart/form-data` 格式（包含文件上传）

#### XML 参数替换
- XPath 表达式支持
- XML 文档结构操作

### 📈 绕过检测机制

自动响应分析系统：

- **SAME**: 响应体和状态码完全相同
- **SIMILAR**: 状态码相同，响应体长度相差 ±5%
- **DIFFERENT**: 其他所有情况
- **BYPASS**: 检测到授权绕过

### 🎛️ 智能过滤系统

多种过滤器精确控制测试范围：

- **范围过滤** (In Scope)
- **代理流量过滤** (Only Proxy Traffic)
- **文件类型过滤** (Exclude Filetypes)
- **HTTP 方法过滤** (Exclude HTTP Methods)
- **状态码过滤** (Exclude Status Codes)
- **路径过滤** (Exclude Paths)
- **查询参数过滤** (Exclude Queries/Params)

### 📤 Postman 导出功能

#### 导出特性
- **Postman Collection v2.1 格式**：完全符合 Postman 规范
- **会话组织**：按会话名称自动分组请求
- **元数据保留**：包含会话信息、绕过状态和响应代码
- **灵活导出选项**：可选择是否包含原始请求

#### 导出格式示例
```
Auth Analyzer Export/
├── Session: Admin/
│   ├── GET /api/users - BYPASS
│   ├── POST /api/users - SAME
│   └── ...
├── Session: User/
│   ├── GET /api/profile - DIFFERENT
│   └── ...
└── Session: Anonymous/
    ├── GET /api/public - SAME
    └── ...
```

## 安装指南

### 环境要求

- **Java**: 1.8 或更高版本
- **Burp Suite**: Professional 或 Community 版本
- **Maven**: 3.6+ (仅编译时需要)

### 编译安装

1. **克隆项目**
   ```bash
   git clone https://github.com/GitHubNull/my_auth_analyzer.git
   cd my_auth_analyzer
   ```

2. **编译打包**
   ```bash
   mvn clean package
   ```

3. **安装扩展**
   - 启动 Burp Suite
   - 进入 `Extender` → `Extensions`
   - 点击 `Add` → `Extension`
   - 选择 `target/myAuthAnalyzer-2.0.0-jar-with-dependencies.jar`

## 使用指南

### 基本使用流程

1. **创建会话**
   - 为每个测试用户角色创建独立会话
   - 配置会话头部和参数替换规则

2. **配置参数**
   - 设置要提取和替换的参数
   - 选择提取方式（自动/静态/提示输入）

3. **设置过滤器**
   - 配置请求过滤规则
   - 确定测试范围

4. **开始测试**
   - 启动 Auth Analyzer
   - 使用高权限用户浏览应用
   - 观察测试结果

### 高级功能使用

#### JSON Path 参数替换示例

**场景**: 替换嵌套 JSON 中的用户 ID

```
配置:
- JSON Path: $.user.id
- 替换值: 12345
- 移除: 否
```

**场景**: 移除认证令牌

```
配置:
- JSON Path: $.auth.token
- 替换值: (空)
- 移除: 是
```

#### 多角色同时测试

创建多个会话来测试不同权限级别：

- **管理员会话**: 完整权限访问
- **普通用户会话**: 有限权限访问
- **匿名会话**: 无权限访问

#### CORS 配置测试

1. 在会话配置中添加 `Origin` 头部
2. 选择 `Test CORS` 选项
3. Auth Analyzer 自动将 HTTP 方法改为 OPTIONS
4. 分析 CORS 响应头

## 技术细节

### 依赖库

- **Burp Extender API (2.3)**: Burp Suite 扩展 API
- **Gson (2.10.1)**: JSON 序列化/反序列化
- **JSON Path (2.9.0)**: JSON 路径查询和操作
- **JSoup (1.15.4)**: HTML 解析和处理
- **FastJSON (2.0.32)**: 额外 JSON 处理支持
- **Apache Tika (2.7.0)**: 内容类型检测
- **Commons Codec (1.17.1)**: 编码操作

### 构建配置

- **Java 版本**: 1.8 (源码和目标)
- **打包方式**: JAR with dependencies
- **编码**: UTF-8
- **构建命令**: `mvn package`

### 性能特性

- **异步处理**: 多线程并发请求处理
- **内存优化**: 高效的数据结构设计
- **响应缓存**: 智能缓存机制减少重复计算

## 项目结构

```
src/
├── burp/
│   └── BurpExtender.java                    # 扩展入口点
└── com/protect7/authanalyzer/
    ├── entities/                            # 核心数据实体
    │   ├── Session.java                     # 会话实体
    │   ├── Token.java                       # 令牌实体
    │   ├── JsonParameterReplace.java        # JSON 参数替换
    │   ├── FormParameterReplace.java        # Form 参数替换
    │   └── XmlParameterReplace.java         # XML 参数替换
    ├── gui/                                 # 图形用户界面
    │   ├── main/                            # 主要 UI 组件
    │   ├── dialog/                          # 对话框窗口
    │   └── entity/                          # UI 实体组件
    ├── controller/                          # 业务逻辑控制器
    │   ├── HttpListener.java                # HTTP 监听器
    │   ├── RequestController.java           # 请求控制器
    │   └── ContextMenuController.java       # 上下文菜单控制器
    ├── filter/                              # 请求过滤器
    │   ├── MethodFilter.java                # HTTP 方法过滤
    │   ├── StatusCodeFilter.java            # 状态码过滤
    │   └── PathFilter.java                  # 路径过滤
    └── util/                                # 工具类
        ├── RequestModifHelper.java          # 请求修改助手
        ├── DataExporter.java                # 数据导出器
        ├── PostmanCollectionBuilder.java    # Postman 集合构建器
        └── ExtractionHelper.java            # 提取助手
```

## 贡献指南

我们欢迎社区贡献！请遵循以下步骤：

### 开发环境设置

1. Fork 项目到你的 GitHub 账户
2. 克隆你的 fork
3. 创建功能分支: `git checkout -b feature/amazing-feature`
4. 提交更改: `git commit -m 'Add amazing feature'`
5. 推送分支: `git push origin feature/amazing-feature`
6. 创建 Pull Request

### 代码规范

- 遵循 Java 编码规范
- 添加适当的注释和文档
- 确保所有测试通过
- 保持代码结构清晰

### 问题报告

使用 GitHub Issues 报告问题时，请包含：

- 详细的错误描述
- 重现步骤
- 环境信息（Java 版本、Burp Suite 版本等）
- 相关日志或截图

## 更新日志

### v2.0.0
- ✨ 新增 Postman Collection v2.1 导出功能
- 🔧 优化 JSON Path 参数替换引擎
- 🎛️ 增强过滤器系统
- 🐛 修复参数提取的边界情况
- 📚 完善文档和示例

### v1.1.14
- 🐛 修复会话配置保存问题
- 🔧 优化响应分析算法
- 📤 改进导出功能
- 🎨 UI 界面优化

## 常见问题

### Q: 如何提取 JavaScript 变量中的值？
A: 使用 "From To String" 提取方式，设置起始和结束字符串来定位变量值。

### Q: JSON Path 支持哪些语法？
A: 支持标准 JSON Path 语法，包括嵌套对象、数组索引、递归搜索等。

### Q: 如何测试 CSRF 保护机制？
A: 配置参数移除功能，移除 CSRF 令牌参数，观察请求是否被拒绝。

### Q: Postman 导出支持哪些格式？
A: 目前支持 Postman Collection v2.1 格式，完全兼容 Postman 导入。

## 免责声明

本工具仅用于合法的安全研究和授权测试。使用者应当：

- 仅在获得明确授权的系统上使用
- 遵守相关法律法规和道德准则
- 对使用本工具产生的任何后果承担责任

详细信息请参阅 [DISCLAIMER.md](DISCLAIMER.md)。

## 开源协议

本项目采用 MIT 开源协议，详情请参阅 [LICENSE](LICENSE)。

## 联系方式

- **项目主页**: https://github.com/GitHubNull/my_auth_analyzer
- **问题反馈**: https://github.com/GitHubNull/my_auth_analyzer/issues
- **作者**: org 0xff (增强功能开发者)

---

**免责声明**: 本工具仅用于授权的安全测试和研究目的。使用者需要确保在合法和道德的范围内使用此工具。