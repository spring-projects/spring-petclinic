Final Project
Due Friday by 8:59pm Points 100 Submitting a file upload Available until Jul 26 at 8:59pm
Goal

Build a DevSecOps pipeline for the Links to an external site.spring-petclinic projectLinks to an external site. using Docker, incorporating security, continuous integration, continuous delivery, and monitoring. Utilize Jenkins for continuous integration, SonarQube for static analysis, Prometheus and Grafana for monitoring, and OWASP ZAP for security analysis. The pipeline should be containerized; however, use Ansible to deploy the application to a production web server, which should be a Virtual Machine (VM).

Instructions

    Use Docker to set up containers for Jenkins, SonarQube, Prometheus, Grafana, and OWASP ZAP.
    Fork the project repository on GitHub/GitLab and clone it to your local machine.
    Create a custom Docker network to connect all the services.
    Run Jenkins in a Docker container connected to the custom network.
    Run SonarQube in a Docker container connected to the custom network.
    Run Prometheus in a Docker container connected to the custom network.
    Run Grafana in a Docker container connected to the custom network.
    Run OWASP ZAP in a Docker container connected to the custom network.
    Create a Jenkins pipeline that uses the forked GitHub repository.
    Set up build triggers to poll Source Control Management (SCM).
    Create and invoke build steps for the spring-petclinic project.
    Configure SonarQube to perform static analysis for the project. Use the Blue Ocean plugin to visualize the build process.
    Execute OWASP ZAP with appropriate configuration.
    Add post-build actions to publish HTML reports for OWASP ZAP.
    Install the Prometheus plugin in Jenkins.
    Configure the Prometheus plugin to monitor Jenkins.
    Configure Grafana to use Prometheus as a data source and create dashboards to visualize Jenkins metrics.
    Set up a Virtual Machine (VM) to act as the production web server.
    Use Ansible on the Jenkins build server to deploy the spring-petclinic application to the production web server (VM).
    Ensure the application is deployed and running on the production web server (VM) by showing the welcome screen.
    Make and push a code change to the GitHub repository.
    Verify Jenkins automatically builds, tests, and deploys the new version, and the content change is reflected in the deployed application.


Deliverables (Grading - 60 points)

    Step-by-Step Instructions: Provide detailed documentation outlining the steps to set up the environment and configure the DevSecOps pipeline. (Grading - 30 points)
    Provisioning Scripts and Configuration Files: Submit the Docker commands and scripts used to set up Jenkins, Prometheus, Grafana, OWASP ZAP, and Ansible playbook (e.g., Dockerfiles, Vagrant files, Groovy scripts, XMLs, INIs, etc.). (Grading - 30 points)


Screenshots (Grading - 20 points)

    Include a screenshot of the spring-petclinic welcome screen on the production web server.
    Include screenshots of key Jenkins, SonarQube, Prometheus, Grafana, and OWASP ZAP screens.
    Provide evidence showing that a code change triggers the pipeline, deployment is done, and the content of the application is automatically updated. Show the deployed version is different from before the code was committed.


Pipeline Demonstration (Grading - 20 points)

    Provide a short video demonstrating the automated build, deployment, and monitoring process in Jenkins, Prometheus, and Grafana. (Link for the video in Project Documents)
    Advanced automated scripting (i.e., fully automated assignment completion) will receive a bonus. (Grading - 15 points)
