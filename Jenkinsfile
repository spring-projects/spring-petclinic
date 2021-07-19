pipeline {
    environment { 
        registry = "sprientera/pet" 
        registryCredential = 'dockerhub_id' 
        dockerImage =      '' 
        RELEASE_NOTES = sh (script: """git log --format="medium" -1 ${GIT_COMMIT}""", returnStdout:true)
    }
    agent any
    stages {
          stage('Jira2') {
            steps {
                jiraAddComment idOrKey: 'DEV-1', comment: 'hello', site: 'butenko992'
                env.revision = sh(script: "git log --pretty=format:\"%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset\" \$prevcommit...\$GIT_COMMIT", , returnStdout: true).trim()
            }
        }
        stage('get hash') {
            steps {
                sh 'echo ${RELEASE_NOTES}'
                sh 'echo ${GIT_COMMIT}'
            }
        }
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh './mvnw package'
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
        stage('Build Docker Image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    app = docker.build("sprientera/pet")                  
                }
            }
        }
        stage('Push Docker Image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'Docker_Hub') {
                        app.push("${env.BUILD_NUMBER}")
                        app.push("latest")
                    }
                }
            }
        }
        stage ('DeployToProduction') {
            when {
                branch 'main'
            }
            steps {
              input 'Deploy to Production'
              milestone(1)
              withCredentials ([usernamePassword(credentialsId: 'cloud_user', usernameVariable: 'USERNAME', passwordVariable: 'USERPASS')]) {
                script {
                  sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.server_api} \"docker pull sprientera/pet:${env.BUILD_NUMBER}\""
                  try {
                      sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.server_api} \"docker stop pet\""
                      sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.server_api} \"docker rm pet\""
                  } catch (err) {
                     echo: 'caught error: $err'
                  }
                  sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.server_api} \"docker run --restart always --name pet -p 8081:8080 -d sprientera/pet:${env.BUILD_NUMBER}\""
                    }
                }
            }
        }
        stage('JIRA') {
            steps {
            script {
            def testIssue = [fields: [ // id or key must present for project.
                               project: [id: 'DEV'],
                               summary: 'New JIRA Created from Jenkins.',
                               description: 'New JIRA Created from Jenkins.',
                               customfield_1000: 'customValue',
                               // id or name must present for issuetype.
                               issuetype: [id: '3']]]
                               response = jiraEditIssue idOrKey: 'DEV-1', issue: testIssue          
    echo response.successful.toString()
    echo response.data.toString()
            }
            }
}
    }
}    
