node('JDK-11-MVN') {
    stage('Git'){
        git 'https://github.com/ShaikNasee/spring-petclinic.git'
        branch 'main'
    }
    stage('build'){
        sh 'mvn clean package'
    }
    stage('Archive artifacts'){
        archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
    stage('publish test results'){
        junit 'target/surefire-reports/*.xml'
    }
}