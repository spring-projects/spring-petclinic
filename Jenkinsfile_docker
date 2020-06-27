library 'my-pipeline-library' _

properties ([
        parameters([
                string(name: 'BRANCH', defaultValue: 'master', description: 'Branch to build'),
	        choice(name: 'RUN_TEST', choices: ['yes', 'no'], description: 'Run test while build'),
	        booleanParam(name: 'MAIL_TRIGGER', defaultValue: true, description: 'mail to be triggred'),
                string(name: 'EMAIL', defaultValue: 'mrcool435@gmail.com', description: 'Mail to be notified')
   
        ])
   ])
           


//myDeliveryPipeline {
//    branch = params.BRANCH
//    runTest = params.RUN_TEST
//    triggerMail = params.MAIL_TRIGGER
//    email = params.EMAIL
//}

myDeliveryPipeline {
    branch = params.BRANCH
    scmUrl = 'ssh://git@myScmServer.com/repos/myRepo.git'
    email = 'team@example.com'
    serverPort = '8080'
    developmentServer = 'dev-myproject.mycompany.com'
    stagingServer = 'staging-myproject.mycompany.com'
    productionServer = 'production-myproject.mycompany.com'
}
