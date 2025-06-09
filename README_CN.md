# Auth Analyzer
### 目录
- [这是什么？](#这是什么)
- [为什么使用Auth Analyzer？](#为什么使用auth-analyzer)
- [界面概览](#界面概览)
- [参数提取](#参数提取)
  * [自动提取](#自动提取)
  * [从到字符串](#从到字符串)
  * [静态值](#静态值)
  * [输入提示](#输入提示)
- [参数替换](#参数替换)
  * [替换位置](#替换位置)
  * [高级参数替换](#高级参数替换)
- [参数移除](#参数移除)
- [使用示例](#使用示例)
  * [自动提取会话Cookie](#自动提取会话cookie)
  * [会话头和CSRF令牌参数](#会话头和csrf令牌参数)
  * [从JavaScript变量自动提取](#从javascript变量自动提取)
  * [自动提取并插入Bearer令牌](#自动提取并插入bearer令牌)
  * [同时测试多个角色](#同时测试多个角色)
  * [刷新自动提取的参数值](#刷新自动提取的参数值)
  * [测试幂等操作](#测试幂等操作)
  * [测试匿名会话](#测试匿名会话)
  * [测试CORS配置](#测试cors配置)
  * [测试CSRF检查机制](#测试csrf检查机制)
  * [高级参数替换使用](#高级参数替换使用)
  * [验证绕过状态](#验证绕过状态)
- [处理过滤器](#处理过滤器)
- [绕过检测](#绕过检测)
- [功能特性](#功能特性)


## 这是什么？
Burp扩展帮助您发现授权漏洞。只需使用高权限用户浏览Web应用程序，让Auth Analyzer为任何定义的非特权用户重复您的请求。通过定义参数的可能性，Auth Analyzer能够自动提取和替换参数值。例如，可以从响应中自动提取CSRF令牌甚至整个会话特征，并在进一步的请求中替换。每个响应都将被分析并根据其绕过状态进行标记。

## 为什么使用Auth Analyzer？
有其他现有的Burp扩展做类似的事情。但是，参数功能和自动值提取的强大功能是选择Auth Analyzer的主要原因。通过这个功能，您不必知道必须交换的数据内容。您可以轻松定义参数和cookies，Auth Analyzer将动态捕获所需的值。Auth Analyzer不执行任何预检请求。它基本上只是做与您的Web应用相同的事情。与您定义的用户角色/会话。

## 界面概览
(1) 为您要测试的每个用户创建或克隆会话。

(2) 保存和加载会话设置

(3) 指定会话特征（要替换的头和/或参数）

(4) 如果需要，设置过滤器

(5) 启动/停止和暂停Auth Analyzer

(6) 指定表格过滤器

(7) 使用另一个用户浏览Web应用并跟踪重复请求的结果

(8) 将表格数据导出为XML或HTML

(9) 手动分析原始和重复的请求/响应


![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/complete_gui.png)

## 半自动化授权测试
如果您在站点地图中有要测试的资源，执行授权测试非常简单快速。首先定义您要测试的会话。然后只需展开站点地图，选择资源并通过上下文菜单重复请求。此外，您可以定义一些选项，确定哪些请求应该重复，哪些不应该。通过这种方式，您可以在几秒钟内对复杂网站执行授权测试。

## 参数提取
Auth Analyzer可以定义在为给定会话重复请求之前替换的参数。给定参数的值可以根据不同的要求设置。

### 自动提取
如果参数值出现在具有以下约束之一的响应中，将被提取：

* 带有`Set-Cookie头`的响应，其Cookie名称设置为定义的`提取字段名`

* `HTML文档响应`包含一个name属性设置为定义的`提取字段名`的输入字段

* `JSON响应`包含一个设置为`提取字段名`的键

默认情况下，Auth Analyzer尝试从所有位置自动提取参数值。但是，单击参数设置图标可以让您根据需要限制自动提取位置。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/param_auto_extract_location.png)

### 从到字符串
如果响应在一行中包含指定的`From String`和`To String`，将提取参数。From-To字符串可以手动设置或直接通过相应的上下文菜单设置。只需在任何响应中标记您要提取的单词，并为您喜欢的参数设置为`From-To Extract`。

默认情况下，Auth Analyzer尝试从大多数文本响应的头和正文中提取值。但是，单击参数设置图标可以让您根据需要限制From-To提取位置。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/param_fromto_extract_location.png)

### 静态值
可以定义静态参数值。例如，这可以用于静态CSRF令牌或登录凭据。

### 输入提示
如果定义的参数存在于请求中，您将被提示输入。例如，这可以用于设置2FA代码。

## 参数替换
如果设置了值（由用户提取或定义），当相应参数存在于请求中时将被替换。参数替换的条件是：

### 替换位置
如果参数存在于以下位置之一，将被替换：

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/param_replace_locations.png)

* `在路径中`（例如`/api/user/99/profile` --> 如果存在名为`user`的参数，值`99`将被替换）

* `URL参数`（例如`email=hans.wurst[a]gmail.com`）

* `Cookie参数`（例如`PHPSESSID=mb8rkrcdg8765dt91vpum4u21v`）

* `Body参数`（`URL编码`或`多部分表单数据`）

* `JSON参数`（例如`{"email":"hans.wurst[a]gmail.com"}`）

默认情况下，参数值将在每个位置被替换。但是，单击参数设置图标可以让您根据需要限制位置。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/param_replace_location.png)

### 高级参数替换
除了标准参数替换方法外，Auth Analyzer为特定参数格式提供高级功能：

#### JSON参数替换
Auth Analyzer支持使用标准JSON Path表达式的高级JSON参数替换。此功能允许您精确定位特定JSON元素进行替换或删除：

* **JSON Path支持**：使用标准JSON Path语法（例如`$.user.name`、`$.store.book[0].title`、`$..price`）
* **嵌套对象处理**：替换深度嵌套JSON结构中的参数
* **数组支持**：定位特定数组元素或使用通配符进行多次替换
* **条件替换**：基于特定条件应用替换
* **参数移除**：完全删除特定JSON参数

#### 表单参数替换
Auth Analyzer现在支持两种标准表单数据格式的专用表单参数替换：

* **URL编码表单**：处理`application/x-www-form-urlencoded`格式参数
* **多部分表单**：处理`multipart/form-data`格式参数（包括文件上传）
* **参数特定操作**：按名称替换或删除特定表单参数
* **大小写敏感匹配**：精确参数名称匹配以进行精确控制
* **表单数据重构**：在参数修改后正确重建表单请求

两个高级替换功能都可以通过专用对话框界面进行配置，可通过会话面板中的"JSON 参数替换"和"Form 参数替换"按钮访问。

## 参数移除
定义的参数可以完全删除，例如用于测试CSRF检查机制。

## 使用示例

### 自动提取会话Cookie
将用户名和密码定义为`静态值`。会话cookie名称必须定义为`自动提取`。验证您开始浏览应用程序时没有设置会话cookie。登录到Web应用。Auth Analyzer将使用静态参数重复登录请求，并通过`Set-Cookie`头自动获取会话。此Cookie将用于给定会话的进一步请求。定义的Cookie将被视为参数，因此不必定义Cookie头。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/auto_extract_session_id_1.png)

提示：您可以限制参数的提取和替换条件，以避免在提取/替换阶段出现故障。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/parameter_settings_session_cookie.png)

### 会话头和CSRF令牌参数
定义一个Cookie头和一个CSRF令牌（使用`自动值提取`）。如果CSRF令牌值存在于给定会话的`HTML输入标签`、`Set-Cookie头`或`JSON响应`中，将被提取。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/session_header_with_csrf_token.png)

### 从JavaScript变量自动提取
由于`自动提取`方法仅适用于`HTML输入字段`、`JSON对象`或`Set-Cookie头`，我们必须使用名为`From To String`的通用提取方法。使用此提取方法，如果值位于唯一的起始和结束字符串之间，我们可以从响应中提取任何值。Auth Analyzer提供上下文菜单方法自动设置`From String`和`To String`。只需标记您要提取的字符串，然后通过上下文菜单设置为`From-To Extract`。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/auto_extract_csrftoken_from_js_var.png)

### 自动提取并插入Bearer令牌
由于Authorization头不被视为参数（如Cookie头那样），我们可以使用头插入点来实现我们想要的。只需标记并右键单击您要在指定头中替换的值。如果尚未提取参数值，将使用`defaultvalue`。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/auto_extract_and_insert_bearer_token.png)

### 同时测试多个角色
只需创建您想要的多个会话来同时测试多个角色。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/several_sessions_1.png)

### 刷新自动提取的参数值
只需在会话状态面板上按`Renew`或通过上下文菜单重复受影响的请求（在表格条目中右键单击）。提示：登录请求可以被标记并随后过滤。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/renew_session.png)

### 测试幂等操作
原始请求可以被丢弃以测试幂等操作（例如`DELETE`功能）。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/idempotent_operations.png)

### 测试匿名会话
如果匿名用户需要有效特征（例如有效的cookie值），您必须照常定义头。否则，您可以定义要删除的头，如下所示：

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/test_anonymous.png)

### 测试CORS配置
您可以通过在`Header(s) to replace`添加Origin头并在会话面板上选择`Test CORS`来轻松测试大量端点的各自CORS设置。通过选择`Test CORS`，Auth Analyzer将在重复请求之前将HTTP方法更改为`OPTIONS`

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/test_cors.png)

### 测试CSRF检查机制
通过选择`Remove Checkbox`可以删除指定参数。例如，这可以用于测试CSRF检查机制。

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/remove_csrf.png)

### 高级参数替换使用

#### JSON参数替换示例
配置JSON参数替换以修改请求正文中的特定JSON字段：

**替换嵌套JSON中的用户ID**：
- JSON路径：`$.user.id`  
- 替换值：`12345`
- 移除：否

**删除认证令牌**：
- JSON路径：`$.auth.token`
- 替换值：（空）
- 移除：是

**替换数组元素**：
- JSON路径：`$.items[0].price`
- 替换值：`99.99` 
- 移除：否

#### 表单参数替换示例
配置表单参数替换以修改请求正文中的表单数据：

**替换用户名参数**：
- 参数名：`username`
- 替换值：`admin`
- 移除：否

**删除CSRF令牌**：
- 参数名：`csrf_token`
- 替换值：（空）
- 移除：是

**替换用户角色**：
- 参数名：`role`
- 替换值：`administrator`
- 移除：否

这些高级功能与标准参数替换系统一起工作，可以同时使用来处理复杂的授权测试场景。

### 验证绕过状态
Auth Analyzer提供内置比较视图来验证两个响应之间的差异。只需标记您要分析的消息并更改消息视图`(1)`。您现在能够比较两个请求`(2) (3)`。内置的`Diff`功能将实时计算并显示两个请求之间的差异`(4)`

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/compare_view.png)

扩展的Diff视图：

![Auth Analyzer](https://github.com/simioni87/auth_analyzer/blob/main/pics/diff_view.png)

## 处理过滤器
Auth Analyzer应该处理两种类型的请求/响应：

* 响应包含必须提取的值

* 请求的资源不应该被定义的会话访问

例如，我们不想处理静态JavaScript文件，因为它对每个人都是可访问的，并且（希望）不包含任何受保护的数据。为了实现这一点，我们可以设置以下类型的过滤器：
*	仅在范围内（仅处理设置范围内的请求）
*	仅代理流量（仅处理"代理历史"的请求）
*	排除文件类型（可以排除指定的文件类型）
*	排除HTTP方法（可以排除指定的HTTP方法）
*	排除状态码（可以排除指定的状态码）
*	排除路径（可以排除指定的路径）
*	排除查询/参数（可以排除指定的查询/参数）

## 自动响应分析
*	如果`两个响应具有相同的响应正文`和`相同的响应代码`，响应将被声明为SAME
*	如果`两个响应具有相同的响应代码`和`两个响应的响应正文长度相差+-5%`，响应将被声明为SIMILAR
*	在所有其他情况下，响应将被声明为DIFFERENT

## 功能特性
*	为每个用户角色创建会话
*	重命名和删除会话
*	克隆会话
*	设置任意数量的要替换/添加的头
*	设置要删除的头
*	设置任意数量的要替换的参数
*	定义如何发现参数值（自动、静态、输入提示、从到字符串）
*	删除指定参数
*	**支持JSON Path的高级JSON参数替换**
*	**URL编码和多部分表单数据的表单参数替换**
*	**参数特定的替换和删除操作**
*	详细的过滤规则
*	每个会话的详细状态面板
*	单独暂停每个会话
*	自动更新自动提取的参数值
*	通过上下文菜单重复请求
*	表格数据过滤器
*	表格数据导出功能
*	启动/停止/暂停"Auth Analyzer"
*	单独暂停每个会话
*	将会话限制为定义的范围
*	过滤具有相同头的请求
* 丢弃原始请求功能
*	所有处理的请求和响应的详细视图
*	通过上下文菜单直接将头和/或参数发送到Auth Analyzer
*	自动保存当前配置
* 保存到文件和从文件加载当前配置
* 重复请求中的搜索功能
* 半自动化授权测试 