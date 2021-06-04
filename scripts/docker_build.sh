#! /usr/bin/env bash

export DEPLOY_STAGE="development"

export CONTAINER_NAME="kubevisor_spring-petclinic"
export IMAGE_NAME="spring-petclinic:latest"

export CONTAINER_REGISTRY="registry.dev.bitco.co.za:4321/bitco/aarborpie"

# docker kill "$CONTAINER_NAME"

# docker rm "$CONTAINER_NAME"

docker_build_cmd="docker image build -t \"$IMAGE_NAME\" ."

docker_run_cmd="docker run -d --name \"$CONTAINER_NAME\""
docker_run_cmd+=" --env DEPLOY_STAGE=\"$DEPLOY_STAGE\"" \
docker_run_cmd+=" --restart always" \
docker_run_cmd+=" --log-opt max-size=10m" \
docker_run_cmd+=" --log-opt max-file=3" \
docker_run_cmd+=" \"$IMAGE_NAME\""

echo "build: $docker_build_cmd"
eval $docker_build_cmd

echo "run: $docker_run_cmd"
eval $docker_run_cmd
