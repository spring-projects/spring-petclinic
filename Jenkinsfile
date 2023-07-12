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
      	sh 'docker build -t hybrid2k3/petclinic:2 .'
      }
    }
    stage('Docker Push') {
      steps {
      	withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
        	sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
          sh 'docker push hybrid2k3/petclinic:2'
        }
      }
    }
  }
}
