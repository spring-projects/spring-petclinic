# OpenTelemetry Sample: Java SpringBoot based APIs instrumented with OTEL and Digma

The following code is a sample application used to test various scenarios, performance bottlenecks and runtime errors with [Digma](https://github.com/digma-ai/digma). This sample uses the standard OTEL instrumentation library as well as the [Digma instrumentation](https://github.com/digma-ai/otel-java-instrumentation/blob/main/instrumentation/spring/spring-boot-autoconfigure/) helper library. 

We took the original application of [PetClinic](https://github.com/spring-projects/spring-petclinic) and modified it a bit to support instrumentation of OpenTelemetry and Digma.

## Prerequisites

- [Java 11+](https://www.oracle.com/sa/java/technologies/javase/jdk11-archive-downloads.html)
- [IntelliJ IDEA 2022.2+](https://www.jetbrains.com/idea/download)

## Running the app with Digma

### Install the IDE extension

1. Install the [Digma Plugin](https://plugins.jetbrains.com/plugin/19470-digma-continuous-feedback) into the Intellij IDE
2. Launch Intellij
3. Open 'Settings' (shortcut <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>) and search for setting of 'Digma Plugin' (under Tools) 

![image](https://user-images.githubusercontent.com/104715391/203003539-3d450c45-9811-4fc0-9188-d2fda3d4f18c.png)

4. Set the 'Digma Api Url' to be https://localhost:5051

### Build the application and run it

After cloning this project to your machine, the following command will build and run the application, please run it:

```shell
./gradlew bootRun
```

### Use the application automatically with ClientTester

Run the following command in order to run ClientTester, which will access the application in various ways

```shell
./gradlew runClientTester
```

### Use the application Manually

Browse to [Local PetClinic](http://localhost:9876/) and use the application freely.

![image](https://user-images.githubusercontent.com/104715391/203006282-b1db606f-1e92-46cd-8a62-40a96d80d7d6.png)

We suggest to mainly focusing on 'Find Owners' and beneath use the Add/Edit Owner, Add/Edit Pet.
Also, in order to see errors in our plugin also click on 'ERROR', 'APPERROR1' and 'APPERROR2' 

### View Digma Insights via Plugin

Within few minutes Digma should have collect the traces and analyze them already.
Meanwhile you can open the UI of Digma Plugin, by clicking on 'Digma' at the left-bottom corner in the Intellij

![image](https://user-images.githubusercontent.com/104715391/203008076-9c8aac11-e499-4a2d-a003-d33ada281fde.png)

Now you can browse to class (shortcut <kbd>Ctrl</kbd> + <kbd>N</kbd>) `OwnerController` and you should see something like that:

![image](https://user-images.githubusercontent.com/104715391/203009185-408f35c0-b7f8-4257-9144-baf0a624a22c.png)

where LOCAL is your local environment, and the list contains linkable methods within this class.

as an example for Insights you can click on `findOwner` method and you should see something like that:

![image](https://user-images.githubusercontent.com/104715391/203009907-248b01b5-b054-4708-b457-753ef9f416fa.png)
