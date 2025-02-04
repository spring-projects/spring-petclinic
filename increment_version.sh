#!/bin/bash

# Fetch the latest tag from the repository
latest_tag=$(git describe --tags --abbrev=0)

# If no tags exist, start with version 1.0.0
if [ -z "$latest_tag" ]; then
    echo "No tags found. Starting from v1.0.0"
    new_version="v1.0.0"
else
    # Extract major and minor version from the latest tag
    version_pattern="v([0-9]+)\.([0-9]+)\.([0-9]+)"
    if [[ $latest_tag =~ $version_pattern ]]; then
        major="${BASH_REMATCH[1]}"
        minor="${BASH_REMATCH[2]}"
        patch="${BASH_REMATCH[3]}"

        # Increment the minor version
        minor=$((minor + 1))
        new_version="v${major}.${minor}.0"  # Reset patch version to 0 when incrementing minor
    else
        echo "Invalid tag format. Expected format: vX.Y.Z"
        exit 1
    fi
fi

# Print new version
echo "New version: $new_version"

# Create the new Git tag
git tag -a "$new_version" -m "Version $new_version"

# Push the new tag to the repository
git push origin "$new_version"

# Optionally, you can print the created tag to verify
git tag -l
