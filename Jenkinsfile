pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-v $HOME/.m2:/root/.m2'
				}
			}
			steps {
				echo 'Build done'
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
