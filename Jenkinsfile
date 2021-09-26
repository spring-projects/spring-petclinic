pipeline {
    agent any
    
    stages {
        stage("Build jartifact") {
            steps {
                echo "=============== Building starts =================="
                sh 'pwd'
                sh './mvnw package'
                sh 'mkdir docker'
                echo "=============== Building is complete =================="
                sh 'mv target/*.jar docker/petclinic.jar'
                sh 'echo GIT_COMMIT: $GIT_COMMIT'
                sh 'echo BUILD_TAG: $BUILD_TAG'
            }            
        }
        stage("Build_image") {
            steps {
                dir ('docker') {
                    sh "docker build -t $BUILD_NUMBER ."
                }
                echo "Keep going!"   
            }

	    }
    }

}
