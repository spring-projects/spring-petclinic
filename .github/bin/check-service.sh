#!/bin/sh -x
while true
do
  sleep 1
  docker service ls
  docker service ls | grep '\([0-9]\)/\1' && break
done
