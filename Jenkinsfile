[1mdiff --git a/Jenkinsfile b/Jenkinsfile[m
[1mindex 77c1567..a9df5cf 100644[m
[1m--- a/Jenkinsfile[m
[1m+++ b/Jenkinsfile[m
[36m@@ -3,21 +3,11 @@[m [mpipeline {[m
     agent any[m
 [m
     environment {[m
[31m-        SONARQUBE_SERVER = 'SonarQube'[m
[32m+[m[32m        SONARQUBE = 'SonarQube'[m
     }[m
 [m
     stages {[m
 [m
[31m-        stage('Checkout') {[m
[31m-            steps {[m
[31m-                echo 'Checking out source code...'[m
[31m-[m
[31m-                git branch: 'devops-project',[m
[31m-                    credentialsId: 'git-cred',[m
[31m-                    url: 'https://github.com/HarshithaLYadav/spring-petclinic.git'[m
[31m-            }[m
[31m-        }[m
[31m-[m
         stage('Environment') {[m
             steps {[m
                 echo 'Checking build environment...'[m
[36m@@ -37,29 +27,22 @@[m [mpipeline {[m
 [m
         stage('Build & Test') {[m
             steps {[m
[31m-                echo 'Building application and running tests...'[m
[32m+[m[32m                echo 'Building Spring PetClinic and running tests...'[m
 [m
                 sh '''[m
[31m-                    ./mvnw clean verify[m
[32m+[m[32m                    mvn clean verify[m
                 '''[m
             }[m
[31m-[m
[31m-            post {[m
[31m-                always {[m
[31m-                    junit testResults: '**/target/surefire-reports/*.xml',[m
[31m-                          allowEmptyResults: true[m
[31m-                }[m
[31m-            }[m
         }[m
 [m
         stage('SonarQube Analysis') {[m
             steps {[m
                 echo 'Running SonarQube analysis...'[m
 [m
[31m-                withSonarQubeEnv("${SONARQUBE_SERVER}") {[m
[32m+[m[32m                withSonarQubeEnv("${SONARQUBE}") {[m
                     sh '''[m
[31m-                        ./mvnw sonar:sonar \[m
[31m-                          -Dsonar.projectKey=Spring-Petclinic \[m
[32m+[m[32m                        mvn sonar:sonar \[m
[32m+[m[32m                          -Dsonar.projectKey=spring-petclinic \[m
                           -Dsonar.projectName=Spring-Petclinic[m
                     '''[m
                 }[m
[36m@@ -70,7 +53,7 @@[m [mpipeline {[m
             steps {[m
                 echo 'Waiting for SonarQube Quality Gate...'[m
 [m
[31m-                timeout(time: 10, unit: 'MINUTES') {[m
[32m+[m[32m                timeout(time: 5, unit: 'MINUTES') {[m
                     waitForQualityGate abortPipeline: true[m
                 }[m
             }[m
[36m@@ -78,17 +61,17 @@[m [mpipeline {[m
 [m
         stage('Package') {[m
             steps {[m
[31m-                echo 'Packaging application...'[m
[32m+[m[32m                echo 'Creating application package...'[m
 [m
                 sh '''[m
[31m-                    ./mvnw package -DskipTests[m
[32m+[m[32m                    mvn package -DskipTests[m
                 '''[m
             }[m
         }[m
 [m
         stage('Archive Artifact') {[m
             steps {[m
[31m-                echo 'Archiving JAR artifact...'[m
[32m+[m[32m                echo 'Archiving build artifacts...'[m
 [m
                 archiveArtifacts artifacts: 'target/*.jar',[m
                                  fingerprint: true[m
[36m@@ -97,23 +80,29 @@[m [mpipeline {[m
     }[m
 [m
     post {[m
[31m-[m
         success {[m
[31m-            echo '========================================'[m
[31m-            echo 'PIPELINE SUCCESSFUL'[m
[31m-            echo 'Build, Tests, SonarQube and Packaging passed.'[m
[31m-            echo '========================================'[m
[32m+[m[32m            echo '''[m
[32m+[m[32m========================================[m
[32m+[m[32mPIPELINE SUCCESS[m
[32m+[m[32m========================================[m
[32m+[m[32mSpring PetClinic build completed successfully.[m
[32m+[m[32mArtifact has been archived.[m
[32m+[m[32m========================================[m
[32m+[m[32m'''[m
         }[m
 [m
         failure {[m
[31m-            echo '========================================'[m
[31m-            echo 'PIPELINE FAILED'[m
[31m-            echo 'Check the failed stage in Jenkins.'[m
[31m-            echo '========================================'[m
[32m+[m[32m            echo '''[m
[32m+[m[32m========================================[m
[32m+[m[32mPIPELINE FAILED[m
[32m+[m[32m========================================[m
[32m+[m[32mCheck the failed stage in Jenkins.[m
[32m+[m[32m========================================[m
[32m+[m[32m'''[m
         }[m
 [m
         always {[m
[31m-            echo "Build completed: ${currentBuild.currentResult}"[m
[32m+[m[32m            echo 'Pipeline execution completed.'[m
         }[m
     }[m
 }[m
