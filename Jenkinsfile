pipeline{
    agent{
        label "ubuntu-agent"
    }
    stages{
        stage("A"){
            steps{
                sh "printenv"
            }
            post{
                always{
                    echo "========always========"
                }
                success{
                    echo "========A executed successfully========"
                }
                failure{
                    echo "========A execution failed========"
                }
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
