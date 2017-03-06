pipeline {
    
    agent any
    
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
            agent { label: "build" }
            steps {
                sh "mvn clean package"
            }
            post {
                always {
                    archive "target/*.jar"
                    junit 'target/surefire-reports/*.xml'
                    stash includes: "**", name: "sources"
                    stash includes: "target/*.jar", name: "binary"
                }
            } 
        }
        
        stage("tests") {
            steps {
                parallel (
                    "static-analysis" : {
                        node("build") {
                            unstash "sources"
                            withSonarQubeEnv('sonarqube') {
                                sh 'mvn sonar:sonar'
                            }
                        }
                    },
                    "performance-tests": {
                        node("build") {
                            echo "performance tests"
                            sleep 20
                        }
                    }
                )            
            }
        }
        
        stage("deploy") {
            agent { label "ssh" }
            steps {
                unstash name:"binary"
                sh "ls -rtl target/"
                input "Deploy ?"                
            }
        }
    }
}
