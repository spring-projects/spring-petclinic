node('master') {
    stage('init') {
        checkout scm
    }

    stage('image build') {
        sh '''
            ./mvnw clean package
            cd target
            mv *.jar petclinic.jar
            cp ../web.config web.config
            zip petclinic.zip web.config petclinic.jar
        '''
    }

    stage('deploy') {
        azureWebAppPublish appName: env.APP_NAME,
            azureCredentialsId: env.CRED_ID,
            resourceGroup: env.RESOURCE_GROUP,
            filePath: 'target/*.zip'
    }
}