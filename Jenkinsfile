pipeline {

	agent any

	stages {

		stage('Clone Repo') {
			checkout scm
		}

		stage('Build') {
			agent {
				dockerfile {
					filename 'Dockerfile.build'
					dir '.'
					args '-v $HOME/.m2:/root/.m2 -v $HOME/app:/root/app'
				}
			}
			steps {
				echo "App Built"
			}
		}

		stage('MySQL setup') {
			steps {
				sh 'docker network create petclinic || true'
				sh 'docker run -d --network petclinic -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7.8'
			}
		}

		stage('Run') {
			def app = docker.build('petclinic-app:${env.BUILD_ID}', '-f Dockerfile.run')
			app.push()
			app.push('latest')
			/* agent { */
			/* 	dockerfile { */
			/* 		filename 'Dockerfile.run' */
			/* 		args '-v $HOME/.m2:/root/.m2 -v $HOME/app:/root/app --network petclinic -t petclinic-app' */
			/* 	} */
			/* } */
			steps {
				sh 'docker run --network petclinic -p8080:3000 -v $HOME/app:/root/app petclinic-app:latest'
			}
		}

	}
}
