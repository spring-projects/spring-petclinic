#!/bin/bash

# This script handles test dataset instance processing for SWE benchmarks
# It accepts parameters for repository, commit, patches, test information, etc.

set -o pipefail

# Parse input parameters
REPO="$1"
COMMIT="$2"
PATCH="$3"
TEST_PATCH="$4"
FAIL_TO_PASS="$5"
PASS_TO_PASS="$6"
TEST_ARGS="$7"
IS_MAVEN=$(echo "$8" | tr '[:upper:]' '[:lower:]')
JAVA_VERSION="$9"
INSTANCE_ID="${10}"

# Validate required parameters
if [[ -z "$REPO" || "$REPO" == "null" ]]; then
  echo "❌ Required parameter 'repo' is missing"
  exit 1
fi

# Default Java version if not specified
if [[ -z "$JAVA_VERSION" || "$JAVA_VERSION" == "null" ]]; then
  JAVA_VERSION="24"
  echo "ℹ️ Java version not specified, using default: $JAVA_VERSION"
fi

# Convert is_maven to lowercase
IS_MAVEN=$(echo "$IS_MAVEN" | tr '[:upper:]' '[:lower:]')

# Use repository name for Docker image if instance ID is not provided
if [[ -z "$INSTANCE_ID" || "$INSTANCE_ID" == "null" ]]; then
  INSTANCE_ID="auto-$(basename "$REPO" | tr '[:upper:]' '[:lower:]')-$(date +%s)"
  echo "ℹ️ Auto-generated instance ID: $INSTANCE_ID"
fi

REPO_URL="git@github.com:$REPO"

# Function to determine container naming strategy
determine_container_name() {
  local name_by_repo="$1"
  local instance_id="$2"
  local repo="$3"

  if [ "$name_by_repo" = true ]; then
    # Use repository name (replace slashes with dashes, convert to lowercase)
    local repo_safe=$(echo "$repo" | tr '/' '-' | tr '[:upper:]' '[:lower:]')
    echo "swe-benchmark-$repo_safe"
  else
    # Use instance ID (default, convert to lowercase)
    echo "swe-benchmark-$(echo "$instance_id" | tr '[:upper:]' '[:lower:]')"
  fi
}

# Function to check Docker environment
check_docker_environment() {
  echo "🐳 Checking Docker environment..."
  if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker and try again."
    echo "   Visit: https://docs.docker.com/get-docker/"
    exit 1
  fi

  if ! docker info &> /dev/null; then
    echo "❌ Docker daemon is not running or not accessible."
    echo "   Please start Docker Desktop or Docker daemon and try again."
    echo "   On macOS: Start Docker Desktop application"
    echo "   On Linux: sudo systemctl start docker"
    exit 1
  fi

  echo "✅ Docker environment is ready"
}

