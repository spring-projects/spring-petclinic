node {


    stage('Checkout') {
        git 'https://github.com/ThilakrajKM/spring-petclinic'
    }

    stage('Build') {
        sh 'mvn clean package'
    }

    stage('Archive') {
        junit allowEmptyResults: true, testResults: '**/target/**/TEST*.xml'
    }

}