pipeline {
    agent any
        stages {
            stage ('Git clone from SCM')
                steps {
                    git branch: 'main', credentialsId: 'Jenkins_Node', url: 'https://github.com/Mallaparao/spring-petclinic.git'
                }
        }
}