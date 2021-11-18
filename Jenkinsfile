pipeline {
    agent {label 'slave-debian'}

    environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub_id')
		DOCKER_TAG = getVersion()
	}

    stages {

        stage('SCM'){
            steps{
 //               git credentialsId: 'github', 
 //                   url: 'https://github.com/ayeliferov/spring.git'
				// Get some code from a GitHub repository
                git branch: 'ansible', url: 'https://github.com/ayeliferov/spring.git'
            }
        }

		stage('Maven Build'){
            steps{
                sh "mvn clean package"
            }
        }

		stage('Docker Build'){
            steps{
                sh "docker build . -t ayeliferov/epam_lab:${DOCKER_TAG} "
            }
        }

		stage('DockerHub Push'){
            steps{
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                }
				
            steps{    
                sh "docker push ayeliferov/epam_lab:${DOCKER_TAG} "
            }
        }

	}
}
	post {
		always {
			sh 'docker logout'
		}
}


def getVersion(){
    def commitHash = sh label: '', returnStdout: true, script: 'git rev-parse --short HEAD'
    return commitHash
}