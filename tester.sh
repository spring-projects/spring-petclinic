#!/bin/bash

logfile="logs/tester.log"

sleep 5

echo "Running automated tests..." > $logfile

while true; do
    curl -s -o /dev/null http://localhost:8080/clm/auto-only
    curl -s -o /dev/null http://localhost:8080/clm/annotation
    curl -s -o /dev/null http://localhost:8080/clm/api
    curl -s -o /dev/null http://localhost:8080/clm/xml
    curl -s -o /dev/null http://localhost:8080/clm/static
    curl -s -o /dev/null http://localhost:8080/clm/http
    curl -s -o /dev/null http://localhost:8080/clm/db

    echo "Completed a full set of operations." >> $logfile

    # go too fast and the agent starts sampling
    sleep 4
done
