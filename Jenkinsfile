groovy
pipeline {
agent any // This can be refined to use a label with Maven installed
tools {
maven 'M3' // Tells Jenkins to use a pre-configured Maven installation
jdk 'JDK17' // Tells Jenkins to use a pre-configured JDK installation
}
stages {
stage('Checkout') {
steps {
// Checkout the code from the branch that triggered the build
git branch: '${BRANCH_NAME}', url:
'https://github.com/YOUR_USERNAME/spring-petclinic.git'
}
}
stage('Build & Package') {
steps {
// Compile the code and package it into a JAR file
sh 'mvn clean compile package -DskipTests'
}
}
stage('Test') {
steps {
// Run the unit tests
sh 'mvn test'
}
post {
always {
// Always publish the JUnit test results, even if the stage fails
junit 'target/surefire-reports/*.xml'
}
}
}
// A more advanced pipeline would have a 'Deploy to Staging' stage here
}
post {
always {
// Clean up after the build
cleanWs()
}
}
}
```