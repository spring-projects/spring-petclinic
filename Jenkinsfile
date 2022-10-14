node('JDK-11-MVN3.8.6'){
    properties([pipelineTriggers([upstream('init-project, ')])])
    environment{
        AWS_ACCOUNT_ID="172455797459"
        AWS_REGION="us-west-2"
        IMAGE_REPO_NAME="dockerimages"
        MAGE_TAG="1.0"
        REPOSITORY_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
    }
    stage('source code '){
        git branch: "scripted", url: "https://github.com/ShaikNasee/spring-petclinic.git"
    }
    stage('mvn build'){
        sh "/usr/local/apache-maven-3.8.6/bin/mvn clean package"

    }
    stage('archive Artifacts'){
        archiveArtifacts artifacts: "target/*.jar", followSymlinks: false
    }
    stage('pushing unit test reports'){
         junit "**/TEST-*.xml"
    }
    stage('Authenticationg Aws ECR '){
        sh "aws --version"
        sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
    }
    stage('running the java appcation on docker '){
              sh "scp deploy.sh ubuntu@35.90.160.204:~/"
              sh "ssh ubuntu@35.90.160.204 "chmod +x deploy.sh""
              sh "ssh ubuntu@35.90.160.204 ./deploy.sh"
    }
    
}