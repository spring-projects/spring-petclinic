node {
    stage('compile') {
         sh '''#!/bin/bash -x
                 source "$HOME/.sdkman/bin/sdkman-init.sh"
                 ls
                 pwd
                 ./mvnw validate compile
         '''
    }
}
