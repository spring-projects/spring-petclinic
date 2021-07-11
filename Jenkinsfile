pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-v $HOME/.m2:/.m2 -v /tmp/petclinic_build:/build'
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
					args '-v /tmp/petclinic_build:/app'
				}
			}
			steps {
				echo 'Done'
			}
		}

	}
}
