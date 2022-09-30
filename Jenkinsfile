pipeline {
  agent any
//   triggers {
//     GenericTrigger(
//      genericVariables: [
//       [key: 'ref', value: '$.ref']
//      ],

//      causeString: 'Triggered on $ref',

//      printContributedVariables: false,
//      printPostContent: true,

//      silentResponse: false,

//      regexpFilterText: '$ref',
//      regexpFilterExpression: 'refs/heads/' + env.BRANCH_NAME
//     )
//   }
  stages {
    stage('Some step..') {
      steps {
        sh "echo ${ref}"
      }
    }
  }
}

