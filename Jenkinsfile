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
        withSonarQubeEnv('sonar')
        env.PATH = "${tool 'Maven3'}/bin:${env.PATH}"
        bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.5.0.1254:sonar'
    }
}
