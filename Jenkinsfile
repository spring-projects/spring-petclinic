pipeline {
    agent any
    
    stages {
        stage("Build jartifact") {
            steps {
                echo "=============== Building starts =================="
                sh "pwd"
                sh "./mvnw package"
                dir ('target') {
                    sh 'mv *.jar petclinic.jar'
                }
                echo "=============== Building is complete =================="
            }
            
        }
        
    }

}
