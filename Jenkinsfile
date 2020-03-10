String buildUrl = "${BUILD_URL}"
String gitStatusPostUrl = "https://<Github Personal Access Token>:x-oauth-basic@api.github.com/repos/<owner>/<repo>/statuses/${gitHash}"

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean' 
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
            when {
                 branch 'master'
             }
            steps {
                sh 'mvn deploy' 
            }
        }
    }
    post {
        success {
        mail to: 'firassawan@icloud.com',
             subject: "Succeeded Pipeline: ${currentBuild.fullDisplayName}",
             body: "Email Notification: The build has successfully completed ${env.BUILD_URL}"
    }
       failure {
        mail to: 'firassawan@icloud.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Email Notification: The build has not completed successfully ${env.BUILD_URL}"
    }

    }
}

sh """
curl -X POST -H "application/json" -d '{"state":"success", "target_url":"${buildUrl}", "description":"Build Success", "context":"build/job"}' "${gitStatusPostUrl}"
"""