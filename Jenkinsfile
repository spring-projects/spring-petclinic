node('Slave_AWS'){
    stage('Checkout')
    {
        git 'https://github.com/spring-projects/spring-petclinic.git'
    }
    stage('Build')
    {
        //sh 'docker run --rm --name build_maven -v $(pwd):/app docker.io/maven:alpine bash -c "cd app;mvn clean install"'
    }
    stage('Unit Test')
    {
        junit 'target/surefire-reports/*.xml'
    }
    stage('Sonarqube')
    {
        sh 'docker run --rm --name maven -v $(pwd):/app docker.io/maven:alpine bash -c "cd app; mvn clean install cobertura:cobertura -Dcobertura.report.format=xml sonar:sonar -Dsonar.host.url=http://34.205.24.188:9000"'
    }
    stage('Archive artifact')
    {
        archiveArtifacts 'target/spring-petclinic-1.5.1.jar'
    }
}
