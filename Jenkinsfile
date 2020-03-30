pipeline{
  agent any
  stages {
    stage("Compile"){
      steps{
        sh './mvnw compile'
      }
    stage("Test"){
      steps{
        sh './mvnw test'
      }
    stage("Install"){
      steps{
        sh './mvnw install'
      }
    stage("Deploy"){
      steps{
        sh './mvnw deploy'
      }
    }
  }
}
