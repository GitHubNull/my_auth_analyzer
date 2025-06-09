# PowerShell Release Script - Create tag and push to GitHub for automatic release

param(
    [string]$Version = ""
)

$ErrorActionPreference = "Continue"

function Write-ColorOutput {
    param(
        [string]$Message,
        [string]$Color = "White"
    )
    Write-Host $Message -ForegroundColor $Color
}

function Test-GitRepository {
    git rev-parse --git-dir 2>$null | Out-Null
    return $LASTEXITCODE -eq 0
}

function Test-GitTagExists {
    param([string]$TagName)
    git rev-parse $TagName 2>$null | Out-Null
    return $LASTEXITCODE -eq 0
}

Write-ColorOutput "myAuthAnalyzer Auto Release Script" "Cyan"
Write-ColorOutput "=================================" "Cyan"

# Check if in git repository
if (-not (Test-GitRepository)) {
    Write-ColorOutput "Error: Not a git repository" "Red"
    exit 1
}

# Check if working directory is clean
$gitStatus = git status --porcelain
if ($gitStatus) {
    Write-ColorOutput "Error: Working directory has uncommitted changes" "Red"
    git status --short
    exit 1
}

# Get current branch
$currentBranch = git rev-parse --abbrev-ref HEAD
Write-ColorOutput "Current branch: $currentBranch" "Yellow"

# Read version from pom.xml
if (-not (Test-Path "pom.xml")) {
    Write-ColorOutput "Error: pom.xml not found" "Red"
    exit 1
}

$pomContent = Get-Content "pom.xml" -Raw
$pomVersionMatch = [regex]::Match($pomContent, '<version>([^<]+)</version>')
if ($pomVersionMatch.Success) {
    $pomVersion = $pomVersionMatch.Groups[1].Value
    Write-ColorOutput "Version in pom.xml: $pomVersion" "Yellow"
} else {
    Write-ColorOutput "Error: Cannot read version from pom.xml" "Red"
    exit 1
}

# Ask for version number
if (-not $Version) {
    Write-ColorOutput "" "White"
    Write-ColorOutput "Enter version number (e.g. 1.8.1):" "Cyan"
    $userInput = Read-Host "Version [$pomVersion]"
    
    if ($userInput) {
        $Version = $userInput
    } else {
        $Version = $pomVersion
    }
}

# Validate version format
if (-not ($Version -match '^\d+\.\d+\.\d+$')) {
    Write-ColorOutput "Error: Invalid version format, should be x.y.z" "Red"
    exit 1
}

$tagName = "v$Version"

# Check if tag already exists
if (Test-GitTagExists $tagName) {
    Write-ColorOutput "Error: Tag $tagName already exists" "Red"
    exit 1
}

Write-ColorOutput "" "White"
Write-ColorOutput "Preparing release:" "Yellow"
Write-ColorOutput "  Version: $Version" "White"
Write-ColorOutput "  Tag: $tagName" "White"
Write-ColorOutput "  Branch: $currentBranch" "White"
Write-ColorOutput "" "White"

# Confirm release
$confirm = Read-Host "Confirm release? (y/N)"
if ($confirm -ne 'y' -and $confirm -ne 'Y') {
    Write-ColorOutput "Release cancelled" "Yellow"
    exit 0
}

Write-ColorOutput "" "White"
Write-ColorOutput "Starting release process..." "Cyan"

# Update version in pom.xml if different
if ($Version -ne $pomVersion) {
    Write-ColorOutput "Updating pom.xml version to $Version..." "Yellow"
    
    $newPomContent = $pomContent -replace '<version>[^<]*</version>', "<version>$Version</version>"
    Set-Content "pom.xml" $newPomContent -Encoding UTF8
    
    git add pom.xml
    git commit -m "chore: bump version to $Version"
    
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: Failed to commit version update" "Red"
        exit 1
    }
}

# Create tag
Write-ColorOutput "Creating tag $tagName..." "Yellow"
git tag -a $tagName -m "Release version $Version"

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "Error: Failed to create tag" "Red"
    exit 1
}

# Push to remote repository
Write-ColorOutput "Pushing code and tag to remote repository..." "Yellow"
git push origin $currentBranch

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "Error: Failed to push branch" "Red"
    exit 1
}

git push origin $tagName

if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput "Error: Failed to push tag" "Red"
    exit 1
}

Write-ColorOutput "" "White"
Write-ColorOutput "Release process started successfully!" "Green"

# Get repository URL
$remoteUrl = git config --get remote.origin.url
if ($remoteUrl) {
    $repoPath = $remoteUrl -replace '.*[:/]([^/]+/[^/]+)\.git.*', '$1'
    Write-ColorOutput "Visit GitHub Actions page to view build progress:" "Green"
    Write-ColorOutput "   https://github.com/$repoPath/actions" "Cyan"
} else {
    Write-ColorOutput "Please manually visit GitHub Actions page to view build progress" "Green"
}

Write-ColorOutput "" "White"
Write-ColorOutput "New version will be automatically published to GitHub Releases after build completes" "Green" 