    node {
        stage('sourcecode'){
            //build step
            git branch: 'main', url: 'https://github.com/vishnu1411/spring-petclinic.git'
        }
        stage('Build and install'){
            //building using maven and install
            sh 'mvn clean install'
        }
        stage('Archive  test results'){
            //insteall & archive  test reuslts
            junit '**/surefire-reports/*xml'
            archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
        }
    }