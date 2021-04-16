# QA Assignment

## How to start?
1. Follow the instructions in the [readme](readme.md) file.
2. Switch the branch to `cucumber`.
3. Create a pull request with your changes.

## Running tests
You can run the Cucumber features from your IDE or via the command line:
```shell
./mvnw -P itest integration-test
```

## Assignment

### Finding owners
The scenarios for this feature are incomplete or even naive. Please cover more cases in more reliable way.
Open the `src/test/resources/scenarios/FindOwners.feature` and follow instructions given in the comments.

### Adding owners
Open `src/test/resources/scenarios/AddOwners.feature` and follow instructions given in the comments.

### Bonus tasks
1. The browser doesn't close after all tests. Please fix it.
2. There is something wrong with the way the HTML elements are selected.
   Prepare a suggestion of how to improve it or even implement such a change.
3. Suggest a replacement for Selenium.
