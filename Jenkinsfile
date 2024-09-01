node('build-jdk17-mvn3.9.4_1') {
    stage('git') {
        git 'https://github.com/Gitprasannag17/spring-petclinic-pras.git'
    }
    stage('build') {
        sh 'mvn clean package'
    }
    stage('archive') {
    archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
}