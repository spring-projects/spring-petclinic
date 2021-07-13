pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					additionalBuildArgs '-t petclinic-build'
					args '-v $HOME/.m2:/root/.m2 --name petclinic-build'
				}
			}
			steps {
				script {
					echo 'Done'
				}
			}
		}

		stage('Copy .jar file') {
			agent {
				docker {
					image 'docker:dind'
					args '-v /var/run/docker.sock:/var/run/docker.sock'
				}
			}
			steps {
				sh """docker run -d --rm \
					-v /var/run/docker.sock:/var/run/docker.sock \
					--name petclinic-build
					petclinic-build"""
				sh 'docker cp petclinic-build:/build/target/app.jar .'
			}
		}

		stage('Run') {
			agent {
				dockerfile {
					filename 'Dockerfile.run'
					dir '.'
				}
			}
			steps {
				echo 'Done'
			}
		}

	}
}
