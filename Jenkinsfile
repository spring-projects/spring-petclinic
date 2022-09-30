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
                    setBuildStatus('OPA Check Approved', 'PENDING', 'params.GIT_COMMIT')
                }
                sh "echo ${REVIEW_STATE}"
                sh "echo ${REPO_NAME}"
                sh "echo ${PR_STATE}"
//                 setBuildStatus('OPA Check Approved', 'SUCCESS', 'params.GIT_COMMIT')
            }
        }
        }
        stage('Build') {
          setRunContext()
            steps {
                echo 'compiling...'
              echo '$'
            }
        }
        stage('Test') {
            steps {
                echo 'testing...'
            }
        }
        stage('Approval') {
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


def setBuildStatus(String message, String state, String sha){
    step([$class: 'GitHubCommitStatusSetter',
    contextSource: [$class: 'ManuallyEnteredCommitContextSource', context: 'OPA test'],
    commitShaSource: [$class: 'ManuallyEnteredShaSource', sha: sha],
    statusResultSource: [$class: 'ConditionalStatusResultSource', result: [$class: 'AnyBuildResult', message: message, state: state]]

    ])
}

def setRunContext(){
    if ("${REVIEW_STATE}" && "${PR_STATE}"){
        echo "Present"
    }
}












