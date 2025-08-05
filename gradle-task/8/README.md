# 8. Create an additional custom task, which depends on the build task (so by calling this task the build and test tasks will also be executed). It should open the browser with generated test results.

1. Open `build.gradle` file
2. Add the following lines:
    ![Screenshot](./1.png)
3. Run `./gradlew testResults`
    ![Screenshot](./2.png)