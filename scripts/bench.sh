#!/bin/bash
INJECT_COUNT=5
OUTPUT_FILE=out_petclinic.txt

function bench () {
  if [ "$1" == "" ];
  then
    echo "Missing tag"
    exit 1
  fi
  if [ "$2" == "" ];
  then
    echo "Missing jdk"
    exit 1
  fi
  TAG=$1
  JDK=$2
  # checking prequisites
  checks $TAG
  for I in $(seq $INJECT_COUNT);
  do
    if [ -f $OUTPUT_FILE ];
    then
      rm $OUTPUT_FILE
    fi
    echo "$(date +%H:%M:%S) Starting application ${TAG}-${JDK} run $I/$INJECT_COUNT..."
    ./start.sh $TAG $JDK &
    PID=$!
    DEAD=0
    sleep 0.2
    while [ "$(grep -o "Started PetClinicApplication" $OUTPUT_FILE)" != "Started PetClinicApplication" -a "$DEAD" != "1" ];
    do
      kill -0 $PID
      DEAD=$?
      sleep 1
    done
    if [ "$DEAD" == "1" ];
    then
      echo "Application not started correctly!"
      exit 1
    fi
    echo "$(date +%H:%M:%S) Sending requests..."
    ./inject.sh results_${TAG}-${JDK}-${I}.csv
    echo "Killing $PID"
    pkill -P $PID
    sleep 1
  done
  python percentiles.py ${TAG}-${JDK}.csv results_${TAG}-${JDK}-*.csv
}

function checks () {
  TAG=$1
  if [ "$TAG" == "ap" ];
  then
    if [ ! -f ../../ap/build/libasyncProfiler.so ];
    then
      echo "Async profiler library is missing."
      exit 1
    fi
    echo 1 | sudo tee /proc/sys/kernel/perf_event_paranoid
    echo 0 | sudo tee /proc/sys/kernel/kptr_restrict
  fi
  if [ "$TAG" == "dd" ];
  then
    if [ ! -f ../../dd-java-agent.jar ];
    then
      echo "DD java agent is missing"
      exit 1
    fi
    if [ ! -f ../../profiling-api-key ];
    then
      echo "DD api-key file is missing"
      exit 1
    fi
  fi
}

bench none zulu8
bench ap zulu8
bench jfr zulu8
bench dd zulu8

