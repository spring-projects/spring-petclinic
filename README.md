# Spring PetClinic Sample Application (Cloudogu Ecosystem)

This is the well-known spring sample application petclinic that has been extended to be a showcase for the Cloudogu
EcoSystem. See [demo.cloudogu.net](https://cloudogu.com/en/#demo).

We extended it by  

* a [Smeagol Wiki](https://github.com/cloudogu/smeagol) (see [Home.md](docs/Home.md))
* an [integration test](src/test/java/org/springframework/samples/petclinic/owner/OwnerControllerITCase.java) (run with failsafe plugin, see [pom.xml](pom.xml))
* a Jenkinsfile for building, testing and SonarQube analysis.

For more details on petclinic, see also [original spring petclinci readme.md](readme-petclinic.md).

---
### What is the Cloudogu EcoSystem?
The Cloudogu EcoSystem is an open platform, which lets you choose how and where your team creates great software. Each service or tool is delivered as a Dogu, a Docker container. Each Dogu can easily be integrated in your environment just by pulling it from our registry. We have a growing number of ready-to-use Dogus, e.g. SCM-Manager, Jenkins, Nexus, SonarQube, Redmine and many more. Every Dogu can be tailored to your specific needs. Take advantage of a central authentication service, a dynamic navigation, that lets you easily switch between the web UIs and a smart configuration magic, which automatically detects and responds to dependencies between Dogus. The Cloudogu EcoSystem is open source and it runs either on-premises or in the cloud. The Cloudogu EcoSystem is developed by Cloudogu GmbH under [MIT License](https://cloudogu.com/license.html).

### How to get in touch?
Want to talk to the Cloudogu team? Need help or support? There are several ways to get in touch with us:

* [Website](https://cloudogu.com)
* [myCloudogu-Forum](https://forum.cloudogu.com/topic/34?ctx=1)
* [Email hello@cloudogu.com](mailto:hello@cloudogu.com)

---
&copy; 2020 Cloudogu GmbH - MADE WITH :heart:&nbsp;FOR DEV ADDICTS. [Legal notice / Impressum](https://cloudogu.com/imprint.html)
