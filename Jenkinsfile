pipeline {
  agent {
    docker{
      image 'maven:alpine'
      args '--network=ci_attachable'
    }
  }
  options {
    gitLabConnection('gitlab')
    buildDiscarder(logRotator(numToKeepStr: '5'))
    timestamps()
  }
  triggers {
    gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
    pollSCM '@hourly'
  }
  stages {
    stage("build") {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true --settings /var/jenkins_home/settings-docker.xml clean package'
      }
    }
    stage('Quality Gates') {
      steps {
        sh 'mvn --settings /var/jenkins_home/settings-docker.xml sonar:sonar'
      }
    }
    stage('Deploy - Dev') {
      steps {
        sh 'echo deploying to dev...'
      }
    }
    stage('Selenium Test') {
      steps {
        parallel (
          "Firefox" : {
            sh "echo testing FFX"
            sh "echo more steps"
          },
          "Chrome" : {
            sh "echo testing Chrome"
            sh "echo more steps"
          }
        )
      }
    }
    stage('Deploy - Staging') {
      steps {
        sh 'echo deploying to staging...'
        sh 'echo smoke tests...'
      }
    }
    stage('Deploy - Production') {
      steps {
        sh 'echo deploying to production...'
      }
    }
  }
  post {
    failure {
      updateGitlabCommitStatus name: 'build', state: 'failed'
    }
    success {
      updateGitlabCommitStatus name: 'build', state: 'success'
    }
    always {
      archive "target/**/*"
      junit(allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml')
      archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true, onlyIfSuccessful: true, defaultExcludes: true)
    }
  }
}
