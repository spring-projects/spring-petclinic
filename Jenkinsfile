pipeline {
    agent {label 'slave-debian'}

    tools {
      maven 'M3'
      dockerTool 'Docker'
    }

    environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub_id')
        DOCKERHUB_TOKEN=credentials('Docker_token')
		DOCKER_TAG = getVersion()
	}

    stages {

        stage('CHECKOUT'){
            steps{
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
                sh "docker build . -t ayeliferov/epam_lab:Clinic_${env.BUILD_NUMBER} "
				sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
				sh "docker push ayeliferov/epam_lab:Clinic_${env.BUILD_NUMBER} "
				sh 'docker logout'
            }
        }

		stage('Docker Deploy'){
    //		steps {
    //          ansiblePlaybook(
    //               credentialsId: 'dev-server',
    //                vaultCredentialsId: 'AnsibleVault',
    //                disableHostKeyChecking: true,
    //                extras: '-e DOCKER_TAG=${DOCKER_TAG}',
    //                installation: 'ansible',
    //                inventory: 'dev.inv',
    //                playbook: 'ansible-playbook.yml'
    //            )
	//		}
            
            steps {
                node ('master'){
                    git branch: 'dev', url: 'https://github.com/ayeliferov/spring.git'

                    dir('terraform'){
                        sh 'terraform init'
                        withCredentials([usernamePassword(credentialsId: 'aws_usr_pass', passwordVariable: 'aws_secret_key', usernameVariable: 'aws_access_key')]) {
                        sh "terraform plan -var='app_docker_tag=ayeliferov/epam_lab:Clinic_${env.BUILD_NUMBER}' -var='aws_access_key=$aws_access_key' -var='aws_secret_key=$aws_secret_key'"
                        sh "terraform apply -var='app_docker_tag=ayeliferov/epam_lab:Clinic_${env.BUILD_NUMBER}' -var='aws_access_key=$aws_access_key' -var='aws_secret_key=$aws_secret_key' --auto-approve"
                        }
                    }
                }
            }
		}
	}
}



def getVersion(){
    def commitHash = sh label: '', returnStdout: true, script: 'git rev-parse --short HEAD'
    return commitHash
}