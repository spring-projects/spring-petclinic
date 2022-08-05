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

        stage('Commit message checkuot') {
            steps {
                script {
                    env.GIT_COMMIT_MSG = sh (script: 'git log -1 --pretty=%B ${GIT_COMMIT}', returnStdout: true).trim()
                }
                echo "Checking commit message format:\n${GIT_COMMIT_MSG}"
                sh '''#!/bin/bash
                    if [[ $(echo ${GIT_COMMIT_MSG} | grep -qP "^[A-Z]+-[0-9]+[0-9a-zA-z ,.:]{1,72}\\n*([0-9a-zA-Z ,.:]{1,80}\\n)*"; echo $?) == 0 ]]; then
                        echo 'Message checkout passed.'
                    else
                        echo 'Message does not complies with best practices. See: https://robertcooper.me/post/git-commit-messages'
                        exit 1
                    fi
                '''
            }
        }

        stage('Dockerfiles lint') {
            agent {
                docker {
                    image 'hadolint/hadolint:latest-debian'
                    args '-v ${WORKSPACE}/:/mnt/'
                }
            }
            steps {
                sh 'hadolint /mnt/Dockerfile'
                sh 'echo $?'
            }
        }
    }
}




