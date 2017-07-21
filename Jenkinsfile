pipeline {
  agent any
  stages {
    stage('test') {
      steps {
        parallel(
          "test": {
            echo 'hello world'
            
          },
          "": {
            build 'pet clinic'
            
          }
        )
      }
    }
    stage('done') {
      steps {
        echo 'Done!!!'
      }
    }
  }
}