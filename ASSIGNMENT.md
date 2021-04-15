# QA Assignment

## Running tests
You can run the Cucumber features from your IDE or via the command line:
```shell
./mvnw -P itest integration-test
```

## Finding owners
The scenarios for this feature are incomplete or even naive. Please cover more cases in more reliable way.
Open the `src/test/resources/scenarios/FindOwners.feature` and follow instructions given in the comments.

## Adding owners
Open `src/test/resources/scenarios/AddOwners.feature` and follow instructions given in the comments.

## Bonus tasks
1. The browser is not closing after all tests were run. Fix it.
2. There is something wrong with the way the HTML elements are selected.
   Prepare a suggestion of how to improve it or even implement such a change.
