pipeline {
    agent {label 'slave-debian'}

    environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub_id')
	}

    stages {
        stage('Build') {
            steps {
				sh 'docker build -t ayeliferov/epam_lab:latest .'
			}
		}
        stage('Login') {

			steps {
				sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
			}
		}

        stage('Push') {

			steps {
				sh 'docker push ayeliferov/epam_lab:latest'
			}
		}
	}

	post {
		always {
			sh 'docker logout'
		}
	}

}