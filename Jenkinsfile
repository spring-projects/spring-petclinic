pipeline {
    agent any

    stages {
        stage('PR - Check') {
            when { branch 'PR-*' }
            steps {
                sh './gradlew check'
            }
        }

        stage('PR - Test') {
            when { branch 'PR-*' }
            steps {
                sh './gradlew test'
            }
        }

        stage('PR - Build') {
            when { branch 'PR-*' }
            steps {
                sh './gradlew clean build'
            }
        }

        stage('PR - Build + Push') {
            when { branch 'PR-*' }
            steps { 
                sh 'docker build -t us-west3-docker.pkg.dev/playground-s-11-5cd45b0d/docker-registry/petclinic:$(git tag | tail -1)-$(git rev-parse --short HEAD)'
                sh 'docker push us-west3-docker.pkg.dev/playground-s-11-5cd45b0d/docker-registry/petclinic'
            }
        }
        
        stage('Main - create tag') {
            when { branch 'main' }
            steps { 
                sh 'echo "<create tag>"'
            }
        }

        stage('Main - tag the artifact') {
            when { branch 'main' }
            steps { 
                sh 'echo "<tag artifact>"'
            }
        }

        stage('Main - push to repo') {
            when { branch 'main' }
            steps {
                sh 'echo "<push>"'
            }
        }
    }
}
