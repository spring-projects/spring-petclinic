#!/bin/bash
#
# Converts Maven Surefire XML test reports to a single JSON file.
# Reads all *.xml files from target/surefire-reports/ and converts them to JSON.
# Usage: ./jfrog/convertXml2Json.sh test-results.json
#

set -euo pipefail

# Find project root directory
# SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(pwd)"
REPORTS_DIR="$PROJECT_ROOT/target/surefire-reports"
OUTPUT_FILE="${1:-$PROJECT_ROOT/test-results.json}"

# Check if reports directory exists
if [ ! -d "$REPORTS_DIR" ]; then
    echo "Error: Reports directory does not exist: $REPORTS_DIR" >&2
    exit 1
fi

# Check if xq is available (XML to JSON converter, part of yq package)
if ! command -v xq &> /dev/null; then
    echo "Error: xq command not found. Please install yq (which includes xq)." >&2
    echo "  macOS: brew install yq" >&2
    echo "  Linux: apt-get install yq or yum install yq" >&2
    exit 1
fi

# Check if jq is available (for JSON manipulation)
if ! command -v jq &> /dev/null; then
    echo "Error: jq command not found. Please install jq." >&2
    echo "  macOS: brew install jq" >&2
    echo "  Linux: apt-get install jq or yum install jq" >&2
    exit 1
fi

