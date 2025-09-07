node {
    git branch 'main',  url: 'https://github.com/k-fathi/spring-petclinic.git'
    stage('build') {
        try{
            sh'echo "Hello, Karim! - Build stage"'
        }
        catch(Exeption e){
            sh'echo "exception run"'
            throw e
        }
    }
    stage('test'){
        if (env.BRANCH_NAME == 'feature '){
            sh'echo "test stage"'
        }
        else {
            sh'echo "skip test stage"'
        }
    }
}