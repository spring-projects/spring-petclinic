pipeline {
  
  agent any
  
  triggers {
    GenericTrigger(
     genericVariables: [
      [key: 'review_state', value: '$.review_state'],
      [key: 'repo_name', value: '$.repository.full_name'],
      [key: 'pr_url', value: '$.pull_request.html_url']
     ],

     causeString: 'Triggered on $repo_name $pr_url',

     token: 'opa-test',
     tokenCredentialId: 'http://178.128.149.213:8080/generic-webhook-trigger/invoke?token=opa-test',

     printContributedVariables: true,
     printPostContent: true,

     silentResponse: false,

     regexpFilterText: '$ref',
     regexpFilterExpression: 'refs/heads/' + BRANCH_NAME
    )
  }
  stages {
    stage('build step') {
      steps {
        sh "echo $review_state"
      }
    }
  }
}

