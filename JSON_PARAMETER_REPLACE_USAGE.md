# JSON Path 参数替换功能使用说明

## 概述
全新的JSON Path参数替换功能基于业界标准的[JSON Path库](https://github.com/json-path/JsonPath)，提供了强大且灵活的JSON操作能力。您现在可以使用完整的JSON Path语法对JSON请求体进行精确的查询、替换和删除操作。

## 功能特性
- ✅ 支持完整的JSON Path标准语法
- ✅ 强大的查询能力：通配符、递归搜索、条件过滤等
- ✅ 数组操作：索引访问、切片、多维数组
- ✅ 条件查询：基于表达式的动态过滤
- ✅ 统一字符串处理：所有替换值统一作为字符串，更具通用性
- ✅ 支持删除和替换操作
- ✅ 与现有的Match and Replace功能无缝集成

## JSON Path 语法参考

### 基本语法
| 操作符 | 描述 | 示例 |
|--------|------|------|
| `$` | 根节点 | `$` |
| `@` | 当前节点（用于过滤器） | `@.price` |
| `.` | 子节点 | `$.store.book` |
| `..` | 递归下降（深度搜索） | `$..author` |
| `*` | 通配符 | `$.store.*` |
| `[]` | 数组访问/过滤器 | `$[0]`, `$[?(@.price)]` |

### 数组操作
```javascript
$[0]          // 第一个元素
$[-1]         // 最后一个元素
$[0,1]        // 第一和第二个元素
$[0:3]        // 前三个元素（切片）
$[1:4:2]      // 索引1到4，步长为2
$[*]          // 所有元素
```

### 过滤器表达式
```javascript
$[?(@.price < 10)]              // 价格小于10的项目
$[?(@.category == 'fiction')]   // 类别为fiction的项目
$[?(@.isbn)]                    // 有isbn字段的项目
$[?(@.price > 10 && @.category == 'fiction')] // 复合条件
```

### 函数
```javascript
$.books.length()                // 数组长度
$.prices.min()                  // 最小值
$.prices.max()                  // 最大值
$.prices.avg()                  // 平均值
$.prices.sum()                  // 求和
```

## 如何使用

### 1. 打开配置界面
在Auth Analyzer的会话配置面板中，点击"JSON 参数替换"按钮。

### 2. 配置替换规则
在配置对话框中填写：
- **JSON Path**：使用标准JSON Path语法指定要操作的元素
- **替换值**：新的值（统一作为字符串处理，服务端通常能正确解析）
- **移除**：勾选此选项将删除匹配的元素

**注意**：所有替换值都会作为字符串处理，这样更具通用性。即使您输入数字如`123`，也会被设置为字符串`"123"`，大多数服务端都能正确解析这种格式。只有输入`null`时才会设置为真正的null值。

### 3. 应用规则
配置完成后，规则将自动应用到所有JSON类型的请求体。

## 实用示例

### 示例1：基本属性操作
**JSON Path**: `$.user.id`
**替换值**: `999`
```json
// 原始
{"user": {"id": 123, "name": "John"}}
// 结果  
{"user": {"id": 999, "name": "John"}}
```

### 示例2：数组元素操作
**JSON Path**: `$.items[0].price`
**替换值**: `99.99`
```json
// 原始
{"items": [{"name": "Book", "price": 19.99}]}
// 结果
{"items": [{"name": "Book", "price": 99.99}]}
```

### 示例3：通配符批量操作
**JSON Path**: `$.products[*].discount`
**替换值**: `true`
```json
// 原始
{"products": [{"name": "A", "discount": false}, {"name": "B", "discount": false}]}
// 结果
{"products": [{"name": "A", "discount": true}, {"name": "B", "discount": true}]}
```

### 示例4：递归搜索替换
**JSON Path**: `$..author`
**替换值**: `"Anonymous"`
```json
// 原始
{"store": {"book": {"author": "John"}, "magazine": {"author": "Jane"}}}
// 结果
{"store": {"book": {"author": "Anonymous"}, "magazine": {"author": "Anonymous"}}}
```

### 示例5：条件过滤替换
**JSON Path**: `$.books[?(@.price > 20)].status`
**替换值**: `"expensive"`
```json
// 原始
{"books": [
  {"title": "Book A", "price": 15, "status": "normal"},
  {"title": "Book B", "price": 25, "status": "normal"}
]}
// 结果
{"books": [
  {"title": "Book A", "price": 15, "status": "normal"},
  {"title": "Book B", "price": 25, "status": "expensive"}
]}
```

### 示例6：数组切片操作
**JSON Path**: `$.items[1:3]`
**操作**: 删除
```json
// 原始
{"items": ["A", "B", "C", "D", "E"]}
// 结果
{"items": ["A", "D", "E"]}
```

### 示例7：复杂嵌套结构
**JSON Path**: `$.users[?(@.role == 'admin')].permissions[*]`
**替换值**: `"full_access"`
```json
// 原始
{"users": [
  {"role": "user", "permissions": ["read"]},
  {"role": "admin", "permissions": ["read", "write"]}
]}
// 结果
{"users": [
  {"role": "user", "permissions": ["read"]},
  {"role": "admin", "permissions": ["full_access", "full_access"]}
]}
```

## 高级用法

### 1. 条件替换
使用过滤器表达式进行有条件的替换：
```javascript
$.products[?(@.category == 'electronics' && @.price > 100)].warranty
```

### 2. 数组操作
```javascript
$[0]                    // 第一个元素
$[-1:]                  // 最后一个元素到末尾
$[1:4]                  // 索引1到3（不包含4）
$[::2]                  // 每隔一个元素
```

### 3. 多路径操作
您可以设置多个规则来处理不同的路径：
- 规则1: `$.user.id` → `999`
- 规则2: `$.user.email` → `"test@example.com"`
- 规则3: `$.settings.debug` → `true`

### 4. 使用函数
```javascript
$.items.length()        // 获取数组长度（只读）
$.prices.min()          // 获取最小值（只读）
$.prices.max()          // 获取最大值（只读）
```

## 与现有功能的集成

### 执行顺序
1. **Match and Replace** - 字符串级别的替换
2. **JSON Path参数替换** - JSON结构级别的操作  
3. **Token参数替换** - 传统的参数替换

### 生效条件
- 请求Content-Type必须为`application/json`
- JSON格式必须有效
- JSON Path表达式必须符合语法规范

## 错误处理

### 常见错误及解决方案

1. **路径未找到**
   - 检查JSON Path语法是否正确
   - 确认目标路径在JSON中存在

2. **无效的JSON Path**
   - 使用标准JSON Path语法
   - 参考[JSON Path在线测试工具](https://jsonpath.herokuapp.com/)

3. **类型转换错误**
   - 确保替换值的类型合适
   - 使用`null`表示空值，`true`/`false`表示布尔值

## 调试技巧

### 1. 查看日志
Burp Suite的输出面板会显示详细的操作日志：
```
正在应用JSON Path替换: $.user.id -> 999
成功替换JSON路径: $.user.id = 999
JSON参数替换成功应用，共处理 3 个规则
```

### 2. 测试JSON Path
使用在线工具测试您的JSON Path表达式：
- [JSONPath Online Evaluator](https://jsonpath.herokuapp.com/)
- [JSONPath Tester](https://jsonpath.com/)

### 3. 逐步验证
1. 先使用简单的路径如`$.user.id`
2. 确认基本功能工作后再使用复杂表达式
3. 使用Burp的Request/Response对比功能验证效果

## 性能考虑

- JSON Path处理比简单字符串替换略慢，但提供了更强大的功能
- 复杂的过滤器表达式可能影响性能
- 建议在生产环境中谨慎使用递归搜索（`..`）操作符

## 配置管理

### 保存和加载
- JSON Path规则会与其他会话配置一起自动保存
- 支持导出/导入配置文件
- 会话克隆时自动复制所有JSON Path规则

### 最佳实践
1. 为每个规则添加注释（在替换值中）
2. 使用具体的路径而非过于宽泛的通配符
3. 定期测试规则的有效性
4. 在开发环境中充分测试后再用于生产

## 故障排除

如果JSON Path替换没有生效，请检查：

1. **请求类型**：确保是JSON请求（Content-Type: application/json）
2. **JSON格式**：确保请求体是有效的JSON
3. **路径语法**：使用标准JSON Path语法，以`$`开头
4. **插件状态**：确保Auth Analyzer已启动
5. **会话配置**：确认会话已正确配置并激活

## 总结

新的JSON Path参数替换功能提供了：
- 🔥 **强大的查询能力**：支持完整的JSON Path语法
- 🎯 **精确的操作**：可以精确定位和修改任何JSON元素  
- 🚀 **高度灵活**：支持条件查询、数组操作、递归搜索等
- 🔧 **易于使用**：直观的界面配置
- 📈 **可扩展性**：可以处理任意复杂的JSON结构

这个功能特别适合：
- API授权测试
- 用户权限验证  
- 参数篡改测试
- 数据注入测试
- 复杂JSON结构的自动化测试 