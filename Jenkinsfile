pipeline {
  
  agent any

  triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'all', value: '$']
    //   [key: 'review_state', value: '$.review.state'],
    //   [key: 'pull_request_state', value: '$.pull_request.state']
     ]
    )
  }

  parameters{
    string(defaultValue: '', description: '', name: 'GIT_COMMIT')
  }
  
  stages {
        stage('PR Approval') {
         
        steps {
            script {
            //   if("${pull_request_state}" == "open" && "${review_state}" == "approved"){
            //     echo "${pull_request_state}"
            //     }
            //     else if("${pull_request_state}" == "open" && "${review_state}" == "commented") {
            //         setBuildStatus('OPA Check Approved', 'PENDING', 'params.GIT_COMMIT')
            //     }
                sh "echo ${all}"
                // sh "echo ${review_state}"
                // sh "echo ${REPO_NAME}"
                // sh "echo ${pull_request_state}"
            }
        }
        }
        stage('Build') {
            // setRunContext()
            steps {
                echo 'compiling...'
            }
        }
        stage('Test') {
            steps {
                echo 'testing...'
            }
        }
        stage('Approval') {
            // no agent, so executors are not used up when waiting for approvals
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

// def setRunContext(){
//     def jsonSlurper = new JsonSlurper()
//     json = jsonSlurper.parse(new File('data to pass in'))
//     if (json.has("${pull_request_state}" && "${review_state}")){
//         String get_state = json.getString("${pull_request_state}" && "${review_state}")
//     }
// }



