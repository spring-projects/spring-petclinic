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
  
 
  stages {
    stage('build step..') {
      steps {
        sh "echo $review_state"
      }
    }
  }
}

