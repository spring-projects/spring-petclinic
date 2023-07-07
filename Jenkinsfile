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
                sh './gradlew build'
            }
        }
        stage('PR - Push') {
            when { branch 'PR-*' }
            steps { 
                sh 'echo <PLACEHOLDER>'
            }
        }
        
        stage('Main - create tag') {
            when { branch 'main' }
            steps { 
                sh 'echo <create tag>'
            }
        }
        stage('Main - tag the artifact') {
            when { branch 'main' }
            steps { 
                sh 'echo <tag artifact>'
            }
        }
        stage('Main - push to repo') {
            when { branch 'main' }
            steps {
                sh 'echo <push>'
            }
        }
    }
}
