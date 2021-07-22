pipeline {
           agent any
           stages {
                stage("Hello") {
                     steps {
                      timeout(time: 1, unit: 'MINUTES') {
                                         sh 'for n in `seq 1 10`; do echo $n; sleep 1; done'
                                     }
                         echo 'Hello World!!!!'
                     }
                }
           }
      }