# Find all XML files in the reports directory
XML_FILES=("$REPORTS_DIR"/*.xml)

if [ ! -e "${XML_FILES[0]}" ]; then
    echo "Error: No XML files found in $REPORTS_DIR" >&2
    exit 1
fi

echo "Found ${#XML_FILES[@]} XML test report file(s)..."

# Initialize arrays and counters
SUITES_JSON="[]"
TOTAL_TESTS=0
TOTAL_FAILURES=0
TOTAL_ERRORS=0
TOTAL_SKIPPED=0
TOTAL_TIME=0

# Process each XML file
echo "Deleting existing output file: $OUTPUT_FILE"
rm -rf "$OUTPUT_FILE"
echo "Output file deleted: $OUTPUT_FILE"

echo "Processing XML files..."
for xml_file in "${XML_FILES[@]}"; do
    if [ ! -f "$xml_file" ]; then
        continue
    fi
    
    echo "Processing: $(basename "$xml_file")"
    
    # Convert XML to JSON using xq
    # xq converts XML to JSON, and we use jq to extract and format the data
    suite_json=$(xq -c '.' "$xml_file" 2>/dev/null)
    
    # Validate that we got valid JSON
    if [ -z "$suite_json" ] || ! echo "$suite_json" | jq empty 2>/dev/null; then
        echo "Warning: Failed to parse $xml_file or invalid JSON output, skipping..." >&2
        continue
    fi
    
    # Extract suite information and test cases using a single jq call to get all values
    suite_data=$(echo "$suite_json" | jq -c '{
        name: (.testsuite."@name" // .testsuite.name // "Unknown"),
        tests: ((.testsuite."@tests" // .testsuite.tests // 0) | if type == "string" and . == "" then 0 else (tonumber // 0) end),
        failures: ((.testsuite."@failures" // .testsuite.failures // 0) | if type == "string" and . == "" then 0 else (tonumber // 0) end),
        errors: ((.testsuite."@errors" // .testsuite.errors // 0) | if type == "string" and . == "" then 0 else (tonumber // 0) end),
        skipped: ((.testsuite."@skipped" // .testsuite.skipped // 0) | if type == "string" and . == "" then 0 else (tonumber // 0) end),
        time: ((.testsuite."@time" // .testsuite.time // 0) | if type == "string" and . == "" then 0 else (tonumber // 0) end),
        testCases: (
            .testsuite.testcase as $testcases |
            if $testcases == null then []
            elif $testcases | type == "array" then
                $testcases | map({
                    name: (."@name" // .name // ""),
                    classname: (."@classname" // .classname // ""),
                    time: ((."@time" // .time // 0) | tonumber),
                    failure: (if .failure then {
                        message: (.failure."@message" // .failure.message // ""),
                        type: (.failure."@type" // .failure.type // ""),
                        content: (.failure."#text" // .failure.content // .failure // "")
                    } else null end),
                    error: (if .error then {
                        message: (.error."@message" // .error.message // ""),
                        type: (.error."@type" // .error.type // ""),
                        content: (.error."#text" // .error.content // .error // "")
                    } else null end),
                    skipped: (if .skipped then (."@message" // .skipped."@message" // .skipped."#text" // .skipped // "") else null end)
                })
            else
                [{
                    name: ($testcases."@name" // $testcases.name // ""),
                    classname: ($testcases."@classname" // $testcases.classname // ""),
                    time: (($testcases."@time" // $testcases.time // 0) | tonumber),
                    failure: (if $testcases.failure then {
                        message: ($testcases.failure."@message" // $testcases.failure.message // ""),
                        type: ($testcases.failure."@type" // $testcases.failure.type // ""),
                        content: ($testcases.failure."#text" // $testcases.failure.content // $testcases.failure // "")
                    } else null end),
                    error: (if $testcases.error then {
                        message: ($testcases.error."@message" // $testcases.error.message // ""),
                        type: ($testcases.error."@type" // $testcases.error.type // ""),
                        content: ($testcases.error."#text" // $testcases.error.content // $testcases.error // "")
                    } else null end),
                    skipped: (if $testcases.skipped then ($testcases."@message" // $testcases.skipped."@message" // $testcases.skipped."#text" // $testcases.skipped // "") else null end)
                }]
            end
        )
    }')
    
    # Extract individual values from suite_data
    suite_name=$(echo "$suite_data" | jq -r '.name')
    tests=$(echo "$suite_data" | jq -r '.tests')
    failures=$(echo "$suite_data" | jq -r '.failures')
    errors=$(echo "$suite_data" | jq -r '.errors')
    skipped=$(echo "$suite_data" | jq -r '.skipped')
    time=$(echo "$suite_data" | jq -r '.time')
    test_cases=$(echo "$suite_data" | jq -c '.testCases')
    
    # Create suite object using the extracted values
    suite_object=$(jq -n \
        --arg name "$suite_name" \
        --argjson tests "$tests" \
        --argjson failures "$failures" \
        --argjson errors "$errors" \
        --argjson skipped "$skipped" \
        --argjson time "$time" \
        --argjson testCases "$test_cases" \
        '{
            name: $name,
            tests: $tests,
            failures: $failures,
            errors: $errors,
            skipped: $skipped,
            time: $time,
            testCases: $testCases
        }')
    
    # Add suite to suites array
    SUITES_JSON=$(echo "$SUITES_JSON" | jq --argjson suite "$suite_object" '. + [$suite]')
    
    # Update totals (extract integer values for arithmetic)
    tests_int=$(echo "$tests" | awk '{print int($1)}')
    failures_int=$(echo "$failures" | awk '{print int($1)}')
    errors_int=$(echo "$errors" | awk '{print int($1)}')
    skipped_int=$(echo "$skipped" | awk '{print int($1)}')
    
    TOTAL_TESTS=$((TOTAL_TESTS + tests_int))
    TOTAL_FAILURES=$((TOTAL_FAILURES + failures_int))
    TOTAL_ERRORS=$((TOTAL_ERRORS + errors_int))
    TOTAL_SKIPPED=$((TOTAL_SKIPPED + skipped_int))
    # Use awk for floating point arithmetic (more portable than bc)
    TOTAL_TIME=$(awk "BEGIN {printf \"%.3f\", $TOTAL_TIME + $time}")
done

# Create final JSON output
FINAL_JSON=$(jq -n \
    --argjson totalTests "$TOTAL_TESTS" \
    --argjson totalFailures "$TOTAL_FAILURES" \
    --argjson totalErrors "$TOTAL_ERRORS" \
    --argjson totalSkipped "$TOTAL_SKIPPED" \
    --arg totalTime "$TOTAL_TIME" \
    --argjson suites "$SUITES_JSON" \
    '{
        summary: {
            totalTests: $totalTests,
            totalFailures: $totalFailures,
            totalErrors: $totalErrors,
            totalSkipped: $totalSkipped,
            totalTime: ($totalTime | tonumber)
        },
        suites: $suites
    }')

# Ensure output directory exists
mkdir -p "$(dirname "$OUTPUT_FILE")"

# Write JSON file
echo "$FINAL_JSON" | jq '.' > "$OUTPUT_FILE"

echo ""
echo "Test results converted to JSON: $OUTPUT_FILE"
echo "Summary: $TOTAL_TESTS tests, $TOTAL_FAILURES failures, $TOTAL_ERRORS errors, $TOTAL_SKIPPED skipped"
echo "Processed $(echo "$SUITES_JSON" | jq 'length') test suite(s)"

