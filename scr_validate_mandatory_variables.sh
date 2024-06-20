#!/usr/bin/env bash
echo "$(date +%Y%m%d_%H%M%S) - Entering $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

# Function to check if a variable is defined
check_variable_defined() {
    local var_name=$1
    if [ -z "${!var_name}" ]; then
        echo "$var_name"
    fi
}

# List of necessary variables to check
necessary_variables=(
    "DOCKER_IMAGE_NAME"
    "HARBOR_LOGIN_ADDRESS"
    "BITBUCKET_REPO_FULL_NAME_SCRUBBED"
    "DOCKER_TAG_PIPELINE_UUID"
    "BITBUCKET_PIPELINE_UUID_SCRUBBED"
    "DOCKER_TAG_COMMIT_SHA"
    "BITBUCKET_COMMIT_SCRUBBED"
    "DOCKER_TAG_BRANCH_NAME"
    "BITBUCKET_BRANCH_SCRUBBED"
    "DOCKER_TAG_BRANCH_NAME"
    "BITBUCKET_PIPELINE_UUID_SCRUBBED"
    "HARBOR_ACCESS_TOKEN"
    "HARBOR_USERNAME"

)

# Array to store undefined variables
undefined_variables=()

# Iterate through the list of necessary variables and check if they are defined
for var in "${necessary_variables[@]}"; do
    result=$(check_variable_defined "$var")
    if [ -n "$result" ]; then
        undefined_variables+=("$result")
    fi
done

# Print out which variables are not defined
if [ ${#undefined_variables[@]} -ne 0 ]; then
    echo "The following variables are not defined:"
    for var in "${undefined_variables[@]}"; do
        echo "$var"
    done
else
    echo "MSG:  All necessary variables are defined, should be good to go."
fi


echo "$(date +%Y%m%d_%H%M%S) - Leaving $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"

