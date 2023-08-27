pipeline{
    agent{
        label 'lahari'
    }
    triggers{
        pollSCM('* * * * *')
    }
    parameters{
        choice(name: 'Branch_Name', choices: ['develop', 'main', 'lahari'], description: 'selecting branch')
    }
    stages{
        stage('clone'){
            steps{
                git url: 'https://github.com/lahari104/spring-petclinic.git',
                    branch: "${params.Branch_Name}"
            }
        }
        stage('build'){
            steps{
                sh "./mvnw clean install"
            }
        }
        stage('archive_artifacts'){
            steps{
                archiveArtifacts artifacts: '**/target/*.jar'
            }
        }
        // stage('junit_reports'){
        //     steps{
        //         junit '**/surefire-reports/*.xml'
        //     }
        // }
    }
    post{
        always{
            echo 'Pipeline is triggered'
            mail to: 'goruputivenkatalahari@gmail.com',
                 subject: 'The pipeline is started',
                 body: """pipeline is started for $env.BUILD_URL
                          and the build number is $env.BUILD_NUMBER"""
        }
        aborted{
            echo 'Pipeline is aborted'
            mail to: 'goruputivenkatalahari@gmail.com',
                 subject: 'The pipeline is aborted',
                 body: """pipeline is aborted for $env.BUILD_URL
                          and the build number is $env.BUILD_NUMBER
                          and jenkins url is $env.JENKINS_URL"""
        }
        failure{
            echo 'Pipeline is falied'
            mail to: 'goruputivenkatalahari@gmail.com',
                 subject: 'The pipeline is failed',
                 body: """pipeline is falied for $env.BUILD_URL
                          and the build number is $env.BUILD_NUMBER
                          and jenkins url is $env.JENKINS_URL"""
        }
        success{
            junit '**/surefire-reports/*.xml'
            echo 'Pipeline is success'
            mail to: 'goruputivenkatalahari@gmail.com',
                 subject: 'The pipeline is success',
                 body: """pipeline is success for $env.BUILD_URL
                          and the build number is $env.BUILD_NUMBER"""
        }
    }
}