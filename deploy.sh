echo "Starting to deploy docker image.."
DOCKER_IMAGE=dockerimages:1.0
docker pull $DOCKER_IMAGE
docker ps -q --filter ancestor=$DOCKER_IMAGE | xargs -r docker stop
docker run -d --name EXP3 -p 8080:8080 $DOCKER_IMAGE
