#!/usr/bin/env bash

APP_PID=
TESTER_PID=

cleanup() {
  echo "Killing app pid $APP_PID"
  kill "$APP_PID"
  wait "$APP_PID"
  echo "Killing tester pid $TESTER_PID"
  kill "$TESTER_PID"
}

trap cleanup INT TERM

# start app
nohup java -javaagent:/app/newrelic/newrelic.jar -jar spring-petclinic-2.7.3.jar > /app/logs/app.log 2>&1 &
APP_PID=$!

# generate load
./tester.sh &
TESTER_PID=$!

echo "Running with APP_PID $APP_PID, TESTER_PID, $TESTER_PID"

wait $TESTER_PID
