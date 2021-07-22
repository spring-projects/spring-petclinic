pipeline {
           agent any
           stages {
                stage("Hello") {
                     steps {
                         echo 'Hello World!!!!'
                     }
                }
                stage("Build") {
                     steps {
                     git url: '', branch: 'jenkins-test'
                     echo 'Checkout branch'
                     script {
                     image = docker.build("asalanevich/spring-petclinic:${env.BUILD_ID}")
                     }
                }
                stage("Push") {
                     steps {
                     withCredentials([usernamePassword(credentialsId: 'jenkins-docker', usernameVariable: 'login', passwordVariable: 'password')])
                     sh """
                     docker login -u ${login} -p ${password}
                     docker push asalanevich/spring-petclinic:${env.BUILD_ID}
                     docker push asalanevich/spring-petclinic:latest
                     """
                     }
                }
           }
      }

