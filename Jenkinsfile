pipeline {
    agent {label 'slave-debian'}

    tools {
      maven 'M3'
      dockerTool 'Docker'
    }

    environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub_id')
		DOCKER_TAG = getVersion()
	}

    stages {

        stage('CHECKOUT'){
            steps{
 				// Get some code from a GitHub repository
                git branch: 'dev', url: 'https://github.com/ayeliferov/spring.git'
            }
        }

		stage('Build'){
            steps{
                sh "mvn clean package"
            }
        }

		stage('CREATE ARTIFACT'){
            steps{
                sh "docker build . -t ayeliferov/epam_lab:FinalProject_${DOCKER_TAG} "
				sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
				sh "docker push ayeliferov/epam_lab:FinalProject_${DOCKER_TAG} "
//				sh 'docker logout'
            }
        }

		stage('Docker Deploy'){
			steps {
                ansiblePlaybook(
                    credentialsId: 'dev-server',
//                    vaultCredentialsId: 'AnsibleVault',
                    disableHostKeyChecking: true,
                    extras: "-e DOCKER_TAG=${DOCKER_TAG} DOCKERHUB_CREDENTIALS_PSW=${DOCKERHUB_CREDENTIALS_PSW}",
                    installation: 'ansible',
                    inventory: 'dev.inv',
                    playbook: 'ansible-playbook.yml'
                )
			}
		}
	}
}



def getVersion(){
    def commitHash = sh label: '', returnStdout: true, script: 'git rev-parse --short HEAD'
    return commitHash
}