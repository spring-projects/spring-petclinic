pipeline{
    agent{
        label "ubuntu-agent"
    }
    stages{
        stage("A"){
            when {
                branch 'main'
            }
            steps{
                echo "MAIN BRANCH"
                sh "printenv"
            }
        }

        stage("B"){
            when {
                changeRequest()
            }
            steps {
                echo "PR TEST"
                sh "printenv"
            }
        }
    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ======="
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}
