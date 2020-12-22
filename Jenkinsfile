node('with-basic-tools-bea') {
    stage("Build and test") {
        checkout scm
        sh "mvn clean install"
    }
}