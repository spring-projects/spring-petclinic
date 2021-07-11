pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-d -v $HOME/.m2:/root/.m2 -v $HOME/app:/root/app'
				}
			}
			steps {
				echo "App Built"
			}
		}

		stage('Run') {
			agent {
				dockerfile {
					filename 'Dockerfile.run'
					dir '.'
					additionalBuildArgs '-t hllvc/spring-petclinic:latest'
				}
			}
			steps {
				sh 'docker-compose up'
			}
		}

	}
}
