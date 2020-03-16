pipeline {
    agent any
    node {
        try {
            notifySlack('STARTED')

            stages {
                stage('Build') {
                    steps {
                        sh './mvnw package'
                    }
                }

                stage('Testing') {
                    steps {
                        echo 'Testing'
                    }
                }

                stage('Package') {
                    steps {
                        echo 'Packaging'
                    }
                }

                stage('Deploy') {
                    steps {
                        echo 'Deploying'
                    }
                }
            }
        } catch (e) {
            // If there was an exception thrown, the build failed
            currentBuild.result = "FAILED"
            throw e
        } finally {
            // Success or failure, always send notifications
            notifySlack(currentBuild.result)
        }
    }  
}

def notifySlack(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus =  buildStatus ?: 'SUCCESSFUL'

  // Default values
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  // Send notifications
  slackSend (color: colorCode, message: summary)
}
