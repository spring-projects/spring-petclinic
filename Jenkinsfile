pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					label 'petclinic-build'
					args '-v $HOME/.m2:/root/.m2 -v ./app:/root/app'
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
					label 'petclinic-run'
					args '-v $HOME/.m2:/root/.m2 -v ./app:/root/app'
				}
			}
			steps {
				echo 'App Running'
			}
		}

	}
}
