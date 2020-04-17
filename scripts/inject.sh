#!/bin/bash
URL=localhost:8080/vets.html
if [ "$"1 == "" ];
then
  echo "Missing result filename"
  exit 1
fi
RESULTS=$1
echo "starttransfer,total" > ${RESULTS}
echo "Warmup..."
for i in {1..1000}
do
    curl -s -o /dev/null $URL
done
echo "Measure..."
date +%H:%M:%S
for i in {1..1000}
do
    curl -w "%{time_starttransfer},%{time_total}\n" -s -o /dev/null $URL >> ${RESULTS}
    sleep 0.01
done
date +%H:%M:%S
