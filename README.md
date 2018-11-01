# Spring PetClinic Sample Application (Cloudogu Ecosystem)

This is the well-known spring sample application petclinic that has been extended to be a showcase for the Clodugou 
EcoSystem. See [demo.cloudogu.net](https://cloudogu.com/en/#demo).

We extended it by  

* a [Smeagol Wiki](https://github.com/cloudogu/smeagol) (see [Home.md](docs/Home.md))
* an [integration test](src/test/java/org/springframework/samples/petclinic/owner/OwnerControllerITCase.java) (run with failsafe plugin, see [pom.xml](pom.xml))
* a Jenkinsfile for building, testing and SonarQube analysis.

For more details on petclinic, see also [original spring petclinci readme.md](readme-petclinic.md).
