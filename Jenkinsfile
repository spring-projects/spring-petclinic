pipeline {
        agent { lebal 'MAVEN' }
        options {
            timeout(time: 1, unit: 'HOURS')
        }
        triggers {
            pollSCM('* * * * *')
        }
        stages {
            stage('git') {
                steps {
                    git url: 'https://github.com/siddhaantkadu/spring-petclinic.git',
                        branch: 'dev'
                }
            }
            stage('build') {
                steps {
                    sh 'mvn clean package'
                }
                post {
                    success {
                        archiveArtifacts artifacts: '**/spring-petclinic-*.jar'
                    }
                    always {
                        junit testResults: '**/TEST-*.xml' 
                    }
                }
            }
        }
}
