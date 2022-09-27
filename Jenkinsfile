node {
    stage('test') {
        sh 'echo hello SIR this is a scripted pipeline'
    }
    stage('learning-gitcloning') {
        git url: 'https://github.com/srikanthreddygajjala070/spring-petclinic.git',
            branch: 'NEW_BRANCH'
    }
    stage('build') {
        sh 'mvn package'
    }
}