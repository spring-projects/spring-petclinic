echo "####################################################"
echo "Stop and remove all running containers..."
echo "docker stop $(docker ps -a -q)"
echo "docker system prune -af"
docker stop $(docker ps -a -q)
docker system prune -af

echo "####################################################"
echo "Build and run container..."
echo "build . -t spring_image"
echo "docker run -itd --net=assignment2 -p:8080:8080 --name spring-clinic spring_image"
docker build . -t spring_image
docker run -itd --net=assignment2 -p:8080:8080 --name spring-clinic spring_image

