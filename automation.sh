echo "####################################################"
echo "Stop and remove all running containers..."
echo "docker stop $(docker ps -a -q)"
echo "docker system prune -af"

docker stop $(docker ps -a -q)

# remove the stopped docker containers
docker rm -v -f $(docker ps -qa)

# uncomment this to remove the images too
# docker system prune -af

echo "####################################################"
echo "Build and run container..."
echo "build . -t spring_image"
echo "docker run -itd --p:8080:8080 --name spring-clinic spring_image"
docker build . -t spring_image
docker run -itd -p:8080:8080 --name spring-clinic spring_image

