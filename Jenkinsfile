pipeline {
    agent { label 'petclinic' }

    environment {
        DOCKER_STORAGE = 'testfiesta/petclinic'
        SHORT_COMMIT = "${GIT_COMMIT[0..7]}"
        GIT_TAG = ''
    }

    tools {
        gradle '8.7'
    }

    parameters {
        choice(name: 'ACTION', choices: ['MR', 'Deploy'], description: 'Choose the action to perform')
    }

    stages {
        // Merge request pipeline
        stage('Checkstyle') {
            when {
                changeRequest()
            }
            steps{
                echo 'Running gradle checkstyle'
                sh './gradlew check --no-daemon'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'build/reports/checkstyleNohttp/*.html', fingerprint: true
                }
            }
        }
        stage('Test') {
            when {
                changeRequest()
            }
            steps {
                echo 'Running gradle test'
                sh './gradlew test -x check --no-daemon'
            }
        }
        stage('Build') {
            when {
                changeRequest()
            }
            steps {
                echo 'Running build automation'
                sh './gradlew build -x test -x check -x checkFormat -x processTestAot --no-daemon'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true            }
        }
        stage('Docker Build (MR)') {
            when {
                changeRequest()
            }
            steps { 
                echo 'Building docker Image'
                sh 'docker build -t $DOCKER_STORAGE:${SHORT_COMMIT} .'
            }
        }
        stage('Docker Login (MR)') {
            when {
                changeRequest()
            }
            steps {
                echo 'Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                        sh 'echo $PASS | docker login -u $USER --password-stdin $DOCKER_STORAGE'
                    }    
                }
            }
        }
        stage('Docker Push (MR)') {
            when {
                changeRequest()
            }
            steps {
                echo 'Pushing Image to Docker repository'
                sh 'docker push $DOCKER_STORAGE:${SHORT_COMMIT}'
            }
        }

        // Main branch pipeline
        stage('Git tag the current state') {
            when {
                branch 'main'
            }
            steps { 
                echo 'Tagging'
                
            }
        }
        stage('Docker Build (Main)') {
            when {
                branch 'main'
            }
            steps { 
                echo 'Building docker Image'
                sh 'docker build -t $DOCKER_STORAGE:${GIT_TAG} .'
            }
        }
        stage('Docker Login (Main)') {
            when {
                branch 'main'
            }
            steps {
                echo 'Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                        sh 'echo $PASS | docker login -u $USER --password-stdin $DOCKER_STORAGE'
                    }
                }
            }
        }
        stage('Docker Push (Main)') {
            when {
                branch 'main'
            }
            steps {
                echo 'Pushing Image to Docker repository'
                sh 'docker push $DOCKER_STORAGE:${GIT_TAG}'
            }
        }
    }
    post {
        always {
            sh 'docker logout'
            cleanWs()
        }
    }
}