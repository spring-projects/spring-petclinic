pipeline {
    agent { label 'kmaster' }
    stages {
	    stage('SCM') {
            steps {
                // Build the application
                sh 'cd /apps/build'
                git 'https://github.com/ametgud4u/spring-petclinic.git'
            }
        }
        stage('Build') {
            steps {
                // Build the application
                sh '''
				export JAVA_HOME=/opt/jdk-17
                export PATH=$PATH:$JAVA_HOME/bin
                java -version
                cd /apps/build/
                ./mvnw package -DskipTests=true
				'''
            }
        }
        stage('Docker Image Creation') {
            steps {
                // Run the tests
                sh '''
		echo "Create a image"
		'''
            }
        }
        
        stage('Deployment on UT') {
            steps {
                // Run the tests
                sh '''
				echo "deploing to UT"
				echo " deploying to REG"
				'''
            }
        }
        stage('Deployment on REG') {
            steps {
                // Deploy the application
                sh 'echo copying to REG'
            }
        }
    }
}
