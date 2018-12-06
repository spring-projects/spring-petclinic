stage('Build') {
    node {
        git url: 'https://github.com/Takayuki-sempai/spring-petclinic'
        env.PATH = "${tool 'Maven3'}/bin:${env.PATH}"
        env.JAVA_HOME = "${tool 'Java8'}"
        bat 'mvn clean package'
        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }
}
stage('SonarQube analysis') {
    node {
        withSonarQubeEnv('sonar') {
            env.PATH = "${tool 'Maven3'}/bin:${env.PATH}"
            bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.5.0.1254:sonar'
        }
    }
    emailext body: 'Build failed', recipientProviders: [requestor()], subject: 'Build Failed', to: 'jenkinstest@rambler.ru'
    mail bcc: '', body: 'Build failed', cc: '', from: '', replyTo: '', subject: 'BuildFailed', to: 'jenkinstest@rambler.ru'
}
post {
    failure {
        emailext body: 'Build failed', recipientProviders: [requestor()], subject: 'Build Failed', to: 'jenkinstest@rambler.ru'
//        mail bcc: '', body: 'Build failed', cc: '', from: '', replyTo: '', subject: 'BuildFailed', to: 'jenkinstest@rambler.ru'
//        mail bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: 'jenkinstest@rambler.ru'
//        sh 'echo "This will run only if failed"'
    }
}
