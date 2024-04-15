#!/bin/bash
docker volume inspect maven-repo >/dev/null 2>&1 || docker volume create maven-repo
docker volume inspect gradle-home >/dev/null 2>&1 || docker volume create gradle-home
