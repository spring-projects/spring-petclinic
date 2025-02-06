# Steps

### DEVELOP BRANCH PULL BASED
-----------------------------
* Java-based Spring Petclinic application
* Pull request-based development
* Create a multi-stage Dockerfile for the Spring Petclinic application on the `develop`    branch
* Execute Dockerfile Using Command
```bash
# Create image command
docker image build -t spc:1.0 .
docker image build -t <your image:tag> (dockerfile name)
# Check if the image was created
docker image ls
# Check if your image is running in a container (for verification purposes)
docker container run -P -d --name mytest spc:1.0
docker container run -P -d --name <container name> <your image:tag>
# Check if the container is running (to verify container status)
docker container ls
```
* Image Scanning with Trivy
* Now, to scan your image with Trivy, follow these steps:
* First, install Trivy on your VM or EC2 instance.
```bash
sudo apt-get install wget apt-transport-https gnupg lsb-release
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | gpg --dearmor | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null
echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main" | sudo tee -a /etc/apt/sources.list.d/trivy.list
sudo apt-get update
sudo apt-get install trivy -y
```
### After Trivy is installed, scan your image using the following command:
```bash
trivy image spc:1.0
trivy image <image:tag>
```
#############################################################################################################################################################################

### RELEASE BRANCH PUSH BASED
-----------------------------
* Push Based `Release`
* Multiple developers have made changes, so we will scan the Docker image again.
* On the release branch, scan the Docker image with Trivy.
### After Trivy is installed, use the following command:
```bash
trivy image spc:1.0
trivy image <image:tag>
```
* The output will show vulnerabilities or reports.
* Pushing the Image to a Registry
* Once the image scan is completed, we can push the image to a registry such as Docker Hub, ECR, or ACR.
### To push to Docker Hub, follow these steps:
```bash
docker login
docker image tag <docke image> <repousename>/<reponame>:<tag>
docker image tag spc:1.0 longflew/javaimagecicd:1.0
docker image push longflew/javaimagecicd:1.0
```
##### Note: Ensure k8s Cluster(AKS or EKS) will be created 
### Installing helm 
* [Refer Here](https://helm.sh/docs/intro/install/) for installing Helm 
* Once installation completed run the following commands
* To Create a new helm chart repo
`helm create <your chat>` 
`helm create spc-chart`
* Deploy the application in k8s using helm , the command will be
 `helm install <release name> <chart name>`
 `helm install spc-release spc-chart`
* Check the appliocation running or not using the k8s commands 
```bash
kubectl get po
kubectl get svc
```
* Take the IP or DNS of the service and open new Browser and paste the IP or DNS.

### Upgrade docker image in helm
* Whenever we upgrade the Docker image in Helm, the deployment manifest file is also automatically updated with the new image
* After making further changes, we updated the values.yaml file to modify the image tag.
* use the following command:
```bash
# docker image upgrade 
helm upgrade spc-release spc-chart -f values.yaml --set image.tag=new-image-tag

# Verify the Upgrade
helm get all <release name>
helm get all spc-release
```


