# Form参数替换功能使用说明

## 功能概述

myAuthAnalyzer v1.7.0 新增了Form参数替换功能，支持对HTTP请求中的form格式参数进行替换或删除操作。

## 支持的格式

- `application/x-www-form-urlencoded` - 标准表单编码格式
- `multipart/form-data` - 多部分表单数据格式（文件上传等）

## 使用方法

### 1. 打开Form参数替换对话框

在Session配置面板中，点击 **"Form 参数替换"** 按钮打开配置对话框。

### 2. 添加替换规则

在对话框中可以配置以下参数：

- **参数名**: 要替换的form参数名称（例如：username, password, token等）
- **替换值**: 新的参数值
- **移除**: 勾选此选项将删除指定参数而不是替换

### 3. 规则示例

#### 替换用户名参数
- 参数名: `username`
- 替换值: `admin`
- 移除: 不勾选

#### 删除CSRF令牌
- 参数名: `csrf_token`
- 替换值: （留空）
- 移除: 勾选

#### 替换用户ID
- 参数名: `user_id`
- 替换值: `12345`
- 移除: 不勾选

## 工作原理

1. **请求拦截**: 当Auth Analyzer处理HTTP请求时，会检查请求的Content-Type
2. **格式识别**: 只有form格式的请求会被处理（URL编码或多部分表单）
3. **参数匹配**: 根据配置的参数名在请求体中查找对应参数
4. **执行操作**: 
   - 如果是替换操作，将参数值更新为新值
   - 如果是删除操作，从请求中移除该参数
5. **请求重构**: 重新构建修改后的HTTP请求

## 注意事项

- Form参数替换只对请求体中的参数生效，不影响URL参数
- 参数名匹配是精确匹配（区分大小写）
- 如果指定的参数在请求中不存在，将跳过该规则
- 替换操作会在JSON参数替换之后执行
- 所有操作都会在控制台输出日志信息

## 配置持久化

Form参数替换规则会随Session配置一起保存，支持：
- 自动保存到Burp扩展存储
- 导出到JSON配置文件
- 从JSON配置文件导入

## 与其他功能的配合

Form参数替换功能与现有功能完全兼容：
- 可以与JSON参数替换同时使用
- 可以与Match and Replace规则组合
- 支持Session克隆时复制规则
- 与Token参数替换功能独立工作

## 调试信息

启用Form参数替换后，可以在Burp的扩展输出中查看详细的处理日志：
- 参数匹配情况
- 替换/删除操作结果
- 错误信息（如果有）

这些日志有助于调试和验证替换规则是否按预期工作。 