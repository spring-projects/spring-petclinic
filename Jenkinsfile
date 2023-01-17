pipeline {

    environment { 
        DOCKERHUB_CREDENTIALS=credentials('rolandgryddynamics-dockerhub')
        MERGE_REPOSITORY='mr'
        MAIN_REPOSITORY='main'
    }

    agent {
        node {
            label 'ubuntu-master'
        }
    }
    stages {
        stage('pull request') {
            steps {
                if(env.CHANGE_ID) {
                    sh "echo 'pull request"
                } else {
                    sh "echo 'pull request"
                }
            }
            // steps {
            //     script {
            //         if (BRANCH_NAME != 'main' && env.CHANGE_ID){
            //             sh "echo 'ndsakmmkdsa'"
            //             sh "echo 'hfiejrfrei'"
            //         } else if (BRANCH_NAME == 'main') {
            //             sh "echo main"
            //         } else {
            //             sh "echo $BRANCH_NAME"
            //             sh "echo $env.BRANCH_NAME"
            //         }
            //     }
            // }       
        }
    }
        // stage('checkstyle') {
        //     steps {
        //         sh './gradlew checkstyleMain'
        //         archiveArtifacts artifacts: 'build/reports/checkstyle/main.html'
        //     }
        // }
        // stage('test') {
        //     steps {
        //         sh './gradlew compileJava'
        //     }
        // }
        // stage('build') {
        //     steps {
        //         sh './gradlew build -x test'
        //     }
        // }
 
    //     stage('Build Docker image') {
    //         steps {
    //             script{
    //                 if (env.BRANCH_NAME == 'main') {
    //                    sh 'docker build -t $DOCKERHUB_CREDENTIALS_USR/$MAIN_REPOSITORY:$BUILD_NUMBER .'
    //                 } else {
    //                     sh 'docker build -t $DOCKERHUB_CREDENTIALS_USR/$MERGE_REPOSITORY:$BUILD_NUMBER .'
    //                 }
    //             }
    //         }
    //     }
    //     stage('Login DockerHub') {
    //         steps {
    //             sh 'docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW'
    //         }
    //     }
    //     stage('Deploy Docker image to DockerHub') {
    //         steps {
    //             script{
    //                 if (env.BRANCH_NAME == 'main') {
    //                     sh 'docker push $DOCKERHUB_CREDENTIALS_USR/$MAIN_REPOSITORY:$BUILD_NUMBER'
    //                 } else {
    //                     sh 'docker push $DOCKERHUB_CREDENTIALS_USR/$MERGE_REPOSITORY:$BUILD_NUMBER'
    //                 }
    //             }
    //         }
    //     }
    // }
    // post {
    //     always {
    //         sh 'docker logout'
    //     }
    // }
}