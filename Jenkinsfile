pipeline {

	agent any

	environment {
		BUILD_CONTAINER_ID = ''
	}

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					additionalBuildArgs '-t petclinic-build'
					args '$HOME/.m2:/root/.m2'
				}
			}
			steps {
				BUILD_CONTAINER_ID = "sh 'docker run -v $HOME/.m2:/root/.m2 -d petclinic-build'"
			}
		}

		stage('Run') {
			agent {
				dockerfile {
					filename 'Dockerfile.run'
					additionalBuildArgs '-t petclinic-run'
				}
			}
			steps {
				sh 'docker cp ${BUILD_CONTAINER_ID}:/build/target/app.jar .'
				sh 'docker-compose up'
				/* sh 'docker run -it --rm -p 8080:8080 petclinic-run' */
			}
		}

	}
}
