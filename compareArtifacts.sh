#!/bin/sh
MAVEN=target/*.jar
GRADLE=build/libs/*.jar

ls -l --block-size=M $MAVEN  | awk '{print $9, $5}'
ls -l --block-size=M $GRADLE  | awk '{print $9, $5}' 