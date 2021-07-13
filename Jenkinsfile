pipeline {

	agent any

	stages {

		stage('Build') {
			steps {
					sh 'docker build -f Dockerfile.build -t petclinic-build .'
					sh 'docker run -v $HOME/.m2:/root/.m2 --name petclinic-build petclinic-build'
					sh 'docker cp petclinic-build:/build/spring-petclinic/target/app.jar .'
			}
		}

		stage('Run') {
			steps {
					sh 'docker build -f Dockerfile.run -t petclinic-run .'
					sh 'docker-compose up'
			}
		}

	}
}
