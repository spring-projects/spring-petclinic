pipeline {
    
    agent { label "build" }
    
    tools {
        maven "M3"
    }
    
    stages {
        
        stage("prepare") {
            steps {
                checkout scm
            }
        }
        
        stage("build") {
            steps {
                sh "mvn clean package"
            }
            post {
                always {
                    archive "target/*.jar"
                    junit 'target/surefire-reports/*.xml'
                    stash includes:"target/*.jar", name: "binary"
                }
            } 
        }
        
        stage("tests") {
            steps {
                parallel (
                    "static-analysis" : {
                        withSonarQubeEnv('sonarqube') {
                            sh 'mvn sonar:sonar'
                        }
                    },
                    "performance-tests": {
                        echo "performance tests"
                    }
                )            
            }
        }
        
        stage("deploy") {
            steps {
                unstash name:"binary"
                input "Deploy ?"                
            }
        }
    }
}
