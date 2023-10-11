// delcarative


pipeline {
    agent any
    
    stages {
                 
        
        stage("Build"){
            steps {
                sh "./mvnw package"
            }
       
        }
        
        stage ("capture") {
            steps {
                            
            archiveArtifacts '**/target/*.jar'
            jacoco()
            junit '**/target/surefire-reports/*.xml'
            
                
            }

        }
    }
    post {
        success {
            emailext(
                subject: "🟢 Build réussi pour ${env.JOB_NAME}",
                body: "Super nouvelle ! Le build ${env.BUILD_NUMBER} s'est bien déroulé. Voir les détails ici : ${env.BUILD_URL}",
                recipientProviders: [culprits(), developers()],
                to: 'test@teachmemore.fr'
            )
        }
        unstable {
            emailext(
                subject: "🟡 Attention : Build instable pour ${env.JOB_NAME}",
                body: "Il semble y avoir quelques problèmes avec le build ${env.BUILD_NUMBER}. Veuillez vérifier ici : ${env.BUILD_URL}",
                recipientProviders: [culprits(), developers()],
                to: 'test@teachmemore.fr'
            )
        }
        failure {
            emailext(
                subject: "🔴 Échec du build pour ${env.JOB_NAME}",
                body: "Malheureusement, le build ${env.BUILD_NUMBER} a échoué. Consultez les détails et les logs ici : ${env.BUILD_URL}",
                recipientProviders: [culprits(), developers()],
                to: 'test@teachmemore.fr'
            )
        }
    }
}
