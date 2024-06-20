#!/usr/bin/env bash
echo "$(date +%Y%m%d_%H%M%S) - Entering $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"



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

scrub_string() {
  local string=$1
  # Replace characters
  for char in "${!char_replacements[@]}"; do
    string=${string//"$char"/"${char_replacements[$char]}"}
  done
  # Replace multiple underscores with a single underscore
  string=$(echo "$string" | sed 's/[_]\{2,\}/_/g')
  # Remove leading and trailing underscores
  string=$(echo "$string" | sed 's/^_//;s/_$//')
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

echo "$(date +%Y%m%d_%H%M%S) - Leaving $(basename "$(realpath "${BASH_SOURCE[0]}")") on host $(hostname)"
