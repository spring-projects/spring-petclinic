pipeline {

	agent any

	stages {

		stage('Build') {
			steps {
				script {
					docker build -f Dockerfile.build -t petclinic-build .
					docker run -v $HOME/.m2:/root/.m2 --name petclinic-build petclinic-build
					docker cp petclinic-build:/build/spring-petclinic/target/app.jar .
				}
			}
		}

		stage('Run') {
			steps {
				script {
					docker build -f Dockerfile.run -t petclinic-run .
					docker-compose up
				}
			}
		}

	}
}
