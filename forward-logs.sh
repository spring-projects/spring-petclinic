#!/bin/bash

LOGFILE="/app/logs/app.log"
INGEST_URL="$OPENPIPELINE_URL"
TOKEN="$OPENPIPELINE_TOKEN"

echo "Starting log forwarder..."
echo "Sending logs from $LOGFILE to $INGEST_URL"

mkdir -p /app/logs
touch "$LOGFILE"

tail -F "$LOGFILE" | while read line; do
    TIMESTAMP=$(date -Iseconds)

    JSON=$(printf '{"timestamp":"%s","message":%s,"source":"petclinic","location":"tallinn"}' \
           "$TIMESTAMP" "$(printf '%s' "$line" | jq -Rs)")

    curl -s -X POST "$INGEST_URL" \
        -H "Authorization: Api-Token $TOKEN" \
        -H "Content-Type: application/json" \
        -d "$JSON" > /dev/null
done
