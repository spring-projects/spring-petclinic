pipeline {
    agent any
    
    stages {
        stage("Build jartifact") {
            steps {
                echo "=============== Building starts =================="
                sh 'pwd'
                sh './mvnw package'
                echo "=============== Building is complete =================="
                sh 'mkdir docker'
                sh 'mv Dockerfile docker/'
                sh 'mv target/*.jar docker/main.jar'
                sh 'echo GIT_COMMIT: $GIT_COMMIT'
                sh 'echo BUILD_TAG: $BUILD_TAG'
            }            
        }
        stage("Build_image") {
            steps {
                dir ('docker') {
                    sh 'docker build -t petclinic.$BUILD_NUMBER .'
                    sh 'docker run -d -p 8080:8080 petclinic.$BUILD_NUMBER'
                }
                echo 'Keep going!'
                
                docker.withRegistry('https://178258651770.dkr.ecr.eu-central-1.amazonaws.com', 'ecr:eu-central-1:jenkins') {
                    docker.image('178258651770.dkr.ecr.eu-central-1.amazonaws.com/myrepo123').push('latest')
                }
            }
        }
    }

}
