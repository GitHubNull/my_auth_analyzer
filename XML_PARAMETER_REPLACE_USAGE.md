# XML 参数替换功能使用指南

## 概述

XML 参数替换功能允许用户在 HTTP 请求的 XML 格式消息体中动态替换或删除特定的元素或属性值。该功能使用标准的 XPath 表达式来定位需要修改的 XML 节点。

## 功能特性

- ✅ 支持标准 XPath 1.0 语法
- ✅ 支持元素和属性的值替换
- ✅ 支持删除指定的 XML 元素或属性
- ✅ 支持复杂的 XPath 查询（递归查找、条件查询等）
- ✅ 配置的保存和加载
- ✅ 实时的配置验证和错误提示

## 使用方法

### 1. 打开配置界面

1. 在 Session 配置面板中找到 **"XML 参数替换"** 按钮
2. 点击按钮打开 XML 参数替换配置对话框

### 2. 添加替换规则

在配置对话框中，您可以配置以下参数：

- **XPath 表达式**: 用于定位需要修改的 XML 元素或属性
- **替换值**: 新的值（当"移除参数"未选中时使用）
- **移除参数**: 勾选后将删除匹配的 XML 节点

### 3. XPath 语法示例

#### 基本元素访问
```xpath
/root/user/name          # 访问根节点下的 user 元素的 name 子元素
/root/users/user[1]/id   # 访问第一个 user 元素的 id 子元素
```

#### 属性访问
```xpath
/root/user/@id           # 访问 user 元素的 id 属性
/root/user[@type='admin']/@id  # 访问 type 属性为 admin 的 user 元素的 id 属性
```

#### 条件查询
```xpath
/root/users/user[@id='123']     # 查找 id 属性为 123 的 user 元素
/root/users/user[name='admin']  # 查找 name 子元素为 admin 的 user 元素
```

#### 递归搜索
```xpath
//user                   # 在整个文档中递归查找所有 user 元素
//user[@active='true']   # 递归查找所有 active 属性为 true 的 user 元素
```

#### 使用 XPath 函数
```xpath
/root/users/user[position()=1]/name    # 使用 position() 函数访问第一个用户的姓名
/root/users/user[last()]/id            # 访问最后一个用户的 ID
```

## 实际应用示例

### 示例 1: 替换用户 ID

**原始 XML:**
```xml
<root>
    <user>
        <id>12345</id>
        <name>admin</name>
    </user>
</root>
```

**配置:**
- XPath: `/root/user/id`
- 替换值: `99999`
- 移除参数: 未选中

**结果 XML:**
```xml
<root>
    <user>
        <id>99999</id>
        <name>admin</name>
    </user>
</root>
```

### 示例 2: 修改属性值

**原始 XML:**
```xml
<root>
    <user id="123" type="normal">
        <name>testuser</name>
    </user>
</root>
```

**配置:**
- XPath: `/root/user/@type`
- 替换值: `admin`
- 移除参数: 未选中

**结果 XML:**
```xml
<root>
    <user id="123" type="admin">
        <name>testuser</name>
    </user>
</root>
```

### 示例 3: 删除元素

**原始 XML:**
```xml
<root>
    <user>
        <id>123</id>
        <sessionToken>abc123def</sessionToken>
        <name>user</name>
    </user>
</root>
```

**配置:**
- XPath: `/root/user/sessionToken`
- 替换值: （留空）
- 移除参数: 选中

**结果 XML:**
```xml
<root>
    <user>
        <id>123</id>
        <name>user</name>
    </user>
</root>
```

### 示例 4: 条件查询和替换

**原始 XML:**
```xml
<root>
    <users>
        <user id="1" role="user">
            <name>normaluser</name>
        </user>
        <user id="2" role="admin">
            <name>adminuser</name>
        </user>
    </users>
</root>
```

**配置:**
- XPath: `/root/users/user[@role='admin']/name`
- 替换值: `superadmin`
- 移除参数: 未选中

**结果 XML:**
```xml
<root>
    <users>
        <user id="1" role="user">
            <name>normaluser</name>
        </user>
        <user id="2" role="admin">
            <name>superadmin</name>
        </user>
    </users>
</root>
```

## 注意事项

1. **XML 格式要求**: 该功能仅对 `Content-Type: application/xml` 或 `text/xml` 的请求生效
2. **XPath 语法**: 支持标准 XPath 1.0 语法，不支持 XPath 2.0+ 的新特性
3. **性能考虑**: 复杂的 XPath 表达式可能影响性能，建议使用尽可能简洁的路径
4. **错误处理**: 如果 XPath 表达式语法错误或无法匹配节点，将在控制台输出错误信息
5. **Content-Length**: 修改 XML 内容后会自动更新 `Content-Length` 头

## 高级用法

### 使用通配符
```xpath
/root/*/name             # 匹配 root 下任意子元素的 name 子元素
/root/user/*             # 匹配 user 元素下的所有子元素
```

### 使用位置索引
```xpath
/root/users/user[1]      # 第一个 user 元素（索引从 1 开始）
/root/users/user[last()] # 最后一个 user 元素
```

### 复合条件
```xpath
/root/users/user[@id='1' and @role='admin']  # 同时满足两个条件
/root/users/user[@id='1' or @id='2']         # 满足任一条件
```

## 故障排除

如果 XML 参数替换功能不工作，请检查以下事项：

1. **Content-Type 检查**: 确认请求的 Content-Type 为 XML 类型
2. **XPath 语法**: 验证 XPath 表达式语法正确
3. **XML 格式**: 确认 XML 格式良好且可解析
4. **控制台日志**: 查看 Burp Suite 的输出控制台获取详细错误信息

## 参考资料

- [XPath 1.0 语法参考](https://www.w3.org/TR/xpath/)
- [XPath 教程](https://www.w3schools.com/xml/xpath_intro.asp)
- [XML 规范](https://www.w3.org/TR/xml/) 