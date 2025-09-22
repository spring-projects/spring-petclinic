#!/bin/zsh
# Usage: ./mcp_sendpr.sh <repourl> <git_message>
# Example: ./mcp_sendpr.sh https://github.com/your/repo.git "My custom commit message"

set -e

REPOURL="$1"
GIT_MESSAGE="$2"
BRANCH_NAME="mcp-pr-$(date +%s)"

if [ -z "$REPOURL" ] || [ -z "$GIT_MESSAGE" ]; then
  echo "Usage: $0 <repourl> <git_message>"
  exit 1
fi

echo "Cloning repository: $REPOURL"
git clone "$REPOURL" repo_tmp
cd repo_tmp
echo "Creating branch: $BRANCH_NAME"
git checkout -b "$BRANCH_NAME"

echo "Copying changes from parent directory..."
cp -r ../src .
cp -r ../build.gradle ../settings.gradle . 2>/dev/null || true
cp -r ../pom.xml . 2>/dev/null || true
cp -r ../README.md . 2>/dev/null || true
cp -r ../docker-compose.yml . 2>/dev/null || true

# Add and commit changes
git add .
git commit -m "$GIT_MESSAGE"

echo "Pushing branch to remote..."
git push origin "$BRANCH_NAME"

echo "Opening pull request (requires GitHub CLI)..."
if command -v gh >/dev/null 2>&1; then
  gh pr create --base main --head "$BRANCH_NAME" --title "$GIT_MESSAGE" --body "Automated PR via MCP script."
else
  echo "GitHub CLI (gh) not found. Please install it to open a PR automatically."
  echo "Branch '$BRANCH_NAME' pushed. Open a PR manually on GitHub."
fi

cd ..
rm -rf repo_tmp
