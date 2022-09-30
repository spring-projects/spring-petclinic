pipeline {
  
  agent any

  parameters{
    string(defaultValue: '', description: '', name: 'GIT_COMMIT')
  }
  
  stages {
        stage('PR Approval') {
          
        steps {
            script {
              if("${PR_STATE}" == "open" && "${REVIEW_STATE}" == "approved"){
              echo "${PR_STATE}"
                }
                else if("${PR_STATE}" == "open" && "${REVIEW_STATE}" == "commented") {
                    echo "COMMENTED......"
                }
                sh "echo ${REVIEW_STATE}"
                sh "echo ${REPO_NAME}"
                sh "echo ${PR_STATE}"
//                 setBuildStatus('OPA Check Approved', 'SUCCESS', 'params.GIT_COMMIT')
            }
        }
        }
        stage('Build') {
            agent any
            steps {
                echo 'compiling...'
            }
        }
        stage('Test') {
            agent any
            steps {
                echo 'testing...'
            }
        }
        stage('Approval') {
            // no agent, so executors are not used up when waiting for approvals
            agent none
            steps {
                script {
                    def deploymentDelay = input id: 'Deploy', message: 'Deploy to production?', submitter: 'rkivisto,admin', parameters: [choice(choices: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24'], description: 'Hours to delay deployment?', name: 'deploymentDelay')]
                    sleep time: deploymentDelay.toInteger(), unit: 'HOURS'
                }
            }
        }
        stage('Deploy') {
            agent any
            steps {
                echo 'Deploying...'
            }
        }
  }
}

















