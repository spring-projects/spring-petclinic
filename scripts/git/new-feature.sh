#!/usr/bin/env bash
set -e

echo "=== Create New Feature Branch ==="

# Check if git repo
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "❌ Not inside a git repository"
  exit 1
fi

read -p "Enter JIRA ticket (e.g. JIRA-123): " JIRA
read -p "Enter short description: " DESC

if [[ -z "$JIRA" || -z "$DESC" ]]; then
  echo "❌ JIRA ticket and description are required"
  exit 1
fi

# Clean description
DESC_CLEAN=$(echo "$DESC" | tr '[:upper:]' '[:lower:]' | tr ' ' '-')

BRANCH="feature/${JIRA}-${DESC_CLEAN}"

echo "📌 Creating branch: $BRANCH"

git fetch origin
git checkout develop
git pull origin develop
git checkout -b "$BRANCH"
git push -u origin "$BRANCH"

echo "✅ Feature branch created and pushed successfully"