# Function to create Dockerfile
create_dockerfile() {
  local java_version="$1"

  cat > Dockerfile << EOF
FROM eclipse-temurin:${java_version}-jdk

# Install Git, jq, and other utilities (Docker CLI will be available via socket mount)
RUN apt-get update && \\
    apt-get install -y \\
    git \\
    jq \\
    patch \\
    openssh-client \\
    wget \\
    unzip \\
    ca-certificates \\
    curl && \\
    rm -rf /var/lib/apt/lists/*

# Install Docker CLI only (no daemon needed)
RUN curl -fsSL https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz | \\
    tar xzf - --strip 1 -C /usr/local/bin docker/docker

# Set up SSH for git clone (if needed)
RUN mkdir -p /root/.ssh && ssh-keyscan github.com >> /root/.ssh/known_hosts

# Set working directory
WORKDIR /workspace

# Default command
CMD ["/bin/bash"]
EOF
}

# Function to create setup script
create_setup_script() {
  SETUP_SCRIPT="setup_project.sh"
  cat > "$SETUP_SCRIPT" << 'EOF'
#!/bin/bash

set -e

REPO_URL="$1"
COMMIT="$2"
IS_MAVEN="$3"

# Source common helper functions
source /workspace/common_functions.sh

echo "📋 Setting up project"
echo "📦 Repository: $REPO_URL"
echo "🏷️  Commit: $COMMIT"

# Clone repository
REPO_NAME=$(basename "$REPO_URL" .git)
echo "📥 Cloning repository..."
if ! git clone "$REPO_URL" "$REPO_NAME"; then
  echo "❌ Failed to clone repository. Trying HTTPS..."
  HTTPS_URL=$(echo "$REPO_URL" | sed 's|git@github.com:|https://github.com/|')
  git clone "$HTTPS_URL" "$REPO_NAME"
fi

cd "$REPO_NAME"

# Checkout specific commit
echo "🔁 Checking out commit $COMMIT..."
git checkout "$COMMIT"

# Make gradlew executable if it exists
if [ -f "./gradlew" ]; then
  chmod +x ./gradlew

  # Check if gradle-wrapper.jar exists, if not generate it
  if [ ! -f "./gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "🔧 Gradle wrapper JAR missing, initializing wrapper..."
    # Always use system Gradle first to generate wrapper
    if command -v gradle &> /dev/null; then
      echo "🔧 Using system Gradle to initialize wrapper..."
      gradle wrapper --no-daemon || {
        echo "❌ Failed: Failed to initialize Gradle wrapper using system Gradle"
        exit 1
      }
    else
      # Install Gradle temporarily to generate wrapper
      echo "🔧 Installing Gradle to initialize wrapper..."
      wget -O gradle.zip https://services.gradle.org/distributions/gradle-9.0.0-bin.zip || {
        echo "❌ Failed: Failed to download Gradle"
        exit 1
      }
      unzip -q gradle.zip || {
        echo "❌ Failed: Failed to unzip Gradle"
        exit 1
      }
      chmod +x gradle-9.0.0/bin/gradle
      ./gradle-9.0.0/bin/gradle wrapper --no-daemon || {
        echo "❌ Failed: Failed to initialize Gradle wrapper using downloaded Gradle"
        exit 1
      }
      rm -rf gradle.zip gradle-9.0.0
    fi

    # Verify wrapper was created successfully
    if [ ! -f "./gradle/wrapper/gradle-wrapper.jar" ]; then
      echo "❌ Failed: Failed to create Gradle wrapper JAR"
      exit 1
    fi

    echo "✅ Gradle wrapper initialized"
  fi
fi

# Make mvn executable if it exists
if [ -f "./mvnw" ]; then
  chmod +x ./mvnw
fi

# Compile project and download dependencies
echo "🏗️  Compiling project and downloading dependencies..."
if [[ "$IS_MAVEN" == "true" ]]; then
  # Try Maven wrapper first, then fallback to system Maven
  MAVEN_CMD=""
  if [ -f "./mvnw" ]; then
    echo "🔧 Using Maven wrapper (./mvnw)"
    MAVEN_CMD="./mvnw"
  elif command -v mvn &> /dev/null; then
    echo "🔧 Using system Maven"
    MAVEN_CMD="mvn"
  else
    echo "🔧 Maven not found, installing..."
    install_build_tools
    MAVEN_CMD="mvn"
  fi

  echo "🔧 Running Maven compile: $MAVEN_CMD compile test-compile"
  $MAVEN_CMD compile test-compile 2>&1 | tee compile_output.log
else
  # Try Gradle wrapper first, then fallback to system Gradle
  GRADLE_CMD=""
  if [ -f "./gradlew" ]; then
    echo "🔧 Using Gradle wrapper (./gradlew)"
    GRADLE_CMD="./gradlew"
  elif command -v gradle &> /dev/null; then
    echo "🔧 Using system Gradle"
    GRADLE_CMD="gradle"
  else
    echo "🔧 Gradle not found, installing..."
    install_build_tools
    GRADLE_CMD="gradle"
  fi

  echo "🔧 Running Gradle compile: $GRADLE_CMD compileJava compileTestJava"
  $GRADLE_CMD compileJava compileTestJava 2>&1 | tee compile_output.log
fi

echo "✅ Project setup and compilation completed"
EOF
  chmod +x "$SETUP_SCRIPT"
}

# Function to create test script
create_test_script() {
  local patch="$1"
  local test_patch="$2" 
  local instance_id="$3"
  local fail_to_pass="$4"
  local pass_to_pass="$5"
  local test_args="$6"
  local is_maven="$7"
  local commit="$8"
  local repo_url="$9"

  # Write parameters to a separate file to avoid quote issues
  PARAMS_FILE="test_params.env"
  cat > "$PARAMS_FILE" << EOF
PATCH=$(printf '%q' "$patch")
TEST_PATCH=$(printf '%q' "$test_patch")
INSTANCE_ID=$(printf '%q' "$instance_id")
FAIL_TO_PASS=$(printf '%q' "$fail_to_pass")
PASS_TO_PASS=$(printf '%q' "$pass_to_pass")
TEST_ARGS=$(printf '%q' "$test_args")
IS_MAVEN=$(printf '%q' "$is_maven")
COMMIT=$(printf '%q' "$commit")
REPO_URL=$(printf '%q' "$repo_url")
EOF

  TEST_SCRIPT="run_tests.sh"
  cat > "$TEST_SCRIPT" << 'EOF'
#!/bin/bash
set -o pipefail

# Resolve module (Maven/Gradle) for a given fully-qualified test name (optionally with method).
# Prints the module directory relative to repo root (e.g., "service/order") and returns 0 on success.
find_module_for_test() {
  local fqn="$1"

  # Normalize: strip method suffix (#method or (..)) and any "module::" prefix
  local fqn_no_method="${fqn%%[#(]*}"
  fqn_no_method="${fqn_no_method#*::}"
  fqn_no_method="$(echo "$fqn_no_method" | xargs)"

  if [[ -z "$fqn_no_method" ]]; then
    return 1
  fi

  local class_name="${fqn_no_method##*.}"
  local package_name="${fqn_no_method%.*}"
  local pkg_path="${package_name//./\/}"

  # roots to search for tests
  local roots=(
    "src/test/java" "src/test/kotlin" "src/test/groovy"
    "src/integrationTest/java" "src/integrationTest/kotlin" "src/integrationTest/groovy"
    "src/it/java" "src/it/kotlin" "src/it/groovy"
  )
  local exts=("java" "kt" "groovy")

  local matches=()
  for root in "${roots[@]}"; do
    for ext in "${exts[@]}"; do
      local suffix="$root"
      if [[ -n "$pkg_path" ]]; then
        suffix="$suffix/$pkg_path/$class_name.$ext"
      else
        suffix="$suffix/$class_name.$ext"
      fi
      # find files under any module
      while IFS= read -r f; do
        matches+=("$f")
      done < <(find . -type f -path "*/$suffix" 2>/dev/null)
    done
  done

  if [[ ${#matches[@]} -eq 0 ]]; then
    return 1
  fi

  local best_mod="" best_score=999999
  for f in "${matches[@]}"; do
    local mod="${f%/src/*}"
    mod="${mod#./}"

    # score: prefer modules that look like Maven/Gradle projects & shallower paths
    local score=0
    [[ -f "$mod/pom.xml" ]] && score=$((score-3))
    [[ -f "$mod/build.gradle" || -f "$mod/build.gradle.kts" ]] && score=$((score-3))

    # bias by build tool if known
    if [[ "$IS_MAVEN" == "true" && -f "$mod/pom.xml" ]]; then
      score=$((score-2))
    fi
    if [[ "$IS_MAVEN" == "false" && ( -f "$mod/build.gradle" || -f "$mod/build.gradle.kts" ) ]]; then
      score=$((score-2))
    fi

    # shallower is better
    local depth="${mod//[^\/]/}"
    score=$((score + ${#depth}))

    if (( score < best_score )) || [[ -z "$best_mod" ]]; then
      best_mod="$mod"
      best_score=$score
    fi
  done

  if [[ -n "$best_mod" ]]; then
    echo "$best_mod"
    return 0
  fi
  return 1
}

# Note: Not using 'set -e' to allow continuation even if patches fail

# Load parameters from environment file
source /workspace/test_params.env

# Source common helper functions
source /workspace/common_functions.sh

echo "📋 Running tests for instance: $INSTANCE_ID"

# Navigate to the already cloned and compiled project (robust detection)
project_dir=""
# Prefer a directory with a .git folder
for dir in /workspace/*; do
  if [ -d "$dir" ] && [ -d "$dir/.git" ]; then
    project_dir="$dir"
    break
  fi
done
# Fallback to first directory under /workspace
if [ -z "$project_dir" ]; then
  for dir in /workspace/*; do
    if [ -d "$dir" ]; then
      project_dir="$dir"
      break
    fi
  done
fi
# If still not found, attempt to clone using REPO_URL
if [ -z "$project_dir" ]; then
  if [[ -n "$REPO_URL" && "$REPO_URL" != "null" ]]; then
    repo_name=$(basename "$REPO_URL" .git)
    echo "📥 Cloning repository into container: $REPO_URL"
    if ! git clone "$REPO_URL" "/workspace/$repo_name"; then
      echo "❌ SSH clone failed, trying HTTPS..."
      https_url=$(echo "$REPO_URL" | sed 's|git@github.com:|https://github.com/|')
      git clone "$https_url" "/workspace/$repo_name"
    fi
    project_dir="/workspace/$repo_name"
  fi
fi
# Final check
if [ -z "$project_dir" ] || [ ! -d "$project_dir" ]; then
  echo "❌ No project directory found in prepared container"
  exit 1
fi

cd "$project_dir"
REPO_NAME=$(basename "$project_dir")
echo "📁 Working in project directory: $REPO_NAME"

# Get the current commit hash from environment or git
COMMIT_HASH=$(git rev-parse HEAD)
echo "🔄 Current commit: $COMMIT_HASH"

# Reset to clean state before applying patches
echo "🧹 Resetting to clean state..."
git reset --hard HEAD
git clean -fd

# Verify we're in a clean state
if [ -n "$(git status --porcelain)" ]; then
  echo "⚠️  Warning: Repository not completely clean after reset"
  git status --short
fi

# Checkout specific commit
echo "🔁 Checking out commit $COMMIT..."
git checkout "$COMMIT"
git reset --hard HEAD
git clean -fd

# Define run_test_class early so it can be used before later redefinition
run_test_class() {
  local test_name="$1"
  local test_type="$2"

  echo "Running $test_type test: $test_class"

  # Check if we have test_args and if it's not "null"
  local test_args_param=""
  if [[ -n "$TEST_ARGS" && "$TEST_ARGS" != "null" ]]; then
    test_args_param="$TEST_ARGS"
    echo "📋 Using test args: $test_args_param"
  fi

  # Split "module::fqn" if provided
  local module_name=""
  if [[ "$test_name" == *"::"* ]]; then
    module_name="${test_name%%::*}"
    test_name="${test_name#*::}"
  fi

  # If module not given, try to auto-detect via package path
  if [[ -z "$module_name" ]]; then
    if module_name="$(find_module_for_test "$test_name")"; then
      echo "🧭 Auto-detected module for test '$test_name' -> '$module_name'"
    else
      echo "⚠️  Could not auto-detect module for test '$test_name'. Falling back to root."
    fi
  fi

  if [[ "$IS_MAVEN" == "true" ]]; then
    # Try Maven wrapper first, then fallback to system Maven
    MAVEN_CMD=""
    if [ -f "./mvnw" ]; then
      echo "🔧 Using Maven wrapper (./mvnw)"
      MAVEN_CMD="./mvnw"
    elif command -v mvn &> /dev/null; then
      echo "🔧 Using system Maven"
      MAVEN_CMD="mvn"
    else
      echo "🔧 Maven not found, installing..."
      install_build_tools
      MAVEN_CMD="mvn"
    fi

    # Run spotless and spring check
    echo "🔧 Running Maven format commands:"
    $MAVEN_CMD spring-javaformat:apply | tee test_output.log
    $MAVEN_CMD spotless:apply | tee test_output.log

    # Maven test execution with test_args
    if [[ -n "$module_name" && "$module_name" != "." ]]; then
      echo "🔧 Running Maven command: $MAVEN_CMD test $test_args_param -pl $module_name -Dtest=\"$test_name\""
      $MAVEN_CMD test $test_args_param -pl "$module_name" -Dtest="$test_name" -Dsurefire.failIfNoSpecifiedTests=true 2>&1 | tee test_output.log
    else
      echo "🔧 Running Maven command: $MAVEN_CMD test $test_args_param -Dtest=\"$test_name\""
      $MAVEN_CMD test $test_args_param -Dtest="$test_name" -Dsurefire.failIfNoSpecifiedTests=true 2>&1 | tee test_output.log
    fi
    exit_code=$?

    # Check if the test wasn't found
    if grep -q "No tests matching pattern" test_output.log || grep -q "No tests were executed" test_output.log; then
      echo "❌ $test_type test NOT FOUND: $test_class"
      return 2
    elif [ $exit_code -eq 0 ]; then
      echo "✅ $test_type test PASSED: $test_class"
      return 0
    else
      echo "❌ $test_type test FAILED: $test_class"
      return 1
    fi
  else
    # Try Gradle wrapper first, then fallback to system Gradle
    GRADLE_CMD=""
    if [ -f "./gradlew" ]; then
      echo "🔧 Using Gradle wrapper (./gradlew)"
      GRADLE_CMD="./gradlew"
    elif command -v gradle &> /dev/null; then
      echo "🔧 Using system Gradle"
      GRADLE_CMD="gradle"
    else
      echo "🔧 Gradle not found, installing..."
      install_build_tools
      GRADLE_CMD="gradle"
    fi

    # Run spotless and spring check
    echo "🔧 Running Gradle format commands:"
    $GRADLE_CMD format | tee compile_output.log
    $GRADLE_CMD spotlessApply | tee compile_output.log

    # Gradle test execution with test_args
    local gradle_task="test"
    # Gradle: derive :a:b:c:test task for the module when known
    local gradle_task="test"
    if [[ -n "$module_name" && "$module_name" != "." ]]; then
      gradle_task=":${module_name//\//:}:test"
    fi
    echo "🔧 Running Gradle command: $GRADLE_CMD $gradle_task $test_args_param --tests \"$test_name\""
    $GRADLE_CMD $gradle_task $test_args_param --tests "$test_name" 2>&1 | tee test_output.log
    exit_code=$?

    # Check if the test wasn't found
    if grep -q "No tests found for given includes" test_output.log || grep -q "No tests found matching" test_output.log; then
      echo "❌ $test_type test FAILED (NOT FOUND): $test_class"
      return 2
    elif [ $exit_code -eq 0 ]; then
      echo "✅ $test_type test PASSED: $test_class"
      return 0
    else
      echo "❌ $test_type test FAILED: $test_class"
      return 1
    fi
  fi
}

# Apply test patch with error handling first
echo "🧪 Applying test patch..."
if [ "$TEST_PATCH" != "null" ] && [ -n "$TEST_PATCH" ]; then
  # Try dry run first to validate patch
  if echo "$TEST_PATCH" | patch -p1 --dry-run > /dev/null 2>&1; then
    echo "$TEST_PATCH" | patch -p1
    echo "✅ Test patch applied successfully"
  else
    echo "⚠️  Test patch dry run failed, trying with force..."
    if echo "$TEST_PATCH" | patch -p1 --force --reject-file=test.rej; then
      echo "✅ Test patch applied with force"
      if [ -f "test.rej" ]; then
        echo "⚠️ Failed: Some parts rejected - see test.rej"
        cat test.rej
        exit 1
      fi
    else
      echo "❌ Failed: Test patch failed completely"
      exit 1
    fi
  fi
else
  echo "ℹ️  No test patch to apply"
fi

# AFTER APPLYING TEST PATCH: Run FAIL_TO_PASS again and gate
echo "👉 Running FAIL_TO_PASS tests after applying test patch (without golden patch)..."
if [[ "$FAIL_TO_PASS" != "[]" && "$FAIL_TO_PASS" != "null" ]]; then
  TP_FAIL_TESTS=$(echo "$FAIL_TO_PASS" | jq -r '.[]' 2>/dev/null || echo "$FAIL_TO_PASS" | tr -d '[]"' | tr ',' '\n')
  tp_fail_to_pass_count=0
  tp_fail_to_pass_success=0
  tp_fail_to_pass_passed_list=""

  for test in $TP_FAIL_TESTS; do
    if [[ -n "$test" && "$test" != "null" ]]; then
      clean_test=$(echo "$test" | sed 's/^src://')
      ((tp_fail_to_pass_count++))
      run_test_class "$clean_test" "FAIL_TO_PASS"
      result=$?
      if [ $result -eq 0 ]; then
        ((tp_fail_to_pass_success++))
        if [[ -z "$tp_fail_to_pass_passed_list" ]]; then
          tp_fail_to_pass_passed_list="$clean_test"
        else
          tp_fail_to_pass_passed_list="$tp_fail_to_pass_passed_list, $clean_test"
        fi
      elif [ $result -eq 2 ]; then
        echo "⚠️ WARNING: FAIL_TO_PASS test '$clean_test' could not be found or executed"
      fi
    fi
  done

  echo "📊 FAIL_TO_PASS summary with test patch: $tp_fail_to_pass_success of $tp_fail_to_pass_count tests passed"
  if [ ${tp_fail_to_pass_success:-0} -gt 0 ]; then
    echo "❌  Failed: FAIL_TO_PASS passed with test patch and without golden patch: $tp_fail_to_pass_passed_list"
    exit 1
  fi
else
  echo "No FAIL_TO_PASS tests to run after test patch"
fi

# Now apply source (golden) patch with error handling
echo "🩹 Applying source patch..."
if [ "$PATCH" != "null" ] && [ -n "$PATCH" ]; then
  # Show patch content for debugging
  echo "📄 Source patch content (first 10 lines):"
  echo "$PATCH" | head -10
  echo "..."

  # Try dry run first to validate patch
  echo "🔍 Running patch dry run..."
  if echo "$PATCH" | patch -p1 --dry-run > patch_dry_run.log 2>&1; then
    echo "$PATCH" | patch -p1
    echo "✅ Source patch applied successfully"
  else
    echo "⚠️  Source patch dry run failed, analyzing..."
    echo "📋 Dry run output:"
    cat patch_dry_run.log

    # Check if target files exist
    TARGET_FILES=$(echo "$PATCH" | grep "^+++" | sed 's/^+++ [ab]\///' | head -5)
    echo "🔍 Checking target files:"
    for file in $TARGET_FILES; do
      if [ -f "$file" ]; then
        echo "✅ Found: $file"
        echo "📄 Current content around line context:"
        # Show some context from the file
        head -50 "$file" | tail -20
      else
        echo "❌  Failed: Missing: $file"
        exit 1
      fi
    done

    echo "⚠️  Trying patch with force and different options..."
    # Try with different patch options
    if echo "$PATCH" | patch -p1 --force --reject-file=source.rej --no-backup-if-mismatch; then
      echo "✅ Source patch applied with force"
      if [ -f "source.rej" ]; then
        cat source.rej
        echo "⚠️   Failed: Some parts rejected - see source.rej:"
        exit 1
      fi
    else
      echo "❌  Failed: Source patch failed completely - continuing anyway to allow test patch"
      exit 1
    fi
  fi
else
  echo "ℹ️  No source patch to apply"
fi

# Show what files were modified
echo "📝 Modified files after patches:"
git status --short

# Add any new files created by patches to git so they get cleaned up on next reset
echo "📋 Adding new files to git for proper cleanup on next run..."
git add -A
if [ -n "$(git status --porcelain)" ]; then
  echo "✅ Added patch-created files to git index:"
  git status --short
else
  echo "ℹ️  No new files to add to git index"
fi

echo "FAIL_TO_PASS tests: $FAIL_TO_PASS"
echo "PASS_TO_PASS tests: $PASS_TO_PASS"

# Parse and run FAIL_TO_PASS tests
if [[ "$FAIL_TO_PASS" != "[]" && "$FAIL_TO_PASS" != "null" ]]; then
  FAIL_TESTS=$(echo "$FAIL_TO_PASS" | jq -r '.[]' 2>/dev/null || echo "$FAIL_TO_PASS" | tr -d '[]"' | tr ',' '\n')
  fail_to_pass_count=0
  fail_to_pass_success=0
  base_fail_to_pass_failed_list=""

  for test in $FAIL_TESTS; do
    if [[ -n "$test" && "$test" != "null" ]]; then
      # Remove "src:" prefix if present
      clean_test=$(echo "$test" | sed 's/^src://')
      ((fail_to_pass_count++))

      run_test_class "$clean_test" "FAIL_TO_PASS"
      result=$?

      if [ $result -eq 0 ]; then
        ((fail_to_pass_success++))
      else
        if [[ -z "$base_fail_to_pass_failed_list" ]]; then
          base_fail_to_pass_failed_list="$clean_test"
        else
          base_fail_to_pass_failed_list="$base_fail_to_pass_failed_list, $clean_test"
        fi
      fi
    fi
  done

  echo "📊 FAIL_TO_PASS summary: $fail_to_pass_success of $fail_to_pass_count tests passed"
  if [ ${fail_to_pass_count:-0} -gt 0 ] && [ ${fail_to_pass_success:-0} -lt ${fail_to_pass_count:-0} ]; then
    echo "❌  Failed: FAIL_TO_PASS tests must all pass. Failed tests: $base_fail_to_pass_failed_list"
    exit 1
  fi
else
  echo "No FAIL_TO_PASS tests to run"
fi

# Parse and run PASS_TO_PASS tests
if [[ "$PASS_TO_PASS" != "[]" && "$PASS_TO_PASS" != "null" ]]; then
  PASS_TESTS=$(echo "$PASS_TO_PASS" | jq -r '.[]' 2>/dev/null || echo "$PASS_TO_PASS" | tr -d '[]"' | tr ',' '\n')
  pass_to_pass_count=0
  pass_to_pass_success=0
  base_pass_to_pass_failed_list=""

  for test in $PASS_TESTS; do
    if [[ -n "$test" && "$test" != "null" ]]; then
      # Remove "src:" prefix if present
      clean_test=$(echo "$test" | sed 's/^src://')
      ((pass_to_pass_count++))

      run_test_class "$clean_test" "PASS_TO_PASS"
      result=$?

      if [ $result -eq 0 ]; then
        ((pass_to_pass_success++))
      else
        if [[ -z "$base_pass_to_pass_failed_list" ]]; then
          base_pass_to_pass_failed_list="$clean_test"
        else
          base_pass_to_pass_failed_list="$base_pass_to_pass_failed_list, $clean_test"
        fi
      fi
    fi
  done

  echo "📊 PASS_TO_PASS summary: $pass_to_pass_success of $pass_to_pass_count tests passed"
  if [ ${pass_to_pass_count:-0} -gt 0 ] && [ ${pass_to_pass_success:-0} -lt ${pass_to_pass_count:-0} ]; then
    echo "❌  Failed: PASS_TO_PASS tests must all pass. Failed tests: $base_pass_to_pass_failed_list"
    exit 1
  fi
else
  echo "No PASS_TO_PASS tests to run"
fi

echo "🏁 Test execution completed for instance: $INSTANCE_ID"
EOF
  chmod +x "$TEST_SCRIPT"
}

# Function to create common functions file
create_common_functions_file() {
  COMMON_FUNCTIONS="common_functions.sh"
  # Remove any existing directory with this name before creating the file
  if [ -d "$COMMON_FUNCTIONS" ]; then
    rm -rf "$COMMON_FUNCTIONS"
  fi
  cat > "$COMMON_FUNCTIONS" << 'EOF'
#!/bin/bash
# Common helper functions for SWE benchmark scripts

# Install build tools if needed based on IS_MAVEN env variable
install_build_tools() {
  if [[ "$IS_MAVEN" == "true" ]]; then
    if ! command -v mvn &> /dev/null; then
      echo "🔧 Installing Maven..."
      apt-get update && apt-get install -y maven
    fi
  else
    if ! command -v gradle &> /dev/null; then
      echo "🔧 Installing Gradle..."
      wget -O gradle.zip https://services.gradle.org/distributions/gradle-9.0.0-bin.zip
      unzip -q gradle.zip
      mv gradle-9.0.0 /opt/gradle
      ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle
      rm gradle.zip
    fi
  fi
}
EOF
  chmod +x "$COMMON_FUNCTIONS"
}

# Function to build and run container for project setup
build_and_run_setup_container() {
  local docker_image_name="$1"
  local repo_url="$2"
  local commit="$3"
  local is_maven="$4"
  local instance_id="$5"

  # Check if prepared container already exists
  if docker image inspect "$docker_image_name-base" > /dev/null 2>&1; then
    echo "✅ Prepared container already exists: $docker_image_name-base"
    echo "🚀 Skipping container preparation..."
    return 0
  fi

  echo "🐳 Prepared container not found, creating new one..."

  # Build base Docker image
  echo "🐳 Building base Docker image: $docker_image_name-base..."
  docker build -t "$docker_image_name-base" .

  # Create setup script
  create_setup_script
  create_common_functions_file

  # Create prepared container with project and dependencies
  echo "🚀 Setting up project in container..."
  # Use unique setup container name per instance to avoid name conflicts
  SETUP_CONTAINER_NAME="${docker_image_name}-setup-$(echo "$instance_id" | tr '[:upper:]' '[:lower:]')"
  # Remove any stale container with the same name (from previous runs)
  docker rm -f "$SETUP_CONTAINER_NAME" 2>/dev/null || true

  docker run -d \
    -v "$(pwd)/$SETUP_SCRIPT:/workspace/setup_project.sh" \
    -v "$(pwd)/$COMMON_FUNCTIONS:/workspace/common_functions.sh" \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --privileged \
    --network bridge \
    -e TESTCONTAINERS_RYUK_DISABLED=true \
    -e TESTCONTAINERS_CHECKS_DISABLE=true \
    -e DOCKER_HOST=unix:///var/run/docker.sock \
    -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal \
    --name "$SETUP_CONTAINER_NAME" \
    "$docker_image_name-base" \
    bash -c "/workspace/setup_project.sh '$repo_url' '$commit' '$is_maven' && sleep infinity"

  # Wait for setup to complete and show logs
  echo "📋 Waiting for project setup to complete..."
  docker logs -f "$SETUP_CONTAINER_NAME" &
  LOGS_PID=$!

  # Wait for the setup script to finish (it will exit, leaving only sleep infinity)
  while docker exec "$SETUP_CONTAINER_NAME" pgrep -f "setup_project.sh" > /dev/null 2>&1; do
    sleep 2
  done

  # Kill the logs process
  kill $LOGS_PID 2>/dev/null || true

  # Check if container is still running and if setup was successful
  if ! docker ps -q -f "name=$SETUP_CONTAINER_NAME" | grep -q .; then
    docker rm -f "$SETUP_CONTAINER_NAME" 2>/dev/null || true
    echo "🗑️  Removing prepared container: $docker_image_name-base"
    docker rmi "$docker_image_name-base" 2>/dev/null || true
    return 1
  fi

  # Check container exit code to determine if setup was successful
  SETUP_EXIT_CODE=$(docker inspect "$SETUP_CONTAINER_NAME" --format='{{.State.ExitCode}}')
  if [ "$SETUP_EXIT_CODE" != "0" ] && [ "$SETUP_EXIT_CODE" != "null" ]; then
    docker rm -f "$SETUP_CONTAINER_NAME" 2>/dev/null || true
    echo "🗑️  Removing prepared container: $docker_image_name-base"
    docker rmi "$docker_image_name-base" 2>/dev/null || true
    echo "❌ Failed: Container preparation failed with exit code: $SETUP_EXIT_CODE"
    return 1
  fi

  # Commit the container with project and dependencies
  echo "💾 Creating prepared container image: $docker_image_name-base..."
  docker commit "$SETUP_CONTAINER_NAME" "$docker_image_name-base"
  docker rm -f "$SETUP_CONTAINER_NAME" 2>/dev/null || true

  return 0
}

# Function to run tests in container
run_tests_in_container() {
  local docker_image_name="$1"
  local patch="$2"
  local test_patch="$3"
  local instance_id="$4"
  local fail_to_pass="$5"
  local pass_to_pass="$6"
  local test_args="$7"
  local is_maven="$8"
  local commit="$9"
  local repo_url="${10}"

  # Create test script
  create_test_script "$patch" "$test_patch" "$instance_id" "$fail_to_pass" "$pass_to_pass" "$test_args" "$is_maven" "$commit" "$repo_url"
  create_common_functions_file

  # Run Docker container and execute tests
  echo "🚀 Running Docker container..."
  # Create temporary file to store full output
  TEMP_OUTPUT_FILE=$(mktemp)

  # Execute docker run and display output in real-time while also saving to a file
  set +e
  docker run --rm \
    -v "$(pwd)/$TEST_SCRIPT:/workspace/run_tests.sh" \
    -v "$(pwd)/$PARAMS_FILE:/workspace/test_params.env" \
    -v "$(pwd)/$COMMON_FUNCTIONS:/workspace/common_functions.sh" \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --privileged \
    --network bridge \
    -e TESTCONTAINERS_RYUK_DISABLED=true \
    -e TESTCONTAINERS_CHECKS_DISABLE=true \
    -e DOCKER_HOST=unix:///var/run/docker.sock \
    -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal \
    "$docker_image_name-base" \
    bash -c "/workspace/run_tests.sh" 2>&1 | tee "$TEMP_OUTPUT_FILE"
  RUN_EXIT_CODE=${PIPESTATUS[0]}

  # Get the last line of output for error reporting
  LAST_LINE=$(tail -n 1 "$TEMP_OUTPUT_FILE")
  rm -f "$TEMP_OUTPUT_FILE"

  # Cleanup
  rm -f "$TEST_SCRIPT" "$PARAMS_FILE"

  set -e

  return $RUN_EXIT_CODE
}

# Function to cleanup resources
cleanup_resources() {
  local docker_image_name="$1"
  local cleanup_containers="$2"

  echo "🧹 Cleaning up..."
  rm -f Dockerfile

  if [ -n "$SETUP_SCRIPT" ]; then
    rm -f "$SETUP_SCRIPT"
  fi

  if [ -n "$COMMON_FUNCTIONS" ]; then
    rm -f "$COMMON_FUNCTIONS"
  fi

  # Remove prepared image with project snapshot (always remove main prepared image tag)
  docker rmi "$docker_image_name" 2>/dev/null || true

  # Handle base image cleanup based on cleanup_containers flag
  if [ "$cleanup_containers" = true ]; then
    echo "🗑️  Removing prepared container: $docker_image_name-base"
    docker rmi "$docker_image_name-base" 2>/dev/null || true
  else
    echo "💾 Prepared container preserved: $docker_image_name-base"
    echo "   Use --cleanup flag to remove containers after execution"
    echo "   Use 'docker rmi $docker_image_name-base' to remove manually"
  fi
}

# Main execution flow
main() {
  local name_by_repo="$1"
  local cleanup_containers="$2"

  # Display basic information
  echo "📋 Instance: $INSTANCE_ID"
  echo "📦 Repository: $REPO_URL"
  echo "🏷️  Commit: $COMMIT"
  echo "🧹 Cleanup containers: $cleanup_containers"

  # Determine container name
  DOCKER_IMAGE_NAME=$(determine_container_name "$name_by_repo" "$INSTANCE_ID" "$REPO")
  if [ "$name_by_repo" = true ]; then
    echo "📋 Container name: $DOCKER_IMAGE_NAME (by repository)"
  else
    echo "📋 Container name: $DOCKER_IMAGE_NAME (by instance ID)"
  fi

  # Check Docker environment
  check_docker_environment

  # Create Dockerfile
  create_dockerfile "$JAVA_VERSION"

  # Build and run setup container
  build_and_run_setup_container "$DOCKER_IMAGE_NAME" "$REPO_URL" "$COMMIT" "$IS_MAVEN" "$INSTANCE_ID"
  if [ $? -ne 0 ]; then
    echo "❌ Failed: Setup container preparation failed"
    exit 1
  fi

  # Run tests in container
  run_tests_in_container "$DOCKER_IMAGE_NAME" "$PATCH" "$TEST_PATCH" "$INSTANCE_ID" "$FAIL_TO_PASS" "$PASS_TO_PASS" "$TEST_ARGS" "$IS_MAVEN" "$COMMIT" "$REPO_URL"
  RUN_EXIT_CODE=$?

  # Cleanup resources
  cleanup_resources "$DOCKER_IMAGE_NAME" "$cleanup_containers"

  # Final result message must contain execution result
  if [ $RUN_EXIT_CODE -eq 0 ]; then
    echo "✅"
    exit 0
  else
    # Strip leading cross mark from reason to match required final message format
    REASON_NO_ICON="${LAST_LINE#*❌  Failed: }"
    echo "❌ Failed: $REASON_NO_ICON"
    exit 0
  fi
}

# Execute script with the provided parameters
if [ $# -ge 10 ]; then
  # Default values for optional parameters
  NAME_BY_REPO=false
  CLEANUP_CONTAINERS=false

  # Parse additional optional parameters if provided
  if [ $# -ge 11 ]; then
    NAME_BY_REPO="${11}"
  fi

  if [ $# -ge 12 ]; then
    CLEANUP_CONTAINERS="${12}"
  fi

  main "$NAME_BY_REPO" "$CLEANUP_CONTAINERS"
else
  echo "❌ Failed: Usage: $0 REPO COMMIT PATCH TEST_PATCH FAIL_TO_PASS PASS_TO_PASS TEST_ARGS IS_MAVEN JAVA_VERSION INSTANCE_ID [NAME_BY_REPO] [CLEANUP_CONTAINERS]"
fi
