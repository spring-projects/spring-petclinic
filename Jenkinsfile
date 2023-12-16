    node {
        stage('sourcecode'){
            //build step
            git branch: 'main', url: 'https://github.com/vishnu1411/spring-petclinic.git'
        }
        stage('Build and package'){
            //building using maven and install
            sh 'mvn clean package'
        }
        stage('Install & Archive  test results'){
            //insteall & archive  test reuslts
            shh 'mvn clean install'
            junit '**/surefire-reports/*xml'
            archiveArtifacts artifacts: '**/*.war', followSymlinks: false
        }
    }