def COLOR_MAP = [
    'SUCCESS': 'good', 
    'FAILURE': 'danger',
]
def getBuildUser() {
    return currentBuild.rawBuild.getCause(Cause.UserIdCause).getUserId()
}
pipeline {
    agent any
     tools {
        maven 'maven 3.6'
        jdk 'Java 1.8'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile' 
            }
        }
        stage('Test') {
            steps {
                 sh 'mvn test'  
            }
        }
        stage('Package') {
            steps {
                 sh 'mvn package'  
            }
        }

        stage('Deploy') {
             /* only deploy when branch is master */
            when {
                branch 'master'
            }
            steps {
                sh 'mvn deploy' 
            }
        }

    }

// Post-build actions
    post {
        success{
                slackSend channel: '#slack-test-channel',
                color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"
        }
        failure{
                slackSend channel: '#slack-test-channel',
                color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} by ${BUILD_USER}\n More info at: ${env.BUILD_URL}"
        }
    }


}


