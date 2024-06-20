#!/usr/bin/env bash
# List of characters to replace
declare -A char_replacements=(
    [" "]=_
    ["/"]=_
    [":"]=_
    ["("]=_
    [")"]=_
    ["{"]=_
    ["}"]=_
    ["-"]=_
)

# Function to scrub a string
scrub_string() {
    local string=$1
    for char in "${!char_replacements[@]}"; do
        string=${string//"$char"/"${char_replacements[$char]}"}
    done
    echo "$string"
}

# Iterate through environment variables
for var in $(printenv | grep '^BITBUCKET_' | cut -d= -f1); do
    # Get the value of the variable
    value=$(printenv "$var")
    # Scrub the value
    scrubbed_value=$(scrub_string "$value")
    # Create a new variable with the scrubbed value
    export "${var}_SCRUBBED=$scrubbed_value"
    echo "${var}_SCRUBBED=$scrubbed_value"
done
