pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw package'
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package'
            }
        }
        stage('Deploy') {
            steps {
                sh './mvnw package'
            }
        }
        stage('Send email') {
          def mailRecipients = "firassawan@icloud.com"
         def jobName = currentBuild.fullDisplayName

    emailext body: '''${SCRIPT, template="groovy-html.template"}''',
        mimeType: 'text/html',
        subject: "[Jenkins] ${jobName}",
        to: "${mailRecipients}",
        replyTo: "${mailRecipients}",
        recipientProviders: [[$class: 'CulpritsRecipientProvider']]
}
    }
}
