node {
    def WORKSPACE = "/var/lib/jenkins/workspace/petclinic"
    def dockerImageTag = "petclinic${env.BUILD_NUMBER}"

try{
     stage('Clone Repo') {
        // for display purposes
        // Get some code from a GitHub repository
        git url:"https://github.com/A-hash-bit/spring-petclinic.git", branch:"main"
     }
      stage('Build docker') {
             dockerImage = docker.build("petclinic:${env.BUILD_NUMBER}")
      }

      stage('Deploy docker'){
              echo "Docker Image Tag Name: ${dockerImageTag}"
              sh "docker stop petclinic || true && docker rm petclinic || true"
              sh "docker run --name petclinic -d -p 8081:8081 petclinic:${env.BUILD_NUMBER}"
      }
}catch(e){
    currentBuild.result = "FAILED"
    throw e
}finally{
    notifyBuild(currentBuild.result)
 }
}

