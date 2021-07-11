pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-v $HOME/.m2:/.m2 -v $PWD/build:/build'
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
				}
			}
			steps {
				echo 'Done'
			}
		}

	}
}
