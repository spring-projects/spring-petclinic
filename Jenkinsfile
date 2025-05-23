pipeline {

    agent any 

    stages {

        stage ("Build"){
            steps {
               sh "./mvnw install"     
            }
        }

        stage ("Run unit-test"){

            steps {
               sh "./mvnw test"
                           }
        }
    }
}
