pipeline {
    agent 'any'
    triggers { pollSCM '* * * * *' }
    stages {
        stage( 'version control sysytem') {
            steps {
            git url: 'https://github.com/spring-projects/spring-petclinic.git',
                branch: 'main'
            }
        }
        stage ('package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('build') {
            steps {
                sh 'export PATH="/usr/lib/jvm/java-1.17.0-openjdk-amd64/bin:$PATH" && mvn package'
            }
        }
        stage('postbuild') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic.war', 
                followSymlinks: false
                junit '**/surefire-reports/TEST-*.xml'                 
            }
        }
    }
}