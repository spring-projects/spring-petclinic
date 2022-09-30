pipeline {
  
  agent any
  
//   triggers {
//     GenericTrigger(
//      genericVariables: [
//       [key: 'review_state', value: '$.review_state'],
//       [key: 'repo_name', value: '$.repository.full_name'],
//       [key: 'pr_url', value: '$.pull_request.html_url']
//      ],
//     )
//   }
  
  pipelineTriggers([
   [$class: 'GenericTrigger',
    genericVariables: [
     [key: 'ref', value: '$.ref'],
     [
      key: 'before',
      value: '$.before',
      expressionType: 'JSONPath', //Optional, defaults to JSONPath
      regexpFilter: '', //Optional, defaults to empty string
      defaultValue: '' //Optional, defaults to empty string
     ]
    ],
    genericRequestVariables: [
     [key: 'requestWithNumber', regexpFilter: '[^0-9]'],
     [key: 'requestWithString', regexpFilter: '']
    ],
    genericHeaderVariables: [
     [key: 'headerWithNumber', regexpFilter: '[^0-9]'],
     [key: 'headerWithString', regexpFilter: '']
    ],

    causeString: 'Triggered on $ref',

    token: 'abc123',
    tokenCredentialId: '',

    printContributedVariables: true,
    printPostContent: true,

    silentResponse: false,

    regexpFilterText: '$ref',
    regexpFilterExpression: 'refs/heads/' + BRANCH_NAME
   ]
  ])
  
  stages {
    stage('build step...') {
      steps {
        sh "echo $review_state"
      }
    }
  }
}

