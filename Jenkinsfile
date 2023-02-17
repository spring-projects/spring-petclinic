pipeline{
    agent{
        label 'docker'
    }
    triggers{
        pollSCM('* * * * *')
    }
    parameters{
        choice(name: 'branch_name', choices: ['main', 'two', 'three'], description: 'selecting branch')
    }
    environment{
        dockerhub_registry_name = "lahari104"
        image_name = "spc" 
    }
    stages{
        stage('docker image build'){
            steps{
                sh 'docker image build -t $env.image_name:${BUILD_NUMBER}-${NODE_NAME} .' 
            }
        }
    }
}
