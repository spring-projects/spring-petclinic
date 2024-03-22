#!/bin/bash
#
REGION="ap-northeast-2"
ECR_REPOSITORY="257307634175.dkr.ecr.ap-northeast-2.amazonaws.com"
ECR_DOCKER_IMAGE="${ECR_REPOSITORY}/std01-spring-petclinic"
ECR_DOCKER_TAG="latest"

aws ecr get-login-password --region ${REGION} \
  | docker login --username AWS --password-stdin ${ECR_REPOSITORY};

export IMAGE=${ECR_DOCKER_IMAGE};
export TAG=${ECR_DOCKER_TAG};
cd /home/ubuntu/deploy
docker-compose up -d --build;
