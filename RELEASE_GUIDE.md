# 📦 自动发布指南

本项目已配置了 GitHub Actions 自动化发布系统，当您推送 tag 到 GitHub 时，会自动编译打包并发布新版本到 GitHub Releases。

## 🚀 快速发布

### 方法一：使用发布脚本（推荐）

#### Linux/macOS 用户
```bash
# 给脚本添加执行权限
chmod +x scripts/release.sh

# 运行发布脚本
./scripts/release.sh
```

#### Windows 用户
```powershell
# 在 PowerShell 中运行
.\scripts\release.ps1

# 或指定版本号
.\scripts\release.ps1 -Version "1.8.1"
```

### 方法二：手动创建 tag

```bash
# 1. 确保所有更改已提交
git add .
git commit -m "你的提交信息"

# 2. 创建并推送 tag
git tag -a v1.8.1 -m "Release version 1.8.1"
git push origin v1.8.1
```

## 🔧 工作流说明

### 自动发布工作流 (release.yml)
- **触发条件**: 推送以 `v` 开头的 tag（如 `v1.8.1`）
- **功能**:
  - 自动设置 Java 8 环境
  - 使用 Maven 编译打包项目
  - 创建 GitHub Release
  - 上传 jar 文件到 Release

### 持续集成工作流 (build.yml)
- **触发条件**: 推送到 main/master/develop 分支或创建 Pull Request
- **功能**:
  - 编译项目
  - 运行测试
  - 上传构建产物作为 artifacts

## 📋 发布流程

1. **准备阶段**
   - 确保所有代码更改已提交
   - 更新版本号（可选，脚本会自动处理）
   - 测试本地构建：`mvn clean package`

2. **创建发布**
   - 运行发布脚本或手动创建 tag
   - 推送 tag 到 GitHub

3. **自动构建**
   - GitHub Actions 自动触发
   - 编译打包项目
   - 创建 GitHub Release

4. **发布验证**
   - 检查 GitHub Actions 构建状态
   - 验证 Release 页面的文件
   - 测试下载的 jar 文件

## 📝 版本号规范

项目使用 [语义化版本控制](https://semver.org/)：

- **主版本号 (MAJOR)**: 不兼容的 API 修改
- **次版本号 (MINOR)**: 向下兼容的功能性新增
- **修订号 (PATCH)**: 向下兼容的问题修正

示例：`1.8.1`
- `1`: 主版本号
- `8`: 次版本号  
- `1`: 修订号

## 🔍 常见问题

### Q: 如何查看构建状态？
A: 访问项目的 GitHub Actions 页面：`https://github.com/你的用户名/你的仓库名/actions`

### Q: 构建失败了怎么办？
A: 
1. 检查 GitHub Actions 的错误日志
2. 确保本地可以成功执行 `mvn clean package`
3. 检查 pom.xml 配置是否正确

### Q: 如何删除错误的 tag？
A:
```bash
# 删除本地 tag
git tag -d v1.8.1

# 删除远程 tag
git push origin :refs/tags/v1.8.1
```

### Q: 如何修改发布说明？
A: 编辑 `.github/workflows/release.yml` 文件中的 `创建发布说明` 步骤。

## 🛠️ 自定义配置

### 修改触发条件
编辑 `.github/workflows/release.yml` 中的 `on.push.tags` 部分：

```yaml
on:
  push:
    tags:
      - 'v*'        # 所有以 v 开头的 tag
      - 'release-*' # 所有以 release- 开头的 tag
```

### 添加构建平台
默认在 Ubuntu 上构建，可以添加多平台支持：

```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
runs-on: ${{ matrix.os }}
```

## 📚 相关链接

- [GitHub Actions 文档](https://docs.github.com/cn/actions)
- [Maven 官方文档](https://maven.apache.org/guides/)
- [语义化版本控制](https://semver.org/lang/zh-CN/)

---

如有问题，请在 Issues 中反馈。 