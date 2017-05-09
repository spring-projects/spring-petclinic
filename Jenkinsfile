pipeline {
    agent {
        docker {
            image 'maven:3.5.0'
            args '--network=plumbing_default'
        }
    }
    stages {
        stage ('Build') {
            steps {
                configFileProvider(
                    [configFile(fileId: 'nexus', variable: 'MAVEN_SETTINGS')]) {
                        sh 'mvn -s $MAVEN_SETTINGS clean deploy -DskipTests=true'
                    }
            }
        }
    }
}
