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
     tokenCredentialId: '',

     printContributedVariables: true,
     printPostContent: true,

     silentResponse: false,

     regexpFilterText: '$ref',
     regexpFilterExpression: 'refs/heads/' + BRANCH_NAME
    )
  }
  stages {
    stage('Some step') {
      steps {
        sh "echo $review_state"
      }
    }
  }
}