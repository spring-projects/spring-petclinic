pipeline{
  agent any
  stages {
    stage("Clean"){
      steps{
        sh './mvnw clean'
      }
    }
    stage("Compile"){
      steps{
        sh './mvnw compile'
      }
    }
    stage("Test"){
      steps{
        sh './mvnw test'
      }
    }
    stage("Install"){
      steps{
        sh './mvnw install'
      }
    }
  }
}
