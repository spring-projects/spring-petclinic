pipeline {
    agent any  
	parameters {
		choice(name: 'BRANCH_TO_BUILD', choices: ['main', 'INT_REL_1.0'], description: 'BUILDING_BRANCHES')
		string(name: 'MAVEN_GOAL', defaultValue: 'mvn package', description: 'maven goals')
		}
    triggers {
		pollSCM ('* * * * *')
	}
        stages {
        stage ('Git clone from SCM') {
            steps {
                git branch: "${params.BRANCH_TO_BUILD}", credentialsId: 'Jenkins_Node', url: 'https://github.com/Mallaparao/spring-petclinic.git'
            }
        }
		stage ('Build') {
            steps {
                sh "${params.MAVEN_GOAL}"
            }
        }
   }
}