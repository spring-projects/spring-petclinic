// node {
//     git branch: 'main', url: 'https://github.com/k-fathi/spring-petclinic.git'
//     stage('build'){
//         try{
//             sh'echo "this is a stage to build" '
//         }
//         catch(Exception err){
//             sh'echo "an error occured"'
//             throw err
//         }
//     }
//     stage('test'){
//         sh'echo "The current branch is >>>${env.BRANCH_NAME}<<< "'
//         if (env.BRANCH_NAME == 'main')
//         {
//             sh'echo "test stage in progress"'
//         }
//         else
//         {
//             sh'echo "skipping test stage"'
//         }
//     }
// }


pipeline {
    agent any
    stages{
        stage('build'){
            steps{
                echo "this is a build stage"
                echo "test push-1"
                echo "test push-2 >> editing security settings"
            }
        }
    }
}