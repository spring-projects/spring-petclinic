node {
    
    environment{
        echo " project Info "
        ENVIRONMENT_NAME = 'CI'
    }
    
    stage ('SCM checkout') {
        git credentialsId: 'Jenkins', url: 'https://github.com/smartinj/spring-petclinic.git'
    }
    
    stage ('MVN Package') {
        def mvnHome = tool name: 'M3', type: 'maven'
        def mvnCMD = "${mvnHome}/bin/mvn"
        sh "${mvnCMD} clean package -DskipTests"
    }
    
    
    stage ('Build Docker Image') {
        docker build -t spring-petclinic .''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '//opt//docker', remoteDirectorySDF: false, removePrefix: 'target', sourceFiles: 'target/*war')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
    }
    
}
