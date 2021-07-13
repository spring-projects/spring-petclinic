pipeline {

	agent any

	stages {

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					additionalBuildArgs '-t petclinic-build'
					args '-v $HOME/.m2:/root/.m2'
				}
			}
			steps {
				sh 'docker run -v $HOME/.m2:/root/.m2 --name petclinic-build petclinic-build'
				sh 'docker cp petclinic-build:/build/spring-petclinic/target/app.jar .'
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
				sh 'docker-compose up'
				/* sh 'docker run -it --rm -p 8080:8080 petclinic-run' */
			}
		}

	}
}
