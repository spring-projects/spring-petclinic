pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               bat  './mvnw compile'
            }
        }
       
    }
    post {
        success {
             mail to: 'mnezam.31@gmail.com',
             subject: "Successfull Build: ${currentBuild.fullDisplayName}",
             body: "The following was successfully built ${env.BUILD_URL}"
    	}
	failure {
             mail to: 'mnezam.31@gmail.com',
             subject: "Failed Build: ${currentBuild.fullDisplayName}",
             body: "Something went wrong with the following ${env.BUILD_URL}"
    	}
    }
}
