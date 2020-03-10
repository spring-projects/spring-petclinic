void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/Firassawan/spring-petclinic"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

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