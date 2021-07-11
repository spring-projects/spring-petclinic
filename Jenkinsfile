pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-d -v $HOME/.m2:/root/.m2 -v ${WORKSPACE}:/build'
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
					dir '/build'
				}
			}
			steps {
				echo 'Done'
			}
		}

	}
}
