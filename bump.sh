#!/bin/bash
existing_version=`xpath pom.xml '/project/version' 2>/dev/null|sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//'`
bumped_version=`xpath pom.xml '/project/version' 2>/dev/null|sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//'|awk -F "." '{$2+=1;OFS=".";print$0}'`

echo "Version $existing_version was bumped to $bumped_version"
