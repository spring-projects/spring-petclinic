node {
    git branch 'test',  url: 'https://github.com/k-fathi/spring-petclinic.git'
    
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
        if (env.BRANCH_NAME == "test"){
            sh'echo "test stage"'
        }
        else {
            sh'echo "skip test stage"'
        }
    }
}