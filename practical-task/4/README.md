# 4. Run all available project tests.

1. Run `./gradlew test`
    ![Screenshot](./3.png)
    * An error might occur:
        ![Screenshot](./1.png)
        1. Set the JAVA_HOME environment variable to point to the installation directory of your JDK.
        2. Check the version in build.gradle (or build.gradle.kts) and set it to the version you have installed:
        ![Screenshot](./2.png)
