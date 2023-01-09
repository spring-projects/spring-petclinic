<# Description: The series of commands I used in the Azure Cloud Shell to deploy Azure App Service for 
spring-petclinic. For reference to recreate a resource group and deploy spring-petclinic in the future if
the existing resource group gets removed. You can't execute this ps1 file due to multiple user inputs needed
during the Azure Maven plugin config step and JDK 17 installation step.
Source: https://learn.microsoft.com/en-us/azure/app-service/quickstart-java?tabs=javase&pivots=platform-linux-development-environment-maven
#>

git clone https://github.com/kevdev424/spring-petclinic.git

cd spring-petclinic

<#
Azure Maven plugin config step:
When prompted with Subscription option, select the proper Subscription by entering the number printed at the line start.
When prompted with Web App option, select the default option, <create>, by pressing enter.
When prompted with OS option, select Linux by pressing enter.
When prompted with javaVersion option, select Java 17.
When prompted with Pricing Tier option, select B2 for Java dev/test workload (which is what I chose) 
or P1v2 for production workload. 
Finally, press enter on the last prompt to confirm your selections.
#>
mvn com.microsoft.azure:azure-webapp-maven-plugin:2.5.0:config

# Cloud shell uses Java 11 but Java 17 is needed to build and deploy spring-petclinic
# Downlaod and unzip JDK 17 and set JAVA_HOME to the unzipped directory.
cd ~
wget https://aka.ms/download-jdk/microsoft-jdk-17.0.5-linux-x64.tar.gz
tar zxvf microsoft-jdk-17.0.5-linux-x64.tar.gz
export JAVA_HOME=/home/<current user>/jdk-17.0.5+8

# Deploy the app to azure
cd spring-petclinic
mvn package azure-webapp:deploy

