Requisites:
- Binary repository for GD JBehave Framework  
  https://nexus.griddynamics.net/nexus/content/repositories/gd_jbehave_framework-snapshots  
  should be added to Maven repositories in `settings.xml`.
  It requires authentication so it should be configured properly.

Run UI test against a remote WebDriver:

    mvn -P runTests clean test -Dthreads=1 \
        -Dsuite.all=**/*Suite.java -Dsuite.list=UITestsExampleSuite \
        -Dspring.profiles.active=remote \
        -Dmeta.filters=-not_impl,-not_in_func,-blocked,-non_ci \
        -DREMOTE_WEBDRIVER_URL=http://localhost:4444/wd/hub -Dbrowser.version= \
        -Dpetclinic.url=http://localhost:9966/petclinic

Run web service tests:

    mvn -P runTests clean test -Dthreads=1 \
        -Dsuite.all=**/*Suite.java -Dsuite.list=WebServicesExampleSuite \
        -Dmeta.filters=-not_impl,-not_in_func,-blocked,-non_ci \
        -Dpetclinic.url=http://localhost:9966/petclinic

(`suite.list` property is `UITestsExampleSuite` for Selenium tests and
`WebServicesExampleSuite` - for direct tests of web service API)
