# Modified Spring Pet Clinic for testing NR's CLM

## Getting the app running with docker

```
git clone https://github.com/meiao/spring-petclinic.git
cd spring-petclinic
NEW_RELIC_LICENSE_KEY=12345 docker-compose up -d --build
```

Agent, tester, and app logs can be found mounted in ./logs

The docker app will generate some traffic that should be visible in NR1 under "CLM Test App"

## Getting the code and running without docker

```
git clone https://github.com/meiao/spring-petclinic.git
cd spring-petclinic
NEW_RELIC_LICENSE_KEY=12345 ./gradlew bootRun
```
Substitute 12345 with your New Relic license key.

You can then access petclinic here: http://localhost:8080/

The main page will have some links that exercise auto and manual instrumentation in different modes.

The respective code is in ClmController.java.

The top menu will take you to the regular Spring Pet Clinic application.

Requires Java 11 or higher.
