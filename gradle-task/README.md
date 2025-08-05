# Gradle Practical Task

This practical task is a part of the `Module 9: Build tools: Gradle` of the `Internship: DevOps` course.

Each task has it's own subfolder (named according to the task number) with a README file. Also check the commit history for file modifications.

This project is forked from: https://github.com/spring-projects/spring-petclinic. 

## Tasks

1. Install required software locally (latest version of Gradle, Java).
2. Go to https://github.com/spring-projects/spring-petclinic, fork it, and clone the forked repo.
3. Check what tasks are available for the project.
4. Run all available project tests.
5. Build the project and run it, and verify it's available on the localhost in the browser.
6. Perform the cleanup.
7. Explore gradle-related files. Identify which gradle file defines the project name and change it. Build the project again and verify the new project name is being used (you need to find jar files).
8. Create an additional custom task, which depends on the build task (so by calling this task the build and test tasks will also be executed). It should open the browser with generated test results.
9. Add the dynamic versioning to your project using the commonly used axion-release-plugin [(GitHub - allegro/axion-release-plugin: Gradle release & version management plugin.)](https://www.google.com/url?q=https://github.com/allegro/axion-release-plugin&sa=D&source=editors&ust=1754045805900124&usg=AOvVaw0zvGRqmK2NI9nUpkgs4vt5). Check the [Version your project (Gradle best practice tip #5)](https://www.google.com/url?q=https://www.youtube.com/watch?v%3DrkR14unfBmE&sa=D&source=editors&ust=1754045805900400&usg=AOvVaw2xlPY-mkrUFXqb8filaU-N) to find the tutorial on how to do this.
10. Check the new available commands after the plugin addition. Using these new commands, check the current project version. Then add and commit some changes to the project and make the project release. Check the current version one more time and git tags available. Note the difference between SNAPSHOT and release versions.
11. Perform the cleanup.

- Hint: you may need the following documentation while working on the practice task:
  - [Authoring Tasks](https://www.google.com/url?q=https://docs.gradle.org/current/userguide/more_about_tasks.html%23sec:adding_dependencies_to_tasks&sa=D&source=editors&ust=1754045805901642&usg=AOvVaw3mCoulFSSEYIOpxtPsdJkU)
  - [The Java Plugin](https://www.google.com/url?q=https://docs.gradle.org/current/userguide/java_plugin.html%23sec:java_project_layout&sa=D&source=editors&ust=1754045805901892&usg=AOvVaw1s4zG2rH4AkUee4ePXAl7j)
  - [Version your project (Gradle best practice tip #5)](https://www.google.com/url?q=https://www.youtube.com/watch?v%3DrkR14unfBmE&sa=D&source=editors&ust=1754045805902167&usg=AOvVaw0h9MRDSTnF7BbY8069TzrJ)
  - [Gradle task to open a url in the default browser - Stack Overflow](https://www.google.com/url?q=https://stackoverflow.com/questions/14847296/gradle-task-to-open-a-url-in-the-default-browser&sa=D&source=editors&ust=1754045805902452&usg=AOvVaw0dXiQ_OHAv8GlSCtnkfWx_)