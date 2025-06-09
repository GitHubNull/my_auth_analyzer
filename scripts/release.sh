#!/bin/bash

# 发布脚本 - 用于创建tag并推送到GitHub自动触发发布

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 myAuthAnalyzer 自动发布脚本${NC}"
echo "================================="

# 检查是否在Git仓库中
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}❌ 错误: 当前目录不是Git仓库${NC}"
    exit 1
fi

# 检查工作区是否干净
if ! git diff-index --quiet HEAD --; then
    echo -e "${RED}❌ 错误: 工作区有未提交的更改，请先提交所有更改${NC}"
    git status --short
    exit 1
fi

# 获取当前分支
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo -e "${YELLOW}📍 当前分支: ${CURRENT_BRANCH}${NC}"

# 从pom.xml读取版本号
POM_VERSION=$(grep -o '<version>[^<]*</version>' pom.xml | head -1 | sed 's/<version>\(.*\)<\/version>/\1/')
echo -e "${YELLOW}📋 pom.xml中的版本: ${POM_VERSION}${NC}"

# 询问版本号
echo ""
echo -e "${BLUE}请输入要发布的版本号 (例如: 1.8.1):${NC}"
read -p "版本号 [${POM_VERSION}]: " VERSION

# 如果用户没有输入，使用pom.xml中的版本
if [ -z "$VERSION" ]; then
    VERSION=$POM_VERSION
fi

# 验证版本号格式
if ! [[ $VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo -e "${RED}❌ 错误: 版本号格式不正确，应该是 x.y.z 格式${NC}"
    exit 1
fi

TAG_NAME="v${VERSION}"

# 检查tag是否已存在
if git rev-parse "$TAG_NAME" >/dev/null 2>&1; then
    echo -e "${RED}❌ 错误: Tag ${TAG_NAME} 已存在${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}准备创建发布:${NC}"
echo "  版本: ${VERSION}"
echo "  Tag: ${TAG_NAME}"
echo "  分支: ${CURRENT_BRANCH}"
echo ""

# 确认发布
read -p "确认发布? (y/N): " CONFIRM
if [[ ! $CONFIRM =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}🚫 发布已取消${NC}"
    exit 0
fi

echo ""
echo -e "${BLUE}🔄 开始发布流程...${NC}"

# 更新pom.xml中的版本号（如果不同）
if [ "$VERSION" != "$POM_VERSION" ]; then
    echo -e "${YELLOW}📝 更新pom.xml版本号到 ${VERSION}...${NC}"
    sed -i.bak "0,/<version>[^<]*<\/version>/s/<version>[^<]*<\/version>/<version>${VERSION}<\/version>/" pom.xml
    rm -f pom.xml.bak
    
    # 提交版本更新
    git add pom.xml
    git commit -m "chore: bump version to ${VERSION}"
fi

# 创建tag
echo -e "${YELLOW}🏷️  创建tag ${TAG_NAME}...${NC}"
git tag -a "$TAG_NAME" -m "Release version ${VERSION}"

# 推送到远程仓库
echo -e "${YELLOW}🚀 推送代码和tag到远程仓库...${NC}"
git push origin "$CURRENT_BRANCH"
git push origin "$TAG_NAME"

echo ""
echo -e "${GREEN}✅ 发布流程已启动！${NC}"
echo -e "${GREEN}🔗 请访问 GitHub Actions 页面查看构建进度:${NC}"
echo -e "${BLUE}   https://github.com/$(git config --get remote.origin.url | sed 's/.*://.*\///;s/.git$//')/actions${NC}"
echo ""
echo -e "${GREEN}📦 构建完成后，新版本将自动发布到 GitHub Releases${NC}" 