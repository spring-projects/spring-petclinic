    node {
        stage('sourcecode'){
            //build step
            git branch: 'main', url: 'https://github.com/vishnu1411/spring-petclinic.git'
        }
        stage('Build and package'){
            //building using maven and install
            sh 'mvn clean package'
        }
        stage('Archive  test results'){
            //archive  test reuslts
            junit '**/surefire-reports/*xml'
            archiveArtifacts artifacts: '**/*.war', followSymlinks: false
        }
    }