#!/usr/bin/env bash

# start app
nohup java -javaagent:/app/newrelic/newrelic.jar -jar spring-petclinic-2.7.3.jar > /app/logs/app.log 2>&1 &

# generate load
./tester.sh
