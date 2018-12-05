stage('Build') {
    node {
        git url: 'https://github.com/Takayuki-sempai/spring-petclinic'
        env.PATH = "${tool 'Maven3'}/bin:${env.PATH}"
        cmd 'mvn clean package'
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }
}
