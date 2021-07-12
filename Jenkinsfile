pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-v $HOME/.m2:/root/.m2 --name petclinic_build'
				}
			}
			steps {
				sh 'docker cp petclinic_build:/build/target/app.jar .'
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
