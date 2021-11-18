pipeline {
    agent {
        label 'docker-exec'
    }
    
    environment {
        ENV = "$BRANCH_NAME"
        TAG = "$ENV-latest"
    }
    
    stages {
        stage('Variables') {
            steps {
                sh 'echo $APP_NAME'
                sh 'echo $ARTIFACT_REPO'
                sh 'echo $ARTIFACT_URL'
                sh 'echo $TAG'
            }
        }
        stage('Checkout') {
            steps {
                git branch: "$ENV", credentialsId: 'github-token', url: '$GITHUB_REPO'
            }
        }
        stage('Build artifact') {
            steps {
                container('docker') {
                    sh 'docker build -t $APP_NAME:$TAG .'
                    sh 'docker tag $APP_NAME:$TAG $ARTIFACT_REPO/$APP_NAME:$TAG'
                    sh 'docker tag $APP_NAME:$TAG $ARTIFACT_REPO/$APP_NAME:$BUILD_NUMBER'
                }
            }
        }
        stage('Push artifact') {
            steps {
                container('docker') {
                    withCredentials([file(credentialsId: 'container-registry-sa', variable: 'SA_JSON')]) {
                        sh "cp \$SA_JSON ./key.json"
                        sh 'cat key.json | docker login -u _json_key --password-stdin https://$ARTIFACT_URL'
                        sh 'docker push $ARTIFACT_REPO/$APP_NAME:$TAG'
                        sh 'docker push $ARTIFACT_REPO/$APP_NAME:$BUILD_NUMBER'
                    }
                }
            }
        }
        stage('Trigger deploy job') {
            steps {
                build job: 'pipeline-deploy-from-scm', parameters: [string(name: 'ENV', value: "$ENV")]
            }
        }
    }
}
