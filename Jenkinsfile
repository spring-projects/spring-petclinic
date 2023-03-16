pipeline{
    agent{
        label 'springpet'
    }
    stages {
        stage ('scm'){
            steps {
                    
                git url: "https://github.com/nagarjuna33/spring-petclinicnew.git",
                    branch: 'main'
            }
        }
        stage ('build package') {
            
            steps {
                sh 'mvn package'
}
   }
       stage ('deployment') {
            
            steps {
                sh 'ansible-playbook -i /home/ansible/inventory.yml springpet.yml '
}
   }
    }
}
        
