pipeline {
    agent any
    tools {
        jdk 'JDK 8'
    }
    environment {
        SPRING_PROFILES_ACTIVE = "ci"
        MVN_POM_VERSION = readMavenPom().getVersion()
        MVN_ARTIFACTID = readMavenPom().getArtifactId()
    }
    stages {
        stage('Initialize') {
            steps {
                sh "printenv"
            }
        }
        stage('Compile') {
            steps {
                withMaven(maven: 'M3', options: [artifactsPublisher(disabled: true), jacocoPublisher(disabled: true)]) {
                    sh "mvn clean compile"
                }
            }
        }
    
    
    stage ('Build Docker Image') {
        docker build -t spring-petclinic .''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '//opt//docker', remoteDirectorySDF: false, removePrefix: 'target', sourceFiles: 'target/*war')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
    }
    
}
#nothing here
