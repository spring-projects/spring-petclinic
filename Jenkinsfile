pipeline {
    agent any
    stages {
        stage('Clone repo from feature branch') {
            steps {
                git branch: 'feature-newimg',
                    credentialsId: 'GitHub_SSH_Key_jenkins',
                    url: 'git@github.com:YuriyPelykh/spring-petclinic.git'
                sh "ls -lat"
            }
        }

        stage('Commit message check') {
            steps {
                script {
                    env.GIT_COMMIT_MSG = sh (script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
                }
                echo "${GIT_COMMIT_MSG}"
            }
        }

        stage('Dockerfiles lint') {
            steps {
                echo "In progress..."
            }
        }
    }
}




