pipeline {
	agent any  
        stages {
  	
          stage('Maven Install') {
            steps {
      	      sh 'mvn clean install'
      }
    }
    stage('Docker Build') {
      steps {
      	sh 'docker build -t registry.hub.docker.com/hybrid2k3/petclinic:1 .'
      }
    }
    stage('Docker Push') {
      steps {
      	withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
        	sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
          sh 'docker push registry.hub.docker.com/hybrid2k3/petclinic:1'
        }
      }
    }
  }
}
