# use this by running sh automation.sh in Terminal


echo "####################################################"
echo "Stop and remove all running containers..."
echo "docker stop $(docker ps -a -q)"
docker stop $(docker ps -a -q)

# remove the stopped docker containers
echo "docker system prune -af"
docker rm -v -f $(docker ps -qa)

# uncomment this to remove the images too
#docker system prune -af

echo "####################################################"
echo "Create a network..."
echo "docker network create --driver bridge petclinic-net"
docker network create --driver bridge petclinic-net

echo "####################################################"
echo "Build and run container..."
echo "build . -t spring_image"
echo "docker run -itd -p:8080:8080 --name spring-petclinic spring_image"
docker build . -t spring_image
docker run -itd -p 8080:8080 --network petclinic-net --name spring-petclinic spring_image



echo "####################################################"
echo "Running OWASP ZAP"

# Pull the latest stable OWASP ZAP image
docker pull ghcr.io/zaproxy/zaproxy:stable

echo "Running OWASP ZAP scan..."
docker run --rm -t --network petclinic-net \
  -v $(pwd)/zap-report:/zap/wrk:rw \
  ghcr.io/zaproxy/zaproxy:stable zap-baseline.py \
  -t http://localhost:8080 -g gen.conf -r zap-report.html


echo "Scan complete. Report saved as zap-report.html"
