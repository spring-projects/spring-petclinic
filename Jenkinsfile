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
        stage('sonar analysis') {
            steps {
                withSonarQubeEnv('SONAR_TOKEN') {
                    sh 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=qtsonarqube_qtsonarqube-token -Dsonar.organization=qtsonarqube'
            }
        }
        stage('postbuild') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic-3.0.0-SNAPSHOT.jar', 
                followSymlinks: false
                junit '**/surefire-reports/TEST-*.xml'                 
            }
        }
    }
}